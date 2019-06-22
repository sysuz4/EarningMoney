package com.example.asus.earingmoney;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.earingmoney.Util.Util;
import com.example.asus.earingmoney.model.GetMissionsObj;
import com.example.asus.earingmoney.model.Mission;
import com.example.asus.earingmoney.model.User;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MissionDetailActivity extends AppCompatActivity {

    private int missionId;
    private Toolbar toolbar;
    private service myservice;
    private ServiceFactory serviceFactory;
    private TextView publish_time,detail,deadline,money,name,title;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission_detail);

        missionId = (int)getIntent().getExtras().get("missionId");

        publish_time = findViewById(R.id.publish_time);
        detail = findViewById(R.id.detail);
        deadline = findViewById(R.id.deadline);
        money = findViewById(R.id.money);
        name = findViewById(R.id.name);
        title = findViewById(R.id.title);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        serviceFactory = new ServiceFactory();
        myservice = serviceFactory.CreatService();

        getMissionDetail();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.missin_detail_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    private void getMissionDetail(){
        Observer<Mission> observer_mission = new Observer<Mission>() {
            @Override
            public void onNext(Mission mission) {
                userId = mission.getUserId();
                getUserDetail();
                publish_time.setText(mission.getPublishTime());
                detail.setText(mission.getDescription());
                deadline.setText(mission.getDeadLine());
                money.setText(mission.getMoney() + "元");
                title.setText(mission.getTitle());
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(getApplicationContext(), "加载任务详情失败", Toast.LENGTH_SHORT).show();
            }
        };
        myservice.getMissionDetail(missionId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer_mission);
    }

    private void getUserDetail(){
        Observer<User> observer_user = new Observer<User>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(User user) {
                //System.out.println(user.getUserId());
                name.setText(user.getName());
            }
        };
        myservice.getUserDetail(Util.getToken(getApplicationContext()),userId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer_user);
    }
}
