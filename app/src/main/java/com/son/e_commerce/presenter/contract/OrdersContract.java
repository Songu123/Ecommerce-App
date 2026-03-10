package com.son.e_commerce.presenter.contract;

import com.son.e_commerce.model.entity.Order;
import java.util.List;

public interface OrdersContract {

    interface View {
        void showLoading();
        void hideLoading();
        void showOrders(List<Order> orders);
        void showError(String message);
        void showEmptyState();
        void navigateToOrderDetail(Order order);
        void showOrderCancelled();
        void confirmCancelOrder(Order order);
    }

    interface Presenter {
        void loadAllOrders();
        void loadOrdersByStatus(String status);
        void onOrderClick(Order order);
        void onCancelOrder(int orderId);
        void onStartShoppingClick();
        void onDestroy();
    }
}
