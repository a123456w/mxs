package com.guo.qlzx.maxiansheng.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.NestedScrollView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.guo.qlzx.maxiansheng.R;
import com.guo.qlzx.maxiansheng.adapter.ToPayAgainDetailsAdapter;
import com.guo.qlzx.maxiansheng.bean.AuthResult;
import com.guo.qlzx.maxiansheng.bean.IsPasswordExitsBean;
import com.guo.qlzx.maxiansheng.bean.PayResult;
import com.guo.qlzx.maxiansheng.bean.TopPayAgainDetailsBean;
import com.guo.qlzx.maxiansheng.bean.TotalSumBean;
import com.guo.qlzx.maxiansheng.bean.WeixinPayBean;
import com.guo.qlzx.maxiansheng.common.Constants;
import com.guo.qlzx.maxiansheng.http.RemoteApi;
import com.guo.qlzx.maxiansheng.util.ToolUtil;
import com.guo.qlzx.maxiansheng.util.WxPayUtil;
import com.guo.qlzx.maxiansheng.util.dialog.AddressDeleteDialog;
import com.guo.qlzx.maxiansheng.util.dialog.MemberCenterDialog;
import com.guo.qlzx.maxiansheng.util.dialog.PayPasswordDialog;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.LogUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.qlzx.mylibrary.widget.NoScrollListView;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.math.BigDecimal;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 李 on 2018/5/24.
 * <p>
 * 二次支付 接口未接
 */

