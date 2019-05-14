package com.example.asus.earingmoney;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.dd.processbutton.iml.ActionProcessButton;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class LoginRegisterActivity extends AppCompatActivity {

    private User user;
    private ConstraintLayout login_area;
    private ScrollView register_area;
    private CircleImageView login_image,register_image;
    private TextView register_button, login_button;
    private EditText login_user_name, login_password, register_nickname, register_password, register_password_clarify, register_name,
            register_age, register_major, register_credit, register_mail, register_phone;
    private Button login_username_clear, login_password_clear, register_nickname_clear, register_password_clear, register_password_clarify_clear, register_name_clear,
            register_age_clear, register_major_clear, register_credit_clear, register_mail_clear, register_phone_clear;
    private RadioGroup register_sex_group, register_grade_group;
    private ActionProcessButton log_in, register;
    private TextWatcher login_username_watcher, login_password_watcher, register_nickname_watcher, register_password_watcher , register_password_clarify_watcher, register_name_watcher,
            register_age_watcher, register_major_watcher, register_credit_watcher, register_mail_watcher, register_phone_watcher;
    private boolean login_has_username, login_has_password, register_has_nickname, register_has_password, register_has_password_clarify, register_has_name,
            register_has_age, register_has_sex, register_has_grade, register_has_major, register_has_credit, register_has_mail, register_has_phone;

    private String default_image = "android.resource://com.example.asus.work2/" + R.mipmap.me;
    private int current_sex, current_grade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);

        login_area = (ConstraintLayout)findViewById(R.id.login_area);
        register_area = (ScrollView)findViewById(R.id.register_area);

        register_image = (CircleImageView)findViewById(R.id.register_image);

        login_user_name = (EditText)findViewById(R.id.login_user_name);
        login_password = (EditText)findViewById(R.id.login_password);

        register_nickname = (EditText)findViewById(R.id.register_nickname);
        register_password = (EditText)findViewById(R.id.register_password);
        register_password_clarify = (EditText)findViewById(R.id.register_password_clarify);
        register_name = (EditText)findViewById(R.id.register_name);
        register_age = (EditText)findViewById(R.id.register_age);
        register_major = (EditText)findViewById(R.id.register_major);
        register_credit = (EditText)findViewById(R.id.register_credit);
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
        register_credit_clear = (Button)findViewById(R.id.register_credit_clear);
        register_mail_clear = (Button)findViewById(R.id.register_mail_clear);
        register_phone_clear = (Button)findViewById(R.id.register_phone_clear);

        register_sex_group = (RadioGroup)findViewById(R.id.register_sex_group);
        register_grade_group = (RadioGroup)findViewById(R.id.register_grade_group);

        log_in = (ActionProcessButton)findViewById(R.id.log_in);
        register = (ActionProcessButton)findViewById(R.id.register);

        login_button = (TextView)findViewById(R.id.login_button);
        register_button = (TextView)findViewById(R.id.register_button);

        initWatcher();

        login_user_name.addTextChangedListener(login_username_watcher);
        login_password.addTextChangedListener(login_password_watcher);
        register_nickname.addTextChangedListener(register_nickname_watcher);
        register_password.addTextChangedListener(register_password_watcher);
        register_password_clarify.addTextChangedListener(register_password_clarify_watcher);
        register_name.addTextChangedListener(register_name_watcher);
        register_age.addTextChangedListener(register_age_watcher);
        register_major.addTextChangedListener(register_major_watcher);
        register_credit.addTextChangedListener(register_credit_watcher);
        register_mail.addTextChangedListener(register_mail_watcher);
        register_phone.addTextChangedListener(register_phone_watcher);

        log_in.setEnabled(false);
        register.setEnabled(false);
        register_has_sex = false;
        register_has_grade = false;

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
                login_user_name.setText("");
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
                register_has_age = false;
                register.setEnabled(false);
            }
        });

        register_major_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register_major.setText("");
                register_has_major = false;
                register.setEnabled(false);
            }
        });

        register_credit_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register_credit.setText("");
                register_has_credit = false;
                register.setEnabled(false);
            }
        });

        register_mail_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register_mail.setText("");
                register_has_mail = false;
                register.setEnabled(false);
            }
        });

        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login_area.setVisibility(View.GONE);
                register_area.setVisibility(View.VISIBLE);
            }
        });

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register_area.setVisibility(View.GONE);
                login_area.setVisibility(View.VISIBLE);
            }
        });

        register_sex_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.female:
                        register_has_sex = true;
                        current_sex = 0;
                        break;
                    case R.id.male:
                        register_has_sex = true;
                        current_sex = 1;
                        break;
                }
                if(register_has_nickname && register_has_password && register_has_password_clarify && register_has_name && register_has_age
                        && register_has_sex && register_has_grade && register_has_major && register_has_credit && register_has_mail && register_has_phone)
                    register.setEnabled(true);
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
                if(register_has_nickname && register_has_password && register_has_password_clarify && register_has_name && register_has_age
                        && register_has_sex && register_has_grade && register_has_major && register_has_credit && register_has_mail && register_has_phone)
                    register.setEnabled(true);
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
                    if(register_has_nickname && register_has_password && register_has_password_clarify && register_has_name && register_has_age
                            && register_has_sex && register_has_grade && register_has_major && register_has_credit && register_has_mail && register_has_phone)
                        register.setEnabled(true);
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
                    if(register_has_nickname && register_has_password && register_has_password_clarify && register_has_name && register_has_age
                            && register_has_sex && register_has_grade && register_has_major && register_has_credit && register_has_mail && register_has_phone)
                        register.setEnabled(true);
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
                    if(register_has_nickname && register_has_password && register_has_password_clarify && register_has_name && register_has_age
                            && register_has_sex && register_has_grade && register_has_major && register_has_credit && register_has_mail && register_has_phone)
                        register.setEnabled(true);
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
                    if(register_has_nickname && register_has_password && register_has_password_clarify && register_has_name && register_has_age
                            && register_has_sex && register_has_grade && register_has_major && register_has_credit && register_has_mail && register_has_phone)
                        register.setEnabled(true);
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
                    register_has_age = true;
                    if(register_has_nickname && register_has_password && register_has_password_clarify && register_has_name && register_has_age
                            && register_has_sex && register_has_grade && register_has_major && register_has_credit && register_has_mail && register_has_phone)
                        register.setEnabled(true);
                }else{
                    register_age_clear.setVisibility(View.INVISIBLE);
                    register_has_age = false;
                }
            }
        };

        register_major_watcher = new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            public void beforeTextChanged(CharSequence s, int start, int count,int after) {}
            public void afterTextChanged(Editable s) {
                if(s.toString().length()>0){
                    register_major_clear.setVisibility(View.VISIBLE);
                    register_has_major = true;
                    if(register_has_nickname && register_has_password && register_has_password_clarify && register_has_name && register_has_age
                            && register_has_sex && register_has_grade && register_has_major && register_has_credit && register_has_mail && register_has_phone)
                        register.setEnabled(true);
                }else{
                    register_major_clear.setVisibility(View.INVISIBLE);
                    register_has_major = false;
                }
            }
        };

        register_credit_watcher = new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            public void beforeTextChanged(CharSequence s, int start, int count,int after) {}
            public void afterTextChanged(Editable s) {
                if(s.toString().length()>0){
                    register_credit_clear.setVisibility(View.VISIBLE);
                    register_has_credit = true;
                    if(register_has_nickname && register_has_password && register_has_password_clarify && register_has_name && register_has_age
                            && register_has_sex && register_has_grade && register_has_major && register_has_credit && register_has_mail && register_has_phone)
                        register.setEnabled(true);
                }else{
                    register_credit_clear.setVisibility(View.INVISIBLE);
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
                    register_has_mail = true;
                    if(register_has_nickname && register_has_password && register_has_password_clarify && register_has_name && register_has_age
                            && register_has_sex && register_has_grade && register_has_major && register_has_credit && register_has_mail && register_has_phone)
                        register.setEnabled(true);
                }else{
                    register_mail_clear.setVisibility(View.INVISIBLE);
                    register_has_mail = false;
                }
            }
        };

        register_phone_watcher = new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            public void beforeTextChanged(CharSequence s, int start, int count,int after) {}
            public void afterTextChanged(Editable s) {
                if(s.toString().length()>0){
                    register_phone_clear.setVisibility(View.VISIBLE);
                    register_has_phone = true;
                    if(register_has_nickname && register_has_password && register_has_password_clarify && register_has_name && register_has_age
                            && register_has_sex && register_has_grade && register_has_major && register_has_credit && register_has_mail && register_has_phone)
                        register.setEnabled(true);
                }else{
                    register_phone_clear.setVisibility(View.INVISIBLE);
                    register_has_phone = false;
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
                String image_name = UUID.randomUUID().toString() + ".jpg";
                //存储图片
                FileOutputStream out = openFileOutput(image_name, MODE_PRIVATE);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                //获取复制后文件的uri
                Uri image_file_uri = Uri.fromFile(getFileStreamPath(image_name));
                //图片预览
                this.register_image.setImageURI(uri);
                //保存该URI
                default_image = image_file_uri.toString();
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
}
