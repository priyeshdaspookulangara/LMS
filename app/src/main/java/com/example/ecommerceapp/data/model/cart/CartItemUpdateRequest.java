package com.example.ecommerceapp.data.model.cart;

import com.google.gson.annotations.SerializedName;

public class CartItemUpdateRequest {
    @SerializedName("quantity")
    private int quantity;

    public CartItemUpdateRequest(int quantity) {
        this.quantity = quantity;
    }

    // Getter
    public int getQuantity() {
        return quantity;
    }
}
