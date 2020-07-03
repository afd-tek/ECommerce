package com.example.bisanat.DAL.Network;

import com.google.gson.GsonBuilder;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpClient {
    private static Retrofit retrofit = null;
    private static String BASE_URL = "http://192.168.1.33:1919/api/";
    public static Retrofit getClient(){
        if(retrofit ==null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create()))
                    .build();
        }
        return retrofit;
    }

}