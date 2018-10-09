package com.guo.qlzx.maxiansheng.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.canyinghao.canrefresh.CanRefreshLayout;
import com.canyinghao.canrefresh.classic.ClassicRefreshView;
import com.guo.qlzx.maxiansheng.R;
import com.guo.qlzx.maxiansheng.activity.CouponCenterActivity;
import com.guo.qlzx.maxiansheng.adapter.CouponCenterAdapter;
import com.guo.qlzx.maxiansheng.bean.OrderCouponListBean;
import com.guo.qlzx.maxiansheng.http.RemoteApi;
import com.guo.qlzx.maxiansheng.util.ToolUtil;
import com.guo.qlzx.maxiansheng.util.dialog.CouponCenterDialog;
import com.qlzx.mylibrary.base.BaseFragment;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.base.OnItemChildClickListener;
import com.qlzx.mylibrary.base.OnRVItemClickListener;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.DensityUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 李 on 2018/5/9.
 * 领券中心
 */

public class CouponCenterFragment extends BaseFragment implements  CanRefreshLayout.OnLoadMoreListener, CanRefreshLayout.OnRefreshListener, OnItemChildClickListener {

    @BindView(R.id.can_content_view)
    RecyclerView canContentView;
    @BindView(R.id.can_refresh_header)
    ClassicRefreshView canRefreshHeader;
    @BindView(R.id.can_refresh_footer)
    ClassicRefreshView canRefreshFooter;
    @BindView(R.id.refresh)
    CanRefreshLayout refresh;
    @BindView(R.id.rl_empty)
    LinearLayout rlEmpty;
    Unbinder unbinder;

    private CouponCenterAdapter adapter;
    private PreferencesHelper helper;
    private int page=1;
    private int type=0;
    private List<OrderCouponListBean> beans;

    @SuppressLint("ValidFragment")
    public CouponCenterFragment(int type){
        this.type=type;
    }
    public CouponCenterFragment(){

    }
    @Override
    public View getContentView(FrameLayout frameLayout) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_recycler_view, frameLayout, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void initView() {
        loadingLayout.setStatus(LoadingLayout.Success);
        helper = new PreferencesHelper(mContext);

        adapter=new CouponCenterAdapter(canContentView);
        canContentView.setLayoutManager(new LinearLayoutManager(mContext));
        canContentView.setAdapter(adapter);
        adapter.setOnItemChildClickListener(this);
        adapter.setType(1);
        refresh.setOnLoadMoreListener(this);
        refresh.setOnRefreshListener(this);
        refresh.setMaxFooterHeight(DensityUtil.dp2px(mContext, 150));
        refresh.setStyle(0, 0);

        canRefreshHeader.setPullStr("下拉刷新");
        canRefreshHeader.setReleaseStr("松开刷新");
        canRefreshHeader.setRefreshingStr("加载中");
        canRefreshHeader.setCompleteStr("");

        canRefreshFooter.setPullStr("上拉加载更多");
        canRefreshFooter.setReleaseStr("松开刷新");
        canRefreshFooter.setRefreshingStr("加载中");
        canRefreshFooter.setCompleteStr("");
    }

    @Override
    public void getData() {
        getCouponCenterData(helper.getToken(),type,page);
    }
    //获取优惠券列表
    private void getCouponCenterData(String token, int type, final int page){
        HttpHelp.getInstance().create(RemoteApi.class).getCouponCenterData(token,type,page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<List<OrderCouponListBean>>>(mContext, null) {
                    @Override
                    public void onNext(BaseBean<List<OrderCouponListBean>> baseBean) {
                        super.onNext(baseBean);
                        refresh.loadMoreComplete();
                        refresh.refreshComplete();
                        if (baseBean.code==0){

                            beans=baseBean.data;
                            if (page==1){
                                adapter.setData(beans);
                            }else {
                                if (beans.size()>0&&beans!=null){
                                    adapter.addMoreData(beans);
                                }else {
                                    ToastUtil.showToast(mContext,"暂无更多");
                                }
                            }
                            if (adapter.getItemCount()==0){
                                rlEmpty.setVisibility(View.VISIBLE);
                                canContentView.setVisibility(View.GONE);
                            }else {
                                rlEmpty.setVisibility(View.GONE);
                                canContentView.setVisibility(View.VISIBLE);
                            }
                        } else if (baseBean.code==4){
                            ToolUtil.goToLogin(mContext);
                        }
                    }
                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        refresh.loadMoreComplete();
                        refresh.refreshComplete();
                    }
                });
    }
    //获取优惠券
    private void getCoupons(String token ,String  id){
        HttpHelp.getInstance().create(RemoteApi.class).getCoupons(token,id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean>(mContext, null) {
                    @Override
                    public void onNext(BaseBean baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code==0){
                            getCouponCenterData(helper.getToken(),type,page);
                            final CouponCenterDialog dialog=new CouponCenterDialog(mContext);
                            dialog.show();
                            final int[] i = {3};
                            final Timer timer=new Timer();
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            i[0]--;
                                            if (i[0] ==0){
                                                dialog.cancel();
                                                timer.cancel();
                                            }
                                            dialog.setTime(i[0]);
                                        }
                                    });
                                }
                            },1000,1000);
                        } else if (baseBean.code==4){
                            ToolUtil.goToLogin(mContext);
                        }else {
                            ToastUtil.showToast(mContext,baseBean.message);
                        }
                    }
                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onLoadMore() {
        page++;
        getCouponCenterData(helper.getToken(),type,page);
    }

    @Override
    public void onRefresh() {
        page=1;
        getCouponCenterData(helper.getToken(),type,page);
    }

    @Override
    public void onItemChildClick(ViewGroup parent, View childView, int position) {
        switch (childView.getId()){
            case R.id.tv_btn:
                getCoupons(helper.getToken(),adapter.getItem(position).getId()+"");
                break;
        }
    }
}
