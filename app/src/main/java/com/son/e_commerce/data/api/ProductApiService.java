package com.son.e_commerce.data.api;

import com.son.e_commerce.model.entity.Product;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.*;

public interface ProductApiService {

    /**
     * GET /api/products - Lấy tất cả sản phẩm
     */
    @GET("api/products")
    Call<List<Product>> getAllProducts();

    /**
     * GET /api/products/{id} - Lấy sản phẩm theo ID
     */
    @GET("api/products/{id}")
    Call<Product> getProductById(@Path("id") int id);

    /**
     * GET /api/products/category/{categoryId} - Lấy sản phẩm theo danh mục
     */
    @GET("api/products/category/{categoryId}")
    Call<List<Product>> getProductsByCategory(@Path("categoryId") int categoryId);

    /**
     * POST /api/products - Tạo sản phẩm mới (Admin)
     */
    @POST("api/products")
    Call<Product> createProduct(@Body Product product);

    /**
     * PUT /api/products/{id} - Cập nhật sản phẩm (Admin)
     */
    @PUT("api/products/{id}")
    Call<Product> updateProduct(@Path("id") int id, @Body Product product);

    /**
     * DELETE /api/products/{id} - Xóa sản phẩm (Admin)
     */
    @DELETE("api/products/{id}")
    Call<Void> deleteProduct(@Path("id") int id);
}
