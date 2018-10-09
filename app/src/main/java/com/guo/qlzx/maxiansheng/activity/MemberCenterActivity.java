package com.guo.qlzx.maxiansheng.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.guo.qlzx.maxiansheng.R;
import com.guo.qlzx.maxiansheng.adapter.MemberCenterAdapter;
import com.guo.qlzx.maxiansheng.adapter.MemberCenterRecommendAdapter;
import com.guo.qlzx.maxiansheng.adapter.MyMemberCenterAdapter;
import com.guo.qlzx.maxiansheng.bean.AuthResult;
import com.guo.qlzx.maxiansheng.bean.HomeRecommendListBean;
import com.guo.qlzx.maxiansheng.bean.IsPasswordExitsBean;
import com.guo.qlzx.maxiansheng.bean.MemberCenterBean;
import com.guo.qlzx.maxiansheng.bean.PayResult;
import com.guo.qlzx.maxiansheng.bean.SpecDialogBean;
import com.guo.qlzx.maxiansheng.bean.TotalSumBean;
import com.guo.qlzx.maxiansheng.bean.VipBean;
import com.guo.qlzx.maxiansheng.bean.WeixinPayBean;
import com.guo.qlzx.maxiansheng.event.ChangeHeaderEvent;
import com.guo.qlzx.maxiansheng.event.ShoppingCartEvent;
import com.guo.qlzx.maxiansheng.http.RemoteApi;
import com.guo.qlzx.maxiansheng.util.ToolUtil;
import com.guo.qlzx.maxiansheng.util.WxPayUtil;
import com.guo.qlzx.maxiansheng.util.costom.HorizontalListView;
import com.guo.qlzx.maxiansheng.util.dialog.AddressDeleteDialog;
import com.guo.qlzx.maxiansheng.util.dialog.MemberCenterDialog;
import com.guo.qlzx.maxiansheng.util.dialog.PayPasswordDialog;
import com.guo.qlzx.maxiansheng.util.dialog.SelectDialog;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.base.OnItemChildClickListener;
import com.qlzx.mylibrary.base.OnRVItemClickListener;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.EventBusUtil;
import com.qlzx.mylibrary.util.GlideUtil;
import com.qlzx.mylibrary.util.LogUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.qlzx.mylibrary.widget.ScrollLinearLayoutManager;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 李 on 2018/4/16.
 * 会员中心 全部完成
 */

public class MemberCenterActivity extends BaseActivity implements  OnItemChildClickListener, OnRVItemClickListener {

    @BindView(R.id.riv_img)
    ImageView rivImg;
    @BindView(R.id.tv_mobile)
    TextView tvMobile;
    @BindView(R.id.tv_vip)
    TextView tvVip;
    @BindView(R.id.tv_number)
    TextView tvNumber;
    @BindView(R.id.tv_day)
    TextView tvDay;
    @BindView(R.id.hlv_vip)
    RecyclerView hlvVip;
    @BindView(R.id.rl_member)
    RecyclerView rlMember;
    @BindView(R.id.tv_daytitle)
    TextView tvDaytitle;
    private MyMemberCenterAdapter adapter;

    private List<MemberCenterBean.PlusListBean> listBeans = new ArrayList<>();
    private MemberCenterBean centerBean;
    private PreferencesHelper helper;
    private MemberCenterDialog dialog;
    private String mBalance = "";
    private SelectDialog cartDialog;

    private int type = 0;
    private int vipId = 0;
    private MemberCenterRecommendAdapter recommendAdapter;
    private List<HomeRecommendListBean> recommendListBeans = new ArrayList<>();
    private  PayPasswordDialog passwordDialog;

