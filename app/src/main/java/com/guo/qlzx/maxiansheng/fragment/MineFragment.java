package com.guo.qlzx.maxiansheng.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.guo.qlzx.maxiansheng.R;
import com.guo.qlzx.maxiansheng.activity.CouponActivity;
import com.guo.qlzx.maxiansheng.activity.FootMarkActivity;
import com.guo.qlzx.maxiansheng.activity.InviteActivity;
import com.guo.qlzx.maxiansheng.activity.LoginActivity;
import com.guo.qlzx.maxiansheng.activity.MemberCenterActivity;
import com.guo.qlzx.maxiansheng.activity.MessageCenterActivity;
import com.guo.qlzx.maxiansheng.activity.MyAssetActivity;
import com.guo.qlzx.maxiansheng.activity.MyBalanceActivity;
import com.guo.qlzx.maxiansheng.activity.OrderActivity;
import com.guo.qlzx.maxiansheng.activity.SetActivity;
import com.guo.qlzx.maxiansheng.activity.SetFeedBackActivity;
import com.guo.qlzx.maxiansheng.activity.SetPersonalDetailsActivity;
import com.guo.qlzx.maxiansheng.activity.SetSafetyActivity;
import com.guo.qlzx.maxiansheng.bean.MessageBean;
import com.guo.qlzx.maxiansheng.bean.OrderNumberBean;
import com.guo.qlzx.maxiansheng.bean.UserInfoBean;
import com.guo.qlzx.maxiansheng.event.ChangeHeaderEvent;
import com.guo.qlzx.maxiansheng.event.LoginEvent;
import com.guo.qlzx.maxiansheng.event.OrderNumberEvent;
import com.guo.qlzx.maxiansheng.event.RefreshNumEvent;
import com.guo.qlzx.maxiansheng.http.RemoteApi;
import com.guo.qlzx.maxiansheng.util.ToolUtil;
import com.qlzx.mylibrary.base.BaseFragment;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.EventBusUtil;
import com.qlzx.mylibrary.util.GlideUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.widget.LoadingLayout;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * 我的页面
 * Created by dillon on 2018/4/10.
 */

public class MineFragment extends BaseFragment {
    @BindView(R.id.tv_number)
    TextView tvNumber;
    @BindView(R.id.tv_plus_day)
    TextView tvPlusDay;
    @BindView(R.id.tv_center)
    TextView tvCenter;
    @BindView(R.id.iv_message)
    ImageView ivMessage;
    @BindView(R.id.title)
    RelativeLayout title;
    @BindView(R.id.iv_center_image)
    ImageView ivCenterImage;
    @BindView(R.id.tv_login_and_register)
    TextView tvLoginAndRegister;
    @BindView(R.id.tv_mynickname)
    TextView tvMynickname;
    @BindView(R.id.rl_comp)
    RelativeLayout rlComp;
    @BindView(R.id.v_middle_bg)
    View vMiddleBg;
    @BindView(R.id.ll_techan_dingdan)
    LinearLayout llTechanDingdan;
    @BindView(R.id.ll_yuding_dingdan)
    LinearLayout llYudingDingdan;
    @BindView(R.id.ll_shangcheng_dingdan)
    LinearLayout llShangchengDingdan;
    @BindView(R.id.ll_top_layout)
    LinearLayout llTopLayout;
    @BindView(R.id.tv_all_orders)
    TextView tvAllOrders;
    @BindView(R.id.ll_wait_pay)
    LinearLayout llWaitPay;
    @BindView(R.id.ll_wait_send)
    LinearLayout llWaitSend;
    @BindView(R.id.ll_wait_deliver)
    LinearLayout llWaitDeliver;
    @BindView(R.id.ll_wait_evalute)
    LinearLayout llWaitEvalute;
    @BindView(R.id.ll_1_row)
    LinearLayout ll1Row;
    @BindView(R.id.ll_order)
    LinearLayout llOrder;
    @BindView(R.id.ll_wode_fudai)
    LinearLayout llWodeFudai;
    @BindView(R.id.ll_wode_qianbao)
    LinearLayout llWodeQianbao;
    @BindView(R.id.ll_kanfang_jilu)
    LinearLayout llKanfangJilu;
//    @BindView(R.id.wode_shoucang)
//    LinearLayout wodeShoucang;
    @BindView(R.id.ll_2_row)
    LinearLayout ll2Row;
    @BindView(R.id.ll_tool)
    LinearLayout llTool;
    Unbinder unbinder;
    @BindView(R.id.number_one)
    TextView numberOne;
    @BindView(R.id.number_two)
    TextView numberTwo;
    @BindView(R.id.number_fourthree)
    TextView numberFourthree;
    @BindView(R.id.number_four)
    TextView numberFour;
    @BindView(R.id.ll_wode_fankui)
    LinearLayout llWodeFankui;
    Unbinder unbinder1;

