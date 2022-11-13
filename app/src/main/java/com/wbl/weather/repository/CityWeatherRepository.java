package com.wbl.weather.repository;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.wbl.weather.api.ApiService;
import com.wbl.weather.model.CityAirResponse;
import com.wbl.weather.model.CityDailyResponse;
import com.wbl.weather.model.CityHourlyWeather;
import com.wbl.weather.model.CityIdResponse;
import com.wbl.weather.model.CityLiveResponse;
import com.wbl.weather.model.CityNowWeather;
import com.wbl.weather.network.BaseObserver;
import com.wbl.weather.network.NetworkApi;
import com.wbl.weather.network.utils.DateUtil;
import com.wbl.weather.network.utils.KLog;
import com.wbl.weather.utils.EasyDate;
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
    final MutableLiveData<CityDailyResponse> cityDailyResponseM = new MutableLiveData<>();
    final MutableLiveData<CityAirResponse> cityAirResponseM = new MutableLiveData<>();
    final MutableLiveData<CityLiveResponse> cityLiveResponseM = new MutableLiveData<>();

    //实时天气
    public MutableLiveData<CityNowWeather> getCityNowWeather(String name) {
        cityid(name,1);
        return cityNowWeatherM;
    }

    //24小时天气
    public MutableLiveData<CityHourlyWeather> getCityHourlyWeather(String name) {
        cityid(name,2);
        return cityHourlyWeatherM;
    }

    //7天气天气
    public MutableLiveData<CityDailyResponse> getCityDailyResponse(String name) {
        cityid(name,3);
        return cityDailyResponseM;
    }

    //空气质量
    public MutableLiveData<CityAirResponse> getCityAirResponse(String name) {
        cityid(name,4);
        return cityAirResponseM;
    }

    //生活指数
    public MutableLiveData<CityLiveResponse> getCityLiveResponse(String name) {
        cityid(name,5);
        return cityLiveResponseM;
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
                    case 3:
                        cityDaily(id);
                        break;
                    case 4:
                        cityAir(id);
                        break;
                    case 5:
                        cityLive(id);
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

    /**
     * 实时天气
     * @param id
     */
    private void CityNow(String id) {
        ApiService apiService = NetworkApi.createService(ApiService.class,2);
        apiService.cityNowWeather(id).compose(NetworkApi.applySchedulers(new BaseObserver<CityNowWeather>() {
            @Override
            public void onSuccess(CityNowWeather cityNowWeather) {
                //Log.i(TAG, "onSuccessaas: "+cityNowWeather);
                cityNowWeatherM.setValue(cityNowWeather);
            }

            @Override
            public void onFailure(Throwable e) {
                KLog.e("CityNow Error: " + e.toString());
            }
        }));
    }


    /**
     * 24小时天气
     * @param id
     */
    private void cityHourly(String id) {
        ApiService apiService = NetworkApi.createService(ApiService.class,2);
        apiService.cityhourlyWeather(id).compose(NetworkApi.applySchedulers(new BaseObserver<CityHourlyWeather>() {
            @Override
            public void onSuccess(CityHourlyWeather cityHourlyWeather) {
                //Log.i(TAG, "onSuccess: "+cityHourlyWeather);
                for (int i = 0;i<cityHourlyWeather.getHourly().size();i++) {
                    String time = cityHourlyWeather.getHourly().get(i).getFxTime();
                    //Log.i(TAG, "时间原onSuccess: "+time);
                    String times = EasyDate.time(time);
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

    /**
     * 7天天气预报
     * @param id
     */
    private void cityDaily(String id) {
        ApiService apiService = NetworkApi.createService(ApiService.class,2);
        apiService.cityDailyWeather(id).compose(NetworkApi.applySchedulers(new BaseObserver<CityDailyResponse>() {
            @Override
            public void onSuccess(CityDailyResponse cityDailyResponse) {
                //Log.i(TAG, "onSuccess: "+cityDailyResponse);
                /*for (int i = 0 ; i<cityDailyResponse.getDaily().size();i++) {
                    String time = cityDailyResponse.getDaily().get(i).getFxDate();
                    Log.i(TAG, "onSuccess时间: "+time);
                }*/
                cityDailyResponseM.postValue(cityDailyResponse);
            }

            @Override
            public void onFailure(Throwable e) {
                KLog.e("CityDaily Error:" +e.toString());
            }
        }));
    }

    /**
     * 空气质量
     */
    private void cityAir(String id) {
        ApiService apiService = NetworkApi.createService(ApiService.class,2);
        apiService.cityAirWeather(id).compose(NetworkApi.applySchedulers(new BaseObserver<CityAirResponse>() {
            @Override
            public void onSuccess(CityAirResponse cityAirResponse) {
                cityAirResponseM.postValue(cityAirResponse);
            }

            @Override
            public void onFailure(Throwable e) {
                KLog.e("CityAir Error:" +e.toString());
            }
        }));

    }

    /**
     * 生活指数
     */
    private void cityLive(String id) {
        ApiService apiService = NetworkApi.createService(ApiService.class,2);
        apiService.cityLiveWeather(id).compose(NetworkApi.applySchedulers(new BaseObserver<CityLiveResponse>() {
            @Override
            public void onSuccess(CityLiveResponse cityLiveResponse) {
                cityLiveResponseM.postValue(cityLiveResponse);
            }

            @Override
            public void onFailure(Throwable e) {
                KLog.e("CityLive Error:"+e.toString());
            }
        }));
    }
}
