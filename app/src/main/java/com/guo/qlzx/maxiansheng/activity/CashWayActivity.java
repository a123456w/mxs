package com.guo.qlzx.maxiansheng.activity;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;

import com.guo.qlzx.maxiansheng.R;
import com.guo.qlzx.maxiansheng.bean.IsAlipayBean;
import com.guo.qlzx.maxiansheng.http.RemoteApi;
import com.guo.qlzx.maxiansheng.util.ToolUtil;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 李 on 2018/4/18.
 * 选择支付方式
 */

public class CashWayActivity extends BaseActivity {
    @BindView(R.id.ll_whact)
    LinearLayout llWhact;
    private PreferencesHelper helper;
    @Override
    public int getContentView() {
        return R.layout.activity_cash_way;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("余额提现");
        titleBar.setLeftImageRes(R.drawable.ic_back_gray);
        llWhact.setVisibility(View.GONE);
        helper=new PreferencesHelper(this);
    }

    @Override
    public void getData() {
    }

    @OnClick({R.id.ll_whact, R.id.ll_alipay})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.ll_whact:
                intent = new Intent(CashWayActivity.this, CashBalanceActivity.class);
                intent.putExtra("PAYWAY", 1);
                startActivity(intent);
                break;
            case R.id.ll_alipay:
                alipay(helper.getToken() );
                break;
        }
    }
    //显示我的余额
    private void alipay(String token){
        HttpHelp.getInstance().create(RemoteApi.class).isAlipay(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<IsAlipayBean>>(this, null) {
                    @Override
                    public void onNext(BaseBean<IsAlipayBean> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code==0) {
                            if (1== baseBean.data.getAlipay_status()){
                                Intent intent = new Intent(CashWayActivity.this, CashBalanceActivity.class);
                                intent.putExtra("PAYWAY", 2);
                                startActivityForResult(intent,100);
                            }else {
                                Intent intent = new Intent(CashWayActivity.this, ALiPayActivity.class);
                                startActivityForResult(intent,100);
                            }

                        }else if (baseBean.code==4){
                            ToolUtil.goToLogin(CashWayActivity.this);
                        }else {
                            ToastUtil.showToast(CashWayActivity.this,baseBean.message);
                        }
                    }
                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        ToastUtil.showToast(CashWayActivity.this,"网络链接错误");
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 200){
            startActivity(new Intent(CashWayActivity.this,PutForwardActivity.class)
                    .putExtra("id",helper.getToken()));
            finish();
        }
    }
}
