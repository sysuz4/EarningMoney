package com.example.asus.earingmoney.lib;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
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
import com.example.asus.earingmoney.model.Question;
import com.flyco.dialog.utils.CornerUtils;
import com.flyco.dialog.widget.base.BaseDialog;

import java.util.ArrayList;
import java.util.List;

public class MultiChooseDialog extends BaseDialog<MultiChooseDialog> {
    private EditText questionText;
    private TextView tv_yes;
    private TextView tv_exit;
    private RadioGroup choosePart;
    private TextView addChooseBtn;
    private TextView removeChooseBtn;
    private EditText tempChooseText;
    private Toast mToast;
    private EditText numberPicker;
    private List<Question> questionModelList;

    private static int chooseNum = 0;
    private int modifyPosition;
    private MyAdapter myAdapter;

    //用于将添加的radiobutton的id保存，用于removeView
    private List<Integer> idList;

    public MultiChooseDialog(Context context, List<Question> questionModelList) {
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

        View inflate = View.inflate(mContext, R.layout.multi_choose_dialog_layout, null);
        questionText = inflate.findViewById(R.id.dialogQuestionText);
        tv_yes = inflate.findViewById( R.id.tv_yes);
        tv_exit = inflate.findViewById(R.id.tv_exit);
        addChooseBtn = inflate.findViewById(R.id.addChooseBtn);
        choosePart = inflate.findViewById(R.id.choosePart);
        tempChooseText = inflate.findViewById(R.id.tempChooseText);
        numberPicker = inflate.findViewById(R.id.numPicker);
        removeChooseBtn = inflate.findViewById(R.id.removeChooseBtn);
        inflate.setBackgroundDrawable(
                CornerUtils.cornerDrawable(Color.parseColor("#ffffff"), dp2px(5)));

        if(modifyPosition != -1)
        {
            Question questionModel = questionModelList.get(modifyPosition);
            questionText.setText(questionModel.getQuestion());
            numberPicker.setText(String.valueOf(questionModel.getChoiceNum()));

            List<String> choices = Util.decodeChoiceStr(questionModel.getChoiceStr());
            chooseNum = choices.size();
            for(int i = 0; i < choices.size(); i++)
            {
                RadioButton  button=new RadioButton(mContext);
                setRadioBtnAttribute(button, choices.get(i), i);
                idList.add(i);
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
                else if(chooseNum == 0)
                    toast("选项数目不能为0");
                else if(numberPicker.getText().toString().isEmpty())
                    toast("请填写可选数目");
                else if(Integer.valueOf(numberPicker.getText().toString()) > chooseNum)
                    toast("可选数目不能大于选项数目");
                else if(Integer.valueOf(numberPicker.getText().toString()) < 0)
                    toast("可选数目不能小于0");
                else if(Integer.valueOf(numberPicker.getText().toString()) == 1)
                    toast("多选题选择数目不能为1");
                else
                {
                    String question = questionText.getText().toString();
                    String choiceStr = "";
                    int choiceNum = Integer.parseInt(numberPicker.getText().toString());
                    for(int i = 0 ; i < idList.size(); i++)
                    {
                        RadioButton button = choosePart.findViewById(idList.get(i));
                        String choice = button.getText().toString();
                        choiceStr += (choice + Constants.CHOICE_SPLIT);
                    }

                    if(modifyPosition != -1)
                    {
                        Question questionModel = questionModelList.get(modifyPosition);
                        questionModel.setQuestion(question);
                        questionModel.setChoiceStr(choiceStr);
                        questionModel.setChoiceNum(choiceNum);
                        myAdapter.notifyDataSetChanged();
                    }
                    else
                    {
                        Question questionModel = new Question(Constants.MULTI_CHOOSE_QUESTION, question, choiceStr, choiceNum);
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
                dismiss();
            }
        });

        addChooseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String chooseText = tempChooseText.getText().toString();
                if(chooseText.isEmpty())
                    toast("选项不能为空");
                else if(chooseNum >= 6)
                    toast("选项不能超过6个");
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
