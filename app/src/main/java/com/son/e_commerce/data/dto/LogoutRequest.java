package com.son.e_commerce.data.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Request DTO for logout endpoint
 */
public class LogoutRequest {
    @SerializedName("refreshToken")
    private String refreshToken;

    public LogoutRequest(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
