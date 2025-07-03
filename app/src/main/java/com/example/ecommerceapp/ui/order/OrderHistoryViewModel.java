package com.example.ecommerceapp.ui.order;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ecommerceapp.data.model.Order;
import com.example.ecommerceapp.data.model.User;
import com.example.ecommerceapp.data.repository.AuthRepository; // To get current user
import com.example.ecommerceapp.data.repository.OrderRepository;

import java.util.List;

public class OrderHistoryViewModel extends ViewModel {

    private final OrderRepository orderRepository;
    private final AuthRepository authRepository; // To get current user ID

    private LiveData<List<Order>> userOrders;
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    public OrderHistoryViewModel() {
        // In a real app, inject these
        orderRepository = OrderRepository.getInstance();
        // Assuming AuthRepository has a way to get the current user or is a singleton
        authRepository = new AuthRepository();

        User currentUser = authRepository.getCurrentUser();
        if (currentUser != null && currentUser.getUserId() != null) {
            // Set the user whose orders are being observed in the repository
            // This is a bit of a workaround for the mock repository's LiveData behavior.
            // In a real app, getOrdersForUser would directly return a LiveData scoped to that user.
            orderRepository.setObservingUser(currentUser.getUserId());
            userOrders = orderRepository.getOrdersForUser(currentUser.getUserId());
        } else {
            // Handle user not logged in - e.g., post an error or an empty list
            errorMessage.setValue("User not logged in. Cannot fetch order history.");
            userOrders = new MutableLiveData<>(); // Empty LiveData
        }
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

    public void refreshOrders() {
        User currentUser = authRepository.getCurrentUser();
        if (currentUser != null && currentUser.getUserId() != null) {
            isLoading.setValue(true);
            // This call in the mock repository will update the LiveData instance it holds
            orderRepository.getOrdersForUser(currentUser.getUserId());
            // In a real app, the repository call itself might be async and update LiveData upon completion
            // For mock, we assume it updates the LiveData that `userOrders` already points to.
            // To better simulate loading, you might do:
            // orderRepository.fetchOrdersAsync(currentUser.getUserId(), new OrderRepository.OrderCallback<List<Order>>() {
            //     @Override
            //     public void onSuccess(List<Order> result) {
            //         ((MutableLiveData<List<Order>>)userOrders).postValue(result); // If userOrders can be cast
            //         isLoading.setValue(false);
            //     }
            //     @Override
            //     public void onError(String message) {
            //         errorMessage.setValue(message);
            //         isLoading.setValue(false);
            //     }
            // });
            // For simplicity with current mock repo, just getting the LiveData again.
            // The setObservingUser and getOrdersForUser in mock repo should trigger update.
            isLoading.setValue(false); // Simulate immediate load for now
        } else {
            errorMessage.setValue("User not logged in. Cannot refresh order history.");
        }
    }
}
