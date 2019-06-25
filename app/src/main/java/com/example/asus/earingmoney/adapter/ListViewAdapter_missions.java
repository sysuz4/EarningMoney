package com.example.asus.earingmoney.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.asus.earingmoney.R;
import com.example.asus.earingmoney.model.Mission;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ListViewAdapter_missions extends ArrayAdapter<Mission> {
    private int resourceId;

    public ListViewAdapter_missions(Context context, int textViewResourceId, List<Mission> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Mission contact = getItem(position );
        View view;
        if (convertView == null) {
            view= LayoutInflater.from(getContext()).inflate(resourceId, null);
        } else {
            view = convertView;
        }
        final ImageView image = view.findViewById(R.id.image);

        //判断是跑腿任务还是问卷任务，来选择显示图标
        if(contact.getTaskType() == 0){
            image.setImageResource(R.mipmap.errand);
        }
        else {
            image.setImageResource(R.mipmap.questionare);
        }

//        final Handler handler = new Handler(){
//            @Override
//            public void handleMessage(Message msg) {
//                if (msg.what == 0)
//                    image.setImageResource(R.mipmap.me);
//                if (msg.what == 1)
//                    image.setImageBitmap((Bitmap) msg.obj);
//            }
//        };
//
//        new Thread(new Runnable() {
//            Bitmap bitmap = null;
//            @Override
//            public void run() {
//                URL url = null;
//                //System.out.println(contact.getAvator());
//                if(contact.getAvator() == null || contact.getAvator().equals("")){
//                    Message msg = new Message();
//                    msg.what = 0;
//                    handler.sendMessage(msg);
//                }
//                else {
//                    try {
//                        url = new URL(contact.getAvator());
//                        InputStream is = null;
//                        BufferedInputStream bis = null;
//                        try {
//                            is = url.openConnection().getInputStream();
//                            bis = new BufferedInputStream(is);
//                            bitmap = BitmapFactory.decodeStream(bis);
//                            Message msg = new Message();
//                            msg.what = 1;
//                            msg.obj = bitmap;
//                            handler.sendMessage(msg);
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    } catch (MalformedURLException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }).start();

        TextView title = view.findViewById(R.id.title);
        title.setText(contact.getTitle());
        TextView time = view.findViewById(R.id.time);
        time.setText(contact.getPublishTime());
        TextView detail = view.findViewById(R.id.detail);
        detail.setText(contact.getDescription());
        return view;
    }
}
