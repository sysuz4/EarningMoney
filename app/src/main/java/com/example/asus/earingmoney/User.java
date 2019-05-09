package com.example.asus.earingmoney;

import java.util.ArrayList;

public class User {
    private int userType;//判断是普通用户还是管理员
    private String name;
    private String avator;//头像
    private String nickName;//昵称
    private int age;
    private int sex;
    private int grade;
    private String major;
    private String mailAdd;
    private String phoneNum;
    private String creditVal;//学号
    private int balance;//余额
    private ArrayList<String> tags;//标签，用于分类
    private String password;

    public User() {
    }
}
