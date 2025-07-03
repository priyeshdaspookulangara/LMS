package com.example.ecommerceapp.ui.order;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.ecommerceapp.R;
import com.example.ecommerceapp.data.model.Order;

import java.util.ArrayList;

public class OrderHistoryActivity extends AppCompatActivity implements OrderHistoryAdapter.OnOrderClickListener {

    private OrderHistoryViewModel orderHistoryViewModel;
    private RecyclerView recyclerViewOrderHistory;
    private OrderHistoryAdapter orderHistoryAdapter;
    private ProgressBar progressBarOrderHistory;
    private TextView textViewEmptyOrderHistory;
    private SwipeRefreshLayout swipeRefreshLayoutOrderHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("My Orders");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        progressBarOrderHistory = findViewById(R.id.progressBarOrderHistory);
        textViewEmptyOrderHistory = findViewById(R.id.textViewEmptyOrderHistory);
        recyclerViewOrderHistory = findViewById(R.id.recyclerViewOrderHistory);
        swipeRefreshLayoutOrderHistory = findViewById(R.id.swipeRefreshLayoutOrderHistory);

        // Setup RecyclerView
        recyclerViewOrderHistory.setLayoutManager(new LinearLayoutManager(this));
        orderHistoryAdapter = new OrderHistoryAdapter(new ArrayList<>(), this);
        recyclerViewOrderHistory.setAdapter(orderHistoryAdapter);

        // Setup ViewModel
        orderHistoryViewModel = new ViewModelProvider(this).get(OrderHistoryViewModel.class);

        observeViewModel();

        // Initial load or refresh
        // orderHistoryViewModel.refreshOrders(); // ViewModel constructor already tries to load

        swipeRefreshLayoutOrderHistory.setOnRefreshListener(() -> {
            orderHistoryViewModel.refreshOrders();
        });
    }

    private void observeViewModel() {
        orderHistoryViewModel.getIsLoading().observe(this, isLoading -> {
            if (isLoading != null && isLoading) {
                if (!swipeRefreshLayoutOrderHistory.isRefreshing()) {
                    progressBarOrderHistory.setVisibility(View.VISIBLE);
                }
            } else {
                progressBarOrderHistory.setVisibility(View.GONE);
                swipeRefreshLayoutOrderHistory.setRefreshing(false);
            }
        });

        orderHistoryViewModel.getUserOrders().observe(this, orders -> {
            progressBarOrderHistory.setVisibility(View.GONE);
            swipeRefreshLayoutOrderHistory.setRefreshing(false);
            if (orders != null && !orders.isEmpty()) {
                orderHistoryAdapter.updateOrders(orders);
                recyclerViewOrderHistory.setVisibility(View.VISIBLE);
                textViewEmptyOrderHistory.setVisibility(View.GONE);
            } else {
                // It could be null if user not logged in, or empty list if no orders
                recyclerViewOrderHistory.setVisibility(View.GONE);
                textViewEmptyOrderHistory.setVisibility(View.VISIBLE);
                 if (orderHistoryViewModel.getErrorMessage().getValue() != null &&
                    orderHistoryViewModel.getErrorMessage().getValue().contains("User not logged in")) {
                    textViewEmptyOrderHistory.setText("Please log in to see your order history.");
                } else {
                    textViewEmptyOrderHistory.setText("You have no past orders.");
                }
            }
        });

        orderHistoryViewModel.getErrorMessage().observe(this, errorMsg -> {
            progressBarOrderHistory.setVisibility(View.GONE);
            swipeRefreshLayoutOrderHistory.setRefreshing(false);
            if (errorMsg != null && !errorMsg.isEmpty()) {
                // Only show toast if list is also empty, to avoid covering data with error
                if (orderHistoryAdapter.getItemCount() == 0) {
                     textViewEmptyOrderHistory.setText(errorMsg);
                     textViewEmptyOrderHistory.setVisibility(View.VISIBLE);
                }
                Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onOrderClick(Order order) {
        // Navigate to OrderDetailActivity (To be implemented)
        Intent intent = new Intent(this, OrderDetailActivity.class);
        intent.putExtra(OrderDetailActivity.EXTRA_ORDER_ID, order.getOrderId());
        startActivity(intent);
        // Toast.makeText(this, "Clicked on Order ID: " + order.getOrderId() + " (Detail view not implemented)", Toast.LENGTH_SHORT).show();
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
