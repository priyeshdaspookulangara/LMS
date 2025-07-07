package com.example.ecommerceapp.ui.cart;

import android.app.Application; // For AndroidViewModel
import androidx.annotation.NonNull; // For AndroidViewModel constructor
import androidx.lifecycle.AndroidViewModel; // Changed from ViewModel
import androidx.lifecycle.LiveData;
// import androidx.lifecycle.ViewModel; // Removed

import com.example.ecommerceapp.data.model.Cart;
import com.example.ecommerceapp.data.model.Product;
import com.example.ecommerceapp.data.repository.CartRepository;

public class CartViewModel extends AndroidViewModel { // Changed to AndroidViewModel

    private CartRepository cartRepository;
    private LiveData<Cart> cartLiveData;
    // TODO: Add LiveData for loading state and error messages from repository

    public CartViewModel(@NonNull Application application) { // Constructor for AndroidViewModel
        super(application);
        // CartRepository is a singleton, getInstance() now needs context
        this.cartRepository = CartRepository.getInstance(application.getApplicationContext());
        this.cartLiveData = cartRepository.getCart(); // This gets the LiveData from repo
        refreshCart(); // Initial fetch
    }

    public LiveData<Cart> getCart() {
        return cartLiveData;
    }

    public void refreshCart() {
        // TODO: Set loading state true
        cartRepository.fetchCartFromServer();
        // Loading state false would be set based on repository callback or another LiveData for errors
    }

    public void addProductToCart(Product product, int quantity) {
        if (product != null && product.getProductId() != null && quantity > 0) {
            // TODO: Set loading state true
            cartRepository.addProductToCart(product, quantity); // Repository updates LiveData on success/fail
        } else {
            // TODO: Post error to LiveData: "Invalid product data for adding to cart"
        }
    }

    public void updateItemQuantity(String productId, int newQuantity) {
        if (productId != null) {
            // TODO: Set loading state true
            // The repository method now handles newQuantity <= 0 by calling remove
            cartRepository.updateItemQuantity(productId, newQuantity);
        } else {
            // TODO: Post error to LiveData: "Invalid product ID for updating quantity"
        }
    }

    public void removeItemFromCart(String productId) {
        if (productId != null) {
            // TODO: Set loading state true
            cartRepository.removeItemFromCart(productId);
        } else {
            // TODO: Post error to LiveData: "Invalid product ID for removal"
        }
    }

    public void clearCart() {
        // TODO: Set loading state true
        cartRepository.clearCart();
    }

    // Called when user logs in or out to refresh the cart data
    // e.g., from MainActivity or an Auth state observer
    public void onUserAuthenticationChanged() {
        refreshCart();
    }
}
