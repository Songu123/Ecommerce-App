package com.son.e_commerce.data.api;

import com.son.e_commerce.data.dto.OrderStatusRequest;
import com.son.e_commerce.model.entity.Order;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.*;

public interface OrderApiService {

    /**
     * GET /api/orders - Lấy tất cả đơn hàng (Admin)
     */
    @GET("api/orders")
    Call<List<Order>> getAllOrders();

    /**
     * GET /api/orders/{id} - Chi tiết đơn hàng
     */
    @GET("api/orders/{id}")
    Call<Order> getOrderById(@Path("id") int id);

    /**
     * GET /api/orders/user/{userId} - Đơn hàng của user
     */
    @GET("api/orders/user/{userId}")
    Call<List<Order>> getOrdersByUser(@Path("userId") int userId);

    /**
     * POST /api/orders - Tạo đơn hàng mới
     */
    @POST("api/orders")
    Call<Order> createOrder(@Body Order order);

    /**
     * PUT /api/orders/{id}/status - Cập nhật trạng thái đơn hàng
     */
    @PUT("api/orders/{id}/status")
    Call<Order> updateOrderStatus(@Path("id") int id, @Body OrderStatusRequest status);

    /**
     * DELETE /api/orders/{id} - Hủy đơn hàng
     */
    @DELETE("api/orders/{id}")
    Call<Void> deleteOrder(@Path("id") int id);
}
