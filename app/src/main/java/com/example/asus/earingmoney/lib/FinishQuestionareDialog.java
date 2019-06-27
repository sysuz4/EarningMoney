package com.example.asus.earingmoney.lib;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.earingmoney.R;
import com.example.asus.earingmoney.Util.Constants;
import com.example.asus.earingmoney.adapter.MyAdapter;
import com.example.asus.earingmoney.creat_errand_activity;
import com.example.asus.earingmoney.createQuestionare;
import com.example.asus.earingmoney.model.QuestionModel;
import com.example.asus.earingmoney.model.ScreenUtils;
import com.flyco.animation.FadeEnter.FadeEnter;
import com.flyco.animation.FadeExit.FadeExit;
import com.flyco.animation.NewsPaperEnter;
import com.flyco.dialog.utils.CornerUtils;
import com.flyco.dialog.widget.base.BaseDialog;

import java.util.Date;
import java.util.List;

public class FinishQuestionareDialog extends BaseDialog<FinishQuestionareDialog> {
    private EditText moneyText;
    private TextView tv_yes;
    private TextView tv_exit;
    private RadioGroup targetGroup;
    private Toast mToast;
    private EditText taskNumText;

    public FinishQuestionareDialog(Context context) {
        super(context);
    }

    @Override
    public View onCreateView() {
        widthScale(0.85f);

        mToast = Toast.makeText(mContext, "", Toast.LENGTH_SHORT);
        // dismissAnim(this, new ZoomOutExit());
        View inflate = View.inflate(mContext, R.layout.create_questionare_dialog_layout, null);
        moneyText = inflate.findViewById(R.id.moneyText);
        tv_yes = inflate.findViewById( R.id.tv_yes);
        tv_exit = inflate.findViewById(R.id.tv_exit);
        targetGroup = inflate.findViewById(R.id.targetGroup);
        taskNumText = inflate.findViewById(R.id.taskNumText);
        inflate.setBackgroundDrawable(
                CornerUtils.cornerDrawable(Color.parseColor("#ffffff"), dp2px(5)));


        return inflate;
    }

