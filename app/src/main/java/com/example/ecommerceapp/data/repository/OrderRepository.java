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

import android.content.Context; // Added for ApiService
import androidx.annotation.NonNull; // Added for Retrofit Callbacks
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData; // Still used for userOrdersLiveData if needed for local caching/updates

import com.example.ecommerceapp.data.model.Order;
import com.example.ecommerceapp.data.model.order.OrderCreationRequest;
import com.example.ecommerceapp.data.model.order.OrderListResponse;
import com.example.ecommerceapp.network.ApiService;
import com.example.ecommerceapp.network.RetrofitClient;
// Removed unused User import, UUID, Date, Handler, Looper, stream.Collectors

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Repository for orders. Interacts with backend API.
 */
public class OrderRepository {

    private static volatile OrderRepository instance;
    private final ApiService apiService;
    // The userOrdersLiveData might be re-evaluated. Typically, ViewModels fetch and hold their own LiveData.
    // If this LiveData is meant to be a global cache, its update logic needs care.
    // For now, let's assume ViewModels will call methods that return data or use callbacks.
    // private final MutableLiveData<List<Order>> userOrdersLiveData = new MutableLiveData<>(new ArrayList<>());


    // Constructor requires Context for ApiService
    private OrderRepository(Context context) {
        this.apiService = RetrofitClient.getApiService(context.getApplicationContext());
    }

    public static OrderRepository getInstance(Context context) { // Context needed for initialization
        if (instance == null) {
            synchronized (OrderRepository.class) {
                if (instance == null) {
                    instance = new OrderRepository(context.getApplicationContext());
                }
            }
        }
        return instance;
    }

    // Constructor for testing or DI
    public OrderRepository(ApiService apiService) {
        this.apiService = apiService;
    }

    public interface OrderCallback<T> {
        void onSuccess(T result);
        void onError(String message);
    }

    public void placeOrder(OrderCreationRequest orderRequest, OrderCallback<Order> callback) {
        apiService.createOrder(orderRequest).enqueue(new Callback<Order>() {
            @Override
            public void onResponse(@NonNull Call<Order> call, @NonNull Response<Order> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body()); // API returns the created Order
                } else {
                    // TODO: Parse errorBody for specific API error message
                    callback.onError("Order placement failed. Code: " + response.code() + ". " + response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Order> call, @NonNull Throwable t) {
                callback.onError("Network error during order placement: " + t.getMessage());
            }
        });
    }

    // Fetches orders for the authenticated user (token handled by interceptor)
    public void getOrdersForUser(Map<String, String> options, OrderCallback<OrderListResponse> callback) {
        apiService.getAllOrdersForUser(options).enqueue(new Callback<OrderListResponse>() {
            @Override
            public void onResponse(@NonNull Call<OrderListResponse> call, @NonNull Response<OrderListResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Failed to fetch orders. Code: " + response.code() + ". " + response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<OrderListResponse> call, @NonNull Throwable t) {
                callback.onError("Network error fetching orders: " + t.getMessage());
            }
        });
    }

    // Fetches a specific order by ID for the authenticated user
    public void getOrderById(String orderId, OrderCallback<Order> callback) {
        apiService.getOrderById(orderId).enqueue(new Callback<Order>() {
            @Override
            public void onResponse(@NonNull Call<Order> call, @NonNull Response<Order> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Failed to fetch order " + orderId + ". Code: " + response.code() + ". " + response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Order> call, @NonNull Throwable t) {
                callback.onError("Network error fetching order " + orderId + ": " + t.getMessage());
            }
        });
    }

    // The setObservingUser and related LiveData logic for userOrdersLiveData is removed.
    // ViewModels (OrderHistoryViewModel) will be responsible for fetching orders for the
    // current user and managing their own LiveData.
}
