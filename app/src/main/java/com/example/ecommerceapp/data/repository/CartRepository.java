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

    private static volatile CartRepository instance;
    private final MutableLiveData<Cart> cartLiveData;
    private final Cart cart;

    // Simulate a user ID for potential future use (e.g., syncing across devices)
    private String currentUserId = "guest_user"; // Could be updated upon login

    private CartRepository() {
        cart = new Cart();
        cartLiveData = new MutableLiveData<>();
        cartLiveData.setValue(cart); // Initialize LiveData with the empty cart
    }

    public static CartRepository getInstance() {
        if (instance == null) {
            synchronized (CartRepository.class) {
                if (instance == null) {
                    instance = new CartRepository();
                }
            }
        }
        return instance;
    }

    public LiveData<Cart> getCart() {
        return cartLiveData;
    }

    public void addProductToCart(Product product, int quantity) {
        if (product == null || quantity <= 0) return;

        // Check if product has enough stock before adding
        if (product.getStockQuantity() < quantity) {
            // Handle insufficient stock - maybe through a callback or LiveData event
            System.err.println("Insufficient stock for product: " + product.getName());
            // For now, we'll just add what's available if some stock exists
            // Or simply don't add if not enough. For simplicity, let's assume we can add.
            // A real app needs robust stock checking.
        }

        cart.addItem(product, quantity);
        cartLiveData.postValue(cart); // Update LiveData
    }

    public void updateItemQuantity(String productId, int newQuantity) {
        CartItem itemToUpdate = null;
        for(CartItem item : cart.getItems()){
            if(item.getProduct().getProductId().equals(productId)){
                itemToUpdate = item;
                break;
            }
        }

        if(itemToUpdate != null){
             // Check stock before updating
            if (itemToUpdate.getProduct().getStockQuantity() < newQuantity && newQuantity > itemToUpdate.getQuantity()) {
                 System.err.println("Insufficient stock to increase quantity for: " + itemToUpdate.getProduct().getName());
                 // Optionally, set to max available stock or notify user
                 // For now, we'll allow it, assuming checks are elsewhere or for simplicity.
            }
            cart.updateItemQuantity(productId, newQuantity);
            cartLiveData.postValue(cart);
        }
    }

    public void removeItemFromCart(String productId) {
        cart.removeItem(productId);
        cartLiveData.postValue(cart);
    }

    public void clearCart() {
        cart.clearCart();
        cartLiveData.postValue(cart);
    }

    // This method would be called when a user logs in
    public void associateCartWithUser(String userId) {
        this.currentUserId = userId;
        // Here you might:
        // 1. Load cart from backend for this user.
        // 2. Merge guest cart with user's server cart.
        // For now, we just update the user ID.
        // If cart was persisted locally per user, load it here.
        System.out.println("Cart associated with user: " + userId);
    }

    // This would be called on logout
    public void disassociateCart() {
        this.currentUserId = "guest_user";
        // Potentially clear cart or save it for the logged-out user if that's a feature.
        // For simplicity, current in-memory cart remains.
        System.out.println("Cart disassociated, now guest cart.");
    }
}
