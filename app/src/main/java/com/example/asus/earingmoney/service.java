package com.example.asus.earingmoney;
import com.example.asus.earingmoney.model.GetTokenObj;
import com.example.asus.earingmoney.model.Questionare;
import org.json.JSONObject;
import com.example.asus.earingmoney.model.Mission;

<<<<<<< HEAD
import java.time.Instant;
=======
>>>>>>> f388a6032de39c9181a77d42faa07360cab8c2c4
import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
<<<<<<< HEAD
import retrofit2.http.HEAD;
import retrofit2.http.Header;
=======
>>>>>>> f388a6032de39c9181a77d42faa07360cab8c2c4
import retrofit2.http.Headers;
import retrofit2.http.POST;
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

    @Headers({"Content-type:application/json; charset=utf8","Accept:application/json"})
    @GET("/missions/{missionID}")
    Call<Mission>getErrandMission(@Header("authorization") String token, @Path("missionID") int missionID);

    @Headers({"Content-type:application/json; charset=utf8","Accept:application/json"})
    @GET("/missions/{missionID}/tasks")
    Call<List<Integer>>getTaskByMissionID(@Header("authorization") String token, @Path("missionID") int missionID);
}
