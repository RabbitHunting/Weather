package com.wbl.weather.ui.fragment;

import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.MapsInitializer;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.district.DistrictItem;
import com.amap.api.services.district.DistrictResult;
import com.amap.api.services.district.DistrictSearch;
import com.amap.api.services.district.DistrictSearchQuery;
import com.google.gson.Gson;
import com.qweather.sdk.bean.base.Code;
import com.qweather.sdk.bean.geo.GeoBean;
import com.qweather.sdk.view.QWeather;
import com.wbl.weather.R;
import com.wbl.weather.databinding.WeatherFragmentBinding;
import com.wbl.weather.ui.adapter.CityAdapter;
import com.wbl.weather.utils.MVUtils;
import com.wbl.weather.viewmodels.WeatherViewModel;

import java.util.ArrayList;
import java.util.List;

public class WeatherFragment extends BaseFragment implements DistrictSearch.OnDistrictSearchListener, AMapLocationListener {

    private WeatherFragmentBinding binding;
    private WeatherViewModel mViewModel;
    private static final String TAG = WeatherFragment.class.getSimpleName();
    //生命AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //生命AMapLocationClientOption类对象
    public AMapLocationClientOption mLocationOption = null;

    //地区搜索
    private DistrictSearch districtSearch;
    //地区搜索查询
    private DistrictSearchQuery districtSearchQuery;
    //数组下标
    private int index = 0;
    //行政区数组
    private final String[] districtArray = new String[5];

    public static WeatherFragment newInstance() {
        return new WeatherFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.weather_fragment, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(WeatherViewModel.class);
        //点击显示城市菜单
        binding.fabCity.setOnClickListener(v -> binding.drawerLayout.openDrawer(GravityCompat.END));
        //抽屉菜单监听
        binding.drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                binding.fabCity.hide();
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                binding.fabCity.show();
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
        MapsInitializer.updatePrivacyShow(requireActivity(), true, true);
        MapsInitializer.updatePrivacyAgree(requireActivity(), true);
        binding.dwCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //开启定位
                mLocationClient.startLocation();
            }
        });
        //初始化操作
        initSearch();
        initLocation();
        //搜索行政区
        districtArray[index] = "中国";
        districtSearch(districtArray[index]);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mLocationClient.onDestroy();
    }

    /**
     * 初始化定位
     */
    private void initLocation() {
        //初始化定位
        try {
            mLocationClient = new AMapLocationClient(requireActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }

        //设置定位回调监听
        mLocationClient.setLocationListener(this);
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //获取最近3s内精度最高的一次定位结果：
        //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
        mLocationOption.setOnceLocationLatest(true);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //获取一次定位数据
        mLocationOption.setOnceLocationLatest(true);
        //设置定位请求超时时间，单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        mLocationOption.setHttpTimeOut(20000);
        //关闭缓存机制，高精度定位会产生缓存。
        mLocationOption.setLocationCacheEnable(false);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
    }

    /**
     * 初始化搜索
     */
    private void initSearch() {
        try {
            districtSearch = new DistrictSearch(requireActivity());
            districtSearch.setOnDistrictSearchListener(this);
            districtSearchQuery = new DistrictSearchQuery();
        } catch (AMapException e) {
            e.printStackTrace();
        }

    }

    /**
     * 行政区搜索
     *
     * @param name
     */
    public void districtSearch(String name) {
        binding.setName(name);
        //设置查询的关键字
        districtSearchQuery.setKeywords(name);
        districtSearch.setQuery(districtSearchQuery);
        //异步查询行政区
        districtSearch.searchDistrictAsyn();
    }

    /**
     * 行政区搜索返回
     *
     * @param districtResult 搜索结果
     */
    @Override
    public void onDistrictSearched(DistrictResult districtResult) {
        if (districtResult != null) {
            if (districtResult.getAMapException().getErrorCode() == AMapException.CODE_AMAP_SUCCESS) {
                final List<String> nameList = new ArrayList<>();
                List<DistrictItem> subDistrict1 = districtResult.getDistrict().get(0).getSubDistrict();
                for (int i = 0; i < subDistrict1.size(); i++) {
                    String name = subDistrict1.get(i).getName();
                    nameList.add(name);
                }
               /* Log.e(TAG, "onDistrictSearched: " + subDistrict1.size());
                for (DistrictItem districtItem : subDistrict1) {
                    Log.e(TAG, "onDistrictSearched: " + districtItem.getName());
                }*/
                //设置数据源
                if (nameList.size() != 0) {
                    CityAdapter cityAdapter = new CityAdapter(nameList);
                    binding.rvCity.setLayoutManager(new LinearLayoutManager(requireActivity()));
                    //item点击事件
                    cityAdapter.setOnItemClickListener((adapter, view, position) -> {
                        index++;
                        districtArray[index] = nameList.get(position);
                        binding.ivBack.setVisibility(View.VISIBLE);
                        //返回键监听
                        binding.ivBack.setOnClickListener(v -> {
                            index--;
                            //搜索上级行政区
                            districtSearch(districtArray[index]);
                            if ("中国".equals(districtArray[index])) {
                                binding.ivBack.setVisibility(View.GONE);
                            }
                        });
                        //搜索此区域的下级行政区
                        districtSearch(districtArray[index]);
                    });
                    binding.rvCity.setAdapter(cityAdapter);
                } else {
                    //开始进行地址转城市代码
                    index = index - 1;
                    String adname = MVUtils.getString("xuanze");
                    binding.drawerLayout.closeDrawer(GravityCompat.END);
                    if (adname.equals(districtArray[index])) {
                        adname = districtArray[index];
                        CityCode(adname);
                    } else {
                        MVUtils.put("xuanze", districtArray[index]);
                        adname = districtArray[index];
                        CityCode(adname);
                    }


                }
            } else {
                showMsg(districtResult.getAMapException().getErrorCode() + "");
            }
        }

    }

    /**
     * 城市名转城市代码
     */
    public void CityCode(String cityName) {
        String name = cityName;
        CityCodes(name);


    }

    /**
     * 接收异步返回的定位结果
     *
     * @param aMapLocation
     */
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                //地址
                String address = aMapLocation.getDistrict();
                String adname = MVUtils.getString("dingwei");
                mLocationClient.stopLocation();
                if (adname.equals(address)) {
                    adname = address;
                    CityCode(adname);
                } else {
                    MVUtils.put("dingwei", address);
                    adname = address;
                    CityCode(adname);
                }
            } else {
                //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表
                Log.e("AmapError", "location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
            }
        }
    }

    //查询城市id
    public void CityCodes(String name) {
        QWeather.getGeoCityLookup(requireActivity(), name, new QWeather.OnResultGeoListener() {
            @Override
            public void onError(Throwable throwable) {
                Log.i(TAG, "getWeather onError: " + throwable);
            }

            @Override
            public void onSuccess(GeoBean geoBean) {
                Log.i(TAG, "onSuccess: " + new Gson().toJson(geoBean));
                if (Code.OK == geoBean.getCode()) {
                    String cityName = geoBean.getLocationBean().get(0).getName();
                    String cityId = geoBean.getLocationBean().get(0).getId();
                    Log.i(TAG, "onSuccess: "+cityName+",id:"+cityId);
                } else {
                    Code code = geoBean.getCode();
                    Log.i(TAG, "onfail: " + code);
                }
            }
        });
    }
}