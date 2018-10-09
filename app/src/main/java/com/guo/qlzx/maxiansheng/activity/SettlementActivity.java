package com.guo.qlzx.maxiansheng.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.guo.qlzx.maxiansheng.R;
import com.guo.qlzx.maxiansheng.adapter.SettlementAdapter;
import com.guo.qlzx.maxiansheng.bean.AuthResult;
import com.guo.qlzx.maxiansheng.bean.CouponListBean;
import com.guo.qlzx.maxiansheng.bean.DistriTimeBean;
import com.guo.qlzx.maxiansheng.bean.IndexBean;
import com.guo.qlzx.maxiansheng.bean.IsPasswordExitsBean;
import com.guo.qlzx.maxiansheng.bean.OrderCouponBean;
import com.guo.qlzx.maxiansheng.bean.PayResult;
import com.guo.qlzx.maxiansheng.bean.SubmitOrderBean;
import com.guo.qlzx.maxiansheng.common.Constants;
import com.guo.qlzx.maxiansheng.event.ChangeHeaderEvent;
import com.guo.qlzx.maxiansheng.event.OrderNumberEvent;
import com.guo.qlzx.maxiansheng.event.RefreshNumEvent;
import com.guo.qlzx.maxiansheng.event.SetPasswordEvent;
import com.guo.qlzx.maxiansheng.event.ShoppingCartEvent;
import com.guo.qlzx.maxiansheng.http.RemoteApi;
import com.guo.qlzx.maxiansheng.util.ToolUtil;
import com.guo.qlzx.maxiansheng.util.WxPayUtil;
import com.guo.qlzx.maxiansheng.util.dialog.AddressDeleteDialog;
import com.guo.qlzx.maxiansheng.util.dialog.DeleteGoodsDialog;
import com.guo.qlzx.maxiansheng.util.dialog.DistriTimeDialog;
import com.guo.qlzx.maxiansheng.util.dialog.MemberCenterDialog;
import com.guo.qlzx.maxiansheng.util.dialog.PayPasswordDialog;
import com.guo.qlzx.maxiansheng.util.dialog.SaleDistriTimeDialog;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.EventBusUtil;
import com.qlzx.mylibrary.util.LogUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.qlzx.mylibrary.widget.NoScrollListView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.qlzx.mylibrary.util.DateUtil.FORMAT_HM;

/**
 * Created by Administrator on 2018/4/19.
 * describe 确认订单
 */

