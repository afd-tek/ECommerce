package com.example.bisanat.DAL.Repository;

import com.example.bisanat.DAL.Entites.CategoriesProduct;
import com.example.bisanat.DAL.Entites.Category;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;


public interface CategoryDAL {

    @GET("Categories/{id}")
    Call<Category> Get(int id);

    @GET("Categories")
    Call<List<Category>> GetAll();

    @POST("Categories/filter")
    Call<List<Category>> GetFilter(@Body Category entity);

    @POST("Categories")
    Call<Category> Add(@Body Category entity);

    @PUT("Categories")
    Call<Category> Update(@Body Category entity);

    @DELETE("Categories/{id}")
    Call<String> Delete(int id);
}
