package com.example.asus.earingmoney.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.view.ActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.Toast;

import com.example.asus.earingmoney.LoginRegisterActivity;
import com.example.asus.earingmoney.MainPartActivity;
import com.example.asus.earingmoney.R;
import com.example.asus.earingmoney.ServiceFactory;
import com.example.asus.earingmoney.Util.Util;
import com.example.asus.earingmoney.model.RequestCommitModel;
import com.example.asus.earingmoney.service;
import com.google.gson.Gson;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ElipsisActionProvider extends ActionProvider {
    /**
     * Creates a new instance.
     *
     * @param context Context for accessing resources.
     */
    private Context context;
    private service myservice;
    public ServiceFactory serviceFactory;

    public ElipsisActionProvider(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public View onCreateActionView() {
        return null;
    }

    @Override
    public void onPrepareSubMenu(SubMenu subMenu) {
        serviceFactory = new ServiceFactory();
        myservice = serviceFactory.CreatService();

        subMenu.clear();
        subMenu.add(("分享"))
                .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        return true;
                    }
                });
        subMenu.add(("举报"))
                .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        final AlertDialog.Builder normalDialog = new AlertDialog.Builder(getContext());
                        final View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_report,null);
                        normalDialog.setTitle("请输入举报内容");
                        normalDialog.setView(dialogView);
                        normalDialog.setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        System.out.println(Util.getMissionId(getContext()));
                                        RequestCommitModel model = new RequestCommitModel();
                                        model.setMissionId(Util.getMissionId(getContext()));
                                        Gson gson=new Gson();
                                        final String jsonBody = gson.toJson(model);
                                        RequestBody reqBody = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=utf-8"),jsonBody);
                                        Observer<Response<ResponseBody>> observer = new Observer<Response<ResponseBody>>() {
                                            @Override
                                            public void onNext(Response<ResponseBody> r) {
                                                System.out.println(r.code());
                                                if(r.code() == 200){
                                                    Toast.makeText(getContext().getApplicationContext(), "举报成功", Toast.LENGTH_SHORT).show();
                                                }
                                                else {
                                                    Toast.makeText(getContext().getApplicationContext(), "举报失败", Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            @Override
                                            public void onCompleted() {

                                            }

                                            @Override
                                            public void onError(Throwable e) {
                                                Toast.makeText(getContext().getApplicationContext(), "举报失败", Toast.LENGTH_SHORT).show();
                                            }
                                        };
                                        myservice.commitReport(Util.getToken(getContext()),Util.getMissionId(getContext()),reqBody)
                                                .subscribeOn(Schedulers.newThread())
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribe(observer);
                                    }
                                });
                        normalDialog.setNegativeButton("取消",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //...To-do
                                    }
                                });
                        // 显示
                        normalDialog.show();
                        return true;
                    }
                });
    }

    @Override
    public boolean hasSubMenu() {
        return true;
    }
}
