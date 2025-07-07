package com.example.ecommerceapp.ui.product;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide; // Import Glide
import com.bumptech.glide.request.RequestOptions; // For placeholder/error
import com.example.ecommerceapp.R;
import com.example.ecommerceapp.data.model.Product;

import java.util.List;
import java.util.Locale;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> productList;
    private OnProductClickListener onProductClickListener;
    private Context context;

    public interface OnProductClickListener {
        void onProductClick(Product product);
    }

    public ProductAdapter(List<Product> productList, OnProductClickListener listener) {
        this.productList = productList;
        this.onProductClickListener = listener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.bind(product, onProductClickListener);
    }

    @Override
    public int getItemCount() {
        return productList == null ? 0 : productList.size();
    }

    public void updateProducts(List<Product> newProducts) {
        this.productList.clear();
        if (newProducts != null) {
            this.productList.addAll(newProducts);
        }
        notifyDataSetChanged(); // Consider using DiffUtil for better performance
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewProduct;
        TextView textViewProductName;
        TextView textViewProductPrice;
        TextView textViewProductRating;
        TextView textViewFeaturedTag; // Added

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewProduct = itemView.findViewById(R.id.imageViewProduct);
            textViewProductName = itemView.findViewById(R.id.textViewProductName);
            textViewProductPrice = itemView.findViewById(R.id.textViewProductPrice);
            textViewProductRating = itemView.findViewById(R.id.textViewProductRating);
            textViewFeaturedTag = itemView.findViewById(R.id.textViewFeaturedTag); // Added
        }

        public void bind(final Product product, final OnProductClickListener listener) {
            textViewProductName.setText(product.getName());
            textViewProductPrice.setText(String.format(Locale.getDefault(), "$%.2f", product.getPrice()));

            if (product.getAverageRating() > 0) {
                textViewProductRating.setText(String.format(Locale.getDefault(), "%.1f", product.getAverageRating()));
                textViewProductRating.setVisibility(View.VISIBLE);
            } else {
                textViewProductRating.setVisibility(View.GONE);
            }

            if (product.isFeatured()) { // Added logic for featured tag
                textViewFeaturedTag.setVisibility(View.VISIBLE);
            } else {
                textViewFeaturedTag.setVisibility(View.GONE);
            }

            // Load image using Glide or Picasso
            // For now, using a placeholder if imageUrlPrimary is null or empty
            if (product.getImageUrl() != null && !product.getImageUrl().isEmpty()) { // Use getImageUrl()
                Glide.with(itemView.getContext())
                     .load(product.getImageUrl()) // Use getImageUrl()
                     .apply(new RequestOptions()
                             .placeholder(R.drawable.ic_placeholder_image)
                             .error(R.drawable.ic_error_image))
                     .into(imageViewProduct);
            } else {
                // Set placeholder if URL is null or empty
                Glide.with(itemView.getContext())
                     .load(R.drawable.ic_placeholder_image) // Load placeholder directly
                     .into(imageViewProduct);
            }
            // Placeholder for image:
            // imageViewProduct.setImageResource(R.mipmap.ic_launcher); // Replace with a proper placeholder

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onProductClick(product);
                }
            });
        }
    }
}
