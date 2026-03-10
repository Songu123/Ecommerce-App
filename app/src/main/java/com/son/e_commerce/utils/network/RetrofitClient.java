package com.son.e_commerce.utils.network;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

public class RetrofitClient {

    // NOTE: BASE_URL được quản lý bởi ApiConfig
    // Để thay đổi IP, mở file ApiConfig.java và thay đổi:
    // 1. CURRENT_MODE (EMULATOR hoặc REAL_DEVICE)
    // 2. YOUR_PC_IP (IP máy tính chạy Spring Boot)

    private static Retrofit retrofit;

    /**
     * Get Retrofit instance
     */
    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            // Logging interceptor for debugging
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            // OkHttp client with timeout and logging
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .addInterceptor(loggingInterceptor)
                    .build();

            // Gson with date format
            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                    .setLenient()
                    .create();

            // Get base URL from ApiConfig
            String baseUrl = ApiConfig.getBaseUrl();
            android.util.Log.d("RetrofitClient", "Using base URL: " + baseUrl + " (Mode: " + ApiConfig.getModeName() + ")");

            // Build Retrofit
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }

    /**
     * Get base URL
     */
    public static String getBaseUrl() {
        return ApiConfig.getBaseUrl();
    }

    /**
     * Reset Retrofit instance (use when changing base URL)
     */
    public static void reset() {
        retrofit = null;
        android.util.Log.d("RetrofitClient", "Retrofit instance reset");
    }
}
