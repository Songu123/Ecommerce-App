package com.son.e_commerce.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.son.e_commerce.R;
import com.son.e_commerce.model.entity.Order;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private Context context;
    private List<Order> orders;
    private OnOrderClickListener listener;
    private SimpleDateFormat dateFormat;

    public interface OnOrderClickListener {
        void onOrderClick(Order order);
    }

    public OrderAdapter(Context context) {
        this.context = context;
        this.orders = new ArrayList<>();
        this.dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
        notifyDataSetChanged();
    }

    public void setOnOrderClickListener(OnOrderClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orders.get(position);
        holder.bind(order);
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    class OrderViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewOrderId;
        private TextView textViewDate;
        private TextView textViewStatus;
        private TextView textViewTotal;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewOrderId = itemView.findViewById(R.id.textViewOrderId);
            textViewDate = itemView.findViewById(R.id.textViewOrderDate);
            textViewStatus = itemView.findViewById(R.id.textViewOrderStatus);
            textViewTotal = itemView.findViewById(R.id.textViewOrderTotal);

            itemView.setOnClickListener(v -> {
                if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                    listener.onOrderClick(orders.get(getAdapterPosition()));
                }
            });
        }

        public void bind(Order order) {
            textViewOrderId.setText("Đơn hàng #" + order.getId());
            textViewDate.setText(dateFormat.format(order.getCreatedAt()));
            textViewTotal.setText(order.getFormattedTotal());

            // Set status with color
            String status = getStatusText(order.getStatus());
            textViewStatus.setText(status);

            // Set status color
            int statusColor = getStatusColor(order.getStatus());
            textViewStatus.setTextColor(statusColor);
        }

        private String getStatusText(String status) {
            switch (status.toLowerCase()) {
                case "pending":
                    return "Chờ xác nhận";
                case "processing":
                    return "Đang xử lý";
                case "shipped":
                    return "Đang giao";
                case "delivered":
                    return "Đã giao";
                case "cancelled":
                    return "Đã hủy";
                default:
                    return status;
            }
        }

        private int getStatusColor(String status) {
            switch (status.toLowerCase()) {
                case "pending":
                    return context.getColor(android.R.color.holo_orange_dark);
                case "processing":
                    return context.getColor(android.R.color.holo_blue_dark);
                case "shipped":
                    return context.getColor(android.R.color.holo_purple);
                case "delivered":
                    return context.getColor(android.R.color.holo_green_dark);
                case "cancelled":
                    return context.getColor(android.R.color.holo_red_dark);
                default:
                    return context.getColor(android.R.color.black);
            }
        }
    }
}
