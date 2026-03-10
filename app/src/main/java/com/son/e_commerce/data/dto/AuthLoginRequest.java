package com.son.e_commerce.data.dto;

import com.google.gson.annotations.SerializedName;

public class AuthLoginRequest {
    @SerializedName("username")
    private String username;

    @SerializedName("password")
    private String password;

    public AuthLoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
