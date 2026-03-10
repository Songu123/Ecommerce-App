package com.son.e_commerce.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.son.e_commerce.R;
import com.son.e_commerce.data.OrderRepositoryImpl;
import com.son.e_commerce.data.UserRepositoryImpl;
import com.son.e_commerce.model.entity.Order;
import com.son.e_commerce.model.entity.User;
import com.son.e_commerce.model.repository.OrderRepository;
import com.son.e_commerce.model.repository.UserRepository;
import com.son.e_commerce.presenter.OrdersPresenter;
import com.son.e_commerce.presenter.contract.OrdersContract;
import com.son.e_commerce.view.adapter.OrderAdapter;

import java.util.List;

public class OrdersFragment extends Fragment implements OrdersContract.View {

    private OrdersPresenter presenter;
    private RecyclerView recyclerViewOrders;
    private OrderAdapter orderAdapter;
    private LinearLayout emptyStateLayout;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders, container, false);

        initViews(view);
        setupRecyclerView();
        setupPresenter();

        presenter.loadAllOrders();

        return view;
    }

    private void initViews(View view) {
        recyclerViewOrders = view.findViewById(R.id.recyclerViewOrders);
        emptyStateLayout = view.findViewById(R.id.emptyStateLayout);
        progressBar = view.findViewById(R.id.progressBar);
    }

    private void setupRecyclerView() {
        orderAdapter = new OrderAdapter(getContext());
        recyclerViewOrders.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewOrders.setAdapter(orderAdapter);

        orderAdapter.setOnOrderClickListener(order -> presenter.onOrderClick(order));
    }

    private void setupPresenter() {
        OrderRepository orderRepo = new OrderRepositoryImpl();
        UserRepository userRepo = new UserRepositoryImpl(getContext());
        presenter = new OrdersPresenter(this, orderRepo, userRepo);
    }

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerViewOrders.setVisibility(View.GONE);
        emptyStateLayout.setVisibility(View.GONE);
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showOrders(List<Order> orders) {
        recyclerViewOrders.setVisibility(View.VISIBLE);
        emptyStateLayout.setVisibility(View.GONE);
        orderAdapter.setOrders(orders);
    }

    @Override
    public void showError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showEmptyState() {
        recyclerViewOrders.setVisibility(View.GONE);
        emptyStateLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void navigateToOrderDetail(Order order) {
        Toast.makeText(getContext(), "Order #" + order.getId(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showOrderCancelled() {
        Toast.makeText(getContext(), "Đơn hàng đã được hủy", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void confirmCancelOrder(Order order) {
        // TODO: Show confirmation dialog
    }

    @Override
    public void onDestroyView() {
        if (presenter != null) {
            presenter.onDestroy();
        }
        super.onDestroyView();
    }
}
