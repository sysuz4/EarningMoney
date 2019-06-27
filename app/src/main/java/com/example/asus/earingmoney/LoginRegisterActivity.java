package com.example.asus.earingmoney;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.processbutton.iml.ActionProcessButton;
import com.example.asus.earingmoney.Util.Constants;
import com.example.asus.earingmoney.Util.Util;
import com.example.asus.earingmoney.model.GetTokenObj;
import com.example.asus.earingmoney.model.Image;
import com.example.asus.earingmoney.model.Msg;
import com.example.asus.earingmoney.model.User;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
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
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LoginRegisterActivity extends AppCompatActivity {

    public static LoginRegisterActivity instance = null;
    private service myservice;
    public ServiceFactory serviceFactory;
    private ConstraintLayout login_area;
    private ScrollView register_area_1, register_area_2, register_area_3, register_area_4;
    private CircleImageView register_image;
    private TextView register_button, login_button, continue_1, continue_2, continue_3;
    private EditText login_username, login_password, register_nickname, register_password, register_password_clarify, register_name,
            register_age, register_major, register_id, register_mail, register_phone;
    private Button login_username_clear, login_password_clear, register_nickname_clear, register_password_clear, register_password_clarify_clear, register_name_clear,
            register_age_clear, register_major_clear, register_id_clear, register_mail_clear, register_phone_clear;
    private RadioGroup register_sex_group, register_grade_group;
    private ActionProcessButton log_in, register;
    private TextWatcher login_username_watcher, login_password_watcher, register_nickname_watcher, register_password_watcher , register_password_clarify_watcher, register_name_watcher,
            register_age_watcher, register_major_watcher, register_id_watcher, register_mail_watcher, register_phone_watcher;
    private boolean login_has_username, login_has_password, register_has_nickname, register_has_password, register_has_password_clarify,
            register_has_name, register_has_credit, register_has_sex, register_has_grade, had_login_in, token_valid;

    private String default_image = null;
    private String image_name = null;
    private int current_sex, current_grade;
    SharedPreferences user_shared_preference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);

        if(MainPartActivity.instance != null){//使得登录后按返回按钮不会返回登录界面
            MainPartActivity.instance.finish();
        }

        instance = this;

        serviceFactory = new ServiceFactory();
        myservice = serviceFactory.CreatService();

        user_shared_preference = getSharedPreferences("user", 0);
