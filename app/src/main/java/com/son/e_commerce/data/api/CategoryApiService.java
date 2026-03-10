package com.son.e_commerce.data.api;

import com.son.e_commerce.model.entity.Category;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.*;

public interface CategoryApiService {

    /**
     * GET /api/categories - Lấy tất cả danh mục
     */
    @GET("api/categories")
    Call<List<Category>> getAllCategories();

    /**
     * GET /api/categories/{id} - Lấy danh mục theo ID
     */
    @GET("api/categories/{id}")
    Call<Category> getCategoryById(@Path("id") int id);

    /**
     * POST /api/categories - Tạo danh mục mới (Admin)
     */
    @POST("api/categories")
    Call<Category> createCategory(@Body Category category);

    /**
     * PUT /api/categories/{id} - Cập nhật danh mục (Admin)
     */
    @PUT("api/categories/{id}")
    Call<Category> updateCategory(@Path("id") int id, @Body Category category);

    /**
     * DELETE /api/categories/{id} - Xóa danh mục (Admin)
     */
    @DELETE("api/categories/{id}")
    Call<Void> deleteCategory(@Path("id") int id);
}
