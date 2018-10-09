package com.guo.qlzx.maxiansheng.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.guo.qlzx.maxiansheng.R;
import com.guo.qlzx.maxiansheng.adapter.CommenFragmentPagerAdapter;
import com.guo.qlzx.maxiansheng.bean.IsPasswordExitsBean;
import com.guo.qlzx.maxiansheng.bean.TotalSumBean;
import com.guo.qlzx.maxiansheng.event.ChangeHeaderEvent;
import com.guo.qlzx.maxiansheng.event.OrderNumberEvent;
import com.guo.qlzx.maxiansheng.event.OrderPayPwEvent;
import com.guo.qlzx.maxiansheng.event.RefreshNumEvent;
import com.guo.qlzx.maxiansheng.event.ShoppingCartNumEvent;
import com.guo.qlzx.maxiansheng.fragment.OrderAllFragment;
import com.guo.qlzx.maxiansheng.fragment.OrderWaitDeliverFragment;
import com.guo.qlzx.maxiansheng.fragment.OrderWaitEvaluateFragment;
import com.guo.qlzx.maxiansheng.fragment.OrderWaitPayFragment;
import com.guo.qlzx.maxiansheng.fragment.OrderWaitSendFragment;
import com.guo.qlzx.maxiansheng.http.RemoteApi;
import com.guo.qlzx.maxiansheng.util.ToolUtil;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.EventBusUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.widget.LoadingLayout;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/4/16 0016.
 */

public class OrderActivity extends BaseActivity {
    @BindView(R.id.my_tablayout)
    TabLayout myTablayout;
    @BindView(R.id.my_viewpager)
    ViewPager myViewpager;


    public String mBalance;
    public boolean havePassword;

    private ArrayList<String> titleList = new ArrayList<>();
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private int type=0;
    private PreferencesHelper helper;

    private CommenFragmentPagerAdapter adapter;

    @Override
    public int getContentView() {
        return R.layout.activity_order;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("全部订单");
        titleBar.setLeftImageRes(R.drawable.ic_back_gray);

        helper = new PreferencesHelper(this);

        titleList.add("全部");
        titleList.add("待付款");
        titleList.add("待配送");
        titleList.add("待收货");
        titleList.add("待评价");

        fragments.add(new OrderAllFragment());         //全部
        fragments.add(new OrderWaitPayFragment());     //待付款
        fragments.add(new OrderWaitSendFragment());    //待发货
        fragments.add(new OrderWaitDeliverFragment()); //待收货
        fragments.add(new OrderWaitEvaluateFragment());//待评价

        adapter = new CommenFragmentPagerAdapter(OrderActivity.this.getSupportFragmentManager(),titleList,fragments);
        myViewpager.setAdapter(adapter);
        myTablayout.setupWithViewPager(myViewpager);

        type = getIntent().getIntExtra("type",0);

        myViewpager.setCurrentItem(type);
        EventBusUtil.register(this);

    }

    @Override
    public void getData() {
        getCashBalanceData(helper.getToken());

        checkPassWord(helper.getToken());

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
                        }else if (baseBean.code==4){
                            ToolUtil.goToLogin(OrderActivity.this);
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

                        }else if (baseBean.code==4){
                            ToolUtil.goToLogin(OrderActivity.this);
                        } else {

                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);

                    }
                });
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void addNumber(OrderPayPwEvent event) {
        getCashBalanceData(helper.getToken());

        checkPassWord(helper.getToken());
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBusUtil.post(new ChangeHeaderEvent());
        EventBusUtil.post(new OrderNumberEvent());
        EventBusUtil.unregister(this);
    }
}
