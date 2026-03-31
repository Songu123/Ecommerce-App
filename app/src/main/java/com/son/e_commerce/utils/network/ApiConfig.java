package com.son.e_commerce.utils.network;

import android.os.Build;

/**
 * API Configuration
 * Dễ dàng chuyển đổi giữa Emulator và Real Device
 */
public class ApiConfig {

    // ============================================================================
    // CONFIGURATION - THAY ĐỔI Ở ĐÂY
    // ============================================================================

    /**
     * MODE: Chọn môi trường đang dùng
     *
     * EMULATOR - Dùng khi test trên Android Emulator
     * REAL_DEVICE - Dùng khi test trên điện thoại thật
     * PRODUCTION - Dùng khi deploy production
     */
    public enum Mode {
        EMULATOR,       // Android Emulator
        REAL_DEVICE,    // Thiết bị thật
        PRODUCTION      // Production server
    }

    // ============================================================================
    // ⚠️ THAY ĐỔI MODE Ở ĐÂY ⚠️
    // ============================================================================

    // Note: We still provide a manual default, but the runtime will prefer the
    // emulator URL when the app is actually running on an emulator. This helps
    // avoid the common mistake of leaving REAL_DEVICE while testing on emulator.
    private static final Mode CURRENT_MODE = Mode.REAL_DEVICE;  // ← THAY ĐỔI MODE TẠI ĐÂY

    // ============================================================================
    // ⚠️ THAY ĐỔI IP MÁY TÍNH CỦA BẠN Ở ĐÂY ⚠️
    // ============================================================================

    /**
     * IP của máy tính chạy Spring Boot
     *
     * Cách tìm IP:
     * Windows: ipconfig | Select-String "IPv4"
     * Mac/Linux: ifconfig | grep "inet "
     *
     * IPs tìm thấy trên máy này:
     * - 172.20.10.8 (Hotspot/WiFi - ĐANG DÙNG) ⭐
     * - 192.168.225.1 (Virtual/Hotspot)
     * - 192.168.220.1 (Virtual/Hotspot)
     *
     * Thường là: 192.168.x.x hoặc 172.x.x.x
     */
    private static final String YOUR_PC_IP = "172.20.10.8";  // ← ĐÃ CẬP NHẬT IP MỚI

    // ============================================================================
    // URL CONFIGURATION
    // ============================================================================

    private static final String EMULATOR_BASE_URL = "http://10.0.2.2:8080/";
    private static final String REAL_DEVICE_BASE_URL = "http://" + YOUR_PC_IP + ":8080/";
    private static final String PRODUCTION_BASE_URL = "https://your-api-domain.com/";

    /**
     * Heuristic to detect emulator at runtime. This helps when developers forget
     * to change CURRENT_MODE while running the app on emulator.
     */
    private static boolean isProbablyEmulator() {
        // Common emulator indicators
        String fingerprint = Build.FINGERPRINT;
        String model = Build.MODEL;
        String product = Build.PRODUCT;
        String manufacturer = Build.MANUFACTURER;

        if (fingerprint.startsWith("generic") || fingerprint.startsWith("unknown")) return true;
        if (model.contains("google_sdk") || model.contains("Emulator") || model.contains("Android SDK built for x86")) return true;
        if (product.contains("sdk") || product.contains("google_sdk") || product.contains("sdk_x86")) return true;
        if (manufacturer.contains("Genymotion")) return true;
        // Additional checks
        if (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic")) return true;
        if (Build.HARDWARE.contains("ranchu") || Build.HARDWARE.contains("goldfish")) return true;

        return false;
    }

    /**
     * Get base URL theo mode hiện tại (auto-detect emulator)
     */
    public static String getBaseUrl() {
        // If app is running on emulator, prefer emulator base URL regardless of CURRENT_MODE
        if (isProbablyEmulator()) {
            return EMULATOR_BASE_URL;
        }

        switch (CURRENT_MODE) {
            case EMULATOR:
                return EMULATOR_BASE_URL;
            case REAL_DEVICE:
                return REAL_DEVICE_BASE_URL;
            case PRODUCTION:
                return PRODUCTION_BASE_URL;
            default:
                return EMULATOR_BASE_URL;
        }
    }

    /**
     * Get current mode
     */
    public static Mode getCurrentMode() {
        return CURRENT_MODE;
    }

    /**
     * Get mode name
     */
    public static String getModeName() {
        if (isProbablyEmulator()) {
            return "Auto-detected Emulator (10.0.2.2)";
        }

        switch (CURRENT_MODE) {
            case EMULATOR:
                return "Emulator (10.0.2.2)";
            case REAL_DEVICE:
                return "Real Device (" + YOUR_PC_IP + ")";
            case PRODUCTION:
                return "Production";
            default:
                return "Unknown";
        }
    }

    /**
     * Check if using emulator
     */
    public static boolean isEmulator() {
        return isProbablyEmulator() || CURRENT_MODE == Mode.EMULATOR;
    }

    /**
     * Check if using real device
     */
    public static boolean isRealDevice() {
        return !isProbablyEmulator() && CURRENT_MODE == Mode.REAL_DEVICE;
    }

    /**
     * Check if production
     */
    public static boolean isProduction() {
        return CURRENT_MODE == Mode.PRODUCTION;
    }

    /**
     * Get connection info for debugging
     */
    public static String getConnectionInfo() {
        return "Mode: " + getModeName() + "\n" +
               "URL: " + getBaseUrl() + "\n" +
               "Status: " + (isProduction() ? "Production" : "Development");
    }
}
