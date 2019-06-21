package com.example.asus.earingmoney;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import de.hdodenhof.circleimageview.CircleImageView;

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
    TextView pass;
    ImageView sexImage;
    CircleImageView header;
    Button logOut;
    SharedPreferences user_shared_preference;

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
    }

}
