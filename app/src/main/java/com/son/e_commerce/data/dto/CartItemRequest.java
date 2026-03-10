package com.son.e_commerce.data.dto;

import com.google.gson.annotations.SerializedName;

public class CartItemRequest {
    @SerializedName("userId")
    private int userId;

    @SerializedName("productId")
    private int productId;

    @SerializedName("quantity")
    private int quantity;

    public CartItemRequest(int userId, int productId, int quantity) {
        this.userId = userId;
        this.productId = productId;
        this.quantity = quantity;
    }

    // Getters and Setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
