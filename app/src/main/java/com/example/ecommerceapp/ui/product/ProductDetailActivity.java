package com.example.ecommerceapp.ui.product;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
// import com.bumptech.glide.Glide; // For image loading

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.ecommerceapp.R;
import com.example.ecommerceapp.data.model.Product;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.Locale;

public class ProductDetailActivity extends AppCompatActivity {

    public static final String EXTRA_PRODUCT_ID = "extra_product_id";

    private ImageView imageViewProductDetail;
    private TextView textViewProductNameDetail;
    private TextView textViewProductPriceDetail;
    private TextView textViewProductDescriptionDetail;
    private TextView textViewProductBrand;
    private TextView textViewProductMaterial;
    private TextView textViewProductColor;
    private TextView textViewStockQuantity;
    private TextView textViewAverageRatingText; // For text like "Average Rating: 4.5 (10 reviews)"
    private RatingBar ratingBarAverageIndicator; // For displaying stars
    private ChipGroup chipGroupSizes;
    private Button buttonAddToCart, buttonWriteReview;
    private ProgressBar progressBarDetail;
    private RecyclerView recyclerViewReviews;
    private ReviewAdapter reviewAdapter;
    private TextView textViewNoReviews;

    private ProductViewModel productViewModel;
    private CartViewModel cartViewModel; // Added CartViewModel
    private Product currentProduct; // To store the fetched product
    private String productId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Product Details");
        }

        imageViewProductDetail = findViewById(R.id.imageViewProductDetail);
        textViewProductNameDetail = findViewById(R.id.textViewProductNameDetail);
        textViewProductPriceDetail = findViewById(R.id.textViewProductPriceDetail);
        textViewProductDescriptionDetail = findViewById(R.id.textViewProductDescriptionDetail);
        textViewProductBrand = findViewById(R.id.textViewProductBrand);
        textViewProductMaterial = findViewById(R.id.textViewProductMaterial);
        textViewProductColor = findViewById(R.id.textViewProductColor);
        textViewStockQuantity = findViewById(R.id.textViewStockQuantity);
        chipGroupSizes = findViewById(R.id.chipGroupSizes);
        buttonAddToCart = findViewById(R.id.buttonAddToCart);
        progressBarDetail = findViewById(R.id.progressBarProductDetail);
        buttonWriteReview = findViewById(R.id.buttonWriteReview);
        recyclerViewReviews = findViewById(R.id.recyclerViewReviews);
        textViewNoReviews = findViewById(R.id.textViewNoReviews);
        textViewAverageRatingText = findViewById(R.id.textViewAverageRating);
        ratingBarAverageIndicator = findViewById(R.id.ratingBarAverage);


        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        cartViewModel = new ViewModelProvider(this).get(CartViewModel.class); // Initialize CartViewModel

        if (getIntent() != null && getIntent().hasExtra(EXTRA_PRODUCT_ID)) {
            productId = getIntent().getStringExtra(EXTRA_PRODUCT_ID);
        } else {
            Toast.makeText(this, "Product ID not found.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        observeViewModel();
        setupRecyclerView();
        progressBarDetail.setVisibility(View.VISIBLE);
        productViewModel.fetchProductById(productId);
        productViewModel.fetchReviewsForProduct(productId); // Fetch reviews when product ID is known

        buttonAddToCart.setOnClickListener(v -> {
            if (currentProduct != null) {
                // TODO: Handle quantity selection if available (e.g., from a NumberPicker)
                int quantityToAdd = 1; // Default to 1 for now
                if (currentProduct.getStockQuantity() >= quantityToAdd) {
                    cartViewModel.addProductToCart(currentProduct, quantityToAdd);
                    Toast.makeText(ProductDetailActivity.this, currentProduct.getName() + " added to cart", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ProductDetailActivity.this, "Insufficient stock for " + currentProduct.getName(), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(ProductDetailActivity.this, "Product details not loaded yet.", Toast.LENGTH_SHORT).show();
            }
        });

        buttonWriteReview.setOnClickListener(v -> {
            if (currentProduct != null) {
                SubmitReviewDialogFragment dialogFragment = SubmitReviewDialogFragment.newInstance(currentProduct.getProductId());
                dialogFragment.show(getSupportFragmentManager(), "SubmitReviewDialog");
            } else {
                Toast.makeText(this, "Product details not loaded yet.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupRecyclerView() {
        reviewAdapter = new ReviewAdapter();
        recyclerViewReviews.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewReviews.setAdapter(reviewAdapter);
        recyclerViewReviews.setNestedScrollingEnabled(false); // If inside NestedScrollView
    }


    private void observeViewModel() {
        productViewModel.getSelectedProduct().observe(this, product -> {
            progressBarDetail.setVisibility(View.GONE); // Might be handled by isLoading LiveData
            if (product != null) {
                currentProduct = product;
                populateProductDetails(product);
            } else {
                // Error handled by errorMessage LiveData
            }
        });

        productViewModel.getProductReviews().observe(this, reviews -> {
            if (reviews != null && !reviews.isEmpty()) {
                reviewAdapter.updateReviews(reviews);
                recyclerViewReviews.setVisibility(View.VISIBLE);
                textViewNoReviews.setVisibility(View.GONE);
            } else {
                recyclerViewReviews.setVisibility(View.GONE);
                textViewNoReviews.setVisibility(View.VISIBLE);
            }
             // Update average rating text based on potentially new review count
            if (currentProduct != null) updateAverageRatingDisplay(currentProduct, reviews != null ? reviews.size() : 0);
        });

        productViewModel.getReviewSubmissionResult().observe(this, success -> {
            if (success != null) {
                if (success) {
                    Toast.makeText(this, "Review submitted successfully!", Toast.LENGTH_SHORT).show();
                    // Reviews list and product details (avg rating) will be refreshed by ViewModel
                } else {
                    // Error message for submission failure will be shown by getErrorMessage observer
                }
            }
        });

        productViewModel.getErrorMessage().observe(this, errorMsg -> {
            progressBarDetail.setVisibility(View.GONE);
            if (errorMsg != null && !errorMsg.isEmpty()) {
                Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void populateProductDetails(Product product) {
        textViewProductNameDetail.setText(product.getName());
        textViewProductPriceDetail.setText(String.format(Locale.getDefault(), "$%.2f", product.getPrice()));
        textViewProductDescriptionDetail.setText(product.getDescription());

        setTextOrHide(textViewProductBrand, "Brand: ", product.getBrand());
        setTextOrHide(textViewProductMaterial, "Material: ", product.getMaterial());
        setTextOrHide(textViewProductColor, "Color: ", product.getColor());

        updateAverageRatingDisplay(product, productViewModel.getProductReviews().getValue() != null ? productViewModel.getProductReviews().getValue().size() : 0);


        textViewStockQuantity.setText(String.format(Locale.getDefault(),"Stock: %d", product.getStockQuantity()));
        if (product.getStockQuantity() > 0) {
            buttonAddToCart.setEnabled(true);
            buttonAddToCart.setText("Add to Cart");
        } else {
            buttonAddToCart.setEnabled(false);
            buttonAddToCart.setText("Out of Stock");
        }

        // Populate sizes
        chipGroupSizes.removeAllViews();
        if (product.getSizesAvailable() != null && !product.getSizesAvailable().isEmpty()) {
            for (String size : product.getSizesAvailable()) {
                Chip chip = new Chip(this);
                chip.setText(size);
                chip.setCheckable(true); // Allow selection if needed for cart
                chipGroupSizes.addView(chip);
            }
            findViewById(R.id.textViewSizesLabel).setVisibility(View.VISIBLE);
            chipGroupSizes.setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.textViewSizesLabel).setVisibility(View.GONE);
            chipGroupSizes.setVisibility(View.GONE);
        }

        // Load image using Glide or Picasso
        // if (product.getImageUrlPrimary() != null && !product.getImageUrlPrimary().isEmpty()) {
        // Glide.with(this)
        // .load(product.getImageUrlPrimary())
        // .placeholder(R.drawable.placeholder_image)
        // .error(R.drawable.error_image)
        // .into(imageViewProductDetail);
        // } else {
        // imageViewProductDetail.setImageResource(R.drawable.placeholder_image);
        // }
        imageViewProductDetail.setImageResource(R.mipmap.ic_launcher); // Placeholder

        // Update toolbar title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(product.getName());
        }
    }

    private void updateAverageRatingDisplay(Product product, int reviewCount) {
        if (product.getAverageRating() > 0) {
            ratingBarAverageIndicator.setRating((float) product.getAverageRating());
            ratingBarAverageIndicator.setVisibility(View.VISIBLE);
            textViewAverageRatingText.setText(String.format(Locale.getDefault(), "Avg Rating: %.1f (%d review%s)",
                    product.getAverageRating(), reviewCount, reviewCount == 1 ? "" : "s"));
            textViewAverageRatingText.setVisibility(View.VISIBLE);
        } else {
            ratingBarAverageIndicator.setVisibility(View.GONE);
            textViewAverageRatingText.setText("No ratings yet.");
            // textViewAverageRatingText.setVisibility(View.GONE); // Or show "No ratings yet"
        }
    }


    private void setTextOrHide(TextView textView, String prefix, String value) {
        if (value != null && !value.isEmpty()) {
            textView.setText(prefix + value);
            textView.setVisibility(View.VISIBLE);
        } else {
            textView.setVisibility(View.GONE);
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
