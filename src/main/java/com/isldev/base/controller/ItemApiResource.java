package com.isldev.base.controller;

import com.isldev.base.entity.Item;
import com.isldev.base.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.util.List;

/**
 * @author YISivlay
 */
@RestController
@RequestMapping("/items")
public class ItemApiResource {

    private final ItemRepository repository;

    @Autowired
    public ItemApiResource(ItemRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public Item save(@RequestBody Item item) {
        return this.repository.save(item);
    }

    @PutMapping("/{id}")
    public Item update(@PathVariable Long id, @RequestBody Item item) {
        Item i = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found with ID: " + id));

        i.setDescription(item.getDescription());
        i.setName(item.getName());
        i.setPrice(item.getPrice());
        i.setStock(item.getStock());

        return this.repository.save(item);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        Item item = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found with ID: " + id));
        this.repository.delete(item);
    }

    @GetMapping
    public List<Item> getAll() {
        return this.repository.findAll();
    }

    @GetMapping("/{id}")
    public Item getById(@PathVariable Long id) {
        return this.repository.findById(id).orElse(null);
    }

    @PostMapping("/{id}/upload-image")
    public String uploadImage(@PathVariable Long id, @RequestParam("image") MultipartFile imageFile) {
        Item item = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found with ID: " + id));
        try {
            String base64Image = Base64.getEncoder().encodeToString(imageFile.getBytes());
            item.setImage(base64Image);
            repository.save(item);
            return "Image uploaded successfully";
        } catch (Exception e) {
            return "Failed to upload image: " + e.getMessage();
        }
    }
}
