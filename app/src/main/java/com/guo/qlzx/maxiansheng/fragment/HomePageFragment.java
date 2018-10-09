package com.guo.qlzx.maxiansheng.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.canyinghao.canrefresh.CanRefreshLayout;
import com.canyinghao.canrefresh.classic.ClassicRefreshView;
import com.guo.qlzx.maxiansheng.R;
import com.guo.qlzx.maxiansheng.activity.AddressManagementActivity;
import com.guo.qlzx.maxiansheng.activity.ClassificationTwoActivity;
import com.guo.qlzx.maxiansheng.activity.CouponCenterActivity;
import com.guo.qlzx.maxiansheng.activity.HeadlineNewsDetailsActivity;
import com.guo.qlzx.maxiansheng.activity.HeadlineNewsListActivity;
import com.guo.qlzx.maxiansheng.activity.InviteActivity;
import com.guo.qlzx.maxiansheng.activity.KillSessionActivity;
import com.guo.qlzx.maxiansheng.activity.MessageCenterActivity;
import com.guo.qlzx.maxiansheng.activity.NewbieActivity;
import com.guo.qlzx.maxiansheng.activity.ProductDetailsActivity;
import com.guo.qlzx.maxiansheng.activity.SearchActivity;
import com.guo.qlzx.maxiansheng.activity.WebActivity;
import com.guo.qlzx.maxiansheng.adapter.HomeActionSecondsKillAdapter;
import com.guo.qlzx.maxiansheng.adapter.HomeActivityAdapter;
import com.guo.qlzx.maxiansheng.adapter.HomeClassifyAdapter;
import com.guo.qlzx.maxiansheng.adapter.HomeLableAdapter;
import com.guo.qlzx.maxiansheng.adapter.HomeRecommendAdapter;
import com.guo.qlzx.maxiansheng.bean.HomeActivityBean;
import com.guo.qlzx.maxiansheng.bean.HomeActivityListBean;
import com.guo.qlzx.maxiansheng.bean.HomeClassifyBean;
import com.guo.qlzx.maxiansheng.bean.HomeRecommendListBean;
import com.guo.qlzx.maxiansheng.bean.HomeTopBean;
import com.guo.qlzx.maxiansheng.bean.MessageBean;
import com.guo.qlzx.maxiansheng.bean.SpecDialogBean;
import com.guo.qlzx.maxiansheng.event.HomeNumEvent;
import com.guo.qlzx.maxiansheng.event.ShoppingCartEvent;
import com.guo.qlzx.maxiansheng.event.ShoppingCartNumEvent;
import com.guo.qlzx.maxiansheng.http.RemoteApi;
import com.guo.qlzx.maxiansheng.util.ToolUtil;
import com.guo.qlzx.maxiansheng.util.callback.OnActivityLoadClickListener;
import com.guo.qlzx.maxiansheng.util.callback.OnLoadClickListener;
import com.guo.qlzx.maxiansheng.util.dialog.HomeCouponDialog;
import com.guo.qlzx.maxiansheng.util.dialog.SelectDialog;
import com.qlzx.mylibrary.base.BaseFragment;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.base.OnItemChildClickListener;
import com.qlzx.mylibrary.base.OnRVItemClickListener;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.DensityUtil;
import com.qlzx.mylibrary.util.EventBusUtil;
import com.qlzx.mylibrary.util.GlideUtil;
import com.qlzx.mylibrary.util.LogUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.qlzx.mylibrary.widget.NoScrollGridView;
import com.qlzx.mylibrary.widget.NoScrollListView;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 首页
 * Created by dillon on 2018/4/10.
 */

