package com.son.e_commerce.model.repository;

import com.son.e_commerce.model.entity.Category;
import java.util.List;

public interface CategoryRepository {
    /**
     * Get all categories
     */
    void getAllCategories(OnCategoriesLoadedListener listener);

    /**
     * Get category by ID
     */
    void getCategoryById(int id, OnCategoryLoadedListener listener);

    /**
     * Callback for loading multiple categories
     */
    interface OnCategoriesLoadedListener {
        void onSuccess(List<Category> categories);
        void onError(String error);
    }

    /**
     * Callback for loading single category
     */
    interface OnCategoryLoadedListener {
        void onSuccess(Category category);
        void onError(String error);
    }
}
