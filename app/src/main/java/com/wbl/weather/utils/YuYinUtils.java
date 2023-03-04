package com.wbl.weather.utils;

public class YuYinUtils {
    public String yuyin() {
        String text = MVUtils.getString("cityNowY")+MVUtils.getString("cityNowA");
        return text;
    }
}
