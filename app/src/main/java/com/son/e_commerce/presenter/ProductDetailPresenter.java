package com.son.e_commerce.presenter;

import android.util.Log;

import com.son.e_commerce.data.CartRepositoryImpl;
import com.son.e_commerce.model.entity.Product;
import com.son.e_commerce.model.entity.User;
import com.son.e_commerce.model.repository.ProductRepository;
import com.son.e_commerce.model.repository.UserRepository;
import com.son.e_commerce.presenter.contract.ProductDetailContract;

public class ProductDetailPresenter implements ProductDetailContract.Presenter {

    private static final String TAG = "ProductDetailPresenter";

    private ProductDetailContract.View view;
    private ProductRepository productRepository;
    private CartRepositoryImpl cartRepository;
    private UserRepository userRepository;
    private Product currentProduct;
    private int currentQuantity = 1;

    public ProductDetailPresenter(ProductDetailContract.View view,
                                 ProductRepository productRepository,
                                 CartRepositoryImpl cartRepository,
                                 UserRepository userRepository) {
        this.view = view;
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void loadProduct(int productId) {
        view.showLoading();
        productRepository.getProductById(productId, new ProductRepository.OnProductLoadedListener() {
            @Override
            public void onSuccess(Product product) {
                if (view != null) {
                    view.hideLoading();
                    currentProduct = product;
                    view.showProduct(product);
                    view.updateQuantity(currentQuantity);
                }
            }

            @Override
            public void onError(String error) {
                if (view != null) {
                    view.hideLoading();
                    view.showError(error);
                }
            }
        });
    }

    @Override
    public void onAddToCartClick(int quantity) {
        Log.d(TAG, "onAddToCartClick called with quantity: " + quantity);

        if (currentProduct == null) {
            Log.e(TAG, "Current product is null");
            view.showError("Sản phẩm không tồn tại");
            return;
        }

        User currentUser = userRepository.getCurrentUser();
        if (currentUser == null) {
            Log.e(TAG, "User is not logged in");
            view.showError("Vui lòng đăng nhập để thêm vào giỏ hàng");
            return;
        }

        Log.d(TAG, "Adding to cart - UserId: " + currentUser.getId() +
            ", ProductId: " + currentProduct.getId() +
            ", Quantity: " + quantity);

        view.showLoading();
        cartRepository.addToCart(
            currentUser.getId(),
            currentProduct.getId(),
            quantity,
            new CartRepositoryImpl.OnCartItemAddedListener() {
                @Override
                public void onSuccess(com.son.e_commerce.model.entity.OrderItem item) {
                    Log.d(TAG, "Successfully added to cart. Item ID: " + item.getId());
                    if (view != null) {
                        view.hideLoading();
                        view.showAddedToCart();
                    }
                }

                @Override
                public void onError(String error) {
                    Log.e(TAG, "Failed to add to cart: " + error);
                    if (view != null) {
                        view.hideLoading();
                        view.showError(error);
                    }
                }
            }
        );
    }

    @Override
    public void onBuyNowClick(int quantity) {
        // Add to cart then navigate to cart
        onAddToCartClick(quantity);
        // TODO: Navigate to cart activity after adding
    }

    @Override
    public void onQuantityIncrease() {
        if (currentProduct != null && currentQuantity < currentProduct.getQuantity()) {
            currentQuantity++;
            view.updateQuantity(currentQuantity);
        }
    }

    @Override
    public void onQuantityDecrease() {
        if (currentQuantity > 1) {
            currentQuantity--;
            view.updateQuantity(currentQuantity);
        }
    }

    @Override
    public void onDestroy() {
        view = null;
    }
}
