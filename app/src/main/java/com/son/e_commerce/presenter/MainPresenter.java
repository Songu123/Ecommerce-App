package com.son.e_commerce.presenter;

import android.util.Log;

import com.son.e_commerce.model.entity.Category;
import com.son.e_commerce.model.entity.Product;
import com.son.e_commerce.model.repository.CategoryRepository;
import com.son.e_commerce.model.repository.ProductRepository;
import com.son.e_commerce.presenter.contract.MainContract;

import java.util.List;

public class MainPresenter implements MainContract.Presenter {

    private MainContract.View view;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public MainPresenter(MainContract.View view,
                        CategoryRepository categoryRepository,
                        ProductRepository productRepository) {
        this.view = view;
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    @Override
    public void loadCategories() {
        view.showLoading();
        categoryRepository.getAllCategories(new CategoryRepository.OnCategoriesLoadedListener() {
            @Override
            public void onSuccess(List<Category> categories) {
                view.hideLoading();
                view.showCategories(categories);
            }

            @Override
            public void onError(String error) {
                view.hideLoading();
                view.showError(error);
            }
        });
    }

    @Override
    public void loadRecommendedProducts() {
        Log.d("MainPresenter", "loadRecommendedProducts() called");
        view.showLoading();

        productRepository.getRecommendedProducts(new ProductRepository.OnProductsLoadedListener() {
            @Override
            public void onSuccess(List<Product> products) {
                Log.d("MainPresenter", "Products loaded successfully. Count: " + (products != null ? products.size() : "null"));
                view.hideLoading();
                if (products != null && !products.isEmpty()) {
                    Log.d("MainPresenter", "Showing products to view");
                    view.showRecommendedProducts(products);
                } else {
                    Log.w("MainPresenter", "Products list is empty or null");
                    view.showError("Không có sản phẩm nào");
                }
            }

            @Override
            public void onError(String error) {
                Log.e("MainPresenter", "Failed to load products: " + error);
                view.hideLoading();
                view.showError(error);
            }
        });
    }

    @Override
    public void onCategoryClick(Category category) {
        if (view != null && category != null) {
            view.navigateToCategory(category);
        }
    }

    @Override
    public void onProductClick(Product product) {
        Log.d("PRODUCT_DEBUG", "onProductClick called");

        if (product == null) {
            Log.e("PRODUCT_ERROR", "Product is null");
            view.showError("Product is missing");
            return;
        }

        Log.d("PRODUCT_DEBUG", "Product details: " + product.toString());

        if (product.getId() <= 0) {
            Log.e("PRODUCT_ERROR", "Invalid Product ID: " + product.getId());
            view.showError("Invalid Product ID");
            return;
        }

        Log.d("PRODUCT_DEBUG", "Navigating to product detail with ID: " + product.getId());

        if (view != null) {
            view.navigateToProductDetail(product);
        }
    }


    @Override
    public void onSearchClick(String query) {
        // Navigate to explore activity with search query
    }

    @Override
    public void onCartClick() {
        // Navigate to cart activity
    }

    @Override
    public void onDestroy() {
        view = null;
    }
}
