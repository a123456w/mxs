package com.guo.qlzx.maxiansheng.util;

import android.content.Context;
import android.util.Log;

import com.guo.qlzx.maxiansheng.activity.SettlementActivity;
import com.guo.qlzx.maxiansheng.bean.PayBean;
import com.guo.qlzx.maxiansheng.bean.WeixinPayBean;
import com.guo.qlzx.maxiansheng.http.RemoteApi;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.LogUtil;
import com.qlzx.mylibrary.util.ToastUtil;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by 87901 on 2016/11/22.
 * 微信支付
 */

public class WxPayUtil {
    private IWXAPI wxApi;

    public static void wxPay(String token, String order_sn, final Context context) {

        HttpHelp.getInstance().create(RemoteApi.class).weixinPay(token, order_sn, "2")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<WeixinPayBean>>(context, null) {

                    public IWXAPI wxApi;

                    @Override
                    public void onNext(BaseBean<WeixinPayBean> wxOrderBean) {
                        super.onNext(wxOrderBean);
                        if (wxOrderBean.code == 0) {

                            Log.i("XXX", wxOrderBean.code + "/" + wxOrderBean.message);

                            if (wxOrderBean != null && wxOrderBean.data != null) {
                                WeixinPayBean order = wxOrderBean.data;
                                if(com.guo.qlzx.maxiansheng.common.Constants.iwxapi==null){
                                    wxApi = WXAPIFactory.createWXAPI(context, Constants.WEIXIN_APP_ID, false);
                                  //  wxApi.registerApp(order.getAppid());
                                    com.guo.qlzx.maxiansheng.common.Constants.iwxapi  = wxApi;
                                }else {
                                    wxApi= com.guo.qlzx.maxiansheng.common.Constants.iwxapi;
                                }
                                wxApi.registerApp(order.getAppid());
                                LogUtil.d("appId---" + order.getAppid());
                                LogUtil.d("appId---2" + order.toString());

                                PayReq req = new PayReq();
                                //req.appId = "wxf8b4f85f3a794e77";  // 测试用appId
                                req.appId = order.getAppid();
                                req.partnerId = order.getPartnerid();
                                req.prepayId = order.getPrepayid();
                                req.nonceStr = order.getNoncestr();
                                req.timeStamp = order.getTimestamp() + "";
                                req.packageValue = order.getPackageX();
                                req.sign = order.getSign();
//                    req.extData = "app data"; // optional
                                // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                                wxApi.sendReq(req);
                            }

                        } else {
                            ToastUtil.showToast(context, wxOrderBean.message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);

                    }
                });
    }


}
