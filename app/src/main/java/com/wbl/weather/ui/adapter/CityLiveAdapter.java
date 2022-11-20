package com.wbl.weather.ui.adapter;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder;
import com.wbl.weather.R;
import com.wbl.weather.databinding.ItemLiveBinding;
import com.wbl.weather.model.CityLiveResponse;
import com.wbl.weather.ui.activity.SourceActivity;
import com.wbl.weather.utils.MVUtils;

import java.util.List;

public class CityLiveAdapter extends BaseQuickAdapter<CityLiveResponse.Daily, BaseDataBindingHolder<ItemLiveBinding>> {

    private List<CityLiveResponse.Daily> dailies;

    public CityLiveAdapter( @Nullable List<CityLiveResponse.Daily> data) {
        super(R.layout.item_live, data);
        this.dailies = data;
    }

    @Override
    public int getItemCount() {
        return dailies.size();
    }

    @Override
    protected void convert(@NonNull BaseDataBindingHolder<ItemLiveBinding> bindingHolder, CityLiveResponse.Daily daily) {
        ItemLiveBinding binding = bindingHolder.getDataBinding();
        if (binding != null) {
            binding.setLiveIndices(daily);
            binding.setOnClick(new Click());
            binding.executePendingBindings();
        }
    }

    public static class Click{
        public void Click(View view) {
            String Url = MVUtils.getString("live");
            Log.i("TAG", "Click: "+Url);
            Intent intent = new Intent(view.getContext(), SourceActivity.class);
            intent.putExtra("Url",Url);
            view.getContext().startActivity(intent);
        }
    }
}
