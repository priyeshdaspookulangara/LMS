package com.example.ecommerceapp.network;

// General Models
import com.example.ecommerceapp.data.model.Category;
import com.example.ecommerceapp.data.model.Product;
import com.example.ecommerceapp.data.model.Cart;
import com.example.ecommerceapp.data.model.Order; // For API responses

// Network Specific Models
import com.example.ecommerceapp.data.model.network.ProductListResponse;

// Auth Models
import com.example.ecommerceapp.data.model.auth.AuthResponse;
import com.example.ecommerceapp.data.model.auth.LoginRequest;
import com.example.ecommerceapp.data.model.auth.RegisterRequest;

// Cart Request Models
import com.example.ecommerceapp.data.model.cart.CartItemRequest;
import com.example.ecommerceapp.data.model.cart.CartItemUpdateRequest;

// Order Request/Response Models
import com.example.ecommerceapp.data.model.order.OrderCreationRequest;
import com.example.ecommerceapp.data.model.order.OrderListResponse;


import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface ApiService {

    String BASE_URL = "http://axlinfotech.com/ecom/api/v1/";

    // Product Endpoints
    @GET("products")
    Call<ProductListResponse> getAllProducts(@QueryMap Map<String, String> options);

    @GET("products/{productId}")
    Call<Product> getProductById(@Path("productId") String productId);

    @GET("products/search")
    Call<ProductListResponse> searchProducts(@Query("q") String query);

    // Product Categories Endpoints
    @GET("categories")
    Call<List<Category>> getAllCategories();

    @GET("categories/{categoryId}/products")
    Call<ProductListResponse> getProductsByCategory(
            @Path("categoryId") String categoryId,
            @QueryMap Map<String, String> options
    );

    // Authentication Endpoints
    @POST("auth/login")
    Call<AuthResponse> loginUser(@Body LoginRequest loginRequest);

    @POST("auth/register")
    Call<AuthResponse> registerUser(@Body RegisterRequest registerRequest);

    // Cart Endpoints
    @GET("cart")
    Call<Cart> getUserCart();

    @POST("cart/items")
    Call<Cart> addItemToCart(@Body CartItemRequest cartItemRequest);

    @PUT("cart/items/{productId}")
    Call<Cart> updateCartItem(
            @Path("productId") String productId,
            @Body CartItemUpdateRequest cartItemUpdateRequest
    );

    @DELETE("cart/items/{productId}")
    Call<Void> removeItemFromCart(@Path("productId") String productId);

    @DELETE("cart")
    Call<Void> clearCart();

    // Order Endpoints
    @POST("orders")
    Call<Order> createOrder(@Body OrderCreationRequest orderCreationRequest);

    @GET("orders")
    Call<OrderListResponse> getAllOrdersForUser(@QueryMap Map<String, String> options);

    @GET("orders/{orderId}")
    Call<Order> getOrderById(@Path("orderId") String orderId);

    // PATCH /orders/{orderId}/status is admin only, not for client app usually
}
