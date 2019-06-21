package com.example.asus.earingmoney;

import com.example.asus.earingmoney.model.MissionModel;
import com.example.asus.earingmoney.model.Questionare;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import rx.Observable;

public interface UserService {
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @GET("/users/{userId}/missions")
    Observable<ArrayList<MissionModel>> getMissionsByUserId(@Header("authorization") String token, @Path("userId") int userId);
}
