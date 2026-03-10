package com.son.e_commerce.utils.network;

import com.son.e_commerce.data.api.*;

public class ApiClient {

    private static ProductApiService productApiService;
    private static CategoryApiService categoryApiService;
    private static UserApiService userApiService;
    private static OrderApiService orderApiService;
    private static CartApiService cartApiService;
    private static AuthApiService authApiService;

    /**
     * Reset all services (call when IP changes)
     */
    public static void resetAll() {
        RetrofitClient.reset();
        productApiService = null;
        categoryApiService = null;
        userApiService = null;
        orderApiService = null;
        cartApiService = null;
        authApiService = null;
    }

    /**
     * Get Product API Service
     */
    public static ProductApiService getProductApiService() {
        if (productApiService == null) {
            productApiService = RetrofitClient.getRetrofitInstance()
                    .create(ProductApiService.class);
        }
        return productApiService;
    }

    /**
     * Get Category API Service
     */
    public static CategoryApiService getCategoryApiService() {
        if (categoryApiService == null) {
            categoryApiService = RetrofitClient.getRetrofitInstance()
                    .create(CategoryApiService.class);
        }
        return categoryApiService;
    }

    /**
     * Get User API Service
     */
    public static UserApiService getUserApiService() {
        if (userApiService == null) {
            userApiService = RetrofitClient.getRetrofitInstance()
                    .create(UserApiService.class);
        }
        return userApiService;
    }

    /**
     * Get Order API Service
     */
    public static OrderApiService getOrderApiService() {
        if (orderApiService == null) {
            orderApiService = RetrofitClient.getRetrofitInstance()
                    .create(OrderApiService.class);
        }
        return orderApiService;
    }

    /**
     * Get Cart API Service
     */
    public static CartApiService getCartApiService() {
        if (cartApiService == null) {
            cartApiService = RetrofitClient.getRetrofitInstance()
                    .create(CartApiService.class);
        }
        return cartApiService;
    }

    /**
     * Get Auth API Service (JWT Authentication)
     */
    public static AuthApiService getAuthApiService() {
        if (authApiService == null) {
            authApiService = RetrofitClient.getRetrofitInstance()
                    .create(AuthApiService.class);
        }
        return authApiService;
    }
}
