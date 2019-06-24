package com.example.asus.earingmoney;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import okhttp3.ResponseBody;
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
    private Button button;
    private Errand errand;
    private int missionId;
    private int taskId;
    private Task task;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_errand_detail_page);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("跑腿任务");
        toolbar.setBackgroundColor(getResources().getColor(R.color.Blue));
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
                if(msg.what == 3)
                {
                    button.setText("已提交 待审核");
                    button.setEnabled(false);
                    button.setBackgroundColor(getResources().getColor(R.color.buttom_tv));
                }
                if(msg.what == 4){
                    button.setText("任务已完成");
                    button.setEnabled(false);
                    button.setBackgroundColor(getResources().getColor(R.color.buttom_tv));
                }

            }
        };

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        taskId = bundle.getInt("taskId");
        Log.d("taskId", "onCreate: " + taskId);

        token = Util.getToken(getApplicationContext());
        //Log.d("token", token);
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

        service myService1 = retrofit.create(service.class);
        Call<Task> myCall5 = myService1.getTaskByTaskId(token,taskId);
        myCall5.enqueue(new Callback<Task>() {
            //请求成功时回调
            @Override
            public void onResponse(Call<Task> call, final Response<Task> response) {
                // 步骤7：处理返回的数据结果
                if(response.code() == 200)
                {
                    task = response.body();
                    initButtonUI(task);
                    missionId = task.getMissionId();
                    loadData(missionId);
                }
                else if(response.code() == 401)
                {
                    Toast.makeText(getApplicationContext(),
                            "Invalid username/password supplied", Toast.LENGTH_SHORT).show();
                }
                else if(response.code() == 404){
                    Toast.makeText(getApplicationContext(),
                            "task not found", Toast.LENGTH_SHORT).show();
                } else{
                    Toast.makeText(getApplicationContext(),
                            "Unknown error", Toast.LENGTH_SHORT).show();
                }

            }
            //请求失败时回调
            @Override
            public void onFailure(Call<Task> call, Throwable throwable) {
                System.out.println("连接失败");
            }
        });
    }

    public void getComponentById(){
        errand_title = findViewById(R.id.errand_title);
        errand_description = findViewById(R.id.errand_description);
        private_info = findViewById(R.id.private_info);
        deadline = findViewById(R.id.deadline);
        reward = findViewById(R.id.reward);
        button = findViewById(R.id.button);
    }


    public void loadData(int missionId)
    {
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
        Call<Mission> myCall1 = myService.getErrandMission(token,missionId);
        Call<List<Task>> myCall2 = myService.getTaskByMissionID(token,missionId);

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

    public void finishErrandTask(View view){
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
        service myService2 = retrofit.create(service.class);
        Call<ResponseBody> myCall6 = myService2.finishErrandTask(token,taskId);
        myCall6.enqueue(new Callback<ResponseBody>() {
            //请求成功时回调
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                // 步骤7：处理返回的数据结果
                if(response.code() == 200)
                {
                    Toast.makeText(getApplicationContext(),
                            "提交成功，等待审核", Toast.LENGTH_SHORT).show();
                    Message msg = new Message();
                    msg.what = 3;
                    handler.sendMessage(msg);

                }
                else if(response.code() == 401)
                {
                    Toast.makeText(getApplicationContext(),
                            "Invalid username/password supplied", Toast.LENGTH_SHORT).show();
                }
                else if(response.code() == 404){
                    Toast.makeText(getApplicationContext(),
                            "failure", Toast.LENGTH_SHORT).show();
                } else{
                    Toast.makeText(getApplicationContext(),
                            "Unknown error", Toast.LENGTH_SHORT).show();
                }

            }
            //请求失败时回调
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                System.out.println("连接失败");
            }
        });
    }

    public void initButtonUI(Task task){
        int status = task.getTaskStatus();
        Log.d("status", "initButtonUI: " + status);
        if( status == 2){
            Message msg = new Message();
            msg.what = 3;
            handler.sendMessage(msg);
        } else if( status == 3){
            Message msg = new Message();
            msg.what = 4;
            handler.sendMessage(msg);
        }
    }

}
