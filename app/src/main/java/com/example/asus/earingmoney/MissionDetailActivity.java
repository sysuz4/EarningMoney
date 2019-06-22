package com.example.asus.earingmoney;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.asus.earingmoney.Util.Util;
import com.example.asus.earingmoney.model.GetMissionsObj;
import com.example.asus.earingmoney.model.Mission;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MissionDetailActivity extends AppCompatActivity {

    private int missionId;
    private Toolbar toolbar;
    private service myservice;
    private ServiceFactory serviceFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission_detail);

        missionId = (int)getIntent().getExtras().get("missionId");

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        serviceFactory = new ServiceFactory();
        myservice = serviceFactory.CreatService();

        Observer<Mission> observer = new Observer<Mission>() {
            @Override
            public void onNext(Mission mission) {
                System.out.println(mission.getMissionId());
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(getApplicationContext(), "加载任务详情失败", Toast.LENGTH_SHORT).show();
            }
        };
        myservice.getMissionsDetail(missionId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
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
}
