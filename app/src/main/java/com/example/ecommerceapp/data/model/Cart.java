package com.example.ecommerceapp.data.model;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private List<CartItem> items;
    private double totalPrice;
    private int totalItems; // Total number of unique products, or sum of quantities

    public Cart() {
        this.items = new ArrayList<>();
        this.totalPrice = 0.0;
        this.totalItems = 0;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
        calculateTotals();
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public int getTotalItemCount() { // This is sum of quantities of all items
        return totalItems;
    }

    public int getUniqueProductCount() { // This is the number of distinct products
        return items.size();
    }

    public void addItem(Product product, int quantity) {
        if (product == null || quantity <= 0) {
            return; // Or throw an exception
        }

        for (CartItem item : items) {
            if (item.getProduct().getProductId().equals(product.getProductId())) {
                // Product already in cart, update quantity
                item.setQuantity(item.getQuantity() + quantity);
                calculateTotals();
                return;
            }
        }
        // Product not in cart, add new CartItem
        items.add(new CartItem(product, quantity));
        calculateTotals();
    }

    public void updateItemQuantity(String productId, int newQuantity) {
        if (newQuantity <= 0) {
            removeItem(productId);
            return;
        }
        for (CartItem item : items) {
            if (item.getProduct().getProductId().equals(productId)) {
                item.setQuantity(newQuantity);
                calculateTotals();
                return;
            }
        }
    }

    public void removeItem(String productId) {
        items.removeIf(item -> item.getProduct().getProductId().equals(productId));
        calculateTotals();
    }

    public void clearCart() {
        items.clear();
        calculateTotals();
    }

    private void calculateTotals() {
        totalPrice = 0.0;
        totalItems = 0;
        for (CartItem item : items) {
            totalPrice += item.getSubtotal();
            totalItems += item.getQuantity();
        }
    }

    @Override
    public String toString() {
        return "Cart{" +
                "itemsCount=" + items.size() +
                ", totalQuantity=" + totalItems +
                ", totalPrice=" + totalPrice +
                '}';
    }
}
