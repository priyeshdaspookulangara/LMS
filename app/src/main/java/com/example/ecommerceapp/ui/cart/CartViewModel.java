package com.example.ecommerceapp.ui.cart;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.ecommerceapp.data.model.Cart;
import com.example.ecommerceapp.data.model.Product;
import com.example.ecommerceapp.data.repository.CartRepository;

public class CartViewModel extends ViewModel {

    private CartRepository cartRepository;
    private LiveData<Cart> cartLiveData;

    public CartViewModel() {
        // In a real app, inject this (e.g., using Hilt)
        // CartRepository is a singleton, so getInstance() is fine for this simulation
        this.cartRepository = CartRepository.getInstance();
        this.cartLiveData = cartRepository.getCart();
    }

    public LiveData<Cart> getCart() {
        return cartLiveData;
    }

    public void addProductToCart(Product product, int quantity) {
        if (product != null && quantity > 0) {
            cartRepository.addProductToCart(product, quantity);
        }
    }

    public void updateItemQuantity(String productId, int newQuantity) {
        if (newQuantity > 0) {
            cartRepository.updateItemQuantity(productId, newQuantity);
        } else {
            // If quantity is 0 or less, remove the item
            cartRepository.removeItemFromCart(productId);
        }
    }

    public void removeItemFromCart(String productId) {
        cartRepository.removeItemFromCart(productId);
    }

    public void clearCart() {
        cartRepository.clearCart();
    }

    // Example: Associate cart when user logs in (called from AuthViewModel or similar)
    public void userLoggedIn(String userId) {
        cartRepository.associateCartWithUser(userId);
    }

    // Example: Disassociate cart when user logs out
    public void userLoggedOut() {
        cartRepository.disassociateCart();
    }
}
