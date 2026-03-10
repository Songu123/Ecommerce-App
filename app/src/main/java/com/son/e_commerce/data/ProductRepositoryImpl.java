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
                        Log.d(TAG, "First product: " + products.get(0).toString());
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
                String error = "Network error: " + t.getMessage();
                Log.e(TAG, "getAllProducts() failed: " + error, t);
                listener.onError(error);
            }
        });
    }

    @Override
    public void getProductsByCategory(int categoryId, OnProductsLoadedListener listener) {
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
                if (response.isSuccessful() && response.body() != null) {
                    Product product = response.body();
                    Log.d(TAG, "getProductById() success: " + product.toString());
                    listener.onSuccess(product);
                } else {
                    String error = "Product not found";
                    Log.e(TAG, error);
                    listener.onError(error);
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                String error = "Network error: " + t.getMessage();
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