    private PreferencesHelper helper;

    @Override
    public View getContentView(FrameLayout frameLayout) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_mine, frameLayout, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void initView() {
        loadingLayout.setStatus(LoadingLayout.Success);
        EventBusUtil.register(this);
        helper = new PreferencesHelper(mContext);
        Log.i("TAG", helper.getToken());
    }

    @Override
    public void getData() {
        if (!TextUtils.isEmpty(new PreferencesHelper(getActivity()).getToken())) {
            getMessageNumber(helper.getToken());
            getUserInfo(helper.getToken());
            getOrderNumber(helper.getToken());
        }
    }
    //获取消息未读个数

    public void getMessageNumber(String token) {
        HttpHelp.getInstance().create(RemoteApi.class).getMessageCount(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<MessageBean>>(mContext, null) {
                    @Override
                    public void onNext(BaseBean<MessageBean> messageBeanBaseBean) {
                        super.onNext(messageBeanBaseBean);
                        if (messageBeanBaseBean.code == 0) {
                            MessageBean messageBean = messageBeanBaseBean.data;
                            String messageNum = messageBean.getCount();
                            if (!TextUtils.isEmpty(messageNum) && !"0".equals(messageNum)) {
                                tvNumber.setVisibility(View.VISIBLE);
                                tvNumber.setText(messageNum);
                            } else {
                                tvNumber.setVisibility(View.GONE);
                            }
                        } else {
                            tvNumber.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        tvNumber.setVisibility(View.GONE);

                    }
                });

    }


    private void getUserInfo(String token) {
        HttpHelp.getInstance().create(RemoteApi.class).getInfo(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<UserInfoBean>>(mContext, null) {
                    @Override
                    public void onNext(BaseBean<UserInfoBean> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            helper.savePhoneNum(baseBean.data.getMobile());
                            GlideUtil.displayAvatar(mContext, Constants.IMG_HOST + baseBean.data.getImg(), ivCenterImage);
                            tvLoginAndRegister.setVisibility(View.GONE);
                            tvMynickname.setText(ToolUtil.setTingPhone(baseBean.data.getMobile()));
                            tvMynickname.setVisibility(View.VISIBLE);
                            if (baseBean.data.getNew_people() == 2) {
                                tvPlusDay.setVisibility(View.VISIBLE);
                                if (Integer.valueOf(baseBean.data.getPlus_last_time()) < 9999) {
                                    tvPlusDay.setText("剩余" + baseBean.data.getPlus_last_time() + "天");
                                } else {
                                    tvPlusDay.setText("剩余9999+天");
                                }
                            } else {
                                tvPlusDay.setVisibility(View.GONE);
                            }
                            Log.i("TAG", "" + helper.getToken());
                        }/*else if(baseBean.code == 4) {
                                    //ToolUtil.loseToLogin(mContext);
                                    tvPlusDay.setVisibility(View.GONE);
                                }*/ else {
                            //ToastUtil.showToast(mContext, baseBean.message);
                            tvLoginAndRegister.setVisibility(View.VISIBLE);
                            tvMynickname.setVisibility(View.GONE);
                            tvPlusDay.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);

                    }
                });
    }

