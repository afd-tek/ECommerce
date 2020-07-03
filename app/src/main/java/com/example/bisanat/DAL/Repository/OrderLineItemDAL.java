package com.example.bisanat.DAL.Repository;

import com.example.bisanat.DAL.Entites.CategoriesProduct;
import com.example.bisanat.DAL.Entites.OrderLineItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;


public interface OrderLineItemDAL {

    @GET("OrderLineItems/{id}")
    Call<OrderLineItem> Get(int id);

    @GET("OrderLineItems")
    Call<List<OrderLineItem>> GetAll();

    @POST("OrderLineItems/filter")
    Call<List<OrderLineItem>> GetFilter(@Body OrderLineItem entity);

    @POST("OrderLineItems")
    Call<OrderLineItem> Add(@Body OrderLineItem entity);

    @POST("OrderLineItems/bulk")
    Call<List<OrderLineItem>> BulkInsert(@Body List<OrderLineItem> entity);

    @PUT("OrderLineItems")
    Call<OrderLineItem> Update(@Body OrderLineItem entity);

    @DELETE("OrderLineItems/{id}")
    Call<String> Delete(int id);
}
