package com.example.asus.earingmoney.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.asus.earingmoney.R;
import com.example.asus.earingmoney.Util.Constants;
import com.example.asus.earingmoney.Util.Util;
import com.example.asus.earingmoney.createQuestionare;
import com.example.asus.earingmoney.model.QuestionModel;

import java.util.List;

public class MyAdapter extends BaseAdapter {
    private List<QuestionModel> dataList;
    private Context context;

    public  MyAdapter(List<QuestionModel> list, Context context)
    {
        this.dataList = list;
        this.context = context;
    }
    
    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return dataList.get(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CustomViewHolder cvh;
        QuestionModel item = (QuestionModel) this.getItem(position);

        if (convertView == null) {
            cvh = new CustomViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_item, null);
            cvh.txtName = (TextView) convertView.findViewById(R.id.questinoText);
            cvh.queNum = convertView.findViewById(R.id.queNum);
            cvh.singleChoosePart = convertView.findViewById(R.id.singleChoosePart);
            cvh.multiChoosePart = convertView.findViewById(R.id.multiChoosePart);
            cvh.answerText = convertView.findViewById(R.id.answerText);
            convertView.setTag(cvh);
        } else {
            cvh = (CustomViewHolder) convertView.getTag();

            //将布局初始化，防止造成复用view的过程中造成数据的混乱
            cvh.singleChoosePart.removeAllViewsInLayout();
            cvh.singleChoosePart.removeAllViews();
            cvh.multiChoosePart.removeAllViews();
            cvh.multiChoosePart.removeAllViewsInLayout();
            cvh.singleChoosePart.setVisibility(View.GONE);
            cvh.answerText.setVisibility(View.VISIBLE);
            cvh.multiChoosePart.setVisibility(View.GONE);
        }


        if(item.getQuestionType() == Constants.QUERY_QUESTION)
        {
            cvh.queNum.setText(position+ ". ");
            cvh.txtName.setText(item.getQuestion());
        }
        else if(item.getQuestionType() == Constants.SINGLE_CHOOSE_QUESTION)
        {
            cvh.queNum.setText(position+ ". ");
            cvh.txtName.setText(item.getQuestion());
            cvh.answerText.setVisibility(View.GONE);
            cvh.singleChoosePart.setVisibility(View.VISIBLE);

            if(cvh.singleChoosePart.getChildCount() == 0)
            {
                List<String> choices = Util.decodeChoiceStr(item.getChoiceStr());
                for(int i = 0; i < choices.size(); i++)
                {
                    RadioButton  button=new RadioButton(context);
                    button.setFocusable(false);
                    button.setEnabled(false);
                    setRadioBtnAttribute(button, choices.get(i), i);
                    cvh.singleChoosePart.addView(button);
                    LinearLayout.LayoutParams layoutParams= (LinearLayout.LayoutParams) button
                            .getLayoutParams();
                    layoutParams.setMargins(0, 35,  10, 0);//4个参数按顺序分别是左上右下
                    button.setLayoutParams(layoutParams);
                }
            }
        }
        else if (item.getQuestionType() == Constants.MULTI_CHOOSE_QUESTION)
        {
            cvh.queNum.setText(position+ ". ");
            cvh.txtName.setText(item.getQuestion());
            cvh.answerText.setVisibility(View.GONE);
            cvh.multiChoosePart.setVisibility(View.VISIBLE);

            if(cvh.multiChoosePart.getChildCount() == 0)
            {
                List<String> choices = Util.decodeChoiceStr(item.getChoiceStr());
                for(int i = 0; i < choices.size(); i++)
                {
                    CheckBox button=new CheckBox(context);
                    button.setFocusable(false);
                    button.setEnabled(false);
                    setCheckBtnAttribute(button, choices.get(i), i);
                    cvh.multiChoosePart.addView(button);
                    LinearLayout.LayoutParams layoutParams= (LinearLayout.LayoutParams) button
                            .getLayoutParams();
                    layoutParams.setMargins(0, 35,  10, 0);//4个参数按顺序分别是左上右下
                    button.setLayoutParams(layoutParams);
                }
            }
        }
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(300);
        convertView.startAnimation(alphaAnimation);
        return convertView;
    }

    class CustomViewHolder {
        public TextView queNum;
        public TextView txtName;
        public RadioGroup singleChoosePart;
        public LinearLayout multiChoosePart;
        public TextView answerText;
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Object o = v.getTag();
            if (o instanceof Integer) {

            }
        }
    };

    @SuppressLint("ResourceType")
    private void setRadioBtnAttribute(final RadioButton codeBtn, String btnContent, int id ){
        if( null == codeBtn ){
            return;
        }
        //codeBtn.setBackgroundResource(R.drawable.radio_group_selector);
        //codeBtn.setTextColor(context.getResources().getColorStateList(R.drawable.color_radiobutton));
        //codeBtn.setButtonDrawable(new ColorDrawable(Color.TRANSPARENT));
        codeBtn.setId( id );
        codeBtn.setText( btnContent );

        codeBtn.setGravity( Gravity.CENTER );

        LinearLayout.LayoutParams rlp = new LinearLayout.LayoutParams( LinearLayout.LayoutParams.WRAP_CONTENT , LinearLayout.LayoutParams.WRAP_CONTENT);
        codeBtn.setLayoutParams( rlp );
    }

    @SuppressLint("ResourceType")
    private void setCheckBtnAttribute(final CheckBox codeBtn, String btnContent, int id ){
        if( null == codeBtn ){
            return;
        }
        //codeBtn.setBackgroundResource(R.drawable.radio_group_selector);
        //codeBtn.setTextColor(context.getResources().getColorStateList(R.drawable.color_radiobutton));
        //codeBtn.setButtonDrawable(new ColorDrawable(Color.TRANSPARENT));
        codeBtn.setId( id );
        codeBtn.setText( btnContent );

        codeBtn.setGravity( Gravity.CENTER );

        LinearLayout.LayoutParams rlp = new LinearLayout.LayoutParams( LinearLayout.LayoutParams.WRAP_CONTENT , LinearLayout.LayoutParams.WRAP_CONTENT);
        codeBtn.setLayoutParams( rlp );
    }
}
