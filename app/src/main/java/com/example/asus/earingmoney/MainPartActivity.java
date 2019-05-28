package com.example.asus.earingmoney;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;

public class MainPartActivity extends AppCompatActivity {

    private ViewPager fragment_vp;
    private RadioGroup tabs_rg;
    private List<Fragment> fragments;
    private FragmentPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_part);

        fragment_vp = findViewById(R.id.fragment_vp);
        tabs_rg = findViewById(R.id.tabs_rg);

        fragments = new ArrayList<>(3);
        fragments.add(MainFragment.newInstance("首页"));
        fragments.add(TasksFragment.newInstance("任务"));
        fragments.add(MeFragment.newInstance("我的"));

        adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), fragments);
        fragment_vp.setAdapter(adapter);

        fragment_vp.addOnPageChangeListener(mPageChangeListener);
        tabs_rg.setOnCheckedChangeListener(mOnCheckedChangeListener);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        fragment_vp.removeOnPageChangeListener(mPageChangeListener);
    }

    private ViewPager.OnPageChangeListener mPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            RadioButton radioButton = (RadioButton) tabs_rg.getChildAt(position);
            radioButton.setChecked(true);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private RadioGroup.OnCheckedChangeListener mOnCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            for (int i = 0; i < group.getChildCount(); i++) {
                if (group.getChildAt(i).getId() == checkedId) {
                    fragment_vp.setCurrentItem(i);
                    return;
                }
            }
        }
    };

    private class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> mList;

        public MyFragmentPagerAdapter(FragmentManager fm, List<Fragment> list) {
            super(fm);
            this.mList = list;
        }

        @Override
        public Fragment getItem(int position) {
            return this.mList == null ? null : this.mList.get(position);
        }

        @Override
        public int getCount() {
            return this.mList == null ? 0 : this.mList.size();
        }
    }

}
