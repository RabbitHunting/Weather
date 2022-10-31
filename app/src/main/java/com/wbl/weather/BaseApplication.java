package com.wbl.weather;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.tencent.mmkv.MMKV;
import com.tencent.smtt.export.external.TbsCoreSettings;
import com.tencent.smtt.sdk.QbSdk;
import com.wbl.weather.db.AppDatabase;
import com.wbl.weather.network.NetworkApi;
import com.wbl.weather.ui.activity.ActivityManager;
import com.wbl.weather.utils.MVUtils;

import java.util.HashMap;

/**
 * 自定义 Application
 * @author llw
 */
public class BaseApplication extends Application {

    @SuppressLint("StaticFieldLeak")
    public static Context context;

    //数据库
    public static AppDatabase db;

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化
        NetworkApi.init(new NetworkRequiredInfo(this));
        context = getApplicationContext();
        //MMkv初始化
        MMKV.initialize(this);
        //工具类初始化
        MVUtils.getInstance();
        //创建本地数据库
        db = AppDatabase.getInstance(this);

    }

    public static Context getContext() {
        return context;
    }

    public static AppDatabase getDb(){
        return db;
    }

    public static ActivityManager getActivityManager() {
        return ActivityManager.getInstance();
    }
}
