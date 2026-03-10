package com.son.e_commerce.presenter;

import com.son.e_commerce.model.entity.Order;
import com.son.e_commerce.model.repository.OrderRepository;
import com.son.e_commerce.model.repository.UserRepository;
import com.son.e_commerce.presenter.contract.OrdersContract;

import java.util.List;

public class OrdersPresenter implements OrdersContract.Presenter {

    private OrdersContract.View view;
    private OrderRepository orderRepository;
    private UserRepository userRepository;

    public OrdersPresenter(OrdersContract.View view,
                          OrderRepository orderRepository,
                          UserRepository userRepository) {
        this.view = view;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void loadAllOrders() {
        int userId = getCurrentUserId();
        if (userId == -1) {
            view.showError("User not logged in");
            return;
        }

        view.showLoading();
        orderRepository.getOrdersByUser(userId, new OrderRepository.OnOrdersLoadedListener() {
            @Override
            public void onSuccess(List<Order> orders) {
                if (view != null) {
                    view.hideLoading();
                    if (orders.isEmpty()) {
                        view.showEmptyState();
                    } else {
                        view.showOrders(orders);
                    }
                }
            }

            @Override
            public void onError(String error) {
                if (view != null) {
                    view.hideLoading();
                    view.showError(error);
                }
            }
        });
    }

    @Override
    public void loadOrdersByStatus(String status) {
        int userId = getCurrentUserId();
        if (userId == -1) {
            view.showError("User not logged in");
            return;
        }

        view.showLoading();
        orderRepository.getOrdersByStatus(userId, status, new OrderRepository.OnOrdersLoadedListener() {
            @Override
            public void onSuccess(List<Order> orders) {
                if (view != null) {
                    view.hideLoading();
                    if (orders.isEmpty()) {
                        view.showEmptyState();
                    } else {
                        view.showOrders(orders);
                    }
                }
            }

            @Override
            public void onError(String error) {
                if (view != null) {
                    view.hideLoading();
                    view.showError(error);
                }
            }
        });
    }

    @Override
    public void onOrderClick(Order order) {
        if (view != null && order != null) {
            view.navigateToOrderDetail(order);
        }
    }

    @Override
    public void onCancelOrder(int orderId) {
        orderRepository.cancelOrder(orderId, new OrderRepository.OnOrderUpdatedListener() {
            @Override
            public void onSuccess(boolean updated) {
                if (view != null && updated) {
                    view.showOrderCancelled();
                    loadAllOrders(); // Refresh list
                }
            }

            @Override
            public void onError(String error) {
                if (view != null) {
                    view.showError(error);
                }
            }
        });
    }

    @Override
    public void onStartShoppingClick() {
        // Navigate to home or explore
    }

    @Override
    public void onDestroy() {
        view = null;
    }

    private int getCurrentUserId() {
        if (userRepository.getCurrentUser() != null) {
            return userRepository.getCurrentUser().getId();
        }
        return -1;
    }
}
