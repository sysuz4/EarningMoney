package com.example.asus.earingmoney.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.asus.earingmoney.R;
import com.example.asus.earingmoney.Util.Constants;
import com.example.asus.earingmoney.model.QuestionModel;

import java.util.ArrayList;

public class DisplayQuestionareAdapter extends BaseAdapter {
    private ArrayList<QuestionModel> list;
    private Context context;

    public DisplayQuestionareAdapter(ArrayList<QuestionModel> list, Context context) {
        this.list = list;
        this.context = context;
    }
    @Override
    public int getCount() {
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public Object getItem(int i) {
        if (list == null) {
            return null;
        }
        return list.get(i);
    }

    public void setList(ArrayList<QuestionModel> questions) {
        this.list.clear();
        this.list.addAll(questions);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        // 新声明一个View变量和ViewHoleder变量,ViewHolder类在下面定义。
        View convertView;
        DisplayQuestionareAdapter.ViewHolder viewHolder;
        // 当view为空时才加载布局，否则，直接修改内容
        if (view == null) {
            // 通过inflate的方法加载布局，context需要在使用这个Adapter的Activity中传入。
            convertView= LayoutInflater.from(context).inflate(R.layout.display_question_item, null);
            viewHolder = new DisplayQuestionareAdapter.ViewHolder();
            viewHolder.questionDes = (LinearLayout) convertView.findViewById(R.id.questionDes);
            viewHolder.optionListText = (TextView) convertView.findViewById(R.id.optionListText);
            viewHolder.jumpBtn = (TextView) convertView.findViewById(R.id.jumpBtn);
            convertView.setTag(viewHolder); // 用setTag方法将处理好的viewHolder放入view中
        } else {
            // 否则，让convertView等于view，然后从中取出ViewHolder即可
            convertView = view;
            viewHolder = (DisplayQuestionareAdapter.ViewHolder) convertView.getTag();
        }

        TextView questionNum = viewHolder.questionDes.findViewById(R.id.questionNumText);
        TextView questionType = viewHolder.questionDes.findViewById(R.id.questionTypeText);
        TextView question = viewHolder.questionDes.findViewById(R.id.questinoText);
        questionNum.setText(Integer.toString(i));
        question.setText(list.get(i).getQuestion());
        if (list.get(i).getQuestionType() == Constants.QUERY_QUESTION) {
            questionType.setText("[问答]");
            viewHolder.optionListText.setVisibility(View.GONE);
        } else if (list.get(i).getQuestionType() == Constants.SINGLE_CHOOSE_QUESTION){
            questionType.setText("[单选]");
            viewHolder.jumpBtn.setVisibility(View.GONE);
        } else {
            questionType.setText("[多选]");
            viewHolder.jumpBtn.setVisibility(View.GONE);
        }

        // 将这个处理好的view返回
        return convertView;
    }

    private class ViewHolder {
        public LinearLayout questionDes;
        public TextView optionListText;
        public TextView jumpBtn;
    }
}
