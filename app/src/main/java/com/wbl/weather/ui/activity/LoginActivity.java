package com.wbl.weather.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.wbl.weather.R;
import com.wbl.weather.databinding.ActivityLoginBinding;
import com.wbl.weather.db.bean.WeatherUser;
import com.wbl.weather.db.dao.WeatherUserDao;
import com.wbl.weather.db.dao.impl.WeatherUserDaoImpl;
import com.wbl.weather.utils.Constant;
import com.wbl.weather.utils.MVUtils;
import com.wbl.weather.viewmodels.LoginViewModel;

public class LoginActivity extends BaseActivity{

    private ActivityLoginBinding binding;
    private LoginViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_login);
        //绑定视图
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);

    }

    public void toRegister(View view){
        jumpActivity(RegisterActivity.class);
    }

    public void login(View view) {
        initView();
    }
    private void initView() {
        new Thread() {
            @Override
            public void run() {
                WeatherUserDao weatherUserDao = new WeatherUserDaoImpl();
                boolean a = weatherUserDao.login(binding.editUsername.getText().toString().trim(),binding.editPassword.getText().toString().trim());
                int msg = 0;
                if (a) {
                    WeatherUser user = weatherUserDao.findUser(binding.editUsername.getText().toString().trim());
                    if (user != null) {
                        Log.i("TAG", "run: "+user.getName());
                        MVUtils.put("nicheng",user.getName());
                        msg = 1;
                    }
                }
                hand1.sendEmptyMessage(msg);
            }
        }.start();
    }


    final Handler hand1 = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == 1) {
                Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_SHORT).show();
                MVUtils.put(Constant.is_Login,true);
                jumpActivityFinish(MainActivity.class);
            } else {
                Toast.makeText(getApplicationContext(),"登录失败",Toast.LENGTH_SHORT).show();
            }
        }
    };
}