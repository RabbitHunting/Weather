package com.wbl.weather.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.wbl.weather.model.CityHourlyWeather;
import com.wbl.weather.model.CityNowWeather;
import com.wbl.weather.repository.CityWeatherRepository;

import java.lang.invoke.MutableCallSite;

public class WeatherViewModel extends BaseViewModel {
    // TODO: Implement the ViewModel
    public LiveData<CityNowWeather> cityNowWeather;
    public LiveData<CityHourlyWeather> cityHourlyWeather;
    public String cityname;

    public void  getNowWeather(String name) {
        cityname = name;
        CityWeatherRepository cityWeatherRepository = new CityWeatherRepository();
        cityNowWeather = cityWeatherRepository.getCityNowWeather(name);
    }
    public void getHourlyWeather(String name) {
        CityWeatherRepository cityWeatherRepository = new CityWeatherRepository();
        cityHourlyWeather = cityWeatherRepository.getCityHourlyWeather(name);
    }

}