public class SettlementActivity extends BaseActivity {
    @BindView(R.id.iv_iz)
    ImageView ivIz;
    @BindView(R.id.tv_address_name)
    TextView tvAddressName;
    @BindView(R.id.tv_address_phone)
    TextView tvAddressPhone;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.iv_dz)
    ImageView ivDz;
    @BindView(R.id.delivery_time)
    LinearLayout deliveryTime;
    @BindView(R.id.goods_list)
    NoScrollListView goodsList;
    @BindView(R.id.isRapeseed)
    CheckBox isRapeseed;
    @BindView(R.id.coupon)
    RelativeLayout coupon;
    @BindView(R.id.distribution_fee)
    TextView distributionFee;
    @BindView(R.id.ll_jinggao)
    LinearLayout llJinggao;
    @BindView(R.id.real_payment)
    TextView realPayment;
    @BindView(R.id.tv_determine)
    TextView tvDetermine;
    @BindView(R.id.ll_botton)
    LinearLayout llBotton;
    @BindView(R.id.tv_xuanzdiz)
    TextView tvXuanzdiz;
    @BindView(R.id.rl_xaunzdiz)
    RelativeLayout rlXaunzdiz;
    @BindView(R.id.tv_caizi)
    TextView tvCaizi;
    @BindView(R.id.tv_order_money)
    TextView tvOrderMoney;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.iv_free_arrow)
    ImageView ivFreeArrow;
    @BindView(R.id.tv_free_money)
    TextView tvFreeMoney;
    @BindView(R.id.ll_caizi)
    LinearLayout llCaizi;
    @BindView(R.id.tv_money)
    TextView tvMoney;
    private SettlementAdapter settlementAdapter;
    private List<IndexBean.OrderListBean> listBeans = new ArrayList<>();


    private IndexBean indexBean;
    private DistriTimeBean timeBean;
    private MemberCenterDialog dialog;//支付方式弹窗

    private String allMoney;
    private String payMoney;//实际支付钱数
    private String freeId = ""; //优惠卷id
    private String freeMoney = "0";//优惠卷 优惠钱数
    private String rapeseed = "0";//使用菜籽数
    private String addressId;
    private String startTime, endTime;

    private String order_sn;//提交订单后得到的 订单编号
    private boolean havePassword = false;

    private String goods_id, spec_key, service_id;
    private int goods_num;
    private int rapeseedSelected = 0;//是否使用菜籽  0不使用，1使用
    private int type = 0;//0 购物车结算，1立即购买
    private String enDTime = "";

    private List<OrderCouponBean> myListCouponListBean = new ArrayList<>();
    private AddressDeleteDialog deleteDialog;

    boolean isLoad = true;   // 给判定增加一个变量，作用是返回的数不再提示优惠券弹框

    private PayPasswordDialog passwordDialog;
    private String differ_money;

    @Override
    public int getContentView() {
        return R.layout.activity_settlement;
    }

    @Override
    public void initView() {
        titleBar.setTitleText("确认订单");
        ButterKnife.bind(this);
        EventBusUtil.register(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        settlementAdapter = new SettlementAdapter(goodsList);
        goodsList.setAdapter(settlementAdapter);


        goods_id = getIntent().getStringExtra("goods_id");
        spec_key = getIntent().getStringExtra("spec_key");
        service_id = getIntent().getStringExtra("service_id");
        goods_num = getIntent().getIntExtra("goods_num", 1);
        enDTime = getIntent().getStringExtra("ENDTIME");

    }

    @Override
    public void getData() {
        showLoadingDialog("加载中...");
        checkPassWord(new PreferencesHelper(SettlementActivity.this).getToken());

        if (TextUtils.isEmpty(goods_id)) {
            //购物车进入
            type = 0;
            getIndex(new PreferencesHelper(SettlementActivity.this).getToken(), rapeseedSelected, null, null, null, 1);
        } else {
            //立即购买进入
            type = 1;
            getIndex(new PreferencesHelper(SettlementActivity.this).getToken(), rapeseedSelected, goods_id, spec_key, service_id, goods_num);
        }
        enDTime = getIntent().getStringExtra("ENDTIME");
        String dayStart = getIntent().getStringExtra("DAYSTART");
        String dayEnd = getIntent().getStringExtra("DAYEND");
        if (enDTime != null && !"".equals(enDTime)) {
            timeBean = new DistriTimeBean();
            timeBean.setBegin_time(dayStart);
            timeBean.setEnd_start(dayEnd);
            timeBean.setNow_time(enDTime);
            timeBean.setTimes(1800);
        } else {
            getTime();
        }
    }

    private void showDialog(String msg) {
        deleteDialog = new AddressDeleteDialog(this);
        deleteDialog.setGoToUseOnclickListener(new AddressDeleteDialog.onGoToUseOnclickListener() {
            @Override
            public void onUseClick() {
                //跳转到优惠卷列表
                Intent intent = new Intent(SettlementActivity.this, OrderCouponActivity.class);
                intent.putExtra("order_price", indexBean.getFood().getOrder_money());
                startActivityForResult(intent, 100);
                deleteDialog.cancel();
//                if (!"0".equals(differ_money)) {
//                    deleteDialog = new AddressDeleteDialog(SettlementActivity.this);
//                    deleteDialog.setGoToUseOnclickListener(new AddressDeleteDialog.onGoToUseOnclickListener() {
//                        @Override
//                        public void onUseClick() {
//                            Intent intent = new Intent(SettlementActivity.this, SearchListActivity.class);
//                            intent.putExtra("search_txt", " ");
//                            startActivity(intent);
//                            //startActivity(new Intent(SettlementActivity.this,SearchListActivity.class));
//                        }
//                    });
//                    deleteDialog.show();
//                    deleteDialog.setTitle("还差" + differ_money + "元包邮，是否去凑单？");
//                }
            }
        });
        deleteDialog.setCancelOnclickListener(new AddressDeleteDialog.onCancelOnclickListener() {
            @Override
            public void onCancelClick() {
                deleteDialog.cancel();
//                if (!"0".equals(differ_money)) {
//                    deleteDialog = new AddressDeleteDialog(SettlementActivity.this);
//                    deleteDialog.setGoToUseOnclickListener(new AddressDeleteDialog.onGoToUseOnclickListener() {
//                        @Override
//                        public void onUseClick() {
//                            Intent intent = new Intent(SettlementActivity.this, SearchListActivity.class);
//                            intent.putExtra("search_txt", " ");
//                            startActivity(intent);
//                            //startActivity(new Intent(SettlementActivity.this,SearchListActivity.class));
//                        }
//                    });
//                    deleteDialog.show();
//                    deleteDialog.setTitle("还差" + differ_money + "元包邮，是否去凑单？");
//                }
            }
        });
        deleteDialog.show();
        deleteDialog.setTitle(msg);
    }

    /**
     * 优惠券
     *
     * @param
     * @param token
     */
    private void getUserCoupon(String token, String money) {
        HttpHelp.getInstance().create(RemoteApi.class).useCoupon(token, money)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<List<OrderCouponBean>>>(this, null) {
                    @Override
                    public void onNext(BaseBean<List<OrderCouponBean>> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            myListCouponListBean = baseBean.data;
                            if (myListCouponListBean.size() > 0) {
                                showDialog("您有可用优惠券");
                            } else {

                            }
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
    }


    private void getIndex(String token, final int rapeseed_Selected, String goodsId, String specId, String serviceId, int num) {
        HttpHelp.getInstance().create(RemoteApi.class).getIndex(token, rapeseed_Selected, goodsId, specId, serviceId, num)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<IndexBean>>(SettlementActivity.this, null) {
                    @Override
                    public void onNext(BaseBean<IndexBean> baseBean) {
                        super.onNext(baseBean);
                        indexBean = baseBean.data;
                        listBeans = baseBean.data.getOrderList();

                        if (baseBean.code == 0) {
                            addressId = baseBean.data.getReceiving().getAddress_id();
                            differ_money = baseBean.data.getFood().getDiffer_money();

                            if (baseBean.data.getReceiving().getStatus() == 1) {
                                rlXaunzdiz.setVisibility(View.VISIBLE);
                                tvXuanzdiz.setVisibility(View.GONE);
                                tvAddressName.setText("收货人:" + baseBean.data.getReceiving().getConsignee());
                                tvAddressPhone.setText(baseBean.data.getReceiving().getMobile());
                                tvAddress.setText(baseBean.data.getReceiving().getCity()
                                        + baseBean.data.getReceiving().getDistrict() + baseBean.data.getReceiving().getAddress());

                            } else if (baseBean.data.getReceiving().getStatus() == 2) {
                                rlXaunzdiz.setVisibility(View.GONE);
                                tvXuanzdiz.setVisibility(View.VISIBLE);

                            }

                            if (listBeans != null && listBeans.size() > 0) {
                                settlementAdapter.setData(listBeans);
                            }
                            rapeseed = indexBean.getFood().getRapeseed();
                           /* if (rapeseed_Selected ==1){
                                isRapeseed.setChecked(true);
                                rapeseed = indexBean.getFood().getRapeseed();
                            }else {
                                isRapeseed.setChecked(false);
                                rapeseed ="0";
                            }*/
                            if (isRapeseed.isChecked()) {
                                llCaizi.setVisibility(View.VISIBLE);
                                tvMoney.setText("-￥" + ((Double.parseDouble(baseBean.data.getFood().getUser_rapeseed()) - Double.parseDouble(baseBean.data.getFood().getRapeseed())) / 100));
                            } else {
                                llCaizi.setVisibility(View.GONE);
                            }
                            tvCaizi.setText(baseBean.data.getFood().getRapeseed() + "菜籽");
                            if ("0".equals(baseBean.data.getFood().getFee())) {
                                distributionFee.setText("包邮");
                            } else {
                                distributionFee.setText("￥" + baseBean.data.getFood().getFee());
                            }

                            tvOrderMoney.setText("￥" + baseBean.data.getFood().getGoods_money());
                            allMoney = new DecimalFormat("###.00").format(new BigDecimal(baseBean.data.getFood().getOrder_money()));

                            payMoney = allMoney;
                            realPayment.setText(Html.fromHtml("应付款：<font color=\"#dd060e\">￥" + payMoney + "</font>"));

                            // 获取优惠券数量
                            if (isLoad) {
                                getUserCoupon(new PreferencesHelper(SettlementActivity.this).getToken(), payMoney);
                            }

                            //是否已经使用优惠卷
                            showFree(freeId, freeMoney);

                        } else if (baseBean.code == 4) {
                            ToolUtil.goToLogin(SettlementActivity.this);
                        } else {
                            ToastUtil.showToast(SettlementActivity.this, baseBean.message);
                        }
                        hideLoadingDialog();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        hideLoadingDialog();

                    }
                });
    }

    /**
     * 获取配送时间
     */
    private void getTime() {
        HttpHelp.getInstance().create(RemoteApi.class).getDistriTime()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<DistriTimeBean>>(SettlementActivity.this, null) {
                    @Override
                    public void onNext(BaseBean<DistriTimeBean> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            timeBean = baseBean.data;
                        } else {
                            ToastUtil.showToast(SettlementActivity.this, baseBean.message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);

                    }
                });
    }


    @OnClick({R.id.tv_xuanzdiz, R.id.rl_xaunzdiz, R.id.delivery_time, R.id.isRapeseed, R.id.coupon, R.id.tv_determine})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_xuanzdiz:
                //添加地址
                Intent intent2 = new Intent(SettlementActivity.this, AddAddressActivity.class);
                startActivity(intent2);
                break;
            case R.id.rl_xaunzdiz:
                //选择地址
                Intent intent1 = new Intent(SettlementActivity.this, AddressManagementActivity.class);
                intent1.putExtra("type", 1);
                startActivity(intent1);
                break;
            case R.id.delivery_time:
                //选择时间
                showTimeDialog();
                break;
            case R.id.isRapeseed:
                if (isRapeseed.isChecked()) {
                    //使用菜籽
                    /*rapeseed = indexBean.getFood().getRapeseed();
                    payMoney = calculateMoney(allMoney, indexBean.getFood().getRapeseed(), freeMoney);
                    realPayment.setText(Html.fromHtml("应付款：<font color=\"#dd060e\">￥" + payMoney + "</font>"));*/
                    rapeseedSelected = 1;
                    //刷新数据
                    if (TextUtils.isEmpty(goods_id)) {
                        //购物车进入
                        getIndex(new PreferencesHelper(SettlementActivity.this).getToken(), rapeseedSelected, null, null, null, 1);
                    } else {
                        //立即购买进入
                        getIndex(new PreferencesHelper(SettlementActivity.this).getToken(), rapeseedSelected, goods_id, spec_key, service_id, goods_num);
                    }
                } else {
                    //不使用菜籽
                    /*rapeseed = "0";
                    payMoney = calculateMoney(allMoney, "0", freeMoney);
                    realPayment.setText(Html.fromHtml("应付款：<font color=\"#dd060e\">￥" + payMoney + "</font>"));*/
                    rapeseedSelected = 0;
                    //刷新数据
                    if (TextUtils.isEmpty(goods_id)) {
                        //购物车进入
                        getIndex(new PreferencesHelper(SettlementActivity.this).getToken(), rapeseedSelected, null, null, null, 1);
                    } else {
                        //立即购买进入
                        getIndex(new PreferencesHelper(SettlementActivity.this).getToken(), rapeseedSelected, goods_id, spec_key, service_id, goods_num);
                    }
                }
                break;
            case R.id.coupon:
                //跳转到优惠卷列表
                if (indexBean != null) {
                    Intent intent = new Intent(SettlementActivity.this, OrderCouponActivity.class);
                    intent.putExtra("order_price", indexBean.getFood().getOrder_money());
                    startActivityForResult(intent, 100);
                }

                break;
            case R.id.tv_determine:
                if (TextUtils.isEmpty(addressId) || "0".equals(addressId)) {
                    ToastUtil.showToast(SettlementActivity.this, "请添加收货地址");
                    return;
                }
                if (TextUtils.isEmpty(startTime) || TextUtils.isEmpty(endTime)) {
                    //选择时间
                    showTimeDialog();
                    //   ToastUtil.showToast(SettlementActivity.this, "请选择送达时间");
                    return;
                }
                int num = 0;
                String goods_id = "";
                for (IndexBean.OrderListBean b : listBeans) {
                    num += Integer.parseInt(b.getGoods_num());
                    goods_id = goods_id + b.getGoods_id() + ",";
                }
                goods_id = goods_id.substring(0, goods_id.length() - 1);
                EventBusUtil.post(new OrderNumberEvent());
                addOrder(addressId, num, goods_id, rapeseed, freeMoney, freeId, payMoney, startTime, endTime, indexBean.getFood().getFee(), spec_key, service_id, type);
                break;
        }
    }

    /**
     * 支付弹窗
     *
     * @param myMoney
     * @param payMoney
     */
    private void showPayWayDialog(final String myMoney, final String payMoney) {
        if(dialog !=null && dialog.isShowing()){
            dialog.dismiss();
        }
        dialog = new MemberCenterDialog(this, myMoney, payMoney);
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
                        ToastUtil.showToast(SettlementActivity.this, "请选择支付方式");
                        break;
                    case 1://余额支付
                        if (new BigDecimal(myMoney).compareTo(new BigDecimal(payMoney)) < 0) {
                            ToastUtil.showToast(SettlementActivity.this, "余额不足");
                        } else {
                            if (havePassword) {
                                //有支付密码
                                showEditPassword();
                            } else {
                                //没有支付密码
                                final AddressDeleteDialog dialog = new AddressDeleteDialog(SettlementActivity.this);

                                dialog.setGoToUseOnclickListener(new AddressDeleteDialog.onGoToUseOnclickListener() {
                                    @Override
                                    public void onUseClick() {
                                        startActivity(new Intent(SettlementActivity.this, SetPayPasswordActivity.class));
                                    }
                                });
                                dialog.show();
                                dialog.setTitle("尚未设置交易密码,是否前往设置?");
                            }

                        }
                        break;
                    case 2:
//                        ToastUtil.showToast(SettlementActivity.this, "微信支付");
                        weixinPay(new PreferencesHelper(SettlementActivity.this).getToken(), order_sn);
                        break;
                    case 3:
//                        ToastUtil.showToast(SettlementActivity.this, "支付宝支付");
                        aliPay(new PreferencesHelper(SettlementActivity.this).getToken(), order_sn);
                        break;
                }
            }
        });
        dialog.setOnCancelClickListener(new MemberCenterDialog.OnCancelClickListener() {
            @Override
            public void onCancelListener() {
                ToastUtil.showToast(SettlementActivity.this, "请到订单中继续支付");
                dialog.dismiss();
                startActivity(new Intent(SettlementActivity.this,OrderActivity.class));
                finish();
            }
        });
        dialog.show();
    }

    /**
     * 选择时间
     */
    private void showTimeDialog() {
        if (timeBean == null) {
            return;
        }
        if (enDTime != null && !"".equals(enDTime)) {
            SaleDistriTimeDialog distriTimeDialog = new SaleDistriTimeDialog(SettlementActivity.this, timeBean);
            distriTimeDialog.show();
            distriTimeDialog.setConfimClickListener(new DistriTimeDialog.OnConfimClickListener() {
                @Override
                public void onConfimListener(String day, long start, long end) {
                    startTime = String.valueOf((int) start);
                    endTime = String.valueOf((int) end);
                    tvTime.setText(day + " " + ToolUtil.getStrTime(startTime, FORMAT_HM) + "-" + ToolUtil.getStrTime(endTime, FORMAT_HM));
                }
            });
        } else {
            DistriTimeDialog timeDialog = new DistriTimeDialog(SettlementActivity.this, timeBean);
            timeDialog.show();
            timeDialog.setConfimClickListener(new DistriTimeDialog.OnConfimClickListener() {
                @Override
                public void onConfimListener(String day, long start, long end) {
                    startTime = String.valueOf((int) start);
                    endTime = String.valueOf((int) end);
                    tvTime.setText(day + " " + ToolUtil.getStrTime(startTime, FORMAT_HM) + "-" + ToolUtil.getStrTime(endTime, FORMAT_HM));
                }
            });
        }
    }


    /**
     * 计算实际应付金额
     */
    private String calculateMoney(String orderMoney, String free) {

        BigDecimal bigDecimalOrder = new BigDecimal(orderMoney);
        BigDecimal bigDecimalFree = new BigDecimal(free);

        if (!"0".equals(free)) {
            //用优惠卷
            bigDecimalOrder = bigDecimalOrder.subtract(bigDecimalFree);
        }

        if (bigDecimalOrder.doubleValue() < 0) {
            return "0.0";
        }

        return new DecimalFormat("##.00").format(bigDecimalOrder.doubleValue());

    }


    /**
     * @param addressId    //地址id
     * @param number       //数量
     * @param goods_id     //商品id  多个用逗号隔开
     * @param rapeseed     //使用菜籽数量  没有0
     * @param coupon_price //优惠卷价格
     * @param counpon_id   //优惠卷id
     * @param end_price    //订单总价
     * @param begin_time   //配送开始时间
     * @param end_time     //配送结束时间
     */
    private void addOrder(String addressId, int number, String goods_id, String rapeseed, String coupon_price, String counpon_id, String end_price,
                          String begin_time, String end_time, String fee, String spec_key, String service_id, int type) {

        if (TextUtils.isEmpty(spec_key)) {
            spec_key = "0";
        }
        if (TextUtils.isEmpty(service_id)) {
            service_id = "0";
        }
        tvDetermine.setEnabled(false);
        SettlementActivity.this.showLoadingDialog("提交订单中...");
        HttpHelp.getInstance().create(RemoteApi.class).addOrder(new PreferencesHelper(SettlementActivity.this).getToken(), addressId, number, goods_id, rapeseed, coupon_price,
                counpon_id, end_price, begin_time, end_time, fee, spec_key, service_id, type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<SubmitOrderBean>>(SettlementActivity.this, null) {
                    @Override
                    public void onNext(BaseBean<SubmitOrderBean> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            //订单提交成功
                            EventBusUtil.post(new ShoppingCartEvent());
                            order_sn = baseBean.data.getOrder_sn();
                            showPayWayDialog(indexBean.getFood().getUser_money(), payMoney);
                        } else if (baseBean.code == 4) {
                            ToolUtil.goToLogin(SettlementActivity.this);
                        } else {
                            ToastUtil.showToast(SettlementActivity.this, baseBean.message);
                        }
                        tvDetermine.setEnabled(true);
                        SettlementActivity.this.hideLoadingDialog();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        ToastUtil.showToast(SettlementActivity.this, "请求失败");
                        tvDetermine.setEnabled(true);
                        SettlementActivity.this.hideLoadingDialog();
                    }
                });
    }


    //弹出密码/输入密码/完毕后的回调
    private void showEditPassword() {
        dialog.cancel();
        if(passwordDialog!=null&&passwordDialog.isVisible()){
            passwordDialog.dismiss();
        }
        passwordDialog = PayPasswordDialog.newInstace(this);
        passwordDialog.setOnFinishClickListener(new PayPasswordDialog.OnFinishClickListener() {
            @Override
            public void onFinish(String psw) {
                pay(new PreferencesHelper(SettlementActivity.this).getToken(), order_sn, psw);
            }
        });
        passwordDialog.setOnCancelClickListener(new PayPasswordDialog.OnCancelClickListener() {
            @Override
            public void onCancel() {
                ToastUtil.showToast(SettlementActivity.this, "请到订单中继续支付");
                passwordDialog.dismiss();
                startActivity(new Intent(SettlementActivity.this,OrderActivity.class));
                finish();
            }
        });
        passwordDialog.setOnForgetClickListener(new PayPasswordDialog.OnForgetClickListener() {
            @Override
            public void onForget() {
                startActivity(new Intent(SettlementActivity.this,OrderActivity.class));
                finish();
            }
        });
        passwordDialog.show(getSupportFragmentManager(), "PassWordFragment");

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
                .subscribe(new BaseSubscriber<BaseBean<IsPasswordExitsBean>>(SettlementActivity.this, null) {
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
                            ToolUtil.goToLogin(SettlementActivity.this);
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
     * 余额支付
     *
     * @param token
     * @param order_sn
     * @param pay_password
     */
    private void pay(String token, String order_sn, String pay_password) {
        showLoadingDialog("支付中...");
        HttpHelp.getInstance().create(RemoteApi.class).pay(token, order_sn, pay_password, "1")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<Object>>(SettlementActivity.this, null) {
                    @Override
                    public void onNext(BaseBean<Object> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            passwordDialog.dismiss();
                            EventBusUtil.post(new RefreshNumEvent());
                            ToastUtil.showToast(SettlementActivity.this, baseBean.message);
                            finish();
                            startActivity(new Intent(SettlementActivity.this,OrderActivity.class));
                        } else if (baseBean.code == 4) {
                            passwordDialog.dismiss();
                            ToolUtil.goToLogin(SettlementActivity.this);
                        } else {
                            passwordDialog.clear();
                            ToastUtil.showToast(SettlementActivity.this, baseBean.message);
                        }
                        hideLoadingDialog();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        hideLoadingDialog();
                        ToastUtil.showToast(SettlementActivity.this, "网络连接失败");
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
        HttpHelp.getInstance().create(RemoteApi.class).aliPay(token, order_sn, "3")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<String>>(SettlementActivity.this, null) {
                    @Override
                    public void onNext(BaseBean<String> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            LogUtil.i("支付宝：" + baseBean.data);
                            startAliPay(baseBean.data);
                        } else {
                            ToastUtil.showToast(SettlementActivity.this, baseBean.message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);

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
        WxPayUtil.wxPay(token, order_sn, SettlementActivity.this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBusUtil.post(new OrderNumberEvent());
        EventBusUtil.post(new ChangeHeaderEvent());
        EventBusUtil.unregister(this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == 101) {
            //选择了优惠卷
            freeId = data.getStringExtra("id");
            freeMoney = data.getStringExtra("price");
            showFree(freeId, freeMoney);
        }
    }

    /**
     * 显示使用优惠卷
     *
     * @param free_id
     * @param free_money
     */
    private void showFree(String free_id, String free_money) {
        if (TextUtils.isEmpty(free_id)) {
            tvFreeMoney.setVisibility(View.GONE);
            ivFreeArrow.setVisibility(View.VISIBLE);
        } else {
            tvFreeMoney.setVisibility(View.VISIBLE);
            ivFreeArrow.setVisibility(View.GONE);
            tvFreeMoney.setText("-￥" + free_money);
            //payMoney = calculateMoney(allMoney, free_money);
            realPayment.setText(Html.fromHtml("应付款：<font color=\"#dd060e\">￥" + payMoney + "</font>"));
        }

    }

    /**
     * 设置密码返回事件
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void upPassWord(SetPasswordEvent event) {
        checkPassWord(new PreferencesHelper(SettlementActivity.this).getToken());
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        isLoad = false;
        getData();
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
                PayTask alipay = new PayTask(SettlementActivity.this);
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
                        Toast.makeText(SettlementActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(SettlementActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    startActivity(new Intent(SettlementActivity.this,OrderActivity.class));
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
                        Toast.makeText(SettlementActivity.this,
                                "授权成功\n" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        // 其他状态值则为授权失败
                        Toast.makeText(SettlementActivity.this,
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


}
