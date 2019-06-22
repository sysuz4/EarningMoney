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
import com.example.asus.earingmoney.model.Errand;
import com.example.asus.earingmoney.model.Mission;
import com.example.asus.earingmoney.model.Task;
import com.google.gson.JsonArray;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class errand_detail_page extends AppCompatActivity {


    private Handler handler;
    private Mission mission;
    private JSONObject authorization_json;
    private String token;
    private List<Task> task_list;
    private TextView errand_title;
    private TextView errand_description;
    private TextView private_info;
    private TextView deadline;
    private TextView reward;
    private Errand errand;
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

        getComponentById();

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                super.handleMessage(msg);
                if(msg.what == 1){
                    String money_str = String.valueOf(mission.getMoney()) + "元";
                    errand_title.setText(mission.getTitle());
                    reward.setText(money_str);
                    deadline.setText(mission.getDeadLine());
                }
                if(msg.what == 2){
                    errand_description.setText(errand.getDescription());
                    private_info.setText(errand.getPrivateInfo());
                }

            }
        };


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
        token = Util.getToken(getApplicationContext());
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

        final service myService = retrofit.create(service.class);
        Call<Mission> myCall1 = myService.getErrandMission(token,9);
        Call<List<Task>> myCall2 = myService.getTaskByMissionID(token,9);

        myCall1.enqueue(new Callback<Mission>() {
            //请求成功时回调
            @Override
            public void onResponse(Call<Mission> call, final Response<Mission> response) {
                // 步骤7：处理返回的数据结果
                if(response.code() == 200)
                {
                    mission = response.body();
                    Message msg = new Message();
                    msg.what = 1;
                    handler.sendMessage(msg);
//                    Log.d("mission", "onResponse: " + mission.getTitle());
//                    Log.d("mission", "onResponse: " + mission.getDeadLine());
//                    Log.d("mission", "onResponse: " + mission.getMoney());
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

        myCall2.enqueue(new Callback<List<Task>>() {
            //请求成功时回调
            @Override
            public void onResponse(Call<List<Task>> call, final Response<List<Task>> response) {
                // 步骤7：处理返回的数据结果
                if(response.code() == 200)
                {

                    task_list = response.body();
                    if(task_list != null){
                        Call<Errand> myCall3 = myService.getErrandByTaskId(token,task_list.get(0).getTaskId());
                        myCall3.enqueue(new Callback<Errand>() {
                            //请求成功时回调
                            @Override
                            public void onResponse(Call<Errand> call, final Response<Errand> response) {
                                // 步骤7：处理返回的数据结果
                                if(response.code() == 200)
                                {
                                    errand = response.body();
                                    Message msg = new Message();
                                    msg.what = 2;
                                    handler.sendMessage(msg);

                                }
                                else if(response.code() == 401)
                                {
                                    Toast.makeText(getApplicationContext(),
                                            "errand Invalid username/password supplied", Toast.LENGTH_SHORT).show();
                                }
                                else if(response.code() == 404){
                                    Toast.makeText(getApplicationContext(),
                                            "errand not found", Toast.LENGTH_SHORT).show();
                                } else{
                                    Toast.makeText(getApplicationContext(),
                                            "Unknown error", Toast.LENGTH_SHORT).show();
                                }

                            }
                            //请求失败时回调
                            @Override
                            public void onFailure(Call<Errand> call, Throwable throwable) {
                                System.out.println("连接失败");
                            }
                        });
                    }

                    Message msg = new Message();
                    msg.what = 1;
                    handler.sendMessage(msg);
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
            public void onFailure(Call<List<Task>> call, Throwable throwable) {
                System.out.println("连接失败");
            }
        });

    }

}
