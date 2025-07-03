package com.example.ecommerceapp.ui.checkout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.ecommerceapp.R;
import com.example.ecommerceapp.data.model.Order;
import com.example.ecommerceapp.data.model.OrderItem; // Make sure this is created
import com.example.ecommerceapp.ui.cart.CartAdapter; // Re-use or create a simpler one for summary

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class OrderSummaryFragment extends Fragment {

    private CheckoutViewModel checkoutViewModel;

    private TextView textViewShippingName, textViewShippingAddress, textViewShippingContact;
    private TextView textViewPaymentMethod, textViewOrderTotal;
    private RecyclerView recyclerViewOrderItemsSummary;
    private OrderSummaryAdapter orderSummaryAdapter; // A simplified adapter for order items

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_summary, container, false);

        textViewShippingName = view.findViewById(R.id.textViewSummaryShippingName);
        textViewShippingAddress = view.findViewById(R.id.textViewSummaryShippingAddress);
        textViewShippingContact = view.findViewById(R.id.textViewSummaryShippingContact);
        textViewPaymentMethod = view.findViewById(R.id.textViewSummaryPaymentMethod);
        textViewOrderTotal = view.findViewById(R.id.textViewSummaryOrderTotal);
        recyclerViewOrderItemsSummary = view.findViewById(R.id.recyclerViewOrderItemsSummary);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        checkoutViewModel = new ViewModelProvider(requireActivity()).get(CheckoutViewModel.class);

        setupRecyclerView();
        observeViewModel();
    }

    private void setupRecyclerView() {
        recyclerViewOrderItemsSummary.setLayoutManager(new LinearLayoutManager(getContext()));
        orderSummaryAdapter = new OrderSummaryAdapter(new ArrayList<>());
        recyclerViewOrderItemsSummary.setAdapter(orderSummaryAdapter);
    }

    private void observeViewModel() {
        checkoutViewModel.getCurrentOrder().observe(getViewLifecycleOwner(), order -> {
            if (order != null) {
                populateOrderSummary(order);
            }
        });
    }

    private void populateOrderSummary(Order order) {
        textViewShippingName.setText(order.getShippingFullName());
        String address = order.getShippingAddressLine1() +
                (order.getShippingAddressLine2() != null && !order.getShippingAddressLine2().isEmpty() ? "\n" + order.getShippingAddressLine2() : "") +
                "\n" + order.getShippingCity() + ", " + order.getShippingState() + " " + order.getShippingPostalCode() +
                "\n" + order.getShippingCountry();
        textViewShippingAddress.setText(address);
        textViewShippingContact.setText("Phone: " + order.getShippingPhoneNumber());

        textViewPaymentMethod.setText("Payment Method: " + order.getPaymentMethod());
        textViewOrderTotal.setText(String.format(Locale.getDefault(), "Order Total: $%.2f", order.getTotalAmount()));

        if (order.getItems() != null) {
            orderSummaryAdapter.updateOrderItems(order.getItems());
        }
    }

    // Inner class for a simple Order Summary Adapter
    private static class OrderSummaryAdapter extends RecyclerView.Adapter<OrderSummaryAdapter.ViewHolder> {
        private List<OrderItem> items;

        OrderSummaryAdapter(List<OrderItem> items) {
            this.items = items;
        }

        void updateOrderItems(List<OrderItem> newItems) {
            this.items = newItems;
            notifyDataSetChanged(); // For simplicity; use DiffUtil in a real app
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_summary_product, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            OrderItem item = items.get(position);
            holder.textViewProductName.setText(item.getProductName());
            holder.textViewQuantity.setText("Qty: " + item.getQuantity());
            holder.textViewSubtotal.setText(String.format(Locale.getDefault(), "$%.2f", item.getSubtotal()));
            // You could add product image here too if needed, using Glide/Picasso
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            TextView textViewProductName, textViewQuantity, textViewSubtotal;
            // ImageView imageViewProduct; // If you add images

            ViewHolder(View itemView) {
                super(itemView);
                textViewProductName = itemView.findViewById(R.id.textViewSummaryItemName);
                textViewQuantity = itemView.findViewById(R.id.textViewSummaryItemQuantity);
                textViewSubtotal = itemView.findViewById(R.id.textViewSummaryItemSubtotal);
                // imageViewProduct = itemView.findViewById(R.id.imageViewSummaryItem);
            }
        }
    }
}
