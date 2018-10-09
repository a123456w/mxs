package com.guo.qlzx.maxiansheng.fragment;

import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.canyinghao.canrefresh.CanRefreshLayout;
import com.canyinghao.canrefresh.classic.ClassicRefreshView;
import com.guo.qlzx.maxiansheng.R;
import com.guo.qlzx.maxiansheng.activity.EvaluateActivity;
import com.guo.qlzx.maxiansheng.activity.LogisticsActivity;
import com.guo.qlzx.maxiansheng.activity.MainActivity;
import com.guo.qlzx.maxiansheng.activity.OrderDetailActivity;
import com.guo.qlzx.maxiansheng.activity.SetPersonalDetailsActivity;
import com.guo.qlzx.maxiansheng.adapter.OrderAllAdapter;
import com.guo.qlzx.maxiansheng.bean.OrdersBean;
import com.guo.qlzx.maxiansheng.event.UpdataOrderEvent;
import com.guo.qlzx.maxiansheng.http.RemoteApi;
import com.guo.qlzx.maxiansheng.util.ToolUtil;
import com.guo.qlzx.maxiansheng.util.dialog.AddressDeleteDialog;
import com.qlzx.mylibrary.base.BaseFragment;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.base.OnItemChildClickListener;
import com.qlzx.mylibrary.base.OnRVItemClickListener;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.DensityUtil;
import com.qlzx.mylibrary.util.EventBusUtil;
import com.qlzx.mylibrary.util.LogUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.qlzx.mylibrary.widget.RecycleViewDivider;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/4/17 0017.
 * describe 待评价订单
 */

