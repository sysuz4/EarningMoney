package com.example.asus.earingmoney;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.earingmoney.Util.Constants;
import com.example.asus.earingmoney.Util.Util;
import com.example.asus.earingmoney.lib.FinishQuestionareDialog2;
import com.example.asus.earingmoney.model.Image;
import com.example.asus.earingmoney.model.Msg;
import com.example.asus.earingmoney.model.ScreenUtils;
import com.example.asus.earingmoney.model.User;
import com.flyco.animation.FadeEnter.FadeEnter;
import com.flyco.animation.FadeExit.FadeExit;
import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MeFragment extends Fragment {
    private static final String ARG_SHOW_TEXT = "text";

    private String mContentText;

    public boolean modifyStatus = false;
    private View view;
    EditText usernameText;
    EditText oldPasswordText;
    EditText newPasswordText;
    EditText realNameText;
    EditText ageText;
    EditText gradeText;
    EditText majorText;
    EditText mailText;
    EditText phoneText;
    TextView balanceText;
    TextView creditValueText;
    EditText studentIdText;
    TextView pass;
    ImageView sexImage;
    CircleImageView header;
    Button logOut;
    SharedPreferences user_shared_preference;
    String ImageName;
    private service myservice;

    Menu mymenu;

    GridView tagsLayout;
    List<Map<String, Object>> tagList;
    private String[] from = { "tag_name" };
    private int[] to = {R.id.tag_name };
    SimpleAdapter pictureAdapter;
    TextView tag_label;
    public MeFragment() {
        // Required empty public constructor
    }

    public static MeFragment newInstance(String param1) {
        MeFragment fragment = new MeFragment();
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

    private void initData() {
        final Call<User> getCall =  myservice.get_user(Util.getToken(getContext()), Util.getUserId(getContext())); //todo
        getCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User user = response.body();
                if(response.code() == 200)
                {
                    //Toast.makeText(getContext(), "200",
                            //Toast.LENGTH_SHORT).show();

                    usernameText.setText(user.getNickName());
                    realNameText.setText(user.getName());
                    ageText.setText(""+user.getAge());
                    phoneText.setText(user.getPhoneNum());
                    gradeText.setText(""+user.getGrade());
                    mailText.setText(user.getMailAddr());
                    majorText.setText(user.getMajor());
                    creditValueText.setText(user.getCreditVal());
                    studentIdText.setText(user.getstuId());
                    balanceText.setText("" + user.getBalance());
                    final MainPartActivity activity = (MainPartActivity)getContext();
                    if(user.getSex() == Constants.MALE){
                        sexImage.setImageResource(R.mipmap.boy);
                        activity.sex = Constants.MALE;
                    }
                    else
                    {
                        sexImage.setImageResource(R.mipmap.girl);
                        activity.sex = Constants.FEMALE;
                    }
                    final int gender = user.getSex();
                    //todo avator image
                    ImageName = user.getAvator();
                    final String imgUrl = Constants.BASEURL + "images/" + ImageName;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            final Bitmap b = getImageBitmap(imgUrl);
                            header.post(new Runnable() {
                                @Override
                                public void run() {
                                    if(b == null){
                                        if(gender == Constants.MALE){
                                            header.setImageResource(R.mipmap.man);
                                        }
                                        else
                                        {
                                            header.setImageResource(R.mipmap.woman);
                                        }
                                    }
                                    else
                                        header.setImageBitmap(b);
                                }
                            });
                        }
                    }).start();

                    str = user.getTags();
                    List<String> tags = Util.decodeTags(user.getTags());
                    tagsLayout.removeAllViewsInLayout();
                    tagList.clear();
                    if(!tags.isEmpty())
                    {
                        Map<String, Object> map = null;
                        for(int i = 0; i < tags.size(); i++)
                        {
                            if(!tags.get(i).isEmpty())
                            {
                                map = new HashMap<String, Object>();
                                map.put("tag_name", tags.get(i));
                                tagList.add(map);
                            }
                        }
                        pictureAdapter.notifyDataSetChanged();
                    }
                }
                else
                {
                    Toast.makeText(getContext(), "something wrong",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("s", t.toString());
                Toast.makeText(getContext(), "网络错误，请刷新",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    public Bitmap getImageBitmap(String url) {
        URL imgUrl = null;
        Bitmap bitmap = null;
        try {
            imgUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) imgUrl
                    .openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.me_fragment, container, false);
        setHasOptionsMenu(true);

        view = rootView;
        usernameText = view.findViewById(R.id.setting_username);
        realNameText = view.findViewById(R.id.realNameText);
        ageText = view.findViewById(R.id.ageText);
        gradeText = view.findViewById(R.id.gradeText);
        majorText = view.findViewById(R.id.majorText);
        mailText = view.findViewById(R.id.mailText);
        phoneText = view.findViewById(R.id.phoneText);
        oldPasswordText = view.findViewById(R.id.oldPasswordText);
        newPasswordText = view.findViewById(R.id.newPasswordText);
        pass = view.findViewById(R.id.pass);
        header = view.findViewById(R.id.headerPic);
        sexImage = view.findViewById(R.id.sexImage);
        logOut = view.findViewById(R.id.log_out);
        balanceText = view.findViewById(R.id.balanceText);
        creditValueText = view.findViewById(R.id.creditValueText);
        studentIdText = view.findViewById(R.id.studentIdText);
        ImageName = "";
        tagsLayout = view.findViewById(R.id.tagsLayout);
        tag_label = view.findViewById(R.id.label_text);
        tag_label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

        tagsLayout.setClickable(false);
        tagList = new ArrayList<>();
        pictureAdapter = new SimpleAdapter(getContext(), tagList,
                R.layout.tag_item_layout, from, to);

        tagsLayout.setAdapter(pictureAdapter);


        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_shared_preference = getActivity().getSharedPreferences("user", 0);
                SharedPreferences.Editor editor = user_shared_preference.edit();
                editor.putString("token","");
                editor.putString("username","");
                editor.putInt("userId",-1);
                editor.putBoolean("had_user",false);
                editor.commit();
                Intent intent = new Intent(getActivity(),LoginRegisterActivity.class);
                startActivity(intent);
            }
        });

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

        initData();
        modifyStatus = false;
        MainPartActivity activity = (MainPartActivity)getContext();
        activity.headerUri = null;

        tag_label.setClickable(false);
        tag_label.setText("我的标签: ");
        usernameText.setEnabled(false);
        realNameText.setEnabled(false);
        ageText.setEnabled(false);
        gradeText.setEnabled(false);
        majorText.setEnabled(false);
        mailText.setEnabled(false);
        phoneText.setEnabled(false);

        header.setEnabled(false);
        sexImage.setEnabled(false);

        pass.setVisibility(View.GONE);
        oldPasswordText.setVisibility(View.GONE);
        newPasswordText.setVisibility(View.GONE);
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.me_menu, menu);
        mymenu = menu;
        super.onCreateOptionsMenu(menu,inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.modifyBtn:
                if(!modifyStatus)
                {
                    modifyInfo();
                    item.setIcon(R.mipmap.tick);
                    modifyStatus=true;
                    MenuItem item2 = mymenu.findItem(R.id.cancelBtn);

                    item2.setVisible(true);
                    item2.setEnabled(true);
                }
                else
                {
                    if(finishModify())
                    {
                        item.setIcon(R.mipmap.modify);
                        modifyStatus=false;
                        MenuItem item2 = mymenu.findItem(R.id.cancelBtn);
                        item2.setVisible(false);
                        item2.setEnabled(false);
                    }

                }
                break;
            case R.id.cancelBtn:
                if(modifyStatus)
                {
                    modifyStatus = false;

                    MainPartActivity activity = (MainPartActivity)getContext();
                    activity.headerUri = null;

                    initData();

                    MenuItem item2 = mymenu.findItem(R.id.modifyBtn);
                    item2.setIcon(R.mipmap.modify);
                    item.setVisible(false);
                    item.setEnabled(false);

                    cancelModifyInfo();
                }
                break;
            case R.id.freshBtn:
                initData();
                Toast.makeText(getContext(), "刷新成功", Toast.LENGTH_SHORT).show();
            default:
                break;
        }
        return true;
    }

    public void modifyInfo()
    {
        Toast.makeText(getActivity(), "点击对应信息进行修改", Toast.LENGTH_SHORT).show();

        tag_label.setClickable(true);
        tag_label.setText("点我修改标签");
        usernameText.setEnabled(true);
        realNameText.setEnabled(true);
        ageText.setEnabled(true);
        gradeText.setEnabled(true);
        majorText.setEnabled(true);
        mailText.setEnabled(true);
        phoneText.setEnabled(true);

        header.setEnabled(true);
        sexImage.setEnabled(true);

        pass.setVisibility(View.VISIBLE);
        oldPasswordText.setVisibility(View.VISIBLE);
        newPasswordText.setVisibility(View.VISIBLE);
    }

    public void cancelModifyInfo()
    {
        modifyStatus = false;
        str = "";
        //Toast.makeText(getActivity(), "取消修改成功", Toast.LENGTH_SHORT).show();

        tag_label.setClickable(false);
        tag_label.setText("我的标签: ");
        usernameText.setEnabled(false);
        realNameText.setEnabled(false);
        ageText.setEnabled(false);
        gradeText.setEnabled(false);
        majorText.setEnabled(false);
        mailText.setEnabled(false);
        phoneText.setEnabled(false);

        header.setEnabled(false);
        sexImage.setEnabled(false);

        pass.setVisibility(View.GONE);
        oldPasswordText.setVisibility(View.GONE);
        newPasswordText.setVisibility(View.GONE);
    }

    public boolean finishModify()
    {
        if(oldPasswordText.getText().toString().isEmpty() || newPasswordText.getText().toString().isEmpty())
        {
            Toast.makeText(getContext(), "请输入密码后确认修改",Toast.LENGTH_SHORT).show();
            return false;
        }

        try
        {
            int a = Integer.valueOf(gradeText.getText().toString());
            int b = Integer.valueOf(ageText.getText().toString());
        }
        catch (Exception E)
        {
            Toast.makeText(getContext(), "年龄或年级必须为正整数", Toast.LENGTH_SHORT).show();
            return false;
        }

        tag_label.setClickable(false);
        tag_label.setText("我的标签：");
        usernameText.setEnabled(false);
        realNameText.setEnabled(false);
        ageText.setEnabled(false);
        gradeText.setEnabled(false);
        majorText.setEnabled(false);
        mailText.setEnabled(false);
        phoneText.setEnabled(false);

        header.setEnabled(false);
        sexImage.setEnabled(false);

        pass.setVisibility(View.GONE);
        oldPasswordText.setVisibility(View.GONE);
        newPasswordText.setVisibility(View.GONE);

        tagsLayout.removeAllViewsInLayout();
        List<String> tags = Util.decodeTags(str);
        Log.e("tagsize", str + tags.size());
        tagList.clear();
        Log.e("tagsize2", str + tags.size());
        if(!tags.isEmpty())
        {
            Map<String, Object> map = null;
            for(int i = 0; i < tags.size(); i++)
            {
                if(!tags.get(i).isEmpty())
                {
                    map = new HashMap<String, Object>();
                    map.put("tag_name", tags.get(i));
                    tagList.add(map);
                }
            }
            pictureAdapter.notifyDataSetChanged();
        }


        MainPartActivity activity = (MainPartActivity)getContext();
        if(activity.headerUri != null)
        {
            String filePath = activity.headerUri.getPath();


            File file = new File(filePath);
            // 创建 RequestBody，用于封装构建RequestBody
            // RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpg"), file);

            // MultipartBody.Part  和后端约定好Key，这里的partName是用file
            MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);

            // 添加描述
            String descriptionString = "hello, 这是文件描述";
            RequestBody description = RequestBody.create(MediaType.parse("multipart/form-data"), descriptionString);

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

            service myservice = retrofit.create(service.class);
            // 执行请求
            myservice.upload_pic(description, body).enqueue(new Callback<Image>() {
                @Override
                public void onResponse(Call<Image> call, Response<Image> response) {
                    if(response.code() == 201)
                    {
                        Image image = response.body();
                        Log.e("name", image.getImageName());
                        update_to_server(image.getImageName());
                    }
                    else
                    {
                        Log.e("error", response.raw().toString());
                    }
                }
                @Override
                public void onFailure(Call<Image> call, Throwable t) {
                    Log.e("error", t.toString());
                }
            });
        }
        else
        {
            update_to_server(ImageName);
        }
        return true;
    }

    private void update_to_server(String imageName) {
        ImageName = imageName;
        MainPartActivity activity = (MainPartActivity)getContext();

        User user = new User(Util.getUserId(getContext()), 0, realNameText.getText().toString(), imageName,
                             usernameText.getText().toString(), Integer.valueOf(ageText.getText().toString()),
                             activity.sex, Integer.valueOf(gradeText.getText().toString()), majorText.getText().toString(),
                             mailText.getText().toString(), phoneText.getText().toString(), studentIdText.getText().toString(),
                             Float.valueOf(balanceText.getText().toString()), str, newPasswordText.getText().toString(),
                             creditValueText.getText().toString());
        Gson gson = new Gson();
        String jsonBody = gson.toJson(user);
        RequestBody reqBody = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=utf-8"),jsonBody);
        Call<Msg> putCall =  myservice.modify_user(Util.getToken(getContext()), Util.getUserId(getContext()), oldPasswordText.getText().toString(),reqBody); //todo
        putCall.enqueue(new Callback<Msg>() {
            @Override
            public void onResponse(Call<Msg> call, Response<Msg> response) {

                if(response.code() == 200)
                {
                    Toast.makeText(getContext(), "修改成功",
                            Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getContext(), "修改失败",
                            Toast.LENGTH_SHORT).show();
                    Log.e("modifyPersonalInfo:", response.raw().toString());
                }

            }

            @Override
            public void onFailure(Call<Msg> call, Throwable t) {
                Log.e("modifyPersonalInfo:", t.toString());
                Toast.makeText(getContext(), "修改失败",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Handler handler;
    private String str = "";
    private Button choice1;
    private Button choice2;
    private Button choice3;
    private Button choice4;
    private Button continue_button;
    private TextView title;

    public String showDialog(){
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
                    choice1.setBackgroundColor(getResources().getColor(R.color.blue));
                    String s = choice1.getText().toString() + "、";
                    if(str.indexOf(s) == -1){
                        str += s;
                    }

                }
                if(msg.what == 2){
                    choice2.setBackgroundColor(getResources().getColor(R.color.blue));
                    String s = choice2.getText().toString() + "、";
                    if(str.indexOf(s) == -1){
                        str += s;
                    }
                }
                if(msg.what == 3){
                    choice3.setBackgroundColor(getResources().getColor(R.color.blue));
                    String s = choice3.getText().toString() + "、";
                    if(str.indexOf(s) == -1){
                        str += s;
                    }
                }
                if(msg.what == 4)
                {
                    choice4.setBackgroundColor(getResources().getColor(R.color.blue));
                    String s = choice4.getText().toString() + "、";
                    if(str.indexOf(s) == -1){
                        str += s;
                    }
                }
                if(msg.what == 5){
                    choice1.setBackgroundColor(getResources().getColor(R.color.gray));
                    str = str.replaceAll(choice1.getText().toString()+"、","");
                }
                if(msg.what == 6){
                    choice2.setBackgroundColor(getResources().getColor(R.color.gray));
                    str = str.replaceAll(choice2.getText().toString()+"、","");
                }
                if(msg.what == 7){
                    choice3.setBackgroundColor(getResources().getColor(R.color.gray));
                    str = str.replaceAll(choice3.getText().toString()+"、","");
                }
                if(msg.what == 8){
                    choice4.setBackgroundColor(getResources().getColor(R.color.gray));
                    str = str.replaceAll(choice4.getText().toString()+"、","");
                }
                if(msg.what == 9){
                    title.setText("专业");
                    choice1.setText("IT");
                    choice2.setText("经管");
                    choice3.setText("物化生医");
                    choice4.setText("文史哲");
                    choice1.setBackgroundColor(getResources().getColor(R.color.gray));
                    choice2.setBackgroundColor(getResources().getColor(R.color.gray));
                    choice3.setBackgroundColor(getResources().getColor(R.color.gray));
                    choice4.setBackgroundColor(getResources().getColor(R.color.gray));
                }
                if(msg.what == 10){
                    title.setText("个人特性");
                    choice1.setText("热情开朗");
                    choice2.setText("文艺青年");
                    choice3.setText("忧郁小王子");
                    choice4.setText("沉着冷静");
                    choice1.setBackgroundColor(getResources().getColor(R.color.gray));
                    choice2.setBackgroundColor(getResources().getColor(R.color.gray));
                    choice3.setBackgroundColor(getResources().getColor(R.color.gray));
                    choice4.setBackgroundColor(getResources().getColor(R.color.gray));
                }
                if(msg.what == 11){
                    title.setText("爱好");
                    choice1.setText("体育运动");
                    choice2.setText("音乐绘画");
                    choice3.setText("二次元");
                    choice4.setText("影视书籍");
                    choice1.setBackgroundColor(getResources().getColor(R.color.gray));
                    choice2.setBackgroundColor(getResources().getColor(R.color.gray));
                    choice3.setBackgroundColor(getResources().getColor(R.color.gray));
                    choice4.setBackgroundColor(getResources().getColor(R.color.gray));
                }
                if(msg.what == 12){
                    dialog.dismiss();
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
        return str;
    }

    @SuppressLint("ResourceType")
    private void setRadioBtnAttribute(final TextView textView, String btnContent, int id ){
        if( null ==textView ){
            return;
        }
        textView.setId( id );
        textView.setText( btnContent );

        textView.setGravity( Gravity.CENTER );

        LinearLayout.LayoutParams rlp = new LinearLayout.LayoutParams( LinearLayout.LayoutParams.WRAP_CONTENT , LinearLayout.LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams( rlp );
    }
}
