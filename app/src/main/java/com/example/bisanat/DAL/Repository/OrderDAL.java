package com.example.bisanat.DAL.Repository;

import com.example.bisanat.DAL.Entites.CategoriesProduct;
import com.example.bisanat.DAL.Entites.Order;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;


public interface OrderDAL {

    @GET("Orders/{id}")
    Call<Order> Get(@Path("id") int id);

    @GET("Orders")
    Call<List<Order>> GetAll();

    @POST("Orders/filter")
    Call<List<Order>> GetFilter(@Body Order entity);

    @POST("Orders")
    Call<Order> Add(@Body Order entity);

    @PUT("Orders")
    Call<Order> Update(@Body Order entity);

    @DELETE("Orders/{id}")
    Call<String> Delete(int id);
}
