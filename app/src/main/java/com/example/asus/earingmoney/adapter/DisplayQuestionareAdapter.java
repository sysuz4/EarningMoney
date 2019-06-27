package com.example.asus.earingmoney.adapter;

import android.content.Context;
import android.util.Log;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class DisplayQuestionareAdapter extends BaseAdapter {
    private List<QuestionModel> list;
    private Context context;
    private int finishNum = 1;

    private static String ch = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private String[] chineseDigit = {"零","一", "双", "三", "四", "五", "六"};
    public DisplayQuestionareAdapter(List<QuestionModel> list, Context context) {
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

    public void setFinishNum(int finishNum) {
        this.finishNum = finishNum;
    }

    public void setList(ArrayList<QuestionModel> questions) {
        this.list.clear();
        this.list.addAll(questions);
        update();
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
            viewHolder.optionList = (TextView) convertView.findViewById(R.id.optionListText);
            viewHolder.jumpBtn = (TextView) convertView.findViewById(R.id.jumpBtn);
            convertView.setTag(viewHolder); // 用setTag方法将处理好的viewHolder放入view中
        } else {
            // 否则，让convertView等于view，然后从中取出ViewHolder即可
            convertView = view;
            viewHolder = (DisplayQuestionareAdapter.ViewHolder) convertView.getTag();
        }

        TextView questionNum = viewHolder.questionDes.findViewById(R.id.questionNumText);
        TextView questionType = viewHolder.questionDes.findViewById(R.id.questionTypeText);
        TextView question = viewHolder.questionDes.findViewById(R.id.questionText);

        questionNum.setText(Integer.toString(i + 1) + ".");
        Log.e("question == null:", list.get(i).getQuestion() == null ? "null" : "not null");
        question.setText(list.get(i).getQuestion());
        if (list.get(i).getQuestionType() == Constants.QUERY_QUESTION) {
            questionType.setText("[问答]");
            viewHolder.optionList.setVisibility(View.GONE);
            viewHolder.jumpBtn.setVisibility(View.VISIBLE);
        } else if (list.get(i).getQuestionType() == Constants.SINGLE_CHOOSE_QUESTION){
            questionType.setText("[单选]");
            viewHolder.jumpBtn.setVisibility(View.GONE);
            viewHolder.optionList.setVisibility(View.VISIBLE);
            viewHolder.optionList.setText(getOptionListText(i));
        } else {
            int num = list.get(i).getChoiceNum();
            if (num != 0) {
                questionType.setText("[" + chineseDigit[num] +"选]");
            } else {
                questionType.setText("[不定项]");
            }
            viewHolder.jumpBtn.setVisibility(View.GONE);
            viewHolder.optionList.setVisibility(View.VISIBLE);
            viewHolder.optionList.setText(getOptionListText(i));
        }

        // 将这个处理好的view返回
        return convertView;
    }

    private String getOptionListText(int i) {
        /*
        StringBuilder sb= new StringBuilder();
        String options = list.get(i).getAnswer();
        Map<String, Integer> map = new HashMap<>();
        for (int j = 0; j < options.length(); ++j) {
            String option = options.substring(j, j+1);
            if (map.get(option) == null) {
                map.put(option, 1);
            } else {
                map.put(option, map.get(option) + 1);
            }
        }

        Iterator iter= map.entrySet().iterator();
        while(iter.hasNext()){
            Map.Entry<String,Integer> entry=(Map.Entry<String,Integer>)iter.next();
            sb.append(entry.getKey());
            sb.append(": ");
            if (finishNum != 0) {
                sb.append(Double.toString((double) entry.getValue() / finishNum * 100));
                sb.append("%\n");
            } else {
                sb.append("无人作答\n");
            }

        }
        return sb.toString();
        */
        StringBuilder sb= new StringBuilder();
        String options = list.get(i).getAnswer();

        /*
        if (options == null) {
            return "无人作答";
        }
        */
        //Log.e("op", options);


        List<String> optionStr = Arrays.asList(list.get(i).getChoiceStr().split("\\|"));
        Map<String, Integer> map = new HashMap<>();
        for (int j = 0; j < optionStr.size(); ++j) {
            map.put(ch.substring(j, j+1), 0);
        }
        if (options != null){
            for (int j = 0; j < options.length(); ++j) {
                String option = options.substring(j, j + 1);
                if (map.get(option) != null) {
                    map.put(option, map.get(option) + 1);
                }
            }
        }
        Log.e("size:", Integer.toString(optionStr.size()));
        for (int j = 0; j < optionStr.size(); ++j) {
            sb.append(ch.substring(j, j+1));
            sb.append(": ");
            sb.append(optionStr.get(j));
            if (finishNum == 0 || options == null) {
                sb.append(" 无人作答\n");
            } else {
                sb.append(" ");
                String percent = String.format("%.2f",(double) map.get(ch.substring(j, j+1)) / finishNum * 100);
                sb.append("   ");
                sb.append(percent);
                sb.append("%\n");


            }
        }
        return sb.toString();


    }

    private class ViewHolder {
        public LinearLayout questionDes;
        public TextView optionList;
        public TextView jumpBtn;
    }
}
