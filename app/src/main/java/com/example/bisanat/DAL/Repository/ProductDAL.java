package com.example.bisanat.DAL.Repository;

import com.example.bisanat.DAL.Entites.CategoriesProduct;
import com.example.bisanat.DAL.Entites.Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;


public interface ProductDAL {

    @GET("Products/{id}")
    Call<Product> Get(int id);

    @GET("Products")
    Call<List<Product>> GetAll();

    @POST("Products/filter")
    Call<List<Product>> GetFilter(@Body Product entity);

    @POST("Products")
    Call<Product> Add(@Body Product entity);

    @PUT("Products")
    Call<Product> Update(@Body Product entity);

    @DELETE("Products/{id}")
    Call<String> Delete(int id);

}
