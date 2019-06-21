package com.example.asus.earingmoney;

import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class creat_errand_activity extends AppCompatActivity implements View.OnLongClickListener{
    private int currentBtn = 0;

    private EditText titleText;
    private EditText descripText;
    public String finishDate;
    public Float money;
    public int taskNum;

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
        finishDate = "";
        money = -1.f;
        taskNum = -1;
    }

    public void okFab_click(android.view.View view) {
        if(titleText.getText().toString().isEmpty())
            Toast.makeText(this, "标题不能为空", Toast.LENGTH_SHORT).show();
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
}
