package com.guo.qlzx.maxiansheng.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.canyinghao.canrefresh.CanRefreshLayout;
import com.canyinghao.canrefresh.classic.ClassicRefreshView;
import com.guo.qlzx.maxiansheng.R;
import com.guo.qlzx.maxiansheng.adapter.LessEvaluateAdapter;
import com.guo.qlzx.maxiansheng.adapter.LikeAdapter;
import com.guo.qlzx.maxiansheng.adapter.ProductDetailsBannerAdapter;
import com.guo.qlzx.maxiansheng.adapter.SpecAdapter;
import com.guo.qlzx.maxiansheng.bean.CartListBean;
import com.guo.qlzx.maxiansheng.bean.GuessLikeBean;
import com.guo.qlzx.maxiansheng.bean.ProductDetailsBean;
import com.guo.qlzx.maxiansheng.bean.ServerBean;
import com.guo.qlzx.maxiansheng.bean.SpecDialogBean;
import com.guo.qlzx.maxiansheng.event.ShoppingCartEvent;
import com.guo.qlzx.maxiansheng.http.RemoteApi;
import com.guo.qlzx.maxiansheng.util.MeasureFlowLayoutManager;
import com.guo.qlzx.maxiansheng.util.SpaceItemDecoration;
import com.guo.qlzx.maxiansheng.util.ToolUtil;
import com.guo.qlzx.maxiansheng.util.costom.CircleMove;
import com.guo.qlzx.maxiansheng.util.costom.ClassifyCircleMoveView;
import com.guo.qlzx.maxiansheng.util.dialog.SelectDialog;
import com.guo.qlzx.maxiansheng.util.dialog.SharedDialog;
import com.guo.qlzx.maxiansheng.view.MyScrollView;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.base.OnRVItemClickListener;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.DensityUtil;
import com.qlzx.mylibrary.util.EventBusUtil;
import com.qlzx.mylibrary.util.GlideUtil;
import com.qlzx.mylibrary.util.LogUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.TimeUtil;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.qlzx.mylibrary.widget.NoScrollGridView;
import com.qlzx.mylibrary.widget.NoScrollListView;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPreviewActivity;
import cn.iwgang.countdownview.CountdownView;
import cn.jzvd.JZVideoPlayerStandard;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by chenlipeng on 2018/4/17 0017
 * describe:  商品详情
 */
public class ProductDetailsActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener, CanRefreshLayout.OnLoadMoreListener {
    private static final String TAG = "ProductDetailsActivity";
    //  点击加入购物车然后先分享，分享成功之后   返回马先生   然后只能加购一件
    @BindView(R.id.ll_addshopcar)
    LinearLayout llAddShopCar;
    @BindView(R.id.my_tablayout)
    TabLayout myTablayout;
    @BindView(R.id.banner_top)
    Banner bannerTop;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_remark)
    TextView tvRemark;
    @BindView(R.id.iv_pic)
    ImageView ivPic;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_old_price)
    TextView tvOldPrice;
    @BindView(R.id.tv_vip_price)
    TextView tvVipPrice;
    @BindView(R.id.ll_vio)
    LinearLayout llVio;
    @BindView(R.id.ll_second_kill)
    LinearLayout llSecondKill;
    @BindView(R.id.ll_tuan)
    LinearLayout llTuan;
    @BindView(R.id.tv_deliver)
    TextView tvDeliver;
    @BindView(R.id.rl_select)
    RelativeLayout rlSelect;
    //    @BindView(R.id.rl_server)
//    RelativeLayout rlServer;
    @BindView(R.id.tv_details)
    TextView tvDetails;
    @BindView(R.id.tv_region)
    TextView tvRegion;
    @BindView(R.id.tv_storage)
    TextView tvStorage;
    @BindView(R.id.tv_weight)
    TextView tvWeight;
    @BindView(R.id.tv_brand)
    TextView tvBrand;
    @BindView(R.id.web_details)
    WebView webDetails;
    @BindView(R.id.can_content_view)
    MyScrollView scroll;
    @BindView(R.id.btn_confim)
    Button btnConfim;
    @BindView(R.id.tv_number)
    TextView tvNumber;
    @BindView(R.id.nos_list)
    NoScrollListView nosList;
    @BindView(R.id.grid_like)
    NoScrollGridView gridLike;
    @BindView(R.id.tv_specf)
    TextView tvSpecf;
    @BindView(R.id.countdown)
    CountdownView countdown;
    @BindView(R.id.tv_tuan)
    TextView tvTuan;
    @BindView(R.id.tv_count_hint)
    TextView tvCountHint;
    @BindView(R.id.vp_details)
    ViewPager vpDetails;

    @BindView(R.id.rl_xiangqing)
    RelativeLayout rlXiangqing;
    @BindView(R.id.rl_pingjia)
    RelativeLayout rlPingjia;
    @BindView(R.id.ll_font)
    LinearLayout llFont;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_cart)
    ImageView ivCart;
    @BindView(R.id.tv_comment_hint)
    TextView tvCommentHint;
    @BindView(R.id.iv_comment_all)
    ImageView ivCommentAll;
    @BindView(R.id.my_titlebar)
    LinearLayout myTitlebar;
    @BindView(R.id.ll_warn)
    LinearLayout llWarn;
    @BindView(R.id.shop_number)
    TextView shopNumber;
    @BindView(R.id.rl_title)
    RelativeLayout tlTitle;

    @BindView(R.id.rl_empty)
    RelativeLayout rlEmpty;
    //    @BindView(R.id.tv_details)
