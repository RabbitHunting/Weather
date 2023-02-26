package com.wbl.weather.viewmodels;

import androidx.lifecycle.LiveData;

import com.wbl.weather.model.NewsDetailResponse;
import com.wbl.weather.repository.WebRepository;

public class WebViewModel extends BaseViewModel{
    public LiveData<NewsDetailResponse> newsDetail;
    public void getNewDetail(String uniquekey) {
        WebRepository webRepository = new WebRepository();
        failed = webRepository.failed;
        newsDetail = webRepository.getNewsDetail(uniquekey);
    }
}