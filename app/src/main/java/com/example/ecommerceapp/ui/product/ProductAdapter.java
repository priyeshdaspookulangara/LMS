package com.example.ecommerceapp.ui.product;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerceapp.R;
import com.example.ecommerceapp.data.model.Product;
// You'll need an image loading library like Glide or Picasso in a real app
// import com.bumptech.glide.Glide;

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
            // if (product.getImageUrlPrimary() != null && !product.getImageUrlPrimary().isEmpty()) {
            //     Glide.with(itemView.getContext())
            //          .load(product.getImageUrlPrimary())
            //          .placeholder(R.drawable.placeholder_image) // Add a placeholder drawable
            //          .error(R.drawable.error_image) // Add an error drawable
            //          .into(imageViewProduct);
            // } else {
            //     imageViewProduct.setImageResource(R.drawable.placeholder_image);
            // }
            // Placeholder for image:
            imageViewProduct.setImageResource(R.mipmap.ic_launcher); // Replace with a proper placeholder

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onProductClick(product);
                }
            });
        }
    }
}
