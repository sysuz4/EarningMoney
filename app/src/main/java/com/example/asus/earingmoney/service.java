package com.example.asus.earingmoney;
import com.example.asus.earingmoney.model.GetMissionsObj;
import com.example.asus.earingmoney.model.Errand;
import com.example.asus.earingmoney.model.GetTokenObj;
import com.example.asus.earingmoney.model.Image;
import com.example.asus.earingmoney.model.Msg;
import com.example.asus.earingmoney.model.Questionare;
import org.json.JSONObject;
import com.example.asus.earingmoney.model.Mission;
import com.example.asus.earingmoney.model.User;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import com.example.asus.earingmoney.model.Task;
import com.example.asus.earingmoney.model.User;
import com.google.gson.JsonArray;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.HEAD;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;

public interface service{

    @Headers({"Content-Type:application/json;charset=utf-8", "Accept:application/json;"})
    @POST("/tokens")
    Observable<GetTokenObj> post_to_get_token(@Query("username") String username, @Query("password") String password);

    //@HEAD()
    @GET("/tasks/{taskID}/questionares")
    Call<Questionare> get_questionare(@Path("taskID") int taskID);

    @Headers({"Content-Type: application/json;charset=utf8","Accept: application/json"})
    @POST("/missions/questionares")
    Call<Msg> create_questionare(@Header("authorization") String token, @Body RequestBody questionare);

    @Headers({"Content-Type: application/json;charset=utf8","Accept: application/json"})
    @POST("/missions/errands")
    Call<Msg> create_errand(@Header("authorization") String token, @Body RequestBody errand);


    @GET("/users/{userID}")
    Call<User> get_user(@Header("authorization") String token, @Path("userID") int userID);

    @PUT("/users/{userID}")
    Call<Msg> modify_user(@Header("authorization") String token, @Path("userID") int userID, @Query("oldPassword") String oldPassword, @Body RequestBody requestBody);

    @Multipart
    @POST("/images")
    Call<Image> upload_pic(@Header("authorization") String token, @Part("description") RequestBody description, @Part MultipartBody.Part file);

    @Streaming
    @GET
    Call<ResponseBody> downloadLatestFeature(@Url String fileUrl);
  
    @Headers({"Content-Type:application/json;charset=utf-8", "Accept:application/json;"})
    @GET("/missions/AllMissions")
    Observable<GetMissionsObj> getMissions(@Header("authorization") String token);

    @GET("/missions/{missionID}")
    Observable<Mission> getMissionDetail(@Path("missionID") int missionID);

    @Headers({"Content-Type:application/json;charset=utf-8", "Accept:application/json;"})
    @GET("/users/{userID}")
    Observable<User> getUserDetail(@Header("authorization") String token, @Path("userID") int userID);

    @Headers({"Content-type:application/json; charset=utf8","Accept:application/json"})
    @GET("/missions/{missionID}")
    Call<Mission>getErrandMission(@Header("authorization") String token, @Path("missionID") int missionID);

    @Headers({"Content-type:application/json; charset=utf8","Accept:application/json"})
    @GET("/missions/{missionID}/tasks")
    Call<List<Task>>getTaskByMissionID(@Header("authorization") String token, @Path("missionID") int missionID);

    @Headers({"Content-type:application/json; charset=utf8","Accept:application/json"})
    @GET("/tasks/{taskID}/errands")
    Call<Errand>getErrandByTaskId(@Header("authorization") String token, @Path("taskID") int taskID);

    @Headers({"Content-type:application/json; charset=utf8","Accept:application/json"})
    @GET("/users/{userID}")
    Call<User>getUserById(@Header("authorization") String token, @Path("userID") int userID);

}