public class ToPayAgainDetailsActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.tv_state)
    TextView tvState;
    @BindView(R.id.tv_name_hint)
    TextView tvNameHint;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.goods_list)
    NoScrollListView goodsList;
    @BindView(R.id.tv_all_money)
    TextView tvAllMoney;
    @BindView(R.id.tv_back_money)
    TextView tvBackMoney;
    @BindView(R.id.tv_hint)
    TextView tvHint;
    @BindView(R.id.can_content_view)
    NestedScrollView canContentView;
    @BindView(R.id.tv_pay)
    TextView tvPay;
    @BindView(R.id.ll_pay)
    LinearLayout llPay;
    @BindView(R.id.tv_new_money)
    TextView tvNewMoney;
    @BindView(R.id.tv_difference)
    TextView tvDifference;
    @BindView(R.id.tv_make)
    TextView tvMake;
    private String orderId = "";
    private String mBalance = "";
    private PreferencesHelper helper;
    private MemberCenterDialog dialog;//支付方式弹窗
    public boolean havePassword;

    private ToPayAgainDetailsAdapter adapter;
    private TopPayAgainDetailsBean detail;
    private PayPasswordDialog passwordDialog;
    private String ordergoods;

    @Override
    public int getContentView() {
        return R.layout.activity_again_pay;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("订单详情");
        tvPay.setOnClickListener(this);
        orderId = getIntent().getStringExtra("ORDERID");
        ordergoods = getIntent().getStringExtra("ORDERGOODS");
        Log.d("xiaou",ordergoods+"");
        helper = new PreferencesHelper(this);
        adapter = new ToPayAgainDetailsAdapter(goodsList);
        goodsList.setAdapter(adapter);
    }

    @Override
    public void getData() {
        getCashBalanceData(helper.getToken());
        checkPassWord(helper.getToken());
        Log.i("TAG", orderId);
        getDetails(helper.getToken(), orderId,ordergoods);
    }

    /**
     * 获取订单信息
     *
     * @param token
     * @param id
     */
    private void getDetails(String token, String id,String order_goods) {
        HttpHelp.getInstance().create(RemoteApi.class).toPayAgainDetails(token, id,order_goods)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<TopPayAgainDetailsBean>>(ToPayAgainDetailsActivity.this, null) {
                    @Override
                    public void onNext(BaseBean<TopPayAgainDetailsBean> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            detail = baseBean.data;
                            if(detail == null){
                                ToastUtil.showToast(ToPayAgainDetailsActivity.this,"系统异常，请联系管理员");
                                finish();
                                return;
                            }
                            Log.i("TAG", detail.getOrder().getPay_code());
                            adapter.setData(baseBean.data.getGoods());
                            TopPayAgainDetailsBean.OrderBean order = baseBean.data.getOrder();
                            TopPayAgainDetailsBean.AddressBean address = baseBean.data.getAddress();
                            //订单
                            if ("0".equals(order.getIs_pay())) {
                                tvState.setText("待付款");
                                llPay.setVisibility(View.VISIBLE);
                            } else if ("1".equals(order.getIs_pay())) {
                                tvState.setText("已付款");
                                llPay.setVisibility(View.GONE);
                            }
                            tvBackMoney.setText("¥" + order.getCompen_price());
                            tvAllMoney.setText("¥" + order.getTotle_amount());
                            tvNewMoney.setText("¥" + order.getEnd_amount());
                            tvDifference.setText("-¥"+order.getChange_price());
                            tvMake.setText("+¥"+order.getCompen_price());
                            //地址
                            tvAddress.setText(address.getProvince() + address.getCity() + address.getAddress());
                            tvName.setText(address.getName() + "");
                            tvPhone.setText(address.getMobile() + "");

                        } else if (baseBean.code == 4) {
                            ToolUtil.goToLogin(ToPayAgainDetailsActivity.this);
                        } else {
                            ToastUtil.showToast(ToPayAgainDetailsActivity.this, baseBean.message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        ToastUtil.showToast(ToPayAgainDetailsActivity.this, "系统异常，请联系管理员 ");
                        finish();
                    }
                });
    }

    private void upDataUI(TopPayAgainDetailsBean data) {

    }

    //以下  为支付需要= =
    @Override
    public void onClick(View v) {
        //立即支付
        showPayWayDialog(mBalance, detail.getOrder().getCompen_price(), orderId);
    }

    //我的余额
    public void getCashBalanceData(String token) {
        HttpHelp.getInstance().create(RemoteApi.class).getBalance(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<TotalSumBean>>(this, null) {
                    @Override
                    public void onNext(BaseBean<TotalSumBean> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            mBalance = baseBean.data.getUser_money();
                        } else if (baseBean.code == 4) {
                            ToolUtil.goToLogin(ToPayAgainDetailsActivity.this);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
    }

    /**
     * 判断是否有支付密码
     *
     * @param token
     */
    private void checkPassWord(String token) {
        HttpHelp.getInstance().create(RemoteApi.class).isPasswordExits(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<IsPasswordExitsBean>>(this, null) {
                    @Override
                    public void onNext(BaseBean<IsPasswordExitsBean> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            if (baseBean.data.getPaypwd_status() == 1) {
                                //已设置
                                havePassword = true;
                            } else {
                                //未设置
                                havePassword = false;
                            }

                        } else if (baseBean.code == 4) {
                            ToolUtil.goToLogin(ToPayAgainDetailsActivity.this);
                        } else {

                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);

                    }
                });
    }

    /**
     * 支付弹窗
     *
     * @param myMoney
     * @param payMoney
     */
    private void showPayWayDialog(final String myMoney, final String payMoney, final String order_sn) {
        dialog = new MemberCenterDialog(ToPayAgainDetailsActivity.this, myMoney, payMoney);
        dialog.setOnCancelClickListener(new MemberCenterDialog.OnCancelClickListener() {
            @Override
            public void onCancelListener() {
                dialog.cancel();
            }
        });
        dialog.setOnGotoPayClickListener(new MemberCenterDialog.OnGotoPayClickListener() {
            @Override
            public void onGotoPayListener(int state) {
                switch (state) {
                    case 0:
                        ToastUtil.showToast(ToPayAgainDetailsActivity.this, "请选择支付方式");
                        break;
                    case 1:
                        ToastUtil.showToast(ToPayAgainDetailsActivity.this, "余额支付");
                        if (new BigDecimal(myMoney).compareTo(new BigDecimal(payMoney)) < 0) {
                            ToastUtil.showToast(ToPayAgainDetailsActivity.this, "余额不足");
                        } else {
                            if (havePassword) {
                                //有支付密码
                                showEditPassword(order_sn);
                            } else {
                                //没有支付密码
                                final AddressDeleteDialog dialog = new AddressDeleteDialog(ToPayAgainDetailsActivity.this);

                                dialog.setGoToUseOnclickListener(new AddressDeleteDialog.onGoToUseOnclickListener() {
                                    @Override
                                    public void onUseClick() {
                                        startActivity(new Intent(ToPayAgainDetailsActivity.this, SetPayPasswordActivity.class));
                                    }
                                });
                                dialog.show();
                                dialog.setTitle("尚未设置交易密码,是否前往设置?");
                            }

                        }
                        break;
                    case 2:
                        //weixinPay(helper.getToken(),order_sn);
                        weixin(helper.getToken(), order_sn);
                        break;
                    case 3:
                        aliPay(helper.getToken(), order_sn);
                        break;
                }
            }
        });
        dialog.setOnCancelClickListener(new MemberCenterDialog.OnCancelClickListener() {
            @Override
            public void onCancelListener() {

            }
        });
        dialog.show();
    }

    /**
     * 密码框
     *
     * @param order_sn
     */
    private void showEditPassword(final String order_sn) {
        dialog.cancel();
        passwordDialog = PayPasswordDialog.newInstace(this);
        passwordDialog.setOnFinishClickListener(new PayPasswordDialog.OnFinishClickListener() {
            @Override
            public void onFinish(String psw) {
                pay(helper.getToken(), order_sn, psw);
            }
        });
        passwordDialog.setOnForgetClickListener(new PayPasswordDialog.OnForgetClickListener() {
            @Override
            public void onForget() {
                finish();
            }
        });
        passwordDialog.show(ToPayAgainDetailsActivity.this.getSupportFragmentManager(), "PassWordFragment");

    }

    /**
     * 支付
     *
     * @param token
     * @param order_sn
     * @param pay_password
     */
    private void pay(String token, String order_sn, String pay_password) {
        HttpHelp.getInstance().create(RemoteApi.class).pay(token, order_sn, pay_password, "1", "1")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<Object>>(this, null) {
                    @Override
                    public void onNext(BaseBean<Object> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            //余额支付
                            ToastUtil.showToast(ToPayAgainDetailsActivity.this, baseBean.message);

                            passwordDialog.dismiss();
                            getCashBalanceData(helper.getToken());
                            checkPassWord(helper.getToken());
                            getDetails(helper.getToken(), orderId,ordergoods);
                        } else if (baseBean.code == 4) {
                            passwordDialog.dismiss();
                            ToolUtil.goToLogin(ToPayAgainDetailsActivity.this);
                        } else {
                            passwordDialog.clear();
                            ToastUtil.showToast(ToPayAgainDetailsActivity.this, baseBean.message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        passwordDialog.clear();
                        ToastUtil.showToast(ToPayAgainDetailsActivity.this, "网络连接失败");
                    }
                });
    }

    /**
     * 支付宝支付(获取支付宝订单信息)
     *
     * @param token
     * @param order_sn
     */
    private void aliPay(String token, String order_sn) {
        HttpHelp.getInstance().create(RemoteApi.class).alipayAgain(token, order_sn, "3", "1")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<String>>(ToPayAgainDetailsActivity.this, null) {
                    @Override
                    public void onNext(BaseBean<String> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            LogUtil.i("支付宝：" + baseBean.data);
                            startAliPay(baseBean.data);
                        } else {
                            ToastUtil.showToast(ToPayAgainDetailsActivity.this, baseBean.message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);

                    }
                });
    }
    IWXAPI wxApi;
    private void weixin(String token, String order_sn) {
        HttpHelp.getInstance().create(RemoteApi.class).wechatpayAgain(token, order_sn, "2", "1")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<WeixinPayBean>>(ToPayAgainDetailsActivity.this, null) {
                    @Override
                    public void onNext(BaseBean<WeixinPayBean> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            LogUtil.i("TAG" + baseBean.data);
                            if (baseBean != null && baseBean.data != null) {
                                WeixinPayBean order = baseBean.data;
                                if(com.guo.qlzx.maxiansheng.common.Constants.iwxapi==null){
                                    wxApi = WXAPIFactory.createWXAPI(ToPayAgainDetailsActivity.this, com.qlzx.mylibrary.common.Constants.WEIXIN_APP_ID, false);
                                    com.guo.qlzx.maxiansheng.common.Constants.iwxapi = wxApi;
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
                                //weixinPay(helper.getToken(),baseBean.data);
                            }
                        } else {
                            ToastUtil.showToast(ToPayAgainDetailsActivity.this, baseBean.message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        ToastUtil.showToast(ToPayAgainDetailsActivity.this, throwable.getMessage());
                    }
                });
    }

    /**
     * 微信支付(获取支付宝订单信息)
     *
     * @param token
     * @param order_sn
     */
    private void weixinPay(String token, String order_sn) {
        WxPayUtil.wxPay(token, order_sn, ToPayAgainDetailsActivity.this);
    }

    /**
     * 发起支付宝支付
     *
     * @param orderInfo
     */
    private void startAliPay(final String orderInfo) {
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(ToPayAgainDetailsActivity.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Message msg = new Message();
                msg.what = Constants.SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        Toast.makeText(ToPayAgainDetailsActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(ToPayAgainDetailsActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    break;
                }
                case Constants.SDK_AUTH_FLAG: {
                    @SuppressWarnings("unchecked")
                    AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
                    String resultStatus = authResult.getResultStatus();

                    // 判断resultStatus 为“9000”且result_code
                    // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                    if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
                        // 获取alipay_open_id，调支付时作为参数extern_token 的value
                        // 传入，则支付账户为该授权账户
                        Toast.makeText(ToPayAgainDetailsActivity.this,
                                "授权成功\n" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        // 其他状态值则为授权失败
                        Toast.makeText(ToPayAgainDetailsActivity.this,
                                "授权失败" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT).show();

                    }
                    break;
                }
                default:
                    break;
            }
        }
    };

    @Override
    protected void onRestart() {
        super.onRestart();
        checkPassWord(new PreferencesHelper(ToPayAgainDetailsActivity.this).getToken());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
