package com.wbl.weather.ui.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder;
import com.wbl.weather.R;
import com.wbl.weather.databinding.ItemDailyBinding;
import com.wbl.weather.model.CityDailyResponse;

import java.util.List;

public class CityDailyAdapter extends BaseQuickAdapter<CityDailyResponse.Daily, BaseDataBindingHolder<ItemDailyBinding>> {

    private List<CityDailyResponse.Daily> data;

    public CityDailyAdapter(@Nullable List<CityDailyResponse.Daily> data) {
        super(R.layout.item_daily, data);
        this.data = data;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    protected void convert(@NonNull BaseDataBindingHolder<ItemDailyBinding> bindingHolder, CityDailyResponse.Daily daily) {
        ItemDailyBinding binding = bindingHolder.getDataBinding();
        if (binding != null) {
            binding.setDailyWeather(daily);
            binding.executePendingBindings();
        }
    }
}
