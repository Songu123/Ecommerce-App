package com.son.e_commerce.presenter;

import android.util.Log;

import com.son.e_commerce.data.AuthRepositoryImpl;
import com.son.e_commerce.data.CartRepositoryImpl;
import com.son.e_commerce.model.entity.OrderItem;
import com.son.e_commerce.model.entity.Product;
import com.son.e_commerce.model.entity.User;
import com.son.e_commerce.model.repository.ProductRepository;
import com.son.e_commerce.model.repository.UserRepository;
import com.son.e_commerce.presenter.contract.ProductDetailContract;

public class ProductDetailPresenter implements ProductDetailContract.Presenter {

    private static final String TAG = "ProductDetailPresenter";

    private ProductDetailContract.View view;
    private final ProductRepository productRepository;
    private final CartRepositoryImpl cartRepository;
    private final UserRepository userRepository;
    private AuthRepositoryImpl authRepository;
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

    public void setAuthRepository(AuthRepositoryImpl authRepository) {
        this.authRepository = authRepository;
    }

    @Override
    public void loadProduct(int productId) {
        Log.d(TAG, "loadProduct() called with id: " + productId);

        if (productId <= 0) {
            Log.e(TAG, "Invalid product ID: " + productId);
            if (view != null) {
                view.showError("ID sản phẩm không hợp lệ");
            }
            return;
        }

        if (view != null) {
            view.showLoading();
        } else {
            Log.e(TAG, "View is null in loadProduct()");
        }

        productRepository.getProductById(productId, new ProductRepository.OnProductLoadedListener() {
            @Override
            public void onSuccess(Product product) {
                Log.d(TAG, "loadProduct() onSuccess called");

                if (product == null) {
                    Log.e(TAG, "Product object is null in onSuccess");
                    if (view != null) {
                        view.hideLoading();
                        view.showError("Không thể tải thông tin sản phẩm");
                    }
                    return;
                }

                Log.d(TAG, "loadProduct() success: " + product.toString());

                if (view != null) {
                    view.hideLoading();
                    currentProduct = product;
                    view.showProduct(product);
                    view.updateQuantity(currentQuantity);
                    Log.d(TAG, "Product displayed to view");
                } else {
                    Log.e(TAG, "View is null when trying to display product");
                }
            }

            @Override
            public void onError(String error) {
                Log.e(TAG, "loadProduct() onError: " + error);
                if (view != null) {
                    view.hideLoading();
                    view.showError("Lỗi tải sản phẩm: " + error);
                } else {
                    Log.e(TAG, "View is null when trying to show error");
                }
            }
        });
    }

    @Override
    public void onAddToCartClick(int quantity) {
        Log.d(TAG, "onAddToCartClick called with quantity: " + quantity);

        if (currentProduct == null) {
            Log.e(TAG, "Current product is null");
            if (view != null) view.showError("Sản phẩm không tồn tại");
            return;
        }

        // Ưu tiên dùng AuthRepository (JWT login)
        User currentUser = null;
        if (authRepository != null) {
            currentUser = authRepository.getCurrentUser();
            Log.d(TAG, "AuthRepository user: " + (currentUser != null ? currentUser.getId() + " / " + currentUser.getUsername() : "null"));
        }
        // Fallback sang UserRepository
        if (currentUser == null) {
            currentUser = userRepository.getCurrentUser();
            Log.d(TAG, "UserRepository user: " + (currentUser != null ? currentUser.getId() + " / " + currentUser.getUsername() : "null"));
        }

        if (currentUser == null) {
            Log.e(TAG, "No user logged in");
            if (view != null) view.showError("Vui lòng đăng nhập để thêm vào giỏ hàng");
            return;
        }

        final int userId = currentUser.getId();
        Log.d(TAG, "Adding to cart - UserId: " + userId +
                ", ProductId: " + currentProduct.getId() +
                ", Quantity: " + quantity);

        if (view != null) view.showLoading();

        cartRepository.addToCart(userId, currentProduct.getId(), quantity,
                new CartRepositoryImpl.OnCartItemAddedListener() {
                    @Override
                    public void onSuccess(OrderItem item) {
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
                            view.showError("Thêm vào giỏ hàng thất bại: " + error);
                        }
                    }
                });
    }

    @Override
    public void onBuyNowClick(int quantity) {
        onAddToCartClick(quantity);
    }

    @Override
    public void onQuantityIncrease() {
        if (currentProduct != null && currentQuantity < currentProduct.getQuantity()) {
            currentQuantity++;
            if (view != null) view.updateQuantity(currentQuantity);
        }
    }

    @Override
    public void onQuantityDecrease() {
        if (currentQuantity > 1) {
            currentQuantity--;
            if (view != null) view.updateQuantity(currentQuantity);
        }
    }

    @Override
    public void onDestroy() {
        view = null;
    }
}
