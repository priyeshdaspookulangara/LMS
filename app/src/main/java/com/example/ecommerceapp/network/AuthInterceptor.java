package com.example.ecommerceapp.network;

import androidx.annotation.NonNull;
import com.example.ecommerceapp.util.TokenManager;
import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {

    private TokenManager tokenManager;

    public AuthInterceptor(TokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request originalRequest = chain.request();
        String token = tokenManager.getAccessToken();

        // Do not add Authorization header to auth endpoints themselves (e.g. login, register)
        // or if token is null.
        if (token != null && !isAuthEndpoint(originalRequest.url().toString())) {
            Request.Builder builder = originalRequest.newBuilder()
                    .header("Authorization", "Bearer " + token);
            originalRequest = builder.build();
        }

        return chain.proceed(originalRequest);
    }

    private boolean isAuthEndpoint(String url) {
        // Add paths that should NOT receive the Authorization header
        return url.contains("/auth/login") || url.contains("/auth/register");
        // Add other public paths if any, e.g., /products if they are public
        // For this app, most product/category views are public,
        // but cart/order will require auth.
        // The current API doc suggests /products are public.
        // Let's assume for now only strict auth endpoints are excluded.
        // A more robust way is to annotate ApiService methods or have a list of public paths.
    }
}