//    TextView tvDetails;
    @BindView(R.id.cm_circle)
    CircleMove cmCircle;
    @BindView(R.id.shop_number_bottom)
    TextView bTvNumber;
    @BindView(R.id.ccm_circle)
    ClassifyCircleMoveView classifyCircleMoveView;
    @BindView(R.id.ll_shopcar)
    RelativeLayout llShopcar;
    @BindView(R.id.comments_gap)
    View commentsGap;
    @BindView(R.id.can_refresh_footer)
    ClassicRefreshView canRefreshFooter;
    @BindView(R.id.refresh)
    CanRefreshLayout refresh;
    @BindView(R.id.ll_call)
    LinearLayout llCall;
    @BindView(R.id.tv_shop)
    ImageView tvShop;
    @BindView(R.id.ll_buy)
    LinearLayout llBuy;
    @BindView(R.id.tv_tuijian)
    TextView tvTuijian;


    private PreferencesHelper helper;
    private String goods_id;
    private ProductDetailsBean details;

    private LessEvaluateAdapter adapter;
    private LikeAdapter likeAdapter;
    private List<GuessLikeBean> liksBean;

    private ServerBean serverBean;
    private ArrayList<ServerBean.ServicePriceBean> serverList = new ArrayList<>();//用于显示的服务集合

    private ProductDetailsBannerAdapter vpAdapter;
    private ArrayList<View> viewList = new ArrayList<>();

    private int page = 1;
    private String specId, specName, serviceId, serviceName = "";
    private int goodsNum = 1;
    private SelectDialog cartDialog;

    private int y_xiangqing, y_pingjia, y_tuijian, y_bar;


    private Dialog popSelect, serverDialog;
    private boolean IsSliding = false;
    private boolean isClickTablayout = false;
    private String shopperNum = "";
    private String endTime = "";
    private String day_start_time = "";
    private String day_end_time = "";

    private float xx = 0;
    private float yy = 0;
    private int seckillnum;
    private int store_count;
    private int storeCount;
    private Intent intent;
    private int dialogStore_count = -1;

    @Override
    public int getContentView() {
        return R.layout.activity_product_details;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        titleBar.setVisibility(View.GONE);
        loadingLayout.setStatus(LoadingLayout.Loading);

        ivBack.setOnClickListener(this);
        ivCart.setOnClickListener(this);

        llFont.bringToFront();
        myTitlebar.getBackground().mutate().setAlpha(100);
        myTablayout.getBackground().mutate().setAlpha(100);
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        ViewGroup.LayoutParams params = bannerTop.getLayoutParams();

        params.height = dm.widthPixels;
        bannerTop.setLayoutParams(params);

        vpDetails.setLayoutParams(params);
        goods_id = getIntent().getStringExtra("goods_id");

        Log.i("TAG", "id" + goods_id);
        helper = new PreferencesHelper(this);

        adapter = new LessEvaluateAdapter(nosList);
        nosList.setAdapter(adapter);

        likeAdapter = new LikeAdapter(gridLike);
        gridLike.setAdapter(likeAdapter);
        gridLike.setOnItemClickListener(this);
        likeAdapter.setOnclickListener(new LikeAdapter.OnAddCartOnclickListener() {
            @Override
            public void onClick(int position, float x, float y) {
                //  seckillnum = serverList.get(position).getSeckillnum();
                xx = x;
                yy = y;
                GuessLikeBean guessLikeBean = likeAdapter.getData().get(position);

                if (likeAdapter.getData().get(position).getSpec_type() == 0) {
                    //不存在规格
                    addShoppingCarts(helper.getToken(), String.valueOf(guessLikeBean.getGoods_id()), "", "", 1);
                } else {
                    //存在规格 选择规格
                    if (details.getType()==2){
                        cartDialog = new SelectDialog(ProductDetailsActivity.this, guessLikeBean.getType(),details.getGroupnum());
                    }else {
                        cartDialog = new SelectDialog(ProductDetailsActivity.this, guessLikeBean.getType());
                    }


                    SpecDialogBean specDialogBean = new SpecDialogBean();
                    specDialogBean.setGoods_id(guessLikeBean.getGoods_id());
                    specDialogBean.setImg(guessLikeBean.getImg());
                    specDialogBean.setSpec_name(guessLikeBean.getSpec_key_name());
                    specDialogBean.setPlus_price(guessLikeBean.getPlus_price());
                    specDialogBean.setShop_price(guessLikeBean.getShop_price());


                    cartDialog.setGoToUseOnclickListener(new SelectDialog.onGoToUseOnclickListener() {
                        @Override
                        public void onUseClick(String goods_id, String spec_id, String service_id, int num, int s, double price, double vipPrice) {
                            tvPrice.setText("￥" + vipPrice + "/" + tvPrice.getText().toString().substring(tvPrice.getText().toString().indexOf("/") + 1));
                            tvVipPrice.setText("￥" + price + tvPrice.getText().toString().substring(tvPrice.getText().toString().indexOf("/") + 1));
                            if (s == 0) {
                                return;
                            }
                            addShoppingCarts(helper.getToken(), goods_id, service_id, spec_id, num);
                        }
                    });
                    cartDialog.show();
                    cartDialog.setData(specDialogBean);
                }
            }
        });

        nosList.setFocusable(false);
        gridLike.setFocusable(false);

//        onScrollChanged里面有4个参数，l代表滑动后当前ScrollView可视界面的左上角在整个ScrollView的X轴中的位置，oldi也就是滑动前的X轴位置了。
//        同理，t也是当前可视界面的左上角在整个ScrollView的Y轴上的位置，oldt也就是移动前的Y轴位置了。
        scroll.setMyScrollChangeListener(new MyScrollView.MyScrollChangeListener() {
            @Override
            public void onScrollChange(int l, int t, int oldl, int oldt) {


                if (t > 100) {
                    myTablayout.setVisibility(View.VISIBLE);
                    myTitlebar.getBackground().mutate().setAlpha(100);
                }
                if (t >= 100 && t <= 255) {
                    myTitlebar.getBackground().mutate().setAlpha(t);
                    myTablayout.getBackground().mutate().setAlpha(t);
                } else if (t > 255) {
                    myTitlebar.getBackground().mutate().setAlpha(255);
                    myTablayout.getBackground().mutate().setAlpha(255);
                } else if (t == 0) {
                    myTablayout.setVisibility(View.GONE);
                    myTitlebar.getBackground().mutate().setAlpha(100);
                    myTablayout.getBackground().mutate().setAlpha(0);
                } else if (t > 0 && t < 100) {
                    myTitlebar.getBackground().mutate().setAlpha(100);
                    myTablayout.getBackground().mutate().setAlpha(t);
                }
                if (isClickTablayout)
                    return;

                IsSliding = true;

                if (t <= y_xiangqing - y_bar*2) {
                    myTablayout.getTabAt(0).select();
                } else if (y_pingjia - y_bar*2 > t && t > y_xiangqing  - y_bar*2) {
                    myTablayout.getTabAt(1).select();
                } else if (t<y_tuijian -  y_bar*2&&t > y_pingjia -  y_bar*2) {
                    myTablayout.getTabAt(2).select();

                } else if (t > y_tuijian -  y_bar*2) {
                    myTablayout.getTabAt(3).select();
                }
                IsSliding = false;

            }
        });

        myTablayout.addTab(myTablayout.newTab().setText("商品"));
        myTablayout.addTab(myTablayout.newTab().setText("详情"));
        myTablayout.addTab(myTablayout.newTab().setText("评论"));
        myTablayout.addTab(myTablayout.newTab().setText("推荐"));

        myTablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if (IsSliding)
                    return;

                if ("商品".equals(tab.getText().toString())) {

                    isClickTablayout = true;
                    scroll.scrollTo(0, 10);
                    isClickTablayout = false;
                } else if ("详情".equals(tab.getText().toString())) {
                    isClickTablayout = true;
                    scroll.scrollTo(0, y_xiangqing - y_bar*2);//详情-标题栏
                    isClickTablayout = false;
                } else if ("评论".equals(tab.getText().toString())) {
                    isClickTablayout = true;
                    scroll.scrollTo(0, y_pingjia - y_bar*2);//评论-标题栏
                    isClickTablayout = false;
                } else if ("推荐".equals(tab.getText().toString())) {
                    isClickTablayout = true;
                    scroll.scrollTo(0, y_tuijian - y_bar*2);//推荐-标题栏
                    isClickTablayout = false;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        ViewTreeObserver vto = llFont.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                llFont.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                y_bar = llFont.getHeight();
            }
        });

        ivCommentAll.setOnClickListener(this);

        //刷新加载
        refresh.setOnLoadMoreListener(this);
        refresh.setMaxFooterHeight(DensityUtil.dp2px(ProductDetailsActivity.this, 150));
        refresh.setStyle(0, 0);
        canRefreshFooter.setPullStr("上拉加载更多");
        canRefreshFooter.setReleaseStr("松开刷新");
        canRefreshFooter.setRefreshingStr("加载中");
        canRefreshFooter.setCompleteStr("");

    }

    @Override
    public void getData() {

        getInfo(goods_id);
        getFirstData();
        getRecommend(page);

        shoppingCartCount(helper.getToken());
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
//        y_xiangqing = rlXiangqing.getTop();
//        y_pingjia = rlPingjia.getTop();
//        y_tuijian =tvTuijian.getTop();
//
//                LogUtil.i("详情：" + y_xiangqing + "   评价：" + y_pingjia + "  头：" + y_bar+" 推荐："+y_tuijian);

    }


    @OnClick({R.id.rl_select, R.id.iv_pic, R.id.btn_confim, R.id.ll_call, R.id.ll_shopcar, R.id.ll_addshopcar, R.id.ll_buy})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.ll_call:
                //电话
                ToolUtil.goToCall(ProductDetailsActivity.this, shopperNum);
                break;
            case R.id.ll_shopcar:
                //购物车
                if (TextUtils.isEmpty(new PreferencesHelper(ProductDetailsActivity.this).getToken())) {
                    ToolUtil.goToLogin(ProductDetailsActivity.this);
                    return;
                }
                Intent intent1 = new Intent(ProductDetailsActivity.this, MainActivity.class);
                intent1.putExtra("go_cart", 1);
                startActivity(intent1);
                break;
            case R.id.ll_addshopcar:

                //   getInfo(goods_id);
                //库存字段
                storeCount = details.getStore_count();
                Log.d("哈哈", storeCount + "");
                if (TextUtils.isEmpty(new PreferencesHelper(ProductDetailsActivity.this).getToken())) {
                    ToolUtil.goToLogin(ProductDetailsActivity.this);
                }
                //库存大于0的时候可以加入购物车
                else if (storeCount > 0) {
                    //加入购物车
                    if (details.getType() != 0 && details.getIs_share() == 0) {
                        //促销产品

                        onViewClick(ivPic);

                        return;
                    }
                    //这个加入购物车的方法吗但是现在不好使啊   没有库存的时候成功了  有库存的时候按钮不能动了
                    if (dialogStore_count != -1 && dialogStore_count > 0) {
                        addShoppingCart(helper.getToken(), goods_id, serviceId, specId, goodsNum);
                    } else {
                        ToastUtil.showToast(this, "库存为0份");
                        return;
                    }


                    if (serverBean!=null&&serverBean.getSpeclist().size() > 1 && TextUtils.isEmpty(specId)) {
                        if (cartDialog == null)
                            showSpecAndServiceDialog(Integer.parseInt(goods_id),
                                    details.getOriginal_img().get(0), details.getWeight() + "克",
                                    Double.parseDouble(details.getPlus_price()),
                                    Double.parseDouble(details.getShop_price()),
                                    details.getType(), false);
                        else cartDialog.show();
                        return;
                    }

                    //库存等于0的时候不可以加入购物车
                } else if (storeCount == 0) {
                    // 放这里可以获取到值
                    llAddShopCar.setFocusableInTouchMode(false);
                    llAddShopCar.setFocusable(false);
                    llBuy.setFocusableInTouchMode(false);
                    llBuy.setFocusable(false);

                    ToastUtil.showToast(ProductDetailsActivity.this, "没有库存");
                    return;
                } else {
                    ToastUtil.showToast(ProductDetailsActivity.this, "库存数据异常");
                }


                break;
            case R.id.ll_buy:
                buyOrder(helper.getToken(), goods_id, serviceId, specId, goodsNum);
                break;

            case R.id.rl_select:
