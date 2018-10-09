package com.guo.qlzx.maxiansheng.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.canyinghao.canrefresh.CanRefreshLayout;
import com.canyinghao.canrefresh.classic.ClassicRefreshView;
import com.guo.qlzx.maxiansheng.R;
import com.guo.qlzx.maxiansheng.adapter.AllClassificationAdapter;
import com.guo.qlzx.maxiansheng.adapter.FlowStyleBrandAdapter;
import com.guo.qlzx.maxiansheng.adapter.FlowStyleProductAdapter;
import com.guo.qlzx.maxiansheng.adapter.LikeSearchAdapter;
import com.guo.qlzx.maxiansheng.bean.ShowSearchBean;
import com.guo.qlzx.maxiansheng.bean.SpecDialogBean;
import com.guo.qlzx.maxiansheng.event.ShoppingCartEvent;
import com.guo.qlzx.maxiansheng.http.RemoteApi;
import com.guo.qlzx.maxiansheng.util.ListDataSave;
import com.guo.qlzx.maxiansheng.util.MeasureFlowLayoutManager;
import com.guo.qlzx.maxiansheng.util.SpaceItemDecoration;
import com.guo.qlzx.maxiansheng.util.ToolUtil;
import com.guo.qlzx.maxiansheng.util.costom.ClassifyCircleMoveView;
import com.guo.qlzx.maxiansheng.util.dialog.SelectDialog;
import com.guo.qlzx.maxiansheng.view.PopWindow;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.DensityUtil;
import com.qlzx.mylibrary.util.EventBusUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.StringUtil;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.qlzx.mylibrary.widget.NoScrollListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/4/18.
 * 搜索结果页面
 */

