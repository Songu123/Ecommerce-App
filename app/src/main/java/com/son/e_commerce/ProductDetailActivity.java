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

import com.son.e_commerce.data.AuthRepositoryImpl;
import com.son.e_commerce.data.CartRepositoryImpl;
import com.son.e_commerce.data.ProductRepositoryImpl;
import com.son.e_commerce.data.UserRepositoryImpl;
import com.son.e_commerce.model.entity.Product;
import com.son.e_commerce.model.repository.ProductRepository;
import com.son.e_commerce.model.repository.UserRepository;
import com.son.e_commerce.presenter.ProductDetailPresenter;
import com.son.e_commerce.presenter.contract.ProductDetailContract;
import com.son.e_commerce.utils.network.ApiConfig;
import com.squareup.picasso.Picasso;

public class ProductDetailActivity extends AppCompatActivity implements ProductDetailContract.View {

    private static final String TAG = "ProductDetailActivity";

    private ProductDetailContract.Presenter presenter;
    private ImageView imageViewProduct;
    private TextView textViewName;
    private TextView textViewPrice;
    private TextView textViewDescription;
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

        // Log API configuration for debugging
        Log.d(TAG, "=== ProductDetailActivity Started ===");
        Log.d(TAG, ApiConfig.getConnectionInfo());
        Log.d(TAG, "===================================");

        initViews();
        setupPresenter();
        setupListeners();

        int productId = getIntent().getIntExtra(MainActivity.EXTRA_PRODUCT_ID, -1);
        Log.d(TAG, "Received productId = " + productId);

        if (productId <= 0) {
            Toast.makeText(this, "Sản phẩm không hợp lệ", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        presenter.loadProduct(productId);
    }

    private void initViews() {
        imageViewProduct   = findViewById(R.id.imageViewProductDetail);
        textViewName       = findViewById(R.id.textViewProductName);
        textViewPrice      = findViewById(R.id.textViewProductPrice);
        textViewDescription= findViewById(R.id.textViewProductDescription);
        textViewQuantityValue = findViewById(R.id.textViewQuantity);
        textViewStock      = findViewById(R.id.textViewStock);
        buttonAddToCart    = findViewById(R.id.buttonAddToCart);
        buttonBuyNow       = findViewById(R.id.buttonBuyNow);
        buttonMinus        = findViewById(R.id.buttonMinus);
        buttonPlus         = findViewById(R.id.buttonPlus);
        buttonBack         = findViewById(R.id.buttonBack);
        progressBar        = findViewById(R.id.progressBar);
    }

    private void setupPresenter() {
        ProductRepository productRepo = new ProductRepositoryImpl();
        CartRepositoryImpl cartRepo   = new CartRepositoryImpl();
        UserRepository userRepo       = new UserRepositoryImpl(this);
        AuthRepositoryImpl authRepo   = new AuthRepositoryImpl(this);

        ProductDetailPresenter detailPresenter =
                new ProductDetailPresenter(this, productRepo, cartRepo, userRepo);
        detailPresenter.setAuthRepository(authRepo);
        presenter = detailPresenter;
    }

    private void setupListeners() {
        buttonBack.setOnClickListener(v -> finish());

        buttonMinus.setOnClickListener(v -> {
            if (currentQuantity > 1) {
                presenter.onQuantityDecrease();
            }
        });

        buttonPlus.setOnClickListener(v -> {
            if (currentProduct != null && currentQuantity < currentProduct.getQuantity()) {
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

    // ─── ProductDetailContract.View ──────────────────────────────────────────

    @Override
    public void showLoading() {
        Log.d(TAG, "showLoading() called");
        progressBar.setVisibility(View.VISIBLE);
        buttonAddToCart.setEnabled(false);
        buttonBuyNow.setEnabled(false);
    }

    @Override
    public void hideLoading() {
        Log.d(TAG, "hideLoading() called");
        progressBar.setVisibility(View.GONE);
        buttonAddToCart.setEnabled(true);
        buttonBuyNow.setEnabled(true);
    }

    @Override
    public void showProduct(Product product) {
        Log.d(TAG, "showProduct() called with product: " + (product != null ? product.toString() : "null"));

        if (product == null) {
            Log.e(TAG, "showProduct: Product is null!");
            showError("Sản phẩm không tồn tại");
            return;
        }

        this.currentProduct = product;

        Log.d(TAG, "Setting product name: " + product.getName());
        textViewName.setText(product.getName());

        String formattedPrice = product.getFormattedPrice();
        Log.d(TAG, "Setting product price: " + formattedPrice);
        textViewPrice.setText(formattedPrice);

        Log.d(TAG, "Setting product description: " + product.getDescription());
        textViewDescription.setText(product.getDescription());

        if (product.isInStock()) {
            String stockText = "Còn hàng: " + product.getQuantity();
            Log.d(TAG, "Product is in stock: " + stockText);
            textViewStock.setText(stockText);
            textViewStock.setTextColor(getColor(android.R.color.holo_green_dark));
            enableAddToCart(true);
        } else {
            Log.d(TAG, "Product is out of stock");
            textViewStock.setText("Hết hàng");
            textViewStock.setTextColor(getColor(android.R.color.holo_red_dark));
            showOutOfStock();
        }

        if (product.getImage() != null && !product.getImage().isEmpty()) {
            Log.d(TAG, "Loading product image: " + product.getImage());
            Picasso.get()
                    .load(product.getImage())
                    .placeholder(R.drawable.ic_shopping_bag)
                    .error(R.drawable.ic_shopping_bag)
                    .fit()
                    .centerInside()
                    .into(imageViewProduct);
        } else {
            Log.d(TAG, "No image URL, using placeholder");
            imageViewProduct.setImageResource(R.drawable.ic_shopping_bag);
        }

        Log.d(TAG, "showProduct() completed successfully");
    }

    @Override
    public void showError(String message) {
        Log.e(TAG, "showError() called: " + message);
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showAddedToCart() {
        Log.d(TAG, "Product added to cart successfully");
        Toast.makeText(this, "✅ Đã thêm vào giỏ hàng!", Toast.LENGTH_SHORT).show();
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
        if (presenter != null) presenter.onDestroy();
        super.onDestroy();
    }
}
