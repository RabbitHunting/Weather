package com.wbl.weather.ui.fragment;

import androidx.annotation.ColorInt;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import com.google.android.material.appbar.AppBarLayout;
import com.wbl.weather.R;
import com.wbl.weather.databinding.ItemHourlyBinding;
import com.wbl.weather.databinding.WeatherFragmentBinding;
import com.wbl.weather.model.CityAirResponse;
import com.wbl.weather.model.CityNowWeather;
import com.wbl.weather.network.utils.DateUtil;
import com.wbl.weather.ui.activity.SourceActivity;
import com.wbl.weather.ui.adapter.CityAdapter;
import com.wbl.weather.ui.adapter.CityDailyAdapter;
import com.wbl.weather.ui.adapter.CityHourlyAdapter;
import com.wbl.weather.ui.adapter.CityLiveAdapter;
import com.wbl.weather.utils.JudgmentWeather;
import com.wbl.weather.utils.MVUtils;
import com.wbl.weather.viewmodels.WeatherViewModel;

import java.util.ArrayList;
import java.util.List;

public class WeatherFragment extends BaseFragment implements DistrictSearch.OnDistrictSearchListener, AMapLocationListener {

    private WeatherFragmentBinding binding;
    private WeatherViewModel mViewModel;
    private static final String TAG = WeatherFragment.class.getSimpleName();
    //??????AMapLocationClient?????????
    public AMapLocationClient mLocationClient = null;
    //??????AMapLocationClientOption?????????
    public AMapLocationClientOption mLocationOption = null;

