package com.example.asus.earingmoney;

import android.app.AlertDialog;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.asus.earingmoney.model.ScreenUtils;

public class Dialog extends AppCompatActivity {
    private Button button;
    private Handler handler;
    private String str = "年级：";
    private Button choice1;
    private Button choice2;
    private Button choice3;
    private Button choice4;
    private Button continue_button;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);

        button = findViewById(R.id.button);
    }

    public String showDialog(View view1){
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_layout,null,false);

        choice1 = view.findViewById(R.id.choice1);
        choice2 = view.findViewById(R.id.choice2);
        choice3 = view.findViewById(R.id.choice3);
        choice4 = view.findViewById(R.id.choice4);
        continue_button = view.findViewById(R.id.continue_button);
        title = view.findViewById(R.id.title);
        final AlertDialog dialog = new AlertDialog.Builder(this).setView(view).create();


        handler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                super.handleMessage(msg);
                if(msg.what == 1){
                    choice1.setBackgroundColor(getResources().getColor(R.color.blue));
                    String s = choice1.getText().toString() + "、";
                    if(str.indexOf(s) == -1){
                        str += s;
                    }

                }
                if(msg.what == 2){
                    choice2.setBackgroundColor(getResources().getColor(R.color.blue));
                    String s = choice2.getText().toString() + "、";
                    if(str.indexOf(s) == -1){
                        str += s;
                    }
                }
                if(msg.what == 3){
                    choice3.setBackgroundColor(getResources().getColor(R.color.blue));
                    String s = choice3.getText().toString() + "、";
                    if(str.indexOf(s) == -1){
                        str += s;
                    }
                }
                if(msg.what == 4)
                {
                    choice4.setBackgroundColor(getResources().getColor(R.color.blue));
                    String s = choice4.getText().toString() + "、";
                    if(str.indexOf(s) == -1){
                        str += s;
                    }
                }
                if(msg.what == 5){
                    choice1.setBackgroundColor(getResources().getColor(R.color.gray));
                    str = str.replaceAll(choice1.getText().toString()+"、","");
                }
                if(msg.what == 6){
                    choice2.setBackgroundColor(getResources().getColor(R.color.gray));
                    str = str.replaceAll(choice2.getText().toString()+"、","");
                }
                if(msg.what == 7){
                    choice3.setBackgroundColor(getResources().getColor(R.color.gray));
                    str = str.replaceAll(choice3.getText().toString()+"、","");
                }
                if(msg.what == 8){
                    choice4.setBackgroundColor(getResources().getColor(R.color.gray));
                    str = str.replaceAll(choice4.getText().toString()+"、","");
                }
                if(msg.what == 9){
                    title.setText("专业");
                    choice1.setText("IT");
                    choice2.setText("经管");
                    choice3.setText("物化生医");
                    choice4.setText("文史哲");
                    choice1.setBackgroundColor(getResources().getColor(R.color.gray));
                    choice2.setBackgroundColor(getResources().getColor(R.color.gray));
                    choice3.setBackgroundColor(getResources().getColor(R.color.gray));
                    choice4.setBackgroundColor(getResources().getColor(R.color.gray));
                    str += "专业：";
                }
                if(msg.what == 10){
                    title.setText("个人特性");
                    choice1.setText("热情开朗");
                    choice2.setText("文艺青年");
                    choice3.setText("忧郁小王子");
                    choice4.setText("沉着冷静");
                    choice1.setBackgroundColor(getResources().getColor(R.color.gray));
                    choice2.setBackgroundColor(getResources().getColor(R.color.gray));
                    choice3.setBackgroundColor(getResources().getColor(R.color.gray));
                    choice4.setBackgroundColor(getResources().getColor(R.color.gray));
                    str += "个人特性：";
                }
                if(msg.what == 11){
                    title.setText("爱好");
                    choice1.setText("体育运动");
                    choice2.setText("音乐绘画");
                    choice3.setText("二次元");
                    choice4.setText("影视书籍");
                    choice1.setBackgroundColor(getResources().getColor(R.color.gray));
                    choice2.setBackgroundColor(getResources().getColor(R.color.gray));
                    choice3.setBackgroundColor(getResources().getColor(R.color.gray));
                    choice4.setBackgroundColor(getResources().getColor(R.color.gray));
                    str += "爱好：";
                }
                if(msg.what == 12){
                    dialog.dismiss();
                }
            }
        };

        choice1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message msg = new Message();
                msg.what = 1;
                handler.sendMessage(msg);
            }
        });
        choice1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Message msg = new Message();
                msg.what = 5;
                handler.sendMessage(msg);
                return true;
            }
        });

        choice2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message msg = new Message();
                msg.what = 2;
                handler.sendMessage(msg);
            }
        });
        choice2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Message msg = new Message();
                msg.what = 6;
                handler.sendMessage(msg);
                return true;
            }
        });

        choice3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message msg = new Message();
                msg.what = 3;
                handler.sendMessage(msg);
            }
        });
        choice3.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Message msg = new Message();
                msg.what = 7;
                handler.sendMessage(msg);
                return true;
            }
        });

        choice4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message msg = new Message();
                msg.what = 4;
                handler.sendMessage(msg);
                // dialog.dismiss();
            }
        });
        choice4.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Message msg = new Message();
                msg.what = 8;
                handler.sendMessage(msg);
                return true;
            }
        });

        continue_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str += ";";
                Log.d("testing", "onClick: " + str);
                if(title.getText().toString().equals("年级")){
                    Message msg = new Message();
                    msg.what = 9;
                    handler.sendMessage(msg);
                } else if(title.getText().toString().equals("专业")){
                    Message msg = new Message();
                    msg.what = 10;
                    handler.sendMessage(msg);
                } else if(title.getText().toString().equals("个人特性")){
                    Message msg = new Message();
                    msg.what = 11;
                    handler.sendMessage(msg);
                } else if(title.getText().toString().equals("爱好")) {
                    Message msg = new Message();
                    msg.what = 12;
                    handler.sendMessage(msg);
                }


            }
        });

        dialog.show();
        //此处设置位置窗体大小 注意一定要在show方法调用后再写设置窗口大小的代码，否则不起效果会
        dialog.getWindow().setLayout((ScreenUtils.getScreenWidth(this)), LinearLayout.LayoutParams.WRAP_CONTENT);
        return str;
    }

}
