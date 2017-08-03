package com.example.administrator.dilixunbao;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;

/**
 * Created by Administrator on 2017/8/2.
 */

public class TreasureAppLication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //一般在此处进行初始化操作
        UserPrefs.init(getApplicationContext());
        //百度sdk初始化
        SDKInitializer.initialize(getApplicationContext());
    }
}
