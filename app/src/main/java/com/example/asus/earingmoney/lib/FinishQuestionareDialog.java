package com.example.asus.earingmoney.lib;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.earingmoney.R;
import com.example.asus.earingmoney.Util.Constants;
import com.example.asus.earingmoney.adapter.MyAdapter;
import com.example.asus.earingmoney.creat_errand_activity;
import com.example.asus.earingmoney.createQuestionare;
import com.example.asus.earingmoney.model.QuestionModel;
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
                        toast("限制人数必须为整数");
                        return;
                    }
                    int taskNum = Integer.valueOf(taskNumText.getText().toString());
                    if(mContext instanceof createQuestionare)
                    {
                        createQuestionare activity = (createQuestionare)mContext;
                        activity.money = money;
                        activity.taskNum = taskNum;
                    }
                    else if(mContext instanceof creat_errand_activity)
                    {
                        creat_errand_activity activity = (creat_errand_activity)mContext;
                        activity.money = money;
                        activity.taskNum = taskNum;
                    }
                    else
                        Log.e("context:", "cant solve this context in finishiQuesionare dialog");
                    //toast(money + " " + taskNum);
                    dismiss();
                    final FinishQuestionareDialog2 dialog = new FinishQuestionareDialog2(mContext);
                    dialog.showAnim(new FadeEnter())//
                            .dismissAnim(new FadeExit())//
                            .show();
                    dialog.setCanceledOnTouchOutside(false);
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
}


