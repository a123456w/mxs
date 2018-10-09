package com.guo.qlzx.maxiansheng.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.canyinghao.canrefresh.CanRefreshLayout;
import com.canyinghao.canrefresh.classic.ClassicRefreshView;
import com.guo.qlzx.maxiansheng.R;
import com.guo.qlzx.maxiansheng.activity.EvaluateActivity;
import com.guo.qlzx.maxiansheng.activity.FaqActivity;
import com.guo.qlzx.maxiansheng.activity.MainActivity;
import com.guo.qlzx.maxiansheng.activity.OrderActivity;
import com.guo.qlzx.maxiansheng.activity.OrderDetailActivity;
import com.guo.qlzx.maxiansheng.activity.SetPayPasswordActivity;
import com.guo.qlzx.maxiansheng.activity.SetPersonalDetailsActivity;
import com.guo.qlzx.maxiansheng.adapter.OrderAllAdapter;
import com.guo.qlzx.maxiansheng.bean.AuthResult;
import com.guo.qlzx.maxiansheng.bean.OrdersBean;
import com.guo.qlzx.maxiansheng.bean.PayResult;
import com.guo.qlzx.maxiansheng.common.Constants;
import com.guo.qlzx.maxiansheng.http.RemoteApi;
import com.guo.qlzx.maxiansheng.util.ToolUtil;
import com.guo.qlzx.maxiansheng.util.WxPayUtil;
import com.guo.qlzx.maxiansheng.util.dialog.AddressDeleteDialog;
import com.guo.qlzx.maxiansheng.util.dialog.MemberCenterDialog;
import com.guo.qlzx.maxiansheng.util.dialog.PayPasswordDialog;
import com.qlzx.mylibrary.base.BaseFragment;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.base.OnItemChildClickListener;
import com.qlzx.mylibrary.base.OnRVItemClickListener;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.DensityUtil;
import com.qlzx.mylibrary.util.LogUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.qlzx.mylibrary.widget.RecycleViewDivider;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/4/16 0016.
 * describe 代付款订单
 */

