package com.example.ecommerceapp.data.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;
// import java.util.Date; // Keep if API returns Date, or use String and parse

public class Order {

    @SerializedName("orderId") // From POST /orders response example
    private String orderId;

    @SerializedName("userId") // Assuming API might return this, or it's known client-side
    private String userId;

    @SerializedName("createdAt") // From POST /orders response example (e.g., "2025-07-03T16:45:00Z")
    private String createdAt; // Store as String, parse to Date for display if needed

    @SerializedName("items") // For GET /orders/{orderId}
    private List<OrderItem> items; // Uses OrderItem model (to be updated)

    @SerializedName("totalAmount") // From POST /orders response example
    private double totalAmount;

    @SerializedName("status") // From POST /orders response example
    private String status;

    // Detailed Order (from GET /orders/{orderId}) might include these:
    // These fields are based on the old Order model and common order details.
    // Adjust them if the GET /orders/{orderId} API response structure is different.

    @SerializedName("shippingAddress") // Assuming this might be a nested object or just an ID string
    private ShippingAddress shippingAddress; // Placeholder for a ShippingAddress model or String ID

    @SerializedName("paymentDetails") // Assuming this might be a nested object or just an ID string
    private PaymentDetails paymentDetails; // Placeholder for PaymentDetails model or String ID

    // The old model had individual shipping fields. If API provides a nested shippingAddress object:
    // Example ShippingAddress class (create if needed):
    // public static class ShippingAddress {
    //     @SerializedName("fullName") public String fullName;
    //     @SerializedName("addressLine1") public String addressLine1;
    //     @SerializedName("addressLine2") public String addressLine2;
    //     @SerializedName("city") public String city;
    //     @SerializedName("state") public String state;
    //     @SerializedName("postalCode") public String postalCode;
    //     @SerializedName("country") public String country;
    //     @SerializedName("phoneNumber") public String phoneNumber;
    // }
    // Example PaymentDetails class (create if needed):
    // public static class PaymentDetails {
    //     @SerializedName("paymentMethod") public String paymentMethod; // e.g., "Credit Card", "PayPal"
    //     @SerializedName("paymentStatus") public String paymentStatus; // e.g., "Paid", "Pending"
    //     @SerializedName("transactionId") public String transactionId;
    // }


    // Default constructor for Gson
    public Order() {}

    // Getters
    public String getOrderId() { return orderId; }
    public String getUserId() { return userId; }
    public String getCreatedAt() { return createdAt; } // Returns String, parse for display
    public List<OrderItem> getItems() { return items; }
    public double getTotalAmount() { return totalAmount; }
    public String getStatus() { return status; }
    public ShippingAddress getShippingAddress() { return shippingAddress; }
    public PaymentDetails getPaymentDetails() { return paymentDetails; }

    // Setters (mainly for Gson)
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public void setUserId(String userId) { this.userId = userId; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public void setItems(List<OrderItem> items) { this.items = items; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
    public void setStatus(String status) { this.status = status; }
    public void setShippingAddress(ShippingAddress shippingAddress) { this.shippingAddress = shippingAddress; }
    public void setPaymentDetails(PaymentDetails paymentDetails) { this.paymentDetails = paymentDetails; }


    // --- Placeholder inner classes for ShippingAddress and PaymentDetails ---
    // These should be moved to their own files if they become complex or are used elsewhere.
    // Define fields based on what GET /orders/{orderId} actually returns.
    public static class ShippingAddress {
        @SerializedName("id") // If API refers to a saved address by ID
        public String id;
        @SerializedName("fullName")
        public String fullName;
        @SerializedName("addressLine1")
        public String addressLine1;
        @SerializedName("addressLine2")
        public String addressLine2;
        @SerializedName("city")
        public String city;
        @SerializedName("state")
        public String state;
        @SerializedName("postalCode")
        public String postalCode;
        @SerializedName("country")
        public String country;
        @SerializedName("phoneNumber")
        public String phoneNumber;
        // Add constructor, getters, setters if needed
    }

    public static class PaymentDetails {
        @SerializedName("id") // If API refers to a saved payment method by ID
        public String id;
        @SerializedName("paymentMethodType") // e.g., "credit_card", "paypal"
        public String paymentMethodType;
        @SerializedName("paymentStatus")
        public String paymentStatus;
        @SerializedName("transactionId")
        public String transactionId; // From payment gateway after processing
        @SerializedName("last4Digits") // Example for card
        public String last4Digits;
        // Add constructor, getters, setters if needed
    }
    // --- End placeholder inner classes ---

}
