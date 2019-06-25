package com.example.asus.earingmoney.Util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.asus.earingmoney.MainPartActivity;
import com.example.asus.earingmoney.model.Image;
import com.example.asus.earingmoney.model.Msg;
import com.example.asus.earingmoney.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;

public class Util {
    public static List<String> decodeChoiceStr(String choiceStr)
    {
        String[] decodeChoices = choiceStr.split("\\|");
        List<String> choices = Arrays.asList(decodeChoices);
        return choices;
    }

    //返回token值
    public static String getToken(Context context)
    {
        SharedPreferences user_shared_preference = context.getSharedPreferences("user", 0);
        return user_shared_preference.getString("token", "");
    }

    //返回userId值
    public static int getUserId(Context context)
    {
        SharedPreferences user_shared_preference = context.getSharedPreferences("user", 0);
        return user_shared_preference.getInt("userId", 0);
    }

    //返回missionId值
    public static int getMissionId(Context context)
    {
        SharedPreferences user_shared_preference = context.getSharedPreferences("user", 0);
        return user_shared_preference.getInt("missionId", 0);
    }

    public static  String convertDate2String(Date date)
    {
        String year = Integer.toString(date.getYear() + 1900);
        String month = Integer.toString(date.getMonth()+1);
        String day = Integer.toString(date.getDay());
        if(month.length() == 1)
            month = "0" + month;
        if(day.length() == 1)
            day = "0" +day;
        String dateString = year + "-" + month + "-" + day;
        return dateString;
    }

//    public static void setListViewHeightBasedOnChildren(ListView listView) {
//        //获取ListView对应的Adapter
//        ListAdapter listAdapter = listView.getAdapter();
//        if (listAdapter == null) {
//            // pre-condition
//            return;
//        }
//
//        int totalHeight = 0;
//        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {   //listAdapter.getCount()返回数据项的数目
//            View listItem = listAdapter.getView(i, null, listView);
//            listItem.measure(0, 0);  //计算子项View 的宽高
//            totalHeight += listItem.getMeasuredHeight();  //统计所有子项的总高度
//        }
//
//        ViewGroup.LayoutParams params = listView.getLayoutParams();
//        params.height = totalHeight;
//        listView.setLayoutParams(params);
//    }
}
