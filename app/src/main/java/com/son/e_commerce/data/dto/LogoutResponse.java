package com.son.e_commerce.data.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Response DTO for logout endpoint
 */
public class LogoutResponse {
    @SerializedName("message")
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
