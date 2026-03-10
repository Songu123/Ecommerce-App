package com.son.e_commerce.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.son.e_commerce.model.entity.User;
import com.son.e_commerce.model.repository.AuthRepository;

/**
 * Mock Auth Repository - For testing UI without server
 *
 * ĐÂY LÀ MOCK VERSION CHỈ DÙNG ĐỂ TEST UI!
 * Không gọi API thật, chỉ giả lập response.
 *
 * Cách sử dụng:
 * - Thay AuthRepositoryImpl bằng MockAuthRepositoryImpl trong LoginFragment/RegisterFragment
 * - Login với bất kỳ email/password nào → Luôn thành công
 * - Lưu vào SharedPreferences như bình thường
 */
public class MockAuthRepositoryImpl implements AuthRepository {

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
    private User currentUser;
    private String jwtToken;

    public MockAuthRepositoryImpl(Context context) {
        this.sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        loadCurrentUser();
    }

    @Override
    public void login(String username, String password, OnAuthListener listener) {
        Log.d(TAG, "🎭 MOCK login() called with username: " + username);

        // Giả lập network delay (500ms)
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            // Tạo mock user
            User user = new User();
            user.setId(1);
            user.setUsername(username);
            user.setEmail(username);
            user.setFullName("Mock User");
            user.setRole("USER");

            // Tạo mock JWT token
            String mockToken = "mock.jwt.token." + System.currentTimeMillis();

            Log.d(TAG, "✅ MOCK login successful - Token: " + mockToken);

            // Lưu vào SharedPreferences
            saveAuthData(user, mockToken);

            // Callback success
            listener.onSuccess(user, mockToken);
        }, 500);
    }

    @Override
    public void register(String username, String email, String password, String fullName, OnAuthListener listener) {
        Log.d(TAG, "🎭 MOCK register() called - username: " + username + ", email: " + email);

        // Giả lập network delay (800ms)
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            // Tạo mock user
            User user = new User();
            user.setId(2);
            user.setUsername(username);
            user.setEmail(email);
            user.setFullName(fullName != null ? fullName : "Mock User");
            user.setRole("USER");

            // Tạo mock JWT token
            String mockToken = "mock.jwt.token." + System.currentTimeMillis();

            Log.d(TAG, "✅ MOCK register successful - Token: " + mockToken);

            // Lưu vào SharedPreferences
            saveAuthData(user, mockToken);

            // Callback success
            listener.onSuccess(user, mockToken);
        }, 800);
    }

    @Override
    public void testApi(OnTestApiListener listener) {
        Log.d(TAG, "🎭 MOCK testApi() called");

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Log.d(TAG, "✅ MOCK testApi successful");
            listener.onSuccess("Mock API is working!");
        }, 300);
    }

    @Override
    public void logout() {
        Log.d(TAG, "🎭 MOCK logout() - Clearing session");

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
     * Save authentication data to SharedPreferences
     */
    private void saveAuthData(User user, String token) {
        Log.d(TAG, "💾 Saving user and token to SharedPreferences");

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

        Log.d(TAG, "✅ User saved - ID: " + user.getId() + ", Username: " + user.getUsername());
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

            Log.d(TAG, "📂 User loaded from preferences - ID: " + user.getId());
        } else {
            Log.d(TAG, "📂 No user logged in");
        }
    }
}
