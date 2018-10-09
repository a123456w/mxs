package com.guo.qlzx.maxiansheng.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.guo.qlzx.maxiansheng.R;
import com.guo.qlzx.maxiansheng.bean.TotalSumBean;
import com.guo.qlzx.maxiansheng.http.RemoteApi;
import com.guo.qlzx.maxiansheng.util.ToolUtil;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.widget.LoadingLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 李 on 2018/4/18.
 * 我的资产 完毕
 */

public class MyAssetActivity extends BaseActivity {
    @BindView(R.id.tv_int)
    TextView tvInt;
    @BindView(R.id.tv_float)
    TextView tvFloat;
    @BindView(R.id.tv_balance)
    TextView tvBalance;
    @BindView(R.id.tv_rapeseed)
    TextView tvRapeseed;
    @BindView(R.id.ll_refund)
    LinearLayout llRefund;
    private PreferencesHelper helper;
    @Override
    public int getContentView() {
        return R.layout.activity_asset;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        hideTitleBar();
        helper = new PreferencesHelper(this);
    }

    @Override
    public void getData() {
        getAssetData(helper.getToken());
    }
    private void getAssetData(String token){
        HttpHelp.getInstance().create(RemoteApi.class).getBalance(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<TotalSumBean>>(this, null) {
                    @Override
                    public void onNext(BaseBean<TotalSumBean> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code==0) {
                            String bbb=baseBean.data.getUser_money();
                            int idx = bbb.lastIndexOf(".");//查找小数点的位置
                            String strNum = bbb.substring(0,idx);//截取从字符串开始到小数点位置的字符串，就是整数部分
                            String bb=bbb.substring(bbb.indexOf("."));
                            tvInt.setText(""+strNum);
                            tvFloat.setText(bb);
                            tvBalance.setText(baseBean.data.getUser_money());
                            tvRapeseed.setText(baseBean.data.getRapeseed());
                        }else if (baseBean.code==4){
                            ToolUtil.goToLogin(MyAssetActivity.this);
                        }
                    }
                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });

    }
    @OnClick({R.id.iv_left,R.id.btn_submit,R.id.ll_balance_item,R.id.ll_rapeseed_item,R.id.ll_faq,R.id.ll_my_balance,
    R.id.ll_put_forward,R.id.ll_refund})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.iv_left:
                finish();
                break;
            case R.id.btn_submit:
                if (TextUtils.isEmpty(new PreferencesHelper(this).getToken())){
                    ToolUtil.goToLogin(MyAssetActivity.this);
                    return;
                }
                startActivity(new Intent(MyAssetActivity.this,CashWayActivity.class));
                break;
            case R.id.ll_balance_item:
                if (TextUtils.isEmpty(new PreferencesHelper(this).getToken())){
                    ToolUtil.goToLogin(MyAssetActivity.this);
                    return;
                }
                intent=new Intent(MyAssetActivity.this,MyBalanceActivity.class);
                intent.putExtra("ISBALANCE",0);
                startActivity(intent);
                break;
            case R.id.ll_rapeseed_item:
                if (TextUtils.isEmpty(new PreferencesHelper(this).getToken())){
                    ToolUtil.goToLogin(MyAssetActivity.this);
                    return;
                }
                intent=new Intent(MyAssetActivity.this,MyBalanceActivity.class);
                intent.putExtra("ISBALANCE",1);
                startActivity(intent);
                break;
            case R.id.ll_faq:
                if (TextUtils.isEmpty(new PreferencesHelper(this).getToken())){
                    ToolUtil.goToLogin(MyAssetActivity.this);
                    return;
                }
                intent=new Intent(MyAssetActivity.this,FaqActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_my_balance:
                //我的余额
                if (TextUtils.isEmpty(helper.getToken())) {
                    ToolUtil.goToLogin(this);
                } else {
                    Intent intent2 = new Intent(this, MyBalanceActivity.class);
                    intent2.putExtra("ISBALANCE", 0);
                    startActivity(intent2);
                }
                break;
            case R.id.ll_put_forward://提现记录
                if (TextUtils.isEmpty(helper.getToken())) {
                    ToolUtil.goToLogin(this);
                } else {
                    startActivity(new Intent(this,PutForwardActivity.class));
                }
                break;
            case R.id.ll_refund:
                if (TextUtils.isEmpty(helper.getToken())) {
                    ToolUtil.goToLogin(this);
                } else {
                    startActivity(new Intent(this,RefundActivity.class)
                            .putExtra("id",helper.getToken()));
                }
                break;
        }
    }
}
