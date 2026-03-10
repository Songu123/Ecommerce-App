package com.son.e_commerce.view.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.son.e_commerce.R;
import com.son.e_commerce.model.entity.Product;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private static final String TAG = "ProductAdapter";
    private Context context;
    private List<Product> products;
    private OnProductClickListener listener;

    public interface OnProductClickListener {
        void onProductClick(Product product);
    }

    public ProductAdapter(Context context) {
        this.context = context;
        this.products = new ArrayList<>();
    }

    public void setProducts(List<Product> products) {
        Log.d(TAG, "setProducts() called with " + (products != null ? products.size() : "null") + " products");
        this.products = products != null ? products : new ArrayList<>();
        notifyDataSetChanged();
        Log.d(TAG, "Adapter updated. Item count: " + getItemCount());
    }

    public void setOnProductClickListener(OnProductClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = products.get(position);
        holder.bind(product);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageViewProduct;
        private TextView textViewName;
        private TextView textViewPrice;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewProduct = itemView.findViewById(R.id.imgProduct);
            textViewName = itemView.findViewById(R.id.tvProductTitle);
            textViewPrice = itemView.findViewById(R.id.tvProductPrice);

            itemView.setOnClickListener(v -> {
                if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                    listener.onProductClick(products.get(getAdapterPosition()));
                }
            });
        }

        public void bind(Product product) {
            Log.d(TAG, "Binding product: " + (product != null ? product.toString() : "null"));

            if (product == null) {
                Log.e(TAG, "Product is null in bind()!");
                return;
            }

            textViewName.setText(product.getName());
            textViewPrice.setText(product.getFormattedPrice());

            // Load product image with Picasso
            if (product.getImage() != null && !product.getImage().isEmpty()) {
                Log.d(TAG, "Loading image: " + product.getImage());
                Picasso.get()
                    .load(product.getImage())
                    .placeholder(R.drawable.ic_shopping_bag)
                    .error(R.drawable.ic_shopping_bag)
                    .fit()
                    .centerCrop()
                    .into(imageViewProduct);
            } else {
                Log.d(TAG, "No image URL, using placeholder");
                // Fallback to placeholder
                imageViewProduct.setImageResource(R.drawable.ic_shopping_bag);
            }
        }
    }
}
