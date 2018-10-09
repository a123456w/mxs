package com.guo.qlzx.maxiansheng.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.guo.qlzx.maxiansheng.event.WxSuccessChangedEvent;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.util.EventBusUtil;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import cn.jiguang.share.wechat.WeChatHandleActivity;

import static android.provider.UserDictionary.Words.APP_ID;

/**
 * Created by 李 on 2018/5/7.
 */

public class WXEntryActivity extends WeChatHandleActivity  implements IWXAPIEventHandler {
    IWXAPI api;
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
       // WeChatModule.getInstance().api.handleIntent(getIntent(), this);
        //如果没回调onResp，八成是这句没有写
        if(com.guo.qlzx.maxiansheng.common.Constants.iwxapi==null){
            api = WXAPIFactory.createWXAPI(this, Constants.WEIXIN_APP_ID, false);
            api.registerApp(Constants.WEIXIN_APP_ID);
            com.guo.qlzx.maxiansheng.common.Constants.iwxapi  = api;
        }else {
            api= com.guo.qlzx.maxiansheng.common.Constants.iwxapi;
            api.registerApp(Constants.WEIXIN_APP_ID);
        }
        api.handleIntent(getIntent(),  this);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }


    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp resp) {
        //在这个方法中处理微信传回的数据
        // 形参resp 有下面两个个属性比较重要
        // 1.resp.errCode
        // 2.resp.transaction则是在分享数据的时候手动指定的字符创,用来分辨是那次分享(参照4.中req.transaction)
       WxSuccessChangedEvent wxSuccessChangedEvent= new  WxSuccessChangedEvent();
       wxSuccessChangedEvent.setCode(resp.errCode);
        wxSuccessChangedEvent.setType(resp.getType());
        EventBusUtil.post(wxSuccessChangedEvent);
        finish();
    }


}