//                showSelectDialog();
                // TODO: 2018/8/14
                if (cartDialog == null && (null != goods_id || null != details))
                    showSpecAndServiceDialog(Integer.parseInt(goods_id),
                            details.getOriginal_img().get(0), details.getWeight() + "克",
                            Double.parseDouble(details.getPlus_price()),
                            Double.parseDouble(details.getShop_price()),
                            details.getType(), true);
                else if (null == goods_id || null == details)
                    ToastUtil.showToast(ProductDetailsActivity.this, "参数获取异常");
                else
                    cartDialog.show();
                break;
            case R.id.btn_confim:
                if (details.getType() != 0 && details.getIs_share() == 0) {
                    //促销产品  没有分享 去分享
                    onViewClick(ivPic);
                    break;
                }
                String str = ((Button) view).getText().toString();
                if (str.equals("立即购买")) {
//                    ToastUtil.showToast(ProductDetailsActivity.this,"立即购买");
                    //直接进入结算页面（携带参数）
                    Intent intentbuy = new Intent(ProductDetailsActivity.this, SettlementActivity.class);
                    intentbuy.putExtra("goods_id", goods_id);
                    intentbuy.putExtra("spec_key", specId);
                    intentbuy.putExtra("service_id", serviceId);
                    intentbuy.putExtra("goods_num", goodsNum);
                    startActivity(intentbuy);

                } else if (str.equals("加入购物车")) {
                    if (serverBean.getSpeclist().size() > 1) {
                        if (cartDialog == null)
                            showSpecAndServiceDialog(Integer.parseInt(goods_id),
                                    details.getOriginal_img().get(0), details.getWeight() + "克",
                                    Double.parseDouble(details.getPlus_price()),
                                    Double.parseDouble(details.getShop_price()),
                                    details.getType(), false);
                        else cartDialog.show();
                        return;
                    }
                    addShoppingCart(helper.getToken(), goods_id, serviceId, specId, goodsNum);
                }
                break;

            case R.id.iv_pic:
                if (TextUtils.isEmpty(helper.getToken())) {
                    ToolUtil.goToLogin(ProductDetailsActivity.this);
                    return;
                }

