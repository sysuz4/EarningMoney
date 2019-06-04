package com.example.asus.earingmoney;

import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainPartActivity extends AppCompatActivity {

    private ViewPager fragment_vp;
    private RadioGroup tabs_rg;
    private List<Fragment> fragments;
    private FragmentPagerAdapter adapter;
    private Toolbar toolbar;
    private SearchView mSearchView;

    //获取服务端数据时要进行初始化
    public Uri headerUri = null;
    public int sex = 0;

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

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
                    if(headerUri != null)
                    {
                        CircleImageView btn = findViewById(R.id.headerPic);
                        if(btn == null)
                        {
                            return;
                        }
                        btn.setImageURI(headerUri);
                    }
                    if(sex == 1)
                    {
                        ImageView sexImage = findViewById(R.id.sexImage);
                        if(sexImage == null)
                            return;
                        sexImage.setImageResource(R.mipmap.girl);
                    }
                    else
                    {
                        ImageView sexImage = findViewById(R.id.sexImage);
                        if(sexImage == null)
                            return;
                        sexImage.setImageResource(R.mipmap.boy);
                    }
                    return;
                }
            }
        }
    };

    public void changeHeader(View view) {
        android.content.Intent intent = new android.content.Intent();
        intent.setAction(android.content.Intent.ACTION_PICK);
        intent.setType("image/*");
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
                CircleImageView btn = findViewById(R.id.headerPic);
                btn.setImageURI(image_file_uri);
                headerUri = image_file_uri;
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

    public void changeSex(View view) {
        ImageView sexImage = findViewById(R.id.sexImage);
        if(sex == 0)
        {
            sex = 1;
            sexImage.setImageResource(R.mipmap.girl);
        }
        else
        {
            sex = 0;
            sexImage.setImageResource(R.mipmap.boy);
        }
    }

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

}