    @Override
    public int getContentView() {
        return R.layout.activity_member_center;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("会员中心");
        helper = new PreferencesHelper(this);


        recommendAdapter = new MemberCenterRecommendAdapter(rlMember);
        rlMember.setLayoutManager(new GridLayoutManager(this, 2));
        rlMember.setAdapter(recommendAdapter);


        adapter = new MyMemberCenterAdapter(hlvVip);
        hlvVip.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));
        hlvVip.setAdapter(adapter);

        recommendAdapter.setOnItemChildClickListener(this);
        recommendAdapter.setOnRVItemClickListener(this);
        adapter.setOnItemChildClickListener(this);

        EventBusUtil.register(MemberCenterActivity.this);
    }

    @Override
    public void getData() {
        getMemberCenter(new PreferencesHelper(MemberCenterActivity.this).getToken());

        //type 0默认 首页推荐商品 1猜你喜欢 2热门推荐 3同类推荐
        // page 一页展示
        getRecommendData(1, 2);
    }

    //会员中心数据
    private void getMemberCenter(String token) {
        HttpHelp.getInstance().create(RemoteApi.class).getMemberCenter(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<MemberCenterBean>>(this, null) {
                    @Override
                    public void onNext(BaseBean<MemberCenterBean> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {

                            centerBean = baseBean.data;
                            listBeans = baseBean.data.getPlus_list();
                            adapter.setData(listBeans);
                            if (centerBean != null) {
                                GlideUtil.displayAvatar(MemberCenterActivity.this, Constants.IMG_HOST + centerBean.getImg(), rivImg);
                                tvMobile.setText(ToolUtil.setTingPhone(centerBean.getMobile()));
                                //0 是新人 1普通用户 2会员
                                int people = centerBean.getNew_people();
                                if (people == 0) {
                                    tvVip.setText("新人");
                                    tvDaytitle.setVisibility(View.GONE);
                                } else if (people == 1) {
                                    tvVip.setText("普通用户");
                                    tvDaytitle.setVisibility(View.GONE);
                                } else {
                                    tvVip.setText(centerBean.getMember_name());
                                    tvDay.setText(centerBean.getPlus_last_time()+"天");
                                }
                                tvNumber.setText(centerBean.getRapeseed());

                            }
                        } else if (baseBean.code == 4) {
                            ToolUtil.goToLogin(MemberCenterActivity.this);
                        } else {
                            ToastUtil.showToast(MemberCenterActivity.this, baseBean.message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        ToastUtil.showToast(MemberCenterActivity.this,"网络连接失败");
                        hlvVip.setVisibility(View.GONE);
                    }
                });
    }

    //显示我的余额
    private void getCashBalanceData(String token,final String price, final int id) {
        HttpHelp.getInstance().create(RemoteApi.class).getBalance(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<TotalSumBean>>(this, null) {
                    @Override
                    public void onNext(BaseBean<TotalSumBean> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            mBalance = baseBean.data.getUser_money();
                            goToPay(price,id);
                        } else if (baseBean.code == 4) {
                            ToolUtil.goToLogin(MemberCenterActivity.this);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
    }

    IWXAPI wxApi;
    //去支付
    private void gotoPayVipByWeixin(String token, final int type, int id, String password) {

        HttpHelp.getInstance().create(RemoteApi.class).gotoPayVipByWeixin(token, type, id, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<WeixinPayBean>>(this, null) {
                    @Override
                    public void onNext(BaseBean<WeixinPayBean> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {

                            if (type == 3) {

                            } else if (type == 2) {
                                //微信
//                                {"code":0,"message":"\u7b7e\u540d\u6210\u529f","data":{"prepayid":"wx141710335001576fb168db282849802465",
// "appid":"wx31005ccd063e38f6","partnerid":"1503882781","
// package":"Sign=WXPay","noncestr":"RW5AYmLdISlJyyp6","timestamp":1526289033,"sign":"5B382B1A066AA8EB3197D83CCEF979B1"}}

                                if (baseBean != null && baseBean.data != null) {
                                    WeixinPayBean order = baseBean.data;

                                 //   wxApi  = WXAPIFactory.createWXAPI(MemberCenterActivity.this, order.getAppid());
                                    if(com.guo.qlzx.maxiansheng.common.Constants.iwxapi==null){
                                        wxApi = WXAPIFactory.createWXAPI(MemberCenterActivity.this, Constants.WEIXIN_APP_ID, false);
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

                            } else if (type == 1) {

                            }
                        } else if (baseBean.code == 4) {
                            ToolUtil.loseToLogin(MemberCenterActivity.this);
                        } else {
                            ToastUtil.showToast(MemberCenterActivity.this, baseBean.message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
    }

    private void gotoPayVip(String token, final int type, int id, String password) {
        HttpHelp.getInstance().create(RemoteApi.class).gotoPayVip(token,type,id,password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<String>>(this, null) {
                    @Override
                    public void onNext(BaseBean<String> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            if (type == 3) {
                                //支付宝
                                startAliPay(baseBean.data);
                            } else if (type == 2) {
                                //微信
                            } else if (type == 1) {
                                //余额
                                passwordDialog.dismiss();
                                ToastUtil.showToast(MemberCenterActivity.this, baseBean.message);
                                getMemberCenter(helper.getToken());//更新数据
                            }
                        } else if (baseBean.code == 4) {
                            ToolUtil.loseToLogin(MemberCenterActivity.this);
                        } else {
                            passwordDialog.clear();
                            ToastUtil.showToast(MemberCenterActivity.this, baseBean.message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        passwordDialog.dismiss();
                        ToastUtil.showToast(MemberCenterActivity.this,"支付失败");
                    }
                });
    }


    /**
     * 4.获取热销数据
     */
    public void getRecommendData(int page, int type) {
        HttpHelp.getInstance().create(RemoteApi.class).getHomeRecommendData(page, type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<List<HomeRecommendListBean>>>(this, null) {
                    @Override
                    public void onNext(BaseBean<List<HomeRecommendListBean>> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            recommendListBeans = baseBean.data;
                            recommendAdapter.setData(recommendListBeans);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);

                    }
                });
    }

    //是否未设置支付密码
    private void isPasswordExits(String token) {
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
                                showEditPassword();
                            } else if (baseBean.data.getPaypwd_status() == 0) {
                                //未设置
                                dialog.cancel();
                                final AddressDeleteDialog dialog = new AddressDeleteDialog(MemberCenterActivity.this);

                                dialog.setGoToUseOnclickListener(new AddressDeleteDialog.onGoToUseOnclickListener() {
                                    @Override
                                    public void onUseClick() {
                                        startActivity(new Intent(MemberCenterActivity.this, SetPayPasswordActivity.class));
                                    }
                                });
                                dialog.show();
                                dialog.setTitle("尚未设置交易密码,是否前往设置?");
                            }
                        } else if (baseBean.code == 4) {
                            ToolUtil.goToLogin(MemberCenterActivity.this);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
    }


    private void goToPay(final String price, final int id){
        if (TextUtils.isEmpty(mBalance)) {
            return;
        }
        dialog = new MemberCenterDialog(this, mBalance, price);
        dialog.setOnCancelClickListener(new MemberCenterDialog.OnCancelClickListener() {
            @Override
            public void onCancelListener() {
                dialog.cancel();
            }
        });
        dialog.setOnGotoPayClickListener(new MemberCenterDialog.OnGotoPayClickListener() {
            @Override
            public void onGotoPayListener(int state) {
                if (state == 0) {
                    ToastUtil.showToast(MemberCenterActivity.this, "请选择支付方式");
                } else if (state == 1) {
                    //余额支付
                    type = 1;
                    vipId = id;
                    isPasswordExits(helper.getToken());
                } else if (state == 2) {
                    //微信支付
                    type = 2;
                    vipId = id;
                    dialog.cancel();
                    gotoPayVipByWeixin(helper.getToken(), type, vipId, "");

                } else if (state == 3) {
                    //阿里支付
                    type = 3;
                    vipId = id;
                    dialog.cancel();

                    gotoPayVip(helper.getToken(), type, vipId, "");

                }
            }
        });
        dialog.show();
    }
    //点击添加按钮
    @Override
    public void onItemChildClick(ViewGroup parent, View childView, int position) {
        switch (childView.getId()){
            case R.id.tv_buy:
                getCashBalanceData(helper.getToken(),adapter.getItem(position).getPrice(),adapter.getItem(position).getId());        //会员卡 点击立即支付
                break;
            case R.id.iv_add:
                showAddDialog(recommendAdapter.getItem(position));
                break;
        }
    }

    //热销点击item
    @Override
    public void onRVItemClick(ViewGroup parent, View itemView, int position) {
        Intent intent = new Intent(MemberCenterActivity.this, ProductDetailsActivity.class);
        intent.putExtra("goods_id", String.valueOf(recommendAdapter.getItem(position).getGoods_id()));
        startActivity(intent);
    }

    //热销 添加购物车
    private void showAddDialog(HomeRecommendListBean bean) {
        if (bean.getSpec_type() == 0) {
            //不存在规格
            addShoppingCart(helper.getToken(), String.valueOf(bean.getGoods_id()), "", "", 1);
        } else {
            //存在规格 选择规格
            cartDialog = new SelectDialog(MemberCenterActivity.this,bean.getType());
            SpecDialogBean specDialogBean = new SpecDialogBean();
            specDialogBean.setGoods_id(bean.getGoods_id());
            specDialogBean.setImg(bean.getImg());
            specDialogBean.setSpec_name(bean.getSpec_key_name());
            specDialogBean.setPlus_price(bean.getPlus_price());
            specDialogBean.setShop_price(bean.getShop_price());
            cartDialog.setGoToUseOnclickListener(new SelectDialog.onGoToUseOnclickListener() {
                @Override
                public void onUseClick(String goods_id, String spec_id, String service_id, int num,int s
                        ,double price,double vipPrice) {
                    if(s == 0){
                        ToastUtil.showToast(MemberCenterActivity.this,"当前商品库存为0份");
                        return;
                    }
                    addShoppingCart(helper.getToken(), goods_id, service_id, spec_id, num);
                }
            });
            cartDialog.show();
            cartDialog.setData(specDialogBean);
        }
    }


    /**
     * 加入购物车
     *
     * @param token
     * @param goods_id
     * @param service_id
     * @param spec_id
     * @param goods_num
     */
    private void addShoppingCart(String token, String goods_id, String service_id, String spec_id, int goods_num) {
        HttpHelp.getInstance().create(RemoteApi.class).addCart(token, goods_id, service_id, spec_id, goods_num)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<Object>>(MemberCenterActivity.this, null) {
                    @Override
                    public void onNext(BaseBean<Object> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            EventBusUtil.post(new ShoppingCartEvent());
                            ToastUtil.showToast(MemberCenterActivity.this, baseBean.message);
                        } else if (baseBean.code == 4) {
                            ToolUtil.goToLogin(MemberCenterActivity.this);
                        } else {
                            ToastUtil.showToast(MemberCenterActivity.this, baseBean.message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);

                    }
                });
    }


    //弹出密码/输入密码/完毕后的回调
    private void showEditPassword() {
        dialog.cancel();
         passwordDialog = PayPasswordDialog.newInstace(this);
        passwordDialog.setOnFinishClickListener(new PayPasswordDialog.OnFinishClickListener() {
            @Override
            public void onFinish(String psw) {
                gotoPayVip(helper.getToken(), type, vipId, psw);
            }
        });
        passwordDialog.show(getSupportFragmentManager(), "PassWordFragment");
    }



    /**
     * 微信支付(获取支付宝订单信息)
     * @param token
     * @param order_sn
     */
    private void weixinPay(String token, String order_sn) {
        WxPayUtil.wxPay(token,order_sn,MemberCenterActivity.this);
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
                PayTask alipay = new PayTask(MemberCenterActivity.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Message msg = new Message();
                msg.what = com.guo.qlzx.maxiansheng.common.Constants.SDK_PAY_FLAG;
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
                case com.guo.qlzx.maxiansheng.common.Constants.SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    Log.e("tag",resultStatus);
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        Toast.makeText(MemberCenterActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(MemberCenterActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    break;
                }
                case com.guo.qlzx.maxiansheng.common.Constants.SDK_AUTH_FLAG: {
                    @SuppressWarnings("unchecked")
                    AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
                    String resultStatus = authResult.getResultStatus();

                    // 判断resultStatus 为“9000”且result_code
                    // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                    if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
                        // 获取alipay_open_id，调支付时作为参数extern_token 的value
                        // 传入，则支付账户为该授权账户
                        Toast.makeText(MemberCenterActivity.this,
                                "授权成功\n" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        // 其他状态值则为授权失败
                        Toast.makeText(MemberCenterActivity.this,
                                "授权失败" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT).show();

                    }
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void VipBean(VipBean event) {
        Boolean paySucceed = event.getPaySucceed();
        if (paySucceed) {
            //更新数据
            getMemberCenter(helper.getToken());
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBusUtil.post(new ChangeHeaderEvent());
        EventBusUtil.unregister(MemberCenterActivity.this);
    }
}
