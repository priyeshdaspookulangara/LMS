package com.example.ecommerceapp.ui.cart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

// import com.bumptech.glide.Glide; // For image loading
import com.example.ecommerceapp.R;
import com.example.ecommerceapp.data.model.CartItem;
import com.example.ecommerceapp.data.model.Product;

import java.util.List;
import java.util.Locale;

public class CartAdapter extends ListAdapter<CartItem, CartAdapter.CartItemViewHolder> {

    private CartItemInteractionListener interactionListener;
    private Context context; // Store context if needed for Glide or other operations

    public interface CartItemInteractionListener {
        void onIncreaseQuantity(CartItem item);
        void onDecreaseQuantity(CartItem item);
        void onRemoveItem(CartItem item);
        // Potentially: void onProductClicked(Product product);
    }

    public CartAdapter(List<CartItem> initialList, CartItemInteractionListener listener) {
        super(DIFF_CALLBACK);
        this.interactionListener = listener;
        submitList(initialList); // Submit initial list
    }

    // Convenience method to update the list
    public void updateCartItems(List<CartItem> newItems) {
        submitList(newItems);
    }


    @NonNull
    @Override
    public CartItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart_product, parent, false);
        return new CartItemViewHolder(view, interactionListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemViewHolder holder, int position) {
        CartItem cartItem = getItem(position);
        holder.bind(cartItem, context);
    }

    static class CartItemViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewCartProduct;
        TextView textViewCartProductName;
        TextView textViewCartProductPrice;
        TextView textViewCartItemQuantity;
        TextView textViewCartItemSubtotal;
        ImageButton buttonDecreaseQuantity;
        ImageButton buttonIncreaseQuantity;
        ImageButton imageButtonRemoveItem;
        CartItemInteractionListener listener;

        public CartItemViewHolder(@NonNull View itemView, CartItemInteractionListener listener) {
            super(itemView);
            this.listener = listener;
            imageViewCartProduct = itemView.findViewById(R.id.imageViewCartProduct);
            textViewCartProductName = itemView.findViewById(R.id.textViewCartProductName);
            textViewCartProductPrice = itemView.findViewById(R.id.textViewCartProductPrice);
            textViewCartItemQuantity = itemView.findViewById(R.id.textViewCartItemQuantity);
            textViewCartItemSubtotal = itemView.findViewById(R.id.textViewCartItemSubtotal);
            buttonDecreaseQuantity = itemView.findViewById(R.id.buttonDecreaseQuantity);
            buttonIncreaseQuantity = itemView.findViewById(R.id.buttonIncreaseQuantity);
            imageButtonRemoveItem = itemView.findViewById(R.id.imageButtonRemoveItem);
        }

        public void bind(final CartItem cartItem, Context context) {
            Product product = cartItem.getProduct();
            textViewCartProductName.setText(product.getName());
            textViewCartProductPrice.setText(String.format(Locale.getDefault(), "$%.2f", product.getPrice()));
            textViewCartItemQuantity.setText(String.valueOf(cartItem.getQuantity()));
            textViewCartItemSubtotal.setText(String.format(Locale.getDefault(), "Subtotal: $%.2f", cartItem.getSubtotal()));

            // Load image using Glide or Picasso
            // if (product.getImageUrlPrimary() != null && !product.getImageUrlPrimary().isEmpty()) {
            // Glide.with(context) // Use context passed to bind or itemView.getContext()
            // .load(product.getImageUrlPrimary())
            // .placeholder(R.drawable.placeholder_image)
            // .error(R.drawable.error_image)
            // .into(imageViewCartProduct);
            // } else {
            // imageViewCartProduct.setImageResource(R.drawable.placeholder_image);
            // }
            imageViewCartProduct.setImageResource(R.mipmap.ic_launcher); // Placeholder

            buttonIncreaseQuantity.setOnClickListener(v -> {
                if (listener != null) listener.onIncreaseQuantity(cartItem);
            });

            buttonDecreaseQuantity.setOnClickListener(v -> {
                if (listener != null) listener.onDecreaseQuantity(cartItem);
            });

            imageButtonRemoveItem.setOnClickListener(v -> {
                if (listener != null) listener.onRemoveItem(cartItem);
            });

            // itemView.setOnClickListener(v -> {
            // if (listener != null) listener.onProductClicked(product);
            // });
        }
    }

    private static final DiffUtil.ItemCallback<CartItem> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<CartItem>() {
                @Override
                public boolean areItemsTheSame(@NonNull CartItem oldItem, @NonNull CartItem newItem) {
                    // Product ID is unique.
                    return oldItem.getProduct().getProductId().equals(newItem.getProduct().getProductId());
                }

                @Override
                public boolean areContentsTheSame(@NonNull CartItem oldItem, @NonNull CartItem newItem) {
                    // Check if quantity and product details (like price, if it can change) are the same.
                    return oldItem.getQuantity() == newItem.getQuantity() &&
                           oldItem.getProduct().getPrice() == newItem.getProduct().getPrice() && // Example
                           oldItem.getProduct().getName().equals(newItem.getProduct().getName());
                }
            };
}
