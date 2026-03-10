package com.son.e_commerce.data.api;

import com.son.e_commerce.data.dto.CartItemRequest;
import com.son.e_commerce.model.entity.OrderItem;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.*;

public interface CartApiService {

    /**
     * GET /api/cart/{userId} - Xem giỏ hàng
     */
    @GET("api/cart/{userId}")
    Call<List<OrderItem>> getCart(@Path("userId") int userId);

    /**
     * POST /api/cart/add - Thêm sản phẩm vào giỏ
     */
    @POST("api/cart/add")
    Call<OrderItem> addToCart(@Body CartItemRequest request);

    /**
     * PUT /api/cart/update - Cập nhật số lượng
     */
    @PUT("api/cart/update")
    Call<OrderItem> updateCartItem(@Body CartItemRequest request);

    /**
     * DELETE /api/cart/{itemId} - Xóa sản phẩm khỏi giỏ
     */
    @DELETE("api/cart/{itemId}")
    Call<Void> removeFromCart(@Path("itemId") int itemId);

    /**
     * DELETE /api/cart/clear/{userId} - Xóa toàn bộ giỏ hàng
     */
    @DELETE("api/cart/clear/{userId}")
    Call<Void> clearCart(@Path("userId") int userId);
}
