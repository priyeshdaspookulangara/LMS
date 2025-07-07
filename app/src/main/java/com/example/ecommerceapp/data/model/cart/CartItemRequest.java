package com.example.ecommerceapp.data.model.cart;

import com.google.gson.annotations.SerializedName;

public class CartItemRequest {
    @SerializedName("productId")
    private String productId;

    @SerializedName("quantity")
    private int quantity;

    public CartItemRequest(String productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    // Getters (optional, public fields are fine with Gson for simple DTOs)
    public String getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }
}
