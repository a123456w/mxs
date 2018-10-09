package com.guo.qlzx.maxiansheng.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.canyinghao.canrefresh.CanRefreshLayout;
import com.canyinghao.canrefresh.classic.ClassicRefreshView;
import com.guo.qlzx.maxiansheng.R;
import com.guo.qlzx.maxiansheng.adapter.MessageCenterListAdapter;
import com.guo.qlzx.maxiansheng.bean.MessageCenterListBean;
import com.guo.qlzx.maxiansheng.http.RemoteApi;
import com.guo.qlzx.maxiansheng.util.ToolUtil;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.base.OnItemChildClickListener;
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
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.guo.qlzx.maxiansheng.util.costom.SwipeItemLayout.OnSwipeItemTouchListener;

/**
 * Created by 李 on 2018/4/27.
 */

public class MessageCenterListActivity extends BaseActivity implements OnItemChildClickListener, CanRefreshLayout.OnLoadMoreListener, CanRefreshLayout.OnRefreshListener {
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
    private PreferencesHelper helper;
    private int type;
    private MessageCenterListAdapter adapter;
    private int page = 1;

    private List<MessageCenterListBean> beans = new ArrayList<>();

    @Override
    public int getContentView() {
        return R.layout.layout_recycler_view;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        type = getIntent().getIntExtra("MESSAGETYPE", 0);

        if (type == 1) {
            titleBar.setTitleText("通知消息");
        } else {
            titleBar.setTitleText("系统消息");
        }
        helper = new PreferencesHelper(this);
        adapter = new MessageCenterListAdapter(canContentView);
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

        adapter.setOnItemChildClickListener(this);
        canContentView.addOnItemTouchListener(new OnSwipeItemTouchListener(this));
    }

    @Override
    public void getData() {
        getMessageData(helper.getToken(), type, page);
    }

    private void getMessageData(String token, final int type, final int page) {
        HttpHelp.getInstance().create(RemoteApi.class).getMessageData(token, type, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<List<MessageCenterListBean>>>(this, null) {
                    @Override
                    public void onNext(BaseBean<List<MessageCenterListBean>> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            refresh.refreshComplete();
                            refresh.loadMoreComplete();
                            if (baseBean.data.size() > 0) {
                                if (baseBean.code == 0) {
                                    beans = baseBean.data;
                                    if (page == 1) {
                                        adapter.setData(beans);
                                    } else {
                                        if (beans.size() > 0 && beans != null) {
                                            adapter.addMoreData(beans);
                                        } else {
                                            ToastUtil.showToast(MessageCenterListActivity.this, "暂无更多");
                                        }
                                    }
                                }
                            }
                            if (adapter.getItemCount()==0){
                                rlEmpty.setVisibility(View.VISIBLE);
                                refresh.setVisibility(View.GONE);
                            }
                        } else if (baseBean.code == 4) {
                            ToolUtil.goToLogin(MessageCenterListActivity.this);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
    }

    private void deleteMessageData(String token, String id) {
        HttpHelp.getInstance().create(RemoteApi.class).deleteMessageItem(token, id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean>(this, null) {
                    @Override
                    public void onNext(BaseBean baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            ToastUtil.showToast(MessageCenterListActivity.this, baseBean.message);
                            getMessageData(helper.getToken(), type, page);
                        } else if (baseBean.code == 4) {
                            ToolUtil.goToLogin(MessageCenterListActivity.this);
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
            case R.id.rl_inform:
                Intent intent = new Intent(MessageCenterListActivity.this, MessageDetailsActivity.class);
                intent.putExtra("MESSAGEID", adapter.getItem(position).getMessage_id());
                startActivity(intent);
                break;
            case R.id.btn_delete:
                deleteMessageData(helper.getToken(), adapter.getItem(position).getMessage_id());
                break;
        }

    }

    @Override
    public void onLoadMore() {
        page++;
        getMessageData(helper.getToken(), type, page);
        canRefreshHeader.onComplete();
    }

    @Override
    public void onRefresh() {
        page = 1;
        getMessageData(helper.getToken(), type, page);

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getMessageData(helper.getToken(), type, page);
    }

}
