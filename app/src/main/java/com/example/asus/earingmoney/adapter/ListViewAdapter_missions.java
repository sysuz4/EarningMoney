package com.example.asus.earingmoney.adapter;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.asus.earingmoney.model.Mission;

import java.util.List;

public class ListViewAdapter_missions extends ArrayAdapter<Mission> {
    private int resourceId;
    public ImageView image;

    public ListViewAdapter_missions(Context context, int textViewResourceId, List<Mission> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }


//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        final Mission contact = getItem(position);
//        View view;
//        if (convertView == null) {
//            view= LayoutInflater.from(getContext()).inflate(resourceId, null);
//        } else {
//            view = convertView;
//        }
//        TextView title = view.findViewById(R.id.title);
//        title.setText(contact.getUser().getLogin());
//
//        image = view.findViewById(R.id.item_image);
//        new Thread(new Runnable() {
//            Bitmap bitmap = null;
//            @Override
//            public void run() {
//                URL url = null;
//                try {
//                    url = new URL(contact.getUser().getUrl());
//                    InputStream is = null;
//                    BufferedInputStream bis = null;
//                    try {
//                        is = url.openConnection().getInputStream();
//                        bis = new BufferedInputStream(is);
//                        bitmap = BitmapFactory.decodeStream(bis);
//                        Message msg = new Message();
//                        msg.obj = bitmap;
//                        handler.sendMessage(msg);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                } catch (MalformedURLException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//
//        TextView created_at = view.findViewById(R.id.item_date);
//        created_at.setText(contact.getCreated_at());
//        TextView body = view.findViewById(R.id.item_comment);
//        body.setText(contact.getBody());
//        return view;
//    }
//
//    Handler handler = new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            image.setImageBitmap((Bitmap) msg.obj);
//        }
//    };
}
