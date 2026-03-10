package com.son.e_commerce.view.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.son.e_commerce.R;
import com.son.e_commerce.model.entity.OrderItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private static final String TAG = "CartAdapter";

    private Context context;
    private List<OrderItem> cartItems;
    private OnCartItemListener listener;

    public interface OnCartItemListener {
        void onQuantityChanged(OrderItem item, int newQuantity);
        void onRemoveItem(OrderItem item);
    }

    public CartAdapter(Context context) {
        this.context = context;
        this.cartItems = new ArrayList<>();
    }

    public void setCartItems(List<OrderItem> items) {
        Log.d(TAG, "setCartItems() called with " + (items != null ? items.size() : "null") + " items");
        this.cartItems = items != null ? items : new ArrayList<>();
        notifyDataSetChanged();
        Log.d(TAG, "Adapter updated. Item count: " + getItemCount());
    }

    public void setOnCartItemListener(OnCartItemListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        OrderItem item = cartItems.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    class CartViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageViewProduct;
        private TextView textViewName;
        private TextView textViewPrice;
        private TextView textViewQuantity;
        private TextView textViewSubtotal;
        private ImageButton buttonMinus;
        private ImageButton buttonPlus;
        private ImageButton buttonRemove;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewProduct = itemView.findViewById(R.id.imageViewCartProduct);
            textViewName = itemView.findViewById(R.id.textViewCartProductName);
            textViewPrice = itemView.findViewById(R.id.textViewCartProductPrice);
            textViewQuantity = itemView.findViewById(R.id.textViewCartQuantity);
            textViewSubtotal = itemView.findViewById(R.id.textViewCartSubtotal);
            buttonMinus = itemView.findViewById(R.id.buttonCartMinus);
            buttonPlus = itemView.findViewById(R.id.buttonCartPlus);
            buttonRemove = itemView.findViewById(R.id.buttonCartRemove);
        }

        public void bind(OrderItem item) {
            Log.d(TAG, "Binding item - ID: " + item.getId() +
                ", ProductID: " + item.getProductId() +
                ", Quantity: " + item.getQuantity() +
                ", Price: " + item.getPrice() +
                ", Has Product: " + (item.getProduct() != null));

            // Set product name and price
            if (item.getProduct() != null) {
                textViewName.setText(item.getProduct().getName());
                Log.d(TAG, "Product name: " + item.getProduct().getName());
            } else {
                textViewName.setText("Product #" + item.getProductId());
                Log.w(TAG, "Product object is null, using fallback name");
            }

            textViewPrice.setText(item.getFormattedPrice());
            textViewQuantity.setText(String.valueOf(item.getQuantity()));
            textViewSubtotal.setText(item.getFormattedSubtotal());

            // Load product image with Picasso
            if (item.getProduct() != null && item.getProduct().getImage() != null && !item.getProduct().getImage().isEmpty()) {
                Picasso.get()
                    .load(item.getProduct().getImage())
                    .placeholder(R.drawable.ic_shopping_bag)
                    .error(R.drawable.ic_shopping_bag)
                    .fit()
                    .centerCrop()
                    .into(imageViewProduct);
            } else {
                // Fallback to placeholder
                imageViewProduct.setImageResource(R.drawable.ic_shopping_bag);
            }

            // Quantity buttons
            buttonMinus.setOnClickListener(v -> {
                if (item.getQuantity() > 1 && listener != null) {
                    listener.onQuantityChanged(item, item.getQuantity() - 1);
                }
            });

            buttonPlus.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onQuantityChanged(item, item.getQuantity() + 1);
                }
            });

            // Remove button
            buttonRemove.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onRemoveItem(item);
                }
            });
        }
    }
}
