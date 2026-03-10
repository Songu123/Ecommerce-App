package com.son.e_commerce.model.entity;

import java.io.Serializable;

public class Category implements Serializable {
    private int id;
    private String name;
    private String slug;
    private int iconResId; // Resource ID for the category icon

    public Category() {
    }

    public Category(int id, String name, String slug, int iconResId) {
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.iconResId = iconResId;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSlug() {
        return slug;
    }

    public int getIconResId() {
        return iconResId;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public void setIconResId(int iconResId) {
        this.iconResId = iconResId;
    }
}
