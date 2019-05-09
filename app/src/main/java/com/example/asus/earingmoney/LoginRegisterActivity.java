package com.example.asus.earingmoney;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dd.processbutton.iml.ActionProcessButton;

import de.hdodenhof.circleimageview.CircleImageView;

public class LoginRegisterActivity extends AppCompatActivity {

    private ConstraintLayout login_area;
    private CircleImageView login_image;
    private TextView login_user_name_text, login_password_text, register_button;
    private EditText login_user_name, login_password;
    private Button login_bt_username_clear, login_bt_password_clear;
    private ActionProcessButton log_in;
    private TextWatcher login_username_watcher, login_password_watcher;
    private boolean has_username, has_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);

        login_area = (ConstraintLayout)findViewById(R.id.login_area);
        login_user_name = (EditText)findViewById(R.id.login_user_name);
        login_password = (EditText)findViewById(R.id.login_password);
        login_bt_username_clear = (Button)findViewById(R.id.login_bt_username_clear);
        login_bt_password_clear = (Button)findViewById(R.id.login_bt_password_clear);
        log_in = (ActionProcessButton)findViewById(R.id.log_in);
        register_button = (TextView)findViewById(R.id.register_button);
        initWatcher();
        login_user_name.addTextChangedListener(login_username_watcher);
        login_password.addTextChangedListener(login_password_watcher);

        log_in.setEnabled(false);

        login_bt_username_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login_user_name.setText("");
                has_username = false;
                log_in.setEnabled(false);
            }
        });

        login_bt_password_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login_password.setText("");
                has_password = false;
                log_in.setEnabled(false);
            }
        });

        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login_area.setVisibility(View.INVISIBLE);
            }
        });
    }

    //用来判断两个edittext是否有内容，如果有再显示清除按钮
    private void initWatcher() {
        login_username_watcher = new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            public void beforeTextChanged(CharSequence s, int start, int count,int after) {}
            public void afterTextChanged(Editable s) {
                login_password.setText("");
                if(s.toString().length()>0){
                    login_bt_username_clear.setVisibility(View.VISIBLE);
                    has_username = true;
                    if(has_username && has_password)
                        log_in.setEnabled(true);
                }else{
                    login_bt_username_clear.setVisibility(View.INVISIBLE);
                    has_username = false;
                }
            }
        };

        login_password_watcher = new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            public void beforeTextChanged(CharSequence s, int start, int count,int after) {}
            public void afterTextChanged(Editable s) {
                if(s.toString().length()>0){
                    login_bt_password_clear.setVisibility(View.VISIBLE);
                    has_password = true;
                    if(has_username && has_password)
                        log_in.setEnabled(true);
                }else{
                    login_bt_password_clear.setVisibility(View.INVISIBLE);
                    has_password = false;
                }
            }
        };
    }
}
