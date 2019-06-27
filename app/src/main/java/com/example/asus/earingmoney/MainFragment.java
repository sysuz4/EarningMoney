package com.example.asus.earingmoney;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.earingmoney.Util.MissionsSortUtil;
import com.example.asus.earingmoney.Util.Util;
import com.example.asus.earingmoney.adapter.ListViewAdapter_missions;
import com.example.asus.earingmoney.model.GetMissionsObj;
import com.example.asus.earingmoney.model.Mission;
import com.example.asus.earingmoney.model.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

import rx.Completable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainFragment extends Fragment {
    private static final String ARG_SHOW_TEXT = "text";

    private Spinner spinner1, spinner2, spinner3;
    private service myservice;
    private ServiceFactory serviceFactory;
    private List<Mission> missionslist = new ArrayList<Mission>();//missionslist为显示的内容，
    private List<Mission> totallist = new ArrayList<Mission>();   //totallist为所有mission内容，ques_list和err_list分别为问卷任务和跑腿任务
    private List<Mission> questionare_missionslist = new ArrayList<Mission>();
    private List<Mission> errand_missionslist = new ArrayList<Mission>();
    public ListViewAdapter_missions adapter;
    private ListView listview;
    private String[] mItems1,mItems2,mItems3;
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

        //初始化下拉刷新组件
        swipeRefreshLayout = rootView.findViewById(R.id.swipeLayout);
        swipeRefreshLayout.setSize(SwipeRefreshLayout.DEFAULT);
        swipeRefreshLayout.setProgressViewEndTarget(true, 200);

        //初始化下拉排序组件
        spinner1 = rootView.findViewById(R.id.spinner1);
        spinner2 = rootView.findViewById(R.id.spinner2);
        spinner3 = rootView.findViewById(R.id.spinner3);
        mItems1 = getResources().getStringArray(R.array.spin1);
        mItems2 = getResources().getStringArray(R.array.spin2);
        mItems3 = getResources().getStringArray(R.array.spin3);
        ArrayAdapter<String> adapter1=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item, mItems1);
        ArrayAdapter<String> adapter2=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item, mItems2);
        ArrayAdapter<String> adapter3=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item, mItems3);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //绑定 Adapter到控件
        spinner1.setAdapter(adapter1);
        spinner2.setAdapter(adapter2);
        spinner3.setAdapter(adapter3);

        iniSpiner();
//        TextView contentTv = rootView.findViewById(R.id.content_tv);
//        contentTv.setText(mContentText);

        serviceFactory = new ServiceFactory();
        myservice = serviceFactory.CreatService();

        listview = rootView.findViewById(R.id.list);
        adapter = new ListViewAdapter_missions(getActivity(), R.layout.mission_item, missionslist);
        listview.setAdapter(adapter);
