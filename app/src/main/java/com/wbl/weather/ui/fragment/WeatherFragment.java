package com.wbl.weather.ui.fragment;


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

import android.os.MemoryFile;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.BackgroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

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
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechEvent;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.cloud.msc.util.FileUtil;
import com.iflytek.cloud.msc.util.log.DebugLog;
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
import com.wbl.weather.utils.PermissionUtils;
import com.wbl.weather.utils.YuYinUtils;
import com.wbl.weather.viewmodels.WeatherViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class WeatherFragment extends BaseFragment implements DistrictSearch.OnDistrictSearchListener, AMapLocationListener
        , Spinner.OnItemSelectedListener {

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

    private static boolean IS_YUYIN = false;
    private String YuyinText;


    // 语音合成对象
    private SpeechSynthesizer mTts;

    // 默认发音人
    private String voicer = "xiaoyan";

    // 引擎类型
    private String mEngineType = SpeechConstant.TYPE_CLOUD;


    private Vector<byte[]> container = new Vector<>();

    //内存文件
    MemoryFile memoryFile;
    //总大小
    public volatile long mTotalSize = 0;

    //发音人名称
    private static final String[] arrayName = {"讯飞小燕", "讯飞许久", "讯飞小萍", "讯飞小婧", "讯飞许小宝"};

    //发音人值
    private static final String[] arrayValue = {"xiaoyan", "aisjiuxu", "aisxping", "aisjinger", "aisbabyxu"};

    //数组适配器
    private ArrayAdapter<String> arrayAdapter;

    //语速
    private String speedValue = "50";
    //音调
    private String pitchValue = "50";
    //音量
    private String volumeValue = "50";

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
        // 初始化合成对象
        mTts = SpeechSynthesizer.createSynthesizer(requireActivity(), mTtsInitListener);
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
                requestLocation();
                //开启定位
                mLocationClient.startLocation();

            }
        });
        binding.laiyuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireActivity(), SourceActivity.class);
                intent.putExtra("Url", "https://www.qweather.com");
                view.getContext().startActivity(intent);
            }
        });


        //初始化操作
        initSearch();
        initLocation();
        initView();
        //搜索行政区
        districtArray[index] = "中国";
        districtSearch(districtArray[index]);
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

    @Override
    public void onDetach() {
        super.onDetach();
        mLocationClient.onDestroy();
    }

    /**
     * 初始化
     */
    private void initView() {
        binding.hourly.setVisibility(View.GONE);
        initweather();
        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initView();
            }
        });
        binding.hd.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                if (i1 == 0) {
                    Log.i(TAG, "onScrollChange: 顶部");

                }
            }
        });
        binding.swipeRefreshLayout.setRefreshing(false);
        //伸缩偏移量监听
        binding.appbarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {//收缩时
                    String name = MVUtils.getString("address");
                    if (name == null) {
                        name = "北京市";
                        Log.i(TAG, "initView: " + name);
                        binding.toolbarLayout.setTitle(name);
                        binding.toolbar.setBackgroundColor(Color.parseColor("#FF6200EE"));
                    } else {
                        Log.i(TAG, "initView: " + name);
                        binding.toolbarLayout.setTitle(name);
                        binding.toolbar.setBackgroundColor(Color.parseColor("#FF6200EE"));
                    }
                    isShow = true;

                } else if (isShow) {//展开时
                    binding.toolbarLayout.setTitle("");
                    binding.toolbar.setBackgroundColor(Color.parseColor("#00000000"));
                    isShow = false;

                }
            }
        });

        //将可选内容与ArrayAdapter连接起来
        arrayAdapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_spinner_item, arrayName);
        //设置下拉列表的风格
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //将adapter 添加到spinner中
        binding.spinner.setAdapter(arrayAdapter);
        //添加事件Spinner事件监听
        binding.spinner.setOnItemSelectedListener(this);

        setSeekBar(binding.sbSpeed, 1);
        setSeekBar(binding.sbPitch, 2);
        setSeekBar(binding.sbVolume, 3);
        binding.weatherYuyinBtn.setOnClickListener(view -> {
            if (mTts == null) {
                this.showTip("创建对象失败，请确认 libmsc.so 放置正确，且有调用 createUtility 进行初始化");
                return;
            }
            if (!IS_YUYIN) {
                IS_YUYIN = true;
                //输入文本
                YuYinUtils yuYinUtils = new YuYinUtils();
                String etStr = yuYinUtils.yuyin();
                if (!etStr.isEmpty()) {
                    YuyinText = etStr;
                }
                //设置参数
                setParam();
                //开始合成播放
                int code = mTts.startSpeaking(YuyinText, mTtsListener);
                if (code != ErrorCode.SUCCESS) {
                    showTip("语音合成失败,错误码: " + code);
                }
            } else {
                IS_YUYIN = false;
                mTts.pauseSpeaking();
            }
        });

    }


    //设置SeekBar
    private void setSeekBar(SeekBar seekBar, final int type) {

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                switch (type) {
                    case 1://设置语速 范围 1~100
                        speedValue = Integer.toString(progress);
                        break;
                    case 2://设置音调  范围 1~100
                        pitchValue = Integer.toString(progress);
                        break;
                    case 3://设置音量  范围 1~100
                        volumeValue = Integer.toString(progress);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });
    }
    /**
     * 开始刷新天气
     */
    private void initweather() {
        String name = MVUtils.getString("address");
        if (name == "" || name == null) {
            name = "北京市";
            Log.i(TAG, "initView: " + name);
            CityCode(name);
        } else {
            Log.i(TAG, "initView: " + name);
            CityCode(name);
        }
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
     * 天气信息
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
            String text = name + "现在的天气" + cityNowWeather.getNow().getText() + "室外温度" + cityNowWeather.getNow().getTemp()
                    + "体感温度" + cityNowWeather.getNow().getFeelsLike() + "风向" + cityNowWeather.getNow().getWindDir()
                    + "风力" + cityNowWeather.getNow().getWindScale();
            MVUtils.put("cityNowY", text);

        });
        mViewModel.airResponse.observe(requireActivity(), cityAirResponse -> {
            binding.setAirweather(mViewModel);
            String text = "空气质量" + cityAirResponse.getNow().getCategory() + "空气中的污染物主要是" + cityAirResponse.getNow().getPrimary();
            MVUtils.put("cityNowA", text);
            binding.air.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(requireActivity(), SourceActivity.class);
                    intent.putExtra("Url", cityAirResponse.getFxLink());
                    requireActivity().startActivity(intent);
                }
            });
        });
        String time = "更新于：" + DateUtil.getDateTime();
        binding.timeRe.setText(time);
        mViewModel.cityHourlyWeather.observe(requireActivity(), cityHourlyWeather1 -> {
            MVUtils.put("hourly", cityHourlyWeather1.getFxLink());
            CityHourlyAdapter cityHourlyAdapter = new CityHourlyAdapter(cityHourlyWeather1.getHourly());
            LinearLayoutManager layoutManager = new LinearLayoutManager(requireActivity());
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            binding.hourly.setLayoutManager(layoutManager);
            binding.hourly.setAdapter(cityHourlyAdapter);
            binding.hourly.setVisibility(View.VISIBLE);
        });

        mViewModel.cityDailyResponse.observe(requireActivity(), cityDailyResponse -> {
            MVUtils.put("cityDail", cityDailyResponse.getFxLink());
            CityDailyAdapter cityDailyAdapter = new CityDailyAdapter(cityDailyResponse.getDaily());
            LinearLayoutManager layoutManager = new LinearLayoutManager(requireActivity());
            binding.dailyWeather.setLayoutManager(layoutManager);
            binding.dailyWeather.setAdapter(cityDailyAdapter);
        });
        mViewModel.liveResponse.observe(requireActivity(), cityLiveResponse -> {
            MVUtils.put("live", cityLiveResponse.getFxLink());
            CityLiveAdapter cityLiveAdapter = new CityLiveAdapter(cityLiveResponse.getDaily());
            LinearLayoutManager layoutManager = new LinearLayoutManager(requireActivity());
            binding.liveIndices.setLayoutManager(layoutManager);
            binding.liveIndices.setAdapter(cityLiveAdapter);
        });


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
                //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表
                Log.e("AmapError", "location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
            }
        }
    }


    /**
     * 初始化监听。
     */
    private InitListener mTtsInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
            Log.i(TAG, "InitListener init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                showTip("初始化失败,错误码：" + code);
            } else {
                showTip("初始化成功");

            }
        }
    };

    /**
     * Toast提示
     *
     * @param msg
     */
    private void showTip(String msg) {
        Toast.makeText(requireActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 参数设置
     *
     * @return
     */
    private void setParam() {
        // 清空参数
        mTts.setParameter(SpeechConstant.PARAMS, null);
        // 根据合成引擎设置相应参数
        if (mEngineType.equals(SpeechConstant.TYPE_CLOUD)) {
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
            //支持实时音频返回，仅在synthesizeToUri条件下支持
            mTts.setParameter(SpeechConstant.TTS_DATA_NOTIFY, "1");
            // 设置在线合成发音人
            mTts.setParameter(SpeechConstant.VOICE_NAME, voicer);

            //设置合成语速
            mTts.setParameter(SpeechConstant.SPEED, speedValue);
            //设置合成音调
            mTts.setParameter(SpeechConstant.PITCH, pitchValue);
            //设置合成音量
            mTts.setParameter(SpeechConstant.VOLUME, volumeValue);
        } else {
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
            mTts.setParameter(SpeechConstant.VOICE_NAME, "");
        }
        // 设置播放合成音频打断音乐播放，默认为true
        mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "false");
        // 设置音频保存路径，保存音频格式支持pcm、wav
        mTts.setParameter(SpeechConstant.AUDIO_FORMAT, "pcm");
        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, requireActivity().getExternalFilesDir(null) + "/msc/tts.pcm");
    }

    /**
     * 合成回调监听。
     */
    private SynthesizerListener mTtsListener = new SynthesizerListener() {
        //开始播放
        @Override
        public void onSpeakBegin() {
            Log.i(TAG, "开始播放");
        }

        //暂停播放
        @Override
        public void onSpeakPaused() {
            Log.i(TAG, "暂停播放");
        }

        //继续播放
        @Override
        public void onSpeakResumed() {
            Log.i(TAG, "继续播放");
        }

        //合成进度
        @Override
        public void onBufferProgress(int percent, int beginPos, int endPos, String info) {
            Log.i(TAG, "合成进度：" + percent + "%");
        }

        //播放进度
        @Override
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
            // 播放进度
            Log.i(TAG, "播放进度：" + percent + "%");
            SpannableStringBuilder style = new SpannableStringBuilder(YuyinText);
            style.setSpan(new BackgroundColorSpan(Color.RED), beginPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        }

        //播放完成
        @Override
        public void onCompleted(SpeechError error) {
            if (error == null) {
                Log.i(TAG, "播放完成," + container.size());
                DebugLog.LogD("播放完成," + container.size());
                for (int i = 0; i < container.size(); i++) {
                    //写入文件
                    writeToFile(container.get(i));
                }
                //保存文件
                FileUtil.saveFile(memoryFile, mTotalSize, requireActivity().getExternalFilesDir(null) + "/1.pcm");
            } else {
                //异常信息
                showTip(error.getPlainDescription(true));
            }
        }

        //事件
        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            //	 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            //	 若使用本地能力，会话id为null
            if (SpeechEvent.EVENT_SESSION_ID == eventType) {
                String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
                Log.i(TAG, "session id =" + sid);
            }

            //当设置SpeechConstant.TTS_DATA_NOTIFY为1时，抛出buf数据
            if (SpeechEvent.EVENT_TTS_BUFFER == eventType) {
                byte[] buf = obj.getByteArray(SpeechEvent.KEY_EVENT_TTS_BUFFER);
                Log.i(TAG, "bufis =" + buf.length);
                container.add(buf);
            }
        }
    };

    /**
     * 写入文件
     */
    private void writeToFile(byte[] data) {
        if (data == null || data.length == 0) {
            return;
        }
        try {
            if (memoryFile == null) {
                Log.i(TAG, "memoryFile is null");
                String mFilepath = requireActivity().getExternalFilesDir(null) + "/1.pcm";
                memoryFile = new MemoryFile(mFilepath, 1920000);
                memoryFile.allowPurging(false);
            }
            memoryFile.writeBytes(data, 0, (int) mTotalSize, data.length);
            mTotalSize += data.length;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 选中
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        voicer = arrayValue[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }


}