package com.guo.qlzx.maxiansheng.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.canyinghao.canrefresh.CanRefreshLayout;
import com.canyinghao.canrefresh.classic.ClassicRefreshView;
import com.guo.qlzx.maxiansheng.R;
import com.guo.qlzx.maxiansheng.activity.ClassificationTwoActivity;
import com.guo.qlzx.maxiansheng.activity.MainActivity;
import com.guo.qlzx.maxiansheng.activity.ProductDetailsActivity;
import com.guo.qlzx.maxiansheng.activity.SettlementActivity;
import com.guo.qlzx.maxiansheng.adapter.LikeAdapter;
import com.guo.qlzx.maxiansheng.adapter.ShoppingCartAdapter;
import com.guo.qlzx.maxiansheng.bean.CartListBean;
import com.guo.qlzx.maxiansheng.bean.GuessLikeBean;
import com.guo.qlzx.maxiansheng.bean.SpecDialogBean;
import com.guo.qlzx.maxiansheng.event.ChangeFragmentEvent;
import com.guo.qlzx.maxiansheng.event.ShoppingCartEvent;
import com.guo.qlzx.maxiansheng.event.ShoppingCartNumEvent;
import com.guo.qlzx.maxiansheng.http.RemoteApi;
import com.guo.qlzx.maxiansheng.util.ToolUtil;
import com.guo.qlzx.maxiansheng.util.costom.ClassifyCircleMoveView;
import com.guo.qlzx.maxiansheng.util.costom.SwipeItemLayout;
import com.guo.qlzx.maxiansheng.util.dialog.AddressDeleteDialog;
import com.guo.qlzx.maxiansheng.util.dialog.DeleteGoodsDialog;
import com.guo.qlzx.maxiansheng.util.dialog.DialogUtil;
import com.guo.qlzx.maxiansheng.util.dialog.SelectDialog;
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
import com.qlzx.mylibrary.widget.NoScrollGridView;
import com.qlzx.mylibrary.widget.ScrollLinearLayoutManager;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 购物车页面
 * Created by dillon on 2018/4/10.
 */

public class ShoppingCarFragment extends BaseFragment implements CanRefreshLayout.OnLoadMoreListener, CanRefreshLayout.OnRefreshListener, OnItemChildClickListener, AdapterView.OnItemClickListener, OnRVItemClickListener {

    Unbinder unbinder;
    @BindView(R.id.shop_goods_list)
    RecyclerView shopGoodsList;
    @BindView(R.id.can_content_view)
    NestedScrollView canContentView;
    @BindView(R.id.isAll)
    CheckBox isAll;
    @BindView(R.id.money)
    TextView money;
    @BindView(R.id.commodity)
    TextView commodity;
    @BindView(R.id.ll)
    RelativeLayout ll;
    @BindView(R.id.grid_like)
    NoScrollGridView gridLike;
    @BindView(R.id.can_refresh_header)
    ClassicRefreshView canRefreshHeader;
    @BindView(R.id.can_refresh_footer)
    ClassicRefreshView canRefreshFooter;
    @BindView(R.id.refresh)
    CanRefreshLayout refresh;
    @BindView(R.id.empty_list)
    RelativeLayout emptyList;
    @BindView(R.id.tv_skip)
    TextView tvSkip;
    @BindView(R.id.cm_circle)
    ClassifyCircleMoveView classifyCircleMoveView;
    private ShoppingCartAdapter shoppingCartAdapter;
    private List<CartListBean.ShowcartListBean> showcartList = new ArrayList<>();
    private int goods_num;
    private float increment;
    private float unit_price;
    private float zPrice;
    private int page = 1;
    private List<GuessLikeBean> liksBean = new ArrayList<>();
    private LikeAdapter likeAdapter;

    private SelectDialog cartDialog;

    private float xx=0f;
    private float yy=0f;
    private float height=0f;
    private  float width=0f;

