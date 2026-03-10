package com.son.e_commerce.utils.auth;

import android.util.Log;

import androidx.annotation.NonNull;

import com.son.e_commerce.data.api.AuthApiService;
import com.son.e_commerce.data.dto.AuthRegisterRequest;
import com.son.e_commerce.data.dto.AuthResponse;
import com.son.e_commerce.utils.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Helper class để gọi API đăng ký
 * Sử dụng Retrofit để call POST /api/auth/register
 */
public class RegisterApiHelper {

    private static final String TAG = "RegisterApiHelper";
    private AuthApiService authApiService;

    public RegisterApiHelper() {
        // Khởi tạo AuthApiService từ RetrofitClient
        this.authApiService = RetrofitClient.getRetrofitInstance()
                .create(AuthApiService.class);
    }

    /**
     * Gọi API đăng ký
     *
     * @param username Tên đăng nhập
     * @param email Email
     * @param password Mật khẩu
     * @param callback Callback để xử lý kết quả
     */
    public void register(String username, String email, String password,
                         final RegisterCallback callback) {
        // Tạo request body
        AuthRegisterRequest request = new AuthRegisterRequest(
            username,
            email,
            password,
            username // fullName mặc định là username
        );

        // Gọi API
        Call<AuthResponse> call = authApiService.register(request);

        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(@NonNull Call<AuthResponse> call,
                                 @NonNull Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Đăng ký thành công
                    AuthResponse authResponse = response.body();
                    Log.d(TAG, "Đăng ký thành công! Token: " + authResponse.getToken());
                    Log.d(TAG, "User ID: " + authResponse.getId());
                    Log.d(TAG, "Username: " + authResponse.getUsername());
                    Log.d(TAG, "Email: " + authResponse.getEmail());

                    callback.onRegisterSuccess(authResponse);
                } else {
                    // Lỗi từ server (4xx, 5xx)
                    String errorMessage = "Đăng ký thất bại: ";
                    try {
                        if (response.errorBody() != null) {
                            errorMessage += response.errorBody().string();
                        } else {
                            errorMessage += "HTTP " + response.code();
                        }
                    } catch (Exception e) {
                        errorMessage += "HTTP " + response.code();
                    }
                    Log.e(TAG, errorMessage);
                    callback.onRegisterError(errorMessage);
                }
            }

            @Override
            public void onFailure(@NonNull Call<AuthResponse> call, @NonNull Throwable t) {
                // Lỗi kết nối hoặc lỗi khác
                String errorMessage = "Lỗi kết nối: " + t.getMessage();
                Log.e(TAG, errorMessage, t);
                callback.onRegisterError(errorMessage);
            }
        });
    }

    /**
     * Gọi API đăng ký với đầy đủ thông tin
     *
     * @param username Tên đăng nhập
     * @param email Email
     * @param password Mật khẩu
     * @param fullName Họ tên đầy đủ
     * @param callback Callback để xử lý kết quả
     */
    public void registerWithFullName(String username, String email, String password,
                                     String fullName, final RegisterCallback callback) {
        // Tạo request body với đầy đủ thông tin
        AuthRegisterRequest request = new AuthRegisterRequest(
            username,
            email,
            password,
            fullName
        );

        // Gọi API
        Call<AuthResponse> call = authApiService.register(request);

        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(@NonNull Call<AuthResponse> call,
                                 @NonNull Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AuthResponse authResponse = response.body();
                    Log.d(TAG, "Đăng ký thành công! Token: " + authResponse.getToken());
                    callback.onRegisterSuccess(authResponse);
                } else {
                    String errorMessage = "Đăng ký thất bại: HTTP " + response.code();
                    try {
                        if (response.errorBody() != null) {
                            errorMessage = response.errorBody().string();
                        }
                    } catch (Exception e) {
                        // Ignore
                    }
                    Log.e(TAG, errorMessage);
                    callback.onRegisterError(errorMessage);
                }
            }

            @Override
            public void onFailure(@NonNull Call<AuthResponse> call, @NonNull Throwable t) {
                String errorMessage = "Lỗi kết nối: " + t.getMessage();
                Log.e(TAG, errorMessage, t);
                callback.onRegisterError(errorMessage);
            }
        });
    }

    /**
     * Interface callback để xử lý kết quả đăng ký
     */
    public interface RegisterCallback {
        /**
         * Được gọi khi đăng ký thành công
         * @param response AuthResponse chứa token và thông tin user
         */
        void onRegisterSuccess(AuthResponse response);

        /**
         * Được gọi khi đăng ký thất bại
         * @param errorMessage Thông báo lỗi
         */
        void onRegisterError(String errorMessage);
    }
}
