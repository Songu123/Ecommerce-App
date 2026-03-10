package com.son.e_commerce;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
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
import com.son.e_commerce.presenter.MainPresenter;
import com.son.e_commerce.presenter.contract.MainContract;
import com.son.e_commerce.view.adapter.CategoryAdapter;
import com.son.e_commerce.view.adapter.ProductAdapter;

import java.util.List;

public class MainActivity extends AppCompatActivity implements MainContract.View {
    public static final String EXTRA_PRODUCT_ID = "EXTRA_PRODUCT_ID";
    private MainPresenter presenter;
    private RecyclerView recyclerViewCategories;
    private RecyclerView recyclerViewProducts;
    private CategoryAdapter categoryAdapter;
    private ProductAdapter productAdapter;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        initViews();
        setupRecyclerViews();
        setupPresenter();
        setupBottomNavigation();

        // Load data from API
        presenter.loadCategories();
        presenter.loadRecommendedProducts();
    }

    private void initViews() {
        recyclerViewCategories = findViewById(R.id.recyclerViewCategories);
        recyclerViewProducts = findViewById(R.id.recyclerViewProducts);
        progressBar = findViewById(R.id.progressBar);

        // Setup cart button
        findViewById(R.id.btnCart).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CartActivity.class);
            startActivity(intent);
        });
    }

    private void setupRecyclerViews() {
        // Categories RecyclerView - Horizontal
        categoryAdapter = new CategoryAdapter(this);
        recyclerViewCategories.setLayoutManager(
            new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        );
        recyclerViewCategories.setAdapter(categoryAdapter);

        categoryAdapter.setOnCategoryClickListener(category -> {
            presenter.onCategoryClick(category);
        });

        // Products RecyclerView - Grid
        productAdapter = new ProductAdapter(this);
        recyclerViewProducts.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerViewProducts.setAdapter(productAdapter);

        productAdapter.setOnProductClickListener(product -> {
            presenter.onProductClick(product);
        });
    }

    private void setupPresenter() {
        CategoryRepository categoryRepo = new CategoryRepositoryImpl();
        ProductRepository productRepo = new ProductRepositoryImpl();
        presenter = new MainPresenter(this, categoryRepo, productRepo);
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.setSelectedItemId(R.id.nav_home);

        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                return true;
            } else if (itemId == R.id.nav_explore) {
                startActivity(new Intent(this, ExploreActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            } else if (itemId == R.id.nav_cart) {
                startActivity(new Intent(this, CartActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            } else if (itemId == R.id.nav_orders) {
                startActivity(new Intent(this, OrdersActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            } else if (itemId == R.id.nav_profile) {
                startActivity(new Intent(this, ProfileActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            }
            return false;
        });
    }

    // MVP View methods
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
    public void showRecommendedProducts(List<Product> products) {
        productAdapter.setProducts(products);
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void navigateToProductDetail(Product product) {

        Intent intent = new Intent(this, ProductDetailActivity.class);
        Log.d("PRODUCT_DEBUG", "Send ID = " + product.getId());
        intent.putExtra(EXTRA_PRODUCT_ID, product.getId());
        startActivity(intent);

    }

    @Override
    public void navigateToCategory(Category category) {
        Toast.makeText(this, "Category: " + category.getName(), Toast.LENGTH_SHORT).show();
        // TODO: Navigate to ExploreActivity with category filter
    }

    @Override
    public void updateCartBadge(int count) {
        // TODO: Update cart badge count
    }

    @Override
    protected void onDestroy() {
        if (presenter != null) {
            presenter.onDestroy();
        }
        super.onDestroy();
    }
}