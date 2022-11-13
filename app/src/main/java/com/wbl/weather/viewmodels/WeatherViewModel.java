package com.wbl.weather.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.wbl.weather.model.CityDailyResponse;
import com.wbl.weather.model.CityHourlyWeather;
import com.wbl.weather.model.CityNowWeather;
import com.wbl.weather.repository.CityWeatherRepository;

import java.lang.invoke.MutableCallSite;

public class WeatherViewModel extends BaseViewModel {
    // TODO: Implement the ViewModel
    public LiveData<CityNowWeather> cityNowWeather;
    public LiveData<CityHourlyWeather> cityHourlyWeather;
    public LiveData<CityDailyResponse> cityDailyResponse;
    public String cityname;
    CityWeatherRepository cityWeatherRepository = new CityWeatherRepository();

    public void  getNowWeather(String name) {
        cityname = name;
        cityNowWeather = cityWeatherRepository.getCityNowWeather(name);
    }
    public void getHourlyWeather(String name) {
        cityHourlyWeather = cityWeatherRepository.getCityHourlyWeather(name);
    }

    public void getDailyWeather(String name){
        cityDailyResponse = cityWeatherRepository.getCityDailyResponse(name);
    }

}