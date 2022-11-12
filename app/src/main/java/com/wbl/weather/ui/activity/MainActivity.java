package com.wbl.weather.ui.activity;


import androidx.databinding.DataBindingUtil;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import android.os.Bundle;

import com.wbl.weather.R;
import com.wbl.weather.databinding.ActivityMainBinding;

import com.wbl.weather.utils.PermissionUtils;
import com.wbl.weather.viewmodels.MainViewModel;


public class MainActivity extends BaseActivity {
    private ActivityMainBinding dataBinding;
    private MainViewModel mainViewModel;
    private String TAG = MainActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestLocation();
        //数据绑定视图
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        //网络请求
        mainViewModel.getBiying();
        //返回数据时更新ViewModel，ViewModel更新则xml更新
        mainViewModel.biying.observe(this, biYingImgResponse -> dataBinding.setViewmodel(mainViewModel));
        //基于个人隐私保护的关系，这里要设置为true，否则会出现地图白屏的情况
        dataBinding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initView();
            }
        });
        requestLocation();
        initView();


    }

    /**
     * 初始化
     */
    private void initView() {
        //获取navController
        NavController navController = Navigation.findNavController(this,R.id.nav_weather_fragment);
        navController.navigate(R.id.weather_fragment);
        dataBinding.swipeRefreshLayout.setRefreshing(false);
    }



    /**
     * 定位请求权限
     */
    private void requestLocation() {
        if (isAndroid6()) {
            if (!hasPermission(PermissionUtils.LOCATION)) {
                requestPermission(PermissionUtils.LOCATION);
            }
        } else {
            showMsg("您无需动态请求权限");
        }
    }
}