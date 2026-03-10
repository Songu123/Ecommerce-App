package com.son.e_commerce.data;

import android.content.Context;
import android.content.SharedPreferences;
import com.son.e_commerce.data.api.UserApiService;
import com.son.e_commerce.data.dto.LoginRequest;
import com.son.e_commerce.data.dto.RegisterRequest;
import com.son.e_commerce.model.entity.User;
import com.son.e_commerce.model.repository.UserRepository;
import com.son.e_commerce.utils.network.ApiClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepositoryImpl implements UserRepository {

    private static final String PREF_NAME = "ECommercePrefs";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_FULL_NAME = "full_name";
    private static final String KEY_ROLE = "role";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";

    private SharedPreferences sharedPreferences;
    private User currentUser;
    private UserApiService apiService;

    public UserRepositoryImpl(Context context) {
        this.sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        this.apiService = ApiClient.getUserApiService();
        loadCurrentUser();
    }

    @Override
    public void login(String username, String password, OnLoginListener listener) {
        LoginRequest request = new LoginRequest(username, password);

        apiService.login(request).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User user = response.body();
                    saveCurrentUser(user);
                    listener.onSuccess(user);
                } else {
                    listener.onError("Invalid username or password");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                listener.onError("Network error: " + t.getMessage());
            }
        });
    }

    @Override
    public void register(User user, OnRegisterListener listener) {
        RegisterRequest request = new RegisterRequest(
            user.getUsername(),
            user.getEmail(),
            user.getPassword(),
            user.getFullName()
        );

        apiService.register(request).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User registeredUser = response.body();
                    saveCurrentUser(registeredUser);
                    listener.onSuccess(registeredUser);
                } else {
                    listener.onError("Registration failed: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                listener.onError("Network error: " + t.getMessage());
            }
        });
    }

    @Override
    public void getUserById(int id, OnUserLoadedListener listener) {
        apiService.getUserById(id).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listener.onSuccess(response.body());
                } else {
                    listener.onError("User not found");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                listener.onError("Network error: " + t.getMessage());
            }
        });
    }

    @Override
    public void updateUser(User user, OnUserUpdatedListener listener) {
        apiService.updateUser(user.getId(), user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    saveCurrentUser(response.body());
                    listener.onSuccess(true);
                } else {
                    listener.onError("Failed to update user: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                listener.onError("Network error: " + t.getMessage());
            }
        });
    }

    @Override
    public void logout() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        currentUser = null;
    }

    @Override
    public User getCurrentUser() {
        // TODO: Remove this mock user after implementing login
        // For testing purposes, return a mock user if no user is logged in
        if (currentUser == null) {
            // Create a temporary mock user for testing
            currentUser = new User(1, "testuser", "test@example.com",
                "Test User", "user", true);
        }
        return currentUser;
    }

    private void saveCurrentUser(User user) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_USER_ID, user.getId());
        editor.putString(KEY_USERNAME, user.getUsername());
        editor.putString(KEY_EMAIL, user.getEmail());
        editor.putString(KEY_FULL_NAME, user.getFullName());
        editor.putString(KEY_ROLE, user.getRole());
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.apply();

        this.currentUser = user;
    }

    private void loadCurrentUser() {
        boolean isLoggedIn = sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
        if (isLoggedIn) {
            int id = sharedPreferences.getInt(KEY_USER_ID, -1);
            String username = sharedPreferences.getString(KEY_USERNAME, "");
            String email = sharedPreferences.getString(KEY_EMAIL, "");
            String fullName = sharedPreferences.getString(KEY_FULL_NAME, "");
            String role = sharedPreferences.getString(KEY_ROLE, "user");

            this.currentUser = new User(id, username, email, fullName, role, true);
        }
    }
}
