package com.example.bisanat.DAL.Repository;

import com.example.bisanat.DAL.Entites.CategoriesProduct;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;


public interface CategoriesProductDAL {

    @GET("CategoriesProducts/{id}")
    Call<CategoriesProduct> Get(int id);

    @GET("CategoriesProducts")
    Call<List<CategoriesProduct>> GetAll();

    @POST("CategoriesProducts/filter")
    Call<List<CategoriesProduct>> GetFilter(@Body CategoriesProduct entity);

    @POST("CategoriesProducts")
    Call<CategoriesProduct> Add(@Body CategoriesProduct entity);

    @PUT("CategoriesProducts")
    Call<CategoriesProduct> Update(@Body CategoriesProduct entity);

    @DELETE("CategoriesProducts/{id}")
    Call<String> Delete(int id);
}
