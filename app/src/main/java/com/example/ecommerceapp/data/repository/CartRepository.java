package com.example.ecommerceapp.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.ecommerceapp.data.model.Cart;
import com.example.ecommerceapp.data.model.CartItem;
import com.example.ecommerceapp.data.model.Product;

/**
 * Simulated repository for the shopping cart.
 * In a real app, this might interact with a local database (e.g., Room for persistence)
 * and sync with a backend API.
 * For this simulation, it's an in-memory singleton.
 */
public class CartRepository {

import android.content.Context; // Added for ApiService
import androidx.annotation.NonNull; // Added for Retrofit Callbacks
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.ecommerceapp.data.model.Cart;
import com.example.ecommerceapp.data.model.Product; // Still needed for addProductToCart method signature
import com.example.ecommerceapp.data.model.cart.CartItemRequest;
import com.example.ecommerceapp.data.model.cart.CartItemUpdateRequest;
import com.example.ecommerceapp.network.ApiService;
import com.example.ecommerceapp.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Repository for the shopping cart. Interacts with backend API.
 */
public class CartRepository {

    private static volatile CartRepository instance;
    private final ApiService apiService;
    private final MutableLiveData<Cart> cartLiveData = new MutableLiveData<>();
    // No local 'cart' object anymore, state comes from API.
    // We might cache the last fetched cart in cartLiveData.

    // currentUserId is no longer needed here as AuthInterceptor handles tokens.

    private CartRepository(Context context) { // Context needed for ApiService
        this.apiService = RetrofitClient.getApiService(context.getApplicationContext());
        fetchCartFromServer(); // Initial fetch if user might be logged in
    }

    public static CartRepository getInstance(Context context) { // Context needed for initialization
        if (instance == null) {
            synchronized (CartRepository.class) {
                if (instance == null) {
                    instance = new CartRepository(context.getApplicationContext());
                }
            }
        }
        return instance;
    }

    // Constructor for testing with mocks
    public CartRepository(ApiService apiService) {
        this.apiService = apiService;
    }


    public LiveData<Cart> getCart() {
        // Optionally, always fetch from server when this is called, or return cached LiveData.
        // For simplicity, we'll have explicit fetch/refresh methods.
        return cartLiveData;
    }

    public void fetchCartFromServer() {
        apiService.getUserCart().enqueue(new Callback<Cart>() {
            @Override
            public void onResponse(@NonNull Call<Cart> call, @NonNull Response<Cart> response) {
                if (response.isSuccessful() && response.body() != null) {
                    cartLiveData.postValue(response.body());
                } else {
                    // Post null or an "error" cart state, or keep old state and notify error separately
                    cartLiveData.postValue(null); // Or new Cart() to represent an empty/error state
                    // TODO: Better error propagation to ViewModel
                    System.err.println("Error fetching cart: " + response.code() + " - " + response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Cart> call, @NonNull Throwable t) {
                cartLiveData.postValue(null); // Or new Cart()
                // TODO: Better error propagation
                 System.err.println("Network error fetching cart: " + t.getMessage());
            }
        });
    }


    public void addProductToCart(Product product, int quantity) { // Product object still useful for productId
        if (product == null || product.getProductId() == null || quantity <= 0) {
            // TODO: Propagate error or handle invalid input
            System.err.println("Invalid product or quantity for addProductToCart");
            return;
        }
        // API might do stock checking. Client-side check was removed for now.

        apiService.addItemToCart(new CartItemRequest(product.getProductId(), quantity)).enqueue(new Callback<Cart>() {
            @Override
            public void onResponse(@NonNull Call<Cart> call, @NonNull Response<Cart> response) {
                if (response.isSuccessful() && response.body() != null) {
                    cartLiveData.postValue(response.body()); // API returns updated cart
                } else {
                    // TODO: Error handling - e.g., product out of stock, other API errors
                    System.err.println("Error adding item to cart: " + response.code() + " - " + response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Cart> call, @NonNull Throwable t) {
                // TODO: Error handling
                 System.err.println("Network error adding item to cart: " + t.getMessage());
            }
        });
    }

    public void updateItemQuantity(String productId, int newQuantity) {
        if (productId == null || newQuantity <= 0) { // API implies quantity > 0 for update, 0 or less might be DELETE
             // If newQuantity is 0, call removeItemFromCart instead.
            if (newQuantity <= 0) {
                removeItemFromCart(productId);
                return;
            }
            System.err.println("Invalid productId or quantity for updateItemQuantity");
            return;
        }

        apiService.updateCartItem(productId, new CartItemUpdateRequest(newQuantity)).enqueue(new Callback<Cart>() {
            @Override
            public void onResponse(@NonNull Call<Cart> call, @NonNull Response<Cart> response) {
                if (response.isSuccessful() && response.body() != null) {
                    cartLiveData.postValue(response.body());
                } else {
                    System.err.println("Error updating cart item: " + response.code() + " - " + response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Cart> call, @NonNull Throwable t) {
                 System.err.println("Network error updating cart item: " + t.getMessage());
            }
        });
    }

    public void removeItemFromCart(String productId) {
        if (productId == null) return;
        apiService.removeItemFromCart(productId).enqueue(new Callback<Void>() { // API returns 204 or 200 with cart
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    // If successful, the cart state has changed on the server.
                    // We need to re-fetch the cart to get its new state.
                    fetchCartFromServer();
                } else {
                     System.err.println("Error removing item from cart: " + response.code() + " - " + response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                System.err.println("Network error removing item from cart: " + t.getMessage());
            }
        });
    }

    public void clearCart() {
        apiService.clearCart().enqueue(new Callback<Void>() { // API returns 204
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    // Cart is cleared on server, update local LiveData to an empty cart or re-fetch.
                    cartLiveData.postValue(new Cart()); // Post an empty cart
                    // Or call fetchCartFromServer(); which should return an empty cart.
                } else {
                    System.err.println("Error clearing cart: " + response.code() + " - " + response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                System.err.println("Network error clearing cart: " + t.getMessage());
            }
        });
    }

    // associateCartWithUser and disassociateCart are no longer strictly needed here
    // as the AuthInterceptor handles the token.
    // However, upon login/logout, CartViewModel might want to trigger a cart refresh.
    // So, fetchCartFromServer() can be called by CartViewModel after auth state changes.

}
