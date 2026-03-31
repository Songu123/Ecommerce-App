package com.son.e_commerce.data.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Request DTO for refreshing access token
 */
public class RefreshTokenRequest {
    @SerializedName("refreshToken")
    private String refreshToken;

    public RefreshTokenRequest(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
