package com.example.ecommerceapp.ui.product;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.widget.SearchView; // Import SearchView
import androidx.appcompat.app.AlertDialog; // For category filter dialog

import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.example.ecommerceapp.R;
import com.example.ecommerceapp.data.model.Category;
import com.example.ecommerceapp.fcm.MyFirebaseMessagingService; // Import for simulation
import com.example.ecommerceapp.ui.cart.CartActivity;
import com.example.ecommerceapp.ui.cart.CartViewModel; // Import CartViewModel
import com.example.ecommerceapp.data.model.Product;

import java.util.ArrayList;

public class ProductListActivity extends AppCompatActivity implements ProductAdapter.OnProductClickListener {

    private ProductViewModel productViewModel;
    private RecyclerView recyclerViewProducts;
    private ProductAdapter productAdapter;
    private ProgressBar progressBar;
    private TextView textViewEmpty;
    private SwipeRefreshLayout swipeRefreshLayout;
    private CartViewModel cartViewModel;
    private TextView cartBadgeTextView;
    private SearchView searchView;
    private List<Category> categoryList = new ArrayList<>(); // To store categories for filter
    private String currentSearchQuery = null;
    private String currentCategoryIdFilter = "all"; // "all" means no filter initially

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Products");
        }

        progressBar = findViewById(R.id.progressBarProductList);
        textViewEmpty = findViewById(R.id.textViewEmptyProductList);
        recyclerViewProducts = findViewById(R.id.recyclerViewProducts);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayoutProducts);

        // Setup RecyclerView
        recyclerViewProducts.setLayoutManager(new GridLayoutManager(this, 2)); // 2 columns
        productAdapter = new ProductAdapter(new ArrayList<>(), this);
        recyclerViewProducts.setAdapter(productAdapter);

        // Setup ViewModels
        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        cartViewModel = new ViewModelProvider(this).get(CartViewModel.class); // Initialize CartViewModel

        observeViewModel();
        observeCartViewModel(); // Observe cart changes for badge

        // Load products
        progressBar.setVisibility(View.VISIBLE);
        // productViewModel.fetchAllProducts(); // Initial load - now combined
        productViewModel.fetchProducts(currentSearchQuery, currentCategoryIdFilter);
        productViewModel.fetchCategories(); // Fetch categories for the filter

        swipeRefreshLayout.setOnRefreshListener(() -> {
            // productViewModel.fetchAllProducts();
             productViewModel.fetchProducts(currentSearchQuery, currentCategoryIdFilter); // Refresh with current filters
        });
    }

    private void observeViewModel() {
        productViewModel.getProducts().observe(this, products -> {
            progressBar.setVisibility(View.GONE);
            swipeRefreshLayout.setRefreshing(false);
            if (products != null && !products.isEmpty()) {
                productAdapter.updateProducts(products);
                recyclerViewProducts.setVisibility(View.VISIBLE);
                textViewEmpty.setVisibility(View.GONE);
            } else {
                recyclerViewProducts.setVisibility(View.GONE);
                textViewEmpty.setVisibility(View.VISIBLE);
            }
        });

        productViewModel.getErrorMessage().observe(this, errorMsg -> {
            progressBar.setVisibility(View.GONE);
            swipeRefreshLayout.setRefreshing(false);
            if (errorMsg != null && !errorMsg.isEmpty()) {
                Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show();
                textViewEmpty.setVisibility(View.VISIBLE);
                textViewEmpty.setText(errorMsg); // Show error in the empty text view
            }
        });

        productViewModel.getCategories().observe(this, categories -> {
            if (categories != null) {
                categoryList = categories;
                // Optionally, pre-populate a spinner or filter UI here if it's always visible
            }
        });
    }

    private void observeCartViewModel() {
        cartViewModel.getCart().observe(this, cart -> {
            if (cart != null) {
                updateCartBadge(cart.getTotalItemCount()); // Use total quantity of items
            } else {
                updateCartBadge(0);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_product_list, menu);
        final MenuItem cartItemMenu = menu.findItem(R.id.action_cart);
        View actionViewCart = cartItemMenu.getActionView();
        if (actionViewCart != null) {
            cartBadgeTextView = actionViewCart.findViewById(R.id.cart_badge);
            updateCartBadgeBasedOnViewModel(); // Initial update
            actionViewCart.setOnClickListener(v -> onOptionsItemSelected(cartItemMenu));
        }

        // Setup SearchView
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Search products...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                currentSearchQuery = query;
                productViewModel.fetchProducts(currentSearchQuery, currentCategoryIdFilter);
                searchView.clearFocus(); // Hide keyboard
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Optionally, implement live search here or just wait for submit
                // If newText is empty, and a query was active, reload all or filtered by category
                if (newText.isEmpty() && currentSearchQuery != null && !currentSearchQuery.isEmpty()) {
                    currentSearchQuery = null;
                    productViewModel.fetchProducts(null, currentCategoryIdFilter);
                }
                return true;
            }
        });

        // Listener for when search view is closed (X button or back button)
        searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true; // Return true to allow expand
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                // When search is collapsed, if there was a query, clear it and reload
                if (currentSearchQuery != null && !currentSearchQuery.isEmpty()) {
                    currentSearchQuery = null;
                    productViewModel.fetchProducts(null, currentCategoryIdFilter);
                }
                return true; // Return true to allow collapse
            }
        });

        return true;
    }
     private void updateCartBadgeBasedOnViewModel() {
        if (cartViewModel.getCart().getValue() != null) {
            updateCartBadge(cartViewModel.getCart().getValue().getTotalItemCount());
        } else {
            updateCartBadge(0);
        }
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_cart) {
            Intent intent = new Intent(this, CartActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_order_history) {
            Intent intent = new Intent(this, com.example.ecommerceapp.ui.order.OrderHistoryActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_filter_category) {
            showCategoryFilterDialog();
            return true;
        } else if (id == R.id.action_simulate_notification) {
            Map<String, String> dataPayload = new HashMap<>();
            // Example: Simulate a notification for a specific order update
            // dataPayload.put("order_id", "ORD-MOCK001"); // Replace with an actual or mock order ID
            // dataPayload.put("notification_type", "order_status");
            // MyFirebaseMessagingService.simulatePushNotification(this, "Order Shipped!", "Your order ORD-MOCK001 has been shipped.", dataPayload);

            // Example: Simulate a general promotional notification
             MyFirebaseMessagingService.simulatePushNotification(this, "Flash Sale!", "Get 50% off on selected items today!", new HashMap<>());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateCartBadge(int count) {
        if (cartBadgeTextView == null) return;
        if (count > 0) {
            cartBadgeTextView.setText(String.valueOf(Math.min(count, 99))); // Cap at 99 for display
            cartBadgeTextView.setVisibility(View.VISIBLE);
        } else {
            cartBadgeTextView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onProductClick(Product product) {
        Intent intent = new Intent(this, ProductDetailActivity.class);
        intent.putExtra(ProductDetailActivity.EXTRA_PRODUCT_ID, product.getProductId());
        startActivity(intent);
    }

    private void showCategoryFilterDialog() {
        if (categoryList.isEmpty()) {
            Toast.makeText(this, "Categories not loaded yet.", Toast.LENGTH_SHORT).show();
            productViewModel.fetchCategories(); // Try fetching again
            return;
        }

        List<String> categoryNames = new ArrayList<>();
        categoryNames.add("All Categories"); // Option to clear filter
        for (Category category : categoryList) {
            categoryNames.add(category.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, categoryNames);

        new AlertDialog.Builder(this)
                .setTitle("Filter by Category")
                .setAdapter(adapter, (dialog, which) -> {
                    if (which == 0) { // "All Categories" selected
                        currentCategoryIdFilter = "all";
                    } else {
                        // Adjust index because "All Categories" was added at the beginning
                        currentCategoryIdFilter = categoryList.get(which - 1).getCategoryId();
                    }
                    productViewModel.fetchProducts(currentSearchQuery, currentCategoryIdFilter);
                    dialog.dismiss();
                })
                .create()
                .show();
    }
}
