package com.son.e_commerce.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.son.e_commerce.data.api.AuthApiService;
import com.son.e_commerce.data.dto.AuthLoginRequest;
import com.son.e_commerce.data.dto.AuthRegisterRequest;
import com.son.e_commerce.data.dto.AuthResponse;
import com.son.e_commerce.data.dto.RefreshTokenRequest;
import com.son.e_commerce.data.dto.LogoutRequest;
import com.son.e_commerce.data.dto.LogoutResponse;
import com.son.e_commerce.model.entity.User;
import com.son.e_commerce.model.repository.AuthRepository;
import com.son.e_commerce.utils.network.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Auth Repository Implementation - JWT Authentication
 */
public class AuthRepositoryImpl implements AuthRepository {

    private static final String TAG = "AuthRepositoryImpl";
    private static final String PREF_NAME = "ECommerceAuthPrefs";
    private static final String KEY_TOKEN = "jwt_token";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_FULL_NAME = "full_name";
    private static final String KEY_ROLE = "role";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";

    private final SharedPreferences sharedPreferences;
    private final AuthApiService apiService;
    private User currentUser;
    private String jwtToken;

    public AuthRepositoryImpl(Context context) {
        this.sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        this.apiService = ApiClient.getAuthApiService();
        loadCurrentUser();
    }

