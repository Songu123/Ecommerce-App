package com.son.e_commerce.utils.network;

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
     * Get base URL theo mode hiện tại
     */
    public static String getBaseUrl() {
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
        return CURRENT_MODE == Mode.EMULATOR;
    }

    /**
     * Check if using real device
     */
    public static boolean isRealDevice() {
        return CURRENT_MODE == Mode.REAL_DEVICE;
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
