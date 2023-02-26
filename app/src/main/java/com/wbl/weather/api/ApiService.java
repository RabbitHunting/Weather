package com.wbl.weather.api;

import com.wbl.weather.model.BiYingResponse;
import com.wbl.weather.model.CityAirResponse;
import com.wbl.weather.model.CityDailyResponse;
import com.wbl.weather.model.CityHourlyWeather;
import com.wbl.weather.model.CityIdResponse;
import com.wbl.weather.model.CityLiveResponse;
import com.wbl.weather.model.CityNowWeather;
import com.wbl.weather.model.NewsDetailResponse;
import com.wbl.weather.model.NewsResponse;

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

    /**
     * 天气数据
     */
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

    @GET("/v7/indices/1d?type=1,2,3,6,8,16&key=922cb910930843a8b60d5588e9cf8182")
    Observable<CityLiveResponse> cityLiveWeather(@Query("location") String id);

    /**
     * 新闻数据
     */
    @GET("/toutiao/index?type=&page=&page_size=&is_filter=&key=72556cae846101cbd61f7b0750c0bdb1")
    Observable<NewsResponse> news();
    /**
     * 聚合新闻数据详情
     */
    @GET("/toutiao/content?key=72556cae846101cbd61f7b0750c0bdb1")
    Observable<NewsDetailResponse> newsDetail(@Query("uniquekey") String uniquekey);


}
