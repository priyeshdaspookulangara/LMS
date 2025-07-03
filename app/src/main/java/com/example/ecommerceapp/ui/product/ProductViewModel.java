package com.example.ecommerceapp.ui.product;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ecommerceapp.data.model.Category;
import com.example.ecommerceapp.data.model.Product;
import com.example.ecommerceapp.data.repository.ProductRepository; // To be created

import java.util.List;

public class ProductViewModel extends ViewModel {

    private ProductRepository productRepository;
    private MutableLiveData<List<Product>> products = new MutableLiveData<>();
    private MutableLiveData<Product> selectedProduct = new MutableLiveData<>();
    private MutableLiveData<List<Category>> categories = new MutableLiveData<>();
    private MutableLiveData<List<Review>> productReviews = new MutableLiveData<>(); // For reviews
    private MutableLiveData<Boolean> reviewSubmissionResult = new MutableLiveData<>(); // For review submission status
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public ProductViewModel() {
        // In a real app, inject this with Dagger/Hilt
        this.productRepository = new ProductRepository();
    }

    public LiveData<List<Product>> getProducts() {
        return products;
    }

    public LiveData<Product> getSelectedProduct() {
        return selectedProduct;
    }

    public LiveData<List<Category>> getCategories() {
        return categories;
    }

    public LiveData<List<Review>> getProductReviews() {
        return productReviews;
    }

    public LiveData<Boolean> getReviewSubmissionResult() {
        return reviewSubmissionResult;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void fetchAllProducts() {
        productRepository.getAllProducts(new ProductRepository.RepositoryCallback<List<Product>>() {
            @Override
            public void onSuccess(List<Product> result) {
                products.setValue(result);
            }

            @Override
            public void onError(String message) {
                errorMessage.setValue(message);
                 products.setValue(null); // Clear previous data on error
            }
        });
    }

    public void fetchProductsByCategory(String categoryId) {
        productRepository.getProductsByCategory(categoryId, new ProductRepository.RepositoryCallback<List<Product>>() {
            @Override
            public void onSuccess(List<Product> result) {
                products.setValue(result);
            }

            @Override
            public void onError(String message) {
                errorMessage.setValue(message);
                products.setValue(null);
            }
        });
    }

    public void fetchProductById(String productId) {
        productRepository.getProductById(productId, new ProductRepository.RepositoryCallback<Product>() {
            @Override
            public void onSuccess(Product result) {
                selectedProduct.setValue(result);
            }

            @Override
            public void onError(String message) {
                errorMessage.setValue(message);
                selectedProduct.setValue(null);
            }
        });
    }

    public void fetchCategories() {
        productRepository.getCategories(new ProductRepository.RepositoryCallback<List<Category>>() {
            @Override
            public void onSuccess(List<Category> result) {
                categories.setValue(result);
            }

            @Override
            public void onError(String message) {
                errorMessage.setValue(message);
                categories.setValue(null);
            }
        });
    }

    public void searchProducts(String query) {
        errorMessage.setValue(null); // Clear previous errors
        productRepository.searchProducts(query, new ProductRepository.RepositoryCallback<List<Product>>() {
            @Override
            public void onSuccess(List<Product> result) {
                products.setValue(result);
            }

            @Override
            public void onError(String message) {
                errorMessage.setValue(message);
                products.setValue(new ArrayList<>()); // Set empty list on error
            }
        });
    }

    // Overload or new method for combined search and category filter
    public void fetchProducts(String query, String categoryId) {
        errorMessage.setValue(null);
        productRepository.getProducts(query, categoryId, new ProductRepository.RepositoryCallback<List<Product>>() {
            @Override
            public void onSuccess(List<Product> result) {
                products.setValue(result);
            }

            @Override
            public void onError(String message) {
                errorMessage.setValue(message);
                products.setValue(new ArrayList<>());
            }
        });
    }

    public void fetchReviewsForProduct(String productId) {
        errorMessage.setValue(null);
        reviewSubmissionResult.setValue(null); // Reset submission status
        productRepository.getReviewsForProduct(productId, new ProductRepository.RepositoryCallback<List<Review>>() {
            @Override
            public void onSuccess(List<Review> result) {
                productReviews.setValue(result);
            }

            @Override
            public void onError(String message) {
                errorMessage.setValue("Failed to load reviews: " + message);
                productReviews.setValue(new ArrayList<>());
            }
        });
    }

    public void submitReview(String productId, int rating, String comment) {
        errorMessage.setValue(null);
        reviewSubmissionResult.setValue(null);
        productRepository.submitReview(productId, rating, comment, new ProductRepository.RepositoryCallback<Review>() {
            @Override
            public void onSuccess(Review newReview) {
                reviewSubmissionResult.setValue(true);
                // After successful submission, refresh reviews and product details (for avg rating)
                fetchReviewsForProduct(productId);
                fetchProductById(productId); // To update average rating on product detail screen
            }

            @Override
            public void onError(String message) {
                errorMessage.setValue("Review submission failed: " + message);
                reviewSubmissionResult.setValue(false);
            }
        });
    }
}