    @Override
    public void login(String username, String password, OnAuthListener listener) {
        Log.d(TAG, "========== LOGIN START ==========");
        Log.d(TAG, "Username/Email: " + username);
        Log.d(TAG, "Password: " + (password != null ? "[" + password.length() + " chars]" : "[null]"));

        AuthLoginRequest request = new AuthLoginRequest(username, password);
        Log.d(TAG, "Request created");

        apiService.login(request).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(@NonNull Call<AuthResponse> call, @NonNull Response<AuthResponse> response) {
                Log.d(TAG, "========== LOGIN RESPONSE ==========");
                Log.d(TAG, "Response Code: " + response.code());
                Log.d(TAG, "Is Successful: " + response.isSuccessful());
                Log.d(TAG, "Body is null: " + (response.body() == null));

                if (response.isSuccessful() && response.body() != null) {
                    AuthResponse authResponse = response.body();
                    Log.d(TAG, "✅ Login successful!");
                    Log.d(TAG, "Access Token: " + (authResponse.getAccessToken() != null ? "[present]" : "[null]"));
                    Log.d(TAG, "Refresh Token: " + (authResponse.getRefreshToken() != null ? "[present]" : "[null]"));
                    Log.d(TAG, "User ID: " + authResponse.getId());
                    Log.d(TAG, "Username: " + authResponse.getUsername());
                    Log.d(TAG, "Email: " + authResponse.getEmail());
                    Log.d(TAG, "Role: " + authResponse.getRole());

                    // Create User object from response
                    User user = createUserFromAuthResponse(authResponse);
                    Log.d(TAG, "User object created");

                    // Save user, access token, and refresh token
                    String accessToken = authResponse.getAccessToken();
                    String refreshToken = authResponse.getRefreshToken();
                    saveAuthDataWithRefreshToken(user, accessToken, refreshToken);
                    Log.d(TAG, "User, access token, and refresh token saved");

                    Log.d(TAG, "========== LOGIN END (SUCCESS) ==========");
                    listener.onSuccess(user, accessToken);
                } else {
                    String error = parseErrorResponse(response);
                    Log.e(TAG, "❌ Login failed!");
                    Log.e(TAG, "Error: " + error);

                    if (response.errorBody() != null) {
                        try {
                            String errorBody = response.errorBody().string();
                            Log.e(TAG, "Error Body: " + errorBody);
                        } catch (Exception e) {
                            Log.e(TAG, "Could not read error body: " + e.getMessage());
                        }
                    }

                    Log.d(TAG, "========== LOGIN END (ERROR) ==========");
                    listener.onError(error);
                }
            }

            @Override
            public void onFailure(@NonNull Call<AuthResponse> call, @NonNull Throwable t) {
                String error = getNetworkErrorMessage(t);
                Log.e(TAG, "❌ Login API call failed!");
                Log.e(TAG, "Error: " + error);
                Log.e(TAG, "Exception: ", t);
                Log.d(TAG, "========== LOGIN END (NETWORK ERROR) ==========");
                listener.onError(error);
            }
        });
    }

    @Override
    public void register(String username, String email, String password, String fullName, OnAuthListener listener) {
        Log.d(TAG, "========== REGISTER START ==========");
        Log.d(TAG, "Username: " + username);
        Log.d(TAG, "Email: " + email);
        Log.d(TAG, "Full Name: " + fullName);
        Log.d(TAG, "Password: " + (password != null ? "[" + password.length() + " chars]" : "[null]"));

        AuthRegisterRequest request = new AuthRegisterRequest(username, email, password, fullName);
        Log.d(TAG, "Request created: " + request.toString());

        apiService.register(request).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(@NonNull Call<AuthResponse> call, @NonNull Response<AuthResponse> response) {
                Log.d(TAG, "========== REGISTER RESPONSE ==========");
                Log.d(TAG, "Response Code: " + response.code());
                Log.d(TAG, "Is Successful: " + response.isSuccessful());
                Log.d(TAG, "Body is null: " + (response.body() == null));

                if (response.isSuccessful() && response.body() != null) {
                    AuthResponse authResponse = response.body();
                    Log.d(TAG, "✅ Register successful!");
                    Log.d(TAG, "Access Token: " + (authResponse.getAccessToken() != null ? "[present]" : "[null]"));
                    Log.d(TAG, "Refresh Token: " + (authResponse.getRefreshToken() != null ? "[present]" : "[null]"));
                    Log.d(TAG, "User ID: " + authResponse.getId());
                    Log.d(TAG, "Username: " + authResponse.getUsername());
                    Log.d(TAG, "Email: " + authResponse.getEmail());
                    Log.d(TAG, "Full Name: " + authResponse.getFullName());
                    Log.d(TAG, "Role: " + authResponse.getRole());

                    // Create User object from response
                    User user = createUserFromAuthResponse(authResponse);
                    Log.d(TAG, "User object created");

                    // Save user, access token, and refresh token
                    String accessToken = authResponse.getAccessToken();
                    String refreshToken = authResponse.getRefreshToken();
                    saveAuthDataWithRefreshToken(user, accessToken, refreshToken);
                    Log.d(TAG, "User, access token, and refresh token saved");

                    Log.d(TAG, "========== REGISTER END (SUCCESS) ==========");
                    listener.onSuccess(user, accessToken);
                } else {
                    String error = parseErrorResponse(response);
                    Log.e(TAG, "❌ Register failed!");
                    Log.e(TAG, "Error: " + error);

                    if (response.errorBody() != null) {
                        try {
                            String errorBody = response.errorBody().string();
                            Log.e(TAG, "Error Body: " + errorBody);
                        } catch (Exception e) {
                            Log.e(TAG, "Could not read error body: " + e.getMessage());
                        }
                    }

                    Log.d(TAG, "========== REGISTER END (ERROR) ==========");
                    listener.onError(error);
                }
            }

            @Override
            public void onFailure(@NonNull Call<AuthResponse> call, @NonNull Throwable t) {
                String error = getNetworkErrorMessage(t);
                Log.e(TAG, "❌ Register API call failed!");
                Log.e(TAG, "Error: " + error);
                Log.e(TAG, "Exception: ", t);
                Log.d(TAG, "========== REGISTER END (NETWORK ERROR) ==========");
                listener.onError(error);
            }
        });
    }

    @Override
    public void testApi(OnTestApiListener listener) {
        Log.d(TAG, "testApi() called");

        apiService.testApi().enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                Log.d(TAG, "testApi() response code: " + response.code());

                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "testApi() successful: " + response.body());
                    listener.onSuccess(response.body());
                } else {
                    String error = "Test API failed: " + response.code();
                    Log.e(TAG, error);
                    listener.onError(error);
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                String error = "Network error: " + t.getMessage();
                Log.e(TAG, "testApi() failed: " + error, t);
                listener.onError(error);
            }
        });
    }

    @Override
    public void logout() {
        Log.d(TAG, "========== LOGOUT START ==========");

        // Get current refresh token if available
        String refreshToken = sharedPreferences.getString("refresh_token", null);
        Log.d(TAG, "Refresh Token available: " + (refreshToken != null));

        if (refreshToken != null) {
            // Call logout API to revoke refresh token
            LogoutRequest logoutRequest = new LogoutRequest(refreshToken);
            apiService.logout(logoutRequest).enqueue(new Callback<LogoutResponse>() {
                @Override
                public void onResponse(@NonNull Call<LogoutResponse> call, @NonNull Response<LogoutResponse> response) {
                    Log.d(TAG, "Logout API Response Code: " + response.code());
                    if (response.isSuccessful()) {
                        Log.d(TAG, "✅ Server revoked refresh token");
                    } else {
                        Log.w(TAG, "⚠️ Server logout failed, but clearing local data anyway");
                    }
                    clearLocalAuthData();
                }

                @Override
                public void onFailure(@NonNull Call<LogoutResponse> call, @NonNull Throwable t) {
                    Log.w(TAG, "⚠️ Logout API call failed: " + t.getMessage());
                    // Still clear local data even if API fails
                    clearLocalAuthData();
                }
            });
        } else {
            // No refresh token, just clear local data
            Log.d(TAG, "No refresh token found, clearing local data only");
            clearLocalAuthData();
        }
    }

    /**
     * Clear all authentication data locally
     */
    private void clearLocalAuthData() {
        Log.d(TAG, "Clearing local auth data");

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        currentUser = null;
        jwtToken = null;

        Log.d(TAG, "========== LOGOUT END ==========");
    }

    /**
     * Refresh access token using refresh token
     */
    public void refreshAccessToken(String refreshToken, OnAuthListener listener) {
        Log.d(TAG, "========== REFRESH TOKEN START ==========");
        Log.d(TAG, "Refresh Token: " + (refreshToken != null ? "[present]" : "[null]"));

        if (refreshToken == null) {
            Log.e(TAG, "Refresh token is null");
            listener.onError("Refresh token not found");
            return;
        }

        RefreshTokenRequest request = new RefreshTokenRequest(refreshToken);

        apiService.refreshToken(request).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(@NonNull Call<AuthResponse> call, @NonNull Response<AuthResponse> response) {
                Log.d(TAG, "========== REFRESH RESPONSE ==========");
                Log.d(TAG, "Response Code: " + response.code());
                Log.d(TAG, "Is Successful: " + response.isSuccessful());

                if (response.isSuccessful() && response.body() != null) {
                    AuthResponse authResponse = response.body();
                    Log.d(TAG, "✅ Refresh token successful!");
                    Log.d(TAG, "New Access Token received");

                    // Save new tokens
                    String newAccessToken = authResponse.getAccessToken();
                    String newRefreshToken = authResponse.getRefreshToken();

                    if (newAccessToken != null) {
                        saveAccessToken(newAccessToken);
                        if (newRefreshToken != null) {
                            saveRefreshToken(newRefreshToken);
                        }

                        Log.d(TAG, "========== REFRESH TOKEN END (SUCCESS) ==========");
                        listener.onSuccess(currentUser, newAccessToken);
                    } else {
                        Log.e(TAG, "Access token is null in response");
                        listener.onError("Access token not received");
                    }
                } else {
                    String error = parseErrorResponse(response);
                    Log.e(TAG, "❌ Refresh token failed: " + error);
                    Log.d(TAG, "========== REFRESH TOKEN END (ERROR) ==========");
                    listener.onError(error);
                }
            }

            @Override
            public void onFailure(@NonNull Call<AuthResponse> call, @NonNull Throwable t) {
                String error = getNetworkErrorMessage(t);
                Log.e(TAG, "❌ Refresh token API call failed: " + error);
                Log.d(TAG, "========== REFRESH TOKEN END (NETWORK ERROR) ==========");
                listener.onError(error);
            }
        });
    }

    /**
     * Get refresh token from storage
     */
    public String getRefreshToken() {
        return sharedPreferences.getString("refresh_token", null);
    }

    /**
     * Save refresh token
     */
    private void saveRefreshToken(String refreshToken) {
        Log.d(TAG, "Saving refresh token");
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("refresh_token", refreshToken);
        editor.apply();
    }

    /**
     * Save only access token (when refreshing)
     */
    private void saveAccessToken(String accessToken) {
        Log.d(TAG, "Saving new access token");
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_TOKEN, accessToken);
        editor.apply();
        jwtToken = accessToken;
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
     * Create User object from AuthResponse
     */
    private User createUserFromAuthResponse(AuthResponse authResponse) {
        User user = new User();
        user.setId(authResponse.getId());
        user.setUsername(authResponse.getUsername());
        user.setEmail(authResponse.getEmail());
        user.setFullName(authResponse.getFullName());
        user.setRole(authResponse.getRole());
        return user;
    }

    /**
     * Save authentication data with refresh token to SharedPreferences
     */
    private void saveAuthDataWithRefreshToken(User user, String accessToken, String refreshToken) {
        Log.d(TAG, "saveAuthDataWithRefreshToken() - Saving user, access token, and refresh token");

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_TOKEN, accessToken);
        editor.putString("refresh_token", refreshToken);
        editor.putInt(KEY_USER_ID, user.getId());
        editor.putString(KEY_USERNAME, user.getUsername());
        editor.putString(KEY_EMAIL, user.getEmail());
        editor.putString(KEY_FULL_NAME, user.getFullName());
        editor.putString(KEY_ROLE, user.getRole() != null ? user.getRole() : "USER");
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.apply();

        currentUser = user;
        jwtToken = accessToken;

        Log.d(TAG, "User saved with tokens - ID: " + user.getId() + ", Username: " + user.getUsername());
        Log.d(TAG, "Access Token: [saved]");
        Log.d(TAG, "Refresh Token: " + (refreshToken != null ? "[saved]" : "[null]"));
    }

    /**
     * Save authentication data to SharedPreferences (legacy - without refresh token)
     */
    private void saveAuthData(User user, String token) {
        Log.d(TAG, "saveAuthData() - Saving user and token");

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

        Log.d(TAG, "User saved - ID: " + user.getId() + ", Username: " + user.getUsername());
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

            Log.d(TAG, "User loaded from preferences - ID: " + user.getId() + ", Username: " + user.getUsername());
        } else {
            Log.d(TAG, "No user logged in");
        }
    }

    /**
     * Parse error response from server
     */
    private String parseErrorResponse(Response<?> response) {
        String errorMessage = "Lỗi không xác định";

        try {
            if (response.errorBody() != null) {
                String errorBody = response.errorBody().string();
                Log.d(TAG, "Error body: " + errorBody);

                // Check for JWT secret key error
                if (errorBody.contains("signing key's size")) {
                    errorMessage = "Lỗi server: JWT secret key không đúng.\nVui lòng liên hệ quản trị viên để fix server.";
                }
                // Check for Spring Security Forbidden (403)
                else if (errorBody.contains("Forbidden") || response.code() == 403) {
                    errorMessage = "Lỗi server: Spring Security chặn truy cập.\n\nAdmin cần permitAll() cho /api/auth/**";
                }
                // Check for invalid credentials
                else if (errorBody.contains("Invalid") || errorBody.contains("credentials") || response.code() == 401) {
                    errorMessage = "Email hoặc mật khẩu không đúng";
                }
                // Check for user already exists
                else if (errorBody.contains("already exists") || errorBody.contains("duplicate") || response.code() == 409) {
                    errorMessage = "Email đã được đăng ký";
                }
                // Generic error with body
                else if (errorBody.length() > 0) {
                    errorMessage = errorBody;
                }
            } else {
                // HTTP status code based errors
                switch (response.code()) {
                    case 400:
                        errorMessage = "Dữ liệu không hợp lệ";
                        break;
                    case 401:
                        errorMessage = "Email hoặc mật khẩu không đúng";
                        break;
                    case 403:
                        errorMessage = "Truy cập bị từ chối. Server config sai.";
                        break;
                    case 404:
                        errorMessage = "API không tồn tại";
                        break;
                    case 409:
                        errorMessage = "Email đã được đăng ký";
                        break;
                    case 500:
                        errorMessage = "Lỗi server. Vui lòng thử lại sau";
                        break;
                    default:
                        errorMessage = "Lỗi HTTP " + response.code();
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error parsing error response", e);
            errorMessage = "Lỗi HTTP " + response.code();
        }

        return errorMessage;
    }

    /**
     * Get user-friendly network error message
     */
    private String getNetworkErrorMessage(Throwable t) {
        String message = t.getMessage();

        if (message == null) {
            return "Lỗi kết nối không xác định";
        }

        // Connection timeout
        if (message.contains("timeout") || message.contains("timed out")) {
            return "Không thể kết nối đến server.\n\nVui lòng kiểm tra:\n" +
                    "1. Server có đang chạy không?\n" +
                    "2. Địa chỉ IP có đúng không?\n" +
                    "3. Port 8080 có mở không?";
        }

        // Connection refused
        if (message.contains("Connection refused") || message.contains("failed to connect")) {
            return "Server không phản hồi.\n\n" +
                    "Đảm bảo Spring Boot server đang chạy tại:\n" +
                    "http://localhost:8080";
        }

        // Host unreachable
        if (message.contains("unreachable") || message.contains("No route to host")) {
            return "Không thể kết nối đến server.\nKiểm tra địa chỉ IP và firewall.";
        }

        // Generic network error
        return "Lỗi kết nối: " + message;
    }
}
