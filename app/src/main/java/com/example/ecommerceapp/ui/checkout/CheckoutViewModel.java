package com.example.ecommerceapp.ui.checkout;

import android.app.Application; // For AndroidViewModel
import androidx.annotation.NonNull; // For AndroidViewModel constructor
import androidx.lifecycle.AndroidViewModel; // Changed from ViewModel
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
// import androidx.lifecycle.ViewModel; // Removed

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

public class CheckoutViewModel extends AndroidViewModel { // Changed to AndroidViewModel

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final AuthRepository authRepository;

    // LiveData for building the order client-side before final placement
    // This 'currentOrder' is more of a draft or view model state than a direct API model at this stage.
    private final MutableLiveData<Order> draftOrder = new MutableLiveData<>(); // Renamed for clarity

    // LiveData for UI input fields
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

    // New LiveData for placeholder IDs, to be set by UI for now
    private final MutableLiveData<String> shippingAddressIdPlaceholder = new MutableLiveData<>();
    private final MutableLiveData<String> paymentMethodIdPlaceholder = new MutableLiveData<>();


    public CheckoutViewModel(@NonNull Application application) { // Updated constructor
        super(application);
        Context appContext = application.getApplicationContext();
        orderRepository = OrderRepository.getInstance(appContext);
        cartRepository = CartRepository.getInstance(appContext);
        authRepository = new AuthRepository(appContext); // AuthRepository also needs context

        initializeDraftOrder();
    }

    private void initializeDraftOrder() { // Renamed
        User currentUser = authRepository.getCurrentUser();
        if (currentUser != null && currentUser.getUserId() != null) {
            // Create a new local Order object to hold draft details.
            // This doesn't have an orderId from the server yet.
            Order newDraftOrder = new Order();
            newDraftOrder.setUserId(currentUser.getUserId()); // Set userId for reference
            // Pre-fill with user's saved address if available (future enhancement)
            // For now, shipping details are collected into separate LiveData.
            draftOrder.setValue(newDraftOrder);
        } else {
            orderPlacementError.setValue("User not logged in. Cannot proceed with checkout.");
        }
    }

    // LiveData Getters for UI observation
    public LiveData<Order> getDraftOrder() { return draftOrder; } // Renamed getter
    public LiveData<String> getShippingFullName() { return shippingFullName; }
    public LiveData<String> getShippingAddressLine1() { return shippingAddressLine1; }
    public LiveData<String> getShippingAddressLine2() { return shippingAddressLine2; }
    public LiveData<String> getShippingCity() { return shippingCity; }
    public LiveData<String> getShippingState() { return shippingState; }
    public LiveData<String> getShippingPostalCode() { return shippingPostalCode; }
    public LiveData<String> getShippingCountry() { return shippingCountry; }
    public LiveData<String> getShippingPhoneNumber() { return shippingPhoneNumber; }
    public LiveData<String> getSelectedPaymentMethod() { return selectedPaymentMethod; } // For UI state
    public LiveData<Boolean> getOrderPlacementResult() { return orderPlacementResult; }
    public LiveData<String> getOrderPlacementError() { return orderPlacementError; }

    // Getters and Setters for placeholder IDs
    public LiveData<String> getShippingAddressIdPlaceholder() { return shippingAddressIdPlaceholder; }
    public void setShippingAddressIdPlaceholder(String id) { shippingAddressIdPlaceholder.setValue(id); }
    public LiveData<String> getPaymentMethodIdPlaceholder() { return paymentMethodIdPlaceholder; }
    public void setPaymentMethodIdPlaceholder(String id) { paymentMethodIdPlaceholder.setValue(id); }


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
        Order order = draftOrder.getValue(); // Use draftOrder
        Cart cart = cartRepository.getCart().getValue();

        if (order == null || cart == null || cart.getItems().isEmpty()) {
            orderPlacementError.setValue("Cannot prepare order summary. Cart is empty or order not initialized.");
            return;
        }

        // Create a new Order.ShippingAddress object for the draftOrder
        Order.ShippingAddress shippingAddr = new Order.ShippingAddress();
        shippingAddr.fullName = shippingFullName.getValue();
        shippingAddr.addressLine1 = shippingAddressLine1.getValue();
        shippingAddr.addressLine2 = shippingAddressLine2.getValue();
        shippingAddr.city = shippingCity.getValue();
        shippingAddr.state = shippingState.getValue();
        shippingAddr.postalCode = shippingPostalCode.getValue();
        shippingAddr.country = shippingCountry.getValue();
        shippingAddr.phoneNumber = shippingPhoneNumber.getValue();
        order.setShippingAddress(shippingAddr);


        // Create a new Order.PaymentDetails object
        Order.PaymentDetails payment = new Order.PaymentDetails();
        payment.paymentMethodType = selectedPaymentMethod.getValue();
        // Other payment details (status, transactionId) will be set by API response or later steps.
        order.setPaymentDetails(payment);


        // Convert CartItems to OrderItems
        List<OrderItem> orderItems = cart.getItems().stream()
                .map(cartItem -> new OrderItem(
                        "client_item_" + cartItem.getProductId(), // Temporary client-side ID
                        cartItem.getProductId(),
                        cartItem.getName(),
                        cartItem.getQuantity(),
                        cartItem.getPrice()
                ))
                .collect(Collectors.toList());
        order.setItems(orderItems);
        order.setTotalAmount(cart.getTotalAmount()); // Use total from cart API

        draftOrder.setValue(order); // Update the LiveData for the order summary screen
    }


    public void placeOrder() {
        Order orderToPlace = draftOrder.getValue(); // Use draftOrder
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
        // orderToPlace.setPaymentStatus("Paid"); // This will be set by backend
        // orderToPlace.setTransactionId("SIM_TXN_" + System.currentTimeMillis()); // This will be set by backend
        // orderToPlace.setStatus("Processing"); // This will be set by backend

        String shipAddrId = shippingAddressIdPlaceholder.getValue();
        String payMethodId = paymentMethodIdPlaceholder.getValue();

        if (shipAddrId == null || shipAddrId.isEmpty()) {
            orderPlacementError.setValue("Shipping Address ID is required.");
            orderPlacementResult.setValue(false);
            return;
        }
        if (payMethodId == null || payMethodId.isEmpty()) {
            orderPlacementError.setValue("Payment Method ID is required.");
            orderPlacementResult.setValue(false);
            return;
        }

        OrderCreationRequest orderRequest = new OrderCreationRequest(shipAddrId, payMethodId);

        orderRepository.placeOrder(orderRequest, new OrderRepository.OrderCallback<Order>() {
            @Override
            public void onSuccess(Order placedOrder) {
                // The API returns the created order.
                // We can update draftOrder or a new LiveData<Order> for the confirmed order.
                draftOrder.setValue(placedOrder); // Update draft with server-confirmed details
                orderPlacementResult.setValue(true);
                cartRepository.clearCart(); // Clear cart on successful API call
            }

            @Override
            public void onError(String message) {
                orderPlacementError.setValue(message);
                orderPlacementResult.setValue(false);
            }
        });
    }
}
