package com.wbl.weather.utils;

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
}
