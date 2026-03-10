package com.son.e_commerce.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Token Manager - Manages JWT token and user data for API authentication
 */
public class TokenManager {

    private static final String PREF_NAME = "ECommerceAuthPrefs";
    private static final String KEY_TOKEN = "jwt_token";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_FULL_NAME = "full_name";
    private static final String KEY_ROLE = "role";

    private final SharedPreferences sharedPreferences;

    public TokenManager(Context context) {
        this.sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    /**
     * Get JWT token
     */
    public String getToken() {
        return sharedPreferences.getString(KEY_TOKEN, null);
    }

    /**
     * Save JWT token
     */
    public void saveToken(String token) {
        sharedPreferences.edit()
                .putString(KEY_TOKEN, token)
                .putBoolean(KEY_IS_LOGGED_IN, true)
                .apply();
    }

    /**
     * Save complete auth data (token + user info)
     */
    public void saveAuthData(String token, int userId, String username, String email, String fullName, String role) {
        sharedPreferences.edit()
                .putString(KEY_TOKEN, token)
                .putBoolean(KEY_IS_LOGGED_IN, true)
                .putInt(KEY_USER_ID, userId)
                .putString(KEY_USERNAME, username)
                .putString(KEY_EMAIL, email)
                .putString(KEY_FULL_NAME, fullName)
                .putString(KEY_ROLE, role)
                .apply();
    }

    /**
     * Clear all auth data (logout)
     */
    public void clearToken() {
        sharedPreferences.edit()
                .clear()
                .apply();
    }

    /**
     * Check if user is logged in
     */
    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false) && hasToken();
    }

    /**
     * Check if token exists
     */
    public boolean hasToken() {
        return getToken() != null && !getToken().isEmpty();
    }

    /**
     * Get user ID
     */
    public int getUserId() {
        return sharedPreferences.getInt(KEY_USER_ID, -1);
    }

    /**
     * Get username
     */
    public String getUsername() {
        return sharedPreferences.getString(KEY_USERNAME, null);
    }

    /**
     * Get email
     */
    public String getEmail() {
        return sharedPreferences.getString(KEY_EMAIL, null);
    }

    /**
     * Get full name
     */
    public String getFullName() {
        return sharedPreferences.getString(KEY_FULL_NAME, null);
    }

    /**
     * Get role
     */
    public String getRole() {
        return sharedPreferences.getString(KEY_ROLE, "USER");
    }

    /**
     * Get Authorization header value
     * Format: "Bearer {token}"
     */
    public String getAuthorizationHeader() {
        String token = getToken();
        if (token != null && !token.isEmpty()) {
            return "Bearer " + token;
        }
        return null;
    }
}
