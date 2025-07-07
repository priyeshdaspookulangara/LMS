package com.example.ecommerceapp.data.model.order;

import com.google.gson.annotations.SerializedName;

public class OrderCreationRequest {

    @SerializedName("shippingAddressId")
    private String shippingAddressId;

    @SerializedName("paymentMethodId")
    private String paymentMethodId;

    // Add other fields if your API requires more for order creation from cart
    // e.g., notes, specific cart ID if not inferred by API from user session

    public OrderCreationRequest(String shippingAddressId, String paymentMethodId) {
        this.shippingAddressId = shippingAddressId;
        this.paymentMethodId = paymentMethodId;
    }

    // Getters (optional, public fields are fine with Gson for simple DTOs)
    public String getShippingAddressId() {
        return shippingAddressId;
    }

    public String getPaymentMethodId() {
        return paymentMethodId;
    }
}
