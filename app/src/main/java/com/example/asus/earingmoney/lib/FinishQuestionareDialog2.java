package com.example.asus.earingmoney.lib;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.earingmoney.R;
import com.example.asus.earingmoney.Util.Constants;
import com.example.asus.earingmoney.adapter.MyAdapter;
import com.example.asus.earingmoney.model.QuestionModel;
import com.flyco.dialog.utils.CornerUtils;
import com.flyco.dialog.widget.base.BaseDialog;

import java.util.Date;
import java.util.List;

public class FinishQuestionareDialog2 extends BaseDialog<FinishQuestionareDialog2> {
    private DatePicker datePicker;
    private TextView tv_yes;
    private TextView tv_exit;
    private Toast mToast;

    public FinishQuestionareDialog2(Context context) {
        super(context);
    }

    @Override
    public View onCreateView() {
        widthScale(0.85f);

        mToast = Toast.makeText(mContext, "", Toast.LENGTH_SHORT);
        // dismissAnim(this, new ZoomOutExit());
        View inflate = View.inflate(mContext, R.layout.create_questionare_dialog_layout2, null);
        datePicker = inflate.findViewById(R.id.datePicker);
        tv_yes = inflate.findViewById( R.id.tv_yes);
        tv_exit = inflate.findViewById(R.id.tv_exit);
        inflate.setBackgroundDrawable(
                CornerUtils.cornerDrawable(Color.parseColor("#ffffff"), dp2px(5)));


        return inflate;
    }

    @Override
    public void setUiBeforShow() {
        tv_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String year = Integer.toString(datePicker.getYear());
                String month = Integer.toString(datePicker.getMonth());
                String day = Integer.toString(datePicker.getDayOfMonth());
                if(month.length() == 1)
                    month = "0" + month;
                if(day.length() == 1)
                    day = "0" +day;
                String date = year + "-" + month + "-" + day;

                toast(" " + date);
                dismiss();
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


