package com.example.asus.earingmoney;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public interface service{

    @POST("/tokens")
    Observable<ResponseBody> post_to_get_token(@Query("username") String username, @Query("password") String password, @Body ResponseBody response);
}
