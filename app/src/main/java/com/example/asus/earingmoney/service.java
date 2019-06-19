package com.example.asus.earingmoney;

<<<<<<< HEAD
import com.example.asus.earingmoney.model.Questionare;

import org.json.JSONObject;
=======
import com.example.asus.earingmoney.model.Mission;
>>>>>>> 8268351cf1ea6b8433e186310c67e9d573c7d3bf

import java.util.List;

import okhttp3.ResponseBody;
<<<<<<< HEAD
import retrofit2.Call;
import retrofit2.Response;
=======
>>>>>>> 8268351cf1ea6b8433e186310c67e9d573c7d3bf
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HEAD;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public interface service{

    @POST("/tokens")
    Observable<ResponseBody> post_to_get_token(@Query("username") String username, @Query("password") String password, @Body ResponseBody response);

<<<<<<< HEAD
    //@HEAD()
    @GET("/tasks/{taskID}/questionares")
    Call<Questionare> get_questionare(@Path("taskID") int taskID);
=======
    @Headers("{token}")
    @GET("/missions")
    Observable<List<Mission>> getMissions(@Path("token") String token);
>>>>>>> 8268351cf1ea6b8433e186310c67e9d573c7d3bf
}
