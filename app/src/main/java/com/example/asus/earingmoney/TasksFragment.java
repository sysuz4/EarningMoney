package com.example.asus.earingmoney;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.example.asus.earingmoney.model.TaskModel;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.support.constraint.Constraints.TAG;

public class TasksFragment extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener{
    private static final String ARG_SHOW_TEXT = "text";

    private SwipeRefreshLayout swipeRefreshLayout1,swipeRefreshLayout2;

    private String mContentText;
    private ListView missionOrTaskList;
    private MissionOrTaskListAdapter missionOrTaskListAdapter;
    private DisplayQuestionareAdapter questionareAdapter;
    private DisplayAnswerAdapter answerAdapter;

    private ArrayList<MissionOrTask> missions;
    private ArrayList<MissionOrTask> tasks;
    private ArrayList<MissionOrTask> list;

    private ArrayList<QuestionModel> questions;
    private ArrayList<String> answers;

    //private QAsummary qAsummary;
    private ConstraintLayout missionPage;
    private ConstraintLayout questionarePage;
    private ConstraintLayout answerPage;
    private ArrayList<MissionOrTask> mList;

    private Spinner missionOrTaskSpinner;
    private Spinner completenessSpinner;

    private ImageView revertBtn;
    private ProgressBar completeness;
    private ListView displayQuestionareList;

    private ImageView answerRevertBtn;
    private ListView answerList;

    private TextView QATitle;
    private TextView QADes;
    private ProgressBar QABar;
    private TextView QAPercent;

    private int lastMissionId;

    private boolean displayMission = true;
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
        final View rootView = inflater.inflate(R.layout.tasks_fragment, container, false);
        setHasOptionsMenu(true);
        //TextView contentTv = rootView.findViewById(R.id.content_tv);
        //contentTv.setText(mContentText);

        swipeRefreshLayout1 = rootView.findViewById(R.id.swipeLayout1);
        swipeRefreshLayout1.setSize(SwipeRefreshLayout.DEFAULT);
        swipeRefreshLayout1.setProgressViewEndTarget(true, 200);

        swipeRefreshLayout2 = rootView.findViewById(R.id.swipeLayout1);
        swipeRefreshLayout2.setSize(SwipeRefreshLayout.DEFAULT);
        swipeRefreshLayout2.setProgressViewEndTarget(true, 200);

        initView(rootView);
        initData();

