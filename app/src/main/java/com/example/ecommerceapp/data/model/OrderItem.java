package com.example.ecommerceapp.data.model;

import com.google.gson.annotations.SerializedName;

public class OrderItem {

    @SerializedName("orderItemId") // Or just "id" if API uses that for line item ID
    private String orderItemId;

    @SerializedName("productId")
    private String productId;

    @SerializedName("productName") // Denormalized for easier display
    private String productName;

    @SerializedName("quantity")
    private int quantity;

    @SerializedName("price") // Assuming API calls it "price" for price per unit at time of order
    private double pricePerUnit;

    @SerializedName("subtotal") // Often calculated, but API might return it
    private double subtotal;

    // Default constructor for Gson
    public OrderItem() {}

    // Constructor for manual creation or testing
    public OrderItem(String orderItemId, String productId, String productName, int quantity, double pricePerUnit) {
        this.orderItemId = orderItemId;
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.pricePerUnit = pricePerUnit;
        this.subtotal = quantity * pricePerUnit; // Calculate subtotal
    }

    // Constructor from CartItem (client-side before sending to API, or if API needs it)
    // This might not be needed if OrderItems are only ever received from API.
    // The previous version was used for local mock data.
    // For API integration, usually the server constructs OrderItems from Cart.
    /*
    public OrderItem(CartItem cartItem) {
        this.productId = cartItem.getProductId(); // Using updated CartItem
        this.productName = cartItem.getName();
        this.quantity = cartItem.getQuantity();
        this.pricePerUnit = cartItem.getPrice();
        this.subtotal = cartItem.getSubtotal();
        this.orderItemId = "client_item_" + this.productId + "_" + System.nanoTime();
    }
    */

    // Getters
    public String getOrderItemId() { return orderItemId; }
    public String getProductId() { return productId; }
    public String getProductName() { return productName; }
    public int getQuantity() { return quantity; }
    public double getPricePerUnit() { return pricePerUnit; }
    public double getSubtotal() {
        // If API doesn't provide subtotal, calculate it. If it does, this getter is fine.
        // Or ensure it's calculated in constructor/setter if not provided by API.
        return pricePerUnit * quantity;
    }

    // Setters (mainly for Gson)
    public void setOrderItemId(String orderItemId) { this.orderItemId = orderItemId; }
    public void setProductId(String productId) { this.productId = productId; }
    public void setProductName(String productName) { this.productName = productName; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setPricePerUnit(double pricePerUnit) { this.pricePerUnit = pricePerUnit; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; } // If API provides it
}
