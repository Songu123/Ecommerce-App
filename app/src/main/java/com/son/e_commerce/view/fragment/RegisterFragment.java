package com.son.e_commerce.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
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

public class RegisterFragment extends Fragment {

    private TextInputEditText editTextFullName;
    private TextInputEditText editTextEmail;
    private TextInputEditText editTextPassword;
    private TextInputEditText editTextConfirmPassword;
    private CheckBox checkBoxTerms;
    private AppCompatButton buttonRegister;
    private ProgressBar progressBar;
    private TextView textViewLogin;

    private AuthRepository authRepository;

    public static RegisterFragment newInstance() {
        return new RegisterFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        initViews(view);
        setupRepository();
        setupListeners();

        return view;
    }

    private void initViews(View view) {
        editTextFullName = view.findViewById(R.id.editTextFullName);
        editTextEmail = view.findViewById(R.id.editTextEmail);
        editTextPassword = view.findViewById(R.id.editTextPassword);
        editTextConfirmPassword = view.findViewById(R.id.editTextConfirmPassword);
        checkBoxTerms = view.findViewById(R.id.checkBoxTerms);
        buttonRegister = view.findViewById(R.id.buttonRegister);
        progressBar = view.findViewById(R.id.progressBar);
        textViewLogin = view.findViewById(R.id.textViewLogin);
    }

    private void setupRepository() {
        // 🎭 MOCK MODE - Test UI without server (no 403 error!)
        // Can register any email - will work instantly!
        authRepository = new MockAuthRepository(requireContext());

        // ⏸️ Switch back to real API when server is ready:
        // authRepository = new AuthRepositoryImpl(requireContext());
    }

    private void setupListeners() {
        buttonRegister.setOnClickListener(v -> attemptRegister());

        textViewLogin.setOnClickListener(v -> navigateToLogin());
    }

    private void attemptRegister() {
        String fullName = editTextFullName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String confirmPassword = editTextConfirmPassword.getText().toString().trim();

        // Validation
        if (TextUtils.isEmpty(fullName)) {
            editTextFullName.setError("Vui lòng nhập họ và tên");
            editTextFullName.requestFocus();
            return;
        }

        if (fullName.length() < 3) {
            editTextFullName.setError("Họ và tên phải có ít nhất 3 ký tự");
            editTextFullName.requestFocus();
            return;
        }

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

        if (TextUtils.isEmpty(confirmPassword)) {
            editTextConfirmPassword.setError("Vui lòng xác nhận mật khẩu");
            editTextConfirmPassword.requestFocus();
            return;
        }

        if (!password.equals(confirmPassword)) {
            editTextConfirmPassword.setError("Mật khẩu không khớp");
            editTextConfirmPassword.requestFocus();
            return;
        }

        if (!checkBoxTerms.isChecked()) {
            Toast.makeText(requireContext(), "Vui lòng đồng ý với điều khoản và điều kiện", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show loading
        showLoading();

        // Call register API with JWT authentication
        // Use email as username
        authRepository.register(email, email, password, fullName, new AuthRepository.OnAuthListener() {
            @Override
            public void onSuccess(User user, String token) {
                hideLoading();
                Toast.makeText(requireContext(), "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                navigateToHome();
            }

            @Override
            public void onError(String error) {
                hideLoading();

                // Show detailed error message
                String errorMessage = getDetailedErrorMessage(error);
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show();
                Log.e("RegisterFragment", "Register error: " + error);
            }
        });
    }

    /**
     * Get detailed user-friendly error message
     */
    private String getDetailedErrorMessage(String error) {
        if (error == null) {
            return "Đăng ký thất bại";
        }

        // JWT Secret Key error
        if (error.contains("signing key") || error.contains("256 bits")) {
            return "⚠️ Lỗi cấu hình server!\n\nServer JWT secret key không đúng.\nVui lòng liên hệ admin để fix server.";
        }

        // Timeout error
        if (error.contains("timeout") || error.contains("failed to connect")) {
            return "❌ Không kết nối được server!\n\nKiểm tra:\n1. Server có chạy không?\n2. Địa chỉ IP có đúng?";
        }

        // User already exists
        if (error.contains("409") || error.contains("already exists") || error.contains("đã được đăng ký")) {
            return "⚠️ Email đã được đăng ký!\n\nVui lòng sử dụng email khác.";
        }

        // Server error
        if (error.contains("500")) {
            return "❌ Lỗi server!\n\nVui lòng thử lại sau.";
        }

        return "Đăng ký thất bại: " + error;
    }

    private void navigateToLogin() {
        if (getActivity() != null) {
            ((com.son.e_commerce.AuthActivity) getActivity()).showLoginFragment();
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
        buttonRegister.setEnabled(false);
        editTextFullName.setEnabled(false);
        editTextEmail.setEnabled(false);
        editTextPassword.setEnabled(false);
        editTextConfirmPassword.setEnabled(false);
        checkBoxTerms.setEnabled(false);
    }

    private void hideLoading() {
        progressBar.setVisibility(View.GONE);
        buttonRegister.setEnabled(true);
        editTextFullName.setEnabled(true);
        editTextEmail.setEnabled(true);
        editTextPassword.setEnabled(true);
        editTextConfirmPassword.setEnabled(true);
        checkBoxTerms.setEnabled(true);
    }
}
