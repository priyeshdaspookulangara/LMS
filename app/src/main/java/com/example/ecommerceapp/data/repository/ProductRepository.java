package com.example.ecommerceapp.data.repository;

import android.os.Handler;
import android.os.Looper;

import com.example.ecommerceapp.data.model.Category;
import com.example.ecommerceapp.data.model.Product;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Simulated repository for products and categories.
 * In a real app, this would interact with a backend API.
 */
public class ProductRepository {

    public interface RepositoryCallback<T> {
        void onSuccess(T result);
        void onError(String message);
    }

    private static final int SIMULATED_DELAY_MS = 1000;
    private Handler handler = new Handler(Looper.getMainLooper());

    // --- Simulated Data Store ---
    private final Map<String, Product> mockProducts = new HashMap<>();
    private final Map<String, Category> mockCategories = new HashMap<>();

    public ProductRepository() {
        // Initialize with some mock data
        this.authRepository = new AuthRepository(); // Or getInstance() if singleton
        setupMockData();
        setupMockReviews(); // Initialize some mock reviews
    }

    private void setupMockData() {
        // Categories
        Category cat1 = new Category("cat_1", "Stitched Cloths", "Ready-to-wear stitched garments", null);
        Category cat2 = new Category("cat_2", "Garments (Unstitched)", "Fabric materials for custom stitching", null);
        Category cat3 = new Category("cat_3", "Kurtis", "Women's kurtis", "cat_1");
        Category cat4 = new Category("cat_4", "Shirts", "Men's and Women's Shirts", "cat_1");
        Category cat5 = new Category("cat_5", "Cotton Fabric", "Cotton based unstitched material", "cat_2");

        mockCategories.put(cat1.getCategoryId(), cat1);
        mockCategories.put(cat2.getCategoryId(), cat2);
        mockCategories.put(cat3.getCategoryId(), cat3);
        mockCategories.put(cat4.getCategoryId(), cat4);
        mockCategories.put(cat5.getCategoryId(), cat5);

        // Products
        mockProducts.put("prod_1", new Product("prod_1", "Elegant Floral Kurti", "Beautifully stitched kurti with floral patterns.",
                25.99, "SKU001", cat3.getCategoryId(), "EthnicWonders", "Cotton Blend", "Pink",
                Arrays.asList("S", "M", "L", "XL"), 20,
                "https://via.placeholder.com/300/FFC0CB/000000?Text=Kurti", new ArrayList<>(), true, 4.5));

        mockProducts.put("prod_2", new Product("prod_2", "Men's Casual Shirt", "Comfortable cotton casual shirt.",
                19.50, "SKU002", cat4.getCategoryId(), "UrbanStyle", "Cotton", "Blue",
                Arrays.asList("M", "L", "XL", "XXL"), 30,
                "https://via.placeholder.com/300/ADD8E6/000000?Text=Shirt", new ArrayList<>(), false, 4.2));

        mockProducts.put("prod_3", new Product("prod_3", "Pure Silk Fabric", "Luxurious pure silk unstitched fabric (5m).",
                75.00, "SKU003", cat2.getCategoryId(), "SilkTreasures", "Silk", "Gold",
                new ArrayList<>(), // No specific sizes for fabric by piece
                10, "https://via.placeholder.com/300/FFD700/000000?Text=SilkFabric", new ArrayList<>(), true, 4.8));

        mockProducts.put("prod_4", new Product("prod_4", "Designer Anarkali Suit", "Heavy embroidered Anarkali suit, stitched.",
                120.00, "SKU004", cat1.getCategoryId(), "RoyalAttire", "Georgette", "Red",
                Arrays.asList("M", "L"), 15,
                "https://via.placeholder.com/300/FF0000/FFFFFF?Text=Anarkali", new ArrayList<>(), true, 4.7));

        mockProducts.put("prod_5", new Product("prod_5", "Basic Cotton T-Shirt", "Plain cotton t-shirt, various colors.",
                9.99, "SKU005", cat4.getCategoryId(), "EverydayBasic", "Cotton", "White",
                Arrays.asList("S", "M", "L", "XL", "XXL"), 50,
                "https://via.placeholder.com/300/FFFFFF/000000?Text=T-Shirt", new ArrayList<>(), false, 4.0));

        mockProducts.put("prod_6", new Product("prod_6", "Linen Summer Shirt", "Lightweight linen shirt for summer.",
                22.99, "SKU006", cat4.getCategoryId(), "SummerCool", "Linen", "Light Blue",
                Arrays.asList("M", "L", "XL"), 25,
                "https://via.placeholder.com/300/B0E0E6/000000?Text=LinenShirt", new ArrayList<>(), true, 4.3));
    }

