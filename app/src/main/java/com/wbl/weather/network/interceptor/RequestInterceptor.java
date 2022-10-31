package com.wbl.weather.network.interceptor;

import com.wbl.weather.network.INetworkRequiredInfo;
import com.wbl.weather.network.utils.DateUtil;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 请求拦截器
 */
public class RequestInterceptor implements Interceptor {

    /**
     * 网络请求信息
     */
    private INetworkRequiredInfo iNetworkRequiredInfo;
    public RequestInterceptor(INetworkRequiredInfo iNetworkRequiredInfo) {
        this.iNetworkRequiredInfo = iNetworkRequiredInfo;
    }

    /**
     * 拦截
     * @param chain
     * @return
     * @throws IOException
     */
    @Override
    public Response intercept(Chain chain) throws IOException {
        String nowDateTime = DateUtil.getDateTime();
        //构建器
        Request.Builder builder = chain.request().newBuilder();
        //添加使用环境
        builder.addHeader("os","android");
        //添加版本号
        builder.addHeader("appVersionCode",this.iNetworkRequiredInfo.getAppVersionCode());
        //添加版本名
        builder.addHeader("appVersionName",this.iNetworkRequiredInfo.getAppVersionName());
        //添加版本时间
        builder.addHeader("datetime",nowDateTime);
        //返回
        return chain.proceed(builder.build());
    }
}
