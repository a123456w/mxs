package com.guo.qlzx.maxiansheng.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.guo.qlzx.maxiansheng.R;
import com.guo.qlzx.maxiansheng.adapter.CouponAdapter;
import com.guo.qlzx.maxiansheng.adapter.CouponLoseAdapter;
import com.guo.qlzx.maxiansheng.bean.CouponListBean;
import com.guo.qlzx.maxiansheng.http.RemoteApi;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.base.OnItemChildClickListener;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.qlzx.mylibrary.widget.RecycleViewDivider;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 李 on 2018/4/17.
 * 个人中心-优惠券
 */

public class CouponActivity extends BaseActivity implements OnItemChildClickListener {

    @BindView(R.id.tl_title)
    TabLayout tlTitle;
    @BindView(R.id.rl_list)
    RecyclerView rlList;
    @BindView(R.id.rl_lose)
    RecyclerView rlLose;
    @BindView(R.id.ll_list)
    RelativeLayout llList;
    @BindView(R.id.ll_lose)
    RelativeLayout llLose;
    @BindView(R.id.empty_list)
    RelativeLayout emptyList;
    @BindView(R.id.empty_lose)
    RelativeLayout emptyLose;
    @BindView(R.id.tv_skip)
    TextView tvSkip;
    private CouponAdapter adapter;
    private PreferencesHelper helper;
    private int type = 0;//未使用
    private CouponLoseAdapter loseAdapter;
    //已使用
    private List<CouponListBean> couponListBeans = new ArrayList<>();
    //未使用
    private List<CouponListBean> listBeans = new ArrayList<>();

    @Override
    public int getContentView() {
        return R.layout.activity_coupon;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setLeftImageRes(R.drawable.ic_back_gray);
        titleBar.setTitleText("优惠券");
        helper = new PreferencesHelper(this);
        adapter = new CouponAdapter(rlList);
        rlList.setLayoutManager(new LinearLayoutManager(this));
        rlList.addItemDecoration(new RecycleViewDivider(this));
        rlList.setAdapter(adapter);

        loseAdapter = new CouponLoseAdapter(rlList);
        rlLose.setLayoutManager(new LinearLayoutManager(this));
        rlLose.addItemDecoration(new RecycleViewDivider(this));
        rlLose.setAdapter(loseAdapter);
        adapter.setOnItemChildClickListener(this);
        initViews();

        tvSkip.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG );
        tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(CouponActivity.this, CouponCenterActivity.class);
                startActivity(intent1);
            }
        });
    }

    private void initViews() {
        tlTitle.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if ("未使用".equals(tab.getText())) {
                    type = 0;
                    llLose.setVisibility(View.GONE);
                    llList.setVisibility(View.VISIBLE);
                } else {
                    type = 1;
                    llLose.setVisibility(View.VISIBLE);
                    llList.setVisibility(View.GONE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void getData() {
        getUserCoupon(0, helper.getToken());
        getUserCoupon(1, helper.getToken());
    }

    private void getUserCoupon(final int type, String token) {
        HttpHelp.getInstance().create(RemoteApi.class).getUserCoupon(type, token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<List<CouponListBean>>>(this, null) {
                    @Override
                    public void onNext(BaseBean<List<CouponListBean>> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            if (type == 1) {
                                couponListBeans = baseBean.data;
                                if (couponListBeans.size() > 0) {
                                    loseAdapter.setData(couponListBeans);
                                }else {
                                    emptyLose.setVisibility(View.VISIBLE);
                                    rlLose.setVisibility(View.GONE);
                                }
                            } else {
                                listBeans = baseBean.data;
                                if (listBeans.size() > 0) {
                                    adapter.setData(listBeans);
                                } else {
                                    emptyList.setVisibility(View.VISIBLE);
                                    rlList.setVisibility(View.GONE);
                                }
                            }
                        } else {
                            ToastUtil.showToast(CouponActivity.this, baseBean.message);
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

    @Override
    public void onItemChildClick(ViewGroup parent, View childView, int position) {
        if ("0".equals(listBeans.get(position).getUser_type())){
            Intent intent1 = new Intent(CouponActivity.this, NewbieActivity.class);
            startActivity(intent1);
        }else {
            Intent intent1 = new Intent(CouponActivity.this, MainActivity.class);
            intent1.putExtra("go_home", 2);
            startActivity(intent1);
            finish();
        }

    }
}
