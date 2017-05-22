package com.example.a009.countryselector.Interface;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


/**
 * Created by 009 on 22.05.2017.
 */

public interface API {

    @GET("/David-Haim/CountriesToCitiesJSON/master/countriesToCities.json")
    Call<ResponseBody> getCountry();

    @GET("/wikipediaSearchJSON")
    Call<ResponseBody> getCityInfo(@Query("q") String query, @Query("maxRows") String maxRows, @Query("username") String username);
}
