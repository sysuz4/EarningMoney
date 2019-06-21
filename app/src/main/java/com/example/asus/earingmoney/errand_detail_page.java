package com.example.asus.earingmoney;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.earingmoney.Util.Util;
import com.example.asus.earingmoney.model.Mission;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class errand_detail_page extends AppCompatActivity {


    private Handler handler;
    private Mission data;
    private JSONObject authorization_json;
    private String token;
    private TextView errand_title;
    private TextView errand_description;
    private TextView private_info;
    private TextView deadline;
    private TextView reward;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_errand_detail_page);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("跑腿任务");
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.mipmap.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    public void getComponentById(){
        errand_title = findViewById(R.id.errand_title);
        errand_description = findViewById(R.id.errand_description);
        private_info = findViewById(R.id.private_info);
        deadline = findViewById(R.id.deadline);
        reward = findViewById(R.id.reward);
    }


    public void loadData(View view)
    {
        String authorization_str = Util.getToken(getApplicationContext());
        try {
            authorization_json = new JSONObject(authorization_str);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            token = authorization_json.getString("token");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        OkHttpClient build = new OkHttpClient.Builder()
                .connectTimeout(2, TimeUnit.SECONDS)
                .readTimeout(2, TimeUnit.SECONDS)
                .writeTimeout(2, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://106.14.225.59/")
                // 本次实验不需要自定义Gson
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                // build 即为okhttp声明的变量，下文会讲
                .client(build)
                .build();

        service myService = retrofit.create(service.class);
        Call<Mission> myCall1 = myService.getErrandMission(token,2);
        Call<List<Integer>> myCall2 = myService.getTaskByMissionID(token,2);

        myCall1.enqueue(new Callback<Mission>() {
            //请求成功时回调
            @Override
            public void onResponse(Call<Mission> call, final Response<Mission> response) {
                // 步骤7：处理返回的数据结果
                if(response.code() == 200)
                {
                    Toast.makeText(getApplicationContext(),
                            "get mission success", Toast.LENGTH_SHORT).show();
                    data = response.body();
                    setComponentValue(data);
                }
                else if(response.code() == 401)
                {
                    Toast.makeText(getApplicationContext(),
                            "Invalid username/password supplied", Toast.LENGTH_SHORT).show();
                }
                else if(response.code() == 404){
                    Toast.makeText(getApplicationContext(),
                            "mission not found", Toast.LENGTH_SHORT).show();
                } else{
                    Toast.makeText(getApplicationContext(),
                            "Unknown error", Toast.LENGTH_SHORT).show();
                }

            }
            //请求失败时回调
            @Override
            public void onFailure(Call<Mission> call, Throwable throwable) {
                System.out.println("连接失败");
            }
        });

        myCall2.enqueue(new Callback<List<Integer>>() {
            //请求成功时回调
            @Override
            public void onResponse(Call<List<Integer>> call, final Response<List<Integer>> response) {
                // 步骤7：处理返回的数据结果
                if(response.code() == 200)
                {
                    Toast.makeText(getApplicationContext(),
                            "get errand success", Toast.LENGTH_SHORT).show();
                    //setComponentValue(data);
                }
                else if(response.code() == 401)
                {
                    Toast.makeText(getApplicationContext(),
                            "Invalid username/password supplied", Toast.LENGTH_SHORT).show();
                }
                else if(response.code() == 404){
                    Toast.makeText(getApplicationContext(),
                            "mission not found", Toast.LENGTH_SHORT).show();
                } else{
                    Toast.makeText(getApplicationContext(),
                            "Unknown error", Toast.LENGTH_SHORT).show();
                }

            }
            //请求失败时回调
            @Override
            public void onFailure(Call<List<Integer>> call, Throwable throwable) {
                System.out.println("连接失败");
            }
        });

    }

    public void setComponentValue(Mission mission){
        errand_title.setText(mission.getTitle());

    }


}
