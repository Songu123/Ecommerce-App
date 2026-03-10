package com.son.e_commerce.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.son.e_commerce.MainActivity;
import com.son.e_commerce.MainActivityNew;
import com.son.e_commerce.ProductDetailActivity;
import com.son.e_commerce.R;
import com.son.e_commerce.data.CategoryRepositoryImpl;
import com.son.e_commerce.data.ProductRepositoryImpl;
import com.son.e_commerce.model.entity.Category;
import com.son.e_commerce.model.entity.Product;
import com.son.e_commerce.model.repository.CategoryRepository;
import com.son.e_commerce.model.repository.ProductRepository;
import com.son.e_commerce.presenter.MainPresenter;
import com.son.e_commerce.presenter.contract.MainContract;
import com.son.e_commerce.view.adapter.CategoryAdapter;
import com.son.e_commerce.view.adapter.ProductAdapter;

import java.util.List;

public class HomeFragment extends Fragment implements MainContract.View {

    private MainPresenter presenter;
    private RecyclerView recyclerViewCategories;
    private RecyclerView recyclerViewProducts;
    private CategoryAdapter categoryAdapter;
    private ProductAdapter productAdapter;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        initViews(view);
        setupRecyclerViews();
        setupPresenter();

        // Load data
        presenter.loadCategories();
        presenter.loadRecommendedProducts();

        return view;
    }

    private void initViews(View view) {
        recyclerViewCategories = view.findViewById(R.id.recyclerViewCategories);
        recyclerViewProducts = view.findViewById(R.id.recyclerViewProducts);
        progressBar = view.findViewById(R.id.progressBar);

        // Setup cart button
        view.findViewById(R.id.btnCart).setOnClickListener(v -> {
            // Navigate to cart fragment
            if (getActivity() instanceof MainActivityNew) {
                ((MainActivityNew) getActivity()).navigateToCart();
            }
        });
    }

    private void setupRecyclerViews() {
        // Categories
        categoryAdapter = new CategoryAdapter(getContext());
        recyclerViewCategories.setLayoutManager(
            new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false)
        );
        recyclerViewCategories.setAdapter(categoryAdapter);
        categoryAdapter.setOnCategoryClickListener(category -> presenter.onCategoryClick(category));

        // Products
        productAdapter = new ProductAdapter(getContext());
        recyclerViewProducts.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerViewProducts.setAdapter(productAdapter);
        productAdapter.setOnProductClickListener(product -> presenter.onProductClick(product));
    }

    private void setupPresenter() {
        CategoryRepository categoryRepo = new CategoryRepositoryImpl();
        ProductRepository productRepo = new ProductRepositoryImpl();
        presenter = new MainPresenter(this, categoryRepo, productRepo);
    }

    @Override
    public void showLoading() {
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideLoading() {
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void showCategories(List<Category> categories) {
        android.util.Log.d("HomeFragment", "showCategories() called with " + (categories != null ? categories.size() : "null") + " categories");
        categoryAdapter.setCategories(categories);
    }

    @Override
    public void showRecommendedProducts(List<Product> products) {
        android.util.Log.d("HomeFragment", "showRecommendedProducts() called with " + (products != null ? products.size() : "null") + " products");
        if (products != null && !products.isEmpty()) {
            android.util.Log.d("HomeFragment", "Setting products to adapter");
            productAdapter.setProducts(products);
        } else {
            android.util.Log.w("HomeFragment", "Products list is empty or null");
        }
    }

    @Override
    public void showError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void navigateToProductDetail(Product product) {
        Intent intent = new Intent(getContext(), ProductDetailActivity.class);
        intent.putExtra(MainActivity.EXTRA_PRODUCT_ID, product.getId());
        startActivity(intent);
    }

    @Override
    public void navigateToCategory(Category category) {
        Toast.makeText(getContext(), "Category: " + category.getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateCartBadge(int count) {
        // TODO: Update cart badge
    }

    @Override
    public void onDestroyView() {
        if (presenter != null) {
            presenter.onDestroy();
        }
        super.onDestroyView();
    }
}
