package com.example.ecommerceapp.data.repository;

import android.os.Handler;
import android.os.Looper;
import androidx.annotation.NonNull;
import com.example.ecommerceapp.data.model.Category;
import com.example.ecommerceapp.data.model.Product;
import com.example.ecommerceapp.data.model.Review;
import com.example.ecommerceapp.data.model.User;
import com.example.ecommerceapp.data.model.network.ProductListResponse;
import com.example.ecommerceapp.network.ApiService;
import com.example.ecommerceapp.network.RetrofitClient;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;


public class ProductRepository {

    public interface RepositoryCallback<T> {
        void onSuccess(T result);
        void onError(String message);
    }

    private static final int SIMULATED_DELAY_MS = 1000; // Kept for mock review delay
    private Handler handler = new Handler(Looper.getMainLooper()); // Kept for mock review delay

    private final ApiService apiService;
    private final Map<String, List<Review>> mockProductReviews = new HashMap<>(); // productId -> List<Review>
    private final AuthRepository authRepository;

    public ProductRepository() {
        this.apiService = RetrofitClient.getApiService();
        // Assuming AuthRepository might be a singleton or accessible globally for now
        // In a DI setup, this would be injected.
        this.authRepository = new AuthRepository();
        setupMockReviews(); // Reviews are still mocked locally
    }

    // Constructor for dependency injection (preferred, but not used in current setup)
    // public ProductRepository(ApiService apiService, AuthRepository authRepository) {
    //     this.apiService = apiService;
    //     this.authRepository = authRepository;
    //     setupMockReviews();
    // }

    public void getAllProducts(Map<String, String> options, RepositoryCallback<ProductListResponse> callback) {
        apiService.getAllProducts(options).enqueue(new retrofit2.Callback<ProductListResponse>() {
            @Override
            public void onResponse(@NonNull Call<ProductListResponse> call, @NonNull Response<ProductListResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    String errorMsg = "Failed to fetch products.";
                    if (response.code() == 404) errorMsg = "Products not found.";
                    else if (response.code() >= 500) errorMsg = "Server error. Please try again later.";
                    else errorMsg += " Error: " + response.code() + " " + response.message();
                    callback.onError(errorMsg);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ProductListResponse> call, @NonNull Throwable t) {
                callback.onError("Network error: Could not fetch products. Please check connection.");
            }
        });
    }

    public void getProductById(String productId, RepositoryCallback<Product> callback) {
        apiService.getProductById(productId).enqueue(new retrofit2.Callback<Product>() {
            @Override
            public void onResponse(@NonNull Call<Product> call, @NonNull Response<Product> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    String errorMsg = "Failed to fetch product " + productId + ".";
                    if (response.code() == 404) errorMsg = "Product " + productId + " not found.";
                    else if (response.code() >= 500) errorMsg = "Server error. Please try again later.";
                    else errorMsg += " Error: " + response.code() + " " + response.message();
                    callback.onError(errorMsg);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Product> call, @NonNull Throwable t) {
                callback.onError("Network error: Could not fetch product " + productId + ". Please check connection.");
            }
        });
    }

    public void getProductsByCategory(String categoryId, Map<String, String> options, RepositoryCallback<ProductListResponse> callback) {
        apiService.getProductsByCategory(categoryId, options).enqueue(new retrofit2.Callback<ProductListResponse>() {
            @Override
            public void onResponse(@NonNull Call<ProductListResponse> call, @NonNull Response<ProductListResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    String errorMsg = "Failed to fetch products for category " + categoryId + ".";
                    if (response.code() == 404) errorMsg = "No products found for category " + categoryId + ".";
                    else if (response.code() >= 500) errorMsg = "Server error. Please try again later.";
                    else errorMsg += " Error: " + response.code() + " " + response.message();
                    callback.onError(errorMsg);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ProductListResponse> call, @NonNull Throwable t) {
                callback.onError("Network error: Could not fetch products for category " + categoryId + ". Please check connection.");
            }
        });
    }

    public void getCategories(RepositoryCallback<List<Category>> callback) {
        apiService.getAllCategories().enqueue(new retrofit2.Callback<List<Category>>() {
            @Override
            public void onResponse(@NonNull Call<List<Category>> call, @NonNull Response<List<Category>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    String errorMsg = "Failed to fetch categories.";
                    if (response.code() >= 500) errorMsg = "Server error fetching categories. Please try again later.";
                    else errorMsg += " Error: " + response.code() + " " + response.message();
                    callback.onError(errorMsg);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Category>> call, @NonNull Throwable t) {
                callback.onError("Network error: Could not fetch categories. Please check connection.");
            }
        });
    }

    public void searchProducts(String query, RepositoryCallback<ProductListResponse> callback) {
         if (query == null || query.trim().isEmpty()) {
             callback.onError("Search query cannot be empty."); // Or return all products if API supports empty query for all
            return;
        }
        apiService.searchProducts(query).enqueue(new retrofit2.Callback<ProductListResponse>() {
            @Override
            public void onResponse(@NonNull Call<ProductListResponse> call, @NonNull Response<ProductListResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    String errorMsg = "Search failed for query '" + query + "'.";
                    if (response.code() == 404) errorMsg = "No results found for '" + query + "'.";
                    else if (response.code() >= 500) errorMsg = "Server error during search. Please try again later.";
                    else errorMsg += " Error: " + response.code() + " " + response.message();
                    callback.onError(errorMsg);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ProductListResponse> call, @NonNull Throwable t) {
                callback.onError("Network error: Could not perform search for '" + query + "'. Please check connection.");
            }
        });
    }

