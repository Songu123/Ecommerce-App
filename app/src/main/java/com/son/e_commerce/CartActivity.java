package com.son.e_commerce;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.son.e_commerce.data.CartRepositoryImpl;
import com.son.e_commerce.data.UserRepositoryImpl;
import com.son.e_commerce.model.entity.OrderItem;
import com.son.e_commerce.model.entity.User;
import com.son.e_commerce.model.repository.UserRepository;
import com.son.e_commerce.view.adapter.CartAdapter;

import java.util.List;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerViewCart;
    private CartAdapter cartAdapter;
    private TextView textViewTotal;
    private Button buttonCheckout;
    private Button buttonContinueShopping;
    private LinearLayout emptyStateLayout;
    private ProgressBar progressBar;

    private CartRepositoryImpl cartRepository;
    private UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        initViews();
        setupRecyclerView();
        setupRepositories();
        setupListeners();
        setupBottomNavigation();

        loadCart();
    }

    private void initViews() {
        recyclerViewCart = findViewById(R.id.recyclerViewCart);
        textViewTotal = findViewById(R.id.textViewTotalPrice);
        buttonCheckout = findViewById(R.id.buttonCheckout);
        buttonContinueShopping = findViewById(R.id.buttonContinueShopping);
        emptyStateLayout = findViewById(R.id.emptyCartLayout);
        progressBar = findViewById(R.id.progressBar);
    }

    private void setupRecyclerView() {
        cartAdapter = new CartAdapter(this);
        recyclerViewCart.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCart.setAdapter(cartAdapter);

        cartAdapter.setOnCartItemListener(new CartAdapter.OnCartItemListener() {
            @Override
            public void onQuantityChanged(OrderItem item, int newQuantity) {
                updateCartItem(item, newQuantity);
            }

            @Override
            public void onRemoveItem(OrderItem item) {
                removeCartItem(item);
            }
        });
    }

    private void setupRepositories() {
        cartRepository = new CartRepositoryImpl();
        userRepository = new UserRepositoryImpl(this);
    }

    private void setupListeners() {
        buttonCheckout.setOnClickListener(v -> {
            Toast.makeText(this, "Checkout functionality coming soon!", Toast.LENGTH_SHORT).show();
        });

        buttonContinueShopping.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
    }

    private void loadCart() {
        User currentUser = userRepository.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "Vui lòng đăng nhập", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        showLoading();
        cartRepository.getCart(currentUser.getId(), new CartRepositoryImpl.OnCartLoadedListener() {
            @Override
            public void onSuccess(List<OrderItem> cartItems) {
                hideLoading();
                if (cartItems.isEmpty()) {
                    showEmptyCart();
                } else {
                    showCart(cartItems);
                }
            }

            @Override
            public void onError(String error) {
                hideLoading();
                Toast.makeText(CartActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateCartItem(OrderItem item, int newQuantity) {
        User currentUser = userRepository.getCurrentUser();
        if (currentUser == null) return;

        cartRepository.updateCartItem(
            currentUser.getId(),
            item.getProductId(),
            newQuantity,
            new CartRepositoryImpl.OnCartItemUpdatedListener() {
                @Override
                public void onSuccess(OrderItem updatedItem) {
                    loadCart(); // Reload to update totals
                }

                @Override
                public void onError(String error) {
                    Toast.makeText(CartActivity.this, error, Toast.LENGTH_SHORT).show();
                }
            }
        );
    }

    private void removeCartItem(OrderItem item) {
        cartRepository.removeFromCart(item.getId(), new CartRepositoryImpl.OnCartItemRemovedListener() {
            @Override
            public void onSuccess(boolean removed) {
                Toast.makeText(CartActivity.this, "Đã xóa khỏi giỏ hàng", Toast.LENGTH_SHORT).show();
                loadCart();
            }

            @Override
            public void onError(String error) {
                Toast.makeText(CartActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerViewCart.setVisibility(View.GONE);
        emptyStateLayout.setVisibility(View.GONE);
    }

    private void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }

    private void showCart(List<OrderItem> cartItems) {
        recyclerViewCart.setVisibility(View.VISIBLE);
        emptyStateLayout.setVisibility(View.GONE);
        cartAdapter.setCartItems(cartItems);
        updateTotal(cartItems);
    }

    private void showEmptyCart() {
        recyclerViewCart.setVisibility(View.GONE);
        emptyStateLayout.setVisibility(View.VISIBLE);
    }

    private void updateTotal(List<OrderItem> cartItems) {
        double total = 0;
        for (OrderItem item : cartItems) {
            total += item.getSubtotal();
        }
        textViewTotal.setText(String.format("$%.2f", total));
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.setSelectedItemId(R.id.nav_cart);

        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                startActivity(new Intent(this, MainActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            } else if (itemId == R.id.nav_explore) {
                startActivity(new Intent(this, ExploreActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            } else if (itemId == R.id.nav_cart) {
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
}
