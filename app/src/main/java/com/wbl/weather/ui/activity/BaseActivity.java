package com.wbl.weather.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.qweather.sdk.view.HeConfig;
import com.wbl.weather.BaseApplication;
import com.wbl.weather.utils.PermissionUtils;

public class BaseActivity extends AppCompatActivity {
    protected AppCompatActivity context;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.context = this;
        BaseApplication.getActivityManager().addActivity(this);
        PermissionUtils.getInstance();
        HeConfig.init("HE2211021543471533", "fe29bde9a4af40658e0c237afe51dd99");
        HeConfig.switchToBizService();
        requestPermissions();
    }

    protected void showMsg(CharSequence msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    protected void showLongMsg(CharSequence msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }



    /**
     * 跳转页面
     * clazz目标页面
     */
    protected void jumpActivity(final Class<?> clazz) {
        startActivity(new Intent(context, clazz));
    }

    /**
     * 跳转页面并关闭当前页面
     */
    protected void jumpActivityFinish(final Class<?> clazz) {
        startActivity(new Intent(context, clazz));
        finish();
    }

    protected void back(Toolbar toolbar) {
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    protected void backAndFinish(Toolbar toolbar) {
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    /**
     * 状态栏文字图标颜色
     * dark 深色 false浅色
     */
    protected void setStatuBar(boolean dark) {
        View decor = getWindow().getDecorView();
        if (dark) {
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else {
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    }

    protected void exitTheProgram() {
        BaseApplication.getActivityManager().finishAllActivity();
    }

    /**
     * 当前是否在Android11.0及以上
     */
    protected boolean isAndroid11() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.R;
    }

    /**
     * 当前是否在Android10.0及以上
     */
    protected boolean isAndroid10() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q;
    }

    /**
     * 当前是否在Android7.0及以上
     */
    protected boolean isAndroid7() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N;
    }

    /**
     * 当前是否在Android6.0及以上
     */
    protected boolean isAndroid6() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }


    @RequiresApi(api = Build.VERSION_CODES.R)
    protected boolean isStorageManager() {
        return Environment.isExternalStorageManager();
    }

    protected boolean hasPermission(String permissionName) {
        return PermissionUtils.hasPermission(this, permissionName);
    }

    protected void requestPermission(String permissionName) {
        PermissionUtils.requestPermission(this, permissionName);
    }

    /**
     * 请求外部存储管理 Android11版本时获取文件读写权限时调用
     */
    protected void requestManageExternalStorage() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivityForResult(intent, PermissionUtils.REQUEST_MANAGE_EXTERNAL_STORAGE_CODE);
    }

    /**
     * 请求权限
     */
    private void requestPermissions() {
        try {
            //Android6.0及以上版本
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                int permission = ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (permission != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]
                            {Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.WRITE_SETTINGS,
                                    Manifest.permission.READ_EXTERNAL_STORAGE}, 0x0010);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 权限请求返回结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}

