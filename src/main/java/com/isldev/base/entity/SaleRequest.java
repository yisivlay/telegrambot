package com.isldev.base.entity;

import java.util.List;

/**
 * @author YISivlay
 */
public class SaleRequest {

    private List<SaleItemRequest> items;
    private String chatId;


    public static class SaleItemRequest {
        private Long itemId;
        private int qty;

        public Long getItemId() {
            return itemId;
        }

        public void setItemId(Long itemId) {
            this.itemId = itemId;
        }

        public int getQty() {
            return qty;
        }

        public void setQty(int qty) {
            this.qty = qty;
        }
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public List<SaleItemRequest> getItems() {
        return items;
    }

    public void setItems(List<SaleItemRequest> items) {
        this.items = items;
    }
}
