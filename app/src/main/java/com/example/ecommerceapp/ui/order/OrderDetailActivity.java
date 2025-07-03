package com.example.ecommerceapp.ui.order;

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

import com.example.ecommerceapp.R;
import com.example.ecommerceapp.data.model.Order;
import com.example.ecommerceapp.data.model.OrderItem;
import com.example.ecommerceapp.data.model.User;
import com.example.ecommerceapp.data.repository.AuthRepository; // To get current user for fetching specific order
import com.example.ecommerceapp.ui.checkout.OrderSummaryFragment; // Reusing its adapter

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class OrderDetailActivity extends AppCompatActivity {

    public static final String EXTRA_ORDER_ID = "extra_order_id";

    private OrderDetailViewModel orderDetailViewModel;
    private String orderId;

    private TextView textViewDetailOrderId, textViewDetailOrderDate, textViewDetailOrderStatus;
    private TextView textViewDetailShippingName, textViewDetailShippingAddress, textViewDetailShippingContact;
    private TextView textViewDetailPaymentMethod, textViewDetailPaymentStatus, textViewDetailTransactionId;
    private TextView textViewDetailOrderTotal;
    private RecyclerView recyclerViewOrderDetailItems;
    private OrderSummaryFragment.OrderSummaryAdapter orderItemsAdapter; // Reusing adapter from OrderSummaryFragment
    private ProgressBar progressBarOrderDetail;
    private View contentOrderDetail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Order Details");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if (getIntent() != null && getIntent().hasExtra(EXTRA_ORDER_ID)) {
            orderId = getIntent().getStringExtra(EXTRA_ORDER_ID);
        } else {
            Toast.makeText(this, "Order ID not found.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        bindViews();

        // Setup ViewModel
        // Pass orderId to ViewModel factory if needed, or set it via a method
        orderDetailViewModel = new ViewModelProvider(this, new OrderDetailViewModelFactory(getApplication(), orderId))
                                .get(OrderDetailViewModel.class);

        setupRecyclerView();
        observeViewModel();

        // orderDetailViewModel.fetchOrderDetails(orderId); // ViewModel constructor will call it
    }

    private void bindViews() {
        contentOrderDetail = findViewById(R.id.contentOrderDetail);
        progressBarOrderDetail = findViewById(R.id.progressBarOrderDetail);
        textViewDetailOrderId = findViewById(R.id.textViewDetailOrderId);
        textViewDetailOrderDate = findViewById(R.id.textViewDetailOrderDate);
        textViewDetailOrderStatus = findViewById(R.id.textViewDetailOrderStatus);
        textViewDetailShippingName = findViewById(R.id.textViewDetailShippingName);
        textViewDetailShippingAddress = findViewById(R.id.textViewDetailShippingAddress);
        textViewDetailShippingContact = findViewById(R.id.textViewDetailShippingContact);
        textViewDetailPaymentMethod = findViewById(R.id.textViewDetailPaymentMethod);
        textViewDetailPaymentStatus = findViewById(R.id.textViewDetailPaymentStatus);
        // textViewDetailTransactionId = findViewById(R.id.textViewDetailTransactionId); // Add if in layout
        textViewDetailOrderTotal = findViewById(R.id.textViewDetailOrderTotal);
        recyclerViewOrderDetailItems = findViewById(R.id.recyclerViewOrderDetailItems);
    }

    private void setupRecyclerView() {
        recyclerViewOrderDetailItems.setLayoutManager(new LinearLayoutManager(this));
        // Reusing the adapter from OrderSummaryFragment as it's simple enough
        orderItemsAdapter = new OrderSummaryFragment.OrderSummaryAdapter(new ArrayList<>());
        recyclerViewOrderDetailItems.setAdapter(orderItemsAdapter);
    }

    private void observeViewModel() {
        orderDetailViewModel.getIsLoading().observe(this, isLoading -> {
            if (isLoading != null && isLoading) {
                progressBarOrderDetail.setVisibility(View.VISIBLE);
                contentOrderDetail.setVisibility(View.GONE);
            } else {
                progressBarOrderDetail.setVisibility(View.GONE);
                contentOrderDetail.setVisibility(View.VISIBLE);
            }
        });

        orderDetailViewModel.getOrderDetails().observe(this, order -> {
            if (order != null) {
                populateOrderDetails(order);
            } else {
                // Error case might be handled by errorMessage LiveData
            }
        });

        orderDetailViewModel.getErrorMessage().observe(this, errorMsg -> {
            if (errorMsg != null && !errorMsg.isEmpty()) {
                Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show();
                // Optionally finish activity if order can't be loaded
                // finish();
            }
        });
    }

    private void populateOrderDetails(Order order) {
        textViewDetailOrderId.setText("Order ID: " + order.getOrderId());
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy, hh:mm a", Locale.getDefault());
        if (order.getOrderDate() != null) {
            textViewDetailOrderDate.setText("Date: " + dateFormat.format(order.getOrderDate()));
        } else {
             textViewDetailOrderDate.setText("Date: N/A");
        }
        textViewDetailOrderStatus.setText("Status: " + order.getStatus());
        // TODO: Set status color like in OrderHistoryAdapter

        textViewDetailShippingName.setText(order.getShippingFullName());
        String address = order.getShippingAddressLine1() +
                (order.getShippingAddressLine2() != null && !order.getShippingAddressLine2().isEmpty() ? "\n" + order.getShippingAddressLine2() : "") +
                "\n" + order.getShippingCity() + ", " + order.getShippingState() + " " + order.getShippingPostalCode() +
                "\n" + order.getShippingCountry();
        textViewDetailShippingAddress.setText(address);
        textViewDetailShippingContact.setText("Phone: " + order.getShippingPhoneNumber());

        textViewDetailPaymentMethod.setText("Paid via: " + order.getPaymentMethod());
        textViewDetailPaymentStatus.setText("Payment: " + order.getPaymentStatus());
        // if (order.getTransactionId() != null) {
        //     textViewDetailTransactionId.setText("Transaction ID: " + order.getTransactionId());
        //     textViewDetailTransactionId.setVisibility(View.VISIBLE);
        // } else {
        //     textViewDetailTransactionId.setVisibility(View.GONE);
        // }

        textViewDetailOrderTotal.setText(String.format(Locale.getDefault(), "Total: $%.2f", order.getTotalAmount()));

        if (order.getItems() != null) {
            orderItemsAdapter.updateOrderItems(order.getItems());
        }
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
