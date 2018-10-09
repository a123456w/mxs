package com.guo.qlzx.maxiansheng.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.canyinghao.canrefresh.CanRefreshLayout;
import com.canyinghao.canrefresh.classic.ClassicRefreshView;
import com.guo.qlzx.maxiansheng.R;
import com.guo.qlzx.maxiansheng.adapter.PutForwardAdapter;
import com.guo.qlzx.maxiansheng.adapter.RefundAdapter;
import com.guo.qlzx.maxiansheng.bean.ForwardBean;
import com.guo.qlzx.maxiansheng.bean.RefundBean;
import com.guo.qlzx.maxiansheng.http.RemoteApi;
import com.guo.qlzx.maxiansheng.util.ToolUtil;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.DensityUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/*
 * create by xuxx  data 2018/09/26
 * */
public class RefundActivity extends BaseActivity implements CanRefreshLayout.OnRefreshListener, CanRefreshLayout.OnLoadMoreListener {

    @BindView(R.id.can_content_view)
    RecyclerView canContentView;
    @BindView(R.id.can_refresh_header)
    ClassicRefreshView canRefreshHeader;
    @BindView(R.id.can_refresh_footer)
    ClassicRefreshView canRefreshFooter;
    @BindView(R.id.refresh)
    CanRefreshLayout refresh;
    @BindView(R.id.iv_empty)
    ImageView ivEmpty;
    @BindView(R.id.tv_empty)
    TextView tvEmpty;
    @BindView(R.id.rl_empty)
    LinearLayout rlEmpty;
    Unbinder unbinder;
    private int page = 1;
    RefundAdapter adapter;

    @Override
    public int getContentView() {
        return R.layout.activity_refund;
    }

    @Override
    public void initView() {
        unbinder = ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("退款记录");
        adapter = new RefundAdapter(canContentView);
        canContentView.setLayoutManager(new LinearLayoutManager(this));
        canContentView.setAdapter(adapter);

        refresh.setOnLoadMoreListener(this);
        refresh.setOnRefreshListener(this);
        refresh.setMaxFooterHeight(DensityUtil.dp2px(this, 150));
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
        getAssetData();
    }

    private void getAssetData() {
        HttpHelp.getInstance().create(RemoteApi.class).getUserReturnOrderList(new PreferencesHelper(this).getToken(), page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<List<RefundBean>>>(this, null) {
                    @Override
                    public void onNext(BaseBean<List<RefundBean>> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            refresh.refreshComplete();
                            refresh.loadMoreComplete();
                            List<RefundBean> beans = baseBean.data;
                            if (page == 1) {
                                adapter.setData(beans);
                                if (beans != null && beans.size() > 0) {
                                    rlEmpty.setVisibility(View.GONE);
                                } else {
                                    rlEmpty.setVisibility(View.VISIBLE);
                                }
                            } else {
                                if (beans.size() > 0 && beans != null) {
                                    adapter.addMoreData(beans);
                                } else {
                                    ToastUtil.showToast(RefundActivity.this, "暂无更多");
                                }
                            }

                        } else if (baseBean.code == 4) {
                            ToolUtil.goToLogin(RefundActivity.this);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void onLoadMore() {
        page++;
        getAssetData();
    }

    @Override
    public void onRefresh() {
        page = 1;
        getAssetData();
    }

}