    public void getAllProducts(RepositoryCallback<List<Product>> callback) {
        handler.postDelayed(() -> {
            if (mockProducts.isEmpty()) {
                 // callback.onError("No products available at the moment.");
                 // Return empty list instead of error for this case
                 callback.onSuccess(new ArrayList<>(mockProducts.values()));
            } else {
                callback.onSuccess(new ArrayList<>(mockProducts.values()));
            }
        }, SIMULATED_DELAY_MS);
    }

    public void getProductById(String productId, RepositoryCallback<Product> callback) {
        handler.postDelayed(() -> {
            Product product = mockProducts.get(productId);
            if (product != null) {
                callback.onSuccess(product);
            } else {
                callback.onError("Product not found.");
            }
        }, SIMULATED_DELAY_MS / 2); // Faster for single item fetch
    }

    public void getProductsByCategory(String categoryId, RepositoryCallback<List<Product>> callback) {
        handler.postDelayed(() -> {
            List<Product> categoryProducts = mockProducts.values().stream()
                    .filter(p -> p.getCategoryId().equals(categoryId) || isSubCategory(p.getCategoryId(), categoryId))
                    .collect(Collectors.toList());
            if (!categoryProducts.isEmpty()) {
                callback.onSuccess(categoryProducts);
            } else {
                // callback.onError("No products found in this category.");
                // Return empty list instead of error
                callback.onSuccess(new ArrayList<>());
            }
        }, SIMULATED_DELAY_MS);
    }

    private boolean isSubCategory(String childCategoryId, String parentCategoryId) {
        Category current = mockCategories.get(childCategoryId);
        while (current != null && current.getParentCategoryId() != null) {
            if (current.getParentCategoryId().equals(parentCategoryId)) {
                return true;
            }
            current = mockCategories.get(current.getParentCategoryId());
        }
        return false;
    }

    public void getCategories(RepositoryCallback<List<Category>> callback) {
        handler.postDelayed(() -> {
            if (mockCategories.isEmpty()) {
                // callback.onError("No categories available.");
                callback.onSuccess(new ArrayList<>(mockCategories.values()));
            } else {
                callback.onSuccess(new ArrayList<>(mockCategories.values()));
            }
        }, SIMULATED_DELAY_MS / 2);
    }

    public void searchProducts(String query, RepositoryCallback<List<Product>> callback) {
        handler.postDelayed(() -> {
            if (query == null || query.trim().isEmpty()) {
                callback.onSuccess(new ArrayList<>(mockProducts.values())); // Return all if query is empty
                return;
            }
            String lowerCaseQuery = query.toLowerCase();
            List<Product> searchResults = mockProducts.values().stream()
                    .filter(product -> (product.getName() != null && product.getName().toLowerCase().contains(lowerCaseQuery)) ||
                                       (product.getDescription() != null && product.getDescription().toLowerCase().contains(lowerCaseQuery)) ||
                                       (product.getBrand() != null && product.getBrand().toLowerCase().contains(lowerCaseQuery)) ||
                                       (product.getSku() != null && product.getSku().toLowerCase().contains(lowerCaseQuery)) ||
                                       (product.getMaterial() != null && product.getMaterial().toLowerCase().contains(lowerCaseQuery)) ||
                                       (product.getColor() != null && product.getColor().toLowerCase().contains(lowerCaseQuery)))
                    .collect(Collectors.toList());
            callback.onSuccess(searchResults);
        }, SIMULATED_DELAY_MS / 2); // Faster simulation for search
    }

    private final Map<String, List<Review>> mockProductReviews = new HashMap<>(); // productId -> List<Review>
    private final AuthRepository authRepository; // To get current user for submitting reviews

