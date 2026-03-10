package com.son.e_commerce.data.api;

import com.son.e_commerce.data.dto.LoginRequest;
import com.son.e_commerce.data.dto.RegisterRequest;
import com.son.e_commerce.model.entity.User;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.*;

public interface UserApiService {

    /**
     * POST /api/users/register - Đăng ký tài khoản
     */
    @POST("api/users/register")
    Call<User> register(@Body RegisterRequest request);

    /**
     * POST /api/users/login - Đăng nhập
     */
    @POST("api/users/login")
    Call<User> login(@Body LoginRequest request);

    /**
     * GET /api/users/{id} - Lấy thông tin user
     */
    @GET("api/users/{id}")
    Call<User> getUserById(@Path("id") int id);

    /**
     * PUT /api/users/{id} - Cập nhật thông tin user
     */
    @PUT("api/users/{id}")
    Call<User> updateUser(@Path("id") int id, @Body User user);

    /**
     * GET /api/users - Lấy danh sách users (Admin)
     */
    @GET("api/users")
    Call<List<User>> getAllUsers();
}
