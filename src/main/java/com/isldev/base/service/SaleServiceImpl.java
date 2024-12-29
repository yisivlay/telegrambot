package com.isldev.base.service;

import com.isldev.base.config.TelegramBot;
import com.isldev.base.entity.Item;
import com.isldev.base.entity.Sale;
import com.isldev.base.entity.SaleRequest;
import com.isldev.base.entity.User;
import com.isldev.base.repository.ItemRepository;
import com.isldev.base.repository.SaleRepository;
import com.isldev.base.repository.UserRepository;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author YISivlay
 */
@Service
public class SaleServiceImpl implements SaleService {

    private final ItemRepository itemRepository;
    private final SaleRepository saleRepository;
    private final TelegramBot telegramBot;
    private final TelegramBotService telegramBotService;
    private final UserRepository userRepository;

    @Autowired
    public SaleServiceImpl(ItemRepository itemRepository, SaleRepository saleRepository, TelegramBot telegramBot, TelegramBotService telegramBotService, UserRepository userRepository) {
        this.itemRepository = itemRepository;
        this.saleRepository = saleRepository;
        this.telegramBot = telegramBot;
        this.telegramBotService = telegramBotService;
        this.userRepository = userRepository;
    }

    @Override
    public String sellItem(SaleRequest request) {
        Optional<User> user = Optional.of(this.userRepository.getReferenceById(1L));
        String chatId = user.get().getChatId();

        // Process the sale
        List<SaleRequest.SaleItemRequest> saleItems = request.getItems();
        double totalAmount = 0;
        int totalQuantity = 0;

        Sale sale = new Sale();
        sale.setSaleDate(LocalDateTime.now());

        // List to hold all Item objects for the sale
        List<Item> itemsInSale = new ArrayList<>();

        // Iterate through each item in the sale request
        for (SaleRequest.SaleItemRequest itemRequest : saleItems) {
            Item item = itemRepository.findById(itemRequest.getItemId())
                    .orElseThrow(() -> new RuntimeException("Item not found"));

            // Check if there is sufficient stock for the item
            if (item.getStock() < itemRequest.getQty()) {
                throw new RuntimeException("Insufficient stock for item: " + item.getName());
            }

            // Update item stock and save it
            item.setStock(item.getStock() - itemRequest.getQty());
            itemRepository.save(item);

            // Add item to sale
            itemsInSale.add(item);

            // Update totalAmount and totalQuantity
            totalAmount += item.getPrice() * itemRequest.getQty();
            totalQuantity += itemRequest.getQty();
        }

        // Set the list of items in the sale and other sale details
        sale.setItems(itemsInSale);
        sale.setTotalAmount(totalAmount);
        sale.setQty(totalQuantity);

        // Save the sale entity
        sale = saleRepository.save(sale);

        // Generate the invoice PDF
        try {
            byte[] invoice = generateInvoicePdf(sale);
            // Send the invoice to the Telegram bot
            this.telegramBotService.sendInvoiceToTelegram(invoice, chatId);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Construct the message to send to Telegram
        String message = "Sale completed successfully!\n" +
                "Total Items Sold: " + totalQuantity + "\n" +
                "Total Amount: $" + totalAmount + "\n" +
                "Sale ID: " + sale.getId();

        // Send a Telegram notification
        telegramBot.sendMessage(chatId, message);

        return "Sale completed and notification sent!";
    }

    public byte[] generateInvoicePdf(Sale sale) throws IOException {
        // Create a new PDF document
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(outputStream);
        PdfDocument pdfDocument = new PdfDocument(writer);
        Document document = new Document(pdfDocument);

        // Load a standard font (Helvetica)
        com.itextpdf.kernel.font.PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);

        // Add content to the document
        document.add(new Paragraph("Invoice").setFont(font).setFontSize(16));

        // Sale information
        document.add(new Paragraph("Sale ID: " + sale.getId()));
        document.add(new Paragraph("Sale Date: " + sale.getSaleDate()));
        document.add(new Paragraph("Total Amount: $" + String.format("%.2f", sale.getTotalAmount())));
        document.add(new Paragraph("Total Items Sold: " + sale.getQty()));
        document.add(new Paragraph(" ")); // Blank line

        // Add some space at the bottom
        document.add(new Paragraph(" "));
        document.add(new Paragraph("Thank you for your purchase!"));

        // Close the document
        document.close();

        // Return the PDF as byte array
        return outputStream.toByteArray();
    }
}
