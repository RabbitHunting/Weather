package com.wbl.weather.repository;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.wbl.weather.api.ApiService;
import com.wbl.weather.model.CityIdResponse;
import com.wbl.weather.model.CityNowWeather;
import com.wbl.weather.network.BaseObserver;
import com.wbl.weather.network.NetworkApi;
import com.wbl.weather.network.utils.KLog;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("CheckResult")
public class CityWeatherRepository {
    private static final String TAG = CityWeatherRepository.class.getSimpleName();
    final MutableLiveData<CityIdResponse> cityIdResponses = new MutableLiveData<>();
    final MutableLiveData<CityNowWeather> cityNowWeatherM = new MutableLiveData<>();

    public MutableLiveData<CityNowWeather> getCityNowWeather(String name) {
        cityid(name);
        return cityNowWeatherM;
    }

    private void cityid(String name) {
        ApiService apiService = NetworkApi.createService(ApiService.class,1);
        apiService.cityid(name).compose(NetworkApi.applySchedulers(new BaseObserver<CityIdResponse>() {
            @Override
            public void onSuccess(CityIdResponse cityIdResponse) {
                String id = cityIdResponse.getLocation().get(0).getId();
                CityNow(id);
                Log.i(TAG, "onSuccess: "+id+"ccs"+name);
            }

            @Override
            public void onFailure(Throwable e) {
                Log.i(TAG, "onFailure: "+e.toString());
            }
        }));
    }
    private void CityNow(String id) {
        ApiService apiService = NetworkApi.createService(ApiService.class,2);
        apiService.cityNowWeather(id).compose(NetworkApi.applySchedulers(new BaseObserver<CityNowWeather>() {
            @Override
            public void onSuccess(CityNowWeather cityNowWeather) {
                Log.i(TAG, "onSuccessaas: "+cityNowWeather);
                cityNowWeatherM.setValue(cityNowWeather);
            }

            @Override
            public void onFailure(Throwable e) {
                KLog.e("BiYing Error: " + e.toString());
            }
        }));
    }
}
