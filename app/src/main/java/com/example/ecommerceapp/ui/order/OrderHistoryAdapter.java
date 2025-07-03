package com.example.ecommerceapp.ui.order;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerceapp.R;
import com.example.ecommerceapp.data.model.Order;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class OrderHistoryAdapter extends ListAdapter<Order, OrderHistoryAdapter.OrderViewHolder> {

    private OnOrderClickListener listener;
    private Context context; // For accessing colors

    public interface OnOrderClickListener {
        void onOrderClick(Order order);
    }

    public OrderHistoryAdapter(List<Order> initialList, OnOrderClickListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
        submitList(initialList);
    }

    public void updateOrders(List<Order> newOrders) {
        submitList(newOrders);
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_order_history, parent, false);
        return new OrderViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = getItem(position);
        holder.bind(order, context);
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView textViewOrderId, textViewOrderDate, textViewOrderTotal, textViewOrderStatus, textViewItemCount;
        OnOrderClickListener clickListener;

        public OrderViewHolder(@NonNull View itemView, OnOrderClickListener listener) {
            super(itemView);
            this.clickListener = listener;
            textViewOrderId = itemView.findViewById(R.id.textViewOrderHistoryId);
            textViewOrderDate = itemView.findViewById(R.id.textViewOrderHistoryDate);
            textViewOrderTotal = itemView.findViewById(R.id.textViewOrderHistoryTotal);
            textViewOrderStatus = itemView.findViewById(R.id.textViewOrderHistoryStatus);
            textViewItemCount = itemView.findViewById(R.id.textViewOrderHistoryItemCount);
        }

        public void bind(final Order order, Context context) {
            textViewOrderId.setText("Order ID: " + order.getOrderId());

            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy, hh:mm a", Locale.getDefault());
            if (order.getOrderDate() != null) {
                textViewOrderDate.setText("Date: " + dateFormat.format(order.getOrderDate()));
            } else {
                textViewOrderDate.setText("Date: N/A");
            }

            textViewOrderTotal.setText(String.format(Locale.getDefault(), "Total: $%.2f", order.getTotalAmount()));

            if(order.getItems() != null) {
                int itemCount = order.getItems().stream().mapToInt(com.example.ecommerceapp.data.model.OrderItem::getQuantity).sum();
                textViewItemCount.setText(String.format(Locale.getDefault(), "%d item%s", itemCount, itemCount > 1 ? "s" : ""));
            } else {
                textViewItemCount.setText("0 items");
            }


            textViewOrderStatus.setText(order.getStatus());
            // Set status color (example)
            String status = order.getStatus() != null ? order.getStatus().toLowerCase() : "";
            int statusColor = ContextCompat.getColor(context, R.color.default_status_color); // Define a default color
            switch (status) {
                case "pending":
                case "processing":
                    statusColor = ContextCompat.getColor(context, R.color.pending_status_color); // Define in colors.xml
                    break;
                case "shipped":
                    statusColor = ContextCompat.getColor(context, R.color.shipped_status_color); // Define in colors.xml
                    break;
                case "delivered":
                    statusColor = ContextCompat.getColor(context, R.color.delivered_status_color); // Define in colors.xml
                    break;
                case "cancelled":
                    statusColor = ContextCompat.getColor(context, R.color.cancelled_status_color); // Define in colors.xml
                    break;
            }
            textViewOrderStatus.setTextColor(statusColor);


            itemView.setOnClickListener(v -> {
                if (clickListener != null) {
                    clickListener.onOrderClick(order);
                }
            });
        }
    }

    private static final DiffUtil.ItemCallback<Order> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Order>() {
                @Override
                public boolean areItemsTheSame(@NonNull Order oldItem, @NonNull Order newItem) {
                    return oldItem.getOrderId().equals(newItem.getOrderId());
                }

                @Override
                public boolean areContentsTheSame(@NonNull Order oldItem, @NonNull Order newItem) {
                    // Check for changes in relevant fields
                    return oldItem.getStatus().equals(newItem.getStatus()) &&
                           oldItem.getTotalAmount() == newItem.getTotalAmount() &&
                           (oldItem.getOrderDate() != null && newItem.getOrderDate() != null ? oldItem.getOrderDate().equals(newItem.getOrderDate()) : oldItem.getOrderDate() == newItem.getOrderDate()) &&
                           (oldItem.getItems() != null && newItem.getItems() != null ? oldItem.getItems().size() == newItem.getItems().size() : oldItem.getItems() == newItem.getItems());
                }
            };
}
