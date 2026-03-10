package com.son.e_commerce.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.son.e_commerce.model.entity.User;
import com.son.e_commerce.model.repository.AuthRepository;

import java.util.HashMap;
import java.util.Map;

/**
 * Mock Auth Repository - FOR TESTING ONLY
 * Sử dụng khi server chưa sẵn sàng
 *
 * Để sử dụng: Thay AuthRepositoryImpl bằng MockAuthRepository trong Fragment
 */
public class MockAuthRepository implements AuthRepository {

    private static final String TAG = "MockAuthRepository";
    private static final String PREF_NAME = "ECommerceAuthPrefs";
    private static final String KEY_TOKEN = "jwt_token";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_FULL_NAME = "full_name";
    private static final String KEY_ROLE = "role";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";

    private final SharedPreferences sharedPreferences;
    private final Handler handler;
    private User currentUser;
    private String jwtToken;

    // Mock database
    private static final Map<String, MockUserData> mockUsers = new HashMap<>();

    static {
        // Pre-registered test users
        mockUsers.put("test@test.com", new MockUserData(1, "test", "test@test.com", "password123", "Test User"));
        mockUsers.put("admin@test.com", new MockUserData(2, "admin", "admin@test.com", "admin123", "Admin User"));
    }

    public MockAuthRepository(Context context) {
        this.sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        this.handler = new Handler(Looper.getMainLooper());
        loadCurrentUser();
    }

    @Override
    public void login(String username, String password, OnAuthListener listener) {
        Log.d(TAG, "🎭 MOCK login() called with username: " + username);

        // Simulate network delay
        handler.postDelayed(() -> {
            MockUserData userData = mockUsers.get(username);

            if (userData != null && userData.password.equals(password)) {
                // Success
                User user = new User();
                user.setId(userData.id);
                user.setUsername(userData.username);
                user.setEmail(userData.email);
                user.setFullName(userData.fullName);
                user.setRole("USER");

                String token = generateMockToken(user);
                saveAuthData(user, token);

                Log.d(TAG, "✅ Mock login successful for: " + username);
                listener.onSuccess(user, token);
            } else {
                // Failed
                Log.e(TAG, "❌ Mock login failed for: " + username);
                listener.onError("Email hoặc mật khẩu không đúng");
            }
        }, 500); // 500ms delay to simulate network
    }

    @Override
    public void register(String username, String email, String password, String fullName, OnAuthListener listener) {
        Log.d(TAG, "🎭 MOCK register() called with email: " + email);

        // Simulate network delay
        handler.postDelayed(() -> {
            // Check if user already exists
            if (mockUsers.containsKey(email)) {
                Log.e(TAG, "❌ Mock register failed: Email already exists");
                listener.onError("Email đã được đăng ký");
                return;
            }

            // Create new user
            int newId = mockUsers.size() + 1;
            MockUserData newUserData = new MockUserData(newId, username, email, password, fullName);
            mockUsers.put(email, newUserData);

            User user = new User();
            user.setId(newId);
            user.setUsername(username);
            user.setEmail(email);
            user.setFullName(fullName);
            user.setRole("USER");

            String token = generateMockToken(user);
            saveAuthData(user, token);

            Log.d(TAG, "✅ Mock register successful for: " + email);
            listener.onSuccess(user, token);
        }, 500); // 500ms delay
    }

    @Override
    public void testApi(OnTestApiListener listener) {
        Log.d(TAG, "🎭 MOCK testApi() called");

        handler.postDelayed(() -> {
            listener.onSuccess("Mock API is working!");
        }, 300);
    }

    @Override
    public void logout() {
        Log.d(TAG, "🎭 Mock logout()");

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        currentUser = null;
        jwtToken = null;
    }

    @Override
    public User getCurrentUser() {
        return currentUser;
    }

    @Override
    public String getToken() {
        return jwtToken;
    }

    @Override
    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false) && jwtToken != null;
    }

    /**
     * Generate mock JWT token
     */
    private String generateMockToken(User user) {
        // Mock JWT token format
        return "mock.jwt.token." + user.getId() + "." + System.currentTimeMillis();
    }

    /**
     * Save auth data to SharedPreferences
     */
    private void saveAuthData(User user, String token) {
        Log.d(TAG, "💾 Saving mock auth data");

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_TOKEN, token);
        editor.putInt(KEY_USER_ID, user.getId());
        editor.putString(KEY_USERNAME, user.getUsername());
        editor.putString(KEY_EMAIL, user.getEmail());
        editor.putString(KEY_FULL_NAME, user.getFullName());
        editor.putString(KEY_ROLE, user.getRole() != null ? user.getRole() : "USER");
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.apply();

        currentUser = user;
        jwtToken = token;

        Log.d(TAG, "✅ Mock user saved - ID: " + user.getId() + ", Email: " + user.getEmail());
    }

    /**
     * Load current user from SharedPreferences
     */
    private void loadCurrentUser() {
        boolean isLoggedIn = sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);

        if (isLoggedIn) {
            jwtToken = sharedPreferences.getString(KEY_TOKEN, null);

            User user = new User();
            user.setId(sharedPreferences.getInt(KEY_USER_ID, 0));
            user.setUsername(sharedPreferences.getString(KEY_USERNAME, ""));
            user.setEmail(sharedPreferences.getString(KEY_EMAIL, ""));
            user.setFullName(sharedPreferences.getString(KEY_FULL_NAME, ""));
            user.setRole(sharedPreferences.getString(KEY_ROLE, "USER"));

            currentUser = user;

            Log.d(TAG, "📂 Mock user loaded - ID: " + user.getId() + ", Email: " + user.getEmail());
        } else {
            Log.d(TAG, "👤 No mock user logged in");
        }
    }

    /**
     * Mock user data
     */
    private static class MockUserData {
        int id;
        String username;
        String email;
        String password;
        String fullName;

        MockUserData(int id, String username, String email, String password, String fullName) {
            this.id = id;
            this.username = username;
            this.email = email;
            this.password = password;
            this.fullName = fullName;
        }
    }
}
