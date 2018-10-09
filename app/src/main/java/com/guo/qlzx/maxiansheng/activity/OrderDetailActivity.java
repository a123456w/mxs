package com.guo.qlzx.maxiansheng.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.canyinghao.canrefresh.CanRefreshLayout;
import com.canyinghao.canrefresh.classic.ClassicRefreshView;
import com.guo.qlzx.maxiansheng.R;
import com.guo.qlzx.maxiansheng.adapter.LikeAdapter;
import com.guo.qlzx.maxiansheng.adapter.OrderGoodsAdapter;
import com.guo.qlzx.maxiansheng.bean.GuessLikeBean;
import com.guo.qlzx.maxiansheng.bean.OrderDetailBean;
import com.guo.qlzx.maxiansheng.bean.SpecDialogBean;
import com.guo.qlzx.maxiansheng.event.ShoppingCartEvent;
import com.guo.qlzx.maxiansheng.http.RemoteApi;
import com.guo.qlzx.maxiansheng.util.ToolUtil;
import com.guo.qlzx.maxiansheng.util.dialog.SelectDialog;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.DensityUtil;
import com.qlzx.mylibrary.util.EventBusUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.qlzx.mylibrary.widget.NoScrollGridView;
import com.qlzx.mylibrary.widget.NoScrollListView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/4/17 0017.
 * describe  订单详情
 */

