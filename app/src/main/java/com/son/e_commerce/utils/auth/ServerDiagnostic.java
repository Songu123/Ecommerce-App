package com.son.e_commerce.utils.auth;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.son.e_commerce.data.api.AuthApiService;
import com.son.e_commerce.utils.network.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Server Diagnostic Utility
 * Kiểm tra kết nối và trạng thái server
 */
public class ServerDiagnostic {

    private static final String TAG = "ServerDiagnostic";
    private final AuthApiService authApiService;
    private final Context context;

    public ServerDiagnostic(Context context) {
        this.context = context;
        this.authApiService = ApiClient.getAuthApiService();
    }

    /**
     * Test kết nối đến server
     */
    public void testServerConnection(TestCallback callback) {
        Log.d(TAG, "Testing server connection...");

        authApiService.testApi().enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.isSuccessful()) {
                    String result = "✅ Server đang hoạt động!\n" +
                            "Status: " + response.code() + "\n" +
                            "Response: " + response.body();
                    Log.d(TAG, result);
                    callback.onSuccess(result);
                } else {
                    String error = "⚠️ Server phản hồi nhưng có lỗi:\n" +
                            "Status: " + response.code();
                    Log.e(TAG, error);
                    callback.onError(error);
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                String error = "❌ Không thể kết nối đến server:\n" + t.getMessage();
                Log.e(TAG, error, t);
                callback.onError(error);
            }
        });
    }

    /**
     * Hiển thị hướng dẫn fix lỗi JWT
     */
    public static String getJwtErrorFixGuide() {
        return "🔧 LỖI JWT SECRET KEY\n\n" +
                "Server JWT secret key quá ngắn!\n\n" +
                "CÁCH FIX:\n" +
                "1. Mở file application.properties trong Spring Boot\n" +
                "2. Thay đổi:\n" +
                "   jwt.secret=myVerySecureSecretKeyForJWTToken1234567890ABCDEFGH\n" +
                "3. Restart server\n" +
                "4. Thử lại từ app\n\n" +
                "Key phải có ít nhất 32 ký tự!";
    }

    /**
     * Hiển thị hướng dẫn fix lỗi timeout
     */
    public static String getTimeoutErrorFixGuide() {
        return "🔧 LỖI KẾT NỐI TIMEOUT\n\n" +
                "KIỂM TRA:\n" +
                "1. Spring Boot server có đang chạy không?\n" +
                "   → ./mvnw spring-boot:run\n\n" +
                "2. Server đang chạy port 8080?\n" +
                "   → http://localhost:8080\n\n" +
                "3. Firewall có chặn port 8080?\n" +
                "   → Tắt tạm hoặc allow port 8080\n\n" +
                "4. Dùng emulator?\n" +
                "   → Dùng IP: 10.0.2.2:8080\n\n" +
                "5. Dùng thiết bị thật?\n" +
                "   → Dùng IP máy tính (192.168.x.x:8080)";
    }

    /**
     * Parse error và trả về hướng dẫn fix
     */
    public static String getErrorGuide(String errorMessage) {
        if (errorMessage == null) {
            return "Lỗi không xác định";
        }

        // JWT error
        if (errorMessage.contains("signing key") || errorMessage.contains("256 bits")) {
            return getJwtErrorFixGuide();
        }

        // Timeout error
        if (errorMessage.contains("timeout") || errorMessage.contains("failed to connect")) {
            return getTimeoutErrorFixGuide();
        }

        // Invalid credentials
        if (errorMessage.contains("401") || errorMessage.contains("Unauthorized")) {
            return "❌ Email hoặc mật khẩu không đúng!\n\n" +
                    "Hoặc user chưa tồn tại trong database.\n" +
                    "Hãy thử đăng ký tài khoản mới trước.";
        }

        // User already exists
        if (errorMessage.contains("409") || errorMessage.contains("already exists")) {
            return "⚠️ Email đã được đăng ký!\n\n" +
                    "Vui lòng sử dụng email khác hoặc đăng nhập.";
        }

        return errorMessage;
    }

    public interface TestCallback {
        void onSuccess(String message);
        void onError(String error);
    }
}