    private void getOrderNumber(String token) {
        HttpHelp.getInstance().create(RemoteApi.class).getOrderNumber(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<OrderNumberBean>>(mContext, null) {
                    @Override
                    public void onNext(BaseBean<OrderNumberBean> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            OrderNumberBean data = baseBean.data;
                            numberOne.setVisibility(View.VISIBLE);
                            numberTwo.setVisibility(View.VISIBLE);
                            numberFourthree.setVisibility(View.VISIBLE);
                            numberFour.setVisibility(View.VISIBLE);
                            //待付款
                            if (0 == data.getPayment()) {
                                numberOne.setVisibility(View.GONE);
                            } else if (data.getPayment() > 99) {
                                numberOne.setText("99+");
                            } else {
                                numberOne.setText("" + data.getPayment());
                            }
                            //代配送
                            if (0 == data.getDistribution()) {
                                numberTwo.setVisibility(View.GONE);
                            } else if (data.getDistribution() > 99) {
                                numberTwo.setText("99+");
                            } else {
                                numberTwo.setText("" + data.getDistribution());
                            }
                            //代收货
                            if (0 == data.getCollect()) {
                                numberFourthree.setVisibility(View.GONE);
                            } else if (data.getCollect() > 99) {
                                numberFourthree.setText("99+");
                            } else {
                                numberFourthree.setText("" + data.getCollect());
                            }
                            //代评价
                            if (0 == data.getComment()) {
                                numberFour.setVisibility(View.GONE);
                            } else if (data.getComment() > 99) {
                                numberFour.setText("99+");
                            } else {
                                numberFour.setText("" + data.getComment());
                            }
                        } else {
                            numberOne.setVisibility(View.GONE);
                            numberTwo.setVisibility(View.GONE);
                            numberFourthree.setVisibility(View.GONE);
                            numberFour.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        numberOne.setVisibility(View.GONE);
                        numberTwo.setVisibility(View.GONE);
                        numberFourthree.setVisibility(View.GONE);
                        numberFour.setVisibility(View.GONE);
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.iv_message, R.id.iv_set, R.id.tv_login_and_register, R.id.ll_wait_pay, R.id.ll_wait_send,
            R.id.ll_wait_deliver, R.id.ll_wait_evalute, R.id.tv_all_orders, R.id.ll_safety, R.id.ll_shangcheng_dingdan,
            R.id.ll_kanfang_jilu, R.id.ll_wode_qianbao, R.id.ll_yuding_dingdan, R.id.ll_techan_dingdan, R.id.ll_wode_fudai, R.id.rl_icon,R.id.ll_wode_fankui})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_message:   // 消息
                if (TextUtils.isEmpty(helper.getToken())) {
                    ToolUtil.goToLogin(mContext);
                } else {
                    Intent intent9 = new Intent(mContext, MessageCenterActivity.class);
                    startActivity(intent9);
                }
                break;
            case R.id.iv_set:     // 设置
                if (TextUtils.isEmpty(helper.getToken())) {
                    ToolUtil.goToLogin(mContext);
                } else {
                    Intent intent = new Intent(mContext, SetActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.tv_login_and_register:// 登陆、注册
                startActivity(new Intent(mContext, LoginActivity.class));
                break;
            case R.id.tv_all_orders://全部订单
                if (TextUtils.isEmpty(helper.getToken())) {
                    ToolUtil.goToLogin(mContext);
                } else {
                    Intent waitAllIntent = new Intent(mContext, OrderActivity.class);
                    waitAllIntent.putExtra("type", 0);
                    startActivity(waitAllIntent);
                }
                break;
            case R.id.ll_wait_pay://待付款
                if (TextUtils.isEmpty(helper.getToken())) {
                    ToolUtil.goToLogin(mContext);
                } else {
                    Intent waitPayIntent = new Intent(mContext, OrderActivity.class);
                    waitPayIntent.putExtra("type", 1);
                    startActivity(waitPayIntent);
                }
                break;
            case R.id.ll_wait_send://待发货
                if (TextUtils.isEmpty(helper.getToken())) {
                    ToolUtil.goToLogin(mContext);
                } else {
                    Intent waitSendIntent = new Intent(mContext, OrderActivity.class);
                    waitSendIntent.putExtra("type", 2);
                    startActivity(waitSendIntent);
                }
                break;
            case R.id.ll_wait_deliver://配送中
                if (TextUtils.isEmpty(helper.getToken())) {
                    ToolUtil.goToLogin(mContext);
                } else {
                    Intent waitDeliverIntent = new Intent(mContext, OrderActivity.class);
                    waitDeliverIntent.putExtra("type", 3);
                    startActivity(waitDeliverIntent);
                }
                break;
            case R.id.ll_wait_evalute://待评价
                if (TextUtils.isEmpty(helper.getToken())) {
                    ToolUtil.goToLogin(mContext);
                } else {
                    Intent waitEvaluteIntent = new Intent(mContext, OrderActivity.class);
                    waitEvaluteIntent.putExtra("type", 4);
                    startActivity(waitEvaluteIntent);
                }
                break;

//            //优惠券
//            case R.id.wode_shoucang:
//                if (TextUtils.isEmpty(helper.getToken())) {
//                    ToolUtil.goToLogin(mContext);
//                } else {
//                    Intent intent1 = new Intent(mContext, CouponActivity.class);
//                    startActivity(intent1);
//                }
//                break;
            //我的足迹
            case R.id.ll_shangcheng_dingdan:
                if (TextUtils.isEmpty(helper.getToken())) {
                    ToolUtil.goToLogin(mContext);
                } else {
                    startActivity(new Intent(mContext, FootMarkActivity.class));
                }
                break;
            //邀请好友
            case R.id.ll_kanfang_jilu:
                if (TextUtils.isEmpty(helper.getToken())) {
                    ToolUtil.goToLogin(mContext);
                } else {
                    startActivity(new Intent(mContext, InviteActivity.class));
                }
                break;
            //会员中心
            case R.id.ll_wode_qianbao:
                if (TextUtils.isEmpty(helper.getToken())) {
                    ToolUtil.goToLogin(mContext);
                } else {
                    startActivity(new Intent(mContext, MemberCenterActivity.class));
                }
                break;
            case R.id.ll_techan_dingdan:
                //我的余额
                if (TextUtils.isEmpty(helper.getToken())) {
                    ToolUtil.goToLogin(mContext);
                } else {
                    Intent intent2 = new Intent(mContext, MyAssetActivity.class);
//                    intent2.putExtra("ISBALANCE", 0);
                    startActivity(intent2);
                }
                break;
            case R.id.ll_yuding_dingdan:
                //我的菜籽
                if (TextUtils.isEmpty(helper.getToken())) {
                    ToolUtil.goToLogin(mContext);
                } else {
                    Intent intent3 = new Intent(mContext, MyBalanceActivity.class);
                    intent3.putExtra("ISBALANCE", 1);
                    startActivity(intent3);
                }
                break;
                //意见反馈
            case R.id.ll_wode_fankui:
                //意见反馈
                if (TextUtils.isEmpty(new PreferencesHelper(mContext).getToken())) {
                    ToolUtil.goToLogin(mContext);
                    return;
                }
                startActivity(new Intent(mContext, SetFeedBackActivity.class));
                break;
            case R.id.ll_wode_fudai:
                //我的资产
                if (TextUtils.isEmpty(helper.getToken())) {
                    ToolUtil.goToLogin(mContext);
                } else {
                    startActivity(new Intent(mContext, MyAssetActivity.class));
                }
                break;
            case R.id.rl_icon:
                //头像
                if (TextUtils.isEmpty(helper.getToken())) {
                    ToolUtil.loseToLogin(mContext);
                    return;
                }
                startActivity(new Intent(mContext, SetPersonalDetailsActivity.class));
                break;
            case R.id.ll_safety:
                //账户安全
                if (TextUtils.isEmpty(new PreferencesHelper(mContext).getToken())) {
                    ToolUtil.goToLogin(mContext);
                    return;
                }
                startActivity(new Intent(mContext, SetSafetyActivity.class));
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginEvent(LoginEvent event) {
        if (event.isLogin()) {
            getData();
        } else {
            GlideUtil.display(mContext, R.drawable.ic_head, ivCenterImage);
            tvLoginAndRegister.setVisibility(View.VISIBLE);
            tvMynickname.setVisibility(View.GONE);
        }

    }

    /**
     * 修改提示数量的时间
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refereshNum(RefreshNumEvent event) {
        getMessageNumber(helper.getToken());
        getOrderNumber(helper.getToken());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHeadChangedEvent(ChangeHeaderEvent event) {
        getUserInfo(helper.getToken());
        getMessageNumber(helper.getToken());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHeadChangeEvent(OrderNumberEvent event) {
        getOrderNumber(helper.getToken());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBusUtil.unregister(this);
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        //   进入当前Fragment
        if (enter) {
            //   这里可以做网络请求或者需要的数据刷新操作
            getMessageNumber(helper.getToken());
            getUserInfo(helper.getToken());
            getOrderNumber(helper.getToken());
        } else {

        }
        return super.onCreateAnimation(transit, enter, nextAnim);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder1 = ButterKnife.bind(this, rootView);
        return rootView;
    }
}
