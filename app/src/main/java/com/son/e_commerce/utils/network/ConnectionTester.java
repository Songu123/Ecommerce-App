package com.son.e_commerce.utils.network;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Connection Test Utility
 * Test kết nối đến server từ app
 */
public class ConnectionTester {

    private static final String TAG = "ConnectionTester";

    /**
     * Test connection và show dialog với kết quả
     */
    public static void testConnection(Context context) {
        String baseUrl = ApiConfig.getBaseUrl();
        String testUrl = baseUrl + "api/auth/test";

        AlertDialog loadingDialog = new AlertDialog.Builder(context)
                .setTitle("🔍 Kiểm tra kết nối")
                .setMessage("Đang test connection đến:\n" + testUrl)
                .setCancelable(false)
                .create();
        loadingDialog.show();

        // Test trong background thread
        new Thread(() -> {
            StringBuilder result = new StringBuilder();
            result.append("📊 CONNECTION TEST RESULT\n\n");
            result.append("Mode: ").append(ApiConfig.getModeName()).append("\n");
            result.append("URL: ").append(testUrl).append("\n\n");

            try {
                URL url = new URL(testUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(10000);
                connection.setReadTimeout(10000);
                connection.setRequestMethod("GET");

                int responseCode = connection.getResponseCode();
                String responseMessage = connection.getResponseMessage();

                connection.disconnect();

                result.append("✅ THÀNH CÔNG!\n\n");
                result.append("Status: ").append(responseCode).append(" ").append(responseMessage).append("\n");
                result.append("Server: OK\n\n");
                result.append("Bạn có thể login từ app!");

                Log.d(TAG, "Connection test SUCCESS: " + responseCode);

                // Show success dialog
                ((android.app.Activity) context).runOnUiThread(() -> {
                    loadingDialog.dismiss();
                    new AlertDialog.Builder(context)
                            .setTitle("✅ Kết nối thành công!")
                            .setMessage(result.toString())
                            .setPositiveButton("OK", null)
                            .show();
                });

            } catch (IOException e) {
                result.append("❌ THẤT BẠI!\n\n");
                result.append("Error: ").append(e.getMessage()).append("\n\n");
                result.append("KIỂM TRA:\n");
                result.append("1. Spring Boot có chạy?\n");
                result.append("2. Firewall có chặn?\n");
                result.append("3. Cùng WiFi với PC?\n");
                result.append("4. IP có đúng? (").append(ApiConfig.getBaseUrl()).append(")\n");

                Log.e(TAG, "Connection test FAILED", e);

                // Show error dialog
                ((android.app.Activity) context).runOnUiThread(() -> {
                    loadingDialog.dismiss();
                    new AlertDialog.Builder(context)
                            .setTitle("❌ Không kết nối được")
                            .setMessage(result.toString())
                            .setPositiveButton("OK", null)
                            .setNegativeButton("Hướng dẫn fix", (dialog, which) -> {
                                showFixGuide(context);
                            })
                            .show();
                });
            }
        }).start();
    }

    /**
     * Show fix guide
     */
    private static void showFixGuide(Context context) {
        String guide = "🔧 HƯỚNG DẪN FIX CONNECTION\n\n" +
                "CURRENT SETTINGS:\n" +
                "• Mode: " + ApiConfig.getModeName() + "\n" +
                "• URL: " + ApiConfig.getBaseUrl() + "\n\n" +
                "FIX STEPS:\n\n" +
                "1. START SERVER:\n" +
                "   cd spring-boot-project\n" +
                "   ./mvnw spring-boot:run\n\n" +
                "2. ADD TO application.properties:\n" +
                "   server.address=0.0.0.0\n\n" +
                "3. ALLOW FIREWALL:\n" +
                "   netsh advfirewall firewall add rule\n" +
                "   name=\"Spring Boot\" dir=in action=allow\n" +
                "   protocol=TCP localport=8080\n\n" +
                "4. SAME WIFI:\n" +
                "   PC và điện thoại cùng WiFi\n\n" +
                "5. TEST FROM PHONE BROWSER:\n" +
                "   " + ApiConfig.getBaseUrl() + "api/auth/test\n\n" +
                "Xem: CONNECTION_FIX_COMPLETE.md";

        new AlertDialog.Builder(context)
                .setTitle("📖 Hướng dẫn fix")
                .setMessage(guide)
                .setPositiveButton("OK", null)
                .show();
    }

    /**
     * Quick test (no dialog)
     */
    public static boolean quickTest() {
        try {
            URL url = new URL(ApiConfig.getBaseUrl() + "api/auth/test");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("GET");

            int code = connection.getResponseCode();
            connection.disconnect();

            return code == 200;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get connection info
     */
    public static String getConnectionInfo() {
        return "Mode: " + ApiConfig.getModeName() + "\n" +
               "Base URL: " + ApiConfig.getBaseUrl() + "\n" +
               "Test URL: " + ApiConfig.getBaseUrl() + "api/auth/test";
    }
}
