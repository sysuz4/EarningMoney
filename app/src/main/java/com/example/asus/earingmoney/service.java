package com.example.asus.earingmoney;
import com.example.asus.earingmoney.model.GetTokenObj;
import com.example.asus.earingmoney.model.Questionare;
import org.json.JSONObject;
import com.example.asus.earingmoney.model.Mission;
import com.example.asus.earingmoney.model.User;

import java.util.List;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HEAD;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public interface service{

    @Headers({"Content-Type:application/json;charset=utf-8", "Accept:application/json;"})
    @POST("/tokens")
    Observable<GetTokenObj> post_to_get_token(@Query("username") String username, @Query("password") String password);

    //@HEAD()
    @GET("/tasks/{taskID}/questionares")
    Call<Questionare> get_questionare(@Path("taskID") int taskID);

    @Headers("{token}")
    @GET("/missions")
    Observable<List<Mission>> getMissions(@Path("token") String token);


    @Headers({"Content-Type: application/json;charset=utf8","Accept: application/json"})
    @POST("/missions/questionares")
    Call<String> create_questionare(@Header("authorization") String token, @Body RequestBody questionare);

    @Headers({"Content-Type: application/json;charset=utf8","Accept: application/json"})
    @POST("/missions/errands")
    Call<String> create_errand(@Header("authorization") String token, @Body RequestBody errand);


    @GET("/users/{userID}")
    Call<User> get_user(@Header("authorization") String token, @Path("userID") int userID);

    @PUT("/users/{userID}")
    Call<String> modify_user(@Header("authorization") String token, @Path("userID") int userID, @Body RequestBody requestBody);
}
