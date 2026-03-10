package com.son.e_commerce.data;

import com.son.e_commerce.data.api.OrderApiService;
import com.son.e_commerce.data.dto.OrderStatusRequest;
import com.son.e_commerce.model.entity.Order;
import com.son.e_commerce.model.repository.OrderRepository;
import com.son.e_commerce.utils.network.ApiClient;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderRepositoryImpl implements OrderRepository {

    private OrderApiService apiService;

    public OrderRepositoryImpl() {
        this.apiService = ApiClient.getOrderApiService();
    }

    @Override
    public void getOrdersByUser(int userId, OnOrdersLoadedListener listener) {
        apiService.getOrdersByUser(userId).enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listener.onSuccess(response.body());
                } else {
                    listener.onError("Failed to load orders: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                listener.onError("Network error: " + t.getMessage());
            }
        });
    }

    @Override
    public void getOrdersByStatus(int userId, String status, OnOrdersLoadedListener listener) {
        getOrdersByUser(userId, new OnOrdersLoadedListener() {
            @Override
            public void onSuccess(List<Order> orders) {
                List<Order> filtered = new java.util.ArrayList<>();
                for (Order order : orders) {
                    if (order.getStatus().equalsIgnoreCase(status)) {
                        filtered.add(order);
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
    public void getOrderById(int orderId, OnOrderLoadedListener listener) {
        apiService.getOrderById(orderId).enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listener.onSuccess(response.body());
                } else {
                    listener.onError("Order not found");
                }
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                listener.onError("Network error: " + t.getMessage());
            }
        });
    }

    @Override
    public void createOrder(Order order, OnOrderCreatedListener listener) {
        apiService.createOrder(order).enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listener.onSuccess(response.body());
                } else {
                    listener.onError("Failed to create order: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                listener.onError("Network error: " + t.getMessage());
            }
        });
    }

    @Override
    public void updateOrderStatus(int orderId, String status, OnOrderUpdatedListener listener) {
        OrderStatusRequest request = new OrderStatusRequest(status);
        apiService.updateOrderStatus(orderId, request).enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                if (response.isSuccessful()) {
                    listener.onSuccess(true);
                } else {
                    listener.onError("Failed to update order: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                listener.onError("Network error: " + t.getMessage());
            }
        });
    }

    @Override
    public void cancelOrder(int orderId, OnOrderUpdatedListener listener) {
        apiService.deleteOrder(orderId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    listener.onSuccess(true);
                } else {
                    listener.onError("Failed to cancel order: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                listener.onError("Network error: " + t.getMessage());
            }
        });
    }
}
