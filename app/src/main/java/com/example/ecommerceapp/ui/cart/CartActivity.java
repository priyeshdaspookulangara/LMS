package com.example.ecommerceapp.ui.cart;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.ecommerceapp.R;
import com.example.ecommerceapp.data.model.CartItem;
// import com.example.ecommerceapp.ui.checkout.CheckoutActivity; // To be created

import java.util.ArrayList;
import java.util.Locale;

public class CartActivity extends AppCompatActivity implements CartAdapter.CartItemInteractionListener {

    private CartViewModel cartViewModel;
    private RecyclerView recyclerViewCartItems;
    private CartAdapter cartAdapter;
    private TextView textViewCartTotal;
    private TextView textViewEmptyCart;
    private Button buttonCheckout;
    private ProgressBar progressBarCart; // Optional, if loading cart is async

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Shopping Cart");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        recyclerViewCartItems = findViewById(R.id.recyclerViewCartItems);
        textViewCartTotal = findViewById(R.id.textViewCartTotal);
        textViewEmptyCart = findViewById(R.id.textViewEmptyCart);
        buttonCheckout = findViewById(R.id.buttonCheckout);
        progressBarCart = findViewById(R.id.progressBarCart); // Initialize if used

        // Setup RecyclerView
        recyclerViewCartItems.setLayoutManager(new LinearLayoutManager(this));
        cartAdapter = new CartAdapter(new ArrayList<>(), this);
        recyclerViewCartItems.setAdapter(cartAdapter);

        // Setup ViewModel
        cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);

        observeViewModel();

        buttonCheckout.setOnClickListener(v -> {
            if (cartAdapter.getItemCount() > 0) {
                Intent intent = new Intent(CartActivity.this, com.example.ecommerceapp.ui.checkout.CheckoutActivity.class);
                startActivity(intent);
                // Optionally, you might want to finish CartActivity or expect a result
                // from CheckoutActivity (e.g., if order is placed, clear cart here or finish).
            } else {
                Toast.makeText(this, "Your cart is empty.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void observeViewModel() {
        // Show progress bar if cart loading is async (not really the case for current mock repo)
        // progressBarCart.setVisibility(View.VISIBLE);

        cartViewModel.getCart().observe(this, cart -> {
            // progressBarCart.setVisibility(View.GONE);
            if (cart != null && !cart.getItems().isEmpty()) {
                cartAdapter.updateCartItems(cart.getItems());
                textViewCartTotal.setText(String.format(Locale.getDefault(), "Total: $%.2f", cart.getTotalPrice()));
                textViewCartTotal.setVisibility(View.VISIBLE);
                buttonCheckout.setVisibility(View.VISIBLE);
                buttonCheckout.setEnabled(true);
                textViewEmptyCart.setVisibility(View.GONE);
                recyclerViewCartItems.setVisibility(View.VISIBLE);
            } else {
                cartAdapter.updateCartItems(new ArrayList<>()); // Clear adapter
                textViewCartTotal.setVisibility(View.GONE);
                buttonCheckout.setVisibility(View.VISIBLE); // Keep visible but disable
                buttonCheckout.setEnabled(false);
                textViewEmptyCart.setVisibility(View.VISIBLE);
                recyclerViewCartItems.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onIncreaseQuantity(CartItem item) {
        // Potentially check stock before increasing, or let ViewModel/Repository handle
        cartViewModel.updateItemQuantity(item.getProduct().getProductId(), item.getQuantity() + 1);
    }

    @Override
    public void onDecreaseQuantity(CartItem item) {
        if (item.getQuantity() > 1) {
            cartViewModel.updateItemQuantity(item.getProduct().getProductId(), item.getQuantity() - 1);
        } else {
            // If quantity is 1, decreasing means removing
            cartViewModel.removeItemFromCart(item.getProduct().getProductId());
        }
    }

    @Override
    public void onRemoveItem(CartItem item) {
        cartViewModel.removeItemFromCart(item.getProduct().getProductId());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
