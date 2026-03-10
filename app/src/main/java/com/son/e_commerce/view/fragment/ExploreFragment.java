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
import com.son.e_commerce.ProductDetailActivity;
import com.son.e_commerce.R;
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

public class ExploreFragment extends Fragment implements ExploreContract.View {

    private ExplorePresenter presenter;
    private RecyclerView recyclerViewCategories;
    private RecyclerView recyclerViewProducts;
    private CategoryAdapter categoryAdapter;
    private ProductAdapter productAdapter;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_explore, container, false);

        initViews(view);
        setupRecyclerViews();
        setupPresenter();

        presenter.loadCategories();
        presenter.loadAllProducts();

        return view;
    }

    private void initViews(View view) {
        recyclerViewCategories = view.findViewById(R.id.recyclerViewExploreCategories);
        recyclerViewProducts = view.findViewById(R.id.recyclerViewExploreProducts);
        progressBar = view.findViewById(R.id.progressBar);
    }

    private void setupRecyclerViews() {
        categoryAdapter = new CategoryAdapter(getContext());
        recyclerViewCategories.setLayoutManager(
            new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false)
        );
        recyclerViewCategories.setAdapter(categoryAdapter);
        categoryAdapter.setOnCategoryClickListener(category -> presenter.onCategoryClick(category));

        productAdapter = new ProductAdapter(getContext());
        recyclerViewProducts.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerViewProducts.setAdapter(productAdapter);
        productAdapter.setOnProductClickListener(product -> presenter.onProductClick(product));
    }

    private void setupPresenter() {
        CategoryRepository categoryRepo = new CategoryRepositoryImpl();
        ProductRepository productRepo = new ProductRepositoryImpl();
        presenter = new ExplorePresenter(this, categoryRepo, productRepo);
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
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showEmptyState() {
        Toast.makeText(getContext(), "Không có sản phẩm", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void navigateToProductDetail(Product product) {
        Intent intent = new Intent(getContext(), ProductDetailActivity.class);
        intent.putExtra(MainActivity.EXTRA_PRODUCT_ID, product.getId());
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        if (presenter != null) {
            presenter.onDestroy();
        }
        super.onDestroyView();
    }
}
