package com.son.e_commerce.model.repository;

import com.son.e_commerce.model.entity.User;

/**
 * Auth Repository Interface - JWT Authentication
 */
public interface AuthRepository {

    /**
     * Login with JWT authentication
     * @param username Username or Email (email is recommended)
     * @param password User's password
     * @param listener Callback for success or error
     */
    void login(String username, String password, OnAuthListener listener);

    /**
     * Login with email and password (convenience method)
     * @param email User's email address
     * @param password User's password
     * @param listener Callback for success or error
     */
    default void loginWithEmail(String email, String password, OnAuthListener listener) {
        login(email, password, listener);
    }

    /**
     * Register new user with JWT
     * @param username Username (can be same as email)
     * @param email User's email address
     * @param password User's password
     * @param fullName User's full name
     * @param listener Callback for success or error
     */
    void register(String username, String email, String password, String fullName, OnAuthListener listener);

    /**
     * Test API connection
     */
    void testApi(OnTestApiListener listener);

    /**
     * Logout and clear session
     */
    void logout();

    /**
     * Get current logged in user
     */
    User getCurrentUser();

    /**
     * Get JWT token
     */
    String getToken();

    /**
     * Check if user is logged in
     */
    boolean isLoggedIn();

    /**
     * Callback for authentication
     */
    interface OnAuthListener {
        void onSuccess(User user, String token);
        void onError(String error);
    }

    /**
     * Callback for test API
     */
    interface OnTestApiListener {
        void onSuccess(String message);
        void onError(String error);
    }
}
