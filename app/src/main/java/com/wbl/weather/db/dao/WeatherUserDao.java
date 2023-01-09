package com.wbl.weather.db.dao;

import com.wbl.weather.db.bean.WeatherUser;

public interface WeatherUserDao {

    public boolean login(String username,String password);
    public boolean register(WeatherUser weatherUser);
    public WeatherUser findUser(String name);
}