public class OrderDetailActivity extends BaseActivity implements CanRefreshLayout.OnLoadMoreListener, AdapterView.OnItemClickListener {

    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.goods_list)
    NoScrollListView goodsList;
    @BindView(R.id.tv_all_money)
    TextView tvAllMoney;
    @BindView(R.id.tv_dispatch)
    TextView tvDispatch;
    @BindView(R.id.tv_real_pay)
    TextView tvRealPay;
    @BindView(R.id.tv_back_price)
    TextView tvBackPrice;
    @BindView(R.id.tv_get_caizi)
    TextView tvGetCaizi;
    @BindView(R.id.tv_back_money)
    TextView tvBackMoney;
    @BindView(R.id.tv_allpay_price)
    TextView tvAllPayPrice;
    @BindView(R.id.tv_order_id)
    TextView tvOrderId;
    @BindView(R.id.tv_order_pay_id)
    TextView tvOrderPayId;
    @BindView(R.id.tv_creat_time)
    TextView tvCreatTime;
    @BindView(R.id.tv_pay_time)
    TextView tvPayTime;
    @BindView(R.id.tv_divider_time)
    TextView tvDividerTime;
    @BindView(R.id.tv_state)
    TextView tvState;
    @BindView(R.id.tv_state_hint)
    TextView tvStateHint;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_addraduce_price)
    TextView tvAddRaducePrice;
    @BindView(R.id.grid_like)
    NoScrollGridView grideLike;
    @BindView(R.id.can_refresh_footer)
    ClassicRefreshView canRefreshFooter;
    @BindView(R.id.refresh)
    CanRefreshLayout refresh;
    @BindView(R.id.tv_name_hint)
    TextView tvNameHint;
    @BindView(R.id.tv_dian)
    TextView tvDian;
    @BindView(R.id.can_content_view)
    NestedScrollView canContentView;
    @BindView(R.id.can_refresh_header)
    ClassicRefreshView canRefreshHeader;
    @BindView(R.id.tv_hint)
    TextView tvHint;
    @BindView(R.id.ll_pay_time)
    LinearLayout llPayTime;
    @BindView(R.id.ll_divider_time)
    LinearLayout llDividerTime;

    @BindView(R.id.ll_real_pay)
    RelativeLayout llRealPay;
    @BindView(R.id.ll_back_price)
    RelativeLayout llBackPrice;
    @BindView(R.id.rl_get_caizi)
    RelativeLayout rlGetCaizi;
    @BindView(R.id.tv_coupon)
    TextView tvCoupon;
    @BindView(R.id.ll_coupon)
    RelativeLayout llCoupon;
    @BindView(R.id.tv_use_caizi)
    TextView tvUseCaizi;
    @BindView(R.id.tv_divider_goods_time)
    TextView tvDividerGoodsTime;
    @BindView(R.id.rl_use_caizi)
    RelativeLayout rlUseCaizi;
    @BindView(R.id.tv_pay_code)
    LinearLayout tvPayCode;
    @BindView(R.id.tv_goods_id)
    TextView tvGoodsId;
    @BindView(R.id.ll_goods_id)
    LinearLayout llGoodsId;
    @BindView(R.id.rn_chajia)
    RelativeLayout rnChajia;
    @BindView(R.id.ll_divider_goods_time)
    LinearLayout llDividerGoodsTime;
    @BindView(R.id.tv_yifu)
    TextView tvYifu;
    @BindView(R.id.tv_dian1)
    TextView tvDian1;
    @BindView(R.id.tv_kehu)
    TextView tvKehu;
    @BindView(R.id.tv_chajia)
    TextView tvChajia;
    @BindView(R.id.tv_chajias)
    TextView tvChajias;


    private PreferencesHelper helper;
    private OrderDetailBean detail;
    private String order_id;

    private OrderGoodsAdapter adapter;
    private LikeAdapter likeAdapter;
    private List<GuessLikeBean> liksBean;

    private int page = 1;
    private SelectDialog cartDialog;

    @Override
    public int getContentView() {
        return R.layout.activity_order_detail;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("订单详情");
        titleBar.setLeftImageRes(R.drawable.ic_back_gray);
        helper = new PreferencesHelper(this);

        order_id = getIntent().getStringExtra("order_id");

        adapter = new OrderGoodsAdapter(goodsList);
        goodsList.setAdapter(adapter);

        likeAdapter = new LikeAdapter(grideLike);
        grideLike.setAdapter(likeAdapter);
        grideLike.setOnItemClickListener(this);
        likeAdapter.setOnclickListener(new LikeAdapter.OnAddCartOnclickListener() {
            @Override
            public void onClick(int position, float x, float y) {
                //加购物车
                GuessLikeBean guessLikeBean = likeAdapter.getData().get(position);

                if (likeAdapter.getData().get(position).getSpec_type() == 0) {
                    //不存在规格
                    addShoppingCart(helper.getToken(), String.valueOf(guessLikeBean.getGoods_id()), "", "", 1);
                } else {
                    //存在规格 选择规格
                    cartDialog = new SelectDialog(OrderDetailActivity.this, guessLikeBean.getType());
                    SpecDialogBean specDialogBean = new SpecDialogBean();
                    specDialogBean.setGoods_id(guessLikeBean.getGoods_id());
                    specDialogBean.setImg(guessLikeBean.getImg());
                    specDialogBean.setSpec_name(guessLikeBean.getSpec_key_name());
                    specDialogBean.setPlus_price(guessLikeBean.getPlus_price());
                    specDialogBean.setShop_price(guessLikeBean.getShop_price());
                    cartDialog.setGoToUseOnclickListener(new SelectDialog.onGoToUseOnclickListener() {
                        @Override
                        public void onUseClick(String goods_id, String spec_id, String service_id, int num, int s
                                , double price, double vipPrice) {
                            if (s == 0) {
                                ToastUtil.showToast(OrderDetailActivity.this, "当前商品库存为0份");
                                return;
                            }
                            addShoppingCart(helper.getToken(), goods_id, service_id, spec_id, num);
                        }
                    });
                    cartDialog.show();
                    cartDialog.setData(specDialogBean);
                }
            }

        });


        refresh.setRefreshEnabled(false);
        refresh.setOnLoadMoreListener(this);
        refresh.setMaxFooterHeight(DensityUtil.dp2px(this, 150));
        refresh.setStyle(0, 0);
        canRefreshFooter.setPullStr("上拉加载更多");
        canRefreshFooter.setReleaseStr("松开刷新");
        canRefreshFooter.setRefreshingStr("加载中");
        canRefreshFooter.setCompleteStr("");
    }

    @Override
    public void getData() {
        getDetails(helper.getToken(), order_id);

        getGuessLikeData(page);
    }

    /**
     * 获取订单信息
     *
     * @param token
     * @param id
     */
    private void getDetails(String token, String id) {
        HttpHelp.getInstance().create(RemoteApi.class).getOrderDetail(token, id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<OrderDetailBean>>(OrderDetailActivity.this, null) {
                    @Override
                    public void onNext(BaseBean<OrderDetailBean> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            detail = baseBean.data;
                            if (detail != null) {
                                adapter.setData(detail.getGoods());
                                updateUI(detail.getAddress(), detail.getOrder());
                            }
                        } else if (baseBean.code == 4) {
                            ToolUtil.goToLogin(OrderDetailActivity.this);
                        } else {
                            ToastUtil.showToast(OrderDetailActivity.this, baseBean.message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
    }


    /**
     * 猜你喜欢
     *
     * @param page
     */
    private void getGuessLikeData(final int page) {
        HttpHelp.getInstance().create(RemoteApi.class).getGuessLikeData(page, "1")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<List<GuessLikeBean>>>(OrderDetailActivity.this, null) {
                    @Override
                    public void onNext(BaseBean<List<GuessLikeBean>> baseBean) {
                        super.onNext(baseBean);
                        liksBean = baseBean.data;
                        refresh.loadMoreComplete();
                        if (baseBean.code == 0) {
                            if (page == 1) {
                                if (liksBean != null && liksBean.size() > 0) {
                                    likeAdapter.setData(liksBean);
                                } else {
                                    likeAdapter.clear();
                                }
                            } else {
                                if (liksBean != null && liksBean.size() > 0) {
                                    likeAdapter.addMoreData(liksBean);
                                } else {
                                    ToastUtil.showToast(OrderDetailActivity.this, "暂无更多");
                                }
                            }
                        } else {
                            ToastUtil.showToast(OrderDetailActivity.this, baseBean.message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        refresh.loadMoreComplete();

                    }
                });
    }

    /**
     * 设置数据
     *
     * @param address
     * @param order
     */
    private void updateUI(OrderDetailBean.AddressBean address, OrderDetailBean.OrderBean order) {

        tvName.setText(address.getName());
        tvPhone.setText(address.getMobile());
        tvAddress.setText(address.getProvince() + address.getCity() + address.getAddress());

        tvAllPayPrice.setText("¥" + order.getEnd_amount());   //已付金额
        tvAddRaducePrice.setText("+¥" + order.getCompen_money());  //补差价
        tvHint.setText("实付款：");
        tvBackMoney.setText(order.getOrder_amount());
        tvAllMoney.setText("¥" + order.getEnd_amount());
        tvDispatch.setText("+¥" + order.getShipping_price());
        tvGetCaizi.setText(order.getRapeseed());

        tvDividerGoodsTime.setVisibility(View.VISIBLE);
        tvDividerGoodsTime.setText(ToolUtil.getStrTime(order.getBegin_time(), "yyyy-MM-dd HH:mm:ss") + "-" +
                ToolUtil.getStrTime(order.getEnd_time(), "yyyy-MM-dd HH:mm:ss"));
        //货物编号
        if (!"0".equals(order.getCargonumber())) {
            tvGoodsId.setText(order.getCargonumber());
        } else {
            llGoodsId.setVisibility(View.GONE);
        }

        if ("yue_pay".equals(order.getPay_code())) {
            tvPayCode.setVisibility(View.GONE);
        }
        switch (order.getOrder_type()) {
            case 3:
                tvState.setText("已取消");
                tvStateHint.setText("已取消，感谢您使用马鲜生，期待再次为您服务！");

                tvHint.setText("需付款：");
                tvBackMoney.setText("¥" + order.getOrder_amount());

                llRealPay.setVisibility(View.GONE);
                llBackPrice.setVisibility(View.GONE);
                llCoupon.setVisibility(View.GONE);
                rlUseCaizi.setVisibility(View.GONE);
                break;
            case 0:
                tvState.setText("待付款");
                tvStateHint.setText("该笔订单未付款，请尽快处理!");
                tvHint.setText("需付款：");
                tvBackMoney.setText(order.getOrder_amount());
                tvYifu.setText("待付金额");
                tvAllPayPrice.setText("¥" + order.getOrder_amount());
                tvCoupon.setText("-¥" + order.getCoupon_price());
                tvUseCaizi.setText("-¥" + order.getIntegral());
                llRealPay.setVisibility(View.GONE);
                llBackPrice.setVisibility(View.GONE);
                rnChajia.setVisibility(View.GONE);
                tvPayCode.setVisibility(View.GONE);
                break;
            case 1:
                tvState.setText("配送中");
                tvStateHint.setText("正在配送中，请耐心等待！");

                tvRealPay.setText("¥" + order.getOrder_amount());
                tvBackPrice.setText("-¥" + order.getChange_money());
                tvCoupon.setText("-¥" + order.getCoupon_price());
                tvUseCaizi.setText("-¥" + order.getIntegral());
                if (!TextUtils.isEmpty(order.getChange_money()) && !order.getChange_money().equals("0.00")) {
                    tvHint.setText("实付款：");
                    tvBackMoney.setText("¥" + order.getOrder_amount());
                } /*else {
                 *//*                    tvHint.setText("实付款：");
                    tvBackMoney.setText("¥" + order.getEnd_amount());*//*
                }*/
                break;

            case 2:
                tvState.setText("待评价");
                tvStateHint.setText("订单已签收，请对我们的服务做出评价！");

                tvRealPay.setText("¥" + order.getOrder_amount());
                tvBackPrice.setText("-¥" + order.getChange_money());
/*
                tvBackMoney.setText("¥" + order.getEnd_amount());
*/
                tvCoupon.setText("-¥" + order.getCoupon_price());
                tvUseCaizi.setText("-¥" + order.getIntegral());

                if (!TextUtils.isEmpty(order.getChange_money()) && !order.getChange_money().equals("0.00")) {
                    tvHint.setText("退还款：");
                    tvBackMoney.setText("¥" + order.getChange_money());
                }/* else {
                    tvHint.setText("实付款：");
                    tvBackMoney.setText("¥" + order.getEnd_amount());
                }*/
                break;
            case 4:
                tvState.setText("待发货");
                tvStateHint.setText("待发货，请耐心等待");
                tvRealPay.setText("¥" + order.getOrder_amount());
                tvBackPrice.setText("¥" + order.getOrder_amount());

                tvKehu.setText("客户付款");
                tvChajia.setText("实际应付");
                tvAddRaducePrice.setText("¥" + order.getOrder_amount());
                tvChajias.setText("补差价");
                tvRealPay.setText("+¥"+order.getCompen_money());
                tvYifu.setText("退还差价");
                tvAllPayPrice.setText("-¥" + order.getChange_money());
                tvHint.setText("实付款：");
                tvBackMoney.setText(order.getOrder_amount());
/*
                tvBackMoney.setText("¥" + order.getEnd_amount());
*/
                tvCoupon.setText("-¥" + order.getCoupon_price());
                tvUseCaizi.setText("-¥" + order.getIntegral());
                if (!TextUtils.isEmpty(order.getChange_money()) && !order.getChange_money().equals("0.00")) {
                    tvHint.setText("实付款：");
                    tvBackMoney.setText("¥" + order.getOrder_amount());
                }/* else {
                    tvHint.setText("实付款：");
                    tvBackMoney.setText("¥" + order.getEnd_amount());
                }*/
                break;
            case 5:
                tvState.setText("完成");
                tvStateHint.setText("已签收，感谢您使用马鲜生，期待再次为您服务！");

                tvRealPay.setText("¥" + order.getOrder_amount());
                tvBackPrice.setText("-¥" + order.getChange_money());
/*
                tvBackMoney.setText("¥" + order.getEnd_amount());
*/
                tvCoupon.setText("-¥" + order.getCoupon_price());
                tvUseCaizi.setText("-¥" + order.getIntegral());
                if (!TextUtils.isEmpty(order.getChange_money()) && !order.getChange_money().equals("0.00")) {
                    tvHint.setText("退还款：");
                    tvBackMoney.setText("¥" + order.getChange_money());
                }/* else {
                    tvHint.setText("实付款：");
                    tvBackMoney.setText("¥" + order.getEnd_amount());
                }*/
                break;
            case 6:
                tvState.setText("已退货");
                tvStateHint.setText("已退货，感谢您使用马鲜生，期待再次为您服务！");

                tvRealPay.setText("¥" + order.getOrder_amount());
                tvBackPrice.setText("-¥" + order.getChange_money());
                tvCoupon.setText("-¥" + order.getCoupon_price());
                tvUseCaizi.setText("-¥" + order.getIntegral());
                if (!TextUtils.isEmpty(order.getChange_money()) && !order.getChange_money().equals("0.00")) {
                    tvHint.setText("退还款：");
                    tvBackMoney.setText("¥" + order.getChange_money());
                } /*else {
                    tvHint.setText("实付款：");
                    tvBackMoney.setText("¥" + order.getEnd_amount());
                }*/
                llDividerTime.setVisibility(View.GONE);
                break;
        }


        tvOrderId.setText(order.getOrder_sn());
        tvOrderPayId.setText(order.getOrder_sn());
        tvCreatTime.setText(ToolUtil.getStrTime(order.getAdd_time(), "yyyy-MM-dd HH:mm:ss"));
        if (order.getPay_time().length() > 1) {
            tvPayTime.setText(ToolUtil.getStrTime(order.getPay_time(), "yyyy-MM-dd HH:mm:ss"));
        } else {
            llPayTime.setVisibility(View.GONE);
        }
        if (order.getShipping_time().length() > 0 && !"0".equals(order.getShipping_time())) {
            tvDividerTime.setText(ToolUtil.getStrTime(order.getShipping_time(), "yyyy-MM-dd HH:mm:ss"));
            llDividerTime.setVisibility(View.VISIBLE);
            tvDividerTime.setText(View.VISIBLE);
        } else {
            llDividerTime.setVisibility(View.GONE);
            tvDividerTime.setText(View.GONE);
        }


    }


    @Override
    public void onLoadMore() {
        page++;
        getGuessLikeData(page);
    }


    /**
     * 猜你喜欢  点击事件
     *
     * @param view
     */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        //猜你喜欢 跳详情
        String goodsId = String.valueOf(likeAdapter.getData().get(i).getGoods_id());
        Intent intent = new Intent(OrderDetailActivity.this, ProductDetailsActivity.class);
        intent.putExtra("goods_id", goodsId);
        startActivity(intent);
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
                .subscribe(new BaseSubscriber<BaseBean<Object>>(OrderDetailActivity.this, null) {
                    @Override
                    public void onNext(BaseBean<Object> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            EventBusUtil.post(new ShoppingCartEvent());
                            ToastUtil.showToast(OrderDetailActivity.this, baseBean.message);
                        } else if (baseBean.code == 4) {
                            ToolUtil.goToLogin(OrderDetailActivity.this);
                        } else {
                            ToastUtil.showToast(OrderDetailActivity.this, baseBean.message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);

                    }
                });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
