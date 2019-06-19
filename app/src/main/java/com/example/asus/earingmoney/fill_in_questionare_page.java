package com.example.asus.earingmoney;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.earingmoney.model.Questionare;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okio.BufferedSource;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class fill_in_questionare_page extends AppCompatActivity {

    private TextView textView;
    private Handler handle;
    private String return_json;
    private int code = 0;
    private service myService;
    public ServiceFactory serviceFactory;
    public Questionare data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_in_questionare_page);

        textView = findViewById(R.id.textview);

        serviceFactory = new ServiceFactory();
        myService = serviceFactory.CreatService();

        handle = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 200: // success
                        textView.setText(return_json);
                        break;
                    case 401: // Invalid username/password supplied
                        Toast.makeText(getApplicationContext(), "Invalid username/password supplied", Toast.LENGTH_SHORT).show();
                        break;
                    case 404: // task not found
                        Toast.makeText(getApplicationContext(), "task not found", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(getApplicationContext(), String.valueOf(code), Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    public void test(View view){

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

        service myservice = retrofit.create(service.class);
        Call<Questionare> call = myservice.get_questionare(1);

        call.enqueue(new Callback<Questionare>() {
            //请求成功时回调
            @Override
            public void onResponse(Call<Questionare> call, final Response<Questionare> response) {
                // 步骤7：处理返回的数据结果
                if(response.code() == 200)
                {
                    Toast.makeText(getApplicationContext(), "200",
                            Toast.LENGTH_SHORT).show();
                }
                else if(response.code() == 401){
                    Toast.makeText(getApplicationContext(), "401",
                            Toast.LENGTH_SHORT).show();
                }
                else if(response.code() == 404){
                    Toast.makeText(getApplicationContext(), "404",
                            Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), String.valueOf(response.code()),
                            Toast.LENGTH_SHORT).show();
                }

            }
            //请求失败时回调
            @Override
            public void onFailure(Call<Questionare> call, Throwable throwable) {
                System.out.println("连接失败");
            }
        });

    }
}
