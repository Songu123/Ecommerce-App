package com.son.e_commerce.view.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.son.e_commerce.MainActivityNew;
import com.son.e_commerce.R;
import com.son.e_commerce.data.CartRepositoryImpl;
import com.son.e_commerce.data.UserRepositoryImpl;
import com.son.e_commerce.model.entity.OrderItem;
import com.son.e_commerce.model.entity.User;
import com.son.e_commerce.model.repository.UserRepository;
import com.son.e_commerce.view.adapter.CartAdapter;

import java.util.List;

public class CartFragment extends Fragment {

    private static final String TAG = "CartFragment";

    private RecyclerView recyclerViewCart;
    private CartAdapter cartAdapter;
    private TextView textViewTotal;
    private Button buttonCheckout;
    private Button buttonContinueShopping;
    private LinearLayout emptyStateLayout;
    private ProgressBar progressBar;

    private CartRepositoryImpl cartRepository;
    private UserRepository userRepository;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        initViews(view);
        setupRecyclerView();
        setupRepositories();
        setupListeners();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadCart();
    }

    private void initViews(View view) {
        recyclerViewCart = view.findViewById(R.id.recyclerViewCart);
        textViewTotal = view.findViewById(R.id.textViewTotalPrice);
        buttonCheckout = view.findViewById(R.id.buttonCheckout);
        buttonContinueShopping = view.findViewById(R.id.buttonContinueShopping);
        emptyStateLayout = view.findViewById(R.id.emptyCartLayout);
        progressBar = view.findViewById(R.id.progressBar);
    }

    private void setupRecyclerView() {
        cartAdapter = new CartAdapter(getContext());
        recyclerViewCart.setLayoutManager(new LinearLayoutManager(getContext()));
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
        userRepository = new UserRepositoryImpl(getContext());
    }

    private void setupListeners() {
        buttonCheckout.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Checkout functionality coming soon!", Toast.LENGTH_SHORT).show();
        });

        buttonContinueShopping.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivityNew) {
                ((MainActivityNew) getActivity()).navigateToHome();
            }
        });
    }

    private void loadCart() {
        Log.d(TAG, "loadCart() called");

        User currentUser = userRepository.getCurrentUser();
        if (currentUser == null) {
            Log.e(TAG, "No user logged in");
            Toast.makeText(getContext(), "Vui lòng đăng nhập", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d(TAG, "Loading cart for user ID: " + currentUser.getId());
        showLoading();

        cartRepository.getCart(currentUser.getId(), new CartRepositoryImpl.OnCartLoadedListener() {
            @Override
            public void onSuccess(List<OrderItem> cartItems) {
                Log.d(TAG, "Cart loaded successfully. Items count: " + (cartItems != null ? cartItems.size() : "null"));
                hideLoading();

                if (cartItems == null || cartItems.isEmpty()) {
                    Log.d(TAG, "Cart is empty, showing empty state");
                    showEmptyCart();
                } else {
                    Log.d(TAG, "Showing cart with " + cartItems.size() + " items");
                    showCart(cartItems);
                }
            }

            @Override
            public void onError(String error) {
                Log.e(TAG, "Error loading cart: " + error);
                hideLoading();
                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
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
                    loadCart();
                }

                @Override
                public void onError(String error) {
                    Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                }
            }
        );
    }

    private void removeCartItem(OrderItem item) {
        cartRepository.removeFromCart(item.getId(), new CartRepositoryImpl.OnCartItemRemovedListener() {
            @Override
            public void onSuccess(boolean removed) {
                Toast.makeText(getContext(), "Đã xóa khỏi giỏ hàng", Toast.LENGTH_SHORT).show();
                loadCart();
            }

            @Override
            public void onError(String error) {
                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
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
        Log.d(TAG, "showCart() - Setting visibility and updating adapter");
        recyclerViewCart.setVisibility(View.VISIBLE);
        emptyStateLayout.setVisibility(View.GONE);
        cartAdapter.setCartItems(cartItems);
        updateTotal(cartItems);
        Log.d(TAG, "Cart displayed with " + cartItems.size() + " items");
    }

    private void showEmptyCart() {
        Log.d(TAG, "showEmptyCart() - Showing empty state");
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
}
