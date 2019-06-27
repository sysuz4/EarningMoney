package com.example.asus.earingmoney;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.earingmoney.Util.Util;
import com.example.asus.earingmoney.model.Mission;
import com.example.asus.earingmoney.model.User;

import java.io.IOException;

import okhttp3.ResponseBody;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MissionDetailActivity extends AppCompatActivity {

    public static MissionDetailActivity instance = null;
    private int missionId,taskType;
    private Toolbar toolbar;
    private service myservice;
    private ServiceFactory serviceFactory;
    private TextView publish_time,detail,deadline,money,name,titleText;
    private Button button;
    private ImageView image;
    private int userId;
    private String description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission_detail);

        instance = this;

        missionId = (int)getIntent().getExtras().get("missionId");
        taskType = (int)getIntent().getExtras().get("taskType");
        description = (String) getIntent().getExtras().get("Description");

        SharedPreferences user_shared_preference = getSharedPreferences("user", 0);
        SharedPreferences.Editor editor = user_shared_preference.edit();
        editor.putInt("missionId",missionId);
        editor.commit();

        publish_time = findViewById(R.id.publish_time);
        detail = findViewById(R.id.detail);
        deadline = findViewById(R.id.deadline);
        money = findViewById(R.id.money);
        name = findViewById(R.id.name);
        titleText = findViewById(R.id.titleText);
        button = findViewById(R.id.button);
        image = findViewById(R.id.image);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        serviceFactory = new ServiceFactory();
        myservice = serviceFactory.CreatService();

        //从后台请求该任务详情信息
        getMissionDetail();

        if(taskType == 0)
            image.setImageResource(R.mipmap.errand);
        else
            image.setImageResource(R.mipmap.questionare);

        //接受任务按钮
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Observer<ResponseBody> observer_accept_mission = new Observer<ResponseBody>() {
                    @Override
                    public void onNext(ResponseBody mission) {
//                        try {
//                            System.out.println(mission.string());
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
                    }

                    @Override
                    public void onCompleted() {
                        MainPartActivity.instance.finish();
                        Toast.makeText(getApplicationContext(), "任务接受成功", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MissionDetailActivity.this,MainPartActivity.class);
                        startActivity(intent);

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getApplicationContext(), "任务接受失败", Toast.LENGTH_SHORT).show();
                    }
                };
                myservice.acceptMission(Util.getToken(getApplicationContext()),missionId)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(observer_accept_mission);
            }
        });
    }

    //右上方小组件变化
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.missin_detail_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //返回按钮
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    //从后台获取mission详情
    private void getMissionDetail(){
        Observer<Mission> observer_mission = new Observer<Mission>() {
            @Override
            public void onNext(Mission mission) {
                userId = mission.getUserId();
                getUserDetail();
                publish_time.setText(mission.getPublishTime());
                detail.setText(description);
                deadline.setText(mission.getDeadLine());
                money.setText(mission.getMoney()/mission.getTaskNum() + "元");
                titleText.setText(mission.getTitle());
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

    //通过userId获取用户昵称
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
