package com.son.e_commerce.examples;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.son.e_commerce.data.dto.AuthResponse;
import com.son.e_commerce.utils.auth.RegisterApiHelper;

/**
 * VÍ DỤ: Cách sử dụng RegisterApiHelper để gọi API đăng ký
 *
 * File này chứa các ví dụ về cách gọi API POST /api/auth/register
 * với Retrofit và xử lý response thành công hoặc lỗi.
 */
public class RegisterApiExample {

    private static final String TAG = "RegisterApiExample";
    private Context context;
    private RegisterApiHelper registerApiHelper;

    public RegisterApiExample(Context context) {
        this.context = context;
        this.registerApiHelper = new RegisterApiHelper();
    }

    /**
     * VÍ DỤ 1: Đăng ký cơ bản với username, email, password
     */
    public void basicRegisterExample() {
        String username = "john_doe";
        String email = "john@example.com";
        String password = "password123";

        registerApiHelper.register(username, email, password,
            new RegisterApiHelper.RegisterCallback() {
                @Override
                public void onRegisterSuccess(AuthResponse response) {
                    // XỬ LÝ KHI ĐĂNG KÝ THÀNH CÔNG
                    Log.d(TAG, "✅ Đăng ký thành công!");
                    Log.d(TAG, "Token: " + response.getToken());
                    Log.d(TAG, "User ID: " + response.getId());
                    Log.d(TAG, "Username: " + response.getUsername());
                    Log.d(TAG, "Email: " + response.getEmail());
                    Log.d(TAG, "Full Name: " + response.getFullName());
                    Log.d(TAG, "Role: " + response.getRole());

                    // Lưu token vào SharedPreferences
                    saveTokenToPreferences(response.getToken());

                    // Hiển thị thông báo thành công
                    showToast("Đăng ký thành công!");

                    // Chuyển sang màn hình chính hoặc màn hình login
                    // navigateToMainScreen();
                }

                @Override
                public void onRegisterError(String errorMessage) {
                    // XỬ LÝ KHI ĐĂNG KÝ THẤT BẠI
                    Log.e(TAG, "❌ Đăng ký thất bại: " + errorMessage);

                    // Hiển thị thông báo lỗi
                    showToast("Đăng ký thất bại: " + errorMessage);

                    // Xử lý các lỗi cụ thể
                    if (errorMessage.contains("username already exists")) {
                        showToast("Tên đăng nhập đã tồn tại");
                    } else if (errorMessage.contains("email already exists")) {
                        showToast("Email đã được sử dụng");
                    } else if (errorMessage.contains("Lỗi kết nối")) {
                        showToast("Không thể kết nối đến server");
                    }
                }
            }
        );
    }

    /**
     * VÍ DỤ 2: Đăng ký với đầy đủ thông tin (bao gồm fullName)
     */
    public void registerWithFullNameExample() {
        String username = "jane_smith";
        String email = "jane@example.com";
        String password = "securePassword456";
        String fullName = "Jane Smith";

        registerApiHelper.registerWithFullName(username, email, password, fullName,
            new RegisterApiHelper.RegisterCallback() {
                @Override
                public void onRegisterSuccess(AuthResponse response) {
                    Log.d(TAG, "✅ Đăng ký thành công với tên đầy đủ!");
                    Log.d(TAG, "Full Name: " + response.getFullName());

                    // Lưu thông tin user
                    saveUserInfo(response);

                    // Chuyển màn hình
                    showToast("Chào mừng " + response.getFullName());
                }

                @Override
                public void onRegisterError(String errorMessage) {
                    Log.e(TAG, "❌ Lỗi: " + errorMessage);
                    showToast("Đăng ký thất bại");
                }
            }
        );
    }

