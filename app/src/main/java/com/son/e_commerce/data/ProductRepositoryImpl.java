package com.son.e_commerce.data;

import android.util.Log;

import com.son.e_commerce.data.api.ProductApiService;
import com.son.e_commerce.model.entity.Product;
import com.son.e_commerce.model.repository.ProductRepository;
import com.son.e_commerce.utils.network.ApiClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductRepositoryImpl implements ProductRepository {

    private static final String TAG = "ProductRepositoryImpl";
    private ProductApiService apiService;

    public ProductRepositoryImpl() {
        this.apiService = ApiClient.getProductApiService();
    }

    @Override
    public void getAllProducts(OnProductsLoadedListener listener) {
        Log.d(TAG, "getAllProducts() called");
        apiService.getAllProducts().enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                Log.d(TAG, "getAllProducts() response code: " + response.code());

                if (response.isSuccessful() && response.body() != null) {
                    List<Product> products = response.body();
                    Log.d(TAG, "getAllProducts() success. Count: " + products.size());
                    if (!products.isEmpty()) {
                        Product first = products.get(0);
                        Log.d(TAG, "First product - ID: " + first.getId() +
                                ", Name: " + first.getName() +
                                ", Price: " + first.getPrice());
                    }
                    listener.onSuccess(products);
                } else {
                    String error = "Failed to load products: " + response.code();
                    Log.e(TAG, error);
                    listener.onError(error);
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                String error = "Network error: " + (t != null ? t.getMessage() : "Unknown");
                Log.e(TAG, "getAllProducts() failed: " + error, t);
                listener.onError(error);
            }
        });
    }

    @Override
    public void getProductsByCategory(int categoryId, ProductRepository.OnProductsLoadedListener listener) {
        apiService.getProductsByCategory(categoryId).enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listener.onSuccess(response.body());
                } else {
                    listener.onError("Failed to load products: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                listener.onError("Network error: " + t.getMessage());
            }
        });
    }
    @Override
    public void getProductById(int id, OnProductLoadedListener listener) {
        Log.d(TAG, "getProductById() called with id: " + id);
        apiService.getProductById(id).enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                Log.d(TAG, "getProductById() response code: " + response.code());

                // Log raw response body for debugging
                try {
                    String rawBody = response.raw().body() != null ? response.raw().body().string() : "null";
                    Log.d(TAG, "Raw response body: " + rawBody);
                } catch (Exception e) {
                    Log.d(TAG, "Could not read raw body: " + e.getMessage());
                }

                if (response.isSuccessful() && response.body() != null) {
                    Product product = response.body();
                    Log.d(TAG, "getProductById() success - Product ID: " + product.getId() +
                            ", Name: " + product.getName() +
                            ", Price: " + product.getPrice());
                    listener.onSuccess(product);
                } else {
                    String error = "Product not found (HTTP " + response.code() + ")";
                    if (response.body() == null) {
                        error += " - Response body is null";
                    }
                    Log.e(TAG, error);
                    listener.onError(error);
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                String error = "Network error: " + (t != null ? t.getMessage() : "Unknown error");
                Log.e(TAG, "getProductById() failed: " + error, t);
                listener.onError(error);
            }
        });
    }

    @Override
    public void searchProducts(String query, OnProductsLoadedListener listener) {
        // Search functionality - filter on client side for now
        // TODO: Implement server-side search endpoint
        getAllProducts(new OnProductsLoadedListener() {
            @Override
            public void onSuccess(List<Product> products) {
                List<Product> filtered = new java.util.ArrayList<>();
                String lowerQuery = query.toLowerCase();
                for (Product product : products) {
                    if (product.getName().toLowerCase().contains(lowerQuery) ||
                        product.getDescription().toLowerCase().contains(lowerQuery)) {
                        filtered.add(product);
                    }
                }
                listener.onSuccess(filtered);
            }

            @Override
            public void onError(String error) {
                listener.onError(error);
            }
        });
    }

    @Override
    public void getRecommendedProducts(OnProductsLoadedListener listener) {
        Log.d(TAG, "getRecommendedProducts() called");
        // Get all products and return first 6 as recommended
        // TODO: Implement server-side recommendation logic
        getAllProducts(new OnProductsLoadedListener() {
            @Override
            public void onSuccess(List<Product> products) {
                List<Product> recommended = products.size() > 6 ?
                    products.subList(0, 6) : products;
                Log.d(TAG, "getRecommendedProducts() returning " + recommended.size() + " products");
                listener.onSuccess(recommended);
            }

            @Override
            public void onError(String error) {
                Log.e(TAG, "getRecommendedProducts() failed: " + error);
                listener.onError(error);
            }
        });
    }
}
