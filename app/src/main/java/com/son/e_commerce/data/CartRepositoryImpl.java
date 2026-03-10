package com.son.e_commerce.data;

import android.util.Log;

import com.son.e_commerce.data.api.CartApiService;
import com.son.e_commerce.data.dto.CartItemRequest;
import com.son.e_commerce.model.entity.OrderItem;
import com.son.e_commerce.utils.network.ApiClient;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartRepositoryImpl {

    private static final String TAG = "CartRepositoryImpl";
    private CartApiService apiService;

    public CartRepositoryImpl() {
        this.apiService = ApiClient.getCartApiService();
    }

    /**
     * Get cart items for a user
     */
    public void getCart(int userId, OnCartLoadedListener listener) {
        apiService.getCart(userId).enqueue(new Callback<List<OrderItem>>() {
            @Override
            public void onResponse(Call<List<OrderItem>> call, Response<List<OrderItem>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listener.onSuccess(response.body());
                } else {
                    listener.onError("Failed to load cart: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<OrderItem>> call, Throwable t) {
                listener.onError("Network error: " + t.getMessage());
            }
        });
    }

    /**
     * Add item to cart
     */
    public void addToCart(int userId, int productId, int quantity, OnCartItemAddedListener listener) {
        Log.d(TAG, "addToCart - UserId: " + userId + ", ProductId: " + productId + ", Quantity: " + quantity);

        CartItemRequest request = new CartItemRequest(userId, productId, quantity);

        apiService.addToCart(request).enqueue(new Callback<OrderItem>() {
            @Override
            public void onResponse(Call<OrderItem> call, Response<OrderItem> response) {
                Log.d(TAG, "addToCart response code: " + response.code());

                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "addToCart success. Item ID: " + response.body().getId());
                    listener.onSuccess(response.body());
                } else {
                    String errorMsg = "Failed to add to cart: " + response.code();
                    Log.e(TAG, errorMsg);
                    listener.onError(errorMsg);
                }
            }

            @Override
            public void onFailure(Call<OrderItem> call, Throwable t) {
                String errorMsg = "Network error: " + t.getMessage();
                Log.e(TAG, "addToCart failed: " + errorMsg, t);
                listener.onError(errorMsg);
            }
        });
    }

    /**
     * Update cart item quantity
     */
    public void updateCartItem(int userId, int productId, int quantity, OnCartItemUpdatedListener listener) {
        CartItemRequest request = new CartItemRequest(userId, productId, quantity);

        apiService.updateCartItem(request).enqueue(new Callback<OrderItem>() {
            @Override
            public void onResponse(Call<OrderItem> call, Response<OrderItem> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listener.onSuccess(response.body());
                } else {
                    listener.onError("Failed to update cart: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<OrderItem> call, Throwable t) {
                listener.onError("Network error: " + t.getMessage());
            }
        });
    }

    /**
     * Remove item from cart
     */
    public void removeFromCart(int itemId, OnCartItemRemovedListener listener) {
        apiService.removeFromCart(itemId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    listener.onSuccess(true);
                } else {
                    listener.onError("Failed to remove item: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                listener.onError("Network error: " + t.getMessage());
            }
        });
    }

    /**
     * Clear entire cart
     */
    public void clearCart(int userId, OnCartClearedListener listener) {
        apiService.clearCart(userId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    listener.onSuccess(true);
                } else {
                    listener.onError("Failed to clear cart: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                listener.onError("Network error: " + t.getMessage());
            }
        });
    }

    // Callback interfaces
    public interface OnCartLoadedListener {
        void onSuccess(List<OrderItem> cartItems);
        void onError(String error);
    }

    public interface OnCartItemAddedListener {
        void onSuccess(OrderItem item);
        void onError(String error);
    }

    public interface OnCartItemUpdatedListener {
        void onSuccess(OrderItem item);
        void onError(String error);
    }

    public interface OnCartItemRemovedListener {
        void onSuccess(boolean removed);
        void onError(String error);
    }

    public interface OnCartClearedListener {
        void onSuccess(boolean cleared);
        void onError(String error);
    }
}
