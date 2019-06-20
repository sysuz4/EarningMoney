package com.example.asus.earingmoney;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
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
import com.example.asus.earingmoney.adapter.MissionOrTaskListAdapter;
import com.example.asus.earingmoney.model.MissionModel;
import com.example.asus.earingmoney.model.MissionOrTask;

import java.util.ArrayList;
import java.util.Arrays;

public class TasksFragment extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener{
    private static final String ARG_SHOW_TEXT = "text";

    private String mContentText;
    private ListView missionOrTaskList;
    private MissionOrTaskListAdapter missionOrTaskListAdapter;
    private ConstraintLayout missionPage;
    private ConstraintLayout questionarePage;
    private ConstraintLayout answerPage;
    private ArrayList<MissionOrTask> mList;

    private MemorySpinner missionOrTaskMS;
    private MemorySpinner completenessMS;

    private ImageView revertBtn;
    private ProgressBar completeness;
    private ListView displayQuestionareList;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.tasks_fragment, container, false);
        setHasOptionsMenu(true);
        //TextView contentTv = rootView.findViewById(R.id.content_tv);
        //contentTv.setText(mContentText);
        
        initData();
        initView(rootView);

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
        missionOrTaskListAdapter = new MissionOrTaskListAdapter(mList, getContext());
        missionOrTaskList.setAdapter(missionOrTaskListAdapter);
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

    private void initData() {
        mList = new ArrayList<>();
        for (int i = 0; i < 5; ++i) {
            mList.add(new MissionModel());
        }
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
}
