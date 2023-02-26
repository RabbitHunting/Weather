package com.wbl.weather.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.wbl.weather.model.NewsResponse;
import com.wbl.weather.repository.NewsRepository;

public class NewsViewModel extends BaseViewModel {
    // TODO: Implement the ViewModel
    public LiveData<NewsResponse> news;

    public void getNews() {
        NewsRepository newsRepository = new NewsRepository();
        failed = newsRepository.failed;
        news = newsRepository.getNews();
    }
}