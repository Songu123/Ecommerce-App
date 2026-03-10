package com.son.e_commerce;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.son.e_commerce.data.OrderRepositoryImpl;
import com.son.e_commerce.data.UserRepositoryImpl;
import com.son.e_commerce.model.entity.Order;
import com.son.e_commerce.model.repository.OrderRepository;
import com.son.e_commerce.model.repository.UserRepository;
import com.son.e_commerce.presenter.OrdersPresenter;
import com.son.e_commerce.presenter.contract.OrdersContract;
import com.son.e_commerce.view.adapter.OrderAdapter;

import java.util.List;

public class OrdersActivity extends AppCompatActivity implements OrdersContract.View {

    private OrdersPresenter presenter;
    private RecyclerView recyclerViewOrders;
    private OrderAdapter orderAdapter;
    private ProgressBar progressBar;
    private LinearLayout emptyStateLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        initViews();
        setupRecyclerView();
        setupPresenter();
        setupBottomNavigation();

        // Load orders
        presenter.loadAllOrders();
    }

    private void initViews() {
        recyclerViewOrders = findViewById(R.id.recyclerViewOrders);
        emptyStateLayout = findViewById(R.id.emptyStateLayout);
        // progressBar = findViewById(R.id.progressBar);
    }

    private void setupRecyclerView() {
        orderAdapter = new OrderAdapter(this);
        recyclerViewOrders.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewOrders.setAdapter(orderAdapter);

        orderAdapter.setOnOrderClickListener(order -> {
            presenter.onOrderClick(order);
        });
    }

    private void setupPresenter() {
        OrderRepository orderRepo = new OrderRepositoryImpl();
        UserRepository userRepo = new UserRepositoryImpl(this);
        presenter = new OrdersPresenter(this, orderRepo, userRepo);
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.setSelectedItemId(R.id.nav_orders);

        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                startActivity(new android.content.Intent(this, MainActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            } else if (itemId == R.id.nav_explore) {
                startActivity(new android.content.Intent(this, ExploreActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            } else if (itemId == R.id.nav_cart) {
                startActivity(new android.content.Intent(this, CartActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            } else if (itemId == R.id.nav_orders) {
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
        recyclerViewOrders.setVisibility(View.GONE);
        emptyStateLayout.setVisibility(View.GONE);
    }

    @Override
    public void hideLoading() {
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void showOrders(List<Order> orders) {
        recyclerViewOrders.setVisibility(View.VISIBLE);
        emptyStateLayout.setVisibility(View.GONE);
        orderAdapter.setOrders(orders);
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showEmptyState() {
        recyclerViewOrders.setVisibility(View.GONE);
        emptyStateLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void navigateToOrderDetail(Order order) {
        Toast.makeText(this, "Order #" + order.getId(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showOrderCancelled() {
        Toast.makeText(this, "Đơn hàng đã được hủy", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void confirmCancelOrder(Order order) {
        // TODO: Show confirmation dialog
    }

    @Override
    protected void onDestroy() {
        if (presenter != null) {
            presenter.onDestroy();
        }
        super.onDestroy();
    }
}
