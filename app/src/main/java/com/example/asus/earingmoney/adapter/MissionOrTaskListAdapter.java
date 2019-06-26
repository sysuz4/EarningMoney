package com.example.asus.earingmoney.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.asus.earingmoney.R;
import com.example.asus.earingmoney.Util.Constants;
import com.example.asus.earingmoney.model.MissionModel;
import com.example.asus.earingmoney.model.MissionOrTask;
import com.example.asus.earingmoney.model.Task;
import com.example.asus.earingmoney.model.TaskModel;

import java.util.ArrayList;
import java.util.List;

public class MissionOrTaskListAdapter extends BaseAdapter {
    private List<MissionOrTask> list;
    private Context context;
    public MissionOrTaskListAdapter(List<MissionOrTask> list, Context context) {
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

    public void update() {
        notifyDataSetChanged();
    }


    public void setList(List<MissionOrTask> _list) {
        this.list.clear();
        this.list.addAll(_list);
        update();
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

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        // 新声明一个View变量和ViewHoleder变量,ViewHolder类在下面定义。
        View convertView;
        ViewHolder viewHolder;
        // 当view为空时才加载布局，否则，直接修改内容
        if (view == null) {
            // 通过inflate的方法加载布局，context需要在使用这个Adapter的Activity中传入。
            convertView= LayoutInflater.from(context).inflate(R.layout.mission_or_task_item, null);
            viewHolder = new ViewHolder();
            viewHolder.avator = (ImageView) convertView.findViewById(R.id.avator);
            viewHolder.title = (TextView) convertView.findViewById(R.id.titleText);
            viewHolder.publishTime = (TextView) convertView.findViewById(R.id.publishTimeText);
            viewHolder.deadLine = (TextView) convertView.findViewById(R.id.deadLineText);
            viewHolder.description = (TextView) convertView.findViewById(R.id.descriptionText);
            viewHolder.missionStatus = (TextView) convertView.findViewById(R.id.missionStatusText);
            viewHolder.taskNum = (TextView) convertView.findViewById(R.id.taskNumText);
            viewHolder.money = (TextView) convertView.findViewById(R.id.moneyText);
            convertView.setTag(viewHolder); // 用setTag方法将处理好的viewHolder放入view中
        } else {
            // 否则，让convertView等于view，然后从中取出ViewHolder即可
            convertView = view;
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // 从viewHolder中取出对应的对象，然后赋值给他们
        if (list.get(i).isMission()) {
            if (((MissionModel)list.get(i)).getTaskType() == Constants.TASK_QUESTIONARE) {
                viewHolder.avator.setImageResource(R.mipmap.questionare);
            } else {
                viewHolder.avator.setImageResource(R.mipmap.errand);
            }

            viewHolder.title.setText(((MissionModel)list.get(i)).getTitle());
            viewHolder.publishTime.setText("起:"+((MissionModel)list.get(i)).getPublishTime());
            viewHolder.deadLine.setText("止:"+((MissionModel)list.get(i)).getDeadLine());
            if (((MissionModel)list.get(i)).getReportNum() != 0) {
                viewHolder.missionStatus.setText(Integer.toString(((MissionModel)list.get(i)).getReportNum()) + "人举报");
                viewHolder.missionStatus.setTextColor(context.getResources().getColor(R.color.red_error));
            } else if (((MissionModel)list.get(i)).getMissionStatus() == Constants.NEED_MORE_PEOPLE) {
                viewHolder.missionStatus.setText("人数不够");
            } else if (((MissionModel)list.get(i)).getMissionStatus() == Constants.MAX_PEOPLE) {
                viewHolder.missionStatus.setText("人数达上限");
            } else if (((MissionModel)list.get(i)).getMissionStatus() == Constants.OVERDUE){
                viewHolder.missionStatus.setText("已经过期");
            } else {
                viewHolder.missionStatus.setText("全部完成");
            }
            viewHolder.taskNum.setText("需" + ((MissionModel)list.get(i)).getTaskNum() + "人");
            viewHolder.money.setText("奖励金:" + ((MissionModel)list.get(i)).getMoney());
            viewHolder.description.setText(((MissionModel)list.get(i)).getDescription());
        } else {
            if (((TaskModel)list.get(i)).getTaskType() == Constants.TASK_QUESTIONARE) {
                viewHolder.avator.setImageResource(R.mipmap.questionare);
            } else {
                viewHolder.avator.setImageResource(R.mipmap.errand);
            }
            if (((TaskModel)list.get(i)).getReportNum() != 0) {
                viewHolder.missionStatus.setText("已被举报");
                viewHolder.missionStatus.setTextColor(context.getResources().getColor(R.color.red_error));
            }
            else if (((TaskModel)list.get(i)).getTaskStatus() == Constants.TASK_TO_DO) {
                viewHolder.missionStatus.setText("待完成");
            } else if (((TaskModel)list.get(i)).getTaskStatus() == Constants.TASK_DOING) {
                viewHolder.missionStatus.setText("正在进行");
            } else if (((TaskModel)list.get(i)).getTaskStatus() == Constants.TASK_DONE_BUT_NOT_CONFIRM) {
                viewHolder.missionStatus.setText("提交等待确认");
            } else if (((TaskModel)list.get(i)).getTaskStatus() == Constants.TASK_DONE_AND_CONFIRM) {
                viewHolder.missionStatus.setText("已完成");
            }
            viewHolder.title.setText(((TaskModel)list.get(i)).getTitle());
            viewHolder.publishTime.setText("起:" + ((TaskModel)list.get(i)).getPublishTime());
            viewHolder.deadLine.setText("止:" + ((TaskModel)list.get(i)).getDeadLine());
            if (((TaskModel)list.get(i)).getFinishTime() != null) {
                viewHolder.taskNum.setText("完成于" + ((TaskModel)list.get(i)).getFinishTime());
            } else {
                viewHolder.taskNum.setText("");
            }
            viewHolder.money.setText("奖励金" + Double.toString(((TaskModel)list.get(i)).getAveMoney()));
            viewHolder.description.setText(((TaskModel)list.get(i)).getDescription());
        }


        // 将这个处理好的view返回
        return convertView;
    }

    private class ViewHolder {
        public ImageView avator;
        public TextView title;
        public TextView publishTime;
        public TextView deadLine;
        public TextView description;
        public TextView missionStatus;
        public TextView taskNum;
        public TextView money;
    }
}
