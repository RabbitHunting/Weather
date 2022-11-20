package com.wbl.weather.ui.adapter;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder;
import com.wbl.weather.R;
import com.wbl.weather.databinding.ItemHourlyBinding;
import com.wbl.weather.model.CityHourlyWeather;
import com.wbl.weather.ui.activity.SourceActivity;
import com.wbl.weather.utils.JudgmentWeather;
import com.wbl.weather.utils.MVUtils;

import java.util.List;

public class CityHourlyAdapter extends BaseQuickAdapter<CityHourlyWeather.Hourly, BaseDataBindingHolder<ItemHourlyBinding>> {
    private List<CityHourlyWeather.Hourly> datas;
    public CityHourlyAdapter( @Nullable List<CityHourlyWeather.Hourly> data) {
        super(R.layout.item_hourly, data);
        this.datas = data;
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    @Override
    protected void convert(@NonNull BaseDataBindingHolder<ItemHourlyBinding> bindingHolder, CityHourlyWeather.Hourly hourly) {
        ItemHourlyBinding binding = bindingHolder.getDataBinding();
        if (binding != null) {
            binding.setHourlyWeather(hourly);
            int a = JudgmentWeather.weatherIcon(hourly.getIcon(),hourly.getFxTime());
            if (a != 1) {
                binding.info.setImageResource(a);
            }
            binding.setOnclick(new Click());
            binding.executePendingBindings();
        }
    }

    public static class Click{
        public void Click(View view) {
            String Url = MVUtils.getString("hourly");
            Log.i("TAG", "Click: "+Url);
            Intent intent = new Intent(view.getContext(), SourceActivity.class);
            intent.putExtra("Url",Url);
            view.getContext().startActivity(intent);
        }
    }

}
