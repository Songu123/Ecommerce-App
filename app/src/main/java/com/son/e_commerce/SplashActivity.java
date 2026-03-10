package com.son.e_commerce;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.son.e_commerce.utils.TokenManager;

/**
 * Splash Activity - Màn hình khởi động
 * Kiểm tra authentication và điều hướng đến Login hoặc Main
 */
public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "SplashActivity";
    private static final long SPLASH_DELAY = 1500; // 1.5 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Kiểm tra authentication sau delay
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            checkAuthenticationAndNavigate();
        }, SPLASH_DELAY);
    }

    /**
     * Kiểm tra xem user đã đăng nhập chưa
     * Nếu đã đăng nhập -> MainActivityNew
     * Nếu chưa -> AuthActivity (Login)
     */
    private void checkAuthenticationAndNavigate() {
        TokenManager tokenManager = new TokenManager(this);

        if (tokenManager.isLoggedIn()) {
            // User đã đăng nhập
            Log.d(TAG, "User đã đăng nhập - Username: " + tokenManager.getUsername());
            navigateToMain();
        } else {
            // User chưa đăng nhập
            Log.d(TAG, "User chưa đăng nhập - Chuyển đến màn hình login");
            navigateToAuth();
        }
    }

    /**
     * Chuyển đến MainActivityNew
     */
    private void navigateToMain() {
        Intent intent = new Intent(this, MainActivityNew.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    /**
     * Chuyển đến AuthActivity (Login)
     */
    private void navigateToAuth() {
        Intent intent = new Intent(this, AuthActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
