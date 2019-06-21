package com.example.asus.earingmoney.Util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Util {
    public static List<String> decodeChoiceStr(String choiceStr)
    {
        String[] decodeChoices = choiceStr.split("\\|");
        List<String> choices = Arrays.asList(decodeChoices);
        return choices;
    }

    public static String getToken(Context context)
    {
        SharedPreferences user_shared_preference = context.getSharedPreferences("user", 0);
        return user_shared_preference.getString("token", "").replaceAll("\r|\n", "");
    }

    public static  String convertDate2String(Date date)
    {
        String year = Integer.toString(date.getYear());
        String month = Integer.toString(date.getMonth()+1);
        String day = Integer.toString(date.getDay());
        if(month.length() == 1)
            month = "0" + month;
        if(day.length() == 1)
            day = "0" +day;
        String dateString = year + "-" + month + "-" + day;
        return dateString;
    }
}
