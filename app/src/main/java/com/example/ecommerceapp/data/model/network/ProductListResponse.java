package com.example.ecommerceapp.data.model.network;

import com.example.ecommerceapp.data.model.Product; // Will be updated later
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ProductListResponse {
    @SerializedName("products")
    public List<Product> products;

    @SerializedName("total")
    public int total;

    @SerializedName("page")
    public int page;

    @SerializedName("limit")
    public int limit;

    // Default constructor
    public ProductListResponse() {}

    // Constructor with all fields
    public ProductListResponse(List<Product> products, int total, int page, int limit) {
        this.products = products;
        this.total = total;
        this.page = page;
        this.limit = limit;
    }

    // Getters and Setters (optional, public fields are often used with Gson for simplicity)
    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