//                if(details.getIs_share()==0){
//
//                }
                SharedDialog dialog = SharedDialog.showDialog(Constants.IMG_HOST + details.getOriginal_img().get(0),
                        details.getShare_url(), details.getGoods_name(), details.getGoods_remark(), this);
                dialog.show();
                dialog.setOnSharedSuccessClickListener(new SharedDialog.OnSharedSuccessClickListener() {
                    @Override
                    public void onSuccess(int type) {
                        //分享成功回调
                        bTvNumber.setVisibility(View.VISIBLE);//吴小飞bug
                        String stringNum = bTvNumber.getText().toString();
                        int i = Integer.parseInt(stringNum) + 1;
                        bTvNumber.setText(i + "");

                        details.setIs_share(1);
                        shared(helper.getToken(), String.valueOf(details.getGoods_id()), type);
                    }
                });
                break;
        }
    }

    private void diatelyBuy() {
        settlement(new PreferencesHelper(ProductDetailsActivity.this).getToken());
        getInfo(goods_id);
        //立即购买
        int storeCount = details.getStore_count();
        Log.d("哈哈s", store_count + "");
        if (TextUtils.isEmpty(new PreferencesHelper(ProductDetailsActivity.this).getToken())) {
            ToolUtil.goToLogin(ProductDetailsActivity.this);
//                    return;
        } else if (storeCount > 0) {
            //这个加入购物车的方法吗但是现在不好使啊   没有库存的时候成功了  有库存的时候按钮不能动了
            if (dialogStore_count != -1 && dialogStore_count == 0) {
                ToastUtil.showToast(this, "库存为0份");
                return;
            }
            if (details.getType() != 0 && details.getIs_share() == 0) {

                onViewClick(ivPic);
                return;
            }
            intent = new Intent(ProductDetailsActivity.this, SettlementActivity.class);
            intent.putExtra("goods_id", goods_id);
            intent.putExtra("spec_key", specId);
            intent.putExtra("service_id", serviceId);
            intent.putExtra("goods_num", goodsNum);
            //促销产品  没有分享 去分享
            startActivity(intent);


            if (!"".equals(endTime) && !TextUtils.isEmpty(endTime)) {
                intent.putExtra("ENDTIME", endTime);
                intent.putExtra("DAYSTART", day_start_time);
                intent.putExtra("DAYEND", day_end_time);
                return;
            }

        } else if (storeCount == 0) {
            llBuy.setFocusableInTouchMode(false);
            llBuy.setFocusable(false);
            ToastUtil.showToast(ProductDetailsActivity.this, "没有库存");
            return;
        } else {
            ToastUtil.showToast(ProductDetailsActivity.this, "库存数据异常");

        }
    }

    private void buyOrder(String token, final String goods_id, String service_id, String spec_id, int goods_num) {
        showLoadingDialog("加载中...");
        HttpHelp.getInstance().create(RemoteApi.class).isExistenceOrders(token,goods_id,service_id,spec_id,String.valueOf(goods_num))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<CartListBean>>(ProductDetailsActivity.this, null) {
                    @Override
                    public void onNext(BaseBean<CartListBean> baseBean) {
                        super.onNext(baseBean);
                        hideLoadingDialog();
                        if (baseBean.code==0){
                            diatelyBuy();
                        }else {
                            ToastUtil.showToast(ProductDetailsActivity.this,baseBean.message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        hideLoadingDialog();
                    }
                });
    }

    private void settlement(String token) {
        HttpHelp.getInstance().create(RemoteApi.class).settlement(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<Object>>(ProductDetailsActivity.this, null) {
                    @Override
                    public void onNext(BaseBean<Object> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                        } else {
                            ToastUtil.showToast(ProductDetailsActivity.this, baseBean.message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);

                    }
                });
    }

    /*
     * 获取第一件商品的库存
     * */
    private void getFirstData() {
        HttpHelp.getInstance().create(RemoteApi.class).getServer(goods_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<ServerBean>>(this, null) {
                    @Override
                    public void onNext(BaseBean<ServerBean> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            ServerBean bean = baseBean.data;
                            dialogStore_count = bean.getSpeclist().get(0).getStore_count();
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);

                    }
                });
    }

    @Override
    protected void onStop() {
        super.onStop();


    }
    Handler handler =new Handler();
    /**
     * 由于onWindowFocusChanged（）方法用来测量控件的高度无法获取
     */
    Runnable runnable=new Runnable() {
        @Override
        public void run() {
            y_xiangqing = rlXiangqing.getTop();
            y_pingjia = rlPingjia.getTop();
            y_tuijian =tvTuijian.getTop();
            y_bar=llFont.getHeight();
            LogUtil.i("详情：" + y_xiangqing + "   评价：" + y_pingjia + "  头：" + y_bar+" 推荐："+y_tuijian);
        }
    };

    /**
     * 获取商品详情
     *
     * @param goods_id
     */
    private void getInfo(final String goods_id) {
        HttpHelp.getInstance().create(RemoteApi.class).getDetails(this.goods_id, new PreferencesHelper(ProductDetailsActivity.this).getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<ProductDetailsBean>>(ProductDetailsActivity.this, null) {
                    @Override
                    public void onNext(BaseBean<ProductDetailsBean> baseBean) {
                        super.onNext(baseBean);
                        loadingLayout.setStatus(LoadingLayout.Success);
                        if (baseBean.code == 0) {
                            details = baseBean.data;
                            handler.postDelayed(runnable,100);
                            tvTitle.setText(details.getGoods_name());
                            updataUI(details);
                            //规格中有些数据  要用到详情的数据 所以获取完详情后获取规格
                            getSpecAndServer(goods_id);
                            if (details.getType() != 0) {
                                llWarn.setVisibility(View.VISIBLE);
                            }

                        } else if (baseBean.code == 4) {
                            ToolUtil.goToLogin(ProductDetailsActivity.this);
                        } else {
                            ToastUtil.showToast(ProductDetailsActivity.this, baseBean.message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        loadingLayout.setStatus(LoadingLayout.Error);
                        ToastUtil.showToast(ProductDetailsActivity.this, "数据加载异常，请联系管理员");
                        finish();
                    }
                });
    }


    /**
     * 推荐商品
     *
     * @param page
     */
    private void getRecommend(final int page) {
        HttpHelp.getInstance().create(RemoteApi.class).recommend(page, "3", goods_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<List<GuessLikeBean>>>(ProductDetailsActivity.this, null) {
                    @Override
                    public void onNext(BaseBean<List<GuessLikeBean>> baseBean) {
                        super.onNext(baseBean);
                        liksBean = baseBean.data;
                        refresh.loadMoreComplete();
                        if (baseBean.code == 0) {
                            if (page == 1) {
                                if (liksBean != null && liksBean.size() > 0) {
                                    likeAdapter.setData(liksBean);
                                } else {
                                    likeAdapter.clear();
                                }
                            } else {
                                if (liksBean != null && liksBean.size() > 0) {
                                    likeAdapter.addMoreData(liksBean);
                                } else {
                                    ToastUtil.showToast(ProductDetailsActivity.this, "暂无更多");
                                }
                            }
                        } else {
                            ToastUtil.showToast(ProductDetailsActivity.this, baseBean.message);
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
     * 获取规格和增值服务
     *
     * @param goods_id
     */
    private void getSpecAndServer(String goods_id) {
        HttpHelp.getInstance().create(RemoteApi.class).getServer(goods_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<ServerBean>>(ProductDetailsActivity.this, null) {

                    @Override
                    public void onNext(BaseBean<ServerBean> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            serverBean = baseBean.data;
                            if (serverBean.getSpeclist().size() == 0) {
                                ServerBean.SpeclistBean bean = new ServerBean.SpeclistBean();


                                bean.setKey("0");
                                bean.setKey_name(details.getWeight() + "克");
                                serverBean.getSpeclist().add(bean);
                            }
                            if (serverBean.getSpeclist().size() > 0) {
                                tvSpecf.setText("已选：" + serverBean.getSpeclist().get(0).getKey_name() + "   " + (isZero(serverBean.getSpeclist().get(0)) ? goodsNum : 0) + "(份)");
                                specId = serverBean.getSpeclist().get(0).getKey();
                                specName = serverBean.getSpeclist().get(0).getKey_name();
                            }

//                            if (serverBean.getService_price() == null || serverBean.getService_price().size() == 0) {
//                                rlServer.setVisibility(View.GONE);
//                                serviceLine.setVisibility(View.GONE);
//                            }
                        } else {
                            ToastUtil.showToast(ProductDetailsActivity.this, baseBean.message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);

                    }
                });
    }

    private boolean isZero(ServerBean.SpeclistBean bean) {
        if (bean.getStore_count() > 0) {
            return true;
        }
        return false;
    }

    /**
     * 分享成功
     *
     * @param token
     * @param goodsId
     * @param type
     */
    private void shared(String token, String goodsId, int type) {
        HttpHelp.getInstance().create(RemoteApi.class).alreadyShare(token, goodsId, type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<Object>>(ProductDetailsActivity.this, null) {
                    @Override
                    public void onNext(BaseBean<Object> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {

                        } else {
                            ToastUtil.showToast(ProductDetailsActivity.this, baseBean.message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);

                    }
                });
    }


    //购物车数量
    private void shoppingCartCount(String token) {
        HttpHelp.getInstance().create(RemoteApi.class).shoppingCartCount(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<String>>(ProductDetailsActivity.this, null) {
                    @Override
                    public void onNext(BaseBean<String> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            if (baseBean.data != null && !TextUtils.isEmpty(baseBean.data)) {
                                shopNumber.setVisibility(View.VISIBLE);
                                bTvNumber.setVisibility(View.VISIBLE);
                                if (Integer.valueOf(baseBean.data) < 100 && Integer.valueOf(baseBean.data) > 0) {
                                    shopNumber.setText(baseBean.data);
                                    bTvNumber.setText(baseBean.data);
                                } else if (Integer.valueOf(baseBean.data) == 0) {
                                    shopNumber.setVisibility(View.GONE);
                                    bTvNumber.setVisibility(View.GONE);
                                } else {
                                    shopNumber.setText("99+");
                                    bTvNumber.setText("99+");
                                }
                            }
                        } else {
                            ToastUtil.showToast(ProductDetailsActivity.this, baseBean.message);
                            shopNumber.setVisibility(View.GONE);
                            bTvNumber.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        shopNumber.setVisibility(View.GONE);
                        bTvNumber.setVisibility(View.GONE);
                    }
                });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        shoppingCartCount(helper.getToken());
    }

    private void updataUI(final ProductDetailsBean detailsBean) {
        shopperNum = detailsBean.getMobile();
        if (detailsBean.getType() == 2) {    // 团购商品
            //团购
            llVio.setVisibility(View.GONE);
            llTuan.setVisibility(View.VISIBLE);
            tvTuan.setText("已拼团 " + detailsBean.getNumber() + "/" + detailsBean.getStore_count());

            tvPrice.setText("￥" + detailsBean.getPlus_price() + "/" + detailsBean.getUnit());
            tvDeliver.setText("现在下单，最快今天" + detailsBean.getDay_start_time() + "-" + detailsBean.getDay_end_time() + "送达");


        } else if (detailsBean.getType() == 1) {   // 秒杀商品
            //秒杀
            llVio.setVisibility(View.GONE);
            llSecondKill.setVisibility(View.VISIBLE);
            tvCountHint.setText("秒杀进行中！剩余：");
            long endTime = TimeUtil.getJavaTime(detailsBean.getEnd_time());
            long time = Calendar.getInstance().getTimeInMillis();
            countdown.start(endTime - time);

            tvPrice.setText("￥" + detailsBean.getPlus_price() + "/" + detailsBean.getUnit());
            tvDeliver.setText("现在下单，最快今天" + detailsBean.getDay_start_time() + "-" + detailsBean.getDay_end_time() + "送达");

        } else if (detailsBean.getType() == 3) {    // 预售商品
            //预售
            btnConfim.setText("立即订购");
            llAddShopCar.setVisibility(View.GONE);
            llShopcar.setVisibility(View.GONE);
            llVio.setVisibility(View.GONE);
            llSecondKill.setVisibility(View.VISIBLE);
            tvCountHint.setText("预售进行中！剩余：");
            endTime = detailsBean.getPresel_timeSmap();
            day_start_time = detailsBean.getDay_start_time();
            day_end_time = detailsBean.getDay_end_time();
            long endTime = TimeUtil.getJavaTime(detailsBean.getEnd_time());
            long time = Calendar.getInstance().getTimeInMillis();
            countdown.start(endTime - time);

            tvPrice.setText("￥" + detailsBean.getPlus_price() + "/" + detailsBean.getUnit());
            tvDeliver.setText("现在下单，最快" + detailsBean.getPresel_time() + "送达");

        } else if (detailsBean.getType() == 0) {  // 普通商品
            //普通
            tvPrice.setText("￥" + detailsBean.getShop_price() + "/" + detailsBean.getUnit());
            tvDeliver.setText("现在下单，最快今天" + detailsBean.getDay_start_time() + "-" + detailsBean.getDay_end_time() + "送达");

        }

        tvOldPrice.setText("￥" + detailsBean.getMarket_price());
        tvOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        tvVipPrice.setText("￥" + detailsBean.getPlus_price() + "/" + detailsBean.getUnit());

        tvName.setText(detailsBean.getGoods_name());
        tvRemark.setText(detailsBean.getGoods_remark());

        if (TextUtils.isEmpty(detailsBean.getVideo())) {
            bannerTop.setVisibility(View.VISIBLE);
            vpDetails.setVisibility(View.GONE);
            bindBannerData();
        } else {
            bannerTop.setVisibility(View.GONE);
            vpDetails.setVisibility(View.VISIBLE);
            bindVideoData();
        }


        tvRegion.setText(detailsBean.getGoods_info().getRegion());
        tvStorage.setText(detailsBean.getGoods_info().getStorage_type());
        tvBrand.setText(detailsBean.getGoods_info().getBrand());
        tvWeight.setText(detailsBean.getGoods_info().getWeight());

        tvNumber.setText("(" + detailsBean.getComment_count() + ")");


        adapter.setData(detailsBean.getComment_list());
        if (detailsBean.getComment_list().size() > 0) {
            View view = LayoutInflater.from(ProductDetailsActivity.this).inflate(R.layout.footer_evaluate, null);
            TextView tvFooter = view.findViewById(R.id.tv_footer);
            tvFooter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ProductDetailsActivity.this, EvaluateListActivity.class);
                    intent.putExtra("goods_id", goods_id);
                    intent.putExtra("evaluateNum", detailsBean.getComment_count());
                    startActivity(intent);
                }
            });
            nosList.addFooterView(view);

            rlEmpty.setVisibility(View.GONE);
            commentsGap.setVisibility(View.GONE);
        } else {
            rlEmpty.setVisibility(View.VISIBLE);
            commentsGap.setVisibility(View.VISIBLE);

        }


        webDetails.loadDataWithBaseURL(null, detailsBean.getGoods_content(), "text/html", "utf-8", null);
        webDetails.getSettings().setUseWideViewPort(true);
        webDetails.getSettings().setLoadWithOverviewMode(true);
        webDetails.getSettings().setLoadsImagesAutomatically(true); //支持自动加载图片
        webDetails.getSettings().setDefaultTextEncodingName("utf-8");//设置编码格式
        webDetails.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                y_pingjia = rlPingjia.getTop();
            }
        });


    }

    /**
     * 为顶部轮播添加图片
     */
    public void bindBannerData() {
        List<String> original_img = details.getOriginal_img();

        bannerTop.setImages(original_img)
                .setImageLoader(new ImageLoader() {
                    @Override
                    public void displayImage(Context context, Object path, ImageView imageView) {
                        String imgUrl = Constants.IMG_HOST + path.toString();

                        GlideUtil.display(ProductDetailsActivity.this, imgUrl, imageView);

                    }
                }).setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                //轮播图跳转，暂时不用，如果是大图的话，检查图片地址是否正确
                //轮播图跳转，暂时不用，如果是大图的话，检查图片地址是否正确
                ArrayList<String> bGAPhotoPreviewBaseList = new ArrayList<>();
                for (int i = 0; i < details.getOriginal_img().size(); i++) {
                    bGAPhotoPreviewBaseList.add(Constants.IMG_HOST + details.getOriginal_img().get(i));
                }
              /*  PhotoPreviewIntent intent = new PhotoPreviewIntent(MainActivity.this);
                intent.setCurrentItem(position); // 当前选中照片的下标
                intent.setPhotoPaths(imagePaths); // 已选中的照片地址
                startActivityForResult(intent, REQUEST_PREVIEW_CODE);*/
                startActivity((BGAPhotoPreviewActivity.newIntent(ProductDetailsActivity.this, null, bGAPhotoPreviewBaseList, position)));
                //轮播图跳转，暂时不用，如果是大图的话，检查图片地址是否正确
                // startActivity((BGAPhotoPreviewActivity.newIntent(mContext, null, bannerList, position)));
            }
        }).start();

    }

    /**
     * 设置顶部有视频的banner
     */
    public void bindVideoData() {
        View videoView = LayoutInflater.from(ProductDetailsActivity.this).inflate(R.layout.item_pager_video, null);
        viewList.add(videoView);
        for (String url : details.getOriginal_img()) {
            View imgView = LayoutInflater.from(ProductDetailsActivity.this).inflate(R.layout.item_pager_img, null);
            viewList.add(imgView);
        }
        List<String> bGAPhotoPreviewBaseList = new ArrayList<>();
        for (int i = 0; i < details.getOriginal_img().size(); i++) {
            bGAPhotoPreviewBaseList.add(Constants.IMG_HOST + details.getOriginal_img().get(i));
        }
        vpAdapter = new ProductDetailsBannerAdapter(ProductDetailsActivity.this,
                viewList,
                details.getVideo(),
                (ArrayList<String>) bGAPhotoPreviewBaseList, details.getVideo_img());

        vpDetails.setAdapter(vpAdapter);
        vpDetails.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                JZVideoPlayerStandard.releaseAllVideos();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    @Override
    protected void onPause() {
        super.onPause();
        JZVideoPlayerStandard.releaseAllVideos();
    }

    /**
     * 选择数量(弃用)
     */
    public void showSelectDialog() {
        if (serverBean == null || serverBean.getSpeclist().size() == 0) {
            return;
        }
        popSelect = new Dialog(ProductDetailsActivity.this, R.style.ActionSheetDialogStyle);
        View contentView = LayoutInflater.from(ProductDetailsActivity.this).inflate(R.layout.pop_goods_detail_select, null);
        TextView add = contentView.findViewById(R.id.img_add);
        TextView less = contentView.findViewById(R.id.img_less);
        ImageView close = contentView.findViewById(R.id.iv_close);
        ImageView pic = contentView.findViewById(R.id.iv_pic);
        GlideUtil.display(ProductDetailsActivity.this, Constants.IMG_HOST + details.getOriginal_img().get(0), pic);
        Button btn = contentView.findViewById(R.id.btn_buy);
        btn.setText(btnConfim.getText().toString());
        TextView tvOneSpec = contentView.findViewById(R.id.tv_one_spec);
        final TextView num = contentView.findViewById(R.id.tv_delete_number);
        RecyclerView grid = contentView.findViewById(R.id.grid_spec);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i = Integer.parseInt(num.getText().toString());
                i++;
                num.setText(String.valueOf(i));
                goodsNum = i;
            }
        });
        less.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i = Integer.parseInt(num.getText().toString());
                if (i > 1) {
                    i--;
                }
                num.setText(String.valueOf(i));
                goodsNum = i;
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popSelect.dismiss();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(specId)) {
                    btnConfim.performClick();
                    popSelect.dismiss();
                } else {
                    ToastUtil.showToast(ProductDetailsActivity.this, "请选择商品规格");
                }
            }
        });

        if (serverBean.getSpeclist().size() == 1) {
            tvOneSpec.setText(serverBean.getSpeclist().get(0).getKey_name());
            grid.setVisibility(View.GONE);
            specId = serverBean.getSpeclist().get(0).getKey();
            specName = serverBean.getSpeclist().get(0).getKey_name();
        } else {
            tvOneSpec.setVisibility(View.GONE);
            grid.setVisibility(View.VISIBLE);
        }

        final SpecAdapter specAdapter = new SpecAdapter(grid);

        LinearLayout.LayoutParams lp1 = (LinearLayout.LayoutParams) grid.getLayoutParams();
        lp1.width = getResources().getDisplayMetrics().widthPixels;
        MeasureFlowLayoutManager flowLayoutManager = new MeasureFlowLayoutManager(this);
        grid.addItemDecoration(new SpaceItemDecoration((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics())));
        grid.setLayoutManager(flowLayoutManager);
        grid.setAdapter(specAdapter);


        specAdapter.setData(serverBean.getSpeclist());

        specAdapter.setOnRVItemClickListener(new OnRVItemClickListener() {
            @Override
            public void onRVItemClick(ViewGroup parent, View itemView, int position) {
                if (serverBean.getSpeclist().get(position).isSelected()) {
                    serverBean.getSpeclist().get(position).setSelected(false);
                    specId = "";
                    specName = "";
                } else {
                    for (int j = 0; j < serverBean.getSpeclist().size(); j++) {
                        serverBean.getSpeclist().get(j).setSelected(false);
                    }
                    serverBean.getSpeclist().get(position).setSelected(true);
                    specId = serverBean.getSpeclist().get(position).getKey();
                    specName = serverBean.getSpeclist().get(position).getKey_name();
                }
                specAdapter.notifyDataSetChanged();
            }
        });

        popSelect.setContentView(contentView);
        Window dialogwindow = popSelect.getWindow();
        dialogwindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogwindow.getAttributes();
        lp.windowAnimations = R.style.PopUpBottomAnimation;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        dialogwindow.setAttributes(lp);
        popSelect.show();

        popSelect.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if (!TextUtils.isEmpty(specId)) {
                    tvSpecf.setText("已选：" + specName + "   " + goodsNum + "(份)");
                }
            }
        });

    }


    private void selectServer(String specId) {
        serverList.clear();
        for (int i = 0; i < serverBean.getService_price().size(); i++) {
            if (specId.equals(serverBean.getService_price().get(i).getKey())) {
                serverList.add(serverBean.getService_price().get(i));
            }
        }
    }

    //加入购物车
    private void addShoppingCarts(String token, String goods_id, String service_id, String spec_id, int goods_num) {
        showLoadingDialog("加载中...");
        HttpHelp.getInstance().create(RemoteApi.class).addCart(token, goods_id, service_id, spec_id, goods_num)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<Object>>(ProductDetailsActivity.this, null) {
                    @Override
                    public void onNext(BaseBean<Object> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            EventBusUtil.post(new ShoppingCartEvent());
                            shoppingCartCount(helper.getToken());
                            classifyCircleMoveView.setVisibility(View.VISIBLE);
                            int[] location = new int[2];
                            ivCart.getLocationOnScreen(location);
                            classifyCircleMoveView.setMovePath(xx, yy, location[0] + (location[0] / 2), location[1]);
                            classifyCircleMoveView.startAnim();
                        } else if (baseBean.code == 4) {
                            ToolUtil.goToLogin(ProductDetailsActivity.this);
                        } else {
                            ToastUtil.showToast(ProductDetailsActivity.this, baseBean.message);
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

    private void addShoppingCart(String token, String goods_id, String service_id, String spec_id, int goods_num) {
        HttpHelp.getInstance().create(RemoteApi.class).addCart(token, goods_id, service_id, spec_id, goods_num)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<Object>>(ProductDetailsActivity.this, null) {
                    @Override
                    public void onNext(BaseBean<Object> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            EventBusUtil.post(new ShoppingCartEvent());
                            shoppingCartCount(helper.getToken());
                            cmCircle.startAnim();

                        } else if (baseBean.code == 4) {
                            ToolUtil.goToLogin(ProductDetailsActivity.this);
                        } else {
                            ToastUtil.showToast(ProductDetailsActivity.this, baseBean.message);


                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);

                    }
                });

    }
//    commentsGap

    /**
     * 选择规格和服务（二合一）
     *
     * @param goodsId
     * @param img
     * @param specName1
     * @param plusPrice
     * @param shopPrice
     */
    private void showSpecAndServiceDialog(int goodsId, String img, String specName1, double plusPrice, double shopPrice, int type, boolean isSpec) {
        if (details.getType()==2)
        cartDialog = new SelectDialog(ProductDetailsActivity.this, type,details.getGroupnum());
        else
            cartDialog = new SelectDialog(ProductDetailsActivity.this, type);

        SpecDialogBean specDialogBean = new SpecDialogBean();
        specDialogBean.setGoods_id(goodsId);
        specDialogBean.setImg(img);
        specDialogBean.setSpec_name(specName1);
        specDialogBean.setPlus_price(plusPrice);
        specDialogBean.setShop_price(shopPrice);
        if (type == 1)
            specDialogBean.setShop_Seckillnum(seckillnum);
        //加购物车
        if (isSpec) {
            cartDialog.setGoToUseOnclickListener(new SelectDialog.onGoToUseOnclickListener() {
                @Override
                public void onUseClick(String goods_id, String spec_id, String service_id, int num, int s, double price, double vipPrice) {
                    //addShoppingCart(helper.getToken(), goods_id, service_id, spec_id, num);
                    cartDialog.cancel();
                }
            });
        } else {
            cartDialog.setGoToUseOnclickListener(new SelectDialog.onGoToUseOnclickListener() {
                @Override
                public void onUseClick(String goods_id, String spec_id, String service_id, int num, int s, double price, double vipPrice) {
                    //tvPrice.setText("￥" + vipPrice + "/" + tvPrice.getText().toString().substring(tvPrice.getText().toString().indexOf("/") + 1));
                    //tvVipPrice.setText("￥" + price + tvPrice.getText().toString().substring(tvPrice.getText().toString().indexOf("/") + 1));
                    if (s == 0) {
                        return;
                    }
                    addShoppingCart(helper.getToken(), goods_id, service_id, spec_id, num);

                }
            });
        }

        cartDialog.show();
        if (type == 1 && details.getSeckillnum() == 1) {
            cartDialog.setEdittextToch(true);
        }

        cartDialog.setServiceId(specId, serviceId, goodsNum);
        cartDialog.setData(specDialogBean);
        //关闭
        cartDialog.setCancelOnclickListener(new SelectDialog.onCancelOnclickListener() {
            @Override
            public void onCancelClick(String spec_id, String spec_name, String service_id, String service_name, int num, int store_count, double price, double vipPrice) {
                specId = spec_id;
                specName = spec_name;
                serviceId = service_id;
                serviceName = service_name;
                goodsNum = num;
                dialogStore_count = store_count;
                tvSpecf.setText("已选：" + specName + "  " + serviceName + "   " + (dialogStore_count > 0 ? goodsNum : 0) + "(份)");
                //tvPrice.setText("￥" + vipPrice + "/" + tvPrice.getText().toString().substring(tvPrice.getText().toString().indexOf("/") + 1));
                //tvVipPrice.setText("￥" + price + tvPrice.getText().toString().substring(tvPrice.getText().toString().indexOf("/") + 1));
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_cart:
                if (TextUtils.isEmpty(new PreferencesHelper(ProductDetailsActivity.this).getToken())) {
                    ToolUtil.goToLogin(ProductDetailsActivity.this);
                    return;
                }
                Intent intent1 = new Intent(ProductDetailsActivity.this, MainActivity.class);
                intent1.putExtra("go_cart", 1);
                startActivity(intent1);
                break;
            case R.id.iv_comment_all:
                //去全部评论
                Intent intent = new Intent(ProductDetailsActivity.this, EvaluateListActivity.class);
                intent.putExtra("goods_id", goods_id);
                intent.putExtra("evaluateNum", details.getComment_count());
                startActivity(intent);
                break;
        }

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        //猜你喜欢点击跳详情
        String goodsId = String.valueOf(likeAdapter.getData().get(i).getGoods_id());
        Intent intent = new Intent(ProductDetailsActivity.this, ProductDetailsActivity.class);
        intent.putExtra("goods_id", goodsId);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    public void onLoadMore() {
        page++;
        getRecommend(page);
    }
}
