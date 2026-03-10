package com.son.e_commerce.examples;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.son.e_commerce.R;
import com.son.e_commerce.data.dto.AuthResponse;
import com.son.e_commerce.utils.auth.RegisterApiHelper;

public class RegisterExampleActivity extends AppCompatActivity {

    // UI Components
    private EditText etUsername;
    private EditText etEmail;
    private EditText etPassword;
    private EditText etConfirmPassword;
    private Button btnRegister;
    private ProgressBar progressBar;

    // API Helper
    private RegisterApiHelper registerApiHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_register_example);

        // Khởi tạo API Helper
        registerApiHelper = new RegisterApiHelper();

        // Khởi tạo views
        initViews();

        // Set click listener cho nút đăng ký
        btnRegister.setOnClickListener(v -> handleRegisterClick());
    }

    /**
     * Khởi tạo views
     */
    private void initViews() {
        // TODO: Uncomment và mapping với layout của bạn
        // etUsername = findViewById(R.id.etUsername);
        // etEmail = findViewById(R.id.etEmail);
        // etPassword = findViewById(R.id.etPassword);
        // etConfirmPassword = findViewById(R.id.etConfirmPassword);
        // btnRegister = findViewById(R.id.btnRegister);
        // progressBar = findViewById(R.id.progressBar);
    }

    /**
     * Xử lý khi click nút đăng ký
     */
    private void handleRegisterClick() {
        // Lấy dữ liệu từ form
        String username = etUsername.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString();
        String confirmPassword = etConfirmPassword.getText().toString();

        // Validate input
        if (!validateInput(username, email, password, confirmPassword)) {
            return;
        }

        // Hiển thị loading
        showLoading(true);

        // Gọi API đăng ký
        callRegisterApi(username, email, password);
    }

    /**
     * Validate input trước khi gọi API
     */
    private boolean validateInput(String username, String email, String password, String confirmPassword) {
        if (username.isEmpty()) {
            showToast("Vui lòng nhập tên đăng nhập");
            etUsername.requestFocus();
            return false;
        }

        if (username.length() < 3) {
            showToast("Tên đăng nhập phải có ít nhất 3 ký tự");
            etUsername.requestFocus();
            return false;
        }

        if (email.isEmpty()) {
            showToast("Vui lòng nhập email");
            etEmail.requestFocus();
            return false;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showToast("Email không hợp lệ");
            etEmail.requestFocus();
            return false;
        }

        if (password.isEmpty()) {
            showToast("Vui lòng nhập mật khẩu");
            etPassword.requestFocus();
            return false;
        }

        if (password.length() < 6) {
            showToast("Mật khẩu phải có ít nhất 6 ký tự");
            etPassword.requestFocus();
            return false;
        }

        if (confirmPassword.isEmpty()) {
            showToast("Vui lòng xác nhận mật khẩu");
            etConfirmPassword.requestFocus();
            return false;
        }

        if (!password.equals(confirmPassword)) {
            showToast("Mật khẩu xác nhận không khớp");
            etConfirmPassword.requestFocus();
            return false;
        }

        return true;
    }

    /**
     * GỌI API ĐĂNG KÝ - PHẦN QUAN TRỌNG NHẤT
     */
    private void callRegisterApi(String username, String email, String password) {
        registerApiHelper.register(username, email, password,
            new RegisterApiHelper.RegisterCallback() {
                @Override
                public void onRegisterSuccess(AuthResponse response) {
                    // ✅ ĐĂNG KÝ THÀNH CÔNG
                    runOnUiThread(() -> {
                        showLoading(false);

                        // Hiển thị thông báo thành công
                        showToast("Đăng ký thành công! Chào mừng " + response.getUsername());

                        // Lưu token vào SharedPreferences
                        saveAuthData(response);

                        // Chuyển sang màn hình chính
                        navigateToMainScreen();
                    });
                }

                @Override
                public void onRegisterError(String errorMessage) {
                    // ❌ ĐĂNG KÝ THẤT BẠI
                    runOnUiThread(() -> {
                        showLoading(false);

                        // Hiển thị thông báo lỗi
                        String userMessage = parseErrorMessage(errorMessage);
                        showToast(userMessage);
                    });
                }
            }
        );
    }

    /**
     * Lưu dữ liệu authentication sau khi đăng ký thành công
     */
    private void saveAuthData(AuthResponse response) {
        getSharedPreferences("auth_prefs", MODE_PRIVATE)
                .edit()
                .putString("jwt_token", response.getToken())
                .putBoolean("is_logged_in", true)
                .putInt("user_id", response.getId())
                .putString("username", response.getUsername())
                .putString("email", response.getEmail())
                .putString("full_name", response.getFullName())
                .putString("role", response.getRole())
                .apply();
    }

    /**
     * Chuyển sang màn hình chính
     */
    private void navigateToMainScreen() {
        // TODO: Chuyển sang màn hình chính của app
        // Intent intent = new Intent(this, MainActivityNew.class);
        // startActivity(intent);
        // finish();

        showToast("Đã đăng nhập! (TODO: Navigate to main screen)");
    }

    /**
     * Parse error message để hiển thị thông báo thân thiện
     */
    private String parseErrorMessage(String errorMessage) {
        if (errorMessage == null) {
            return "Đã có lỗi xảy ra";
        }

        if (errorMessage.contains("username") && errorMessage.contains("exist")) {
            return "Tên đăng nhập đã tồn tại";
        }

        if (errorMessage.contains("email") && errorMessage.contains("exist")) {
            return "Email đã được sử dụng";
        }

        if (errorMessage.contains("Lỗi kết nối") || errorMessage.contains("Unable to resolve host")) {
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
     * Hiển thị/ẩn loading
     */
    private void showLoading(boolean show) {
        if (progressBar != null) {
            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        }
        if (btnRegister != null) {
            btnRegister.setEnabled(!show);
            btnRegister.setText(show ? "Đang xử lý..." : "Đăng ký");
        }
    }

    /**
     * Hiển thị Toast
     */
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
