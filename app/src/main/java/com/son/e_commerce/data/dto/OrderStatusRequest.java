package com.son.e_commerce.data.dto;

import com.google.gson.annotations.SerializedName;

public class OrderStatusRequest {
    @SerializedName("status")
    private String status;

    public OrderStatusRequest(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
