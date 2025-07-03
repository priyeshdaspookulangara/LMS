package com.example.ecommerceapp.data.repository;

import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.ecommerceapp.data.model.Order;
import com.example.ecommerceapp.data.model.User; // Assuming User model for user ID

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Simulated repository for orders.
 * In a real app, this would interact with a backend API.
 */
public class OrderRepository {

    private static volatile OrderRepository instance;
    private final MutableLiveData<List<Order>> userOrdersLiveData;
    private final Map<String, Order> mockOrders; // Stores all orders by orderId
    private final Handler handler = new Handler(Looper.getMainLooper());
    private static final int SIMULATED_DELAY_MS = 1200;


    private OrderRepository() {
        mockOrders = new HashMap<>();
        userOrdersLiveData = new MutableLiveData<>(new ArrayList<>());
        // setupMockOrders(); // Optional: pre-populate some orders
    }

    public static OrderRepository getInstance() {
        if (instance == null) {
            synchronized (OrderRepository.class) {
                if (instance == null) {
                    instance = new OrderRepository();
                }
            }
        }
        return instance;
    }

    public interface OrderCallback<T> {
        void onSuccess(T result);
        void onError(String message);
    }

    public void placeOrder(Order order, OrderCallback<Order> callback) {
        handler.postDelayed(() -> {
            if (order.getUserId() == null || order.getUserId().isEmpty()) {
                callback.onError("User ID is required to place an order.");
                return;
            }
            if (order.getItems() == null || order.getItems().isEmpty()) {
                callback.onError("Order must contain at least one item.");
                return;
            }
            // Simulate backend processing
            String orderId = "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            order.setOrderId(orderId);
            order.setOrderDate(new Date()); // Ensure order date is set at placement
            // In a real scenario, payment processing would happen here or before.
            // If payment is successful:
            order.setStatus("Processing"); // Or "Pending Payment" if payment is async
            order.setPaymentStatus("Paid"); // Assuming payment was successful

            mockOrders.put(orderId, order);

            // Update LiveData for the specific user if they are currently being observed
            List<Order> currentUserOrders = userOrdersLiveData.getValue();
            if (currentUserOrders != null && order.getUserId().equals(getCurrentUserIdForLiveData())) {
                // This is a simplification. LiveData should ideally be fetched again or updated more robustly.
                ArrayList<Order> updatedList = new ArrayList<>(currentUserOrders);
                updatedList.add(order);
                userOrdersLiveData.postValue(updatedList);
            }

            callback.onSuccess(order); // Return the processed order (with ID, status, etc.)
        }, SIMULATED_DELAY_MS);
    }

    public LiveData<List<Order>> getOrdersForUser(String userId) {
        // In a real app, this would fetch from backend.
        // Here, we filter our mock data.
        // For simplicity, this LiveData is updated when a new order is placed.
        // A more robust solution would fetch and then post, or have a dedicated LiveData per user.

        // This is a simplified way to update the LiveData for the "current" user.
        // In a real app, you'd likely fetch based on userId when this method is called.
        List<Order> filteredOrders = mockOrders.values().stream()
                .filter(o -> o.getUserId().equals(userId))
                .collect(Collectors.toList());
        userOrdersLiveData.postValue(filteredOrders); // Update the LiveData with orders for this specific user
        return userOrdersLiveData;
    }

    public void getOrderById(String orderId, String userId, OrderCallback<Order> callback) {
        handler.postDelayed(() -> {
            Order order = mockOrders.get(orderId);
            if (order != null && order.getUserId().equals(userId)) {
                callback.onSuccess(order);
            } else if (order != null && !order.getUserId().equals(userId)){
                callback.onError("Order does not belong to this user.");
            }
            else {
                callback.onError("Order not found.");
            }
        }, SIMULATED_DELAY_MS / 2);
    }


    // Helper to simulate which user's orders are "live" - very simplified.
    // In a real app, ViewModels would subscribe for specific users.
    private String currentUserIdForLiveData = null;
    public void setObservingUser(String userId) {
        this.currentUserIdForLiveData = userId;
        // Trigger a refresh for the new user being observed
        if (userId != null) {
            getOrdersForUser(userId);
        } else {
            userOrdersLiveData.postValue(new ArrayList<>());
        }
    }
    private String getCurrentUserIdForLiveData() {
        // This is a placeholder. In a real app, you'd get the current logged-in user's ID.
        // For now, it might come from AuthRepository or a session manager.
        // If using the setObservingUser mechanism:
        return this.currentUserIdForLiveData;
    }


    // Optional: Mock some initial orders
    private void setupMockOrders() {
        // User user1 = new User("user123", "testuser", "test@example.com", "Test User");
        // ArrayList<OrderItem> items1 = new ArrayList<>();
        // items1.add(new OrderItem("item_prod_1", "prod_1", "Elegant Floral Kurti", 1, 25.99));
        // items1.add(new OrderItem("item_prod_2", "prod_2", "Men's Casual Shirt", 2, 19.50));
        // double total1 = (1 * 25.99) + (2 * 19.50);

        // Order order1 = new Order("ORD-MOCK001", user1.getUserId(), new Date(System.currentTimeMillis() - 86400000), items1, total1,
        // "Delivered", "Test User", "123 Main St", null, "Anytown", "CA", "90210", "USA", "555-1234",
        // "Credit Card", "Paid", "txn_mock123");
        // mockOrders.put(order1.getOrderId(), order1);
    }
}
