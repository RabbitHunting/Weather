package com.wbl.weather.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.wbl.weather.R;
import com.wbl.weather.databinding.ActivityRegisterBinding;
import com.wbl.weather.db.bean.WeatherUser;
import com.wbl.weather.db.dao.WeatherUserDao;
import com.wbl.weather.db.dao.impl.WeatherUserDaoImpl;

public class RegisterActivity extends BaseActivity {

    private ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_register);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_register);
    }

    public void register(View view) {
        String username = binding.etUsername.getText().toString().trim();
        String password = binding.etPwd.getText().toString().trim();
        String qpassword = binding.etConfirmPwd.getText().toString().trim();
        String phone = binding.etPhone.getText().toString().trim();
        String name = binding.etNicheng.getText().toString().trim();
        Log.i("TAG", "register: "+password+"sa?:"+qpassword+"S:"+username+"s:"+phone+"ss:"+name);
        if (password.equals(qpassword)) {
            WeatherUser user = new WeatherUser();
            user.setUsername(username);
            user.setPassword(password);
            user.setPhone(phone);
            user.setName(name);
            new Thread() {
                @Override
                public void run() {
                    int msg = 0;
                    WeatherUserDao userDao = new WeatherUserDaoImpl();
                    WeatherUser user1 = userDao.findUser(user.getUsername());
                    if (user1 != null) {
                        msg =1;
                    } else {
                        boolean flag = userDao.register(user);
                        if (flag) {
                            msg =2;
                        }
                    }
                    hand.sendEmptyMessage(msg);
                }
            }.start();
        } else {
            Toast.makeText(context, "两次输入的密码不相同,请重新输入", Toast.LENGTH_SHORT).show();
            return;
        }

    }

    public void blacks(View view) {
        jumpActivityFinish(LoginActivity.class);
    }

    final Handler hand = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == 0) {
                Toast.makeText(getApplicationContext(), "注册失败", Toast.LENGTH_LONG).show();

            }
            if (msg.what == 1) {
                Toast.makeText(getApplicationContext(), "该账号已经存在，请换一个账号", Toast.LENGTH_LONG).show();

            }
            if (msg.what == 2) {
                //startActivity(new Intent(getApplication(),MainActivity.class));
                Toast.makeText(getApplicationContext(), "注册成功", Toast.LENGTH_LONG).show();
                jumpActivityFinish(LoginActivity.class);
            }
        }
    };
}