    public void getProducts(String query, String categoryId, Map<String, String> options, RepositoryCallback<ProductListResponse> callback) {
        Map<String, String> queryOptions = new HashMap<>(options != null ? options : new HashMap<>());
        boolean isSearch = false;
        if (query != null && !query.trim().isEmpty()) {
            queryOptions.put("q", query);
            isSearch = true;
        }
        if (categoryId != null && !categoryId.isEmpty() && !categoryId.equals("all")) {
            queryOptions.put("category", categoryId);
        }

        Call<ProductListResponse> apiCall;

        // Determine the most appropriate endpoint based on parameters
        if (isSearch && (categoryId == null || categoryId.isEmpty() || categoryId.equals("all"))) {
            // Only search query is active (or category is "all") -> Use dedicated search endpoint
            // Note: The API spec GET /products/search only shows 'q'. If it supports other options, ApiService needs update.
            // For now, assuming it only takes 'q', other options in queryOptions might be ignored by this specific endpoint.
            apiCall = apiService.searchProducts(queryOptions.get("q"));
        } else if (!isSearch && categoryId != null && !categoryId.isEmpty() && !categoryId.equals("all")) {
            // Only category filter is active (and not "all") -> Use getProductsByCategory endpoint
            apiCall = apiService.getProductsByCategory(categoryId, options != null ? options : new HashMap<>());
        } else {
            // General case: either no filters, or multiple filters that /products is assumed to handle
            // (e.g., /products?category=X&q=Y or /products?q=Y or /products?category=X)
            apiCall = apiService.getAllProducts(queryOptions);
        }

        apiCall.enqueue(new retrofit2.Callback<ProductListResponse>() {
            @Override
            public void onResponse(@NonNull Call<ProductListResponse> call, @NonNull Response<ProductListResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    String errorMsg = "Failed to fetch products with current filters.";
                    if (response.code() == 404) errorMsg = "No products found matching criteria.";
                    else if (response.code() >= 500) errorMsg = "Server error. Please try again later.";
                    else errorMsg += " Error: " + response.code() + " " + response.message();
                    callback.onError(errorMsg);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ProductListResponse> call, @NonNull Throwable t) {
                callback.onError("Network error: Could not fetch filtered products. Please check connection.");
            }
        });
    }

    private void setupMockReviews() {
        // Mock reviews remain, as API endpoints for reviews are not specified.
        // Product IDs used here should ideally match IDs that your API might return for testing.
        List<Review> reviewsForProd001 = new ArrayList<>();
        reviewsForProd001.add(new Review("revmock_001_1", "prod_001", "user_mock_jane", "Jane M.", 5, "This is a mock review for product 001! Looks great.", new Date(System.currentTimeMillis() - 86400000 * 2)));
        reviewsForProd001.add(new Review("revmock_001_2", "prod_001", "user_mock_sara", "Sara M.", 4, "Good mock product, fast mock delivery.", new Date(System.currentTimeMillis() - 86400000)));
        mockProductReviews.put("prod_001", reviewsForProd001);

        List<Review> reviewsForProd002 = new ArrayList<>();
        reviewsForProd002.add(new Review("revmock_002_1", "prod_002", "user_mock_john", "John M.", 3, "Okay mock product, could be better.", new Date(System.currentTimeMillis() - 86400000 * 3)));
        mockProductReviews.put("prod_002", reviewsForProd002);
    }

    public void getReviewsForProduct(String productId, RepositoryCallback<List<Review>> callback) {
        handler.postDelayed(() -> {
            List<Review> reviews = mockProductReviews.getOrDefault(productId, new ArrayList<>());
            callback.onSuccess(reviews);
        }, SIMULATED_DELAY_MS / 3); // Simulate delay
    }

    public void submitReview(String productId, int rating, String comment, RepositoryCallback<Review> callback) {
        User currentUser = authRepository.getCurrentUser();
        if (currentUser == null || currentUser.getUserId() == null) {
            callback.onError("User not logged in. Please log in to submit a review.");
            return;
        }
        String userId = currentUser.getUserId();
        String username = currentUser.getUsername() != null ? currentUser.getUsername() : "Anonymous";

        List<Review> existingReviews = mockProductReviews.getOrDefault(productId, new ArrayList<>());
        for (Review existingReview : existingReviews) {
            if (existingReview.getUserId().equals(userId)) {
                callback.onError("You have already reviewed this product.");
                return;
            }
        }

        handler.postDelayed(() -> {
            String reviewId = "revmock_" + productId + "_" + userId.substring(0, Math.min(userId.length(),4)) + "_" + System.currentTimeMillis();
            Review newReview = new Review(reviewId, productId, userId, username, rating, comment, new Date());
            List<Review> productReviews = mockProductReviews.computeIfAbsent(productId, k -> new ArrayList<>());
            productReviews.add(newReview);
            callback.onSuccess(newReview);
        }, SIMULATED_DELAY_MS / 2); // Simulate delay
    }
}
