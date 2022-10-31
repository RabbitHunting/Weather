package com.wbl.weather.ui.adapter;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder;
import com.wbl.weather.R;
import com.wbl.weather.databinding.ItemCityBinding;

import java.util.List;

public class CityAdapter extends BaseQuickAdapter<String, BaseDataBindingHolder<ItemCityBinding>> {

    public CityAdapter(@NonNull List<String> data) {
        super(R.layout.item_city,data);
    }

    @Override
    protected void convert(@NonNull BaseDataBindingHolder<ItemCityBinding> bindingHolder, String s) {
        ItemCityBinding binding = bindingHolder.getDataBinding();
        if (binding != null) {
            binding.setCityName(s);
            binding.executePendingBindings();
        }
    }
}
