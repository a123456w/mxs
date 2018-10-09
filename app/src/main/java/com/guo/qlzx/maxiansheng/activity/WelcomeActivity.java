package com.guo.qlzx.maxiansheng.activity;

import android.content.Intent;
import android.view.WindowManager;

import com.guo.qlzx.maxiansheng.R;
import com.guo.qlzx.maxiansheng.application.MyApplication;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.widget.LoadingLayout;

import java.util.logging.Handler;

import butterknife.ButterKnife;

/**
 * Created by 李 on 2018/5/14.
 * 欢迎页
 */

public class WelcomeActivity extends BaseActivity {
    private PreferencesHelper preferencesHelper;
    private Handler mHandler;
    @Override
    public int getContentView() {
        return R.layout.activity_splash;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        preferencesHelper = new PreferencesHelper(this);
        hideTitleBar();
        if (preferencesHelper.isFirstTime()) {
            gotoGuide();
        } else {
            gotoMain();
        }
    }

    @Override
    public void getData() {

    }
    private void gotoMain() {
        MyApplication.getMainHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3000);
    }

    private void gotoGuide() {
        MyApplication.getMainHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(WelcomeActivity.this, GuideActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3000);
    }
}