public class HomePageFragment extends BaseFragment
        implements View.OnClickListener,CanRefreshLayout.OnRefreshListener, CanRefreshLayout.OnLoadMoreListener, OnItemChildClickListener, AdapterView.OnItemClickListener, HomeRecommendAdapter.OnAddGoodsClickListener, OnLoadClickListener, OnActivityLoadClickListener, OnRVItemClickListener {

    @BindView(R.id.home_banner)
    Banner homeBanner;
    @BindView(R.id.tv_number)
    TextView tvNumber;
    @BindView(R.id.nsgv_home_label)
    NoScrollGridView nsgvHomeLabel;
    @BindView(R.id.iv_coupon)
    ImageView ivCoupon;
    @BindView(R.id.vf_recommended)
    ViewFlipper vfRecommended;
    @BindView(R.id.rv_seconds_kill)
    RecyclerView rvSecondsKill;
    @BindView(R.id.ll_kill)
    LinearLayout llKill;
    @BindView(R.id.ns_activity)
    NoScrollListView nsActivity;
    @BindView(R.id.ns_list_recommend)
    NoScrollListView nsListRecommend;
    @BindView(R.id.can_content_view)
    NestedScrollView scrollView;
    @BindView(R.id.can_refresh_footer)
    ClassicRefreshView canRefreshFooter;
    @BindView(R.id.refresh)
    CanRefreshLayout refresh;
    @BindView(R.id.iv_location)
    ImageView ivLocation;
    @BindView(R.id.ll_search)
    LinearLayout llSearch;
    @BindView(R.id.iv_message)
    ImageView ivMessage;
    @BindView(R.id.rl_top_search)
    LinearLayout rlTopSearch;
    Unbinder unbinder;
    @BindView(R.id.ns_classify)
    NoScrollListView nsClassify;
    @BindView(R.id.ib_top)
    ImageButton ibTop;
    @BindView(R.id.can_refresh_header)
    ClassicRefreshView canRefreshHeader;
    Unbinder unbinder1;
    //private HomeTopBean topBean;
    private HomeLableAdapter homeLableAdapter;
    private List<View> views = new ArrayList<>();

    //活动
    private HomeActivityAdapter activityAdapter;
    private List<HomeActivityBean> activityBeans = new ArrayList<>();
    private HomeActionSecondsKillAdapter secondsKillAdapter;
    //分类
    private HomeClassifyAdapter classifyAdapter;
    private List<HomeClassifyBean> classifyBeans = new ArrayList<>();
    //热销
    private HomeRecommendAdapter recommendAdapter;
    private List<HomeRecommendListBean> recommendListBeans = new ArrayList<>();

    private HomeCouponDialog couponDialog;

    SelectDialog cartDialog;//加购物车dialog

    private PreferencesHelper helper;
    private int page = 1;

    private int vip = 0;//0新人 1普通会员 2plus会员
    private int labelSize = 0;
    List<HomeTopBean.NavigationBean.ClassifyBean> label = new ArrayList<>();
    List<HomeTopBean.NavigationBean.ClassifyBean> classifyBean = new ArrayList<>();

    @Override
    public View getContentView(FrameLayout frameLayout) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_homepage, frameLayout, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void initView() {
        loadingLayout.setStatus(LoadingLayout.Success);
        WindowManager wm = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
        final int height = wm.getDefaultDisplay().getHeight();
        final int imageHeight = DensityUtil.dp2px(mContext, 200 - 45 - 40);
        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY <= 0) {
                    rlTopSearch.setBackgroundColor(Color.argb((int) 0, 50, 157, 253));//AGB由相关工具获得，或者美工提供
                } else if (scrollY > 0 && scrollY <= imageHeight) {
                    float scale = (float) scrollY / imageHeight;
                    float alpha = (255 * scale);
                    // 只是layout背景透明(仿知乎滑动效果)
                    rlTopSearch.setBackgroundColor(Color.argb((int) alpha, 255, 255, 255));
                    // tvSearch.setBackgroundColor(Color.argb((int) alpha, 144, 144, 144));
                } else {
                    rlTopSearch.setBackgroundColor(Color.argb((int) 255, 21, 164, 224));
                    // tvSearch.setBackgroundColor(Color.argb((int)255, 144, 144, 144));
                }
                if (scrollY > height) {
                    ibTop.setVisibility(View.VISIBLE);
                } else {
                    ibTop.setVisibility(View.GONE);
                }
            }
        });
        //首页标签
        homeLableAdapter = new HomeLableAdapter(nsgvHomeLabel);
        nsgvHomeLabel.setFocusable(false);
        nsgvHomeLabel.setAdapter(homeLableAdapter);


        /**  推荐商品 **/
        recommendAdapter = new HomeRecommendAdapter(nsListRecommend);
        nsListRecommend.setAdapter(recommendAdapter);
        nsListRecommend.setFocusable(false);
        nsListRecommend.setOnItemClickListener(this);
        recommendAdapter.setOnAddGoodsClickListener(this);

        //常见活动
        activityAdapter = new HomeActivityAdapter(nsActivity);
        nsActivity.setAdapter(activityAdapter);
        nsActivity.setFocusable(false);
        activityAdapter.setOnActivityLoadClickListener(this);
        //分类商品
        classifyAdapter = new HomeClassifyAdapter(nsClassify);
        nsClassify.setAdapter(classifyAdapter);
        nsClassify.setFocusable(false);
        classifyAdapter.setOnLoadClickListener(this);


        //秒杀
        secondsKillAdapter = new HomeActionSecondsKillAdapter(rvSecondsKill);
        rvSecondsKill.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        rvSecondsKill.setAdapter(secondsKillAdapter);
        secondsKillAdapter.setOnRVItemClickListener(this);

        //刷新加载
        refresh.setOnLoadMoreListener(this);
        refresh.setOnRefreshListener(this);
        refresh.setMaxFooterHeight(DensityUtil.dp2px(mContext, 150));
        refresh.setStyle(0, 0);


        canRefreshFooter.setPullStr("上拉加载更多");
        canRefreshFooter.setReleaseStr("松开刷新");
        canRefreshFooter.setRefreshingStr("加载中");
        canRefreshFooter.setCompleteStr("");

        canRefreshHeader.setPullStr("下拉刷新");
        canRefreshHeader.setReleaseStr("松开刷新");
        canRefreshHeader.setRefreshingStr("加载中");
        canRefreshHeader.setCompleteStr("");

        //存储类
        helper = new PreferencesHelper(mContext);
        //5个菜单点
        nsgvHomeLabel.setOnItemClickListener(onItemClickListener);

        setHomeLabel();

        EventBusUtil.register(this);

    }

    private void setHomeLabel() {

        label.add(new HomeTopBean.NavigationBean.ClassifyBean(-1, "秒杀专场", "" + R.drawable.ic_kitchen));
        label.add(new HomeTopBean.NavigationBean.ClassifyBean(-2, "团购专场", "" + R.drawable.ic_holiday));
        label.add(new HomeTopBean.NavigationBean.ClassifyBean(-3, "预售专场", "" + R.drawable.ic_drinks));
        label.add(new HomeTopBean.NavigationBean.ClassifyBean(-4, "邀请好友", "" + R.drawable.ic_friend));
        label.add(new HomeTopBean.NavigationBean.ClassifyBean(-5, "新闻中心", "" + R.drawable.ic_news));
    }

    @Override
    public void getData() {


        // 获取推荐数据
        getRecommendData();
        //获取头部数据
        getHeadData(helper.getToken());
        //获取活动数据
        getActivityData();
        //获取分类数据
        getClassifyData();
        //获取消息总数
        getMessageNumber(helper.getToken());
    }
    //获取消息未读个数

    public void getMessageNumber(String token) {
        HttpHelp.getInstance().create(RemoteApi.class).getMessageCount(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<MessageBean>>(mContext, null) {
                    @Override
                    public void onNext(BaseBean<MessageBean> messageBeanBaseBean) {
                        super.onNext(messageBeanBaseBean);
                        if (messageBeanBaseBean.code == 0) {
                            MessageBean messageBean = messageBeanBaseBean.data;
                            String messageNum = messageBean.getCount();
                            if (!TextUtils.isEmpty(messageNum) && !"0".equals(messageNum)) {
                                tvNumber.setVisibility(View.VISIBLE);
                                tvNumber.setText(messageNum);
                            } else {
                                tvNumber.setVisibility(View.GONE);
                            }
                        } else {
                            tvNumber.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        tvNumber.setVisibility(View.GONE);

                    }
                });

    }

    //设置头条新闻
    public void setViewFlipper(final List<HomeTopBean.NewsBean> newsBeans) {
        for (int i = 0; i < newsBeans.size(); i++) {
            //设置滚动的单个布局
            //初始化布局的控件
            LinearLayout moreView = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.item_home_top_news, null);
            ImageView ivPic = (ImageView) moreView.findViewById(R.id.iv_img);
            TextView tvTitle = (TextView) moreView.findViewById(R.id.tv_content);
            tvTitle.setText(newsBeans.get(i).getTitle());
            GlideUtil.display(getContext(), Constants.IMG_HOST + newsBeans.get(i).getImg(), ivPic);
            //添加到循环滚动数组里面去
            views.add(moreView);
        }
        for (int i = 0; i < views.size(); i++) {
            vfRecommended.addView(views.get(i));
        }
        vfRecommended.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent intent = new Intent(mContext, HeadlineNewsListActivity.class);
                startActivity(intent);*/
                //需要对应新闻内容用下面
                int i = vfRecommended.getDisplayedChild();
                HomeTopBean.NewsBean headBean = newsBeans.get(i);
                int id = headBean.getId();
                HeadlineNewsDetailsActivity.startActivity(mContext,
                        headBean.getTitle(), "", String.valueOf(id));
            }
        });
    }

    //轮播图放置
    public void setBannerData(List<String> bannerData, final List<String> bannerList) {
        homeBanner.setImages(bannerData)
                .setImageLoader(new ImageLoader() {
                    @Override
                    public void displayImage(Context context, Object path, ImageView imageView) {
                        String imgUrl = (String) path;
                        GlideUtil.display(mContext, Constants.IMG_HOST + imgUrl, imageView);
                    }
                }).setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                //轮播图跳转，暂时不用，如果是大图的话，检查图片地址是否正确
                startActivity(new Intent(getActivity(), WebActivity.class).putExtra("title", "详情")
                        .putExtra("url", bannerList.get(position)));
            }
        }).start();
    }


    /**
     * 所有网络获取
     * 1.获取头部数据
     */
    public void getHeadData(String token) {
        HttpHelp.getInstance().create(RemoteApi.class).getHomeTopData(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<HomeTopBean>>(mContext, null) {
                    @Override
                    public void onNext(BaseBean<HomeTopBean> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            HomeTopBean topBean = baseBean.data;
                            Log.d("TAG", "" + topBean);
                            if (topBean != null) {
                                if (topBean.getBanner().size() > 0) {
                                    List<String> imageUrl = new ArrayList<>();
                                    List<String> url = new ArrayList<>();
                                    for (HomeTopBean.BannerBean bean : topBean.getBanner()) {
                                        imageUrl.add(bean.getAd_code());
                                        url.add(bean.getAd_link());
                                    }
                                    setBannerData(imageUrl, url);
                                }
                                classifyBean = topBean.getNavigation().getClassify();
                                labelSize = classifyBean.size();
                                homeLableAdapter.setPos(labelSize);
                                classifyBean.addAll(label);
                                homeLableAdapter.setData(classifyBean);

                                if (topBean.getCoupon() != null) {
                                    int type = topBean.getCoupon().getType();
                                    vip = type;
                                    //传递本帐号是否为新人/普通会员/plus会员
                                    GlideUtil.display(mContext, Constants.IMG_HOST + topBean.getCoupon().getAd_code(), ivCoupon);
//                                    if (topBean.getNew_coupon().size() > 0) {
//                                        getCouponData(type, topBean.getNew_coupon());
//                                    }
                                }
                                if (topBean.getNews().size() > 0) {
                                    setViewFlipper(topBean.getNews());
                                }
                            }
                        } else {

                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        LogUtil.i(throwable.getMessage());
                    }
                });
    }

    /**
     * 所有网络获取
     * 1.获取头部数据
     */
    public void getAgainHeadData(String token) {
        HttpHelp.getInstance().create(RemoteApi.class).getHomeTopData(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<HomeTopBean>>(mContext, null) {
                    @Override
                    public void onNext(BaseBean<HomeTopBean> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            HomeTopBean topBean = baseBean.data;
                            Log.d("TAG", "" + topBean);
                            if (topBean != null) {
                                if (topBean.getBanner().size() > 0) {
                                    List<String> imageUrl = new ArrayList<>();
                                    List<String> url = new ArrayList<>();
                                    for (HomeTopBean.BannerBean bean : topBean.getBanner()) {
                                        imageUrl.add(bean.getAd_code());
                                        url.add(bean.getAd_link());
                                    }
                                    setBannerData(imageUrl, url);
                                }
                                classifyBean = topBean.getNavigation().getClassify();
                                labelSize = classifyBean.size();
                                homeLableAdapter.setPos(labelSize);
                                classifyBean.addAll(label);
                                homeLableAdapter.setData(classifyBean);

                                if (topBean.getCoupon() != null) {
                                    int type = topBean.getCoupon().getType();
                                    vip = type;
                                    GlideUtil.display(mContext, Constants.IMG_HOST + topBean.getCoupon().getAd_code(), ivCoupon);
                                    //传递本帐号是否为新人/普通会员/plus会员
                                }
                                if (topBean.getNews().size() > 0) {
                                    setViewFlipper(topBean.getNews());
                                }
                            }
                        } else {

                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        LogUtil.i(throwable.getMessage());
                    }
                });
    }

    /**
     * 2.获取活动数据
     */
    public void getActivityData() {
        HttpHelp.getInstance().create(RemoteApi.class).getHomeActivityData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<List<HomeActivityBean>>>(mContext, null) {
                    @Override
                    public void onNext(BaseBean<List<HomeActivityBean>> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {

                            activityBeans = baseBean.data;

                            List<HomeActivityListBean> listBeans = new ArrayList<>();
                            for (int i = 0; i < activityBeans.size(); i++) {
                                if (activityBeans.get(i).getType() == 1) {
                                    listBeans.addAll(activityBeans.get(i).getList());
                                    activityBeans.remove(i);
                                }
                            }
                            if (listBeans.size() > 0) {
                                llKill.setVisibility(View.VISIBLE);
                            }
                            secondsKillAdapter.setData(listBeans);

                            activityAdapter.setData(activityBeans);
                        } else {

                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);

                    }
                });
    }

    /**
     * 3.获取分类数据
     */
    public void getClassifyData() {
        HttpHelp.getInstance().create(RemoteApi.class).getHomeClassifyData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<List<HomeClassifyBean>>>(mContext, null) {
                    @Override
                    public void onNext(BaseBean<List<HomeClassifyBean>> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            classifyBeans = baseBean.data;
                            classifyAdapter.setType(vip);
                            classifyAdapter.setData(classifyBeans);
                        } else {
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);

                    }
                });
    }

    /**
     * 4.获取热销数据
     */
    public void getRecommendData() {
        HttpHelp.getInstance().create(RemoteApi.class).getHomeRecommendData(page, 0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<List<HomeRecommendListBean>>>(mContext, null) {
                    @Override
                    public void onNext(BaseBean<List<HomeRecommendListBean>> baseBean) {
                        super.onNext(baseBean);
                        refresh.loadMoreComplete();
                        refresh.refreshComplete();
                        if (baseBean.code == 0) {
                            recommendListBeans = baseBean.data;
                            if (page == 1) {
                                recommendAdapter.setData(recommendListBeans);
                            } else {
                                if (recommendListBeans.size() > 0 && recommendListBeans != null) {
                                    recommendAdapter.addMoreData(recommendListBeans);
                                } else {
                                    ToastUtil.showToast(mContext, "暂无更多");
                                }
                            }
                        } else {

                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        refresh.loadMoreComplete();
                    }
                });
    }

    /**
     * 5.获取弹出优惠券优惠券数据
     * 需要传递哪一种会员类型
     * -------已取消该方法
     */
    public void getCouponData(int type, List<HomeTopBean.NewCouponBean> list) {
        couponDialog = new HomeCouponDialog(mContext, list);
        couponDialog.setCancelOnclickListener(new HomeCouponDialog.onCancelOnclickListener() {
            @Override
            public void onCancelClick() {
                //右上角取消按钮
                couponDialog.cancel();
            }
        });
        couponDialog.setGoToUseOnclickListener(new HomeCouponDialog.onGoToUseOnclickListener() {
            @Override
            public void onUseClick() {
                //去使用 按钮
                couponDialog.cancel();
                Intent intent = new Intent(mContext, NewbieActivity.class);
                startActivity(intent);
            }
        });

        couponDialog.show();
    }

    /**
     * 所有的监听事件
     */
    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //导航菜单
            Intent intent;
            int beanId = classifyBean.get(position).getId();
            if (labelSize != 0) {
                if (position < labelSize) {
                    intent = new Intent(mContext, ClassificationTwoActivity.class);
                    intent.putExtra("name", classifyBean.get(position).getName());
                    intent.putExtra("id", "" + classifyBean.get(position).getId());
                    mContext.startActivity(intent);
                } else {

                    if (beanId == -1) {
                        //秒杀专场
                        intent = new Intent(mContext, KillSessionActivity.class);
                        intent.putExtra("TYPEID", 1);
                        startActivity(intent);
                    } else if (beanId == -2) {
                        //团购专场
                        intent = new Intent(mContext, KillSessionActivity.class);
                        intent.putExtra("TYPEID", 2);
                        startActivity(intent);
                    } else if (beanId == -3) {
                        //预售专场
                        intent = new Intent(mContext, KillSessionActivity.class);
                        intent.putExtra("TYPEID", 3);
                        startActivity(intent);
                    } else if (beanId == -4) {
                        //邀请好友
                        if (TextUtils.isEmpty(helper.getToken())) {
                            ToolUtil.loseToLogin(mContext);
                        } else {
                            intent = new Intent(mContext, InviteActivity.class);
                            startActivity(intent);
                        }
                    } else if (beanId == -5) {
                        //新闻中心
                        intent = new Intent(mContext, HeadlineNewsListActivity.class);
                        startActivity(intent);
                    }
                }
            } else {
                if (beanId == -1) {
                    //秒杀专场
                    intent = new Intent(mContext, KillSessionActivity.class);
                    intent.putExtra("TYPEID", 1);
                    startActivity(intent);
                } else if (beanId == -2) {
                    //团购专场
                    intent = new Intent(mContext, KillSessionActivity.class);
                    intent.putExtra("TYPEID", 2);
                    startActivity(intent);
                } else if (beanId == -3) {
                    //预售专场
                    intent = new Intent(mContext, KillSessionActivity.class);
                    intent.putExtra("TYPEID", 3);
                    startActivity(intent);
                } else if (beanId == -4) {
                    //邀请好友
                    if (TextUtils.isEmpty(helper.getToken())) {
                        ToolUtil.loseToLogin(mContext);
                    } else {
                        intent = new Intent(mContext, InviteActivity.class);
                        startActivity(intent);
                    }
                } else if (beanId == -5) {
                    //新闻中心
                    intent = new Intent(mContext, HeadlineNewsListActivity.class);
                    startActivity(intent);
                }
            }
        }
    };

    @OnClick({R.id.iv_location, R.id.ll_search, R.id.iv_message, R.id.iv_coupon, R.id.ll_kill, R.id.ib_top})
    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            //定位
            case R.id.iv_location:
                if (TextUtils.isEmpty(new PreferencesHelper(mContext).getToken())) {
                    ToolUtil.goToLogin(mContext);
                    return;
                }
                intent = new Intent(getActivity(), AddressManagementActivity.class);
                startActivity(intent);
                break;
            //搜索
            case R.id.ll_search:
                intent = new Intent(mContext, SearchActivity.class);
                startActivity(intent);
                break;
            //消息
            case R.id.iv_message:
                if (TextUtils.isEmpty(helper.getToken())) {
                    ToolUtil.loseToLogin(mContext);
                } else {
                    intent = new Intent(mContext, MessageCenterActivity.class);
                    startActivity(intent);
                }
                break;
            // 新人/会员 有礼图片
            case R.id.iv_coupon:
                intent = new Intent(mContext, CouponCenterActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_kill:
                intent = new Intent(mContext, KillSessionActivity.class);
                intent.putExtra("TYPEID", 1);
                startActivity(intent);
                break;
            case R.id.ib_top:
                scrollView.scrollTo(0, 0);
                break;
        /*    case R.id.tv_news:
                break;
            case R.id.tv_kitchen:
                break;
            case R.id.tv_holiday:
                break;
            case R.id.tv_friend:
                break;
            case R.id.tv_drinks:
                break;
                */

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBusUtil.unregister(this);
    }


    /**
     * 在本fragment隐藏时停止滚动 显示时开始滚动
     * 为避免视图复用
     *
     * @param hidden
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        Log.i("TAG", "onHiddenChanged" + hidden);
        if (hidden) {
            vfRecommended.stopFlipping();
        } else {
            vfRecommended.startFlipping();
        }
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        Log.i("TAG", "animation" + enter);
        //   进入当前Fragment
        if (enter) {
            //   这里可以做网络请求或者需要的数据刷新操作
            getAgainHeadData(helper.getToken());
            getMessageNumber(helper.getToken());
        } else {

        }
        return super.onCreateAnimation(transit, enter, nextAnim);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(HomeNumEvent event) {
        getMessageNumber(helper.getToken());
    }

    @Override
    public void onLoadMore() {
        page++;
        getRecommendData();
    }


    //点击加入购物车
    private void showAddDialog(HomeRecommendListBean bean) {
        if (bean.getSpec_type() == 0) {
            //不存在规格
            if (TextUtils.isEmpty(helper.getToken())) {
                ToolUtil.loseToLogin(mContext);
            } else {
                addShoppingCart(helper.getToken(), String.valueOf(bean.getGoods_id()), "", "", 1);
            }
        } else {
            if (TextUtils.isEmpty(helper.getToken())) {
                ToolUtil.loseToLogin(mContext);
            } else {
                //存在规格 选择规格
                cartDialog = new SelectDialog(mContext, bean.getType());
                SpecDialogBean specDialogBean = new SpecDialogBean();
                specDialogBean.setGoods_id(bean.getGoods_id());
                specDialogBean.setImg(bean.getImg());
                specDialogBean.setSpec_name(bean.getSpec_key_name());
                specDialogBean.setPlus_price(bean.getPlus_price());
                specDialogBean.setShop_price(bean.getShop_price());
                cartDialog.setGoToUseOnclickListener(new SelectDialog.onGoToUseOnclickListener() {
                    @Override
                    public void onUseClick(String goods_id, String spec_id, String service_id, int num, int s
                            , double price, double vipPrice) {
                        if (s == 0) {
                            ToastUtil.showToast(getActivity(), "当前商品库存为0份");
                            return;
                        }
                        addShoppingCart(helper.getToken(), goods_id, service_id, spec_id, num);
                    }
                });
                cartDialog.show();
                cartDialog.setData(specDialogBean);
            }
        }
    }


    /**
     * 加入购物车
     *
     * @param token
     * @param goods_id
     * @param service_id
     * @param spec_id
     * @param goods_num
     */
    private void addShoppingCart(String token, String goods_id, String service_id, String spec_id, int goods_num) {
        HttpHelp.getInstance().create(RemoteApi.class).addCart(token, goods_id, service_id, spec_id, goods_num)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<Object>>(mContext, null) {
                    @Override
                    public void onNext(BaseBean<Object> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            ToastUtil.showToast(mContext, baseBean.message);
                            EventBusUtil.post(new ShoppingCartNumEvent());
                            EventBusUtil.post(new ShoppingCartEvent());
                        } else if (baseBean.code == 4) {
                            ToolUtil.goToLogin(mContext);
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
    public void onItemChildClick(ViewGroup parent, View childView, int position) {
        switch (childView.getId()) {
            case R.id.iv_add:
                showAddDialog(recommendListBeans.get(position));
                break;
        }
    }

    //热销专区的加入购物车
    @Override
    public void onAdd(HomeRecommendListBean model) {
        showAddDialog(model);
    }

    //分类专区的 左拉加载
    @Override
    public void onLoadClick(int pos) {
        Intent intent = new Intent(mContext, ClassificationTwoActivity.class);
        intent.putExtra("name", classifyBeans.get(pos).getName());
        intent.putExtra("id", "" + classifyBeans.get(pos).getId());
        mContext.startActivity(intent);
    }

    //活动专区的 左拉加载
    @Override
    public void onActivityLoad(int pos) {
        Intent intent = new Intent(mContext, KillSessionActivity.class);
        intent.putExtra("TYPEID", activityBeans.get(pos).getType());
        mContext.startActivity(intent);
    }

    //秒杀的ite监听
    @Override
    public void onRVItemClick(ViewGroup parent, View itemView, int position) {
        Intent intent = new Intent(mContext, ProductDetailsActivity.class);
        intent.putExtra("goods_id", "" + secondsKillAdapter.getItem(position).getGoods_id());
        mContext.startActivity(intent);
    }

    //热销的item监听
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(mContext, ProductDetailsActivity.class);
        HomeRecommendListBean data = (HomeRecommendListBean) recommendAdapter.getItem(position);
        intent.putExtra("goods_id", String.valueOf(data.getGoods_id()));
        mContext.startActivity(intent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder1 = ButterKnife.bind(this, rootView);
        return rootView;
    }

    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh() {
        page=1;
        getData();
    }
}
