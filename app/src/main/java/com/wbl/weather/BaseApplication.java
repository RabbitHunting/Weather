package com.wbl.weather;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.iflytek.cloud.SpeechUtility;
import com.tencent.mmkv.MMKV;
import com.tencent.smtt.export.external.TbsCoreSettings;
import com.tencent.smtt.sdk.QbSdk;
import com.wbl.weather.db.AppDatabase;
import com.wbl.weather.network.NetworkApi;
import com.wbl.weather.ui.activity.ActivityManager;
import com.wbl.weather.utils.MVUtils;
import com.iflytek.cloud.SpeechConstant;
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
        //腾讯WebView初始化
        initX5WebView();
        //语音初始化
        SpeechUtility.createUtility(BaseApplication.this, SpeechConstant.APPID +"=d8f7fb4f");

    }

    public static Context getContext() {
        return context;
    }

    public static AppDatabase getDb(){
        return db;
    }

    private void initX5WebView() {
        HashMap map = new HashMap(2);
        map.put(TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER,true);
        map.put(TbsCoreSettings.TBS_SETTINGS_USE_DEXLOADER_SERVICE,true);
        QbSdk.initTbsSettings(map);
        //收集本地tbs内核信息并上报服务器，服务器返回结果决定用那个内核
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
            @Override
            public void onCoreInitFinished() {

            }

            @Override
            public void onViewInitFinished(boolean b) {
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.d("app", " onViewInitFinished is " + b);
            }
        };
        QbSdk.initX5Environment(getApplicationContext(),cb);
    }
    public static ActivityManager getActivityManager() {
        return ActivityManager.getInstance();
    }
}
