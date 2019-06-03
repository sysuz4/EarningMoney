package com.example.asus.earingmoney.Util;

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
}
