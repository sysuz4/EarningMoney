package com.example.asus.earingmoney;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceFactory {

    public service CreatService(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://106.14.225.59")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(createOkHttp())
                .build();
        return retrofit.create(service.class);
    }

    public OkHttpClient createOkHttp() {
        OkHttpClient.Builder temp = new OkHttpClient.Builder();
        temp.connectTimeout(6000, TimeUnit.MILLISECONDS);
        temp.readTimeout(6000, TimeUnit.MILLISECONDS);
        temp.writeTimeout(6000, TimeUnit.MILLISECONDS);
        temp.interceptors().clear();
        temp.networkInterceptors().clear();
        temp.retryOnConnectionFailure(true);
        return temp.build();
    }
}
