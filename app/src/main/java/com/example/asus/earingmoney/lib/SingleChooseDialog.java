package com.example.asus.earingmoney.lib;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.service.chooser.ChooserTarget;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewParent;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.earingmoney.R;
import com.example.asus.earingmoney.Util.Constants;
import com.example.asus.earingmoney.Util.Util;
import com.example.asus.earingmoney.adapter.MyAdapter;
import com.example.asus.earingmoney.model.QuestionModel;
import com.flyco.dialog.utils.CornerUtils;
import com.flyco.dialog.widget.base.BaseDialog;

import java.util.ArrayList;
import java.util.List;

public class SingleChooseDialog extends BaseDialog<SingleChooseDialog> {
    private EditText questionText;
    private TextView tv_yes;
    private TextView tv_exit;
    private RadioGroup choosePart;
    private TextView addChooseBtn;
    private TextView removeChooseBtn;
    private EditText tempChooseText;
    private Toast mToast;

    private List<QuestionModel> questionModelList;

    private static int chooseNum = 0;

    private int modifyPosition;
    private MyAdapter myAdapter;

    //用于将添加的radiobutton的id保存，用于removeView
    private List<Integer> idList;

    public SingleChooseDialog(Context context, List<QuestionModel> questionModelList) {
        super(context);
        this.questionModelList = questionModelList;
        this.modifyPosition= -1;
        idList = new ArrayList<>();
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

        View inflate = View.inflate(mContext, R.layout.single_choose_dialog_layout, null);
        questionText = inflate.findViewById(R.id.dialogQuestionText);
        tv_yes = inflate.findViewById( R.id.tv_yes);
        tv_exit = inflate.findViewById(R.id.tv_exit);
        addChooseBtn = inflate.findViewById(R.id.addChooseBtn);
        choosePart = inflate.findViewById(R.id.choosePart);
        tempChooseText = inflate.findViewById(R.id.tempChooseText);
        removeChooseBtn = inflate.findViewById(R.id.removeChooseBtn);
        inflate.setBackgroundDrawable(
                CornerUtils.cornerDrawable(Color.parseColor("#ffffff"), dp2px(5)));

        if(modifyPosition != -1)
        {
            QuestionModel questionModel = questionModelList.get(modifyPosition);
            questionText.setText(questionModel.getQuestion());

            List<String> choices = Util.decodeChoiceStr(questionModel.getChoiceStr());
            chooseNum = choices.size();
            for(int i = 0; i < choices.size(); i++)
            {
                RadioButton  button=new RadioButton(mContext);
                idList.add(i);
                setRadioBtnAttribute(button, choices.get(i), i);
                choosePart.addView(button);
                LinearLayout.LayoutParams layoutParams= (LinearLayout.LayoutParams) button
                        .getLayoutParams();
                layoutParams.setMargins(0, 35,  10, 0);//4个参数按顺序分别是左上右下
                button.setLayoutParams(layoutParams);
            }
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
                else if(idList.size() == 0)
                    toast("选项数目不能为0");
                else
                {
                    String question = questionText.getText().toString();
                    String choiceStr = "";
                    for(int i = 0 ; i < idList.size(); i++)
                    {
                        RadioButton button = choosePart.findViewById(idList.get(i));
                        String choice = button.getText().toString();
                        choiceStr += (choice + Constants.CHOICE_SPLIT);
                    }

                    if(modifyPosition != -1)
                    {
                        QuestionModel questionModel = questionModelList.get(modifyPosition);
                        questionModel.setQuestion(question);
                        questionModel.setChoiceStr(choiceStr);
                        myAdapter.notifyDataSetChanged();
                    }
                   else
                    {
                        QuestionModel questionModel = new QuestionModel(Constants.SINGLE_CHOOSE_QUESTION, question, choiceStr, 1);
                        questionModelList.add(questionModel);
                    }

                    chooseNum = 0;
                    dismiss();
                }

            }
        });

        tv_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseNum = 0;
                dismiss();
            }
        });

        addChooseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String chooseText = tempChooseText.getText().toString();
                if(chooseText.isEmpty())
                    toast("选项不能为空");
                else
                {
                    RadioButton  button=new RadioButton(mContext);

                    idList.add(chooseNum);

                    setRadioBtnAttribute(button, chooseText, chooseNum++);
                    choosePart.addView(button);
                    LinearLayout.LayoutParams layoutParams= (LinearLayout.LayoutParams) button
                            .getLayoutParams();
                    layoutParams.setMargins(0, 35,  10, 0);//4个参数按顺序分别是左上右下
                    button.setLayoutParams(layoutParams);
                    tempChooseText.setText("");
                }

            }
        });

        removeChooseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int index = idList.indexOf(choosePart.getCheckedRadioButtonId());
                if(index == -1)
                    toast("选中一个选项来删除");
                else
                {
                    choosePart.removeViewAt(index);
                    idList.remove(index);
                }

            }
        });
    }
    private void toast(String toast) {
        mToast.setText(toast);
        mToast.show();
    }

    @SuppressLint("ResourceType")
    private void setRadioBtnAttribute(final RadioButton codeBtn, String btnContent, int id ){
        if( null == codeBtn ){
            return;
        }
        codeBtn.setId( id );
        codeBtn.setText( btnContent );

        codeBtn.setGravity( Gravity.CENTER );

        LinearLayout.LayoutParams rlp = new LinearLayout.LayoutParams( LinearLayout.LayoutParams.WRAP_CONTENT , LinearLayout.LayoutParams.WRAP_CONTENT);
        codeBtn.setLayoutParams( rlp );
    }
}

