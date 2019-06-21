package com.example.asus.earingmoney;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.earingmoney.Util.Constants;
import com.example.asus.earingmoney.Util.Util;
import com.example.asus.earingmoney.model.User;
import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
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
    EditText balanceText;
    EditText creditValueText;
    EditText studentIdText;
    TextView pass;
    ImageView sexImage;
    CircleImageView header;
    Button logOut;
    SharedPreferences user_shared_preference;
    private service myservice;

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
        Call<User> getCall =  myservice.get_user(Util.getToken(getContext()), userID); //todo
        getCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User user = response.body();
                if(response.code() == 200)
                {
                    Toast.makeText(getContext(), "200",
                            Toast.LENGTH_SHORT).show();

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
                    MainPartActivity activity = (MainPartActivity)getContext();
                    if(user.getSex() == Constants.MALE){
                        sexImage.setImageResource(R.mipmap.boy);
                        activity.sex = Constants.MALE;
                    }
                    else
                    {
                        sexImage.setImageResource(R.mipmap.girl);
                        activity.sex = Constants.FEMALE;
                    }
                    //todo avator image
                }
                else if(response.code() == 401){
                    Toast.makeText(getContext(), "401",
                            Toast.LENGTH_SHORT).show();
                }
                else if(response.code() == 404){
                    Toast.makeText(getContext(), "404",
                            Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getContext(), String.valueOf(response.code()),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("s", t.toString());
                Toast.makeText(getContext(), "fail",
                        Toast.LENGTH_SHORT).show();
            }
        });
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

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_shared_preference = getActivity().getSharedPreferences("user", 0);
                SharedPreferences.Editor editor = user_shared_preference.edit();
                editor.putString("token","");
                editor.putString("username","");
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

        finishModify();
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.me_menu, menu);
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
                }
                else
                {
                    finishModify();
                    item.setIcon(R.mipmap.modify);
                    modifyStatus=false;
                }
                break;
            default:
                break;
        }
        return true;
    }

    public void modifyInfo()
    {
        Toast.makeText(getActivity(), "点击对应信息进行修改", Toast.LENGTH_SHORT).show();

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

    public void finishModify()
    {
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

        update_to_server();
    }

    private void update_to_server() {

        MainPartActivity activity = (MainPartActivity)getContext();

        User user = new User(userID, 0, realNameText.getText().toString(), "",
                             usernameText.getText().toString(), Integer.valueOf(ageText.getText().toString()),
                             activity.sex, Integer.valueOf(gradeText.getText().toString()), majorText.getText().toString(),
                             mailText.getText().toString(), phoneText.getText().toString(), studentIdText.getText().toString(),
                             Float.valueOf(balanceText.getText().toString()), "", newPasswordText.getText().toString(),
                             creditValueText.getText().toString());
        Gson gson = new Gson();
        String jsonBody = gson.toJson(user);
        RequestBody reqBody = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=utf-8"),jsonBody);
        Call<String> putCall =  myservice.modify_user(Util.getToken(getContext()), userID, reqBody); //todo
        putCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.code() == 200)
                {
                    Toast.makeText(getContext(), "200",
                            Toast.LENGTH_SHORT).show();


                }
                else if(response.code() == 401){
                    Toast.makeText(getContext(), "401",
                            Toast.LENGTH_SHORT).show();
                }
                else if(response.code() == 404){
                    Toast.makeText(getContext(), "404",
                            Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getContext(), String.valueOf(response.code()),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("s", t.toString());
                Toast.makeText(getContext(), "fail",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

}
