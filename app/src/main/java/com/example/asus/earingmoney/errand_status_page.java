package com.example.asus.earingmoney;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class errand_status_page extends AppCompatActivity {

    private Toolbar.OnMenuItemClickListener onMenuItemClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_errand_status_page);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("任务详情");
        setSupportActionBar(toolbar);

        onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                String msg = "";
                switch (menuItem.getItemId()) {
                    case R.id.shensu:
                        msg += "您的申诉已受理";
                        break;
                }
                if(!msg.equals("")) {
                    Toast.makeText(errand_status_page.this, msg, Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        };

        toolbar.setNavigationIcon(R.mipmap.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.setOnMenuItemClickListener(onMenuItemClick);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.errand_page_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

}
