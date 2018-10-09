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
import com.guo.qlzx.maxiansheng.adapter.RapeseedAdapter;
import com.guo.qlzx.maxiansheng.bean.BalanceListBean;
import com.guo.qlzx.maxiansheng.bean.TotalSumBean;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 李 on 2018/4/18.
 * 我的余额/我的菜籽
 */

public class MyBalanceActivity extends BaseActivity implements CanRefreshLayout.OnRefreshListener, CanRefreshLayout.OnLoadMoreListener {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_int)
    TextView tvInt;
    @BindView(R.id.tv_float)
    TextView tvFloat;
    @BindView(R.id.iv_left)
    ImageView ivLeft;
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
    @BindView(R.id.tv_empty)
    TextView tvEmpty;
    @BindView(R.id.tv_text)
    TextView tvText;
    private PreferencesHelper helper;
    private int type = 0;
    private RapeseedAdapter adapter;
    private int page = 1;
    private List<BalanceListBean> beans = new ArrayList<>();

    @Override
    public int getContentView() {
        return R.layout.activity_balance;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        hideTitleBar();
        helper = new PreferencesHelper(this);
        adapter = new RapeseedAdapter(canContentView);
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

        type = getIntent().getIntExtra("ISBALANCE", 0);
        adapter.setType(type);
    }

    @Override
    public void getData() {
        showLoadingDialog("加载中...");
        type = getIntent().getIntExtra("ISBALANCE", 0);
        getAllMemory(helper.getToken());
        if (type == 0) {
            getBalanceData(helper.getToken(), String.valueOf(page));
            tvTitle.setText("元");
            tvEmpty.setText("您还没有余额相关记录！");
        } else {
            getRapeseedData(helper.getToken(), String.valueOf(page));
            tvTitle.setText("菜籽");
            tvText.setText("我的菜籽 ");
            tvEmpty.setText("您还没有菜籽相关记录！");
        }
    }

    //获取总额数据
    private void getAllMemory(String token) {
        HttpHelp.getInstance().create(RemoteApi.class).getBalance(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<TotalSumBean>>(this, null) {
                    @Override
                    public void onNext(BaseBean<TotalSumBean> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            float bal=0;
                            String bbb="";
                            if (type == 0) {
                                bbb= baseBean.data.getUser_money();
                            } else {
                                 bbb = baseBean.data.getRapeseed();
                            }
                            int idx = bbb.lastIndexOf(".");//查找小数点的位置
                            String strNum = bbb.substring(0,idx);//截取从字符串开始到小数点位置的字符串，就是整数部分
                            String bb = bbb.substring(bbb.indexOf("."));
                            tvInt.setText("" + strNum);
                            tvFloat.setText(bb);
                        } else if (baseBean.code == 4) {
                            ToolUtil.goToLogin(MyBalanceActivity.this);
                        }
                        hideLoadingDialog();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        hideLoadingDialog();
                    }
                });
    }

    //获取余额数据
    private void getBalanceData(String token, String type) {

        HttpHelp.getInstance().create(RemoteApi.class).getBalanceData(token, type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<List<BalanceListBean>>>(this, null) {
                    @Override
                    public void onNext(BaseBean<List<BalanceListBean>> baseBean) {
                        super.onNext(baseBean);
                        hideLoadingDialog();
                        if (baseBean.code == 0) {
                            refresh.refreshComplete();
                            refresh.loadMoreComplete();
                            beans = baseBean.data;
                            if (page == 1) {
                                adapter.setData(beans);
                                if (beans!=null&&beans.size()>0){
                                    rlEmpty.setVisibility(View.GONE);
                                }
                            } else {
                                if (beans.size() > 0 && beans != null) {
                                    adapter.addMoreData(beans);
                                } else {
                                    ToastUtil.showToast(MyBalanceActivity.this, "暂无更多");
                                }
                            }
                            if (adapter.getData().size()>0){
                                rlEmpty.setVisibility(View.GONE);
                            }
                        } else if (baseBean.code == 4) {
                            refresh.refreshComplete();
                            refresh.loadMoreComplete();
                            ToolUtil.goToLogin(MyBalanceActivity.this);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        refresh.refreshComplete();
                        refresh.loadMoreComplete();
                        hideLoadingDialog();
                        super.onError(throwable);
                    }
                });
    }

    //获取菜籽余额
    private void getRapeseedData(String token, String type) {
        HttpHelp.getInstance().create(RemoteApi.class).getRapeseedData(token, type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<List<BalanceListBean>>>(this, null) {
                    @Override
                    public void onNext(BaseBean<List<BalanceListBean>> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            refresh.refreshComplete();
                            refresh.loadMoreComplete();
                            beans = baseBean.data;
                            if (page == 1) {
                                adapter.setData(beans);
                                if (beans!=null&&beans.size()>0){
                                    rlEmpty.setVisibility(View.GONE);
                                }
                            } else {
                                if (beans.size() > 0 && beans != null) {
                                    adapter.addMoreData(beans);
                                } else {
                                    ToastUtil.showToast(MyBalanceActivity.this, "暂无更多");
                                }
                            }
                            if (adapter.getData().size()>0){
                                rlEmpty.setVisibility(View.GONE);
                            }
                        } else if (baseBean.code == 4) {
                            refresh.refreshComplete();
                            refresh.loadMoreComplete();
                            ToolUtil.goToLogin(MyBalanceActivity.this);
                        }
                        hideLoadingDialog();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        refresh.refreshComplete();
                        refresh.loadMoreComplete();
                        hideLoadingDialog();
                    }
                });
    }

    @OnClick({R.id.iv_left})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_left:
                finish();
                break;
        }
    }


    @Override
    public void onLoadMore() {
        page++;
        if (type == 0) {
            getBalanceData(helper.getToken(), String.valueOf(page));
        } else {
            getRapeseedData(helper.getToken(), String.valueOf(page));
        }
    }

    @Override
    public void onRefresh() {
        page = 1;
        if (type == 0) {
            getBalanceData(helper.getToken(), String.valueOf(page));
        } else {
            getRapeseedData(helper.getToken(), String.valueOf(page));
        }
    }
}
