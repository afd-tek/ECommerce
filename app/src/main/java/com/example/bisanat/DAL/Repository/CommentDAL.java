package com.example.bisanat.DAL.Repository;

import com.example.bisanat.DAL.Entites.CategoriesProduct;
import com.example.bisanat.DAL.Entites.Comment;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;


public interface CommentDAL {

    @GET("Comments/{id}")
    Call<Comment> Get(int id);

    @GET("Comments")
    Call<List<Comment>> GetAll();

    @POST("Comments/filter")
    Call<List<Comment>> GetFilter(@Body Comment entity);

    @POST("Comments")
    Call<Comment> Add(@Body Comment entity);

    @PUT("Comments")
    Call<Comment> Update(@Body Comment entity);

    @DELETE("Comments/{id}")
    Call<String> Delete(int id);
}
