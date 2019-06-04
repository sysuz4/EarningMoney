package com.example.asus.earingmoney.lib;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.earingmoney.R;
import com.example.asus.earingmoney.Util.Constants;
import com.example.asus.earingmoney.adapter.MyAdapter;
import com.example.asus.earingmoney.model.QuestionModel;
import com.flyco.animation.Attention.Swing;
import com.flyco.dialog.utils.CornerUtils;
import com.flyco.dialog.widget.base.BaseDialog;

import java.util.List;

public class QueryDialog extends BaseDialog<QueryDialog> {
    private EditText questionText;
    private TextView tv_yes;
    private TextView tv_exit;
    private Toast mToast;
    private List<QuestionModel> questionModelList;

    private int modifyPosition;
    private MyAdapter myAdapter;

    public QueryDialog(Context context, List<QuestionModel> questionModelList) {
        super(context);
        this.questionModelList = questionModelList;
        this.modifyPosition= -1;
    }

    public void  setContent(int position, MyAdapter myAdapter)
    {
        this.modifyPosition = position;
        this.myAdapter = myAdapter;
    }

    @Override
    public View onCreateView() {
        widthScale(0.85f);

        mToast = Toast.makeText(mContext, "", Toast.LENGTH_SHORT);
        // dismissAnim(this, new ZoomOutExit());
        View inflate = View.inflate(mContext, R.layout.query_dialog_layout, null);
        questionText = inflate.findViewById(R.id.dialogQuestionText);
        tv_yes = inflate.findViewById( R.id.tv_yes);
        tv_exit = inflate.findViewById(R.id.tv_exit);
        inflate.setBackgroundDrawable(
                CornerUtils.cornerDrawable(Color.parseColor("#ffffff"), dp2px(5)));

        if(modifyPosition != -1)
        {
            questionText.setText(questionModelList.get(modifyPosition).getQuestion());
        }

        return inflate;
    }

    @Override
    public void setUiBeforShow() {
        tv_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(questionText.getText().toString().isEmpty())
                    toast("问题不能为空");
                else
                {
                    if(modifyPosition != -1)
                    {
                        questionModelList.get(modifyPosition).setQuestion(questionText.getText().toString());
                        myAdapter.notifyDataSetChanged();
                        dismiss();
                    }
                    else
                    {
                        QuestionModel questionModel = new QuestionModel(Constants.QUERY_QUESTION, questionText.getText().toString());
                        questionModelList.add(questionModel);
                        dismiss();
                    }

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

