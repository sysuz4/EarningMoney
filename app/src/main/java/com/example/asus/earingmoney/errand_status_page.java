package com.example.asus.earingmoney;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.earingmoney.Util.Util;
import com.example.asus.earingmoney.model.Errand;
import com.example.asus.earingmoney.model.Mission;
import com.example.asus.earingmoney.model.Task;
import com.example.asus.earingmoney.model.User;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class errand_status_page extends AppCompatActivity {

    private Toolbar.OnMenuItemClickListener onMenuItemClick;
    private TextView errand_title1;
    private TextView errand_status1;
    private TextView info1;
    private TextView errand_discription1;
    private TextView errand_private_info1;
    private TextView errand_deadline1;
    private TextView payment1;
    private Handler handler;
    private String token;
    private Mission mission;
    private Errand errand;
    private List<Task> task_list;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_errand_status_page);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("任务详情");
        setSupportActionBar(toolbar);

        onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                String msg = "";
                switch (menuItem.getItemId()) {
                    case R.id.shensu:
                        msg += "您的申诉已受理";
                        break;
                }
                if(!msg.equals("")) {
                    Toast.makeText(errand_status_page.this, msg, Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        };

        toolbar.setNavigationIcon(R.mipmap.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.setOnMenuItemClickListener(onMenuItemClick);

        getComponentById();

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                super.handleMessage(msg);
                if(msg.what == 1){
                    String money_str = String.valueOf(mission.getMoney()) + "元";
                    String status_str = "";
                    errand_title1.setText(mission.getTitle());
                    errand_deadline1.setText(mission.getDeadLine());
                    payment1.setText(money_str);
                    int status_num = mission.getMissionStatus();
                    if(status_num == 0){
                        status_str = "    待领取";
                    } else if(status_num == 1){
                        status_str = "    待完成";
                    } else if(status_num == 2){
                        status_str = "    待验收";
                    } else if(status_num == 3){
                        status_str = "    已完成";
                    }
                    errand_status1.setText(status_str);
                    errand_status1.setTextSize(21);
                }
                if(msg.what == 2){
                    errand_discription1.setText(errand.getDescription());
                    errand_private_info1.setText(errand.getPrivateInfo());
                }
                if(msg.what == 3){
                    String acc_user_info = "姓名:" + user.getName() + '\n';
                    acc_user_info += "手机:" + user.getPhoneNum();
                    info1.setText(acc_user_info);
                }

            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.errand_page_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void getComponentById()
    {
        errand_title1 = findViewById(R.id.errand_title1);
        errand_status1 = findViewById(R.id.errand_status1);
        info1 = findViewById(R.id.info1);
        errand_discription1 = findViewById(R.id.errand_description1);
        errand_private_info1 = findViewById(R.id.errand_private_info1);
        errand_deadline1 = findViewById(R.id.errand_deadline1);
        payment1 = findViewById(R.id.payment1);

    }
    public void loadData(View view){
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
//                    Toast.makeText(getApplicationContext(),
//                            "get mission success", Toast.LENGTH_SHORT).show();
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
//                    Toast.makeText(getApplicationContext(),
//                            "get task success", Toast.LENGTH_SHORT).show();
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
//                                    Toast.makeText(getApplicationContext(),
//                                            "get errand success", Toast.LENGTH_SHORT).show();
                                    errand = response.body();
                                    Message msg = new Message();
                                    msg.what = 2;
                                    handler.sendMessage(msg);
                                }
                                else if(response.code() == 401)
                                {
                                    Toast.makeText(getApplicationContext(),
                                            "Invalid username/password supplied", Toast.LENGTH_SHORT).show();
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
                        int userId = task_list.get(0).getAccUserId();
                        Log.d("userId", "onResponse: "+ userId);
                        Log.d("userId", "onResponse: " + task_list.get(0).getPubUserId());
                        Call<User> myCall4 = myService.getUserById(token,userId);
                        myCall4.enqueue(new Callback<User>() {
                            //请求成功时回调
                            @Override
                            public void onResponse(Call<User> call, final Response<User> response) {
                                // 步骤7：处理返回的数据结果
                                if(response.code() == 200)
                                {
//                                    Toast.makeText(getApplicationContext(),
//                                            "get user success", Toast.LENGTH_SHORT).show();
                                    user = response.body();
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
                                            "user not found", Toast.LENGTH_SHORT).show();
                                } else{
                                    Toast.makeText(getApplicationContext(),
                                            "Unknown error", Toast.LENGTH_SHORT).show();
                                }

                            }
                            //请求失败时回调
                            @Override
                            public void onFailure(Call<User> call, Throwable throwable) {
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
