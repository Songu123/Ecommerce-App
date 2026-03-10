package com.son.e_commerce.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.son.e_commerce.MainActivityNew;
import com.son.e_commerce.R;
import com.son.e_commerce.data.AuthRepositoryImpl;
import com.son.e_commerce.data.MockAuthRepository;
import com.son.e_commerce.model.entity.User;
import com.son.e_commerce.model.repository.AuthRepository;

public class LoginFragment extends Fragment {

    private TextInputEditText editTextEmail;
    private TextInputEditText editTextPassword;
    private AppCompatButton buttonLogin;
    private ProgressBar progressBar;
    private TextView textViewForgotPassword;
    private TextView textViewRegister;

    private AuthRepository authRepository;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        initViews(view);
        setupRepository();
        setupListeners();

        return view;
    }

    private void initViews(View view) {
        editTextEmail = view.findViewById(R.id.editTextEmail);
        editTextPassword = view.findViewById(R.id.editTextPassword);
        buttonLogin = view.findViewById(R.id.buttonLogin);
        progressBar = view.findViewById(R.id.progressBar);
        textViewForgotPassword = view.findViewById(R.id.textViewForgotPassword);
        textViewRegister = view.findViewById(R.id.textViewRegister);
    }

    private void setupRepository() {
        // 🎭 MOCK MODE - Test UI without server (no 403 error!)
        // Pre-registered test accounts:
        // - test@test.com / password123
        // - admin@test.com / admin123
        authRepository = new MockAuthRepository(requireContext());

        // ⏸️ Switch back to real API when server is ready:
        // authRepository = new AuthRepositoryImpl(requireContext());
    }

    private void setupListeners() {
        buttonLogin.setOnClickListener(v -> attemptLogin());

        textViewRegister.setOnClickListener(v -> navigateToRegister());

        textViewForgotPassword.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Tính năng đang phát triển", Toast.LENGTH_SHORT).show();
        });

        // Social login buttons (placeholder) - will be initialized later
    }

    private void attemptLogin() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        // Validation
        if (TextUtils.isEmpty(email)) {
            editTextEmail.setError("Vui lòng nhập email");
            editTextEmail.requestFocus();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Email không hợp lệ");
            editTextEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError("Vui lòng nhập mật khẩu");
            editTextPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            editTextPassword.setError("Mật khẩu phải có ít nhất 6 ký tự");
            editTextPassword.requestFocus();
            return;
        }

        // Show loading
        showLoading();

        // Call login API with JWT authentication
        authRepository.login(email, password, new AuthRepository.OnAuthListener() {
            @Override
            public void onSuccess(User user, String token) {
                hideLoading();
                Toast.makeText(requireContext(), "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                navigateToHome();
            }

            @Override
            public void onError(String error) {
                hideLoading();

                // Show detailed error message
                String errorMessage = getDetailedErrorMessage(error);
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show();
                Log.e("LoginFragment", "Login error: " + error);
            }
        });
    }

    /**
     * Get detailed user-friendly error message
     */
    private String getDetailedErrorMessage(String error) {
        if (error == null) {
            return "Đăng nhập thất bại";
        }

        // JWT Secret Key error
        if (error.contains("signing key") || error.contains("256 bits")) {
            return "⚠️ Lỗi cấu hình server!\n\nServer JWT secret key không đúng.\nVui lòng liên hệ admin để fix server.";
        }

        // Timeout error
        if (error.contains("timeout") || error.contains("failed to connect")) {
            return "❌ Không kết nối được server!\n\nKiểm tra:\n1. Server có chạy không?\n2. Địa chỉ IP có đúng?";
        }

        // Invalid credentials
        if (error.contains("401") || error.contains("Unauthorized") || error.contains("không đúng")) {
            return "❌ Email hoặc mật khẩu không đúng!\n\nHoặc tài khoản chưa tồn tại.";
        }

        // Server error
        if (error.contains("500")) {
            return "❌ Lỗi server!\n\nVui lòng thử lại sau.";
        }

        return "Đăng nhập thất bại: " + error;
    }

    private void navigateToRegister() {
        if (getActivity() != null) {
            ((com.son.e_commerce.AuthActivity) getActivity()).showRegisterFragment();
        }
    }

    private void navigateToHome() {
        Intent intent = new Intent(requireContext(), MainActivityNew.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        requireActivity().finish();
    }

    private void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
        buttonLogin.setEnabled(false);
        editTextEmail.setEnabled(false);
        editTextPassword.setEnabled(false);
    }

    private void hideLoading() {
        progressBar.setVisibility(View.GONE);
        buttonLogin.setEnabled(true);
        editTextEmail.setEnabled(true);
        editTextPassword.setEnabled(true);
    }
}
