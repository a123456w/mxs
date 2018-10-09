package com.guo.qlzx.maxiansheng.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;


import com.guo.qlzx.maxiansheng.activity.OrderActivity;
import com.guo.qlzx.maxiansheng.activity.SettlementActivity;
import com.guo.qlzx.maxiansheng.bean.VipBean;
import com.guo.qlzx.maxiansheng.event.RefreshNumEvent;
import com.guo.qlzx.maxiansheng.event.WxPayEvent;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.EventBusUtil;
import com.qlzx.mylibrary.util.LogUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by 87901 on 2016/10/26.
 * 微信支付回调
 */

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  setContentView(R.layout.pay_result);
        if (com.guo.qlzx.maxiansheng.common.Constants.iwxapi == null) {
            api = WXAPIFactory.createWXAPI(this, Constants.WEIXIN_APP_ID);
            com.guo.qlzx.maxiansheng.common.Constants.iwxapi = api;
        } else {
            api = com.guo.qlzx.maxiansheng.common.Constants.iwxapi;
        }

        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {

    }

    @Override
    public void onResp(BaseResp resp) {
        LogUtil.d("onPayFinish, errCode = " + resp.errCode);

        if (resp.errCode == 0) {
            LogUtil.d("WxPayEntryActivity  微信支付成功");

            Toast.makeText(WXPayEntryActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
            finish();

            EventBusUtil.post(new VipBean(true));

            EventBusUtil.post(new RefreshNumEvent());

        } else if (resp.errCode == -1) {
            LogUtil.d("WxPayEntryActivity  支付失败");

            Toast.makeText(WXPayEntryActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
            finish();

            EventBusUtil.post(new VipBean(false));

        } else if (resp.errCode == -2) {
            LogUtil.d("WxPayEntryActivity  支付取消");

            Toast.makeText(WXPayEntryActivity.this, "支付取消", Toast.LENGTH_SHORT).show();
            finish();

            EventBusUtil.post(new VipBean(false));

        }
        startActivity(new Intent(this, OrderActivity.class));

    }


}
