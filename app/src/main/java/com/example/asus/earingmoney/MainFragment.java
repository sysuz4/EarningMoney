package com.example.asus.earingmoney;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.asus.earingmoney.Util.MissionsSortUtil;
import com.example.asus.earingmoney.Util.Util;
import com.example.asus.earingmoney.adapter.ListViewAdapter_missions;
import com.example.asus.earingmoney.model.GetMissionsObj;
import com.example.asus.earingmoney.model.Mission;

import java.util.ArrayList;
import java.util.List;

import rx.Completable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainFragment extends Fragment {
    private static final String ARG_SHOW_TEXT = "text";

    private Spinner spinner1, spinner2, spinner3, spinner4;
    private service myservice;
    private ServiceFactory serviceFactory;
    private List<Mission> missionslist = new ArrayList<Mission>();
    private List<Mission> totallist = new ArrayList<Mission>();
    private List<Mission> questionare_missionslist = new ArrayList<Mission>();
    private List<Mission> errand_missionslist = new ArrayList<Mission>();
    public ListViewAdapter_missions adapter;
    private ListView listview;
    private String[] mItems1,mItems2,mItems3,mItems4;
    private SwipeRefreshLayout swipeRefreshLayout;


    public MainFragment() {
        // Required empty public constructor
    }

    public static MainFragment newInstance(String param1) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SHOW_TEXT, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.main_fragment, container, false);
        setHasOptionsMenu(true);

        swipeRefreshLayout = rootView.findViewById(R.id.swipeLayout);
        swipeRefreshLayout.setSize(SwipeRefreshLayout.DEFAULT);
        swipeRefreshLayout.setProgressViewEndTarget(true, 200);

        spinner1 = rootView.findViewById(R.id.spinner1);
        spinner2 = rootView.findViewById(R.id.spinner2);
        spinner3 = rootView.findViewById(R.id.spinner3);
        spinner4 = rootView.findViewById(R.id.spinner4);
        mItems1 = getResources().getStringArray(R.array.spin1);
        mItems2 = getResources().getStringArray(R.array.spin2);
        mItems3 = getResources().getStringArray(R.array.spin3);
        mItems4 = getResources().getStringArray(R.array.spin4);
        ArrayAdapter<String> adapter1=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item, mItems1);
        ArrayAdapter<String> adapter2=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item, mItems2);
        ArrayAdapter<String> adapter3=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item, mItems3);
        ArrayAdapter<String> adapter4=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item, mItems4);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //绑定 Adapter到控件
        spinner1.setAdapter(adapter1);
        spinner2.setAdapter(adapter2);
        spinner3.setAdapter(adapter3);
        spinner4.setAdapter(adapter4);

        iniSpiner();
//        TextView contentTv = rootView.findViewById(R.id.content_tv);
//        contentTv.setText(mContentText);

        serviceFactory = new ServiceFactory();
        myservice = serviceFactory.CreatService();

        listview = rootView.findViewById(R.id.list);
        adapter = new ListViewAdapter_missions(getActivity(), R.layout.mission_item, missionslist);
        listview.setAdapter(adapter);
//        Util.setListViewHeightBasedOnChildren(listview);

        getMissions();

        listview.setOnItemClickListener(new MyOnItemClickListener());

//        Button button = rootView.findViewById(R.id.button);
////        button.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                Intent intent = new Intent(getActivity(),MissionDetailActivity.class);
////                startActivity(intent);
////            }
////        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                getMissions();
            }
        });

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.main_menu, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    private class MyOnItemClickListener implements AdapterView.OnItemClickListener {//任务的点击事件，跳转到详情
        @Override
        public void onItemClick(AdapterView<?> parent,View view,int position, long id) {
            if(position >= 0) {
                Intent intent = new Intent(getActivity(), MissionDetailActivity.class);
                intent.putExtra("missionId", missionslist.get(position).getMissionId());
                intent.putExtra("taskType", missionslist.get(position).getTaskType());
                intent.putExtra("Description", missionslist.get(position).getDescription());
                startActivity(intent);
            }
        }
    }

    private void iniSpiner(){//用于排序、筛选

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    missionslist.clear();
                    missionslist.addAll(totallist);
                }
                else if(position == 1){
                    missionslist.clear();
                    missionslist.addAll(questionare_missionslist);
                }
                else{
                    missionslist.clear();
                    missionslist.addAll(errand_missionslist);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    MissionsSortUtil.sortById(missionslist);
                }
                else if(position == 1){
                    MissionsSortUtil.sortByPriceUp(missionslist);
                }
                else {
                    MissionsSortUtil.sortByPriceDown(missionslist);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    MissionsSortUtil.sortById(missionslist);
                }
                else if(position == 1){
                    MissionsSortUtil.sortByTimeUp(missionslist);
                }
                else {
                    MissionsSortUtil.sortByTimeDown(missionslist);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getMissions(){ //获取任务列表
        Observer<GetMissionsObj> observer = new Observer<GetMissionsObj>() {
            @Override
            public void onNext(GetMissionsObj missions) {
                for(Mission i : missions.getAllMissions()){
                    boolean have_this_mission = false;
                    //System.out.println(i.getMissionId());
                    if(!(i.isMyAccept() || i.isMyPub())){//判断该任务是否已经接受，或是否为自己创建的，如是则不显示
                        for(Mission j :missionslist){
                            if(j.getMissionId() == i.getMissionId())
                                have_this_mission = true;
                        }
                        if(!have_this_mission){
                            totallist.add(i);
                            missionslist.add(i);
                            if(i.getTaskType() == 1){
                                questionare_missionslist.add(i);
                            }
                            else {
                                errand_missionslist.add(i);
                            }
                        }

                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(getContext().getApplicationContext(), "加载任务失败", Toast.LENGTH_SHORT).show();
            }
        };
        myservice.getMissions(Util.getToken(getActivity()))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

        swipeRefreshLayout.setRefreshing(false);
    }
}
