package com.son.e_commerce.data.api;

import com.son.e_commerce.data.dto.AuthLoginRequest;
import com.son.e_commerce.data.dto.AuthRegisterRequest;
import com.son.e_commerce.data.dto.AuthResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Auth API Service - JWT Authentication
 */
public interface AuthApiService {

    /**
     * POST /api/auth/login - Đăng nhập và nhận JWT token
     */
    @POST("api/auth/login")
    Call<AuthResponse> login(@Body AuthLoginRequest request);

    /**
     * POST /api/auth/register - Đăng ký tài khoản mới
     */
    @POST("api/auth/register")
    Call<AuthResponse> register(@Body AuthRegisterRequest request);

    /**
     * GET /api/auth/test - Test API hoạt động
     */
    @GET("api/auth/test")
    Call<String> testApi();
}
