package com.example.bisanat.DAL.Repository;

import com.example.bisanat.DAL.Entites.CategoriesProduct;
import com.example.bisanat.DAL.Entites.Person;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;


public interface PersonDAL {

    @GET("People/{id}")
    Call<Person> Get(int id);

    @GET("People")
    Call<List<Person>> GetAll();

    @POST("People/filter")
    Call<List<Person>> GetFilter(@Body Person entity);

    @POST("People")
    Call<Person> Add(@Body Person entity);

    @PUT("People")
    Call<Person> Update(@Body Person entity);

    @DELETE("People/{id}")
    Call<String> Delete(int id);
}
