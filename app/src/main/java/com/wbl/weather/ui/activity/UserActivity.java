package com.wbl.weather.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import com.wbl.weather.R;
import com.wbl.weather.databinding.ActivityUserBinding;
import com.wbl.weather.viewmodels.UserViewModel;

public class UserActivity extends BaseActivity {

    private ActivityUserBinding databing;
    private UserViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_user);
        databing = DataBindingUtil.setContentView(this,R.layout.activity_user);
        viewModel = new ViewModelProvider(this).get(UserViewModel.class);

    }
}