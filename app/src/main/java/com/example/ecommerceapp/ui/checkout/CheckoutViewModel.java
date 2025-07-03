package com.example.ecommerceapp.ui.checkout;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ecommerceapp.data.model.Cart;
import com.example.ecommerceapp.data.model.Order;
import com.example.ecommerceapp.data.model.OrderItem;
import com.example.ecommerceapp.data.model.User; // Assuming User model exists
import com.example.ecommerceapp.data.repository.AuthRepository; // To get current user
import com.example.ecommerceapp.data.repository.CartRepository;
import com.example.ecommerceapp.data.repository.OrderRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CheckoutViewModel extends ViewModel {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final AuthRepository authRepository; // To get current user

    private final MutableLiveData<Order> currentOrder = new MutableLiveData<>();
    private final MutableLiveData<String> shippingFullName = new MutableLiveData<>();
    private final MutableLiveData<String> shippingAddressLine1 = new MutableLiveData<>();
    private final MutableLiveData<String> shippingAddressLine2 = new MutableLiveData<>();
    private final MutableLiveData<String> shippingCity = new MutableLiveData<>();
    private final MutableLiveData<String> shippingState = new MutableLiveData<>();
    private final MutableLiveData<String> shippingPostalCode = new MutableLiveData<>();
    private final MutableLiveData<String> shippingCountry = new MutableLiveData<>();
    private final MutableLiveData<String> shippingPhoneNumber = new MutableLiveData<>();

    private final MutableLiveData<String> selectedPaymentMethod = new MutableLiveData<>();
    // Add LiveData for other payment details if needed (e.g., card token)

    private final MutableLiveData<Boolean> orderPlacementResult = new MutableLiveData<>();
    private final MutableLiveData<String> orderPlacementError = new MutableLiveData<>();


    public CheckoutViewModel() {
        // In a real app, inject these (e.g., using Hilt)
        orderRepository = OrderRepository.getInstance();
        cartRepository = CartRepository.getInstance();
        // AuthRepository might not be a singleton, adjust as per your AuthRepository implementation
        // For this example, let's assume it has a static method or is also a singleton for simplicity
        authRepository = new AuthRepository(); // Or AuthRepository.getInstance() if it's a singleton

        initializeOrder();
    }

    private void initializeOrder() {
        User currentUser = authRepository.getCurrentUser(); // Get current logged-in user
        if (currentUser != null) {
            Order newOrder = new Order(currentUser.getUserId());
            // Pre-fill with user's saved address if available
            // For now, we'll leave them blank for user to input
            // newOrder.setShippingFullName(currentUser.getFullName());
            // ... set other address fields ...
            currentOrder.setValue(newOrder);
        } else {
            // Handle case where user is not logged in - checkout shouldn't be accessible
            // Or it should force login first. For now, this is a potential issue.
            orderPlacementError.setValue("User not logged in. Cannot proceed with checkout.");
        }
    }

    // LiveData Getters for UI observation
    public LiveData<Order> getCurrentOrder() { return currentOrder; }
    public LiveData<String> getShippingFullName() { return shippingFullName; }
    public LiveData<String> getShippingAddressLine1() { return shippingAddressLine1; }
    public LiveData<String> getShippingAddressLine2() { return shippingAddressLine2; }
    public LiveData<String> getShippingCity() { return shippingCity; }
    public LiveData<String> getShippingState() { return shippingState; }
    public LiveData<String> getShippingPostalCode() { return shippingPostalCode; }
    public LiveData<String> getShippingCountry() { return shippingCountry; }
    public LiveData<String> getShippingPhoneNumber() { return shippingPhoneNumber; }
    public LiveData<String> getSelectedPaymentMethod() { return selectedPaymentMethod; }
    public LiveData<Boolean> getOrderPlacementResult() { return orderPlacementResult; }
    public LiveData<String> getOrderPlacementError() { return orderPlacementError; }


    // Update methods for shipping info
    public void updateShippingFullName(String name) { shippingFullName.setValue(name); }
    public void updateShippingAddressLine1(String address1) { shippingAddressLine1.setValue(address1); }
    public void updateShippingAddressLine2(String address2) { shippingAddressLine2.setValue(address2); }
    public void updateShippingCity(String city) { shippingCity.setValue(city); }
    public void updateShippingState(String state) { shippingState.setValue(state); }
    public void updateShippingPostalCode(String postalCode) { shippingPostalCode.setValue(postalCode); }
    public void updateShippingCountry(String country) { shippingCountry.setValue(country); }
    public void updateShippingPhoneNumber(String phone) { shippingPhoneNumber.setValue(phone); }

    // Update payment method
    public void updatePaymentMethod(String paymentMethod) { selectedPaymentMethod.setValue(paymentMethod); }


    public boolean validateShippingInfo() {
        return shippingFullName.getValue() != null && !shippingFullName.getValue().isEmpty() &&
               shippingAddressLine1.getValue() != null && !shippingAddressLine1.getValue().isEmpty() &&
               shippingCity.getValue() != null && !shippingCity.getValue().isEmpty() &&
               shippingState.getValue() != null && !shippingState.getValue().isEmpty() &&
               shippingPostalCode.getValue() != null && !shippingPostalCode.getValue().isEmpty() &&
               shippingCountry.getValue() != null && !shippingCountry.getValue().isEmpty() &&
               shippingPhoneNumber.getValue() != null && !shippingPhoneNumber.getValue().isEmpty();
        // Add more sophisticated validation (e.g., regex for postal code, phone)
    }

    public boolean validatePaymentMethod() {
        return selectedPaymentMethod.getValue() != null && !selectedPaymentMethod.getValue().isEmpty();
        // Add validation for specific payment details if collected (e.g., card number format)
    }


    public void prepareOrderForSummary() {
        Order order = currentOrder.getValue();
        Cart cart = cartRepository.getCart().getValue(); // Get current cart

        if (order == null || cart == null || cart.getItems().isEmpty()) {
            orderPlacementError.setValue("Cannot prepare order summary. Cart is empty or order not initialized.");
            return;
        }

        // Set shipping details from LiveData
        order.setShippingFullName(shippingFullName.getValue());
        order.setShippingAddressLine1(shippingAddressLine1.getValue());
        order.setShippingAddressLine2(shippingAddressLine2.getValue());
        order.setShippingCity(shippingCity.getValue());
        order.setShippingState(shippingState.getValue());
        order.setShippingPostalCode(shippingPostalCode.getValue());
        order.setShippingCountry(shippingCountry.getValue());
        order.setShippingPhoneNumber(shippingPhoneNumber.getValue());

        // Set payment method
        order.setPaymentMethod(selectedPaymentMethod.getValue());

        // Convert CartItems to OrderItems
        List<OrderItem> orderItems = cart.getItems().stream()
                .map(OrderItem::new) // Using the constructor OrderItem(CartItem)
                .collect(Collectors.toList());
        order.setItems(orderItems);
        order.setTotalAmount(cart.getTotalPrice()); // Or recalculate based on OrderItems if prices could change

        currentOrder.setValue(order); // Update the LiveData for the order summary screen
    }


    public void placeOrder() {
        Order orderToPlace = currentOrder.getValue();
        if (orderToPlace == null) {
            orderPlacementError.setValue("Order details not available.");
            orderPlacementResult.setValue(false);
            return;
        }
        if (orderToPlace.getUserId() == null || orderToPlace.getUserId().isEmpty()){
            orderPlacementError.setValue("User not identified. Cannot place order.");
            orderPlacementResult.setValue(false);
            return;
        }
         if (orderToPlace.getItems() == null || orderToPlace.getItems().isEmpty()){
            orderPlacementError.setValue("No items in order. Cannot place order.");
            orderPlacementResult.setValue(false);
            return;
        }


        // Simulate payment processing step here if it wasn't done before summary
        // For now, assume payment details are collected and "processed"
        orderToPlace.setPaymentStatus("Paid"); // Mark as paid for simulation
        orderToPlace.setTransactionId("SIM_TXN_" + System.currentTimeMillis()); // Simulated TXN ID
        orderToPlace.setStatus("Processing");

        orderRepository.placeOrder(orderToPlace, new OrderRepository.OrderCallback<Order>() {
            @Override
            public void onSuccess(Order placedOrder) {
                currentOrder.setValue(placedOrder); // Update with final order details from "backend"
                orderPlacementResult.setValue(true);
                cartRepository.clearCart(); // Clear cart after successful order
            }

            @Override
            public void onError(String message) {
                orderPlacementError.setValue(message);
                orderPlacementResult.setValue(false);
            }
        });
    }
}
