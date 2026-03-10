package com.son.e_commerce.examples;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.son.e_commerce.MainActivityNew;
import com.son.e_commerce.R;
import com.son.e_commerce.model.entity.User;
import com.son.e_commerce.utils.auth.LoginHelper;

/**
 * Example Activity: Đăng nhập bằng Email và Password
 *
 * File này là ví dụ về cách sử dụng LoginHelper để xử lý đăng nhập
 * Bạn có thể tham khảo code này để implement trong Fragment hoặc Activity của bạn
 */
public class EmailLoginExampleActivity extends AppCompatActivity {

    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonLogin;
    private ProgressBar progressBar;

    private LoginHelper loginHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Assume you have a layout file
        // setContentView(R.layout.activity_email_login_example);

        // Initialize views
        // editTextEmail = findViewById(R.id.editTextEmail);
        // editTextPassword = findViewById(R.id.editTextPassword);
        // buttonLogin = findViewById(R.id.buttonLogin);
        // progressBar = findViewById(R.id.progressBar);

        // Initialize LoginHelper
        loginHelper = new LoginHelper(this);

        // Check if user is already logged in
        if (loginHelper.isLoggedIn()) {
            User currentUser = loginHelper.getCurrentUser();
            Toast.makeText(this, "Already logged in as: " + currentUser.getEmail(), Toast.LENGTH_SHORT).show();
            navigateToMainActivity();
            return;
        }

        // Setup login button click listener
        setupLoginButton();
    }

    /**
     * Setup login button
     */
    private void setupLoginButton() {
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });
    }

    /**
     * Xử lý đăng nhập
     */
    private void attemptLogin() {
        // Get input values
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        // Show loading
        showLoading(true);

        // Call LoginHelper
        loginHelper.loginWithEmail(email, password, new LoginHelper.LoginCallback() {
            @Override
            public void onLoginSuccess(User user, String token) {
                // Hide loading
                showLoading(false);

                // Show success message
                Toast.makeText(EmailLoginExampleActivity.this,
                    "Đăng nhập thành công!\nChào mừng " + user.getFullName(),
                    Toast.LENGTH_LONG).show();

                // Navigate to main activity
                navigateToMainActivity();
            }

            @Override
            public void onLoginError(String error) {
                // Hide loading
                showLoading(false);

                // Show error message
                Toast.makeText(EmailLoginExampleActivity.this,
                    "Đăng nhập thất bại:\n" + error,
                    Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Show/hide loading
     */
    private void showLoading(boolean show) {
        if (show) {
            progressBar.setVisibility(View.VISIBLE);
            buttonLogin.setEnabled(false);
            editTextEmail.setEnabled(false);
            editTextPassword.setEnabled(false);
        } else {
            progressBar.setVisibility(View.GONE);
            buttonLogin.setEnabled(true);
            editTextEmail.setEnabled(true);
            editTextPassword.setEnabled(true);
        }
    }

    /**
     * Navigate to main activity after successful login
     */
    private void navigateToMainActivity() {
        Intent intent = new Intent(this, MainActivityNew.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    // ============================================================================
    // EXAMPLE 2: Simple Login (Minimal Code)
    // ============================================================================

    /**
     * Ví dụ đơn giản nhất để đăng nhập
     */
    private void simpleLoginExample() {
        LoginHelper helper = new LoginHelper(this);

        helper.loginWithEmail("test@gmail.com", "password123", new LoginHelper.LoginCallback() {
            @Override
            public void onLoginSuccess(User user, String token) {
                Toast.makeText(EmailLoginExampleActivity.this, "Success!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLoginError(String error) {
                Toast.makeText(EmailLoginExampleActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // ============================================================================
    // EXAMPLE 3: Check Login Status
    // ============================================================================

    /**
     * Kiểm tra trạng thái đăng nhập
     */
    private void checkLoginStatusExample() {
        LoginHelper helper = new LoginHelper(this);

        if (helper.isLoggedIn()) {
            User user = helper.getCurrentUser();
            String token = helper.getToken();

            Toast.makeText(this,
                "Logged in as: " + user.getEmail() + "\nToken: " + token.substring(0, 20) + "...",
                Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Not logged in", Toast.LENGTH_SHORT).show();
        }
    }

    // ============================================================================
    // EXAMPLE 4: Logout
    // ============================================================================

    /**
     * Đăng xuất
     */
    private void logoutExample() {
        LoginHelper helper = new LoginHelper(this);
        helper.logout();

        Toast.makeText(this, "Đã đăng xuất", Toast.LENGTH_SHORT).show();

        // Navigate to login screen
        // Intent intent = new Intent(this, AuthActivity.class);
        // startActivity(intent);
        // finish();
    }
}
