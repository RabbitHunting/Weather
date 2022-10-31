package com.wbl.weather.viewmodels;

import androidx.lifecycle.LiveData;

import com.wbl.weather.model.BiYingResponse;
import com.wbl.weather.repository.MainRepository;

public class MainViewModel extends BaseViewModel{
    public LiveData<BiYingResponse> biying;

    public void getBiying(){
        biying = new MainRepository().getBiYing();
    }
}