public class OrderWaitEvaluateFragment extends BaseFragment implements OnItemChildClickListener, OnRVItemClickListener,
        CanRefreshLayout.OnRefreshListener, CanRefreshLayout.OnLoadMoreListener{

    Unbinder unbinder;
    @BindView(R.id.can_content_view)
    RecyclerView canContentView;
    @BindView(R.id.can_refresh_header)
    ClassicRefreshView canRefreshHeader;
    @BindView(R.id.can_refresh_footer)
    ClassicRefreshView canRefreshFooter;
    @BindView(R.id.refresh)
    CanRefreshLayout refresh;
    @BindView(R.id.rl_empty)
    RelativeLayout rlEmpty;
    @BindView(R.id.tv_skip)
    TextView tvSkip;
    private PreferencesHelper helper;
    private OrderAllAdapter adapter;
    private List<OrdersBean> list;
    private int page = 1;
    private boolean isLazy = false;
    private AddressDeleteDialog deleteDialog;
    @Override
    public View getContentView(FrameLayout frameLayout) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_order, frameLayout, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void initView() {
        loadingLayout.setStatus(LoadingLayout.Success);
        EventBusUtil.register(this);
        helper = new PreferencesHelper(mContext);
        adapter = new OrderAllAdapter(canContentView);
        canContentView.setAdapter(adapter);
        canContentView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        canContentView.addItemDecoration(new RecycleViewDivider(mContext,R.drawable.bg_recycler_divider));
        adapter.setOnItemChildClickListener(this);
        adapter.setOnRVItemClickListener(this);

        refresh.setOnLoadMoreListener(this);
        refresh.setOnRefreshListener(this);
        refresh.setMaxFooterHeight(DensityUtil.dp2px(getActivity(), 150));
        refresh.setStyle(0, 0);
        canRefreshHeader.setPullStr("下拉刷新");
        canRefreshHeader.setReleaseStr("松开刷新");
        canRefreshHeader.setRefreshingStr("加载中");
        canRefreshHeader.setCompleteStr("");

        canRefreshFooter.setPullStr("上拉加载更多");
        canRefreshFooter.setReleaseStr("松开刷新");
        canRefreshFooter.setRefreshingStr("加载中");
        canRefreshFooter.setCompleteStr("");
        tvSkip.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG );
        tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(mContext, MainActivity.class);
                intent1.putExtra("go_home", 2);
                startActivity(intent1);
            }
        });
    }

    @Override
    public void getData() {
        isLazy = true;
        getOrderList(helper.getToken(), "4", page);
    }


    private void getOrderList(String token, String type, final int page) {
        HttpHelp.getInstance().create(RemoteApi.class).getMyOrder(token, type, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<List<OrdersBean>>>(mContext, null) {
                    @Override
                    public void onNext(BaseBean<List<OrdersBean>> baseBean) {
                        super.onNext(baseBean);
                        if(getActivity()==null||getActivity().isFinishing()){
                            return;
                        }
                        refresh.refreshComplete();
                        refresh.loadMoreComplete();
                        if (baseBean.code == 0) {
                            list = baseBean.data;
                            if (page == 1) {
                                if (list != null && list.size() > 0) {
                                    rlEmpty.setVisibility(View.GONE);
                                    adapter.setData(baseBean.data);
                                } else {
                                    adapter.clear();
                                    rlEmpty.setVisibility(View.VISIBLE);
                                }

                            } else {
                                if (list != null && list.size() > 0) {
                                    adapter.addMoreData(list);
                                } else {
                                    ToastUtil.showToast(getActivity(), "暂无更多");
                                }
                            }

                        } else if (baseBean.code==4){
                            ToolUtil.goToLogin(mContext);
                        }else {
                            ToastUtil.showToast(mContext, baseBean.message);
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
    public void onStop() {
        super.onStop();

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

    @Override
    public void onRVItemClick(ViewGroup parent, View itemView, int position) {
        Intent intent = new Intent(mContext, OrderDetailActivity.class);
        intent.putExtra("order_id",adapter.getItem(position).getOrder_id());
        startActivity(intent);
    }

    @Override
    public void onItemChildClick(ViewGroup parent, View childView, final int position) {
        TextView btn = (TextView) childView;
        switch (btn.getText().toString()){
            case "查看物流":
                Intent intent=new Intent(mContext, LogisticsActivity.class);
                intent.putExtra("ORDERSN",adapter.getItem(position).getOrder_sn());
                intent.putExtra("ORDERID",adapter.getItem(position).getOrder_id());
                startActivity(intent);
                break;
            case "确认收货":
                deleteDialog=new AddressDeleteDialog(mContext);
                deleteDialog.setGoToUseOnclickListener(new AddressDeleteDialog.onGoToUseOnclickListener() {
                    @Override
                    public void onUseClick() {
                        confirm(helper.getToken(), adapter.getItem(position).getOrder_id());
                    }
                });
                deleteDialog.show();
                deleteDialog.setTitle("确认收货？");
                break;
            case "删除订单":
                deleteDialog=new AddressDeleteDialog(mContext);
                deleteDialog.setGoToUseOnclickListener(new AddressDeleteDialog.onGoToUseOnclickListener() {
                    @Override
                    public void onUseClick() {
                        delOrder(adapter.getItem(position).getOrder_id());
                    }
                });
                deleteDialog.show();
                deleteDialog.setTitle("确认删除订单？");
                break;
            case "立即评价":
                goEvaluate(adapter.getItem(position));
                break;
            case "联系卖家":
                if ("".equals(adapter.getItem(position).getPhone())){
                    ToastUtil.showToast(mContext,"暂无联系方式");
                    return;
                }
                call(adapter.getItem(position).getPhone());
                break;
            case "立即付款"://当前状态没有这个按钮
                ToastUtil.showToast(mContext,"立即付款");
                break;
            case "取消订单":
                deleteDialog=new AddressDeleteDialog(mContext);
                deleteDialog.setGoToUseOnclickListener(new AddressDeleteDialog.onGoToUseOnclickListener() {
                    @Override
                    public void onUseClick() {
                        cancelOrder(adapter.getItem(position).getOrder_id());
                    }
                });
                deleteDialog.show();
                deleteDialog.setTitle("确认取消订单？");
                break;
            case "提醒发货"://这个页面不会触发这个
                break;
            case "退货"://这个页面不会触发这个
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        page=1;
        unbinder.unbind();
    }

    /**
     * 取消订单
     * @param order_id
     */
    private void cancelOrder(String order_id){
        HttpHelp.getInstance().create(RemoteApi.class).cancelOrder(helper.getToken(),order_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<Object>>(mContext, null) {
                    @Override
                    public void onNext(BaseBean<Object> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            ToastUtil.showToast(mContext, baseBean.message);
                            refresh.autoRefresh();
                        } else if (baseBean.code==4){
                            ToolUtil.goToLogin(mContext);
                        }else {
                            ToastUtil.showToast(mContext, baseBean.message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);

                    }
                });
    }

    /**
     * 删除订单
     * @param order_id
     */
    private void delOrder(String order_id){
        HttpHelp.getInstance().create(RemoteApi.class).delOrder(helper.getToken(),order_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<Object>>(mContext, null) {
                    @Override
                    public void onNext(BaseBean<Object> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            ToastUtil.showToast(mContext, baseBean.message);
                            refresh.autoRefresh();
                        } else if (baseBean.code==4){
                            ToolUtil.goToLogin(mContext);
                        }else {
                            ToastUtil.showToast(mContext, baseBean.message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);

                    }
                });
    }

    /**
     * 联系卖家
     * @param phoneNum
     */
    private void call(String phoneNum){
        ToolUtil.goToCall(mContext,phoneNum);
    }
    /**
     * 去评价
     */
    private void goEvaluate(OrdersBean bean){
        Intent intent = new Intent(mContext,EvaluateActivity.class);
        intent.putExtra("info",bean);
        startActivity(intent);
    }

    /**
     * 确认订单
     * @param token
     * @param order_id
     */
    private void confirm(String token,String order_id){
        HttpHelp.getInstance().create(RemoteApi.class).confirmOrder(token,order_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<Object>>(mContext, null) {
                    @Override
                    public void onNext(BaseBean<Object> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            ToastUtil.showToast(mContext, baseBean.message);
                            refresh.autoRefresh();
                        } else if (baseBean.code==4){
                            ToolUtil.loseToLogin(mContext);
                        } else {
                            ToastUtil.showToast(mContext, baseBean.message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);

                    }
                });
    }



    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isLazy){
            LogUtil.i("userVisible_OrderWaitEvaluateFragment: "+isVisibleToUser);
            adapter.clear();
            refresh.autoRefresh();
        }
    }

    /**
     * 评论后更新数据
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateOrder(UpdataOrderEvent event){
        getData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBusUtil.unregister(this);
    }
}
