package com.example.ecommerceapp.data.model;

import com.google.gson.annotations.SerializedName;
import java.util.Objects;

public class CartItem {

    @SerializedName("productId")
    private String productId;

    @SerializedName("name") // Name of the product, provided by cart API
    private String name;

    @SerializedName("quantity")
    private int quantity;

    @SerializedName("price") // Price per unit at the time it was added or current price from API
    private double price;

    // Client-side helper: full Product object, might be null or partially populated
    // This won't be directly deserialized from the cart item list in API response,
    // but can be populated by the app if needed.
    private transient Product productDetails;


    // Default constructor for Gson
    public CartItem() {}

    // Constructor for API data
    public CartItem(String productId, String name, int quantity, double price) {
        this.productId = productId;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    // Getters
    public String getProductId() { return productId; }
    public String getName() { return name; }
    public int getQuantity() { return quantity; }
    public double getPrice() { return price; }
    public Product getProductDetails() { return productDetails; }


    // Setters
    public void setProductId(String productId) { this.productId = productId; }
    public void setName(String name) { this.name = name; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setPrice(double price) { this.price = price; }
    public void setProductDetails(Product productDetails) { this.productDetails = productDetails; }


    public double getSubtotal() {
        return price * quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartItem cartItem = (CartItem) o;
        // A cart item is unique by its productId within a cart
        return Objects.equals(productId, cartItem.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId);
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "productId='" + productId + '\'' +
                ", name='" + name + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                '}';
    }
}
