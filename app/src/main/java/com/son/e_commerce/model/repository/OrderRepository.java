package com.son.e_commerce.model.repository;

import com.son.e_commerce.model.entity.Order;
import java.util.List;

public interface OrderRepository {
    /**
     * Get all orders for a user
     */
    void getOrdersByUser(int userId, OnOrdersLoadedListener listener);

    /**
     * Get orders by status
     */
    void getOrdersByStatus(int userId, String status, OnOrdersLoadedListener listener);

    /**
     * Get order by ID
     */
    void getOrderById(int orderId, OnOrderLoadedListener listener);

    /**
     * Create new order
     */
    void createOrder(Order order, OnOrderCreatedListener listener);

    /**
     * Update order status
     */
    void updateOrderStatus(int orderId, String status, OnOrderUpdatedListener listener);

    /**
     * Cancel order
     */
    void cancelOrder(int orderId, OnOrderUpdatedListener listener);

    /**
     * Callback for loading multiple orders
     */
    interface OnOrdersLoadedListener {
        void onSuccess(List<Order> orders);
        void onError(String error);
    }

    /**
     * Callback for loading single order
     */
    interface OnOrderLoadedListener {
        void onSuccess(Order order);
        void onError(String error);
    }

    /**
     * Callback for creating order
     */
    interface OnOrderCreatedListener {
        void onSuccess(Order order);
        void onError(String error);
    }

    /**
     * Callback for updating order
     */
    interface OnOrderUpdatedListener {
        void onSuccess(boolean updated);
        void onError(String error);
    }
}
