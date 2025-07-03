package com.example.ecommerceapp.data.model;

import java.util.Objects;

public class CartItem {
    private Product product;
    private int quantity;
    // selectedSize and selectedColor could be added if products have variants
    // private String selectedSize;
    // private String selectedColor;

    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getSubtotal() {
        return product.getPrice() * quantity;
    }

    // Equals and HashCode are important if you're managing items in a Set or using DiffUtil
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartItem cartItem = (CartItem) o;
        // Assuming a product is uniquely identified by its ID for cart purposes
        return product.getProductId().equals(cartItem.product.getProductId());
        // If variants (size, color) are important, include them in equals and hashCode
    }

    @Override
    public int hashCode() {
        // Assuming a product is uniquely identified by its ID for cart purposes
        return Objects.hash(product.getProductId());
        // If variants (size, color) are important, include them in equals and hashCode
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "product=" + product.getName() +
                ", quantity=" + quantity +
                '}';
    }
}
