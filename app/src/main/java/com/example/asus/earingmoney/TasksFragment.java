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
import com.example.asus.earingmoney.adapter.MissionOrTaskListAdapter;
import com.example.asus.earingmoney.model.MissionModel;
import com.example.asus.earingmoney.model.MissionOrTask;

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

    private ArrayList<MissionOrTask> missions;
    private ArrayList<MissionOrTask> tasks;

    private ConstraintLayout missionPage;
    private ConstraintLayout questionarePage;
    private ConstraintLayout answerPage;
    private ArrayList<MissionOrTask> mList;

    private MemorySpinner missionOrTaskMS;
    private MemorySpinner completenessMS;

    private ImageView revertBtn;
    private ProgressBar completeness;
    private ListView displayQuestionareList;

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

        Toast.makeText(getContext(), getToken(), Toast.LENGTH_SHORT).show();
    }

    private void initAnswerPage(View rootView) {
    }

    private void initQuestionPage(View rootView) {
        revertBtn = rootView.findViewById(R.id.revertBtn);
        completeness = rootView.findViewById(R.id.completeness);
        displayQuestionareList = rootView.findViewById(R.id.displayQuestionareList);

        revertBtn.setOnClickListener(this);
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

    private void displayQuestionarePage() {
        missionPage.setVisibility(View.GONE);
        questionarePage.setVisibility(View.VISIBLE);
        answerPage.setVisibility(View.GONE);
    }

    private void displayAnswerPage() {
        missionPage.setVisibility(View.GONE);
        questionarePage.setVisibility(View.GONE);
        answerPage.setVisibility(View.VISIBLE);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initData() {
        mList = new ArrayList<>();

        missionOrTaskListAdapter = new MissionOrTaskListAdapter(mList, getContext());
        missionOrTaskList.setAdapter(missionOrTaskListAdapter);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        UserService userService = retrofit.create(UserService.class);
        Observable<ArrayList<MissionModel>> repoObservable = userService.getMissionsByUserId(getToken(),9);

        repoObservable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ArrayList<MissionModel>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        //Toast.makeText(getContext(), "request missions error!", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(ArrayList<MissionModel> missionModels) {
                        Toast.makeText(getContext(), missionModels.get(0).getTitle(), Toast.LENGTH_SHORT).show();
                        mList.clear();
                        mList.addAll(missionModels);

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
        displayQuestionarePage();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.revertBtn) {
            displayMissionPage();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String getToken() {
        user_shared_preference = getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        String[] token = user_shared_preference.getString("token", "").split("\n");
        return String.join("", token);
    }
}
