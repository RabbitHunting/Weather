package com.wbl.weather.api;

import com.wbl.weather.model.BiYingResponse;
import com.wbl.weather.model.CityAirResponse;
import com.wbl.weather.model.CityDailyResponse;
import com.wbl.weather.model.CityHourlyWeather;
import com.wbl.weather.model.CityIdResponse;
import com.wbl.weather.model.CityNowWeather;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * 所有的Api网络接口
 */
public interface ApiService {
    /**
     * 必应每日一图
     */
    @GET("/HPImageArchive.aspx?format=js&idx=0&n=1")
    Observable<BiYingResponse> biying();

    @GET("/v2/city/lookup?key=922cb910930843a8b60d5588e9cf8182")
    Observable<CityIdResponse> cityid(@Query("location") String name);

    @GET("/v7/weather/now?key=922cb910930843a8b60d5588e9cf8182")
    Observable<CityNowWeather> cityNowWeather(@Query("location") String id);

    @GET("/v7/weather/24h?key=922cb910930843a8b60d5588e9cf8182")
    Observable<CityHourlyWeather> cityhourlyWeather(@Query("location") String id);

    @GET("/v7/weather/7d?key=922cb910930843a8b60d5588e9cf8182")
    Observable<CityDailyResponse> cityDailyWeather(@Query("location") String id);

    @GET("/v7/air/now?key=922cb910930843a8b60d5588e9cf8182")
    Observable<CityAirResponse> cityAirWeather(@Query("location") String id);


}
