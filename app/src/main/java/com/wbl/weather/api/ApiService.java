package com.wbl.weather.api;

import com.wbl.weather.model.BiYingResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * 所有的Api网络接口
 */
public interface ApiService {
    /**
     * 必应每日一图
     */
    @GET("/HPImageArchive.aspx?format=js&idx=0&n=1")
    Observable<BiYingResponse> biying();

}