    /**
     * VÍ DỤ 3: Đăng ký từ form input với validation
     */
    public void registerFromFormExample(String username, String email, String password, String confirmPassword) {
        // VALIDATION TRƯỚC KHI GỌI API
        if (username == null || username.trim().isEmpty()) {
            showToast("Vui lòng nhập tên đăng nhập");
            return;
        }

        if (email == null || email.trim().isEmpty()) {
            showToast("Vui lòng nhập email");
            return;
        }

        if (!isValidEmail(email)) {
            showToast("Email không hợp lệ");
            return;
        }

        if (password == null || password.length() < 6) {
            showToast("Mật khẩu phải có ít nhất 6 ký tự");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showToast("Mật khẩu xác nhận không khớp");
            return;
        }

        // GỌI API SAU KHI VALIDATION
        Log.d(TAG, "Đang đăng ký...");
        showToast("Đang xử lý đăng ký...");

        registerApiHelper.register(username, email, password,
            new RegisterApiHelper.RegisterCallback() {
                @Override
                public void onRegisterSuccess(AuthResponse response) {
                    Log.d(TAG, "✅ Đăng ký thành công!");

                    // Lưu token và user info
                    saveTokenToPreferences(response.getToken());
                    saveUserInfo(response);

                    showToast("Đăng ký thành công! Chào mừng " + response.getUsername());

                    // TODO: Navigate to main screen
                    // Intent intent = new Intent(context, MainActivityNew.class);
                    // context.startActivity(intent);
                    // ((Activity) context).finish();
                }

                @Override
                public void onRegisterError(String errorMessage) {
                    Log.e(TAG, "❌ Đăng ký thất bại: " + errorMessage);

                    // Parse error message để hiển thị thông báo phù hợp
                    String userMessage = parseErrorMessage(errorMessage);
                    showToast(userMessage);
                }
            }
        );
    }

    /**
     * VÍ DỤ 4: Sử dụng trong Fragment hoặc Activity
     */
    public void registerInFragmentOrActivityExample(
            String username,
            String email,
            String password,
            Runnable onSuccessCallback,
            Runnable onErrorCallback) {

        registerApiHelper.register(username, email, password,
            new RegisterApiHelper.RegisterCallback() {
                @Override
                public void onRegisterSuccess(AuthResponse response) {
                    // Lưu dữ liệu
                    saveTokenToPreferences(response.getToken());
                    saveUserInfo(response);

                    // Thực hiện callback
                    if (onSuccessCallback != null) {
                        onSuccessCallback.run();
                    }
                }

                @Override
                public void onRegisterError(String errorMessage) {
                    Log.e(TAG, errorMessage);
                    showToast(parseErrorMessage(errorMessage));

                    // Thực hiện callback
                    if (onErrorCallback != null) {
                        onErrorCallback.run();
                    }
                }
            }
        );
    }

    // ==================== HELPER METHODS ====================

    /**
     * Lưu token vào SharedPreferences
     */
    private void saveTokenToPreferences(String token) {
        if (context != null) {
            context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
                    .edit()
                    .putString("jwt_token", token)
                    .putBoolean("is_logged_in", true)
                    .apply();
            Log.d(TAG, "Token đã được lưu");
        }
    }

    /**
     * Lưu thông tin user vào SharedPreferences
     */
    private void saveUserInfo(AuthResponse response) {
        if (context != null) {
            context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
                    .edit()
                    .putInt("user_id", response.getId())
                    .putString("username", response.getUsername())
                    .putString("email", response.getEmail())
                    .putString("full_name", response.getFullName())
                    .putString("role", response.getRole())
                    .apply();
            Log.d(TAG, "Thông tin user đã được lưu");
        }
    }

    /**
     * Validate email format
     */
    private boolean isValidEmail(String email) {
        return email != null && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    /**
     * Parse error message để hiển thị cho user
     */
    private String parseErrorMessage(String errorMessage) {
        if (errorMessage == null) {
            return "Đã có lỗi xảy ra";
        }

        if (errorMessage.contains("username already exists") ||
            errorMessage.contains("Username already taken")) {
            return "Tên đăng nhập đã tồn tại";
        }

        if (errorMessage.contains("email already exists") ||
            errorMessage.contains("Email already registered")) {
            return "Email đã được sử dụng";
        }

        if (errorMessage.contains("Lỗi kết nối") ||
            errorMessage.contains("Unable to resolve host") ||
            errorMessage.contains("Failed to connect")) {
            return "Không thể kết nối đến server. Vui lòng kiểm tra kết nối mạng.";
        }

        if (errorMessage.contains("HTTP 400")) {
            return "Dữ liệu không hợp lệ";
        }

        if (errorMessage.contains("HTTP 500")) {
            return "Lỗi server. Vui lòng thử lại sau.";
        }

        return "Đăng ký thất bại. Vui lòng thử lại.";
    }

    /**
     * Hiển thị Toast message
     */
    private void showToast(String message) {
        if (context != null) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }
}
