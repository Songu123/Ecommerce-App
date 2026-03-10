package com.son.e_commerce.presenter.contract;

import com.son.e_commerce.model.entity.Category;
import com.son.e_commerce.model.entity.Product;
import java.util.List;

public interface MainContract {

    interface View {
        void showLoading();
        void hideLoading();
        void showCategories(List<Category> categories);
        void showRecommendedProducts(List<Product> products);
        void showError(String message);
        void navigateToProductDetail(Product product);
        void navigateToCategory(Category category);
        void updateCartBadge(int count);
    }

    interface Presenter {
        void loadCategories();
        void loadRecommendedProducts();
        void onCategoryClick(Category category);
        void onProductClick(Product product);
        void onSearchClick(String query);
        void onCartClick();
        void onDestroy();
    }
}
