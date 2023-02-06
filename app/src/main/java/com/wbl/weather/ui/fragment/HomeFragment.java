package com.wbl.weather.ui.fragment;

import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wbl.weather.R;
import com.wbl.weather.databinding.HomeFragmentBinding;
import com.wbl.weather.ui.activity.LoginActivity;
import com.wbl.weather.utils.Constant;
import com.wbl.weather.utils.MVUtils;
import com.wbl.weather.viewmodels.HomeViewModel;

public class HomeFragment extends BaseFragment {

    private HomeViewModel mViewModel;
    private HomeFragmentBinding binding;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.home_fragment, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        intView();
        binding.drawLay.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                binding.tc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MVUtils.put(Constant.is_Login,false);
                        intView();
                        binding.drawLay.closeDrawer(GravityCompat.START);
                    }
                });
                binding.dl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(requireActivity(), LoginActivity.class);
                        view.getContext().startActivity(intent);
                    }
                });
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
        // TODO: Use the ViewModel
    }

    private void intView() {
        if (MVUtils.getBoolean(Constant.is_Login)) {
            String nicheng = MVUtils.getString("nicheng");
            binding.zt.setText(nicheng);
            //用户已登录则跳转到用户信息列表



        } else {
            binding.zt.setText("未登录");
            binding.zt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(requireActivity(), LoginActivity.class);
                    view.getContext().startActivity(intent);
                }
            });
        }

    }



}