//        had_login_in = user_shared_preference.getBoolean("had_user",false);
//        if(had_login_in){
//            Intent intent = new Intent(LoginRegisterActivity.this,MainPartActivity.class);
//            startActivity(intent);
//        }

        check_token();//判断token是否过期，如过期则需重新登录

        current_sex = 2;//初始化性别为未知
        current_grade = 0;

        login_area = (ConstraintLayout)findViewById(R.id.login_area);
        register_area_1 = (ScrollView)findViewById(R.id.register_area_1);
        register_area_2 = (ScrollView)findViewById(R.id.register_area_2);
        register_area_3 = (ScrollView)findViewById(R.id.register_area_3);
        register_area_4 = (ScrollView)findViewById(R.id.register_area_4);

        register_image = (CircleImageView)findViewById(R.id.register_image);

        login_username = (EditText)findViewById(R.id.login_username);
        login_password = (EditText)findViewById(R.id.login_password);

        register_nickname = (EditText)findViewById(R.id.register_nickname);
        register_password = (EditText)findViewById(R.id.register_password);
        register_password_clarify = (EditText)findViewById(R.id.register_password_clarify);
        register_name = (EditText)findViewById(R.id.register_name);
        register_age = (EditText)findViewById(R.id.register_age);
        register_major = (EditText)findViewById(R.id.register_major);
        register_id = (EditText)findViewById(R.id.register_id);
        register_mail = (EditText)findViewById(R.id.register_mail);
        register_phone = (EditText)findViewById(R.id.register_phone);

        login_username_clear = (Button)findViewById(R.id.login_username_clear);
        login_password_clear = (Button)findViewById(R.id.login_password_clear);

        register_nickname_clear = (Button)findViewById(R.id.register_nickname_clear);
        register_password_clear = (Button)findViewById(R.id.register_password_clear);
        register_password_clarify_clear = (Button)findViewById(R.id.register_password_clarify_clear);
        register_name_clear = (Button)findViewById(R.id.register_name_clear);
        register_age_clear = (Button)findViewById(R.id.register_age_clear);
        register_major_clear = (Button)findViewById(R.id.register_major_clear);
        register_id_clear = (Button)findViewById(R.id.register_id_clear);
        register_mail_clear = (Button)findViewById(R.id.register_mail_clear);
        register_phone_clear = (Button)findViewById(R.id.register_phone_clear);

        register_sex_group = (RadioGroup)findViewById(R.id.register_sex_group);
        register_grade_group = (RadioGroup)findViewById(R.id.register_grade_group);

        log_in = (ActionProcessButton)findViewById(R.id.log_in);
        register = (ActionProcessButton)findViewById(R.id.register);

        login_button = (TextView)findViewById(R.id.login_button);
        register_button = (TextView)findViewById(R.id.register_button);
        continue_1 = (TextView)findViewById(R.id.continue_1);
        continue_2 = (TextView)findViewById(R.id.continue_2);
        continue_3 = (TextView)findViewById(R.id.continue_3);

        initWatcher();//初始化Edittext监听器

        login_username.addTextChangedListener(login_username_watcher);
        login_password.addTextChangedListener(login_password_watcher);
        register_nickname.addTextChangedListener(register_nickname_watcher);
        register_password.addTextChangedListener(register_password_watcher);
        register_password_clarify.addTextChangedListener(register_password_clarify_watcher);
        register_name.addTextChangedListener(register_name_watcher);
        register_age.addTextChangedListener(register_age_watcher);
        register_major.addTextChangedListener(register_major_watcher);
        register_id.addTextChangedListener(register_id_watcher);
        register_mail.addTextChangedListener(register_mail_watcher);
        register_phone.addTextChangedListener(register_phone_watcher);

        log_in.setEnabled(false);
        register.setEnabled(false);
        register_has_sex = false;
        register_has_grade = false;

        initListener();
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            if(msg.what == -1){
                Toast.makeText(getApplicationContext(), "该用户名已被注册", Toast.LENGTH_SHORT).show();
                register.setProgress(100);
                register_area_4.setVisibility(View.GONE);
                register_area_1.setVisibility(View.VISIBLE);
            }
            else if(msg.what == -2){
                Toast.makeText(getApplicationContext(), "登录失败", Toast.LENGTH_SHORT).show();
                login_username.setText("");
                login_password.setText("");
            }
            else if(msg.what == -3){
                Toast.makeText(getApplicationContext(), msg.obj.toString(), Toast.LENGTH_SHORT).show();
                login_username.setText("");
                login_password.setText("");
            }
            else {
                register.setProgress(register.getProgress() + msg.what);
                if(register.getProgress() == 100){
                    Toast.makeText(getApplicationContext(), "注册成功", Toast.LENGTH_SHORT).show();
                    register_area_4.setVisibility(View.GONE);
                    login_area.setVisibility(View.VISIBLE);
                }
            }
        }
    };

    //用来初始化各个按钮的Listener
    private void initListener(){
        register_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 0);
            }
        });

        login_username_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login_username.setText("");
                login_has_username = false;
                log_in.setEnabled(false);
            }
        });

        login_password_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login_password.setText("");
                login_has_password = false;
                log_in.setEnabled(false);
            }
        });

        register_nickname_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register_nickname.setText("");
                register_has_nickname = false;
                register.setEnabled(false);
            }
        });

        register_password_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register_password.setText("");
                register_has_password = false;
                register.setEnabled(false);
            }
        });

        register_password_clarify_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register_password_clarify.setText("");
                register_has_password_clarify = false;
                register.setEnabled(false);
            }
        });

        register_name_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register_name.setText("");
                register_has_name = false;
                register.setEnabled(false);
            }
        });

        register_age_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register_age.setText("");
                register.setEnabled(false);
            }
        });

        register_major_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register_major.setText("");
                register.setEnabled(false);
            }
        });

        register_id_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register_id.setText("");
                register_has_credit = false;
                register.setEnabled(false);
            }
        });

        register_mail_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register_mail.setText("");
                register.setEnabled(false);
            }
        });

        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login_area.setVisibility(View.GONE);
                register_area_1.setVisibility(View.VISIBLE);
            }
        });

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register_area_4.setVisibility(View.GONE);
                login_area.setVisibility(View.VISIBLE);
            }
        });

        continue_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(register_password.getText().toString().equals(register_password_clarify.getText().toString()) && register_has_password && register_has_nickname){
                    register_area_1.setVisibility(View.GONE);
                    register_area_2.setVisibility(View.VISIBLE);
                }
                else if(!register_has_nickname){
                    Toast.makeText(getApplicationContext(), "请输入昵称", Toast.LENGTH_SHORT).show();
                }
                else if(!register_has_password){
                    Toast.makeText(getApplicationContext(), "您还没输入密码，请重新输入", Toast.LENGTH_SHORT).show();
                    register_password.setText("");
                    register_password_clarify.setText("");
                }
                else {
                    Toast.makeText(getApplicationContext(), "两次密码输入不一致，请重新输入", Toast.LENGTH_SHORT).show();
                    register_password.setText("");
                    register_password_clarify.setText("");
                }
            }
        });

        continue_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(register_has_name){
                    register_area_2.setVisibility(View.GONE);
                    register_area_3.setVisibility(View.VISIBLE);
                }
                else {
                    Toast.makeText(getApplicationContext(), "请输入姓名", Toast.LENGTH_SHORT).show();
                }
            }
        });

        continue_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(register_has_credit){
                    register_area_3.setVisibility(View.GONE);
                    register_area_4.setVisibility(View.VISIBLE);
                }
                else {
                    Toast.makeText(getApplicationContext(), "请输入学号", Toast.LENGTH_SHORT).show();
                }
            }
        });

        register_sex_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.female:
                        register_has_sex = true;
                        current_sex = Constants.FEMALE;
                        break;
                    case R.id.male:
                        register_has_sex = true;
                        current_sex = Constants.MALE;
                        break;
                }
            }
        });

        register_grade_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.first:
                        register_has_grade = true;
                        current_grade = 1;
                    case R.id.second:
                        register_has_grade = true;
                        current_grade = 2;
                    case R.id.third:
                        register_has_grade = true;
                        current_grade = 3;
                        break;
                    case R.id.fourth:
                        register_has_grade = true;
                        current_grade = 4;
                        break;
                }
            }
        });

        log_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = login_username.getText().toString();
                final String password = login_password.getText().toString();

                getToken(username,password);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload_img();
            }

        });
    }

    //用来判断edittext是否有内容，如果有再显示清除按钮
    private void initWatcher() {
        login_username_watcher = new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            public void beforeTextChanged(CharSequence s, int start, int count,int after) {}
            public void afterTextChanged(Editable s) {
                login_password.setText("");
                if(s.toString().length()>0){
                    login_username_clear.setVisibility(View.VISIBLE);
                    login_has_username = true;
                    if(login_has_username && login_has_password)
                        log_in.setEnabled(true);
                }else{
                    login_username_clear.setVisibility(View.INVISIBLE);
                    login_has_username = false;
                }
            }
        };

        login_password_watcher = new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            public void beforeTextChanged(CharSequence s, int start, int count,int after) {}
            public void afterTextChanged(Editable s) {
                if(s.toString().length()>0){
                    login_password_clear.setVisibility(View.VISIBLE);
                    login_has_password = true;
                    if(login_has_username && login_has_password)
                        log_in.setEnabled(true);
                }else{
                    login_password_clear.setVisibility(View.INVISIBLE);
                    login_has_password = false;
                }
            }
        };

        register_nickname_watcher = new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            public void beforeTextChanged(CharSequence s, int start, int count,int after) {}
            public void afterTextChanged(Editable s) {
                if(s.toString().length()>0){
                    register_nickname_clear.setVisibility(View.VISIBLE);
                    register_has_nickname = true;
                }else{
                    register_nickname_clear.setVisibility(View.INVISIBLE);
                    register_has_nickname = false;
                }
            }
        };

        register_password_watcher = new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            public void beforeTextChanged(CharSequence s, int start, int count,int after) {}
            public void afterTextChanged(Editable s) {
                if(s.toString().length()>0){
                    register_password_clear.setVisibility(View.VISIBLE);
                    register_has_password = true;
                }else{
                    register_password_clear.setVisibility(View.INVISIBLE);
                    register_has_password = false;
                }
            }
        };

        register_password_clarify_watcher = new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            public void beforeTextChanged(CharSequence s, int start, int count,int after) {}
            public void afterTextChanged(Editable s) {
                if(s.toString().length()>0){
                    register_password_clarify_clear.setVisibility(View.VISIBLE);
                    register_has_password_clarify = true;
                }else{
                    register_password_clarify_clear.setVisibility(View.INVISIBLE);
                    register_has_password_clarify = false;
                }
            }
        };

        register_name_watcher = new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            public void beforeTextChanged(CharSequence s, int start, int count,int after) {}
            public void afterTextChanged(Editable s) {
                if(s.toString().length()>0){
                    register_name_clear.setVisibility(View.VISIBLE);
                    register_has_name = true;
                }else{
                    register_name_clear.setVisibility(View.INVISIBLE);
                    register_has_name = false;
                }
            }
        };

        register_age_watcher = new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            public void beforeTextChanged(CharSequence s, int start, int count,int after) {}
            public void afterTextChanged(Editable s) {
                if(s.toString().length()>0){
                    register_age_clear.setVisibility(View.VISIBLE);
                }else{
                    register_age_clear.setVisibility(View.INVISIBLE);
                }
            }
        };

        register_major_watcher = new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            public void beforeTextChanged(CharSequence s, int start, int count,int after) {}
            public void afterTextChanged(Editable s) {
                if(s.toString().length()>0){
                    register_major_clear.setVisibility(View.VISIBLE);
                }else{
                    register_major_clear.setVisibility(View.INVISIBLE);
                }
            }
        };

        register_id_watcher = new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            public void beforeTextChanged(CharSequence s, int start, int count,int after) {}
            public void afterTextChanged(Editable s) {
                if(s.toString().length()>0){
                    register_id_clear.setVisibility(View.VISIBLE);
                    register_has_credit = true;
                    if(register_has_nickname && register_has_password && register_has_password_clarify && register_has_name && register_has_credit)
                        register.setEnabled(true);
                }else{
                    register_id_clear.setVisibility(View.INVISIBLE);
                    register_has_credit = false;
                }
            }
        };

        register_mail_watcher = new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            public void beforeTextChanged(CharSequence s, int start, int count,int after) {}
            public void afterTextChanged(Editable s) {
                if(s.toString().length()>0){
                    register_mail_clear.setVisibility(View.VISIBLE);
                }else{
                    register_mail_clear.setVisibility(View.INVISIBLE);
                }
            }
        };

        register_phone_watcher = new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            public void beforeTextChanged(CharSequence s, int start, int count,int after) {}
            public void afterTextChanged(Editable s) {
                if(s.toString().length()>0){
                    register_phone_clear.setVisibility(View.VISIBLE);
                }else{
                    register_phone_clear.setVisibility(View.INVISIBLE);
                }
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            // 得到图片的全路径
            Uri uri = data.getData();
            ContentResolver cr = this.getContentResolver();
            //存储副本
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                //通过UUID生成字符串文件名
                String image_name_temp = UUID.randomUUID().toString() + ".jpg";
                //存储图片
                FileOutputStream out = openFileOutput(image_name_temp, MODE_PRIVATE);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                //获取复制后文件的uri
                Uri image_file_uri = Uri.fromFile(getFileStreamPath(image_name_temp));
                //图片预览
                this.register_image.setImageURI(image_file_uri);
                //保存该URI
                default_image = image_file_uri.getPath();
                out.flush();
                out.close();
            }
            catch (FileNotFoundException e) {
                Log.e("FileNotFoundException", e.getMessage(),e);
            }
            catch (IOException e) {
                Log.w("IOException", e.getMessage(), e);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //登录，输入姓名和密码返回token值，将token值保存以便后续使用
    public void getToken(final String username, final String password) {

        myservice.post_to_get_token(username,password)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GetTokenObj>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(LoginRegisterActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(GetTokenObj response) {
                        String token;
                        int userId;
                        token = response.getToken();
                        userId = response.getUserId();
                        //System.out.println(token);
                        //Log.e("666", token);
                        //System.out.println(userId);
                        SharedPreferences.Editor editor = user_shared_preference.edit();
                        editor.putString("token",token);
                        editor.putInt("userId",userId);
                        editor.putString("username",username);
                        editor.putBoolean("had_user",true);
                        editor.commit();
                        Intent intent = new Intent(LoginRegisterActivity.this,MainPartActivity.class);
                        startActivity(intent);
                    }
                });
    }

    //上传图片
    private void upload_img(){
        if(default_image == null){
            register_new_user();
        }
        else {
            File file = new File(default_image);
            // 创建 RequestBody，用于封装构建RequestBody
            // RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpg"), file);

            // MultipartBody.Part  和后端约定好Key，这里的partName是用file
            MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);

            // 添加描述
            String descriptionString = "hello, 这是文件描述";
            RequestBody description = RequestBody.create(MediaType.parse("multipart/form-data"), descriptionString);

            OkHttpClient build = new OkHttpClient.Builder()
                    .connectTimeout(2, TimeUnit.SECONDS)
                    .readTimeout(2, TimeUnit.SECONDS)
                    .writeTimeout(2, TimeUnit.SECONDS)
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.BASEURL)
                    // 本次实验不需要自定义Gson
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    // build 即为okhttp声明的变量，下文会讲
                    .client(build)
                    .build();

            service myservice_2 = retrofit.create(service.class);
            // 执行请求
            System.out.println(Util.getToken(this));
            myservice_2.upload_pic(description, body).enqueue(new Callback<Image>() {
                @Override
                public void onResponse(Call<Image> call, Response<Image> response) {
                    if(response.code() == 201)
                    {
                        Image image = response.body();
                        image_name = image.getImageName();
                        System.out.println(image.getImageName());
                        register_new_user();
                    }
                    else
                    {
                        System.out.println(response.raw().toString());
                    }
                }
                @Override
                public void onFailure(Call<Image> call, Throwable t) {
                    System.out.println(t.toString());
                }
            });
        }
    }

    //注册新用户
    private void register_new_user(){
        final String nickname = register_nickname.getText().toString();
        final String password = register_password.getText().toString();
        final String name = register_name.getText().toString();
        final String age_temp = register_age.getText().toString();
        final int age;
        if(age_temp.isEmpty())
            age = -1;
        else
            age = Integer.parseInt(age_temp);
        final int sex = current_sex;
        final int grade = current_grade;
        final String major = register_major.getText().toString();
        final String id_temp = register_id.getText().toString();
        final int id = Integer.parseInt(id_temp);
        final String mail = register_mail.getText().toString();
        final String phone_temp = register_phone.getText().toString();
        final int phone;
        if(phone_temp.isEmpty())
            phone = -1;
        else
            phone = Integer.parseInt(phone_temp);

        register.setProgress(0);

        new Thread(){
            @Override
            public void run () {
                //params.setUseJsonStreamer(true);
                JSONObject body = new JSONObject();
                try {
                    body.put("userId", 4396);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    body.put("avator", image_name);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    body.put("userType", 0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    body.put("name", name);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    body.put("nickName", nickname);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    body.put("age", age);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    body.put("sex", sex);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    body.put("grade", grade);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    body.put("major", major);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    body.put("mailAddr", mail);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    body.put("phoneNum", phone);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    body.put("stuId", id);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    body.put("tags", "0");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    body.put("password", password);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Message msg1 = new Message();
                msg1.what = 20;
                handler.sendMessage(msg1);

                String urlPath = "http://106.14.225.59/users";
                URL url = null;
                try {
                    url = new URL(urlPath);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                HttpURLConnection conn = null;
                try {
                    conn = (HttpURLConnection) url.openConnection();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                conn.setConnectTimeout(5000);
                // 设置允许输出
                conn.setDoOutput(true);
                conn.setDoInput(true);
                try {
                    conn.setRequestMethod("POST");
                } catch (ProtocolException e) {
                    e.printStackTrace();
                }

                Message msg2 = new Message();
                msg2.what = 20;
                handler.sendMessage(msg2);

                // 设置contentType
                conn.setRequestProperty("Content-Type", "application/json");
                DataOutputStream os = null;
                try {
                    os = new DataOutputStream(conn.getOutputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Message msg3 = new Message();
                msg3.what = 20;
                handler.sendMessage(msg3);

                String content = String.valueOf(body);
                try {
                    os.writeBytes(content);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    os.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    //System.out.println(conn.getResponseCode());
                    if (conn.getResponseCode() == 201) {
                        InputStreamReader in = new InputStreamReader(conn.getInputStream());
                        BufferedReader bf = new BufferedReader(in);
                        String recieveData = null;
                        String result = "";
                        while ((recieveData = bf.readLine()) != null) {
                            result += recieveData + "\n";
                        }
                        //System.out.println(result);
                        in.close();
                        conn.disconnect();

                        Message msg4 = new Message();
                        msg4.what = 40;
                        handler.sendMessage(msg4);

                        getToken(nickname,password);
                    }
                    else if(conn.getResponseCode() ==  409){
                        Message msg5 = new Message();
                        msg5.what = -1;
                        handler.sendMessage(msg5);
                    }
                    else{
                        Message msg5 = new Message();
                        msg5.what = -3;
                        msg5.obj = conn.getResponseCode();
                        handler.sendMessage(msg5);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void check_token(){
        Observer<Response<ResponseBody>> observer_2 = new Observer<Response<ResponseBody>>() {
            @Override
            public void onNext(Response<ResponseBody> r) {
                //System.out.println(r.code());
                if(r.code() == 200){
                    token_valid = true;
                }
                else {
                    token_valid = false;
                    Toast.makeText(getApplicationContext(), "登录已失效", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCompleted() {
                if(token_valid){
                    Intent intent = new Intent(LoginRegisterActivity.this,MainPartActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(getApplicationContext(), "登录已失效", Toast.LENGTH_SHORT).show();
                token_valid = false;
            }
        };
        myservice.check_token(Util.getToken(this))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer_2);


    }
}
