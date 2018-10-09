package com.guo.qlzx.maxiansheng.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.canyinghao.canrefresh.CanRefreshLayout;
import com.canyinghao.canrefresh.classic.ClassicRefreshView;
import com.guo.qlzx.maxiansheng.R;
import com.guo.qlzx.maxiansheng.adapter.ActivityListAdapter;
import com.guo.qlzx.maxiansheng.bean.ActivityBean;
import com.guo.qlzx.maxiansheng.bean.ActivityListBean;
import com.guo.qlzx.maxiansheng.bean.DistriTimeBean;
import com.guo.qlzx.maxiansheng.http.RemoteApi;
import com.guo.qlzx.maxiansheng.util.ToolUtil;
import com.guo.qlzx.maxiansheng.util.dialog.DistriTimeDialog;
import com.guo.qlzx.maxiansheng.util.dialog.SaleDistriTimeDialog;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.base.OnItemChildClickListener;
import com.qlzx.mylibrary.base.OnRVItemClickListener;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.DensityUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.qlzx.mylibrary.widget.RecycleViewDivider;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.iwgang.countdownview.CountdownView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.qlzx.mylibrary.util.DateUtil.FORMAT_HM;

/**
 * Created by 李 on 2018/4/16.
 * 秒杀专场/团购专场/预售专场
 */

