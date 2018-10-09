package com.guo.qlzx.maxiansheng.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.guo.qlzx.maxiansheng.R;
import com.guo.qlzx.maxiansheng.adapter.OrderCouponAdapter;
import com.guo.qlzx.maxiansheng.bean.OrderCouponBean;
import com.guo.qlzx.maxiansheng.http.RemoteApi;
import com.guo.qlzx.maxiansheng.util.ToolUtil;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.base.OnItemChildClickListener;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 李 on 2018/4/27.
 * 订单中-使用优惠券
 */

public class OrderCouponActivity extends BaseActivity implements OnItemChildClickListener {
    @BindView(R.id.rl_list)
    RecyclerView rlList;
    @BindView(R.id.rl_empty)
    LinearLayout rlEmpty;
    @BindView(R.id.tv_skip)
    TextView tvSkip;

    private PreferencesHelper helper;
    private OrderCouponAdapter adapter;
    private List<OrderCouponBean> listBeans = new ArrayList<>();
    private String price = "";

    @Override
    public int getContentView() {
        return R.layout.activity_order_coupon;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("使用优惠券");
        helper = new PreferencesHelper(this);

        adapter = new OrderCouponAdapter(rlList);
        rlList.setLayoutManager(new LinearLayoutManager(this));
        rlList.setAdapter(adapter);


        price = getIntent().getStringExtra("order_price");
        adapter.setOnItemChildClickListener(this);

        tvSkip.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG );
        tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(OrderCouponActivity.this, CouponCenterActivity.class);
                startActivity(intent1);
            }
        });
    }

    @Override
    public void getData() {
        useCoupon(price, helper.getToken());
    }

    private void useCoupon(String price, String token) {
        HttpHelp.getInstance().create(RemoteApi.class).useCoupon(token, price)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<List<OrderCouponBean>>>(this, null) {
                    @Override
                    public void onNext(BaseBean<List<OrderCouponBean>> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            listBeans = baseBean.data;
                            adapter.setData(listBeans);
                            if (listBeans.size()==0){
                                rlEmpty.setVisibility(View.VISIBLE);
                                rlList.setVisibility(View.GONE);
                            }else {
                                rlEmpty.setVisibility(View.GONE);
                                rlList.setVisibility(View.VISIBLE);
                            }
                        } else if (baseBean.code == 4) {
                            rlEmpty.setVisibility(View.VISIBLE);
                            rlList.setVisibility(View.GONE);
                            ToolUtil.goToLogin(OrderCouponActivity.this);
                        } else {
                            rlEmpty.setVisibility(View.VISIBLE);
                            rlList.setVisibility(View.GONE);
                            ToastUtil.showToast(OrderCouponActivity.this, "暂无可用优惠券");
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        rlEmpty.setVisibility(View.VISIBLE);
                        rlList.setVisibility(View.GONE);
                    }
                });
    }


    //选择优惠券
    private void selectCoupon(String token, final String cid, final String price) {
        HttpHelp.getInstance().create(RemoteApi.class).selectCoupon(token, cid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<Object>>(OrderCouponActivity.this, null) {
                    @Override
                    public void onNext(BaseBean<Object> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            Intent intent = new Intent();
                            intent.putExtra("id", cid);
                            intent.putExtra("price", price);
                            setResult(101, intent);
                            finish();
                        } else if (baseBean.code == 4) {
                            ToolUtil.goToLogin(OrderCouponActivity.this);
                        } else {
                            ToastUtil.showToast(OrderCouponActivity.this, baseBean.message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);

                    }
                });
    }

    @Override
    public void onItemChildClick(ViewGroup parent, View childView, int position) {
        switch (childView.getId()) {
            case R.id.tv_use://立即使用
                selectCoupon(helper.getToken(), adapter.getItem(position).getId() + "", adapter.getItem(position).getMoney() + "");
                break;
        }
    }
}
