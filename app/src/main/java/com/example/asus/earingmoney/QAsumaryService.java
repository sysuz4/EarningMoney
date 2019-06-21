package com.example.asus.earingmoney;

import com.example.asus.earingmoney.model.Mission;
import com.example.asus.earingmoney.model.QAsummary;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import rx.Observable;

public interface QAsumaryService {
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @GET("/missions/{missionId}/QAsummary")
    Observable<QAsummary> getQAsummary( @Header("authorization") String token, @Path("missionId") int missionId);
}
