package com.wbl.weather.ui.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder;
import com.wbl.weather.R;
import com.wbl.weather.databinding.ItemHourlyBinding;
import com.wbl.weather.model.CityHourlyWeather;

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
            binding.executePendingBindings();
        }
    }
}
