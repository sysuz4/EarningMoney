package com.example.asus.earingmoney.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.earingmoney.R;
import com.example.asus.earingmoney.Util.Constants;
import com.example.asus.earingmoney.model.Question;
import com.example.asus.earingmoney.model.QuestionModel;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FillInQuestionareAdapter  extends BaseAdapter
        implements RadioGroup.OnCheckedChangeListener, CompoundButton.OnCheckedChangeListener {

    private List<Question> list;
    private Context context;
    private static String ch = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public FillInQuestionareAdapter(List<Question> list, Context context) {
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

    public void setList(ArrayList<Question> questions) {
        this.list.clear();
        this.list.addAll(questions);
        update();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        // 新声明一个View变量和ViewHoleder变量,ViewHolder类在下面定义。
        View convertView;
        FillInQuestionareAdapter.ViewHolder viewHolder;
        // 当view为空时才加载布局，否则，直接修改内容
        if (view == null) {
            // 通过inflate的方法加载布局，context需要在使用这个Adapter的Activity中传入。
            convertView= LayoutInflater.from(context).inflate(R.layout.fill_in_questionare_item, null);
            viewHolder = new FillInQuestionareAdapter.ViewHolder();
            viewHolder.questionDes = (LinearLayout) convertView.findViewById(R.id.questionDes);
            viewHolder.single = (RadioGroup) convertView.findViewById(R.id.single);
            viewHolder.multiple = (LinearLayout) convertView.findViewById(R.id.multiple);
            viewHolder.editText = (EditText) convertView.findViewById(R.id.editText);
            convertView.setTag(viewHolder); // 用setTag方法将处理好的viewHolder放入view中
        } else {
            // 否则，让convertView等于view，然后从中取出ViewHolder即可
            convertView = view;
            viewHolder = (FillInQuestionareAdapter.ViewHolder) convertView.getTag();
        }

        TextView questionNum = viewHolder.questionDes.findViewById(R.id.questionNumText);
        TextView questionType = viewHolder.questionDes.findViewById(R.id.questionTypeText);
        TextView question = viewHolder.questionDes.findViewById(R.id.questionText);

        questionNum.setText(Integer.toString(i + 1) + ".");
        question.setText(list.get(i).getQuestion());
        if (list.get(i).getQuestionType() == Constants.QUERY_QUESTION) {
            questionType.setText("[问答]");
            viewHolder.single.setVisibility(View.GONE);
            viewHolder.multiple.setVisibility(View.GONE);
            fillItem(0, i, viewHolder.editText);
        } else if (list.get(i).getQuestionType() == Constants.SINGLE_CHOOSE_QUESTION){
            questionType.setText("[单选]");
            viewHolder.editText.setVisibility(View.GONE);
            viewHolder.multiple.setVisibility(View.GONE);
            fillItem(1, i, viewHolder.single);
            viewHolder.single.setOnCheckedChangeListener(this);
        } else {
            int num = list.get(i).getChoiceNum();
            if (num != 0) {
                questionType.setText("[" + Integer.toString(num) +"选]");
            } else {
                questionType.setText("[不定项]");
            }
            viewHolder.single.setVisibility(View.GONE);
            viewHolder.editText.setVisibility(View.GONE);
            fillItem(2, i, viewHolder.multiple);
        }

        // 将这个处理好的view返回
        return convertView;
    }

    private void fillItem(int type, int i, View rootview) {
        if (type == 0) {
            ((EditText)rootview).setText(list.get(i).getAnswer());
            ((EditText)rootview).setId(i*100);
        } else if (type == 1) {
            List<String> options = Arrays.asList(list.get(i).getChoiceStr().split("\\|"));
            String option = list.get(i).getAnswer();
            for (int j = 0; j < options.size(); ++j) {
                RadioButton button = new RadioButton(context);
                setRadioBtnAttribute(button, ch.substring(j, j+1) + "." + options.get(j), i*100 + j);
                ((RadioGroup)rootview).addView(button);
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) button
                        .getLayoutParams();
                layoutParams.setMargins(30, 5, 0, 5);//4个参数按顺序分别是左上右下
                button.setLayoutParams(layoutParams);
            }

            if (option != null) {
                int index = ch.indexOf(option);
                ((RadioGroup)rootview).check(i*100 + index);
            }
        } else {
            List<String> options = Arrays.asList(list.get(i).getChoiceStr().split("\\|"));
            String option = list.get(i).getAnswer();
            for (int j = 0; j < options.size(); ++j) {
                CheckBox box = new CheckBox(context);
                setCheckBoxAttribute(box, ch.substring(j, j+1) + "." + options.get(j), i*100 + j);
                ((LinearLayout)rootview).addView(box);
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) box
                        .getLayoutParams();
                layoutParams.setMargins(30, 5, 0, 5);//4个参数按顺序分别是左上右下
                box.setLayoutParams(layoutParams);

                box.setOnCheckedChangeListener(this);
            }
            if (option != null) {
                List<String> optionList = Arrays.asList(option.split(""));
                for (int j = 0; j < optionList.size(); ++j) {
                    int index = ch.indexOf(optionList.get(j));
                    //int index = j;
                    CheckBox box = rootview.findViewById(i*100 + index);
                    box.setChecked(true);
                }
            }
        }
    }

    @SuppressLint("ResourceType")
    private void setRadioBtnAttribute(final RadioButton codeBtn, String btnContent, int id){
        if( null == codeBtn ){
            return;
        }
        codeBtn.setId( id );
        codeBtn.setText( btnContent );
        codeBtn.setGravity( Gravity.LEFT );
        codeBtn.setGravity( Gravity.CENTER_VERTICAL );
        LinearLayout.LayoutParams rlp = new LinearLayout.LayoutParams( LinearLayout.LayoutParams.MATCH_PARENT , LinearLayout.LayoutParams.WRAP_CONTENT);
        codeBtn.setLayoutParams( rlp );
    }

    @SuppressLint("ResourceType")
    private void setCheckBoxAttribute(final CheckBox box, String btnContent, int id){
        if( null == box ){
            return;
        }
        box.setId( id );
        box.setText( btnContent );
        box.setGravity( Gravity.LEFT );
        box.setGravity( Gravity.CENTER_VERTICAL );
        LinearLayout.LayoutParams rlp = new LinearLayout.LayoutParams( LinearLayout.LayoutParams.MATCH_PARENT , LinearLayout.LayoutParams.WRAP_CONTENT);
        box.setLayoutParams( rlp );
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        int questionIndex = checkedId / 100;
        int option = checkedId % 100;
        /*
        Log.e("index1:", Integer.toString(questionIndex));
        Log.e("index2:", Integer.toString(option));
        Log.e("index3:", ch.substring(option, option + 1));
        */
        list.get(questionIndex).setAnswer(ch.substring(option, option + 1));
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView,
                                 boolean isChecked) {
        //Toast.makeText(context, Integer.toString(buttonView.getId()), Toast.LENGTH_SHORT).show();
        int checkedId = buttonView.getId();
        int questionIndex = checkedId / 100;
        int option = checkedId % 100;
        String optionStr = ch.substring(option, option + 1);
        if (isChecked) {
            if (list.get(questionIndex).getAnswer() == null) {
                list.get(questionIndex).setAnswer(optionStr);
            } else {
                list.get(questionIndex).setAnswer(list.get(questionIndex).getAnswer() + optionStr);
            }
        } else {
            String originOption = list.get(questionIndex).getAnswer();
            String newOption = deleteSubString(originOption, optionStr);
            list.get(questionIndex).setAnswer(newOption);
        }
    }

    private String deleteSubString(String str1, String str2) {
        int index = str1.indexOf(str2);
        return str1.substring(0, index) + str1.substring(index + 1);
    }

    private class ViewHolder {
        public LinearLayout questionDes;
        public RadioGroup single;
        public LinearLayout multiple;
        public EditText editText;
    }
}
