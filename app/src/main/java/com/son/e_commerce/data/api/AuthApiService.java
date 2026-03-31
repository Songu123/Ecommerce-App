package com.son.e_commerce.data.api;

import com.son.e_commerce.data.dto.AuthLoginRequest;
import com.son.e_commerce.data.dto.AuthRegisterRequest;
import com.son.e_commerce.data.dto.AuthResponse;
import com.son.e_commerce.data.dto.RefreshTokenRequest;
import com.son.e_commerce.data.dto.LogoutRequest;
import com.son.e_commerce.data.dto.LogoutResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Auth API Service - JWT Authentication with Refresh Token Support
 */
public interface AuthApiService {

    /**
     * POST /api/auth/login - Đăng nhập và nhận Access Token + Refresh Token
     */
    @POST("api/auth/login")
    Call<AuthResponse> login(@Body AuthLoginRequest request);

    /**
     * POST /api/auth/register - Đăng ký tài khoản mới
     */
    @POST("api/auth/register")
    Call<AuthResponse> register(@Body AuthRegisterRequest request);

    /**
     * POST /api/auth/refresh - Làm mới Access Token bằng Refresh Token
     */
    @POST("api/auth/refresh")
    Call<AuthResponse> refreshToken(@Body RefreshTokenRequest request);

    /**
     * POST /api/auth/logout - Đăng xuất và vô hiệu hóa Refresh Token
     */
    @POST("api/auth/logout")
    Call<LogoutResponse> logout(@Body LogoutRequest request);

    /**
     * GET /api/auth/test - Test API hoạt động
     */
    @GET("api/auth/test")
    Call<String> testApi();
}


