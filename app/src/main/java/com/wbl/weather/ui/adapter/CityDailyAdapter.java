package com.wbl.weather.ui.adapter;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder;
import com.wbl.weather.R;
import com.wbl.weather.databinding.ItemDailyBinding;
import com.wbl.weather.model.CityDailyResponse;
import com.wbl.weather.ui.activity.SourceActivity;
import com.wbl.weather.utils.MVUtils;

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
            binding.setOnClick(new Click());
            binding.executePendingBindings();
        }
    }

    public static class Click{
        public void Click(View view) {
            String Url = MVUtils.getString("cityDail");
            Log.i("TAG", "Click: "+Url);
            Intent intent = new Intent(view.getContext(), SourceActivity.class);
            intent.putExtra("Url",Url);
            view.getContext().startActivity(intent);
        }
    }
}
