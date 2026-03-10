package com.son.e_commerce.model.repository;

import com.son.e_commerce.model.entity.User;

public interface UserRepository {
    /**
     * Login user
     */
    void login(String username, String password, OnLoginListener listener);

    /**
     * Register new user
     */
    void register(User user, OnRegisterListener listener);

    /**
     * Get user by ID
     */
    void getUserById(int id, OnUserLoadedListener listener);

    /**
     * Update user profile
     */
    void updateUser(User user, OnUserUpdatedListener listener);

    /**
     * Logout user
     */
    void logout();

    /**
     * Get current logged in user
     */
    User getCurrentUser();

    /**
     * Callback for login
     */
    interface OnLoginListener {
        void onSuccess(User user);
        void onError(String error);
    }

    /**
     * Callback for register
     */
    interface OnRegisterListener {
        void onSuccess(User user);
        void onError(String error);
    }

    /**
     * Callback for loading user
     */
    interface OnUserLoadedListener {
        void onSuccess(User user);
        void onError(String error);
    }

    /**
     * Callback for updating user
     */
    interface OnUserUpdatedListener {
        void onSuccess(boolean updated);
        void onError(String error);
    }
}
