package com.son.e_commerce.data;

import com.son.e_commerce.data.api.CategoryApiService;
import com.son.e_commerce.model.entity.Category;
import com.son.e_commerce.model.repository.CategoryRepository;
import com.son.e_commerce.utils.network.ApiClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryRepositoryImpl implements CategoryRepository {

    private CategoryApiService apiService;

    public CategoryRepositoryImpl() {
        this.apiService = ApiClient.getCategoryApiService();
    }

    @Override
    public void getAllCategories(OnCategoriesLoadedListener listener) {
        apiService.getAllCategories().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listener.onSuccess(response.body());
                } else {
                    listener.onError("Failed to load categories: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                listener.onError("Network error: " + t.getMessage());
            }
        });
    }

    @Override
    public void getCategoryById(int id, OnCategoryLoadedListener listener) {
        apiService.getCategoryById(id).enqueue(new Callback<Category>() {
            @Override
            public void onResponse(Call<Category> call, Response<Category> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listener.onSuccess(response.body());
                } else {
                    listener.onError("Category not found");
                }
            }

            @Override
            public void onFailure(Call<Category> call, Throwable t) {
                listener.onError("Network error: " + t.getMessage());
            }
        });
    }
}
