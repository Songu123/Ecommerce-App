package com.son.e_commerce;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.son.e_commerce.data.CategoryRepositoryImpl;
import com.son.e_commerce.data.ProductRepositoryImpl;
import com.son.e_commerce.model.entity.Category;
import com.son.e_commerce.model.entity.Product;
import com.son.e_commerce.model.repository.CategoryRepository;
import com.son.e_commerce.model.repository.ProductRepository;
import com.son.e_commerce.presenter.ExplorePresenter;
import com.son.e_commerce.presenter.contract.ExploreContract;
import com.son.e_commerce.view.adapter.CategoryAdapter;
import com.son.e_commerce.view.adapter.ProductAdapter;

import java.util.List;

public class ExploreActivity extends AppCompatActivity implements ExploreContract.View {

    private ExplorePresenter presenter;
    private RecyclerView recyclerViewCategories;
    private RecyclerView recyclerViewProducts;
    private CategoryAdapter categoryAdapter;
    private ProductAdapter productAdapter;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);

        initViews();
        setupRecyclerViews();
        setupPresenter();
        setupBottomNavigation();

        presenter.loadCategories();
        presenter.loadAllProducts();
    }

    private void initViews() {
        recyclerViewCategories = findViewById(R.id.recyclerViewExploreCategories);
        recyclerViewProducts = findViewById(R.id.recyclerViewExploreProducts);
        // progressBar = findViewById(R.id.progressBar);
    }

    private void setupRecyclerViews() {
        categoryAdapter = new CategoryAdapter(this);
        recyclerViewCategories.setLayoutManager(
            new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        );
        recyclerViewCategories.setAdapter(categoryAdapter);
        categoryAdapter.setOnCategoryClickListener(category -> presenter.onCategoryClick(category));

        productAdapter = new ProductAdapter(this);
        recyclerViewProducts.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerViewProducts.setAdapter(productAdapter);
        productAdapter.setOnProductClickListener(product -> presenter.onProductClick(product));
    }

    private void setupPresenter() {
        CategoryRepository categoryRepo = new CategoryRepositoryImpl();
        ProductRepository productRepo = new ProductRepositoryImpl();
        presenter = new ExplorePresenter(this, categoryRepo, productRepo);
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.setSelectedItemId(R.id.nav_explore);

        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                finish();
                return true;
            } else if (itemId == R.id.nav_explore) {
                return true;
            } else if (itemId == R.id.nav_cart) {
                startActivity(new Intent(this, CartActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            } else if (itemId == R.id.nav_orders) {
                startActivity(new android.content.Intent(this, OrdersActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            } else if (itemId == R.id.nav_profile) {
                startActivity(new android.content.Intent(this, ProfileActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            }
            return false;
        });
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
        categoryAdapter.setCategories(categories);
    }

    @Override
    public void showProducts(List<Product> products) {
        productAdapter.setProducts(products);
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showEmptyState() {
        Toast.makeText(this, "Không có sản phẩm", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void navigateToProductDetail(Product product) {
        Intent intent = new Intent(this, ProductDetailActivity.class);
        intent.putExtra(MainActivity.EXTRA_PRODUCT_ID, product.getId());
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        if (presenter != null) {
            presenter.onDestroy();
        }
        super.onDestroy();
    }
}
