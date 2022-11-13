package com.wbl.weather.utils;

import android.util.Log;

public class JudgmentWeather {

    public static String pd(String text1, String text2) {
        String text = null;
        if (text1.equals(text2)) {
            text = text1;
        } else {
            text = text1 + "转" + text2;
        }
        return text;
    }

    public static String kqzl(String text1, String text2) {
        String text = "null";
        if (text1 != null) {
            if (text1.equals("优")) {
                text = "空气质量好！基本没有空气污染。";
            } else {
                text = "空气中主要污染物是：" + text2;
            }
        }

        return text;
    }
}
