package com.guo.qlzx.maxiansheng.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.guo.qlzx.maxiansheng.R;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.widget.LoadingLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by 李 on 2018/4/20.
 */

public class CashBalanceResultActivity extends BaseActivity {
    @BindView(R.id.tv_submit)
    Button tvSubmit;
    Unbinder unbinder;
    @Override
    public int getContentView() {
        return R.layout.activity_cash_result;
    }

    @Override
    public void initView() {
        unbinder = ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setLeftImageRes(R.drawable.ic_back_gray);
        titleBar.setTitleText("余额提现");
        titleBar.getIbLeft().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(200);
                finish();
            }
        });
    }

    @Override
    public void getData() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @OnClick(R.id.tv_submit)
    public void onViewClicked() {
        setResult(200);
        finish();
    }
}
