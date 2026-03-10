package com.son.e_commerce.model.repository;

import com.son.e_commerce.model.entity.Product;
import java.util.List;

public interface ProductRepository {
    /**
     * Get all products
     */
    void getAllProducts(OnProductsLoadedListener listener);

    /**
     * Get products by category
     */
    void getProductsByCategory(int categoryId, OnProductsLoadedListener listener);

    /**
     * Get product by ID
     */
    void getProductById(int id, OnProductLoadedListener listener);

    /**
     * Search products by name
     */
    void searchProducts(String query, OnProductsLoadedListener listener);

    /**
     * Get recommended products
     */
    void getRecommendedProducts(OnProductsLoadedListener listener);

    /**
     * Callback for loading multiple products
     */
    interface OnProductsLoadedListener {
        void onSuccess(List<Product> products);
        void onError(String error);
    }

    /**
     * Callback for loading single product
     */
    interface OnProductLoadedListener {
        void onSuccess(Product product);
        void onError(String error);
    }
}