    // Combined method for search and category filtering
    public void getProducts(String query, String categoryId, RepositoryCallback<List<Product>> callback) {
        handler.postDelayed(() -> {
            List<Product> results = new ArrayList<>(mockProducts.values());

            // Filter by category first if categoryId is provided
            if (categoryId != null && !categoryId.isEmpty() && !categoryId.equals("all")) { // "all" means no category filter
                results = results.stream()
                        .filter(p -> p.getCategoryId().equals(categoryId) || isSubCategory(p.getCategoryId(), categoryId))
                        .collect(Collectors.toList());
            }

            // Then filter by search query if query is provided
            if (query != null && !query.trim().isEmpty()) {
                String lowerCaseQuery = query.toLowerCase();
                results = results.stream()
                        .filter(product -> (product.getName() != null && product.getName().toLowerCase().contains(lowerCaseQuery)) ||
                                           (product.getDescription() != null && product.getDescription().toLowerCase().contains(lowerCaseQuery)) ||
                                           (product.getBrand() != null && product.getBrand().toLowerCase().contains(lowerCaseQuery)) ||
                                           (product.getSku() != null && product.getSku().toLowerCase().contains(lowerCaseQuery)))
                        .collect(Collectors.toList());
            }

            // if (results.isEmpty()){
            //     callback.onError("No products match your criteria.");
            // } else {
                callback.onSuccess(results);
            // }
        }, SIMULATED_DELAY_MS);
    }

    private void setupMockReviews() {
        // Reviews for prod_1 (Elegant Floral Kurti)
        List<Review> reviewsForProd1 = new ArrayList<>();
        reviewsForProd1.add(new Review("rev1_1", "prod_1", "user_jane", "Jane D.", 5, "Absolutely beautiful kurti! Fits perfectly.", new Date(System.currentTimeMillis() - 86400000 * 2))); // 2 days ago
        reviewsForProd1.add(new Review("rev1_2", "prod_1", "user_sara", "Sara K.", 4, "Good quality material, color is vibrant.", new Date(System.currentTimeMillis() - 86400000))); // 1 day ago
        mockProductReviews.put("prod_1", reviewsForProd1);
        updateProductAverageRating("prod_1");


        // Reviews for prod_2 (Men's Casual Shirt)
        List<Review> reviewsForProd2 = new ArrayList<>();
        reviewsForProd2.add(new Review("rev2_1", "prod_2", "user_john", "John B.", 5, "Great shirt, very comfortable.", new Date(System.currentTimeMillis() - 86400000 * 3)));
        mockProductReviews.put("prod_2", reviewsForProd2);
        updateProductAverageRating("prod_2");

        // No reviews for prod_3 initially
    }

    public void getReviewsForProduct(String productId, RepositoryCallback<List<Review>> callback) {
        handler.postDelayed(() -> {
            List<Review> reviews = mockProductReviews.getOrDefault(productId, new ArrayList<>());
            callback.onSuccess(reviews);
        }, SIMULATED_DELAY_MS / 3);
    }

    public void submitReview(String productId, int rating, String comment, RepositoryCallback<Review> callback) {
        User currentUser = authRepository.getCurrentUser();
        if (currentUser == null) {
            callback.onError("User not logged in. Please log in to submit a review.");
            return;
        }
        String userId = currentUser.getUserId();
        String username = currentUser.getUsername(); // Or FullName

        // Check if user already reviewed this product
        List<Review> existingReviews = mockProductReviews.getOrDefault(productId, new ArrayList<>());
        for (Review existingReview : existingReviews) {
            if (existingReview.getUserId().equals(userId)) {
                callback.onError("You have already reviewed this product.");
                // Optionally, allow editing:
                // existingReview.setRating(rating);
                // existingReview.setComment(comment);
                // existingReview.setReviewDate(new Date());
                // updateProductAverageRating(productId);
                // callback.onSuccess(existingReview);
                return;
            }
        }

        handler.postDelayed(() -> {
            Product product = mockProducts.get(productId);
            if (product == null) {
                callback.onError("Product not found.");
                return;
            }

            String reviewId = "rev_" + productId + "_" + userId + "_" + System.currentTimeMillis();
            Review newReview = new Review(reviewId, productId, userId, username, rating, comment, new Date());

            List<Review> productReviews = mockProductReviews.computeIfAbsent(productId, k -> new ArrayList<>());
            productReviews.add(newReview);

            updateProductAverageRating(productId);
            callback.onSuccess(newReview);

        }, SIMULATED_DELAY_MS / 2);
    }

    private void updateProductAverageRating(String productId) {
        Product product = mockProducts.get(productId);
        List<Review> reviews = mockProductReviews.get(productId);

        if (product != null && reviews != null && !reviews.isEmpty()) {
            double sum = 0;
            for (Review review : reviews) {
                sum += review.getRating();
            }
            double average = sum / reviews.size();
            product.setAverageRating(average);
        } else if (product != null) {
            product.setAverageRating(0.0); // Reset if no reviews
        }
    }
}
