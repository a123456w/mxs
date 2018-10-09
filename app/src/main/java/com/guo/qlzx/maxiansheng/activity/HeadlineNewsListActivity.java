package com.guo.qlzx.maxiansheng.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.canyinghao.canrefresh.CanRefreshLayout;
import com.canyinghao.canrefresh.classic.ClassicRefreshView;
import com.guo.qlzx.maxiansheng.R;
import com.guo.qlzx.maxiansheng.adapter.HeadlineNewsAdapter;
import com.guo.qlzx.maxiansheng.bean.HeadlineNewsBean;
import com.guo.qlzx.maxiansheng.http.RemoteApi;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.base.OnItemChildClickListener;
import com.qlzx.mylibrary.base.OnRVItemClickListener;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.DensityUtil;
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
 * Created by 李 on 2018/4/11.
 * 头条新闻列表
 */

public class HeadlineNewsListActivity extends BaseActivity implements CanRefreshLayout.OnLoadMoreListener, CanRefreshLayout.OnRefreshListener, OnRVItemClickListener {
    @BindView(R.id.can_content_view)
    RecyclerView canContentView;
    @BindView(R.id.can_refresh_header)
    ClassicRefreshView canRefreshHeader;
    @BindView(R.id.can_refresh_footer)
    ClassicRefreshView canRefreshFooter;
    @BindView(R.id.refresh)
    CanRefreshLayout refresh;
    @BindView(R.id.iv_left)
    ImageView ivLeft;


    private HeadlineNewsAdapter adapter;
    private List<HeadlineNewsBean> beans = new ArrayList<>();

    private int page = 1;

    @Override
    public int getContentView() {
        return R.layout.activity_headline;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        hideTitleBar();

        adapter = new HeadlineNewsAdapter(canContentView);
        canContentView.setLayoutManager(new LinearLayoutManager(this));
        canContentView.addItemDecoration(new RecycleViewDivider(this));
        canContentView.setAdapter(adapter);


        adapter.setOnRVItemClickListener(this);
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
        ivLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void getData() {
        getListData(page);
    }

    private void getListData(final int page) {
        HttpHelp.getInstance().create(RemoteApi.class).getHeadlineData(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<List<HeadlineNewsBean>>>(this, null) {
                    @Override
                    public void onNext(BaseBean<List<HeadlineNewsBean>> baseBean) {
                        super.onNext(baseBean);
                        refresh.refreshComplete();
                        refresh.loadMoreComplete();
                        if (baseBean.code == 0) {
                            beans = baseBean.data;
                            if (page == 1) {
                                adapter.setData(beans);
                            } else {
                                if (beans.size() > 0 && beans != null) {
                                    adapter.addMoreData(beans);
                                } else {
                                    ToastUtil.showToast(HeadlineNewsListActivity.this, "暂无更多");
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
    }

    ;

    @Override
    public void onLoadMore() {
        page++;
        getListData(page);
    }

    @Override
    public void onRefresh() {
        page = 1;
        getListData(page);
    }
    @Override
    public void onRVItemClick(ViewGroup parent, View itemView, int position) {
        HeadlineNewsDetailsActivity.startActivity(HeadlineNewsListActivity.this,adapter.getItem(position).getTitle(),
                adapter.getItem(position).getNews_link(),adapter.getItem(position).getId());
//        WebActivity.startActivity(HeadlineNewsListActivity.this,adapter.getItem(position).getTitle(),adapter.getItem(position).getNews_link());
    }
}
