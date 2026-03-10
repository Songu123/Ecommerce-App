package com.son.e_commerce.utils.auth;

import android.content.Context;
import android.util.Log;
import android.util.Patterns;

import com.son.e_commerce.data.AuthRepositoryImpl;
import com.son.e_commerce.model.entity.User;
import com.son.e_commerce.model.repository.AuthRepository;

/**
 * Login Helper - Xử lý đăng nhập bằng Email và Password
 *
 * Usage:
 * LoginHelper loginHelper = new LoginHelper(context);
 * loginHelper.loginWithEmail("test@gmail.com", "password123", new LoginCallback() {
 *     @Override
 *     public void onLoginSuccess(User user, String token) {
 *         // Đăng nhập thành công
 *     }
 *
 *     @Override
 *     public void onLoginError(String error) {
 *         // Đăng nhập thất bại
 *     }
 * });
 */
public class LoginHelper {

    private static final String TAG = "LoginHelper";
    private final AuthRepository authRepository;

    public LoginHelper(Context context) {
        this.authRepository = new AuthRepositoryImpl(context);
    }

    /**
     * Đăng nhập bằng email và password
     *
     * @param email Email của user
     * @param password Mật khẩu
     * @param callback Callback để xử lý kết quả
     */
    public void loginWithEmail(String email, String password, LoginCallback callback) {
        Log.d(TAG, "loginWithEmail() called with email: " + email);

        // Validate input
        ValidationResult validation = validateLoginInput(email, password);
        if (!validation.isValid) {
            callback.onLoginError(validation.errorMessage);
            return;
        }

        // Call repository login (username = email)
        authRepository.login(email, password, new AuthRepository.OnAuthListener() {
            @Override
            public void onSuccess(User user, String token) {
                Log.d(TAG, "Login successful for email: " + email);
                callback.onLoginSuccess(user, token);
            }

            @Override
            public void onError(String error) {
                Log.e(TAG, "Login failed for email: " + email + ", error: " + error);
                callback.onLoginError(error);
            }
        });
    }

    /**
     * Kiểm tra xem user đã đăng nhập chưa
     *
     * @return true nếu đã đăng nhập, false nếu chưa
     */
    public boolean isLoggedIn() {
        return authRepository.isLoggedIn();
    }

    /**
     * Lấy user hiện tại đang đăng nhập
     *
     * @return User object hoặc null nếu chưa đăng nhập
     */
    public User getCurrentUser() {
        return authRepository.getCurrentUser();
    }

    /**
     * Lấy JWT token hiện tại
     *
     * @return JWT token hoặc null nếu chưa đăng nhập
     */
    public String getToken() {
        return authRepository.getToken();
    }

    /**
     * Đăng xuất và xóa session
     */
    public void logout() {
        Log.d(TAG, "logout() called");
        authRepository.logout();
    }

    /**
     * Validate input trước khi đăng nhập
     *
     * @param email Email
     * @param password Password
     * @return ValidationResult với trạng thái và error message
     */
    private ValidationResult validateLoginInput(String email, String password) {
        // Check email empty
        if (email == null || email.trim().isEmpty()) {
            return new ValidationResult(false, "Vui lòng nhập email");
        }

        // Check email format
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return new ValidationResult(false, "Email không hợp lệ");
        }

        // Check password empty
        if (password == null || password.trim().isEmpty()) {
            return new ValidationResult(false, "Vui lòng nhập mật khẩu");
        }

        // Check password length
        if (password.length() < 6) {
            return new ValidationResult(false, "Mật khẩu phải có ít nhất 6 ký tự");
        }

        return new ValidationResult(true, null);
    }

    /**
     * Validation result
     */
    private static class ValidationResult {
        boolean isValid;
        String errorMessage;

        ValidationResult(boolean isValid, String errorMessage) {
            this.isValid = isValid;
            this.errorMessage = errorMessage;
        }
    }

    /**
     * Callback interface cho login
     */
    public interface LoginCallback {
        /**
         * Được gọi khi đăng nhập thành công
         *
         * @param user User đã đăng nhập
         * @param token JWT token
         */
        void onLoginSuccess(User user, String token);

        /**
         * Được gọi khi đăng nhập thất bại
         *
         * @param error Thông báo lỗi
         */
        void onLoginError(String error);
    }
}
