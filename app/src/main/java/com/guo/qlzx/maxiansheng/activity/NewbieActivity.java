package com.guo.qlzx.maxiansheng.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;

import com.guo.qlzx.maxiansheng.R;
import com.guo.qlzx.maxiansheng.adapter.HomeCouponsAdapter;
import com.guo.qlzx.maxiansheng.adapter.HomeRecommendAdapter;
import com.guo.qlzx.maxiansheng.adapter.NewbieCouponsAdapter;
import com.guo.qlzx.maxiansheng.adapter.NewbieDisCountsAdapter;
import com.guo.qlzx.maxiansheng.bean.HomeCouponListBeans;
import com.guo.qlzx.maxiansheng.bean.HomeRecommendListBean;
import com.guo.qlzx.maxiansheng.bean.SpecDialogBean;
import com.guo.qlzx.maxiansheng.event.ShoppingCartEvent;
import com.guo.qlzx.maxiansheng.http.RemoteApi;
import com.guo.qlzx.maxiansheng.util.ToolUtil;
import com.guo.qlzx.maxiansheng.util.dialog.HomeCouponDialog;
import com.guo.qlzx.maxiansheng.util.dialog.SelectDialog;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.base.OnItemChildClickListener;
import com.qlzx.mylibrary.base.OnRVItemClickListener;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.EventBusUtil;
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
 * Created by 李 on 2018/4/25.
 * 新人页面
 */

public class NewbieActivity extends BaseActivity implements OnItemChildClickListener, OnRVItemClickListener {
    @BindView(R.id.rl_coupon)
    GridView rlCoupon;
    @BindView(R.id.rl_discounts)
    RecyclerView rlDiscounts;
    @BindView(R.id.iv_left)
    ImageView ivLeft;
    private NewbieDisCountsAdapter recommendAdapter;
    private List<HomeRecommendListBean> recommendListBeans=new ArrayList<>();
    private int page=1,type=1;
    private NewbieCouponsAdapter couponsAdapter;
    private List<HomeCouponListBeans> couponListBeans;
    private PreferencesHelper helper;
    @Override
    public int getContentView() {
        return R.layout.activity_new_preson;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        hideTitleBar();
        initEvent();
        recommendAdapter = new NewbieDisCountsAdapter(rlDiscounts);
        GridLayoutManager layoutManager=new GridLayoutManager(this,2){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        rlDiscounts.setLayoutManager(new GridLayoutManager(this,2));
        rlDiscounts.setAdapter(recommendAdapter);

        couponsAdapter = new NewbieCouponsAdapter(rlCoupon);
        //rlCoupon.setLayoutManager(new GridLayoutManager(this,2));
        rlCoupon.setFocusable(false);
        rlCoupon.setAdapter(couponsAdapter);
        recommendAdapter.setOnItemChildClickListener(this);
        recommendAdapter.setOnRVItemClickListener(this);
        helper=new PreferencesHelper(this);
    }

    private void initEvent() {
        ivLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void getData() {
        getRecommendData(page,type);
        getCouponData(2,helper.getToken());
    }
    /**
     * 4.获取热销数据
     */
    public void getRecommendData(int page,int type) {
        HttpHelp.getInstance().create(RemoteApi.class).getHomeRecommendData(page,type)
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
    /**
     * 5.获取弹出优惠券优惠券数据
     * 需要传递哪一种会员类型
     */
    public void getCouponData(int type,String token) {
        HttpHelp.getInstance().create(RemoteApi.class).getHomeCouponData(type,token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<List<HomeCouponListBeans>>>(this, null) {
                    @Override
                    public void onNext(BaseBean<List<HomeCouponListBeans>> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            couponListBeans = baseBean.data;
                            couponsAdapter.setData(couponListBeans);
                        } else {

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

        switch (childView.getId()){
            case R.id.iv_add:
                showAddDialog(recommendAdapter.getItem(position));
                break;
        }
    }

    @Override
    public void onRVItemClick(ViewGroup parent, View itemView, int position) {
        Intent intent=new Intent(NewbieActivity.this,ProductDetailsActivity.class);
        intent.putExtra("goods_id",""+recommendAdapter.getItem(position).getGoods_id());
        startActivity(intent);
    }
    private SelectDialog cartDialog;
    //热销 添加购物车
    private void showAddDialog(HomeRecommendListBean bean){
        if (bean.getSpec_type() == 0) {
            //不存在规格
            addShoppingCart(helper.getToken(),String.valueOf(bean.getGoods_id()),"","",1);
        } else {
            //存在规格 选择规格
            cartDialog = new SelectDialog(NewbieActivity.this,bean.getType());
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
                        ToastUtil.showToast(NewbieActivity.this,"当前商品库存为0份");
                        return;
                    }
                    addShoppingCart(helper.getToken(),goods_id,service_id,spec_id,num);
                }
            });
            cartDialog.show();
            cartDialog.setData(specDialogBean);
        }
    }


    /**
     * 加入购物车
     * @param token
     * @param goods_id
     * @param service_id
     * @param spec_id
     * @param goods_num
     */
    private void addShoppingCart(String token,String goods_id,String service_id,String spec_id,int goods_num){
        HttpHelp.getInstance().create(RemoteApi.class).addCart(token,goods_id,service_id,spec_id,goods_num)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<Object>>(NewbieActivity.this, null) {
                    @Override
                    public void onNext(BaseBean<Object> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            EventBusUtil.post(new ShoppingCartEvent());
                            ToastUtil.showToast(NewbieActivity.this, baseBean.message);
                        } else if (baseBean.code==4){
                            ToolUtil.goToLogin(NewbieActivity.this);
                        }else {
                            ToastUtil.showToast(NewbieActivity.this, baseBean.message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);

                    }
                });
    }
}
