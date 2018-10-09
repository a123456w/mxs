package com.guo.qlzx.maxiansheng.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.guo.qlzx.maxiansheng.R;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.widget.LoadingLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 李 on 2018/4/16.
 *  活动专场
 */

public class ActivitySessionActivity extends BaseActivity {
    @BindView(R.id.iv_presell)
    ImageView ivPresell;
    @BindView(R.id.iv_kill)
    ImageView ivKill;
    @BindView(R.id.iv_groupon)
    ImageView ivGroupon;

    private Intent intent;
    @Override
    public int getContentView() {
        return R.layout.activity_activity_session;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setLeftImageRes(R.drawable.ic_back_gray);
        titleBar.setTitleText("活动专场");
        intent=new Intent(ActivitySessionActivity.this,KillSessionActivity.class);
    }
    @Override
    public void getData() {

    }

    @OnClick({R.id.iv_presell,R.id.iv_kill,R.id.iv_groupon})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.iv_presell:
                intent.putExtra("TYPEID",3);
                startActivity(intent);
                break;
            case R.id.iv_kill:
                intent.putExtra("TYPEID",1);
                startActivity(intent);
                break;
            case R.id.iv_groupon:
                intent.putExtra("TYPEID",2);
                startActivity(intent);
                break;
        }
    }
}
