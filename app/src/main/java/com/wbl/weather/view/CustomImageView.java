package com.wbl.weather.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.imageview.ShapeableImageView;
import com.wbl.weather.BaseApplication;
import com.wbl.weather.network.utils.KLog;

import io.reactivex.annotations.Nullable;
/**
 * 自定义View
 * @author llw
 * @description CustomImageVIew
 */
public class CustomImageView extends AppCompatImageView {

    public CustomImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
    /**
     * 必应壁纸  因为拿到的url不完整，因此需要做一次地址拼接
     * @param imageView 图片视图
     * @param url 网络url
     */
    @BindingAdapter(value = {"biyingUrl"}, requireAll = false)
    public static void setBiyingUrl(ImageView imageView, String url) {
        String assembleUrl = "http://cn.bing.com" + url;
        KLog.d(assembleUrl);
        Glide.with(BaseApplication.getContext()).load(assembleUrl).into(imageView);
    }

    /**
     * 普通网络地址图片
     * @param imageView 图片视图
     * @param url 网络url
     */
    @BindingAdapter(value = {"networkUrl"}, requireAll = false)
    public static void setNetworkUrl(ImageView imageView, String url) {
        Glide.with(BaseApplication.getContext()).load(url).into(imageView);
    }
}