    @Override
    public View getContentView(FrameLayout frameLayout) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_shoppingcart, frameLayout, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void initView() {
        titleBar.setVisibility(View.VISIBLE);
        titleBar.setTitleText("购物车");
        titleBar.setLeftGone();
        titleBar.setRightTextRes("删除");
        titleBar.getRl_title().setBackgroundColor(getResources().getColor(R.color.colorAccent));
        titleBar.getTvRight().setTextColor(Color.WHITE);
        titleBar.getTitle().setTextColor(Color.WHITE);
        titleBar.setRightTextClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ifTiaoTrue()) {
                    showD();
                } else {
                    ToastUtil.showToast(mContext, "请选择要删除的商品");
                }
            }
        });

        EventBusUtil.register(this);

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

        loadingLayout.setStatus(LoadingLayout.Success);

        shoppingCartAdapter = new ShoppingCartAdapter(shopGoodsList);
        shoppingCartAdapter.setOnItemChildClickListener(this);
        ScrollLinearLayoutManager manager =   new ScrollLinearLayoutManager(mContext);
        manager.setStackFromEnd(true);
        shopGoodsList.setLayoutManager(manager);
        shopGoodsList.setAdapter(shoppingCartAdapter);


        likeAdapter = new LikeAdapter(gridLike);
        gridLike.setAdapter(likeAdapter);
        gridLike.setOnItemClickListener(this);
        gridLike.setFocusable(false);
        DisplayMetrics dm2 = getResources().getDisplayMetrics();
        height=dm2.heightPixels;
       width=dm2.widthPixels;
        likeAdapter.setOnclickListener(new LikeAdapter.OnAddCartOnclickListener() {
            @Override
            public void onClick(final int position,float x,float y) {
                xx=x;
                yy=y;
                GuessLikeBean guessLikeBean = likeAdapter.getData().get(position);

                if (likeAdapter.getData().get(position).getSpec_type() == 0) {
                    //不存在规格
                 addShoppingCart(new PreferencesHelper(mContext).getToken(), String.valueOf(guessLikeBean.getGoods_id()), "", "", 1,position);
                } else {
                    //存在规格 选择规格
                    cartDialog = new SelectDialog(mContext,guessLikeBean.getType());
                    SpecDialogBean specDialogBean = new SpecDialogBean();
                    specDialogBean.setGoods_id(guessLikeBean.getGoods_id());
                    specDialogBean.setImg(guessLikeBean.getImg());
                    specDialogBean.setSpec_name(guessLikeBean.getSpec_key_name());
                    specDialogBean.setPlus_price(guessLikeBean.getPlus_price());
                    specDialogBean.setShop_price(guessLikeBean.getShop_price());
                    cartDialog.setGoToUseOnclickListener(new SelectDialog.onGoToUseOnclickListener() {
                        @Override
                        public void onUseClick(String goods_id, String spec_id, String service_id, int num,int s
                                ,double price,double vipPrice) {
                            if(s == 0){
                                ToastUtil.showToast(getActivity(),"当前商品库存为0份");
                                return;
                            }
                            addShoppingCart(new PreferencesHelper(mContext).getToken(), goods_id, service_id, spec_id, num,position);
                        }
                    });
                    cartDialog.show();
                    cartDialog.setData(specDialogBean);
                }


            }
        });
        shoppingCartAdapter.setOnRVItemClickListener(this);
        shopGoodsList.addOnItemTouchListener(new SwipeItemLayout.OnSwipeItemTouchListener(getActivity()));
        shoppingCartAdapter.setUpDataNum(new ShoppingCartAdapter.UpDataNum() {
            @Override
            public void getNum(int num,int position) {
                if (shoppingCartAdapter.getItemCount()==0){
                    return;
                }
                 numCart(
                        new PreferencesHelper(mContext).getToken(),
                        shoppingCartAdapter.getItem(position).getId(),
                        num,
                        String.valueOf(shoppingCartAdapter.getItem(position).getSelected()),
                        position, shoppingCartAdapter.getItem(position).getGoods_num());

            }

            @Override
            public void getPosition(final int position) {
                canContentView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        canContentView.scrollTo(0,(position)*DensityUtil.dp2px(getActivity(), 150));
                    }
                },500);

            }
        });
        tvSkip.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG );
    }

    /**
     * 删除的弹框
     */
    private void showD() {
        final DeleteGoodsDialog dialog = new DeleteGoodsDialog(mContext);
        dialog.setOnGoToUseOnclickListener(new DeleteGoodsDialog.onGoToUseOnclickListener() {
            @Override
            public void onUseClick() {
                dialog.dismiss();
                delGoods();
            }
        });
        dialog.show();
    }


    @Override
    public void getData() {
        getGuessLikeData(page);
        cart_list(new PreferencesHelper(mContext).getToken());
    }

    private void cart_list(String token) {
        HttpHelp.getInstance().create(RemoteApi.class).cart_list(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<CartListBean>>(mContext, null) {
                    @Override
                    public void onNext(BaseBean<CartListBean> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            if (baseBean.data != null) {
                                money.setText("￥" + baseBean.data.getTotal_fee());
                                commodity.setText("结算(" + baseBean.data.getGoods_num() + ")");
                                showcartList = baseBean.data.getShowcartList();
                                if (showcartList != null && showcartList.size() > 0) {
                                    shoppingCartAdapter.setData(showcartList);
                                    shopGoodsList.setVisibility(View.VISIBLE);
                                    emptyList.setVisibility(View.GONE);
                                    isAll.setEnabled(true);
                                    isAll.setChecked(ifAllTrue());
                                } else {
                                    shoppingCartAdapter.clear();
                                    shopGoodsList.setVisibility(View.GONE);
                                    emptyList.setVisibility(View.VISIBLE);
                                    isAll.setChecked(false);
                                    isAll.setEnabled(false);
                                }

                                for (CartListBean.ShowcartListBean b : showcartList) {
                                    LogUtil.i("购物车——id:" + b.getId());
                                }

                            }
                        } else if (baseBean.code == 4) {
                            //账号失效  清空数据
                            shoppingCartAdapter.clear();
                            shopGoodsList.setVisibility(View.GONE);
                            emptyList.setVisibility(View.VISIBLE);
                            isAll.setChecked(false);
                            isAll.setEnabled(false);
                            if (TextUtils.isEmpty(new PreferencesHelper(mContext).getToken())){
                                ToolUtil.goToLogin(mContext);
                            }else {
                                ToolUtil.loseToLogin(mContext);
                            }
                            money.setText("￥0");
                            commodity.setText("结算");

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


    @OnClick({R.id.isAll, R.id.commodity,R.id.tv_skip})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.isAll:

                if (isAll.isChecked()) {
//                    setIsAll(1);
                    //全选
                    choiceGoods(new PreferencesHelper(mContext).getToken(), 0, "1", 1, -1);
                } else {
//                    setIsAll(0);
                    //返选
                    choiceGoods(new PreferencesHelper(mContext).getToken(), 0, "0", 2, -1);
                }

//                shoppingCartAdapter.notifyDataSetChanged();
//
//                commodity.setText("结算(" + getNum() + ")");
//
//                getZPrice();


                break;

            case R.id.commodity:
                if (ifTiaoTrue()) {
                    settlement(new PreferencesHelper(mContext).getToken());
                    Intent intent = new Intent(mContext, SettlementActivity.class);
                    startActivity(intent);
                } else {
                    ToastUtil.showToast(mContext, "请选择要结算的商品");
                }
                break;
            case R.id.tv_skip:
                EventBusUtil.post(new ChangeFragmentEvent());
                break;
        }
    }


    @Override
    public void onLoadMore() {
        page++;
        getGuessLikeData(page);
    }

    @Override
    public void onRefresh() {
        page = 1;
        getData();

    }


    /**
     * 猜你喜欢
     *
     * @param page
     */
    private void getGuessLikeData(final int page) {
        HttpHelp.getInstance().create(RemoteApi.class).getGuessLikeData(page, "1")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<List<GuessLikeBean>>>(mContext, null) {
                    @Override
                    public void onNext(BaseBean<List<GuessLikeBean>> baseBean) {
                        super.onNext(baseBean);
                        liksBean = baseBean.data;
                        refresh.loadMoreComplete();
                        refresh.refreshComplete();
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
                                    ToastUtil.showToast(mContext, "暂无更多");
                                }
                            }
                        } else {
                            ToastUtil.showToast(mContext, baseBean.message);
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
    public void onItemChildClick(ViewGroup parent, View childView, final int position) {
        if (R.id.shop_goods_list == parent.getId()) {

            switch (childView.getId()) {
                //   删除
                case R.id.btn_delete:
                    numCart(new PreferencesHelper(mContext).getToken(),
                            shoppingCartAdapter.getItem(position).getId(),
                            0,
                            "1",
                            position, shoppingCartAdapter.getItem(position).getGoods_num());
                    break;
                    // 跳到详情页
                case R.id.rl_goodstotal:
                    Intent intent = new Intent(mContext, ProductDetailsActivity.class);
                    intent.putExtra("goods_id", "" + shoppingCartAdapter.getItem(position).getGoods_id());
                    mContext.startActivity(intent);
                    break;
                // 跳到详情页
                case R.id.value_added_service:
                    Intent intentservice = new Intent(mContext, ProductDetailsActivity.class);
                    intentservice.putExtra("goods_id", "" + shoppingCartAdapter.getItem(position).getGoods_id());
                    mContext.startActivity(intentservice);
                    break;
                // 跳到详情页
                case R.id.ll__item_shopcar:
                    Intent intentshopcar = new Intent(mContext, ProductDetailsActivity.class);
                    intentshopcar.putExtra("goods_id", "" + shoppingCartAdapter.getItem(position).getGoods_id());
                    mContext.startActivity(intentshopcar);
                    break;

                case R.id.goods_add:

                    /*shoppingCartAdapter.getData().get(position).setGoods_num((shoppingCartAdapter.getData().get(position).getGoods_num() + 1));
                    shoppingCartAdapter.notifyDataSetChanged();
                    commodity.setText("结算(" + getNum() + ")");
                    getZPrice();*/
                    if(shoppingCartAdapter.getItem(position).getStore_count()<(shoppingCartAdapter.getItem(position).getGoods_num() + 1)){
                        ToastUtil.showToast(getActivity(),"不可超过最大库存");
                        return;
                    }
                    numCart(new PreferencesHelper(mContext).getToken(),
                            shoppingCartAdapter.getItem(position).getId(),
                            shoppingCartAdapter.getItem(position).getGoods_num() + 1,
                            String.valueOf(shoppingCartAdapter.getItem(position).getSelected()),
                            position, shoppingCartAdapter.getItem(position).getGoods_num());

                    break;

                case R.id.goods_reduce:
                    if (shoppingCartAdapter.getData().get(position).getGoods_num() == 1) {
                        AddressDeleteDialog dialog=new AddressDeleteDialog(mContext);
                        dialog.setGoToUseOnclickListener(new AddressDeleteDialog.onGoToUseOnclickListener() {
                            @Override
                            public void onUseClick() {
                                numCart(new PreferencesHelper(mContext).getToken(),
                                        shoppingCartAdapter.getItem(position).getId(),
                                        shoppingCartAdapter.getItem(position).getGoods_num() - 1,
                                        String.valueOf(shoppingCartAdapter.getItem(position).getSelected()),
                                        position, shoppingCartAdapter.getItem(position).getGoods_num());
                            }
                        });
                        dialog.show();
                        dialog.setTitle("确定要删除选中商品吗？");
                        return;
                    }

                        /*shoppingCartAdapter.getData().get(position).setGoods_num((shoppingCartAdapter.getData().get(position).getGoods_num() - 1));
                        shoppingCartAdapter.notifyDataSetChanged();*/

                    numCart(new PreferencesHelper(mContext).getToken(),
                            shoppingCartAdapter.getItem(position).getId(),
                            shoppingCartAdapter.getItem(position).getGoods_num() - 1,
                            String.valueOf(shoppingCartAdapter.getItem(position).getSelected()),
                            position, shoppingCartAdapter.getItem(position).getGoods_num());
//                    }

                    /*commodity.setText("结算(" + getNum() + ")");
                    getZPrice();*/

                    break;

                case R.id.id_cb_select_child:

                    /*if (shoppingCartAdapter.getData().get(position).getSelected() == 1) {
                        shoppingCartAdapter.getData().get(position).setSelected(0);
                    } else {
                        shoppingCartAdapter.getData().get(position).setSelected(1);
                    }
                    shoppingCartAdapter.notifyDataSetChanged();
                    isAll.setChecked(ifAllTrue());
                    commodity.setText("结算(" + getNum() + ")");
                    getZPrice();*/


                    if (1 == shoppingCartAdapter.getItem(position).getSelected()) {
                        choiceGoods(new PreferencesHelper(mContext).getToken(),
                                shoppingCartAdapter.getItem(position).getId(),
                                "0", 0, position);
                    } else if (0 == shoppingCartAdapter.getItem(position).getSelected()) {
                        choiceGoods(new PreferencesHelper(mContext).getToken(),
                                shoppingCartAdapter.getItem(position).getId(),
                                "1", 0, position);
                    }


                    break;
            }
        }
    }

    /**
     * 判断是否有选中的条目
     */
    private boolean ifTiaoTrue() {
        for (int i = 0; i < shoppingCartAdapter.getData().size(); i++) {
            //获取该商品的库存
            CartListBean.ShowcartListBean item = shoppingCartAdapter.getItem(i);
            int store_count = item.getStore_count();
            final int goods_num = item.getGoods_num();
            if (store_count<goods_num){
                final int finalI = i;
                DialogUtil.showAlert(
                        getActivity(),
                        "提示",
                        "商品数量大于商家库存是否将数量改为该商品库存的最大值?",
                        "确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //点击确定
                                shoppingCartAdapter.getItem(finalI).setGoods_num(shoppingCartAdapter.getItem(finalI).getStore_count());
                                shoppingCartAdapter.notifyDataSetChanged();
                                numCart(new PreferencesHelper(mContext).getToken(),
                                        shoppingCartAdapter.getItem(finalI).getId(),
                                        shoppingCartAdapter.getItem(finalI).getStore_count(),
                                        String.valueOf(shoppingCartAdapter.getItem(finalI).getSelected()),
                                        finalI, shoppingCartAdapter.getItem(finalI).getGoods_num());
                                //commodity.setText("结算(" + getNum() + ")");
                            }
                        },
                        "",
                        null,
                        "取消",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (dialog!=null)dialog.dismiss();
                            }
                        },
                        null,
                        false,
                        null,
                        null);
                return false;
            }
        }
        for (int i = 0; i < shoppingCartAdapter.getData().size(); i++) {
            if (shoppingCartAdapter.getData().get(i).getSelected() == 1) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否全选
     */
    private boolean ifAllTrue() {
        for (int i = 0; i < shoppingCartAdapter.getData().size(); i++) {
            if (shoppingCartAdapter.getData().get(i).getSelected() == 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * 设置权限返选
     *
     * @param isAll
     */
    private void setIsAll(int isAll) {
        for (int i = 0; i < shoppingCartAdapter.getData().size(); i++) {
            shoppingCartAdapter.getData().get(i).setSelected(isAll);
        }
    }

    /**
     * 选择了几样商品
     */
    private int getNum() {
        goods_num = 0;
        for (int i = 0; i < shoppingCartAdapter.getData().size(); i++) {
            if (shoppingCartAdapter.getData().get(i).getSelected() == 1) {
                goods_num += shoppingCartAdapter.getData().get(i).getGoods_num();
            }
        }
        return goods_num;
    }

    /**
     * 删除商品
     */
    private void delGoods() {
//        for (int i = 0; i < shoppingCartAdapter.getData().size(); i++) {
//            if (shoppingCartAdapter.getData().get(i).getSelected() == 1) {
//                /**此处调用删除接口*/
//
//            }
//        }
//        shoppingCartAdapter.notifyDataSetChanged();

        HttpHelp.getInstance().create(RemoteApi.class).delCartGoods(new PreferencesHelper(mContext).getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<Object>>(mContext, null) {
                    @Override
                    public void onNext(BaseBean<Object> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            //刷新数据
                            cart_list(new PreferencesHelper(mContext).getToken());
                            EventBusUtil.post(new ShoppingCartNumEvent());
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

    private void settlement(String token){
        HttpHelp.getInstance().create(RemoteApi.class).settlement(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<Object>>(mContext, null) {
                    @Override
                    public void onNext(BaseBean<Object> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
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
    /**
     * 选择商品
     *
     * @param token
     * @param id       购物车id
     * @param selected 最终状态 0未选  1已选
     * @param type     0(默认) 1全选 2反选
     * @[param position 类表中的位置 -1 全选或反选
     */
    private void choiceGoods(String token, int id, final String selected, int type, final int position) {

        showLoadingDialog("");
        HttpHelp.getInstance().create(RemoteApi.class).choiceCartGoods(token, id, selected, type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<Object>>(mContext, null) {
                    @Override
                    public void onNext(BaseBean<Object> baseBean) {
                        hideLoadingDialog();
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            if (position != -1) {

                                shoppingCartAdapter.getData().get(position).setSelected(Integer.parseInt(selected));

                                shoppingCartAdapter.notifyDataSetChanged();

                                isAll.setChecked(ifAllTrue());

                                commodity.setText("结算(" + getNum() + ")");

                                getZPrice();
                            } else {
                                setIsAll(Integer.parseInt(selected));
                                shoppingCartAdapter.notifyDataSetChanged();
                                commodity.setText("结算(" + getNum() + ")");
                                getZPrice();
                            }
                        } else {
                            ToastUtil.showToast(mContext, baseBean.message);
                            if (position != -1) {
                                shoppingCartAdapter.notifyDataSetChanged();
                            } else {
                                isAll.setChecked(!isAll.isChecked());
                            }
                        }
                        EventBusUtil.post(new ShoppingCartNumEvent());
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        hideLoadingDialog();
                        super.onError(throwable);

                    }
                });

    }

    /**
     * 购物车加减数量
     *
     * @param token
     * @param id
     * @param num      变化后的数量
     * @param selected
     * @[param position 类表中的位置
     * @[param preNum 之前的数量
     */
    private void numCart(String token, int id, final int num, String selected, final int position, int preNum) {
        HttpHelp.getInstance().create(RemoteApi.class).numCart(token, id, num, selected)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<Object>>(mContext, null) {
                    @Override
                    public void onNext(BaseBean<Object> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            shoppingCartAdapter.getData().get(position).setGoods_num(num);
                            shoppingCartAdapter.notifyItemChanged(position);
                            commodity.setText("结算(" + getNum() + ")");
                            getZPrice();
                            EventBusUtil.post(new ShoppingCartNumEvent());
                        }if (baseBean.code==4){
                            if (TextUtils.isEmpty(new PreferencesHelper(mContext).getToken())){
                                ToolUtil.goToLogin(mContext);
                            }else {
                                ToolUtil.loseToLogin(mContext);
                            }
                        }else if (baseBean.code == 13) {//删除
                            cart_list(new PreferencesHelper(mContext).getToken());
                            shopGoodsList.removeAllViews();
                            EventBusUtil.post(new ShoppingCartNumEvent());
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
     * 选择的商品总价
     */
    private void getZPrice() {
        increment = 0;
        unit_price = 1;
        zPrice = 0;
        for (int i = 0; i < shoppingCartAdapter.getData().size(); i++) {
            if (shoppingCartAdapter.getData().get(i).getSelected() == 1) {
                if (shoppingCartAdapter.getData().get(i).getService_id() == 0) {

                } else {
                    increment = Float.parseFloat(shoppingCartAdapter.getData().get(i).getService_price());
                }
                if (!TextUtils.isEmpty(shoppingCartAdapter.getData().get(i).getMember_goods_price())) {
                    unit_price = Float.parseFloat(shoppingCartAdapter.getData().get(i).getMember_goods_price());
                } else {
                    unit_price = Float.parseFloat(shoppingCartAdapter.getData().get(i).getGoods_price());
                }

                zPrice += increment * shoppingCartAdapter.getData().get(i).getGoods_num() + unit_price * shoppingCartAdapter.getData().get(i).getGoods_num();
            }
        }
        DecimalFormat decimalFormat = new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
        String resultString = decimalFormat.format(zPrice);//format 返回的是字符串
        money.setText("￥" + resultString);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /**
     * 猜你喜欢  点击事件
     *
     * @param view
     */

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        String goodsId = String.valueOf(likeAdapter.getData().get(i).getGoods_id());
        Intent intent = new Intent(mContext, ProductDetailsActivity.class);
        intent.putExtra("goods_id", goodsId);
        startActivity(intent);
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
    private void addShoppingCart(String token, String goods_id, String service_id, String spec_id, int goods_num,final int position) {
        HttpHelp.getInstance().create(RemoteApi.class).addCart(token, goods_id, service_id, spec_id, goods_num)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<Object>>(mContext, null) {
                    @Override
                    public void onNext(BaseBean<Object> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            if (likeAdapter.getData().get(position).getSpec_type() == 0) {
                                //不存在规格
                                classifyCircleMoveView.setVisibility(View.VISIBLE);
                                classifyCircleMoveView.setMovePath(xx,yy,(width/8)*5,height-100);
                                classifyCircleMoveView.startAnim();
                            }
                            cart_list(new PreferencesHelper(mContext).getToken());
                            EventBusUtil.post(new ShoppingCartNumEvent());
                            EventBusUtil.post(new ShoppingCartEvent());
                        }if (baseBean.code==4){
                            if (TextUtils.isEmpty(new PreferencesHelper(mContext).getToken())){
                                ToolUtil.goToLogin(mContext);
                            }else {
                                ToolUtil.loseToLogin(mContext);
                            }
                        } else {
                           // ToastUtil.showToast(mContext, baseBean.message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);

                    }
                });

    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBusUtil.unregister(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void upPassWord(ShoppingCartEvent event) {
        cart_list(new PreferencesHelper(mContext).getToken());

    }

    @Override
    public void onRVItemClick(ViewGroup parent, View itemView, int position) {
        Intent intent = new Intent(mContext, ProductDetailsActivity.class);
        intent.putExtra("goods_id", "" + shoppingCartAdapter.getItem(position).getGoods_id());
        mContext.startActivity(intent);
    }

}
