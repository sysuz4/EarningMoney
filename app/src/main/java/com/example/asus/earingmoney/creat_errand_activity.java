package com.example.asus.earingmoney;

import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.asus.earingmoney.Util.Constants;
import com.example.asus.earingmoney.Util.Util;
import com.example.asus.earingmoney.model.CreateErrandModel;
import com.example.asus.earingmoney.model.CreateQuestionareModel;
import com.example.asus.earingmoney.model.Errand;
import com.example.asus.earingmoney.model.Mission;
import com.example.asus.earingmoney.model.Questionare;
import com.example.asus.earingmoney.model.Task;
import com.google.gson.Gson;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class creat_errand_activity extends AppCompatActivity implements View.OnLongClickListener{
    private int currentBtn = 0;

    private EditText titleText;
    private EditText descripText;
    private EditText errandContactText;
    public String finishDate;
    public Float money;
    public int taskNum;

    private service myservice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creat_errand_activity);

        android.widget.ImageButton button = findViewById(R.id.addImgBtn1);
        button.setOnLongClickListener(this);
        button = findViewById(R.id.addImgBtn2);
        button.setOnLongClickListener(this);
        button = findViewById(R.id.addImgBtn3);
        button.setOnLongClickListener(this);
        button = findViewById(R.id.addImgBtn4);
        button.setOnLongClickListener(this);
        button = findViewById(R.id.addImgBtn5);
        button.setOnLongClickListener(this);
        button = findViewById(R.id.addImgBtn6);
        button.setOnLongClickListener(this);
        button = findViewById(R.id.addImgBtn7);
        button.setOnLongClickListener(this);
        button = findViewById(R.id.addImgBtn8);
        button.setOnLongClickListener(this);
        button = findViewById(R.id.addImgBtn9);
        button.setOnLongClickListener(this);

        titleText = findViewById(R.id.titleText);
        descripText = findViewById(R.id.descripText);
        errandContactText = findViewById(R.id.errandContactText);
        finishDate = "";
        money = -1.f;
        taskNum = -1;

        OkHttpClient build = new OkHttpClient.Builder()
                .connectTimeout(2, TimeUnit.SECONDS)
                .readTimeout(2, TimeUnit.SECONDS)
                .writeTimeout(2, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASEURL)
                // 本次实验不需要自定义Gson
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                // build 即为okhttp声明的变量，下文会讲
                .client(build)
                .build();

        myservice = retrofit.create(service.class);
    }

    public void okFab_click(android.view.View view) {
        if(titleText.getText().toString().isEmpty())
            Toast.makeText(this, "标题不能为空", Toast.LENGTH_SHORT).show();
        else if(errandContactText.getText().toString().isEmpty())
            Toast.makeText(this, "联系方式不能为空", Toast.LENGTH_SHORT).show();
        else
        {
            final com.example.asus.earingmoney.lib.FinishQuestionareDialog dialog = new com.example.asus.earingmoney.lib.FinishQuestionareDialog(creat_errand_activity.this);
            dialog.showAnim(new com.flyco.animation.NewsPaperEnter())//
                    .dismissAnim(new com.flyco.animation.FadeExit.FadeExit())//
                    .show();
            dialog.setCanceledOnTouchOutside(false);
        }
    }

    public void imageBtn_click(android.view.View view)
    {

            android.content.Intent intent = new android.content.Intent();
            intent.setAction(android.content.Intent.ACTION_PICK);
            intent.setType("image/*");
            currentBtn = view.getId();
            startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, android.content.Intent data) {
        if (data != null) {
            // 得到图片的全路径
            android.net.Uri uri = data.getData();
            android.content.ContentResolver cr = this.getContentResolver();
            //存储副本
            try {
                android.graphics.Bitmap bitmap = android.graphics.BitmapFactory.decodeStream(cr.openInputStream(uri));
                //通过UUID生成字符串文件名
                String image_name = java.util.UUID.randomUUID().toString() + ".jpg";
                //存储图片
                java.io.FileOutputStream out = openFileOutput(image_name, MODE_PRIVATE);
                bitmap.compress(android.graphics.Bitmap.CompressFormat.JPEG, 100, out);
                //获取复制后文件的uri
                android.net.Uri image_file_uri = android.net.Uri.fromFile(getFileStreamPath(image_name));
                //图片预览
                android.widget.ImageButton btn = findViewById(currentBtn);
                btn.setImageURI(image_file_uri);

                Toast.makeText(this, "长按可删除图片", Toast.LENGTH_SHORT).show();

                out.flush();
                out.close();
            }
            catch (java.io.FileNotFoundException e) {
                android.util.Log.e("FileNotFoundException", e.getMessage(),e);
            }
            catch (java.io.IOException e) {
                android.util.Log.w("IOException", e.getMessage(), e);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onLongClick(View view) {
        ImageButton button = (ImageButton)view;
        button.setImageResource(R.mipmap.addd);
        return false;
    }

    public void create_errand_to_server()
    {
        CreateErrandModel createErrandModel = new CreateErrandModel();
        Mission mission = new Mission();
        Task task = new Task();
        Errand errand = new Errand();

        errand.setDescription(descripText.getText().toString());
        errand.setPrivateInfo(errandContactText.getText().toString());

        task.setFinishTime(finishDate);
        task.setTaskType(Constants.TASK_QUESTIONARE);
        task.setTaskStatus(Constants.TO_DO);

        mission.setDeadLine(finishDate);
        mission.setMoney(money);
        mission.setTags(""); //todo
        mission.setTitle(titleText.getText().toString());
        mission.setTaskNum(taskNum);
        mission.setPublishTime(Util.convertDate2String(new Date()));
        mission.setMissionStatus(Constants.NEED_MORE_PEOPLE);
        mission.setUserId(); //todo

        createErrandModel.setErrand(errand);
        createErrandModel.setMission(mission);
        createErrandModel.setTask(task);

        Gson gson=new Gson();
        String jsonBody = gson.toJson(createErrandModel);
        RequestBody reqBody = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=utf-8"),jsonBody);

        Call<String> postCall =  myservice.create_errand(Util.getToken(this), reqBody);
        postCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.code() == 200)
                {
                    Toast.makeText(getApplicationContext(), "200",
                            Toast.LENGTH_SHORT).show();
                }
                else if(response.code() == 401){
                    Toast.makeText(getApplicationContext(), "401",
                            Toast.LENGTH_SHORT).show();
                }
                else if(response.code() == 404){
                    Toast.makeText(getApplicationContext(), "404",
                            Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), String.valueOf(response.code()),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("s", t.toString());
                Toast.makeText(getApplicationContext(), "fail",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
