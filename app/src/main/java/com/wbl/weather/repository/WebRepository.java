package com.wbl.weather.repository;

import android.annotation.SuppressLint;

import androidx.lifecycle.MutableLiveData;

import com.wbl.weather.api.ApiService;
import com.wbl.weather.model.NewsDetailResponse;
import com.wbl.weather.network.BaseObserver;
import com.wbl.weather.network.NetworkApi;

@SuppressLint("CheckResult")
public class WebRepository {
    final MutableLiveData<NewsDetailResponse> newsDetail = new MutableLiveData<>();
    public final MutableLiveData<String> failed =  new MutableLiveData<>();

    /**
     * 获取新闻详情数据
     * uniquekey 新闻ID
     */
    public MutableLiveData<NewsDetailResponse> getNewsDetail(String uniqueke) {
        NetworkApi.createService(ApiService.class,3).newsDetail(uniqueke).compose(NetworkApi.applySchedulers(new BaseObserver<NewsDetailResponse>() {
            @Override
            public void onSuccess(NewsDetailResponse newsDetailResponse) {
                if (newsDetailResponse.getError_code() == 0) {
                    newsDetail.setValue(newsDetailResponse);
                } else {
                    failed.postValue(newsDetailResponse.getReasin());
                }
            }

            @Override
            public void onFailure(Throwable e) {
                failed.postValue("NewsDetail Error: " + e.toString());
            }
        }));
        return newsDetail;
    }

}
