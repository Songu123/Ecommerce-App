package com.son.e_commerce.utils.auth;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.son.e_commerce.data.api.AuthApiService;
import com.son.e_commerce.utils.network.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Quick Server Test Utility
 * Sử dụng để kiểm tra server connection
 */
public class ServerTestUtil {

    private static final String TAG = "ServerTestUtil";

    /**
     * Test server connection và hiển thị kết quả
     */
    public static void testServerConnection(Context context) {
        AuthApiService apiService = ApiClient.getAuthApiService();

        Log.d(TAG, "Testing server connection...");

        // Show loading dialog
        AlertDialog loadingDialog = new AlertDialog.Builder(context)
                .setTitle("Đang kiểm tra server...")
                .setMessage("Vui lòng đợi...")
                .setCancelable(false)
                .create();
        loadingDialog.show();

        apiService.testApi().enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                loadingDialog.dismiss();

                if (response.isSuccessful()) {
                    String message = "✅ SERVER HOẠT ĐỘNG TỐT!\n\n" +
                            "Status: " + response.code() + " OK\n" +
                            "Response: " + response.body() + "\n\n" +
                            "Server sẵn sàng nhận request!";

                    Log.d(TAG, "Server OK: " + message);

                    new AlertDialog.Builder(context)
                            .setTitle("✅ Kết nối thành công")
                            .setMessage(message)
                            .setPositiveButton("OK", null)
                            .show();
                } else {
                    String error = "⚠️ SERVER CÓ VẤN ĐỀ\n\n" +
                            "HTTP Status: " + response.code() + "\n\n" +
                            "Server đang chạy nhưng có lỗi cấu hình.";

                    Log.e(TAG, "Server error: " + error);

                    new AlertDialog.Builder(context)
                            .setTitle("⚠️ Cảnh báo")
                            .setMessage(error)
                            .setPositiveButton("OK", null)
                            .show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                loadingDialog.dismiss();

                String error = "❌ KHÔNG KẾT NỐI ĐƯỢC SERVER\n\n" +
                        "Lỗi: " + t.getMessage() + "\n\n" +
                        "KIỂM TRA:\n" +
                        "1. Spring Boot server có chạy?\n" +
                        "2. Port 8080 có mở?\n" +
                        "3. Firewall có chặn?\n" +
                        "4. IP address có đúng? (10.0.2.2 for emulator)";

                Log.e(TAG, "Connection failed: " + error, t);

                new AlertDialog.Builder(context)
                        .setTitle("❌ Lỗi kết nối")
                        .setMessage(error)
                        .setPositiveButton("OK", null)
                        .setNegativeButton("Xem hướng dẫn", (dialog, which) -> {
                            showFixGuide(context);
                        })
                        .show();
            }
        });
    }

    /**
     * Show fix guide dialog
     */
    private static void showFixGuide(Context context) {
        String guide = "🔧 HƯỚNG DẪN FIX LỖI\n\n" +
                "1. CHECK SERVER:\n" +
                "   - Mở terminal\n" +
                "   - cd your-spring-boot-project\n" +
                "   - ./mvnw spring-boot:run\n\n" +
                "2. CHECK PORT:\n" +
                "   - Server phải chạy port 8080\n" +
                "   - Test: http://localhost:8080\n\n" +
                "3. FIX JWT SECRET:\n" +
                "   - Mở application.properties\n" +
                "   - jwt.secret=... (phải >= 32 ký tự)\n\n" +
                "4. RESTART SERVER:\n" +
                "   - Ctrl+C để stop\n" +
                "   - ./mvnw spring-boot:run để start\n\n" +
                "5. TEST LẠI:\n" +
                "   - Mở app và thử lại";

        new AlertDialog.Builder(context)
                .setTitle("📖 Hướng dẫn fix lỗi")
                .setMessage(guide)
                .setPositiveButton("OK", null)
                .show();
    }

    /**
     * Show JWT error fix guide
     */
    public static void showJwtErrorGuide(Context context) {
        String guide = "🚨 LỖI JWT SECRET KEY\n\n" +
                "Server trả về lỗi:\n" +
                "\"JWT signing key's size is not secure enough\"\n\n" +
                "NGUYÊN NHÂN:\n" +
                "JWT secret key trên server < 256 bits\n\n" +
                "CÁCH FIX:\n\n" +
                "1. Mở file application.properties trong Spring Boot\n\n" +
                "2. Thay đổi jwt.secret thành:\n" +
                "jwt.secret=3K9mNpQ7rS2vX5yB8eG1hJ4lM6nP9qR3tU6vW8xZ0aC2dF5gH7iK0mL3oN6pQ9sT\n\n" +
                "3. Hoặc dùng script generate_jwt_key.ps1\n\n" +
                "4. Restart server\n\n" +
                "5. Thử lại từ app";

        new AlertDialog.Builder(context)
                .setTitle("🔧 Fix JWT Error")
                .setMessage(guide)
                .setPositiveButton("OK", null)
                .show();
    }
}