    @Override
    public void setUiBeforShow() {
        tv_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(moneyText.getText().toString().isEmpty() || taskNumText.getText().toString().isEmpty())
                    toast("金额或任务限制人数不能为空");
                else
                {

                    Float money = Float.valueOf(moneyText.getText().toString());
                    if(taskNumText.getText().toString().indexOf('.')!=-1)
                    {
                        toast("人数必须为整数");
                        return;
                    }
                    int taskNum = Integer.valueOf(taskNumText.getText().toString());
                    if(mContext instanceof createQuestionare)
                    {
                        createQuestionare activity = (createQuestionare)mContext;
                        activity.money = money;
                        activity.taskNum = taskNum;
                        activity.tags = str;
                    }
                    else if(mContext instanceof creat_errand_activity)
                    {
                        if(taskNum != 1)
                        {
                            toast("跑腿任务人数只能为1");
                            return;
                        }
                        creat_errand_activity activity = (creat_errand_activity)mContext;
                        activity.money = money;
                        activity.taskNum = taskNum;
                        activity.tags = str;
                    }
                    else
                        Log.e("context:", "cant solve this context in finishiQuesionare dialog");
                    //toast(money + " " + taskNum);
                    dismiss();
                    showDialog();
                    /*
                    final FinishQuestionareDialog2 dialog = new FinishQuestionareDialog2(mContext);
                    dialog.showAnim(new FadeEnter())//
                            .dismissAnim(new FadeExit())//
                            .show();
                    dialog.setCanceledOnTouchOutside(false);
                    */
                }
            }
        });

        tv_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void toast(String toast) {
        mToast.setText(toast);
        mToast.show();
    }

    private Handler handler;
    private String str = "";
    private Button choice1;
    private Button choice2;
    private Button choice3;
    private Button choice4;
    private Button continue_button;
    private TextView title;

    public String showDialog(){

        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_layout,null,false);

        choice1 = view.findViewById(R.id.choice1);
        choice2 = view.findViewById(R.id.choice2);
        choice3 = view.findViewById(R.id.choice3);
        choice4 = view.findViewById(R.id.choice4);
        continue_button = view.findViewById(R.id.continue_button);
        title = view.findViewById(R.id.title);
        final AlertDialog dialog = new AlertDialog.Builder(mContext).setView(view).create();


        handler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                super.handleMessage(msg);
                if(msg.what == 1){
                    choice1.setBackgroundColor(mContext.getResources().getColor(R.color.blue));
                    String s = choice1.getText().toString() + "、";
                    if(str.indexOf(s) == -1){
                        str += s;
                    }

                }
                if(msg.what == 2){
                    choice2.setBackgroundColor(mContext.getResources().getColor(R.color.blue));
                    String s = choice2.getText().toString() + "、";
                    if(str.indexOf(s) == -1){
                        str += s;
                    }
                }
                if(msg.what == 3){
                    choice3.setBackgroundColor(mContext.getResources().getColor(R.color.blue));
                    String s = choice3.getText().toString() + "、";
                    if(str.indexOf(s) == -1){
                        str += s;
                    }
                }
                if(msg.what == 4)
                {
                    choice4.setBackgroundColor(mContext.getResources().getColor(R.color.blue));
                    String s = choice4.getText().toString() + "、";
                    if(str.indexOf(s) == -1){
                        str += s;
                    }
                }
                if(msg.what == 5){
                    choice1.setBackgroundColor(mContext.getResources().getColor(R.color.gray));
                    str = str.replaceAll(choice1.getText().toString()+"、","");
                }
                if(msg.what == 6){
                    choice2.setBackgroundColor(mContext.getResources().getColor(R.color.gray));
                    str = str.replaceAll(choice2.getText().toString()+"、","");
                }
                if(msg.what == 7){
                    choice3.setBackgroundColor(mContext.getResources().getColor(R.color.gray));
                    str = str.replaceAll(choice3.getText().toString()+"、","");
                }
                if(msg.what == 8){
                    choice4.setBackgroundColor(mContext.getResources().getColor(R.color.gray));
                    str = str.replaceAll(choice4.getText().toString()+"、","");
                }
                if(msg.what == 9){
                    title.setText("专业");
                    choice1.setText("IT");
                    choice2.setText("经管");
                    choice3.setText("物化生医");
                    choice4.setText("文史哲");
                    choice1.setBackgroundColor(mContext.getResources().getColor(R.color.gray));
                    choice2.setBackgroundColor(mContext.getResources().getColor(R.color.gray));
                    choice3.setBackgroundColor(mContext.getResources().getColor(R.color.gray));
                    choice4.setBackgroundColor(mContext.getResources().getColor(R.color.gray));
                }
                if(msg.what == 10){
                    title.setText("个人特性");
                    choice1.setText("热情开朗");
                    choice2.setText("文艺青年");
                    choice3.setText("忧郁小王子");
                    choice4.setText("沉着冷静");
                    choice1.setBackgroundColor(mContext.getResources().getColor(R.color.gray));
                    choice2.setBackgroundColor(mContext.getResources().getColor(R.color.gray));
                    choice3.setBackgroundColor(mContext.getResources().getColor(R.color.gray));
                    choice4.setBackgroundColor(mContext.getResources().getColor(R.color.gray));
                }
                if(msg.what == 11){
                    title.setText("爱好");
                    choice1.setText("体育运动");
                    choice2.setText("音乐绘画");
                    choice3.setText("二次元");
                    choice4.setText("影视书籍");
                    choice1.setBackgroundColor(mContext.getResources().getColor(R.color.gray));
                    choice2.setBackgroundColor(mContext.getResources().getColor(R.color.gray));
                    choice3.setBackgroundColor(mContext.getResources().getColor(R.color.gray));
                    choice4.setBackgroundColor(mContext.getResources().getColor(R.color.gray));
                }
                if(msg.what == 12){
                    if(mContext instanceof createQuestionare)
                    {
                        createQuestionare activity = (createQuestionare)mContext;
                        activity.tags = str;
                    }
                    else if(mContext instanceof creat_errand_activity)
                    {
                        creat_errand_activity activity = (creat_errand_activity)mContext;
                        activity.tags = str;
                    }

                    dialog.dismiss();

                    final FinishQuestionareDialog2 dialog = new FinishQuestionareDialog2(mContext);
                    dialog.showAnim(new FadeEnter())//
                            .dismissAnim(new FadeExit())//
                            .show();
                    dialog.setCanceledOnTouchOutside(false);
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
        dialog.getWindow().setLayout((ScreenUtils.getScreenWidth(mContext)), LinearLayout.LayoutParams.WRAP_CONTENT);
        return str;
    }
}


