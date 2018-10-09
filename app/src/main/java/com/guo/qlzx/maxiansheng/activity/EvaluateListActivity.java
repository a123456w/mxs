package com.guo.qlzx.maxiansheng.activity;

import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.canyinghao.canrefresh.CanRefreshLayout;
import com.canyinghao.canrefresh.classic.ClassicRefreshView;
import com.guo.qlzx.maxiansheng.R;
import com.guo.qlzx.maxiansheng.adapter.EvaluateListAdapter;
import com.guo.qlzx.maxiansheng.bean.EvaluateListBean;
import com.guo.qlzx.maxiansheng.http.RemoteApi;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.DensityUtil;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/4/19 0019.
 * describle 评价列表
 */

public class EvaluateListActivity extends BaseActivity implements CanRefreshLayout.OnRefreshListener, CanRefreshLayout.OnLoadMoreListener{
    @BindView(R.id.can_content_view)
    ListView canContentView;
    @BindView(R.id.can_refresh_header)
    ClassicRefreshView canRefreshHeader;
    @BindView(R.id.can_refresh_footer)
    ClassicRefreshView canRefreshFooter;
    @BindView(R.id.refresh)
    CanRefreshLayout refresh;
    @BindView(R.id.rl_empty)
    RelativeLayout rlEmpty;

    private View headView;
    private TextView tvNumber;
    private String goods_id;
    private int page =1;

    private EvaluateListAdapter adapter;
    private List<EvaluateListBean> evaluateList = new ArrayList<>();

    @Override
    public int getContentView() {
        return R.layout.activity_list_evaluate;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("订单评价");
        titleBar.setLeftImageRes(R.drawable.ic_back_gray);

        goods_id = getIntent().getStringExtra("goods_id");

        headView = getLayoutInflater().inflate(R.layout.headview_evaluate_list, null);
        canContentView.addHeaderView(headView);
        tvNumber = headView.findViewById(R.id.tv_number);
        int number=getIntent().getIntExtra("evaluateNum",0);
        tvNumber.setText(""+number+"");
        adapter = new EvaluateListAdapter(canContentView);
        canContentView.setAdapter(adapter);

        refresh.setOnLoadMoreListener(this);
        refresh.setOnRefreshListener(this);
        refresh.setMaxFooterHeight(DensityUtil.dp2px(EvaluateListActivity.this, 150));
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
        getCommentList(goods_id,page);
    }

    private void getCommentList(String goodsId, final int page){
        HttpHelp.getInstance().create(RemoteApi.class).getCommentList(goodsId,page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<List<EvaluateListBean>>>(EvaluateListActivity.this, null) {
                    @Override
                    public void onNext(BaseBean<List<EvaluateListBean>> baseBean) {
                        super.onNext(baseBean);
                        refresh.refreshComplete();
                        refresh.loadMoreComplete();
                        evaluateList.clear();
                        if (baseBean.code == 0) {
                            evaluateList = baseBean.data;
                            if (page == 1){
                                if (evaluateList !=null && evaluateList.size()>0){
                                    adapter.setData(evaluateList);
                                }else {
                                    adapter.clear();
                                }
                            }else {
                                if (evaluateList !=null && evaluateList.size()>0){
                                    adapter.addMoreData(evaluateList);
                                }else {
                                    ToastUtil.showToast(EvaluateListActivity.this, "没有更多！");
                                }
                            }
                        } else {
                            ToastUtil.showToast(EvaluateListActivity.this, baseBean.message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        refresh.refreshComplete();
                        refresh.loadMoreComplete();
                    }
                });
    }


    @Override
    public void onLoadMore() {
        page++;
        getData();
    }

    @Override
    public void onRefresh() {
        page = 1;
        getData();
    }
}
