package com.son.e_commerce.presenter.contract;

import com.son.e_commerce.model.entity.Product;

public interface ProductDetailContract {

    interface View {
        void showLoading();
        void hideLoading();
        void showProduct(Product product);
        void showError(String message);
        void showAddedToCart();
        void updateQuantity(int quantity);
        void enableAddToCart(boolean enable);
        void showOutOfStock();
    }

    interface Presenter {
        void loadProduct(int productId);
        void onAddToCartClick(int quantity);
        void onBuyNowClick(int quantity);
        void onQuantityIncrease();
        void onQuantityDecrease();
        void onDestroy();
    }
}
