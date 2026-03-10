package com.son.e_commerce;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.son.e_commerce.data.CartRepositoryImpl;
import com.son.e_commerce.data.ProductRepositoryImpl;
import com.son.e_commerce.data.UserRepositoryImpl;
import com.son.e_commerce.model.entity.Product;
import com.son.e_commerce.model.entity.User;
import com.son.e_commerce.model.repository.ProductRepository;
import com.son.e_commerce.model.repository.UserRepository;
import com.son.e_commerce.presenter.ProductDetailPresenter;
import com.son.e_commerce.presenter.contract.ProductDetailContract;
import com.squareup.picasso.Picasso;

public class ProductDetailActivity extends AppCompatActivity implements ProductDetailContract.View {

    private static final String TAG = "ProductDetailActivity";

    private ProductDetailPresenter presenter;
    private ImageView imageViewProduct;
    private TextView textViewName;
    private TextView textViewPrice;
    private TextView textViewDescription;
    private TextView textViewQuantityLabel;
    private TextView textViewQuantityValue;
    private TextView textViewStock;
    private Button buttonAddToCart;
    private Button buttonBuyNow;
    private ImageButton buttonMinus;
    private ImageButton buttonPlus;
    private ImageButton buttonBack;
    private ProgressBar progressBar;

    private int currentQuantity = 1;
    private Product currentProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        initViews();
        setupPresenter();
        setupListeners();

        // Get product ID from intent
        int productId = getIntent().getIntExtra(MainActivity.EXTRA_PRODUCT_ID, -1);

        Log.d("PRODUCT_DEBUG", "Receive productId = " + productId);

        if (productId <= 0) {

            Toast.makeText(this, "Product ID invalid", Toast.LENGTH_SHORT).show();
            finish();
            return;

        }

        presenter.loadProduct(productId);
    }

    private void initViews() {
        imageViewProduct = findViewById(R.id.imageViewProductDetail);
        textViewName = findViewById(R.id.textViewProductName);
        textViewPrice = findViewById(R.id.textViewProductPrice);
        textViewDescription = findViewById(R.id.textViewProductDescription);
        textViewQuantityLabel = findViewById(R.id.textViewQuantityLabel);
        textViewQuantityValue = findViewById(R.id.textViewQuantity);
        textViewStock = findViewById(R.id.textViewStock);
        buttonAddToCart = findViewById(R.id.buttonAddToCart);
        buttonBuyNow = findViewById(R.id.buttonBuyNow);
        buttonMinus = findViewById(R.id.buttonMinus);
        buttonPlus = findViewById(R.id.buttonPlus);
        buttonBack = findViewById(R.id.buttonBack);
        progressBar = findViewById(R.id.progressBar);
    }

    private void setupPresenter() {
        ProductRepository productRepo = new ProductRepositoryImpl();
        CartRepositoryImpl cartRepo = new CartRepositoryImpl();
        UserRepository userRepo = new UserRepositoryImpl(this);
        presenter = new ProductDetailPresenter(this, productRepo, cartRepo, userRepo);
    }

    private void setupListeners() {
        buttonBack.setOnClickListener(v -> finish());

        buttonMinus.setOnClickListener(v -> {
            if (currentQuantity > 1) {
                currentQuantity--;
                presenter.onQuantityDecrease();
            }
        });

        buttonPlus.setOnClickListener(v -> {
            if (currentProduct != null && currentQuantity < currentProduct.getQuantity()) {
                currentQuantity++;
                presenter.onQuantityIncrease();
            } else {
                Toast.makeText(this, "Đã đạt số lượng tối đa", Toast.LENGTH_SHORT).show();
            }
        });

        buttonAddToCart.setOnClickListener(v -> {
            Log.d(TAG, "Add to cart clicked. Product: " +
                (currentProduct != null ? currentProduct.getName() : "null") +
                ", Quantity: " + currentQuantity);
            presenter.onAddToCartClick(currentQuantity);
        });

        buttonBuyNow.setOnClickListener(v -> {
            Log.d(TAG, "Buy now clicked");
            presenter.onBuyNowClick(currentQuantity);
        });
    }

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
        buttonAddToCart.setEnabled(false);
        buttonBuyNow.setEnabled(false);
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
        buttonAddToCart.setEnabled(true);
        buttonBuyNow.setEnabled(true);
    }

    @Override
    public void showProduct(Product product) {
        this.currentProduct = product;

        textViewName.setText(product.getName());
        textViewPrice.setText(product.getFormattedPrice());
        textViewDescription.setText(product.getDescription());

        // Show stock
        if (product.isInStock()) {
            textViewStock.setText("Còn hàng: " + product.getQuantity());
            textViewStock.setTextColor(getColor(android.R.color.holo_green_dark));
            enableAddToCart(true);
        } else {
            textViewStock.setText("Hết hàng");
            textViewStock.setTextColor(getColor(android.R.color.holo_red_dark));
            showOutOfStock();
        }

        // Load product image with Picasso
        if (product.getImage() != null && !product.getImage().isEmpty()) {
            Picasso.get()
                .load(product.getImage())
                .placeholder(R.drawable.ic_shopping_bag)
                .error(R.drawable.ic_shopping_bag)
                .fit()
                .centerInside()
                .into(imageViewProduct);
        } else {
            // Fallback to placeholder
            imageViewProduct.setImageResource(R.drawable.ic_shopping_bag);
        }
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showAddedToCart() {
        Log.d(TAG, "Product added to cart successfully");
        Toast.makeText(this, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateQuantity(int quantity) {
        this.currentQuantity = quantity;
        textViewQuantityValue.setText(String.valueOf(quantity));
    }

    @Override
    public void enableAddToCart(boolean enable) {
        buttonAddToCart.setEnabled(enable);
        buttonBuyNow.setEnabled(enable);
        buttonMinus.setEnabled(enable);
        buttonPlus.setEnabled(enable);
    }

    @Override
    public void showOutOfStock() {
        buttonAddToCart.setEnabled(false);
        buttonBuyNow.setEnabled(false);
        buttonAddToCart.setText("Hết hàng");
        buttonBuyNow.setText("Hết hàng");
    }

    @Override
    protected void onDestroy() {
        if (presenter != null) {
            presenter.onDestroy();
        }
        super.onDestroy();
    }
}
