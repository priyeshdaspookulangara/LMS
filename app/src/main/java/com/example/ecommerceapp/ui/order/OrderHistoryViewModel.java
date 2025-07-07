package com.example.ecommerceapp.ui.order;

import android.app.Application; // For AndroidViewModel
import androidx.annotation.NonNull; // For AndroidViewModel constructor
import androidx.lifecycle.AndroidViewModel; // Changed from ViewModel
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
// import androidx.lifecycle.ViewModel; // Removed

import com.example.ecommerceapp.data.model.Order;
import com.example.ecommerceapp.data.model.User;
import com.example.ecommerceapp.data.model.order.OrderListResponse;
import com.example.ecommerceapp.data.repository.AuthRepository;
import com.example.ecommerceapp.data.repository.OrderRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderHistoryViewModel extends AndroidViewModel { // Changed to AndroidViewModel

    private final OrderRepository orderRepository;
    private final AuthRepository authRepository;

    private final MutableLiveData<List<Order>> userOrders = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    // TODO: Add LiveData for pagination details (total, page, limit) if implementing UI pagination

    public OrderHistoryViewModel(@NonNull Application application) { // Updated constructor
        super(application);
        Context appContext = application.getApplicationContext();
        orderRepository = OrderRepository.getInstance(appContext);
        authRepository = new AuthRepository(appContext); // AuthRepository also needs context

        loadOrders(); // Initial load
    }

    public LiveData<List<Order>> getUserOrders() {
        return userOrders;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public void loadOrders() { // Renamed from refreshOrders for clarity of initial load
        User currentUser = authRepository.getCurrentUser();
        if (currentUser != null && currentUser.getUserId() != null) {
            isLoading.setValue(true);
            errorMessage.setValue(null); // Clear previous errors

            Map<String, String> options = new HashMap<>();
            // TODO: Add pagination parameters to options if needed, e.g., options.put("page", "1");

            orderRepository.getOrdersForUser(options, new OrderRepository.OrderCallback<OrderListResponse>() {
                @Override
                public void onSuccess(OrderListResponse result) {
                    if (result != null && result.getOrders() != null) {
                        userOrders.setValue(result.getOrders());
                        // TODO: Update pagination LiveData here
                    } else {
                        userOrders.setValue(new ArrayList<>()); // Empty list
                    }
                    isLoading.setValue(false);
                }

                @Override
                public void onError(String message) {
                    errorMessage.setValue(message);
                    userOrders.setValue(new ArrayList<>()); // Clear list on error
                    isLoading.setValue(false);
                }
            });
        } else {
            errorMessage.setValue("User not logged in. Cannot fetch order history.");
            userOrders.setValue(new ArrayList<>()); // Clear list
            isLoading.setValue(false);
        }
    }
}
