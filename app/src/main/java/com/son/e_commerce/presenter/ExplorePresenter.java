package com.son.e_commerce.presenter;

import com.son.e_commerce.model.entity.Category;
import com.son.e_commerce.model.entity.Product;
import com.son.e_commerce.model.repository.CategoryRepository;
import com.son.e_commerce.model.repository.ProductRepository;
import com.son.e_commerce.presenter.contract.ExploreContract;

import java.util.List;

public class ExplorePresenter implements ExploreContract.Presenter {

    private ExploreContract.View view;
    private CategoryRepository categoryRepository;
    private ProductRepository productRepository;

    public ExplorePresenter(ExploreContract.View view,
                           CategoryRepository categoryRepository,
                           ProductRepository productRepository) {
        this.view = view;
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    @Override
    public void loadCategories() {
        categoryRepository.getAllCategories(new CategoryRepository.OnCategoriesLoadedListener() {
            @Override
            public void onSuccess(List<Category> categories) {
                if (view != null) {
                    view.showCategories(categories);
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
    public void loadAllProducts() {
        view.showLoading();
        productRepository.getAllProducts(new ProductRepository.OnProductsLoadedListener() {
            @Override
            public void onSuccess(List<Product> products) {
                if (view != null) {
                    view.hideLoading();
                    if (products.isEmpty()) {
                        view.showEmptyState();
                    } else {
                        view.showProducts(products);
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
    public void loadProductsByCategory(int categoryId) {
        view.showLoading();
        productRepository.getProductsByCategory(categoryId, new ProductRepository.OnProductsLoadedListener() {
            @Override
            public void onSuccess(List<Product> products) {
                if (view != null) {
                    view.hideLoading();
                    if (products.isEmpty()) {
                        view.showEmptyState();
                    } else {
                        view.showProducts(products);
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
    public void searchProducts(String query) {
        if (query == null || query.trim().isEmpty()) {
            loadAllProducts();
            return;
        }

        view.showLoading();
        productRepository.searchProducts(query, new ProductRepository.OnProductsLoadedListener() {
            @Override
            public void onSuccess(List<Product> products) {
                if (view != null) {
                    view.hideLoading();
                    if (products.isEmpty()) {
                        view.showEmptyState();
                    } else {
                        view.showProducts(products);
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
    public void onProductClick(Product product) {
        if (view != null && product != null) {
            view.navigateToProductDetail(product);
        }
    }

    @Override
    public void onCategoryClick(Category category) {
        if (category != null) {
            loadProductsByCategory(category.getId());
        }
    }

    @Override
    public void onFilterClick() {
        // Show filter dialog
    }

    @Override
    public void onDestroy() {
        view = null;
    }
}
