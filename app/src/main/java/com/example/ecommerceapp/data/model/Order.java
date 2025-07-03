package com.example.ecommerceapp.data.model;

import java.util.List;
import java.util.Date;

public class Order {
    private String orderId;
    private String userId; // From User model
    private Date orderDate;
    private List<OrderItem> items;
    private double totalAmount;
    private String status; // e.g., "Pending", "Processing", "Shipped", "Delivered", "Cancelled"

    // Shipping Information
    private String shippingFullName;
    private String shippingAddressLine1;
    private String shippingAddressLine2;
    private String shippingCity;
    private String shippingState;
    private String shippingPostalCode;
    private String shippingCountry;
    private String shippingPhoneNumber;

    // Payment Information (Simulated)
    private String paymentMethod; // e.g., "Credit Card", "PayPal"
    private String paymentStatus; // e.g., "Pending", "Paid", "Failed"
    private String transactionId; // From payment gateway

    public Order(String orderId, String userId, Date orderDate, List<OrderItem> items,
                 double totalAmount, String status, String shippingFullName, String shippingAddressLine1,
                 String shippingAddressLine2, String shippingCity, String shippingState,
                 String shippingPostalCode, String shippingCountry, String shippingPhoneNumber,
                 String paymentMethod, String paymentStatus, String transactionId) {
        this.orderId = orderId;
        this.userId = userId;
        this.orderDate = orderDate;
        this.items = items;
        this.totalAmount = totalAmount;
        this.status = status;
        this.shippingFullName = shippingFullName;
        this.shippingAddressLine1 = shippingAddressLine1;
        this.shippingAddressLine2 = shippingAddressLine2;
        this.shippingCity = shippingCity;
        this.shippingState = shippingState;
        this.shippingPostalCode = shippingPostalCode;
        this.shippingCountry = shippingCountry;
        this.shippingPhoneNumber = shippingPhoneNumber;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.transactionId = transactionId;
    }

    // Minimal constructor for building up the order
    public Order(String userId) {
        this.userId = userId;
        this.orderDate = new Date(); // Set current date/time
        this.status = "Pending"; // Default status
        this.paymentStatus = "Pending";
    }


    // Getters
    public String getOrderId() { return orderId; }
    public String getUserId() { return userId; }
    public Date getOrderDate() { return orderDate; }
    public List<OrderItem> getItems() { return items; }
    public double getTotalAmount() { return totalAmount; }
    public String getStatus() { return status; }
    public String getShippingFullName() { return shippingFullName; }
    public String getShippingAddressLine1() { return shippingAddressLine1; }
    public String getShippingAddressLine2() { return shippingAddressLine2; }
    public String getShippingCity() { return shippingCity; }
    public String getShippingState() { return shippingState; }
    public String getShippingPostalCode() { return shippingPostalCode; }
    public String getShippingCountry() { return shippingCountry; }
    public String getShippingPhoneNumber() { return shippingPhoneNumber; }
    public String getPaymentMethod() { return paymentMethod; }
    public String getPaymentStatus() { return paymentStatus; }
    public String getTransactionId() { return transactionId; }

    // Setters (needed for building the order through checkout steps)
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public void setOrderDate(Date orderDate) { this.orderDate = orderDate; }
    public void setItems(List<OrderItem> items) { this.items = items; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
    public void setStatus(String status) { this.status = status; }
    public void setShippingFullName(String shippingFullName) { this.shippingFullName = shippingFullName; }
    public void setShippingAddressLine1(String shippingAddressLine1) { this.shippingAddressLine1 = shippingAddressLine1; }
    public void setShippingAddressLine2(String shippingAddressLine2) { this.shippingAddressLine2 = shippingAddressLine2; }
    public void setShippingCity(String shippingCity) { this.shippingCity = shippingCity; }
    public void setShippingState(String shippingState) { this.shippingState = shippingState; }
    public void setShippingPostalCode(String shippingPostalCode) { this.shippingPostalCode = shippingPostalCode; }
    public void setShippingCountry(String shippingCountry) { this.shippingCountry = shippingCountry; }
    public void setShippingPhoneNumber(String shippingPhoneNumber) { this.shippingPhoneNumber = shippingPhoneNumber; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
}
