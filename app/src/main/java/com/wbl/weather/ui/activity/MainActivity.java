package com.wbl.weather.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;

import com.amap.api.services.district.DistrictItem;

import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.MapsInitializer;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.district.DistrictResult;
import com.amap.api.services.district.DistrictSearch;
import com.amap.api.services.district.DistrictSearchQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.google.android.material.appbar.AppBarLayout;
import com.wbl.weather.R;
import com.wbl.weather.databinding.ActivityMainBinding;
import com.wbl.weather.utils.MVUtils;
import com.wbl.weather.utils.PermissionUtils;
import com.wbl.weather.viewmodels.MainViewModel;

import java.util.ArrayList;
import java.util.List;

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