    //????????????
    private DistrictSearch districtSearch;
    //??????????????????
    private DistrictSearchQuery districtSearchQuery;
    //????????????
    private int index = 0;
    //???????????????
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
        //????????????????????????
        binding.fabCity.setOnClickListener(v -> binding.drawerLayout.openDrawer(GravityCompat.END));
        //??????????????????
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
                //????????????
                mLocationClient.startLocation();

            }
        });
        binding.laiyuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireActivity(), SourceActivity.class);
                intent.putExtra("Url","https://www.qweather.com");
                view.getContext().startActivity(intent);
            }
        });




        //???????????????
        initSearch();
        initLocation();
        initView();
        //???????????????
        districtArray[index] = "??????";
        districtSearch(districtArray[index]);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mLocationClient.onDestroy();
    }

    /**
     * ?????????
     */
    private void initView() {
        binding.hourly.setVisibility(View.GONE);
        initweather();
        binding.hd.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                if (i1 == 0) {
                    Log.i(TAG, "onScrollChange: ??????");
                    binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            initView();
                        }
                    });
                }
            }
        });
        binding.swipeRefreshLayout.setRefreshing(false);
        //?????????????????????
        binding.appbarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {//?????????
                    String name = MVUtils.getString("address");
                    if (name == null) {
                        name = "?????????";
                        Log.i(TAG, "initView: " + name);
                        binding.toolbarLayout.setTitle(name);
                        binding.toolbar.setBackgroundColor(Color.parseColor("#FF6200EE"));
                    } else {
                        Log.i(TAG, "initView: " + name);
                        binding.toolbarLayout.setTitle(name);
                        binding.toolbar.setBackgroundColor(Color.parseColor("#FF6200EE"));
                    }
                    isShow = true;

                } else if (isShow) {//?????????
                    binding.toolbarLayout.setTitle("");
                    binding.toolbar.setBackgroundColor(Color.parseColor("#00000000"));
                    isShow = false;

                }
            }
        });



    }
    /**
     * ??????????????????
     */
    private void initweather() {
        String name = MVUtils.getString("address");
        if (name == "" || name == null) {
            name = "?????????";
            Log.i(TAG, "initView: " + name);
            CityCode(name);
        } else {
            Log.i(TAG, "initView: " + name);
            CityCode(name);
        }
    }

    /**
     * ???????????????
     */
    private void initLocation() {
        //???????????????
        try {
            mLocationClient = new AMapLocationClient(requireActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }

        //????????????????????????
        mLocationClient.setLocationListener(this);
        //?????????AMapLocationClientOption??????
        mLocationOption = new AMapLocationClientOption();
        //?????????????????????AMapLocationMode.Hight_Accuracy?????????????????????
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //????????????3s???????????????????????????????????????
        //??????setOnceLocationLatest(boolean b)?????????true??????????????????SDK???????????????3s?????????????????????????????????????????????????????????true???setOnceLocation(boolean b)????????????????????????true???????????????????????????false???
        mLocationOption.setOnceLocationLatest(true);
        //????????????????????????????????????????????????????????????
        mLocationOption.setNeedAddress(true);
        //????????????????????????
        mLocationOption.setOnceLocationLatest(true);
        //?????????????????????????????????????????????????????????30000???????????????????????????????????????8000?????????
        mLocationOption.setHttpTimeOut(20000);
        //??????????????????????????????????????????????????????
        mLocationOption.setLocationCacheEnable(false);
        //??????????????????????????????????????????
        mLocationClient.setLocationOption(mLocationOption);
    }

    /**
     * ???????????????
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
     * ???????????????
     *
     * @param name
     */
    public void districtSearch(String name) {
        binding.setName(name);
        //????????????????????????
        districtSearchQuery.setKeywords(name);
        districtSearch.setQuery(districtSearchQuery);
        //?????????????????????
        districtSearch.searchDistrictAsyn();
    }

    /**
     * ?????????????????????
     *
     * @param districtResult ????????????
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
                //???????????????
                if (nameList.size() != 0) {
                    CityAdapter cityAdapter = new CityAdapter(nameList);
                    binding.rvCity.setLayoutManager(new LinearLayoutManager(requireActivity()));
                    //item????????????
                    cityAdapter.setOnItemClickListener((adapter, view, position) -> {
                        index++;
                        districtArray[index] = nameList.get(position);
                        binding.ivBack.setVisibility(View.VISIBLE);
                        //???????????????
                        binding.ivBack.setOnClickListener(v -> {
                            index--;
                            //?????????????????????
                            districtSearch(districtArray[index]);
                            if ("??????".equals(districtArray[index])) {
                                binding.ivBack.setVisibility(View.GONE);
                            }
                        });
                        //?????????????????????????????????
                        districtSearch(districtArray[index]);
                    });
                    binding.rvCity.setAdapter(cityAdapter);
                } else {
                    //?????????????????????????????????
                    index = index - 1;
                    String adname = MVUtils.getString("xuanze");
                    binding.drawerLayout.closeDrawer(GravityCompat.END);
                    if (adname.equals(districtArray[index])) {
                        adname = districtArray[index];
                        MVUtils.put("address", districtArray[index]);
                        CityCode(adname);
                    } else {
                        MVUtils.put("xuanze", districtArray[index]);
                        MVUtils.put("address", districtArray[index]);
                        adname = districtArray[index];

                        CityCode(adname);
                    }
                    initView();
                }
            } else {
                showMsg(districtResult.getAMapException().getErrorCode() + "");
            }
        }

    }

    /**
     * ????????????????????????
     */
    public void CityCode(String cityName) {
        String name = cityName;

        mViewModel.getNowWeather(name);
        mViewModel.getHourlyWeather(name);
        mViewModel.getDailyWeather(name);
        mViewModel.getAirWeather(name);
        mViewModel.getLiveIndices(name);
        mViewModel.cityNowWeather.observe(requireActivity(), cityNowWeather -> {
            binding.setWeather(mViewModel);
            int id = JudgmentWeather.backid(cityNowWeather.getNow().getIcon());
            binding.weatherFragment.setBackgroundResource(id);

        });
        mViewModel.airResponse.observe(requireActivity(),cityAirResponse ->{
            binding.setAirweather(mViewModel);
            binding.air.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(requireActivity(), SourceActivity.class);
                    intent.putExtra("Url",cityAirResponse.getFxLink());
                    requireActivity().startActivity(intent);
                }
            });
        });
        String time = "????????????"+ DateUtil.getDateTime();
        binding.timeRe.setText(time);
        mViewModel.cityHourlyWeather.observe(requireActivity(),cityHourlyWeather1 -> {
            MVUtils.put("hourly",cityHourlyWeather1.getFxLink());
            CityHourlyAdapter cityHourlyAdapter = new CityHourlyAdapter(cityHourlyWeather1.getHourly());
            LinearLayoutManager layoutManager = new LinearLayoutManager(requireActivity());
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            binding.hourly.setLayoutManager(layoutManager);
            binding.hourly.setAdapter(cityHourlyAdapter);
            binding.hourly.setVisibility(View.VISIBLE);
        });

        mViewModel.cityDailyResponse.observe(requireActivity(),cityDailyResponse -> {
            MVUtils.put("cityDail",cityDailyResponse.getFxLink());
            CityDailyAdapter cityDailyAdapter = new CityDailyAdapter(cityDailyResponse.getDaily());
            LinearLayoutManager layoutManager = new LinearLayoutManager(requireActivity());
            binding.dailyWeather.setLayoutManager(layoutManager);
            binding.dailyWeather.setAdapter(cityDailyAdapter);
        });
        mViewModel.liveResponse.observe(requireActivity(),cityLiveResponse -> {
            MVUtils.put("live",cityLiveResponse.getFxLink());
            CityLiveAdapter cityLiveAdapter = new CityLiveAdapter(cityLiveResponse.getDaily());
            LinearLayoutManager layoutManager = new LinearLayoutManager(requireActivity());
            binding.liveIndices.setLayoutManager(layoutManager);
            binding.liveIndices.setAdapter(cityLiveAdapter);
        });


    }

    /**
     * ?????????????????????????????????
     *
     * @param aMapLocation
     */
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                //??????
                String adress = aMapLocation.getDistrict();
                String adname = MVUtils.getString("dingwei");
                mLocationClient.stopLocation();
                if (adname.equals(adress)) {
                    adname = adress;
                    MVUtils.put("address", adress);
                    CityCode(adname);
                } else {
                    MVUtils.put("dingwei", adress);
                    MVUtils.put("address", adress);
                    adname = adress;
                    CityCode(adname);
                }
                initView();
            } else {
                //???????????????????????????ErrCode????????????????????????????????????????????????errInfo????????????????????????????????????
                Log.e("AmapError", "location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
            }
        }
    }





}