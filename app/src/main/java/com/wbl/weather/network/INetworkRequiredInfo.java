package com.wbl.weather.network;

import android.app.Application;

public interface INetworkRequiredInfo {
    /**
     * 获取APP版本名
     */
    String getAppVersionName();

    /**
     * 获取APP版本号
     */
    String getAppVersionCode();

    /**
     * 判断是否时DeBug模式
     */
    boolean isDebug();

    /**
     * 获取全文上下文参数
     */
    Application getApplicationContext();
}
