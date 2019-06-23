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
import com.example.asus.earingmoney.model.MissionOrTask;
import com.example.asus.earingmoney.model.QuestionModel;

import java.util.ArrayList;

public class DisplayAnswerAdapter extends BaseAdapter{
    private ArrayList<String> list;
    private Context context;

    public DisplayAnswerAdapter(ArrayList<String> list, Context context) {
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

    public void update() {
        notifyDataSetChanged();
    }

    public void setList(ArrayList<String> answers) {
        this.list.clear();
        this.list.addAll(answers);
        update();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        // 新声明一个View变量和ViewHoleder变量,ViewHolder类在下面定义。
        View convertView;
        DisplayAnswerAdapter.ViewHolder viewHolder;
        // 当view为空时才加载布局，否则，直接修改内容
        if (view == null) {
            // 通过inflate的方法加载布局，context需要在使用这个Adapter的Activity中传入。
            convertView= LayoutInflater.from(context).inflate(R.layout.display_answer_item, null);
            viewHolder = new DisplayAnswerAdapter.ViewHolder();
            viewHolder.answerId = (TextView) convertView.findViewById(R.id.answerIdText);
            viewHolder.answer = (TextView) convertView.findViewById(R.id.answerText);
            convertView.setTag(viewHolder); // 用setTag方法将处理好的viewHolder放入view中
        } else {
            // 否则，让convertView等于view，然后从中取出ViewHolder即可
            convertView = view;
            viewHolder = (DisplayAnswerAdapter.ViewHolder) convertView.getTag();
        }

        viewHolder.answerId.setText(Integer.toString(i));
        viewHolder.answer.setText(list.get(i));

        // 将这个处理好的view返回
        return convertView;
    }

    private class ViewHolder {
        public TextView answerId;
        public TextView answer;
    }
}
