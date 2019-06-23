package com.example.asus.earingmoney;

import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.earingmoney.Util.Constants;
import com.example.asus.earingmoney.Util.Util;
import com.example.asus.earingmoney.adapter.FillInQuestionareAdapter;
import com.example.asus.earingmoney.model.MissionModel;
import com.example.asus.earingmoney.model.Msg;
import com.example.asus.earingmoney.model.QAsummary;
import com.example.asus.earingmoney.model.Question;
import com.example.asus.earingmoney.model.Questionare;
import com.example.asus.earingmoney.model.QuestionareCommitModel;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okio.BufferedSource;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class fill_in_questionare_page extends AppCompatActivity implements View.OnClickListener{
    private Questionare data;
    private FillInQuestionareAdapter adapter;
    private int taskId;

    private TextView title;
    private TextView description;
    private ListView questions;
    private Button commitBtn;
    private Button cancelBtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_in_questionare_page);
        initView();
        initData();
    }

    private void initData() {
        taskId = getIntent().getExtras().getInt("taskId");


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        service ser = retrofit.create(service.class);
        Observable<Questionare> questionareObservable = ser.getQuestionareByTaskId(Util.getToken(getApplicationContext()), taskId);
        questionareObservable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Questionare>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        //Toast.makeText(getContext(), "获取问卷错误!", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Questionare questionare) {
                        data = questionare;
                        title.setText(data.getTitle());
                        description.setText(data.getDescription());

                        adapter = new FillInQuestionareAdapter(data.getQuestions(), getApplicationContext());
                        questions.setAdapter(adapter);
                    }
                });
    }

    private void initView() {
        title = findViewById(R.id.titleText);
        description = findViewById(R.id.descriptionText);
        questions = findViewById(R.id.questions);
        commitBtn = findViewById(R.id.commitBtn);
        cancelBtn = findViewById(R.id.cancelBtn);
        commitBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.commitBtn) {
            int wrongIndex = checkQuestionare();
            if (wrongIndex != -1) {
                presentDialog(wrongIndex);
            } else {
                commitQuestionare();
            }
        } else if (v.getId() == R.id.cancelBtn) {
            AlertDialog.Builder bulider =new AlertDialog.Builder(fill_in_questionare_page.this);
            String message = "";
            bulider.setTitle("确定取消填写？").setMessage(message)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            finish();
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
            }).create().show();
        }
    }

    private void presentDialog(int wrongIndex) {
        //Toast.makeText(getApplication(), Integer.toString(wrongIndex), Toast.LENGTH_SHORT).show();
        AlertDialog.Builder bulider =new AlertDialog.Builder(fill_in_questionare_page.this);
        String message = "";
        if (data.getQuestions().get(wrongIndex).getQuestionType() == Constants.SINGLE_CHOOSE_QUESTION) {
            message += "请选择第" + Integer.toString(wrongIndex + 1) + "题。";
        } else if (data.getQuestions().get(wrongIndex).getQuestionType() == Constants.MULTI_CHOOSE_QUESTION) {
            int num = data.getQuestions().get(wrongIndex).getChoiceNum();
            String answer = data.getQuestions().get(wrongIndex).getAnswer();
            if (answer == null || answer.isEmpty()) {
                message += "请选择第" + Integer.toString(wrongIndex + 1) + "题。";
            } else {
                message += "第" + Integer.toString(wrongIndex + 1) + "题为" + Integer.toString(num) + "选题。";
            }

        } else {
            message += "请填写第" + Integer.toString(wrongIndex + 1) + "题。";
        }
        bulider.setTitle("请继续填写问卷").setMessage(message)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
    }

    private void commitQuestionare() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        ArrayList<QuestionareCommitModel.QuestionCommitModel> questions = new ArrayList<>();
        for (Question q : data.getQuestions()) {
            questions.add(new QuestionareCommitModel.QuestionCommitModel(q.getQuestionId(), q.getAnswer()));
        }
        QuestionareCommitModel model = new QuestionareCommitModel();
        model.setQuestions(questions);
        Gson gson=new Gson();
        final String jsonBody = gson.toJson(model);
        Log.e("hhh", jsonBody);
        RequestBody reqBody = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=utf-8"),jsonBody);

        service ser = retrofit.create(service.class);
        Observable<Msg> msgObservable = ser.commitQuestionare(Util.getToken(getApplication()),taskId, reqBody);
        msgObservable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Msg>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getApplication(), "此问卷已经填写过!", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Msg msg) {
                        Toast.makeText(getApplication(), "提交问卷成功", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }

    private int checkQuestionare() {
        int ret = -1;
        for (int i = 0; i < data.getQuestions().size(); ++i) {
            if (data.getQuestions().get(i).getQuestionType() == Constants.SINGLE_CHOOSE_QUESTION) {
                String answer = data.getQuestions().get(i).getAnswer();
                if (answer == null || answer.isEmpty()) {
                    return i;
                }
            } else if (data.getQuestions().get(i).getQuestionType() == Constants.MULTI_CHOOSE_QUESTION) {
                String answer = data.getQuestions().get(i).getAnswer();
                if (answer == null || answer.isEmpty()) {
                    return i;
                }else {
                    Log.e("num:", Integer.toString(data.getQuestions().get(i).getChoiceNum()));
                    if (data.getQuestions().get(i).getChoiceNum() != 0 && answer.length() != data.getQuestions().get(i).getChoiceNum()) {
                        return i;
                    }
                }
            } else {
                String answer = ((EditText)findViewById(i*100)).getText().toString();
                if (answer == null || answer.isEmpty()) {
                    return i;
                } else {
                    data.getQuestions().get(i).setAnswer(answer);
                }
            }
        }

        return ret;
    }
}
