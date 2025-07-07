package com.example.ecommerceapp.data.model.order;

import com.example.ecommerceapp.data.model.Order; // Will be updated to match API
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class OrderListResponse {

    @SerializedName("orders") // Assuming the list of orders is under a key like "orders" or "data"
    private List<Order> orders;

    @SerializedName("total")
    private int total;

    @SerializedName("page")
    private int page;

    @SerializedName("limit")
    private int limit;

    // Default constructor for Gson
    public OrderListResponse() {}

    public OrderListResponse(List<Order> orders, int total, int page, int limit) {
        this.orders = orders;
        this.total = total;
        this.page = page;
        this.limit = limit;
    }

    // Getters
    public List<Order> getOrders() {
        return orders;
    }

    public int getTotal() {
        return total;
    }

    public int getPage() {
        return page;
    }

    public int getLimit() {
        return limit;
    }

    // Setters (optional)
    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
