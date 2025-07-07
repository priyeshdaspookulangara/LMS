package com.example.ecommerceapp.network;

import android.content.Context;
import com.example.ecommerceapp.util.TokenManager;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.util.concurrent.TimeUnit;

public class RetrofitClient {

    private static volatile Retrofit retrofit = null;
    private static final int TIMEOUT_SECONDS = 30;
    private static volatile TokenManager tokenManagerInstance = null;

    public static synchronized void initializeTokenManager(Context applicationContext) {
        if (tokenManagerInstance == null) {
            tokenManagerInstance = new TokenManager(applicationContext.getApplicationContext());
        }
    }

    private static TokenManager getTokenManager(Context contextForFallback) {
        if (tokenManagerInstance == null) {
            initializeTokenManager(contextForFallback.getApplicationContext());
        }
        return tokenManagerInstance;
    }

    public static Retrofit getClient(Context context) {
        if (retrofit == null) {
            synchronized (RetrofitClient.class) {
                if (retrofit == null) {
                    HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
                    // In a real app, use BuildConfig.DEBUG to set log level
                    loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

                    TokenManager tm = getTokenManager(context);
                    AuthInterceptor authInterceptor = new AuthInterceptor(tm);

                    OkHttpClient okHttpClient = new OkHttpClient.Builder()
                            .addInterceptor(loggingInterceptor)
                            .addInterceptor(authInterceptor)
                            .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                            .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                            .writeTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                            .build();

                    retrofit = new Retrofit.Builder()
                            .baseUrl(ApiService.BASE_URL)
                            .client(okHttpClient)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                }
            }
        }
        return retrofit;
    }

    public static ApiService getApiService(Context context) {
        return getClient(context).create(ApiService.class);
    }
}