//        Util.setListViewHeightBasedOnChildren(listview);

        //向后台请求任务数据
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

        //下拉刷新更新数据
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        //筛选标签
        switch (item.getItemId()) {
            case R.id.filtrate:
                showDialog();
                break;
        }
        return true;
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

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { //对问卷或跑腿任务进行筛选
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

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){ //对酬劳进行排序

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    MissionsSortUtil.sortById(missionslist);
                }
                else if(position == 1){
                    MissionsSortUtil.sortByPriceDown(missionslist);
                }
                else {
                    MissionsSortUtil.sortByPriceUp(missionslist);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){ //对截止时间进行排序

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
        totallist.clear();
        missionslist.clear();
        questionare_missionslist.clear();
        errand_missionslist.clear();
        Observer<GetMissionsObj> observer = new Observer<GetMissionsObj>() {
            @Override
            public void onNext(GetMissionsObj missions) {
                for(Mission i : missions.getAllMissions()){
                    boolean have_this_mission = false;
                    //System.out.println(i.getTitle());
                    //System.out.println(i.getMissionStatus());
                    if(i.getMissionStatus() != 0) //如果问卷填写人数不为可接受则不显示
                        continue;
                    if(i.getReportNum() >= 4) //如果举报次数>=4就不显示
                        continue;
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


    private Handler handler;
    private String str = "";
    private Button choice1;
    private Button choice2;
    private Button choice3;
    private Button choice4;
    private Button continue_button;
    private TextView title;
    private ArrayList<String> tags;

    //选择想要进行筛选的标签
    public void showDialog(){
        str = "";
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_layout,null,false);

        choice1 = view.findViewById(R.id.choice1);
        choice2 = view.findViewById(R.id.choice2);
        choice3 = view.findViewById(R.id.choice3);
        choice4 = view.findViewById(R.id.choice4);
        continue_button = view.findViewById(R.id.continue_button);
        title = view.findViewById(R.id.title);
        final AlertDialog dialog = new AlertDialog.Builder(getContext()).setView(view).create();


        handler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                super.handleMessage(msg);
                if(msg.what == 1){
                    choice1.setBackgroundColor(getContext().getResources().getColor(R.color.blue));
                    str += choice1.getText().toString() + "、";
                }
                if(msg.what == 2){
                    choice2.setBackgroundColor(getContext().getResources().getColor(R.color.blue));
                    str += choice2.getText().toString() + "、";
                }
                if(msg.what == 3){
                    choice3.setBackgroundColor(getContext().getResources().getColor(R.color.blue));
                    str += choice3.getText().toString() + "、";
                }
                if(msg.what == 4)
                {
                    choice4.setBackgroundColor(getContext().getResources().getColor(R.color.blue));
                    str += choice4.getText().toString() + "、";
                }
                if(msg.what == 5){
                    choice1.setBackgroundColor(getContext().getResources().getColor(R.color.gray));
                    str = str.replaceAll(choice1.getText().toString()+"、","");
                }
                if(msg.what == 6){
                    choice2.setBackgroundColor(getContext().getResources().getColor(R.color.gray));
                    str = str.replaceAll(choice2.getText().toString()+"、","");
                }
                if(msg.what == 7){
                    choice3.setBackgroundColor(getContext().getResources().getColor(R.color.gray));
                    str = str.replaceAll(choice3.getText().toString()+"、","");
                }
                if(msg.what == 8){
                    choice4.setBackgroundColor(getContext().getResources().getColor(R.color.gray));
                    str = str.replaceAll(choice4.getText().toString()+"、","");
                }
                if(msg.what == 9){
                    title.setText("专业");
                    choice1.setText("IT");
                    choice2.setText("经管");
                    choice3.setText("物化生医");
                    choice4.setText("文史哲");
                    choice1.setBackgroundColor(getContext().getResources().getColor(R.color.gray));
                    choice2.setBackgroundColor(getContext().getResources().getColor(R.color.gray));
                    choice3.setBackgroundColor(getContext().getResources().getColor(R.color.gray));
                    choice4.setBackgroundColor(getContext().getResources().getColor(R.color.gray));
                }
                if(msg.what == 10){
                    title.setText("个人特性");
                    choice1.setText("热情开朗");
                    choice2.setText("文艺青年");
                    choice3.setText("忧郁小王子");
                    choice4.setText("沉着冷静");
                    choice1.setBackgroundColor(getContext().getResources().getColor(R.color.gray));
                    choice2.setBackgroundColor(getContext().getResources().getColor(R.color.gray));
                    choice3.setBackgroundColor(getContext().getResources().getColor(R.color.gray));
                    choice4.setBackgroundColor(getContext().getResources().getColor(R.color.gray));
                }
                if(msg.what == 11){
                    title.setText("爱好");
                    choice1.setText("体育运动");
                    choice2.setText("音乐绘画");
                    choice3.setText("二次元");
                    choice4.setText("影视书籍");
                    choice1.setBackgroundColor(getContext().getResources().getColor(R.color.gray));
                    choice2.setBackgroundColor(getContext().getResources().getColor(R.color.gray));
                    choice3.setBackgroundColor(getContext().getResources().getColor(R.color.gray));
                    choice4.setBackgroundColor(getContext().getResources().getColor(R.color.gray));
                }
                if(msg.what == 12){
                    dialog.dismiss();
                    compare();
                }
            }
        };

        choice1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message msg = new Message();
                msg.what = 1;
                handler.sendMessage(msg);
            }
        });
        choice1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Message msg = new Message();
                msg.what = 5;
                handler.sendMessage(msg);
                return true;
            }
        });

        choice2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message msg = new Message();
                msg.what = 2;
                handler.sendMessage(msg);
            }
        });
        choice2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Message msg = new Message();
                msg.what = 6;
                handler.sendMessage(msg);
                return true;
            }
        });

        choice3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message msg = new Message();
                msg.what = 3;
                handler.sendMessage(msg);
            }
        });
        choice3.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Message msg = new Message();
                msg.what = 7;
                handler.sendMessage(msg);
                return true;
            }
        });

        choice4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message msg = new Message();
                msg.what = 4;
                handler.sendMessage(msg);
                // dialog.dismiss();
            }
        });
        choice4.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Message msg = new Message();
                msg.what = 8;
                handler.sendMessage(msg);
                return true;
            }
        });

        continue_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str += ";";
                Log.d("testing", "onClick: " + str);
                if(title.getText().toString().equals("年级")){
                    Message msg = new Message();
                    msg.what = 9;
                    handler.sendMessage(msg);
                } else if(title.getText().toString().equals("专业")){
                    Message msg = new Message();
                    msg.what = 10;
                    handler.sendMessage(msg);
                } else if(title.getText().toString().equals("个人特性")){
                    Message msg = new Message();
                    msg.what = 11;
                    handler.sendMessage(msg);
                } else if(title.getText().toString().equals("爱好")) {
                    Message msg = new Message();
                    msg.what = 12;
                    handler.sendMessage(msg);
                }


            }
        });

        dialog.show();
        //此处设置位置窗体大小 注意一定要在show方法调用后再写设置窗口大小的代码，否则不起效果会
        dialog.getWindow().setLayout((ScreenUtils.getScreenWidth(getContext())), LinearLayout.LayoutParams.WRAP_CONTENT);

    }

    private ArrayList<String> divideTags(String Tags){
        ArrayList<String> tags = new ArrayList<String>();
        String[] tag1 = Tags.split(";");
        for(int i = 0; i < tag1.length; i++){
            String[] tag2 = tag1[i].split("、");
            for(int j = 0; j < tag2.length; j++){
                if(!tags.contains(tag2[j])){
                    tags.add(tag2[j]);
                    //Log.d("testing", "onClick: " + tag2[j]);
                }
            }
        }
        return  tags;
    }

    //对比任务和选择的tag，进行筛选
    private void compare(){
        tags = divideTags(str);
        ArrayList<String> temp = new ArrayList<String>();

        if(tags.size() == 0)
            return;

        missionslist.clear();
        for(Mission i :totallist){
            if(i.getTags() == null)
                continue;
            temp = divideTags(i.getTags());
            Log.d("testing",  i.getTags());
            boolean equal = false;
            for(String j : tags){
                for(String k : temp){
                    if(j.equals(k)){
                        equal = true;
                        break;
                    }
                    equal = false;
                }
            }
            if(equal == true){
                missionslist.add(i);
            }
        }
        adapter.notifyDataSetChanged();
    }
}
