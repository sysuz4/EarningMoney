package com.example.asus.earingmoney.Util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Util {
    public static List<String> decodeChoiceStr(String choiceStr)
    {
        String[] decodeChoices = choiceStr.split("\\|");
        List<String> choices = Arrays.asList(decodeChoices);
        return choices;
    }

    public static String getToken(Context context){
        SharedPreferences user_shared_preference = context.getSharedPreferences("user", 0);
        return user_shared_preference.getString("token","");
    }

    public static int getUserId(Context context){
        SharedPreferences user_shared_preference = context.getSharedPreferences("user", 0);
        return user_shared_preference.getInt("userId",-1);
    }
}
