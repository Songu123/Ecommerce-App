package com.son.e_commerce.presenter.contract;

import com.son.e_commerce.model.entity.Category;
import com.son.e_commerce.model.entity.Product;
import java.util.List;

public interface ExploreContract {

    interface View {
        void showLoading();
        void hideLoading();
        void showCategories(List<Category> categories);
        void showProducts(List<Product> products);
        void showError(String message);
        void showEmptyState();
        void navigateToProductDetail(Product product);
    }

    interface Presenter {
        void loadCategories();
        void loadAllProducts();
        void loadProductsByCategory(int categoryId);
        void searchProducts(String query);
        void onProductClick(Product product);
        void onCategoryClick(Category category);
        void onFilterClick();
        void onDestroy();
    }
}