public class KillSessionActivity extends BaseActivity implements CanRefreshLayout.OnLoadMoreListener, CanRefreshLayout.OnRefreshListener, OnItemChildClickListener, OnRVItemClickListener {
    @BindView(R.id.can_content_view)
    RecyclerView canContentView;
    @BindView(R.id.can_refresh_header)
    ClassicRefreshView canRefreshHeader;
    @BindView(R.id.can_refresh_footer)
    ClassicRefreshView canRefreshFooter;
    @BindView(R.id.refresh)
    CanRefreshLayout refresh;
    @BindView(R.id.cdv_time)
    CountdownView cdvTime;
    @BindView(R.id.rl_empty)
    LinearLayout rlEmpty;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.shop_number)
    TextView shopNumber;
    @BindView(R.id.ll_head)
    LinearLayout llHead;

    private int typeId = 1;
    private int page = 1;
    private ActivityListAdapter adapter;
    private ActivityBean bean;
    private String endTime="";

    private List<ActivityListBean> listBeans = new ArrayList<>();


    @Override
    public int getContentView() {
        return R.layout.activity_kill_session;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        hideTitleBar();

        typeId = getIntent().getIntExtra("TYPEID", 1);
        if (typeId == 1) {
            tvTitle.setText("秒杀专场");
            tvName.setText("秒杀中，先下单先得哦！");
        } else if (typeId == 2) {
            tvTitle.setText("团购专场");
            tvName.setText("团购中，先下单先得哦！");
        } else if (typeId == 3) {
            tvTitle.setText("预售专场");
            tvName.setText("预售中，先下单先得哦！");
        }


        adapter = new ActivityListAdapter(canContentView);
        canContentView.setLayoutManager(new LinearLayoutManager(this));
        canContentView.addItemDecoration(new RecycleViewDivider(this));
        canContentView.setAdapter(adapter);
        adapter.setType(typeId);

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
        adapter.setOnRVItemClickListener(this);
    }

    @Override
    public void getData() {
        getActivityData(typeId + "", page);
        shoppingCartCount(new PreferencesHelper(this).getToken());
    }

    private void getActivityData(String id, int mPage) {
        HttpHelp.getInstance().create(RemoteApi.class).getActivityData(id, mPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<ActivityBean>>(this, null) {
                    @Override
                    public void onNext(BaseBean<ActivityBean> baseBean) {
                        super.onNext(baseBean);
                        refresh.refreshComplete();
                        refresh.loadMoreComplete();
                        if (baseBean.code == 0) {
                            bean = baseBean.data;
                            listBeans = baseBean.data.getGoods_list();
                            long time=bean.getEnd_time() - (getPresentTime()/1000) ;
                            Log.i("TAG",time+"");
                            cdvTime.start(time*1000);
                            if (listBeans==null||listBeans.size()==0){
                        //   llHead.setVisibility(View.GONE);
                            }
                            if (page == 1) {
                                if (listBeans != null&&listBeans.size() > 0) {
                                    adapter.setData(listBeans);
                                    refresh.setVisibility(View.VISIBLE);
                                    rlEmpty.setVisibility(View.GONE);
                                } else {
                                    adapter.clear();
                                    refresh.setVisibility(View.GONE);
                                    rlEmpty.setVisibility(View.VISIBLE);
                                }

                            } else {
                                if (listBeans != null&&listBeans.size() > 0) {
                                    adapter.addMoreData(listBeans);
                                } else {
                                    ToastUtil.showToast(KillSessionActivity.this, "暂无更多");
                                }
                            }
                        } else {
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
        getActivityData(typeId + "", page);
    }

    @Override
    public void onRefresh() {
        page = 1;
        getActivityData(typeId + "", page);
    }

    @Override
    public void onItemChildClick(ViewGroup parent, View childView, int position) {

        switch (childView.getId()) {
            case R.id.tv_btn:
                //立即抢购
                    Intent intent = new Intent(KillSessionActivity.this, ProductDetailsActivity.class);
                    intent.putExtra("goods_id", "" + adapter.getItem(position).getGoods_id());
                    startActivity(intent);

                break;
        }
    }
    //购物车数量
    private void shoppingCartCount(String token){
        HttpHelp.getInstance().create(RemoteApi.class).shoppingCartCount(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<String>>(KillSessionActivity.this, null) {
                    @Override
                    public void onNext(BaseBean<String> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            if (baseBean.data!=null&& !TextUtils.isEmpty(baseBean.data)){
                                shopNumber.setVisibility(View.VISIBLE);
                                if (Integer.valueOf(baseBean.data)<100&&Integer.valueOf(baseBean.data)>0){
                                    shopNumber.setText(baseBean.data);
                                }else if (Integer.valueOf(baseBean.data)==0){
                                  shopNumber.setVisibility(View.GONE);
                                }else {
                                    shopNumber.setText("99+");
                                }
                            }
                        } else {
                            ToastUtil.showToast(KillSessionActivity.this, baseBean.message);
                           // shopNumber.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    shopNumber.setVisibility(View.GONE);
                    }
                });
    }

    @OnClick({R.id.iv_back, R.id.iv_cart})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_cart:
                //右上角 购物车监听
                if (TextUtils.isEmpty(new PreferencesHelper(KillSessionActivity.this).getToken())){
                    ToolUtil.goToLogin(KillSessionActivity.this);
                    return;
                }
                Intent intent1 = new Intent(KillSessionActivity.this, MainActivity.class);
                intent1.putExtra("go_cart", 1);
                startActivity(intent1);
                break;
        }
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        shoppingCartCount(new  PreferencesHelper(KillSessionActivity.this).getToken());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cdvTime.stop();
    }

    @Override
    public void onRVItemClick(ViewGroup parent, View itemView, int position) {
        if(listBeans==null||position>=listBeans.size()){
            return;
        }
        Intent intent = new Intent(KillSessionActivity.this, ProductDetailsActivity.class);
        intent.putExtra("goods_id",String.valueOf(listBeans.get(position).getGoods_id()));
        startActivity(intent);
    }

    private   long getPresentTime(){
        SimpleDateFormat formatter   =   new   SimpleDateFormat   ("yyyy年MM月dd日   HH:mm:ss");
        Date curDate =  new Date(System.currentTimeMillis());
        String   str   =   formatter.format(curDate);
        long time=ToolUtil.getTimeStamp(str,"yyyy年MM月dd日   HH:mm:ss");
        return time;
    }
}
