package com.example.ecommerceapp.data.repository;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
// import android.content.Context; // Cannot use Android context in standard unit tests

import com.example.ecommerceapp.data.model.Product;
import com.example.ecommerceapp.data.model.network.ProductListResponse;
import com.example.ecommerceapp.network.ApiService;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProductRepositoryTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private ApiService mockApiService;
    // @Mock private Context mockContext; // Cannot easily mock context here for TokenManager in AuthRepository
    @Mock
    private AuthRepository mockAuthRepository; // ProductRepository instantiates this for reviews

    private ProductRepository productRepository;

    @Mock
    private ProductRepository.RepositoryCallback<Product> productCallback;
    @Mock
    private ProductRepository.RepositoryCallback<ProductListResponse> productListCallback;

    @Captor
    private ArgumentCaptor<Product> productCaptor;
    @Captor
    private ArgumentCaptor<ProductListResponse> productListCaptor;
    @Captor
    private ArgumentCaptor<String> stringErrorCaptor;


    @Before
    public void setUp() {
        // MockitoAnnotations.openMocks(this); // Already using @RunWith(MockitoJUnitRunner.class)

        // ProductRepository's constructor new AuthRepository() is problematic for pure unit test.
        // For this test, we'll focus on methods NOT using AuthRepository, or accept it's a limitation.
        // A better design would inject AuthRepository into ProductRepository.
        // For now, the AuthRepository instance inside ProductRepository will be the real one (which needs context).
        // This makes testing review submission hard here. We'll test getProductById.

        // We pass the mocked ApiService. The AuthRepository instance inside will be the real one.
        productRepository = new ProductRepository(mockApiService, mockAuthRepository);
    }

    @Test
    public void getProductById_success() {
        String productId = "prod_test1";
        Product mockProduct = new Product(productId, "Test Product", "Desc", 10.0, "cat1", "url", 10, null);

        Call<Product> mockCall = mock(Call.class);
        when(mockApiService.getProductById(productId)).thenReturn(mockCall);

        doAnswer(invocation -> {
            Callback<Product> callback = invocation.getArgument(0);
            callback.onResponse(mockCall, Response.success(mockProduct));
            return null;
        }).when(mockCall).enqueue(any());

        productRepository.getProductById(productId, productCallback);

        verify(productCallback).onSuccess(productCaptor.capture());
        assertEquals(productId, productCaptor.getValue().getProductId());
        assertEquals("Test Product", productCaptor.getValue().getName());
    }

    @Test
    public void getProductById_failure_apiError() {
        String productId = "prod_test2";
        Call<Product> mockCall = mock(Call.class);
        when(mockApiService.getProductById(productId)).thenReturn(mockCall);

        doAnswer(invocation -> {
            Callback<Product> callback = invocation.getArgument(0);
            callback.onResponse(mockCall, Response.error(404, mock(okhttp3.ResponseBody.class)));
            return null;
        }).when(mockCall).enqueue(any());

        productRepository.getProductById(productId, productCallback);

        verify(productCallback).onError(stringErrorCaptor.capture());
        assertTrue(stringErrorCaptor.getValue().contains("Product " + productId + " not found."));
    }

    @Test
    public void getProductById_failure_networkError() {
        String productId = "prod_test3";
        Call<Product> mockCall = mock(Call.class);
        when(mockApiService.getProductById(productId)).thenReturn(mockCall);

        doAnswer(invocation -> {
            Callback<Product> callback = invocation.getArgument(0);
            callback.onFailure(mockCall, new IOException("Network down"));
            return null;
        }).when(mockCall).enqueue(any());

        productRepository.getProductById(productId, productCallback);

        verify(productCallback).onError(stringErrorCaptor.capture());
        assertTrue(stringErrorCaptor.getValue().contains("Network error: Could not fetch product"));
    }

    @Test
    public void getAllProducts_success() {
        Map<String, String> options = new HashMap<>();
        ProductListResponse mockResponse = new ProductListResponse(new ArrayList<>(), 0, 1, 10);
        Call<ProductListResponse> mockCall = mock(Call.class);
        when(mockApiService.getAllProducts(options)).thenReturn(mockCall);

        doAnswer(invocation -> {
            Callback<ProductListResponse> callback = invocation.getArgument(0);
            callback.onResponse(mockCall, Response.success(mockResponse));
            return null;
        }).when(mockCall).enqueue(any());

        productRepository.getAllProducts(options, productListCallback);

        verify(productListCallback).onSuccess(productListCaptor.capture());
        assertNotNull(productListCaptor.getValue());
    }


    // Note: Testing methods that use AuthRepository (like submitReview) in this pure unit test
    // is difficult because ProductRepository instantiates its own AuthRepository, which needs Context.
    // This would require PowerMockito to mock the constructor of AuthRepository, or refactoring
    // ProductRepository to have AuthRepository injected.
}