public class SearchListActivity extends BaseActivity implements CanRefreshLayout.OnLoadMoreListener, CanRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener, LikeSearchAdapter.OnAddClickListener {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.tv_search)
    ImageView tvSearch;
    @BindView(R.id.gowuche)
    ImageView gowuche;
    @BindView(R.id.titlebar)
    RelativeLayout titlebar;
    @BindView(R.id.condition)
    LinearLayout condition;
    @BindView(R.id.can_content_view)
    GridView canContentView;
    @BindView(R.id.can_refresh_header)
    ClassicRefreshView canRefreshHeader;
    @BindView(R.id.can_refresh_footer)
    ClassicRefreshView canRefreshFooter;
    @BindView(R.id.refresh)
    CanRefreshLayout refresh;
    @BindView(R.id.classification_text)
    TextView classificationText;
    @BindView(R.id.classification_rl)
    RelativeLayout classificationRl;
    @BindView(R.id.sort_text)
    TextView sortText;
    @BindView(R.id.sort_rl)
    RelativeLayout sortRl;
    @BindView(R.id.screen_rl)
    RelativeLayout screenRl;
    @BindView(R.id.dr_left)
    RelativeLayout drLeft;
    @BindView(R.id.mDrawerLayout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.ed_maxPrice)
    EditText edMaxPrice;
    @BindView(R.id.ed_minPrice)
    EditText edMinPrice;
    @BindView(R.id.brand_list)
    RecyclerView brandList;
    @BindView(R.id.product_list)
    RecyclerView productList;
    @BindView(R.id.tv_reset)
    TextView tvReset;
    @BindView(R.id.tv_determine)
    TextView tvDetermine;
    @BindView(R.id.btton_foot)
    LinearLayout bttonFoot;
    @BindView(R.id.rl_empty)
    LinearLayout rlEmpty;
    @BindView(R.id.tv_number)
    TextView tvNumber;
    @BindView(R.id.iv_classification)
    ImageView ivClassification;
    @BindView(R.id.iv_sort)
    ImageView ivSort;
    @BindView(R.id.cm_circle)
    ClassifyCircleMoveView classifyCircleMoveView;
    @BindView(R.id.iv_delete_input)
    ImageView ivDeleteInput;
    private int page = 1;


    private LikeSearchAdapter likeSearchAdapter;
    private List<ShowSearchBean.GoodsListBean> listGoods = new ArrayList<>();


    //品牌列表
    private FlowStyleBrandAdapter flowStyleBrandAdapter;
    private List<ShowSearchBean.BrandBean> listBrand = new ArrayList<>();
    private MeasureFlowLayoutManager flowLayoutManagerBrand;
    //产地列表
    private FlowStyleProductAdapter flowStyleProductAdapter;
    private List<ShowSearchBean.RegionBean> listProduct = new ArrayList<>();
    private MeasureFlowLayoutManager flowLayoutManagerProduct;
    private int widths;
    //全部分类
    private AllClassificationAdapter allClassificationAdapter;
    private List<ShowSearchBean.ClassifyBean> listClassify = new ArrayList<>();
    private PopWindow popuClassification;

    private PopWindow popuSort;
    private SortClick sortClick;
    private String searchTxt = "";
    private String brand;//品牌
    private String region;//产地
    private String category;//分类
    private String sort;//排序
    private String minPrice;
    private String maxPrice;
    private String brandName;//品牌
    private String regionNme;//产地
    private SelectDialog cartDialog;
    private PreferencesHelper helper;
    private List<String> historyBeans = new ArrayList<>();
    private ListDataSave dataSave;
    //0为显示 1为隐藏
    private int productType = 0;
    private int brandType = 0;

    private float xx = 0f;
    private float yy = 0f;


    @Override
    public int getContentView() {
        return R.layout.activity_search_list;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        hideTitleBar();

        helper = new PreferencesHelper(this);
        searchTxt = getIntent().getStringExtra("search_txt");
        etSearch.setText(searchTxt);
        etSearch.setSelection(searchTxt.length());

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


        likeSearchAdapter = new LikeSearchAdapter(canContentView);
        canContentView.setAdapter(likeSearchAdapter);
        canContentView.setOnItemClickListener(this);
        likeSearchAdapter.setOnAddClickListener(this);

        flowLayoutManagerBrand = new MeasureFlowLayoutManager(this);
        flowStyleBrandAdapter = new FlowStyleBrandAdapter(SearchListActivity.this);
        brandList.addItemDecoration(new SpaceItemDecoration((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics())));
        brandList.setLayoutManager(flowLayoutManagerBrand);
        brandList.setAdapter(flowStyleBrandAdapter);


        flowStyleBrandAdapter.setLisner(new FlowStyleBrandAdapter.OnClickRecyclerTypeBrandListner() {
            @Override
            public void onItemClick(View view, int position) {
                //brand = String.valueOf(listBrand.get(position).getId());
                for (int i = 0; i < flowStyleBrandAdapter.getItemCount(); i++) {
                    if (position == i) {
                        flowStyleBrandAdapter.getList().get(i).setTrue(true);
                    } else {
                        flowStyleBrandAdapter.getList().get(i).setTrue(false);
                    }
                }
                flowStyleBrandAdapter.notifyDataSetChanged();
            }
        });


        flowLayoutManagerProduct = new MeasureFlowLayoutManager(this);
        flowStyleProductAdapter = new FlowStyleProductAdapter(SearchListActivity.this);
        productList.addItemDecoration(new SpaceItemDecoration((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics())));
        productList.setLayoutManager(flowLayoutManagerProduct);
        productList.setAdapter(flowStyleProductAdapter);

        flowStyleProductAdapter.setLisner(new FlowStyleProductAdapter.OnClickRecyclerTypeProductListner() {
            @Override
            public void onItemClick(View view, int position) {
                for (int i = 0; i < flowStyleProductAdapter.getItemCount(); i++) {
                    if (position == i) {
                        flowStyleProductAdapter.getList().get(i).setTrue(true);
                    } else {
                        flowStyleProductAdapter.getList().get(i).setTrue(false);
                    }
                }
                flowStyleProductAdapter.notifyDataSetChanged();
            }
        });


        ViewTreeObserver vtos = classificationRl.getViewTreeObserver();
        vtos.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                widths = classificationRl.getWidth();
                return true;
            }
        });
        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            /**
             * 当抽屉滑动状态改变的时候被调用
             * 状态值是STATE_IDLE（闲置--0）, STATE_DRAGGING（拖拽的--1）, STATE_SETTLING（固定--2）中之一。
             * 抽屉打开的时候，点击抽屉，drawer的状态就会变成STATE_DRAGGING，然后变成STATE_IDLE
             */
            @Override
            public void onDrawerStateChanged(int arg0) {
                Log.i("drawer", "drawer的状态：" + arg0);
            }

            /**
             * 当抽屉被滑动的时候调用此方法
             * arg1 表示 滑动的幅度（0-1）
             */
            @Override
            public void onDrawerSlide(View arg0, float arg1) {
                Log.i("drawer", arg1 + "");
            }

            /**
             * 当一个抽屉被完全打开的时候被调用
             */
            @Override
            public void onDrawerOpened(View arg0) {
                Log.i("drawer", "抽屉被完全打开了！");


                if (!"".equals(brandName) && brandName != null) {
                    for (int i = 0; i < flowStyleBrandAdapter.getItemCount(); i++) {
                        if (brandName.equals(flowStyleBrandAdapter.getList().get(i).getName())) {
                            flowStyleBrandAdapter.getList().get(i).setTrue(true);
                        }
                    }
                }
                if (!"".equals(regionNme) && regionNme != null) {
                    for (int i = 0; i < flowStyleProductAdapter.getItemCount(); i++) {
                        if (regionNme.equals(flowStyleProductAdapter.getList().get(i).getName())) {
                            flowStyleProductAdapter.getList().get(i).setTrue(true);
                        }
                    }
                }
                flowStyleBrandAdapter.notifyDataSetChanged();

                flowStyleProductAdapter.notifyDataSetChanged();
            }

            /**
             * 当一个抽屉完全关闭的时候调用此方法
             */
            @Override
            public void onDrawerClosed(View arg0) {
                Log.i("drawer", "抽屉被完全关闭了！");
            }
        });
        dataSave = new ListDataSave(this, "SEARCHWORDS");
        getHistoryData();
        etSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {//修改回车键功能
                    searchTxt = etSearch.getText().toString();
                    page = 1;
                    upReset();
                    showSearch(searchTxt, category, sort, "", "", "", "", page);
                    historyBeans.add(etSearch.getText().toString());
                    dataSave.setDataList("SEARCHWORDS", historyBeans);
                }
                return false;
            }
        });
    }

    //获取历史搜索
    public void getHistoryData() {
        historyBeans.clear();
        historyBeans = dataSave.getDataList("SEARCHWORDS");
    }

    @Override
    public void getData() {
        showSearch(searchTxt, category, sort, minPrice, maxPrice, brand, region, page);
        shoppingCartCount(helper.getToken());
    }

    private void showSearch(String title, String category_id, String sort, String min_price,
                            String max_price, String brand_id, String region_id, final int page) {
        HttpHelp.getInstance().create(RemoteApi.class).showSearch(title, category_id, sort, min_price, max_price, brand_id, region_id, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<ShowSearchBean>>(SearchListActivity.this, null) {
                    @Override
                    public void onNext(BaseBean<ShowSearchBean> baseBean) {
                        super.onNext(baseBean);
                        refresh.loadMoreComplete();
                        refresh.refreshComplete();
                        if (baseBean.code == 0) {
                            listClassify = baseBean.data.getClassify();
                            listClassify.add(0, new ShowSearchBean.ClassifyBean("", "全部分类"));
                            listBrand = baseBean.data.getBrand();
                            if (listBrand != null && listBrand.size() > 0) {
                                flowStyleBrandAdapter.setList(listBrand);
                            }

                            listProduct = baseBean.data.getRegion();
                            if (listProduct != null && listProduct.size() > 0) {
                                flowStyleProductAdapter.setList(listProduct);
                            }


                            listGoods = baseBean.data.getGoods_list();
                            if (page == 1) {
                                if (listGoods != null && listGoods.size() > 0) {
                                    likeSearchAdapter.setData(listGoods);
                                    refresh.setVisibility(View.VISIBLE);
                                    rlEmpty.setVisibility(View.GONE);
                                } else {
                                    likeSearchAdapter.clear();
                                    refresh.setVisibility(View.GONE);
                                    rlEmpty.setVisibility(View.VISIBLE);
                                }
                            } else {
                                if (listGoods != null && listGoods.size() > 0) {
                                    likeSearchAdapter.addMoreData(listGoods);
                                } else {
                                    ToastUtil.showToast(SearchListActivity.this, "暂无更多");
                                }
                            }

                        } else {
                            ToastUtil.showToast(SearchListActivity.this, baseBean.message);
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

    @OnClick({R.id.tv_search, R.id.gowuche, R.id.classification_rl, R.id.sort_rl, R.id.screen_rl,
            R.id.tv_reset, R.id.tv_determine, R.id.iv_back, R.id.rl_brand, R.id.ll_product,R.id.iv_delete_input})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_delete_input:
                etSearch.setText("");
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_search:
                //右上角 购物车监听
                if (TextUtils.isEmpty(new PreferencesHelper(SearchListActivity.this).getToken())) {
                    ToolUtil.goToLogin(SearchListActivity.this);
                    return;
                }
                Intent intent1 = new Intent(SearchListActivity.this, MainActivity.class);
                intent1.putExtra("go_cart", 1);
                startActivity(intent1);
                break;

            case R.id.gowuche:

                break;

            case R.id.classification_rl:
                showThreePop(view);
                if (popuClassification != null && popuClassification.isShowing()) {
                    ivClassification.setImageResource(R.drawable.ic_up);
                } else {
                    ivClassification.setImageResource(R.drawable.quanquan);
                }
                break;

            case R.id.sort_rl:
                showSortPop(view);
                if (popuSort != null && popuSort.isShowing()) {
                    ivSort.setImageResource(R.drawable.ic_up);
                } else {
                    ivSort.setImageResource(R.drawable.quanquan);
                }
                break;

            case R.id.screen_rl:
                //打开抽屉
                mDrawerLayout.openDrawer(Gravity.RIGHT);

                break;
            case R.id.tv_reset://重置

                upReset();


                break;

            case R.id.tv_determine://确认
                minPrice = edMinPrice.getEditableText().toString();
                maxPrice = edMaxPrice.getEditableText().toString();

                for (int i = 0; i < flowStyleBrandAdapter.getList().size(); i++) {
                    if (flowStyleBrandAdapter.getList().get(i).isTrue()) {
                        brand = String.valueOf(flowStyleBrandAdapter.getList().get(i).getId());
                        brandName = flowStyleBrandAdapter.getList().get(i).getName();
                        break;
                    }
                }
                for (int i = 0; i < flowStyleProductAdapter.getList().size(); i++) {
                    if (flowStyleProductAdapter.getList().get(i).isTrue()) {
                        region = String.valueOf(flowStyleProductAdapter.getList().get(i).getId());
                        regionNme = flowStyleProductAdapter.getList().get(i).getName();
                        break;
                    }
                }

                mDrawerLayout.closeDrawer(Gravity.RIGHT);
                showSearch(searchTxt, category, sort, minPrice, maxPrice, brand, region, page);
                break;
            case R.id.rl_brand:
                //品牌
                /*  //0为显示 1为隐藏
                private int productType=0;
                private int brandType=0;*/
                if (brandType == 0) {
                    brandType = 1;
                    brandList.setVisibility(View.GONE);
                } else {
                    brandType = 0;
                    brandList.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.ll_product:
                //产地
                if (productType == 0) {
                    productType = 1;
                    productList.setVisibility(View.GONE);
                } else {
                    productType = 0;
                    productList.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    //重置
    private void upReset() {
        edMaxPrice.getEditableText().clear();
        edMinPrice.getEditableText().clear();
        region = "";
        brand = "";
        brandName = "";
        regionNme = "";
        for (int i = 0; i < flowStyleBrandAdapter.getList().size(); i++) {
            flowStyleBrandAdapter.getList().get(i).setTrue(false);
        }
        for (int i = 0; i < flowStyleProductAdapter.getList().size(); i++) {
            flowStyleProductAdapter.getList().get(i).setTrue(false);
        }

        flowStyleBrandAdapter.notifyDataSetChanged();
        flowStyleProductAdapter.notifyDataSetChanged();

    }


    /**
     * 全部分类
     *
     * @param view
     */
    private void showThreePop(View view) {
        if (listClassify != null && listClassify.size() > 0) {
            View inflate = LayoutInflater.from(SearchListActivity.this).inflate(
                    R.layout.layout_classification, null);

            popuClassification = new PopWindow(inflate, widths,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            popuClassification.setFocusable(true);
            popuClassification.setOutsideTouchable(true);
            WindowManager wm = (WindowManager) this
                    .getSystemService(Context.WINDOW_SERVICE);
            int width = wm.getDefaultDisplay().getWidth();
            popuClassification.setWidth(width);
            ColorDrawable cd = new ColorDrawable(0x00cccccc);// 背景颜色
            popuClassification.setBackgroundDrawable(cd);
            popuClassification.setAnimationStyle(R.style.style_pop_animation);// 动画效果必须放在showAsDropDown()方法上边，否则无效

            NoScrollListView recyclerView = inflate.findViewById(R.id.flowlayout);

            final LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) recyclerView.getLayoutParams();
//        lp.width = getResources().getDisplayMetrics().widthPixels;
            lp.width = widths - 10;
            allClassificationAdapter = new AllClassificationAdapter(recyclerView);
            recyclerView.setAdapter(allClassificationAdapter);


            allClassificationAdapter.setData(listClassify);


            recyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    popuClassification.dismiss();
                    classificationText.setText(allClassificationAdapter.getData().get(position).getName());
                    category = String.valueOf(allClassificationAdapter.getData().get(position).getId());
                    getData();
                }
            });
            popuClassification.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    ivClassification.setImageResource(R.drawable.quanquan);
                }
            });
            popuClassification.showAsDropDown(view);
        } else {
            ToastUtil.showToast(SearchListActivity.this, "暂无分类");
        }
    }


    /**
     * 排序
     *
     * @param view
     */
    private void showSortPop(View view) {
        View inflate = LayoutInflater.from(SearchListActivity.this).inflate(
                R.layout.pop_sort, null);

        if (sortClick == null) {
            sortClick = new SortClick();
        }
        popuSort = new PopWindow(inflate, widths,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        popuSort.setFocusable(true);
        popuSort.setOutsideTouchable(true);
        WindowManager wm = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);
       /* int width = wm.getDefaultDisplay().getWidth();
        popuSort.setWidth(width);*/
        ColorDrawable cd = new ColorDrawable(0x00cccccc);// 背景颜色
        popuSort.setBackgroundDrawable(cd);
        popuSort.setAnimationStyle(R.style.style_pop_animation);// 动画效果必须放在showAsDropDown()方法上边，否则无效
        inflate.findViewById(R.id.tv_moren).setOnClickListener(sortClick);
        inflate.findViewById(R.id.tv_price_down).setOnClickListener(sortClick);
        inflate.findViewById(R.id.tv_price_up).setOnClickListener(sortClick);
        inflate.findViewById(R.id.tv_num_down).setOnClickListener(sortClick);
        popuSort.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                ivSort.setImageResource(R.drawable.quanquan);
            }
        });
        popuSort.showAsDropDown(view);
    }


    //购物车数量
    private void shoppingCartCount(String token) {
        HttpHelp.getInstance().create(RemoteApi.class).shoppingCartCount(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<String>>(this, null) {
                    @Override
                    public void onNext(BaseBean<String> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            if (StringUtil.isNumeric(baseBean.data)&&baseBean.data != null && !TextUtils.isEmpty(baseBean.data)) {
                                tvNumber.setVisibility(View.VISIBLE);

                                if (Integer.valueOf(baseBean.data) < 100 && Integer.valueOf(baseBean.data) > 0) {
                                    tvNumber.setText(baseBean.data);
                                } else if (Integer.valueOf(baseBean.data) == 0) {
                                    tvNumber.setVisibility(View.GONE);
                                } else {
                                    tvNumber.setText("99+");
                                }
                            } else {
                                tvNumber.setVisibility(View.GONE);
                            }
                        } else {
                            ToastUtil.showToast(SearchListActivity.this, baseBean.message);
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

    //搜索结果item监听
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(SearchListActivity.this, ProductDetailsActivity.class);
        intent.putExtra("goods_id", String.valueOf(listGoods.get(position).getGoods_id()));
        startActivity(intent);
    }

    //添加购物车按钮
    @Override
    public void onAddClick(ShowSearchBean.GoodsListBean model, float x, float y) {
        showAddDialog(model, x, y);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }


    class SortClick implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.tv_moren:
                    sort = "";
                    break;
                case R.id.tv_price_down:
                    sort = "1";
                    break;
                case R.id.tv_price_up:
                    sort = "2";
                    break;
                case R.id.tv_num_down:
                    sort = "3";
                    break;
            }
            sortText.setText(((TextView) view).getText().toString());
            popuSort.dismiss();
            getData();
        }
    }

    //热销 添加购物车
    private void showAddDialog(final ShowSearchBean.GoodsListBean bean, float x, float y) {
        xx = x;
        yy = y;
        if (bean.getSpec_type() == 0) {
            //不存在规格
            addShoppingCart(helper.getToken(), String.valueOf(bean.getGoods_id()), "", "", 1, bean.getSpec_type());
        } else {
            //存在规格 选择规格
            cartDialog = new SelectDialog(SearchListActivity.this, bean.getType());
            SpecDialogBean specDialogBean = new SpecDialogBean();
            specDialogBean.setGoods_id(bean.getGoods_id());
            specDialogBean.setImg(bean.getImg());
            specDialogBean.setSpec_name(bean.getGoods_name());
            specDialogBean.setPlus_price(Double.valueOf(bean.getPlus_price()));
            specDialogBean.setShop_price(Double.valueOf(bean.getShop_price()));
            cartDialog.setGoToUseOnclickListener(new SelectDialog.onGoToUseOnclickListener() {
                @Override
                public void onUseClick(String goods_id, String spec_id, String service_id, int num, int s
                        , double price, double vipPrice) {
                    if (s == 0) {
                        ToastUtil.showToast(SearchListActivity.this, "当前商品库存为0份");
                        return;
                    }
                    addShoppingCart(helper.getToken(), goods_id, service_id, spec_id, num, bean.getSpec_type());
                }
            });
            cartDialog.show();
            cartDialog.setData(specDialogBean);
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
    private void addShoppingCart(String token, String goods_id, String service_id, String spec_id, int goods_num, final int type) {
        HttpHelp.getInstance().create(RemoteApi.class).addCart(token, goods_id, service_id, spec_id, goods_num)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<Object>>(SearchListActivity.this, null) {
                    @Override
                    public void onNext(BaseBean<Object> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            shoppingCartCount(helper.getToken());
                            if (type == 0) {
                                int[] location = new int[2];
                                tvSearch.getLocationOnScreen(location);
                                classifyCircleMoveView.setVisibility(View.VISIBLE);
                                classifyCircleMoveView.setMovePath(xx, yy, location[0], location[1]);
                                classifyCircleMoveView.startAnim();
                                EventBusUtil.post(new ShoppingCartEvent());
                            }
                        } else if (baseBean.code == 4) {
                            ToolUtil.goToLogin(SearchListActivity.this);
                        } else {
                            ToastUtil.showToast(SearchListActivity.this, baseBean.message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);

                    }
                });
    }
}