        swipeRefreshLayout1.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout1.setRefreshing(true);
                initData();
            }
        });

        swipeRefreshLayout2.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout2.setRefreshing(true);
                initData();
            }
        });

        return rootView;
    }


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
        answerRevertBtn = rootView.findViewById(R.id.answerRevertBtn);
        answerList = rootView.findViewById(R.id.displayAnswerList);
        answers = new ArrayList<String>();
        answerAdapter = new DisplayAnswerAdapter(answers, getContext());
        answerList.setAdapter(answerAdapter);
        answerRevertBtn.setOnClickListener(this);
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

        displayQuestionareList.setOnItemClickListener(this);

    }

    private void initMissionPage(View rootView) {
        missionOrTaskSpinner = rootView.findViewById(R.id.missionOrTaskSpinner);
        completenessSpinner = rootView.findViewById(R.id.completenessSpinner);

        String []mItems1 = getResources().getStringArray(R.array.missionOrTask);
        String []mItems2 = getResources().getStringArray(R.array.completeness);

        ArrayAdapter<String> adapter1=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item, mItems1);
        ArrayAdapter<String> adapter2=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item, mItems2);

        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        missionOrTaskSpinner.setAdapter(adapter1);
        completenessSpinner.setAdapter(adapter2);


        missionOrTaskList = rootView.findViewById(R.id.MissionOrTaskList);

        missionOrTaskList.setOnItemClickListener(this);

        missionOrTaskSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                list.clear();
                if (position == 0) {
                    displayMission = true;
                    list.addAll(missions);

                } else if (position == 1) {
                    displayMission = false;
                    list.addAll(tasks);
                    //Toast.makeText(getContext(),  Integer.toString(tasks.size()), Toast.LENGTH_SHORT).show();
                }
                completenessSpinner.setSelection(0);
                missionOrTaskListAdapter.update();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        completenessSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                list.clear();
                if (position == 0) {
                    list.addAll(filter(Constants.FILTER_ALL));
                } else if (position == 1) {
                    list.addAll(filter(Constants.FILTER_COMPLETED));
                } else if (position == 2) {
                    list.addAll(filter(Constants.FILTER_NOT_COMPLETED));
                } else {
                    list.addAll(filter(Constants.FILTER_OVERDUE));
                }

                missionOrTaskListAdapter.update();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void displayMissionPage() {
        missionPage.setVisibility(View.VISIBLE);
        questionarePage.setVisibility(View.GONE);
        answerPage.setVisibility(View.GONE);
    }

    private void displayQuestionarePage(int missionId) {
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
                        questionareAdapter.setFinishNum(qAsummary.getFinishNum());
                        QATitle.setText(qAsummary.getQATitle());
                        QADes.setText(qAsummary.getQADes());
                        QABar.setMax(qAsummary.getTaskNum());
                        QABar.setProgress(qAsummary.getFinishNum());
                        double percent = (double)qAsummary.getFinishNum() / qAsummary.getTaskNum();
                        percent *= 100;
                        String perStr = String.format("%.1f",percent);
                        QAPercent.setText(perStr + "%");

                        questions.clear();
                        questions.addAll(qAsummary.getQuestions());
                        questionareAdapter.update();
                        missionPage.setVisibility(View.GONE);
                        answerPage.setVisibility(View.GONE);
                        questionarePage.setVisibility(View.VISIBLE);

                    }
                });
    }

    private void displayAnswerPage(int index) {
        missionPage.setVisibility(View.GONE);
        questionarePage.setVisibility(View.GONE);
        answerPage.setVisibility(View.VISIBLE);

        answers.clear();
        Log.e("ans:", questions.get(index).getAnswer());
        String[] ans = questions.get(index).getAnswer().split("\\;");
        /*
        answers.add("sdhfksdj");
        answers.add("dasjk");
        answers.add("fhejh");
        */
        answers.addAll(Arrays.asList(ans));
        answerAdapter.update();

    }

    private void initData() {
        list = new ArrayList<>();
        missions = new ArrayList<>();
        tasks = new ArrayList<>();

        missionOrTaskListAdapter = new MissionOrTaskListAdapter(list, getContext());
        missionOrTaskList.setAdapter(missionOrTaskListAdapter);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        UserService userService = retrofit.create(UserService.class);
        Observable<ArrayList<MissionModel>> missionObservable = userService.getMissionsByUserId(getToken(),getUserId());
        missionObservable.subscribeOn(Schedulers.newThread())
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

                        if (displayMission) {
                            list.clear();
                            list.addAll(missions);
                            missionOrTaskListAdapter.update();
                        }

                    }
                });

        Observable<ArrayList<TaskModel>> taskObservable = userService.getTasksByUserId(getToken(),getUserId());
        taskObservable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ArrayList<TaskModel>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getContext(), "获取我的任务错误!", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(ArrayList<TaskModel> taskModels) {
                        tasks.clear();
                        tasks.addAll(taskModels);
                    }
                });

        swipeRefreshLayout1.setRefreshing(false);
        swipeRefreshLayout2.setRefreshing(false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.tasks_menu, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView.getId() == R.id.MissionOrTaskList) {
            //点击我发布的问卷任务
            if (displayMission && ((MissionModel) list.get(i)).getTaskType() == Constants.TASK_QUESTIONARE) {
                lastMissionId = ((MissionModel) list.get(i)).getMissionId();
                displayQuestionarePage(lastMissionId);
            }
            //点击我发布的跑腿任务
            else if (displayMission && ((MissionModel) list.get(i)).getTaskType() == Constants.TASK_ERRAND){
                int missionId = ((MissionModel)list.get(i)).getMissionId();
                Bundle bundle = new Bundle();
                bundle.putInt("missionId",missionId);
                Intent intent  = new Intent();
                intent.setClass(getActivity(),errand_status_page.class);
                intent.putExtras(bundle);
                startActivity(intent);

            }
            //点击我领取的问卷任务
            else if (!displayMission && ((TaskModel) list.get(i)).getTaskType() == Constants.TASK_QUESTIONARE) {
                int taskId = ((TaskModel) list.get(i)).getTaskId();
                Bundle bundle = new Bundle();
                Intent intent = new Intent(getActivity(), fill_in_questionare_page.class);
                bundle.putInt("taskId", taskId);
                intent.putExtras(bundle);
                startActivity(intent);
            }
            //点击我领取的跑腿任务
            else if(!displayMission && ((TaskModel) list.get(i)).getTaskType() == Constants.TASK_ERRAND){
                int taskId = ((TaskModel) list.get(i)).getTaskId();
                Bundle bundle = new Bundle();
                bundle.putInt("taskId",taskId);
                Intent intent  = new Intent();
                intent.setClass(getActivity(),errand_detail_page.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        } else if (adapterView.getId() == R.id.displayQuestionareList) {
            //点击问卷中的问答题
            if (questions.get(i).getQuestionType() == Constants.QUERY_QUESTION) {
                displayAnswerPage(i);
            }
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.revertBtn) {
            displayMissionPage();
        }

        if (view.getId() == R.id.answerRevertBtn) {
            displayQuestionarePage(lastMissionId);
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

    private ArrayList<MissionOrTask> filter(int mode) {

        if (displayMission) {

            ArrayList<MissionOrTask> ret = new ArrayList<>();
            if (mode == Constants.FILTER_ALL) {
                return missions;
            } else if (mode == Constants.FILTER_COMPLETED) {
                for (MissionOrTask m: missions) {
                    if (((MissionModel)m).getMissionStatus() == Constants.MAX_PEOPLE_DONE) {
                        ret.add(m);
                    }
                }
            } else if (mode == Constants.FILTER_OVERDUE) {
                for (MissionOrTask m: missions) {
                    if (((MissionModel)m).getMissionStatus() == Constants.OVERDUE) {
                        ret.add(m);
                    }
                }

            } else {
                for (MissionOrTask m: missions) {
                    if (((MissionModel)m).getMissionStatus() == Constants.NEED_MORE_PEOPLE ||
                            ((MissionModel)m).getMissionStatus() == Constants.MAX_PEOPLE) {
                        ret.add(m);
                    }
                }
            }

            return ret;
        } else {
            ArrayList<MissionOrTask> ret = new ArrayList<>();
            if (mode == Constants.FILTER_ALL) {
                return tasks;
            } else if (mode == Constants.FILTER_COMPLETED) {
                for (MissionOrTask m: tasks) {
                    if (((TaskModel)m).getTaskStatus() == Constants.TASK_DONE_AND_CONFIRM) {
                        ret.add(m);
                    }
                }
            } else if (mode == Constants.FILTER_NOT_COMPLETED) {
                for (MissionOrTask m: tasks) {
                    if (((TaskModel)m).getTaskStatus() == Constants.TASK_DONE_BUT_NOT_CONFIRM ||
                            ((TaskModel)m).getTaskStatus() == Constants.TASK_DOING ||
                            ((TaskModel)m).getTaskStatus() == Constants.TASK_TO_DO) {
                        ret.add(m);
                    }
                }
            }

            return ret;
        }

    }
}
