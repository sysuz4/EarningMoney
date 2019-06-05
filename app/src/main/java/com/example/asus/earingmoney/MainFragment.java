package com.example.asus.earingmoney;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.clj.memoryspinner.MemorySpinner;

import java.util.ArrayList;
import java.util.Arrays;

public class MainFragment extends Fragment {
    private static final String ARG_SHOW_TEXT = "text";

    private String mContentText;


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
        if (getArguments() != null) {
            mContentText = getArguments().getString(ARG_SHOW_TEXT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.main_fragment, container, false);
        setHasOptionsMenu(true);

        MemorySpinner ms1 = rootView.findViewById(R.id.ms1);
        ArrayList<String> list = new ArrayList<>(Arrays.asList("跑腿任务", "问卷任务"));
        ms1.setMemoryCount(1);
        ms1.setData(null, list);
//        TextView contentTv = rootView.findViewById(R.id.content_tv);
//        contentTv.setText(mContentText);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.main_menu, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

}
