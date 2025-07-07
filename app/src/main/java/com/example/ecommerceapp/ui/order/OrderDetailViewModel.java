package com.example.ecommerceapp.ui.order;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;


import com.example.ecommerceapp.data.model.Order;
import com.example.ecommerceapp.data.model.User;
import com.example.ecommerceapp.data.repository.AuthRepository;
import com.example.ecommerceapp.data.repository.OrderRepository;

public class OrderDetailViewModel extends AndroidViewModel {

    private final OrderRepository orderRepository;
    private final AuthRepository authRepository;
    private final String orderId;

    private final MutableLiveData<Order> orderDetails = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    public OrderDetailViewModel(@NonNull Application application, String orderId) {
        super(application);
        Context appContext = application.getApplicationContext();
        this.orderRepository = OrderRepository.getInstance(appContext);
        this.authRepository = new AuthRepository(appContext); // AuthRepository needs context
        this.orderId = orderId;
        fetchOrderDetails();
    }

    public LiveData<Order> getOrderDetails() {
        return orderDetails;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    private void fetchOrderDetails() {
        isLoading.setValue(true);
        User currentUser = authRepository.getCurrentUser();
        if (currentUser == null || currentUser.getUserId() == null) {
            errorMessage.setValue("User not logged in. Cannot fetch order details.");
            isLoading.setValue(false);
            return;
        }
        // The API GET /orders/{orderId} is user-specific via token, so userId not needed in call
        orderRepository.getOrderById(orderId, new OrderRepository.OrderCallback<Order>() {
            @Override
            public void onSuccess(Order result) {
                orderDetails.setValue(result);
                isLoading.setValue(false);
            }

            @Override
            public void onError(String message) {
                errorMessage.setValue(message);
                isLoading.setValue(false);
            }
        });
    }
}

class OrderDetailViewModelFactory implements ViewModelProvider.Factory {
    private Application application;
    private String orderId;

    public OrderDetailViewModelFactory(Application application, String orderId) {
        this.application = application;
        this.orderId = orderId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(OrderDetailViewModel.class)) {
            return (T) new OrderDetailViewModel(application, orderId);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
