package com.wbl.weather.repository;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.wbl.weather.api.ApiService;
import com.wbl.weather.model.CityHourlyWeather;
import com.wbl.weather.model.CityIdResponse;
import com.wbl.weather.model.CityNowWeather;
import com.wbl.weather.network.BaseObserver;
import com.wbl.weather.network.NetworkApi;
import com.wbl.weather.network.utils.DateUtil;
import com.wbl.weather.network.utils.KLog;
import com.wbl.weather.utils.MVUtils;

import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("CheckResult")
public class CityWeatherRepository {
    private static final String TAG = CityWeatherRepository.class.getSimpleName();
    final MutableLiveData<CityIdResponse> cityIdResponses = new MutableLiveData<>();
    final MutableLiveData<CityNowWeather> cityNowWeatherM = new MutableLiveData<>();
    final MutableLiveData<CityHourlyWeather> cityHourlyWeatherM = new MutableLiveData<>();

    public MutableLiveData<CityNowWeather> getCityNowWeather(String name) {
        cityid(name,1);
        return cityNowWeatherM;
    }

    public MutableLiveData<CityHourlyWeather> getCityHourlyWeather(String name) {
        cityid(name,2);
        return cityHourlyWeatherM;
    }

    private void cityid(String name,int type) {
        ApiService apiService = NetworkApi.createService(ApiService.class,1);
        apiService.cityid(name).compose(NetworkApi.applySchedulers(new BaseObserver<CityIdResponse>() {
            @Override
            public void onSuccess(CityIdResponse cityIdResponse) {
                String id = cityIdResponse.getLocation().get(0).getId();
                switch (type) {
                    case 1:
                        CityNow(id);
                        break;
                    case 2:
                        cityHourly(id);
                        break;
                    default:
                        break;
                }
                cityIdResponses.postValue(cityIdResponse);
                Log.i(TAG, "onSuccess: "+id+"ccs"+name+"type:"+type);
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
                KLog.e("CityNow Error: " + e.toString());
            }
        }));
    }


    private void cityHourly(String id) {
        ApiService apiService = NetworkApi.createService(ApiService.class,2);
        apiService.cityhourlyWeather(id).compose(NetworkApi.applySchedulers(new BaseObserver<CityHourlyWeather>() {
            @Override
            public void onSuccess(CityHourlyWeather cityHourlyWeather) {
                Log.i(TAG, "onSuccess: "+cityHourlyWeather);
                for (int i = 0;i<cityHourlyWeather.getHourly().size();i++) {
                    String time = cityHourlyWeather.getHourly().get(i).getFxTime();
                    //Log.i(TAG, "时间原onSuccess: "+time);
                    String times = DateUtil.time(time);
                    //Log.i(TAG, "时间转onSuccess: "+times);
                    cityHourlyWeather.getHourly().get(i).setFxTime(times);
                }
                cityHourlyWeatherM.postValue(cityHourlyWeather);
            }

            @Override
            public void onFailure(Throwable e) {
                KLog.e("CityHourly Error:"+e.toString());
            }
        }));
    }
}
