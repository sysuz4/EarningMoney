package com.example.asus.earingmoney;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.clj.memoryspinner.MemorySpinner;
import com.example.asus.earingmoney.Util.Constants;
import com.example.asus.earingmoney.adapter.DisplayAnswerAdapter;
import com.example.asus.earingmoney.adapter.DisplayQuestionareAdapter;
import com.example.asus.earingmoney.adapter.MissionOrTaskListAdapter;
import com.example.asus.earingmoney.model.MissionModel;
import com.example.asus.earingmoney.model.MissionOrTask;
import com.example.asus.earingmoney.model.QAsummary;
import com.example.asus.earingmoney.model.Question;
import com.example.asus.earingmoney.model.QuestionModel;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class TasksFragment extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener{
    private static final String ARG_SHOW_TEXT = "text";

    private String mContentText;
    private ListView missionOrTaskList;
    private MissionOrTaskListAdapter missionOrTaskListAdapter;
    private DisplayQuestionareAdapter questionareAdapter;
    private DisplayAnswerAdapter answerAdapter;

    private ArrayList<MissionOrTask> missions;
    //private ArrayList<MissionOrTask> tasks;

    private ArrayList<QuestionModel> questions;
    private ArrayList<String> answers;

    //private QAsummary qAsummary;
    private ConstraintLayout missionPage;
    private ConstraintLayout questionarePage;
    private ConstraintLayout answerPage;
    private ArrayList<MissionOrTask> mList;

    private MemorySpinner missionOrTaskMS;
    private MemorySpinner completenessMS;

    private ImageView revertBtn;
    private ProgressBar completeness;
    private ListView displayQuestionareList;

    private TextView QATitle;
    private TextView QADes;
    private ProgressBar QABar;
    private TextView QAPercent;

    String token = "";
    int userId = -1;
    SharedPreferences user_shared_preference;

    public TasksFragment() {
        // Required empty public constructor
    }

    public static TasksFragment newInstance(String param1) {
        TasksFragment fragment = new TasksFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SHOW_TEXT, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mContentText = getArguments().getString(ARG_SHOW_TEXT);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.tasks_fragment, container, false);
        setHasOptionsMenu(true);
        //TextView contentTv = rootView.findViewById(R.id.content_tv);
        //contentTv.setText(mContentText);
        initView(rootView);
        initData();


        return rootView;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initView(View rootView) {
        missionPage = rootView.findViewById(R.id.missionPage);
        questionarePage = rootView.findViewById(R.id.questionarePage);
        answerPage = rootView.findViewById(R.id.answerPage);

        initMissionPage(rootView);
        initQuestionPage(rootView);
        initAnswerPage(rootView);
        
        displayMissionPage();

        //Toast.makeText(getContext(), getToken(), Toast.LENGTH_SHORT).show();
    }

    private void initAnswerPage(View rootView) {
    }

    private void initQuestionPage(View rootView) {
        revertBtn = rootView.findViewById(R.id.revertBtn);
        completeness = rootView.findViewById(R.id.completeness);
        displayQuestionareList = rootView.findViewById(R.id.displayQuestionareList);

        QATitle = rootView.findViewById(R.id.titleText);
        QADes = rootView.findViewById(R.id.descriptionText);
        QABar = rootView.findViewById(R.id.completeness);
        QAPercent = rootView.findViewById(R.id.percent);

        revertBtn.setOnClickListener(this);
        questions = new ArrayList<QuestionModel>();
        //questions.add(new QuestionModel(0, Constants.SINGLE_CHOOSE_QUESTION, "单选题题干。", "", 4, "ABCAA",  2));
        //questions.add(new QuestionModel(0, Constants.MULTI_CHOOSE_QUESTION, "多选题题干。", "", 4, "ABCAA",  2));
        //questions.add(new QuestionModel(0, Constants.QUERY_QUESTION, "问答题题干。", "", 4, "ABCAA",  2));
        questionareAdapter = new DisplayQuestionareAdapter(questions, getContext());
        displayQuestionareList.setAdapter(questionareAdapter);



    }

    private void initMissionPage(View rootView) {
        missionOrTaskMS = rootView.findViewById(R.id.missionOrTaskMS);
        ArrayList<String> list1 = new ArrayList<>(Arrays.asList("我发布的", "我接受的"));
        missionOrTaskMS.setMemoryCount(1);
        missionOrTaskMS.setData(null, list1);

        missionOrTaskMS.setSelection(1);

        completenessMS = rootView.findViewById(R.id.completenessMS);
        ArrayList<String> list2 = new ArrayList<>(Arrays.asList("全部","已完成", "未完成"));
        completenessMS.setMemoryCount(1);
        completenessMS.setData(null, list2);

        completenessMS.setSelection(1);

        missionOrTaskList = rootView.findViewById(R.id.MissionOrTaskList);

        missionOrTaskList.setOnItemClickListener(this);
    }

    private void displayMissionPage() {
        missionPage.setVisibility(View.VISIBLE);
        questionarePage.setVisibility(View.GONE);
        answerPage.setVisibility(View.GONE);
    }

    private void displayQuestionarePage(int missionId) {
        missionPage.setVisibility(View.GONE);
        questionarePage.setVisibility(View.VISIBLE);
        answerPage.setVisibility(View.GONE);
        initQAsummary(missionId);
    }

    private void initQAsummary(int missionId) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        QAsummaryService qAsummaryService = retrofit.create(QAsummaryService.class);
        Observable<QAsummary> repoObservable = qAsummaryService.getQAsummary(getToken(), missionId);

        repoObservable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<QAsummary>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getContext(), "获取问卷填写结果错误!", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(QAsummary qAsummary) {
                        QATitle.setText(qAsummary.getQATitle());
                        QADes.setText(qAsummary.getQADes());
                        QABar.setMax(qAsummary.getTaskNum());
                        QABar.setProgress(qAsummary.getFinishNum());
                        double percent = (double)qAsummary.getFinishNum() / qAsummary.getTaskNum();
                        percent *= 100;
                        QAPercent.setText(Double.toString(percent) + "%");

                        questions.clear();
                        questions.addAll(qAsummary.getQuestions());
                        questionareAdapter.update();
                    }
                });
    }

    private void displayAnswerPage(int index) {
        missionPage.setVisibility(View.GONE);
        questionarePage.setVisibility(View.GONE);
        answerPage.setVisibility(View.VISIBLE);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initData() {
        missions = new ArrayList<>();

        missionOrTaskListAdapter = new MissionOrTaskListAdapter(missions, getContext());
        missionOrTaskList.setAdapter(missionOrTaskListAdapter);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        UserService userService = retrofit.create(UserService.class);
        Observable<ArrayList<MissionModel>> repoObservable = userService.getMissionsByUserId(getToken(),getUserId());
        Log.e("userId", Integer.toString(getUserId()));
        Log.e("token", getToken());
        repoObservable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ArrayList<MissionModel>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getContext(), "获取我的任务错误!", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(ArrayList<MissionModel> missionModels) {
                        //Toast.makeText(getContext(), Integer.toString(missionModels.size()), Toast.LENGTH_SHORT).show();
                        missions.clear();
                        missions.addAll(missionModels);
                        missionOrTaskListAdapter.update();
                    }
                });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.tasks_menu, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (missions.get(i).isMission() && ((MissionModel)missions.get(i)).getTaskType() == Constants.TASK_QUESTIONARE) {
            displayQuestionarePage(((MissionModel)missions.get(i)).getMissionId());
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.revertBtn) {
            displayMissionPage();
        }

    }

    private String getToken() {
        if (token.isEmpty()) {
            user_shared_preference = getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
            token = user_shared_preference.getString("token", "");

        }
        return token;
    }

    private int getUserId() {
        if (userId == -1) {
            user_shared_preference = getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
            userId = user_shared_preference.getInt("userId", -1);
        }

        return userId;
    }
}
