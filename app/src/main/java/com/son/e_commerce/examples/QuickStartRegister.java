/*
 * ============================================================================
 * QUICK START: Gọi API POST /api/auth/register
 * ============================================================================
 *
 * CODE SNIPPET ĐƠN GIẢN NHẤT ĐỂ GỌI API ĐĂNG KÝ
 * Copy và paste vào Activity/Fragment của bạn
 *
 * ============================================================================
 */

package com.son.e_commerce.examples;

import com.son.e_commerce.data.dto.AuthResponse;
import com.son.e_commerce.utils.auth.RegisterApiHelper;

public class QuickStartRegister {

    /**
     * ============================================================================
     * CODE 1: CÁCH ĐơN GIẢN NHẤT (3 DÒNG CODE)
     * ============================================================================
     */
    public void simpleRegister() {
        new RegisterApiHelper().register("username", "email@example.com", "password",
            new RegisterApiHelper.RegisterCallback() {
                public void onRegisterSuccess(AuthResponse response) {
                    // ✅ Thành công! Nhận được token: response.getToken()
                }
                public void onRegisterError(String error) {
                    // ❌ Thất bại! Lỗi: error
                }
            }
        );
    }

    /**
     * ============================================================================
     * CODE 2: TRONG ACTIVITY (ĐẦY ĐỦ HƠN)
     * ============================================================================
     */
    public void registerInActivity() {
        RegisterApiHelper helper = new RegisterApiHelper();

        helper.register("john_doe", "john@example.com", "password123",
            new RegisterApiHelper.RegisterCallback() {
                @Override
                public void onRegisterSuccess(AuthResponse response) {
                    // Lấy thông tin
                    String token = response.getToken();       // JWT token
                    int userId = response.getId();            // User ID
                    String username = response.getUsername(); // Username
                    String email = response.getEmail();       // Email

                    // Hiển thị toast (nếu trong Activity)
                    // Toast.makeText(activity, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();

                    // Lưu token (nếu cần)
                    // getSharedPreferences("auth_prefs", MODE_PRIVATE)
                    //     .edit()
                    //     .putString("jwt_token", token)
                    //     .apply();

                    // Chuyển màn hình (nếu cần)
                    // startActivity(new Intent(this, MainActivity.class));
                }

                @Override
                public void onRegisterError(String errorMessage) {
                    // Hiển thị lỗi
                    // Toast.makeText(activity, "Lỗi: " + errorMessage, Toast.LENGTH_SHORT).show();

                    // Log lỗi
                    // Log.e("Register", errorMessage);
                }
            }
        );
    }

    /**
     * ============================================================================
     * CODE 3: VỚI VALIDATION (THỰC TẾ NHẤT)
     * ============================================================================
     */
    public void registerWithValidation(String username, String email, String password) {
        // Validate trước khi gọi API
        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            // Toast: "Vui lòng điền đầy đủ thông tin"
            return;
        }

        if (password.length() < 6) {
            // Toast: "Mật khẩu phải có ít nhất 6 ký tự"
            return;
        }

        // Gọi API
        RegisterApiHelper helper = new RegisterApiHelper();
        helper.register(username, email, password,
            new RegisterApiHelper.RegisterCallback() {
                @Override
                public void onRegisterSuccess(AuthResponse response) {
                    // ✅ THÀNH CÔNG - Xử lý tại đây
                }

                @Override
                public void onRegisterError(String errorMessage) {
                    // ❌ THẤT BẠI - Xử lý tại đây
                }
            }
        );
    }
}

/*
 * ============================================================================
 * THÔNG TIN API
 * ============================================================================
 *
 * Endpoint: POST /api/auth/register
 * Base URL: http://10.0.2.2:8080/ (emulator)
 *
 * Request Body:
 * {
 *     "username": "john_doe",
 *     "email": "john@example.com",
 *     "password": "password123",
 *     "fullName": "John Doe"
 * }
 *
 * Response (Success):
 * {
 *     "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
 *     "type": "Bearer",
 *     "id": 1,
 *     "username": "john_doe",
 *     "email": "john@example.com",
 *     "fullName": "John Doe",
 *     "role": "USER"
 * }
 *
 * ============================================================================
 * FILES CẦN THIẾT (ĐÃ CÓ SẴN)
 * ============================================================================
 *
 * ✅ RetrofitClient.java - Cấu hình Retrofit
 * ✅ AuthApiService.java - Interface API
 * ✅ AuthRegisterRequest.java - Request DTO
 * ✅ AuthResponse.java - Response DTO
 * ✅ RegisterApiHelper.java - Helper class (MỚI TẠO)
 *
 * ============================================================================
 * XEM THÊM
 * ============================================================================
 *
 * - RegisterApiExample.java - Nhiều ví dụ hơn
 * - RegisterExampleActivity.java - Activity hoàn chỉnh
 * - REGISTER_API_GUIDE.md - Hướng dẫn chi tiết
 *
 * ============================================================================
 */