public class OrderWaitPayFragment extends BaseFragment implements OnItemChildClickListener, OnRVItemClickListener,
        CanRefreshLayout.OnRefreshListener, CanRefreshLayout.OnLoadMoreListener {

    Unbinder unbinder;
    @BindView(R.id.can_content_view)
    RecyclerView canContentView;
    @BindView(R.id.can_refresh_header)
    ClassicRefreshView canRefreshHeader;
    @BindView(R.id.can_refresh_footer)
    ClassicRefreshView canRefreshFooter;
    @BindView(R.id.refresh)
    CanRefreshLayout refresh;
    @BindView(R.id.rl_empty)
    RelativeLayout rlEmpty;
    @BindView(R.id.tv_skip)
    TextView tvSkip;
    private PreferencesHelper helper;
    private OrderAllAdapter adapter;
    private List<OrdersBean> list;
    private int page = 1;
    private boolean isLazy = false;
    private MemberCenterDialog dialog;//支付方式弹窗
    private AddressDeleteDialog deleteDialog;
    PayPasswordDialog passwordDialog ;
    @Override
    public View getContentView(FrameLayout frameLayout) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_order, frameLayout, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void initView() {
        loadingLayout.setStatus(LoadingLayout.Success);
        helper = new PreferencesHelper(mContext);
        adapter = new OrderAllAdapter(canContentView);
        canContentView.setAdapter(adapter);
        canContentView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        canContentView.addItemDecoration(new RecycleViewDivider(mContext, R.drawable.bg_recycler_divider));
        adapter.setOnItemChildClickListener(this);
        adapter.setOnRVItemClickListener(this);

        refresh.setOnLoadMoreListener(this);
        refresh.setOnRefreshListener(this);
        refresh.setMaxFooterHeight(DensityUtil.dp2px(getActivity(), 150));
        refresh.setStyle(0, 0);
        canRefreshHeader.setPullStr("下拉刷新");
        canRefreshHeader.setReleaseStr("松开刷新");
        canRefreshHeader.setRefreshingStr("加载中");
        canRefreshHeader.setCompleteStr("");
        canRefreshFooter.setPullStr("上拉加载更多");
        canRefreshFooter.setReleaseStr("松开刷新");
        canRefreshFooter.setRefreshingStr("加载中");
        canRefreshFooter.setCompleteStr("");
        tvSkip.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG );
        tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(mContext, MainActivity.class);
                intent1.putExtra("go_home", 2);
                startActivity(intent1);
            }
        });
    }

    @Override
    public void getData() {
        isLazy = true;
        getOrderList(helper.getToken(), "1", page);
    }

    private void getOrderList(String token, String type, final int page) {
        HttpHelp.getInstance().create(RemoteApi.class).getMyOrder(token, type, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<List<OrdersBean>>>(mContext, null) {
                    @Override
                    public void onNext(BaseBean<List<OrdersBean>> baseBean) {
                        super.onNext(baseBean);
                        if(getActivity()==null||getActivity().isFinishing()){
                            return;
                        }
                        refresh.refreshComplete();
                        refresh.loadMoreComplete();
                        if (baseBean.code == 0) {
                            list = baseBean.data;
                            if (page == 1) {
                                if (list != null && list.size() > 0) {
                                    rlEmpty.setVisibility(View.GONE);
                                    adapter.setData(baseBean.data);
                                } else {
                                    adapter.clear();
                                    rlEmpty.setVisibility(View.VISIBLE);
                                }

                            } else {
                                if (list != null && list.size() > 0) {
                                    adapter.addMoreData(list);
                                } else {
                                    ToastUtil.showToast(getActivity(), "暂无更多");
                                }
                            }

                        } else if (baseBean.code == 4) {
                            ToolUtil.goToLogin(mContext);
                        } else {
                            ToastUtil.showToast(mContext, baseBean.message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        refresh.refreshComplete();
                        refresh.loadMoreComplete();

                    }
                });
    }
    @Override
    public void onLoadMore() {
        page++;
        getData();
    }

    @Override
    public void onRefresh() {
        page = 1;
        getData();
    }


    @Override
    public void onRVItemClick(ViewGroup parent, View itemView, int position) {
        Intent intent = new Intent(mContext, OrderDetailActivity.class);
        intent.putExtra("order_id", adapter.getItem(position).getOrder_id());
        startActivity(intent);
    }

    @Override
    public void onItemChildClick(ViewGroup parent, View childView, final int position) {
        TextView btn = (TextView) childView;
        switch (btn.getText().toString()) {
            case "查看物流":
                ToastUtil.showToast(mContext, "查看物流功能待完成");
                break;
            case "确认收货":
                deleteDialog=new AddressDeleteDialog(mContext);
                deleteDialog.setGoToUseOnclickListener(new AddressDeleteDialog.onGoToUseOnclickListener() {
                    @Override
                    public void onUseClick() {
                        confirm(helper.getToken(), adapter.getItem(position).getOrder_id());
                    }
                });
                deleteDialog.show();
                deleteDialog.setTitle("确认收货？");
                break;
            case "删除订单":
                deleteDialog=new AddressDeleteDialog(mContext);
                deleteDialog.setGoToUseOnclickListener(new AddressDeleteDialog.onGoToUseOnclickListener() {
                    @Override
                    public void onUseClick() {
                        delOrder(adapter.getItem(position).getOrder_id());
                    }
                });
                deleteDialog.show();
                deleteDialog.setTitle("确认删除订单？");
                break;
            case "立即评价":
                goEvaluate(adapter.getItem(position));
                break;
            case "联系卖家":
                if ("".equals(adapter.getItem(position).getStaff_iphone())){
                    ToastUtil.showToast(mContext,"暂无联系方式");
                    return;
                }
                call(adapter.getItem(position).getStaff_iphone());
                break;
            case "立即付款":
                showPayWayDialog(((OrderActivity) getActivity()).mBalance, adapter.getItem(position).getOrder_amount(), adapter.getItem(position).getOrder_sn());
                break;
            case "取消订单":
                deleteDialog=new AddressDeleteDialog(mContext);
                deleteDialog.setGoToUseOnclickListener(new AddressDeleteDialog.onGoToUseOnclickListener() {
                    @Override
                    public void onUseClick() {
                        cancelOrder(adapter.getItem(position).getOrder_id());
                    }
                });
                deleteDialog.show();
                deleteDialog.setTitle("确认取消订单？");
                break;
            case "提醒发货"://这个页面不会触发这个
                break;
            case "退货"://这个页面不会触发这个
                break;
        }
    }


    /**
     * 取消订单
     *
     * @param order_id
     */
    private void cancelOrder(String order_id) {
        HttpHelp.getInstance().create(RemoteApi.class).cancelOrder(helper.getToken(), order_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<Object>>(mContext, null) {
                    @Override
                    public void onNext(BaseBean<Object> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            ToastUtil.showToast(mContext, baseBean.message);
                            refresh.autoRefresh();
                        } else if (baseBean.code == 4) {
                            ToolUtil.goToLogin(mContext);
                        } else {
                            ToastUtil.showToast(mContext, baseBean.message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);

                    }
                });
    }

    /**
     * 删除订单
     *
     * @param order_id
     */
    private void delOrder(String order_id) {
        HttpHelp.getInstance().create(RemoteApi.class).delOrder(helper.getToken(), order_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<Object>>(mContext, null) {
                    @Override
                    public void onNext(BaseBean<Object> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            ToastUtil.showToast(mContext, baseBean.message);
                            refresh.autoRefresh();
                        } else if (baseBean.code == 4) {
                            ToolUtil.goToLogin(mContext);
                        } else {
                            ToastUtil.showToast(mContext, baseBean.message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);

                    }
                });
    }

    /**
     * 联系卖家
     *
     * @param phoneNum
     */
    private void call(String phoneNum) {
        ToolUtil.goToCall(mContext, phoneNum);
    }

    /**
     * 去评价
     */
    private void goEvaluate(OrdersBean bean) {
        Intent intent = new Intent(mContext, EvaluateActivity.class);
        intent.putExtra("info", bean);
        startActivity(intent);
    }

    /**
     * 确认订单
     *
     * @param token
     * @param order_id
     */
    private void confirm(String token, String order_id) {
        HttpHelp.getInstance().create(RemoteApi.class).confirmOrder(token, order_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<Object>>(mContext, null) {
                    @Override
                    public void onNext(BaseBean<Object> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            ToastUtil.showToast(mContext, baseBean.message);
                            refresh.autoRefresh();
                        } else if (baseBean.code == 4) {
                            ToolUtil.loseToLogin(mContext);
                        } else {
                            ToastUtil.showToast(mContext, baseBean.message);
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
        dialog = new MemberCenterDialog(mContext, myMoney, payMoney);
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
                        ToastUtil.showToast(mContext, "请选择支付方式");
                        break;
                    case 1:
                        if (new BigDecimal(myMoney).compareTo(new BigDecimal(payMoney)) < 0) {
                            ToastUtil.showToast(mContext, "余额不足");
                        } else {
                            if (((OrderActivity) getActivity()).havePassword) {
                                //有支付密码
                                showEditPassword(order_sn);
                            } else {
                                //没有支付密码
                                final AddressDeleteDialog dialog = new AddressDeleteDialog(mContext);

                                dialog.setGoToUseOnclickListener(new AddressDeleteDialog.onGoToUseOnclickListener() {
                                    @Override
                                    public void onUseClick() {
                                        startActivity(new Intent(mContext, SetPayPasswordActivity.class));
                                    }
                                });
                                dialog.show();
                                dialog.setTitle("尚未设置交易密码,是否前往设置?");
                            }

                        }
                        break;
                    case 2:
                        weixinPay(helper.getToken(), order_sn);
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
        passwordDialog = PayPasswordDialog.newInstace(mContext);
        passwordDialog.setOnFinishClickListener(new PayPasswordDialog.OnFinishClickListener() {
            @Override
            public void onFinish(String psw) {
                pay(helper.getToken(), order_sn, psw);
            }
        });
        passwordDialog.show(getActivity().getSupportFragmentManager(), "PassWordFragment");

    }


    /**
     * 支付
     *
     * @param token
     * @param order_sn
     * @param pay_password
     */
    private void pay(String token, String order_sn, final String pay_password) {
        HttpHelp.getInstance().create(RemoteApi.class).pay(token, order_sn, pay_password, "1")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<Object>>(mContext, null) {
                    @Override
                    public void onNext(BaseBean<Object> baseBean) {
                        super.onNext(baseBean);

                        if (baseBean.code == 0) {

                            //余额支付
                            ToastUtil.showToast(mContext, baseBean.message);
                            refresh.autoRefresh();
                            ((OrderActivity) getActivity()).getCashBalanceData(helper.getToken());
                            passwordDialog.dismiss();
                        } else if (baseBean.code == 4) {
                            ToolUtil.goToLogin(mContext);
                        } else {
                            passwordDialog.clear();
                            ToastUtil.showToast(mContext, baseBean.message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        ToastUtil.showToast(mContext,"请检查订单");
                        passwordDialog.dismiss();
                    }
                });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        page=1;
        unbinder.unbind();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isLazy) {
            LogUtil.i("userVisible_OrderWaitPayFragment: " + isVisibleToUser);
            getData();
        }
    }
    /**
     * 微信支付(获取支付宝订单信息)
     * @param token
     * @param order_sn
     */
    private void weixinPay(String token, String order_sn) {
        WxPayUtil. wxPay(token,order_sn,mContext);
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
                .subscribe(new BaseSubscriber<BaseBean<String>>(mContext, null) {
                    @Override
                    public void onNext(BaseBean<String> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            LogUtil.i("支付宝：" + baseBean.data);
                            startAliPay(baseBean.data);
                        } else {
                            ToastUtil.showToast(mContext, baseBean.message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);

                    }
                });
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
                PayTask alipay = new PayTask((OrderActivity)getActivity());
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
                        Toast.makeText(mContext, "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(mContext, "支付失败", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(mContext,
                                "授权成功\n" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        // 其他状态值则为授权失败
                        Toast.makeText(mContext,
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
