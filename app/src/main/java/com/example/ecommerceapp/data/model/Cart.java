package com.example.ecommerceapp.data.model;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

public class Cart {

    @SerializedName("cartId")
    private String cartId;

    @SerializedName("userId")
    private String userId;

    @SerializedName("items")
    private List<CartItem> items; // Uses the updated CartItem model

    @SerializedName("totalAmount")
    private double totalAmount;

    // Client-side calculated fields, not directly from this specific API response structure,
    // but useful if Cart model is also used for local manipulation before API sync.
    // However, for direct API mapping, these might be redundant if totalAmount is authoritative.
    // private int totalItemsQuantity; // Sum of quantities of all items

    // Default constructor for Gson
    public Cart() {
        this.items = new ArrayList<>(); // Initialize to avoid null pointer if API returns empty cart without items array
    }

    public Cart(String cartId, String userId, List<CartItem> items, double totalAmount) {
        this.cartId = cartId;
        this.userId = userId;
        this.items = items;
        this.totalAmount = totalAmount;
    }

    // Getters
    public String getCartId() { return cartId; }
    public String getUserId() { return userId; }
    public List<CartItem> getItems() {
        if (items == null) { // Defensive coding
            items = new ArrayList<>();
        }
        return items;
    }
    public double getTotalAmount() { return totalAmount; }

    // Setters (mainly for Gson, or if client needs to modify)
    public void setCartId(String cartId) { this.cartId = cartId; }
    public void setUserId(String userId) { this.userId = userId; }
    public void setItems(List<CartItem> items) { this.items = items; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }


    // Client-side helper methods (these were from the old Cart model, may or may not be needed
    // if all cart logic is now server-driven and client just displays API response)

    public int getTotalItemCount() { // Sum of quantities of all items
        if (items == null) return 0;
        int count = 0;
        for (CartItem item : items) {
            count += item.getQuantity();
        }
        return count;
    }

    public int getUniqueProductCount() { // Number of distinct line items
        return items != null ? items.size() : 0;
    }

    // The following methods (addItem, updateItemQuantity, removeItem, clearCart, calculateTotals)
    // were for client-side manipulation of a local cart.
    // With an API-driven cart, these operations will be handled by making API calls
    // (e.g., POST /cart/items, PUT /cart/items/{id}, etc.) in CartRepository.
    // The Cart model itself primarily becomes a DTO for API responses.
    // So, these local manipulation methods might be removed or adapted if there's a
    // temporary client-side cart state before syncing with the API.
    // For now, I will comment them out to emphasize the shift to API-driven state.

    /*
    public void addItem(Product product, int quantity) {
        // ... old logic ...
    }

    public void updateItemQuantity(String productId, int newQuantity) {
        // ... old logic ...
    }

    public void removeItem(String productId) {
        // ... old logic ...
    }

    public void clearCart() {
        // ... old logic ...
    }

    private void calculateTotals() {
        // ... old logic, now totalAmount comes from API ...
    }
    */

    @Override
    public String toString() {
        return "Cart{" +
                "cartId='" + cartId + '\'' +
                ", userId='" + userId + '\'' +
                ", itemCount=" + getUniqueProductCount() +
                ", totalQuantity=" + getTotalItemCount() +
                ", totalAmount=" + totalAmount +
                '}';
    }
}
