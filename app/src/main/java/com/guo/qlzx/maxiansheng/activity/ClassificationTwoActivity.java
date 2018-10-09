package com.guo.qlzx.maxiansheng.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.canyinghao.canrefresh.CanRefreshLayout;
import com.canyinghao.canrefresh.classic.ClassicRefreshView;
import com.guo.qlzx.maxiansheng.R;
import com.guo.qlzx.maxiansheng.adapter.ClassficationGoodsAdapter;
import com.guo.qlzx.maxiansheng.adapter.FlowStyleAdapter;
import com.guo.qlzx.maxiansheng.adapter.LikeAdapter;
import com.guo.qlzx.maxiansheng.adapter.OneClassificationAdapter;
import com.guo.qlzx.maxiansheng.adapter.ThreeClasssficationAdapter;
import com.guo.qlzx.maxiansheng.adapter.TwoClassificationAdapter;
import com.guo.qlzx.maxiansheng.bean.ClassficationGoodsBean;
import com.guo.qlzx.maxiansheng.bean.ClassificationBean;
import com.guo.qlzx.maxiansheng.bean.SpecDialogBean;
import com.guo.qlzx.maxiansheng.bean.TwoCategoryBean;
import com.guo.qlzx.maxiansheng.event.ShoppingCartEvent;
import com.guo.qlzx.maxiansheng.http.RemoteApi;
import com.guo.qlzx.maxiansheng.util.MeasureFlowLayoutManager;
import com.guo.qlzx.maxiansheng.util.SpaceItemDecoration;
import com.guo.qlzx.maxiansheng.util.ToolUtil;
import com.guo.qlzx.maxiansheng.util.costom.ClassifyCircleMoveView;
import com.guo.qlzx.maxiansheng.util.dialog.SelectDialog;
import com.guo.qlzx.maxiansheng.view.PopWindow;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.base.OnRVItemClickListener;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.DensityUtil;
import com.qlzx.mylibrary.util.EventBusUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.StringUtil;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.qlzx.mylibrary.widget.NoScrollGridView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/4/16.
 * 二级分类页面
 */

public class ClassificationTwoActivity extends BaseActivity implements CanRefreshLayout.OnLoadMoreListener, CanRefreshLayout.OnRefreshListener {


    Unbinder unbinder;
    @BindView(R.id.class_break)
    ImageView classBreak;
    @BindView(R.id.class_title)
    TextView classTitle;
    @BindView(R.id.class_pic_gwc)
    ImageView classPicGwc;
    @BindView(R.id.class_pic_gosuo)
    ImageView classPicGosuo;
    @BindView(R.id.class_twe)
    ListView classTwe;
    @BindView(R.id.view_fgx)
    View viewFgx;
    @BindView(R.id.im_xiala)
    ImageView imXiala;
    @BindView(R.id.class_three)
    RelativeLayout classThree;
    //    @BindView(R.id.class_tow_name)
//    TextView classTowName;
    @BindView(R.id.can_content_view)
    ListView classGoodsList;
    @BindView(R.id.waitao)
    RelativeLayout waitao;
    @BindView(R.id.hor_recycler)
    RecyclerView horRecycler;
    @BindView(R.id.can_refresh_header)
    ClassicRefreshView canRefreshHeader;
    @BindView(R.id.can_refresh_footer)
    ClassicRefreshView canRefreshFooter;
    @BindView(R.id.refresh)
    CanRefreshLayout refresh;
    @BindView(R.id.empty_list)
    RelativeLayout emptyList;
    @BindView(R.id.shop_number)
    TextView shopNumber;
    @BindView(R.id.iv_title)
    ImageView ivTitle;
    @BindView(R.id.cm_circle)
    ClassifyCircleMoveView classifyCircleMoveView;
    private PreferencesHelper helper;
    private String name;
    private String id;
    private PopupWindow popupWindowOne;
    private PopWindow popupWindowThree;

    SelectDialog cartDialog;//加购物车dialog

    HashMap<String,List<ClassficationGoodsBean>> hashMap=new HashMap<>();

    //一级
    private List<ClassificationBean.ListBean> listBean = new ArrayList<>();
    private OneClassificationAdapter adapter;
    //二级
    private TwoClassificationAdapter twoClassificationAdapter;
    private List<TwoCategoryBean> twoCategoryList = new ArrayList<>();
    //三级
    private ThreeClasssficationAdapter threeClasssficationAdapter;
    private FlowStyleAdapter flowStyleAdapter;
    private List<TwoCategoryBean.SonBean> sonList = new ArrayList<>();
    //商品
    private ClassficationGoodsAdapter goodsAdapter;
    private List<ClassficationGoodsBean> goodsList = new ArrayList<>();
    private int widths;
    private int width;

    private int page = 1;
    private String cartId3, cartId2;


    private int first = 0;
    private float xx = 0f;
    private float yy = 0f;
    /**
     * 当前二级列表的标识
     */
    private int position = 0;

    @Override
    public int getContentView() {
        return R.layout.activity_classification_tow;
    }

    @Override
    public void initView() {
        loadingLayout.setStatus(LoadingLayout.Success);
        unbinder = ButterKnife.bind(ClassificationTwoActivity.this);
        helper = new PreferencesHelper(ClassificationTwoActivity.this);
        hideTitleBar();

        name = getIntent().getStringExtra("name");
        id = getIntent().getStringExtra("id");

        classTitle.setText(name);

        refresh.setOnLoadMoreListener(this);
        refresh.setOnRefreshListener(this);

        refresh.setMaxFooterHeight(DensityUtil.dp2px(ClassificationTwoActivity.this, 100));
        refresh.setStyle(0, 0);
        canRefreshHeader.setPullStr("下拉刷新");
        canRefreshHeader.setReleaseStr("松开刷新");
        canRefreshHeader.setRefreshingStr("加载中");
        canRefreshHeader.setCompleteStr("");

        canRefreshFooter.setPullStr("上拉加载更多");
        canRefreshFooter.setReleaseStr("松开刷新");
        canRefreshFooter.setRefreshingStr("加载中");
        canRefreshFooter.setCompleteStr("");

        twoClassificationAdapter = new TwoClassificationAdapter(classTwe);
        classTwe.setAdapter(twoClassificationAdapter);
        /**
         * 二级列表的点击事件
         */
        classTwe.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /**
                 *
                 */
                setFalse();
                setThreeFalse();
                ClassificationTwoActivity.this.position=position;
                twoClassificationAdapter.getData().get(position).setTrue(true);
                twoClassificationAdapter.notifyDataSetChanged();
//                classTowName.setText(twoClassificationAdapter.getData().get(position).getName());
                sonList = twoClassificationAdapter.getData().get(position).getSon();

                updateThree(sonList, twoClassificationAdapter.getData().get(position).getId(),position);



            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        horRecycler.setLayoutManager(linearLayoutManager);
        horRecycler.addItemDecoration(new SpaceItemDecoration((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics())));
        threeClasssficationAdapter = new ThreeClasssficationAdapter(horRecycler);
        horRecycler.setAdapter(threeClasssficationAdapter);
        threeClasssficationAdapter.setOnRVItemClickListener(new OnRVItemClickListener() {
            @Override
            public void onRVItemClick(ViewGroup parent, View itemView, int position) {
                setThreeFalse();
                page = 1;
                threeClasssficationAdapter.getItem(position).setSelect(true);
                String id = threeClasssficationAdapter.getItem(position).getId();
                if (hashMap.containsKey(id)) {
                    goodsAdapter.setData(hashMap.get(id));
                }else {
                    goods(threeClasssficationAdapter.getItem(position).getId(), threeClasssficationAdapter.getItem(position).getParent_id(), page);
                }
                threeClasssficationAdapter.notifyDataSetChanged();
            }
        });

        goodsAdapter = new ClassficationGoodsAdapter(classGoodsList);
        classGoodsList.setAdapter(goodsAdapter);
        classGoodsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //跳详情
                Intent intent = new Intent(ClassificationTwoActivity.this, ProductDetailsActivity.class);
                intent.putExtra("goods_id", goodsAdapter.getData().get(i).getGoods_id());
                startActivity(intent);
            }
        });
        goodsAdapter.setOnclickListener(new ClassficationGoodsAdapter.OnAddCartOnclickListener() {
            @Override
            public void onClick(int position, float x, float y) {

                showAddDialog(goodsAdapter.getData().get(position), x, y);
            }
        });


        ViewTreeObserver vtos = classThree.getViewTreeObserver();
        vtos.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                widths = classThree.getWidth();
                return true;
            }
        });

    }

    private void setFalse() {
        for (int i = 0; i < twoClassificationAdapter.getData().size(); i++) {
            twoClassificationAdapter.getData().get(i).setTrue(false);
        }
    }

    private void setThreeFalse() {
        for (int i = 0; i < threeClasssficationAdapter.getData().size(); i++) {
            threeClasssficationAdapter.getData().get(i).setSelect(false);
        }

    }



    /**
     * 更新三级列表
     *
     * @param list
     */
    private void updateThree(List<TwoCategoryBean.SonBean> list, String classTwoId,int position) {
        page = 1;
        if (list != null && list.size() > 0) {
            classThree.setVisibility(View.VISIBLE);
//            list.get(0).setSelect(true);
            threeClasssficationAdapter.setData(list);
            if (hashMap.containsKey(list.get(0).getParent_id())){
                goodsAdapter.setData(hashMap.get(list.get(0).getParent_id()));
                classGoodsList.scrollTo((int) classTwe.getX(),0);
            }else {
                goods(list.get(0).getId(), list.get(0).getParent_id(), page,position);
            }
        } else {

            classThree.setVisibility(View.GONE);
            threeClasssficationAdapter.clear();
            goodsAdapter.clear();
            if (hashMap.containsKey(classTwoId)){
                goodsAdapter.setData(hashMap.get(classTwoId));
                classGoodsList.scrollTo((int) classTwe.getX(),0);
            }else {
                goods("", classTwoId, page,position);
            }
        }

    }
    /**
     * 更新三级列表
     *
     * @param list
     */
    private void updateThree(List<TwoCategoryBean.SonBean> list, String classTwoId) {
        page = 1;
        if (list != null && list.size() > 0) {
            classThree.setVisibility(View.VISIBLE);
            list.get(0).setSelect(true);
            threeClasssficationAdapter.setData(list);
            goods(list.get(0).getId(), list.get(0).getParent_id(), page);
        } else {
            classThree.setVisibility(View.GONE);
            threeClasssficationAdapter.clear();
            goodsAdapter.clear();
            goods("", classTwoId, page);
        }
    }

    @Override
    public void getData() {
        top_category("1");
        two_category(id);
        shoppingCartCount(helper.getToken());
    }

    /**
     * 一级
     *
     * @param type
     */
    private void top_category(String type) {
        HttpHelp.getInstance().create(RemoteApi.class).top_category(type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<ClassificationBean>>(ClassificationTwoActivity.this, null) {
                    @Override
                    public void onNext(BaseBean<ClassificationBean> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            if (baseBean.data != null) {
                                listBean.clear();
                                listBean = baseBean.data.getList();
                                adapter.setData(listBean);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);

                    }
                });

    }

    /**
     * 二级
     *
     * @param category_id
     */
    private void two_category(String category_id) {
        HttpHelp.getInstance().create(RemoteApi.class).two_category(category_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<List<TwoCategoryBean>>>(ClassificationTwoActivity.this, null) {
                    @Override
                    public void onNext(BaseBean<List<TwoCategoryBean>> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            if (baseBean.data != null) {
                                twoCategoryList.clear();
                                twoCategoryList = baseBean.data;
                                if (twoCategoryList != null && twoCategoryList.size() > 0) {
                                    sonList = twoCategoryList.get(0).getSon();
//                                    classTowName.setText(twoCategoryList.get(0).getName());
                                    twoCategoryList.get(position).setTrue(true);
                                    twoClassificationAdapter.setData(twoCategoryList);
                                    updateThree(sonList, twoCategoryList.get(0).getId(),position);

                                }
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        twoCategoryList.clear();
                    }
                });

    }

    /**
     * 请求商品
     *
     * @param cat_id3
     * @param cat_id2
     * @param page
     */
    private void goods(String cat_id3, final String cat_id2, final int page, final int positions) {
        cartId3 = cat_id3;
        cartId2 = cat_id2;
        HttpHelp.getInstance().create(RemoteApi.class).classficationGoods(cat_id3, cat_id2, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<List<ClassficationGoodsBean>>>(ClassificationTwoActivity.this, null) {
                    @Override
                    public void onNext(BaseBean<List<ClassficationGoodsBean>> baseBean) {
                        super.onNext(baseBean);
                        refresh.loadMoreComplete();
                        refresh.refreshComplete();
                        if (baseBean.code == 0) {
                            goodsList = baseBean.data;
                            if (page == 1) {
                                if (goodsList != null && goodsList.size() > 0) {
                                    if (twoClassificationAdapter.getData().get(positions).isTrue()){
                                        emptyList.setVisibility(View.GONE);
                                        goodsAdapter.setData(goodsList);
                                        hashMap.put(cat_id2,goodsList);
                                    }
                                } else {
                                    emptyList.setVisibility(View.VISIBLE);
                                    goodsAdapter.clear();
                                }
                            } else {
                                if (goodsList != null && goodsList.size() > 0) {
                                    goodsAdapter.addMoreData(goodsList);
                                    List<ClassficationGoodsBean> classficationGoodsBeans = hashMap.get(cat_id2);
                                    classficationGoodsBeans.addAll(goodsList);
                                    hashMap.put(cat_id2,classficationGoodsBeans);
                                } else {
                                    ToastUtil.showToast(ClassificationTwoActivity.this, "没有更多!");
                                }
                            }

                        } else if (baseBean.code == 1) {
                            twoCategoryList.get(position).setTrue(false);
                            if (position == threeClasssficationAdapter.getData().size() - 1) {
                                position = 0;
                                twoCategoryList.get(position).setTrue(true);
                                sonList = twoClassificationAdapter.getData().get(position).getSon();
                                updateThree(sonList, twoClassificationAdapter.getData().get(position).getId(),position);
                            } else {
                                position++;
                                twoCategoryList.get(position).setTrue(true);
                                sonList = twoClassificationAdapter.getData().get(position).getSon();
                                updateThree(sonList, twoClassificationAdapter.getData().get(position).getId(),position);
                            }
                            twoClassificationAdapter.notifyDataSetChanged();
                        }


                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        if (refresh != null) {
                            refresh.loadMoreComplete();
                            refresh.refreshComplete();
                        }
                    }
                });
    }

    /**
     * 请求商品
     *
     * @param cat_id3
     * @param cat_id2
     * @param page
     */
    private void goods(final String cat_id3, final String cat_id2, final int page) {
        cartId3 = cat_id3;
        cartId2 = cat_id2;
        HttpHelp.getInstance().create(RemoteApi.class).classficationGoods(cat_id3, cat_id2, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<List<ClassficationGoodsBean>>>(ClassificationTwoActivity.this, null) {
                    @Override
                    public void onNext(BaseBean<List<ClassficationGoodsBean>> baseBean) {
                        super.onNext(baseBean);
                        refresh.loadMoreComplete();
                        refresh.refreshComplete();
                        if (baseBean.code == 0) {
                            goodsList = baseBean.data;
                            if (page == 1) {
                                if (goodsList != null && goodsList.size() > 0) {
                                    if (twoClassificationAdapter.getData().get(0).isTrue()){
                                        emptyList.setVisibility(View.GONE);
                                        goodsAdapter.setData(goodsList);
                                        hashMap.put(StringUtil.isEmpty(cat_id3)?cat_id2:cat_id3,goodsList);

                                    }
                                } else {
                                    emptyList.setVisibility(View.VISIBLE);
                                    goodsAdapter.clear();
                                }

                            } else {
                                if (goodsList != null && goodsList.size() > 0) {
                                    goodsAdapter.addMoreData(goodsList);

                                } else {
                                    ToastUtil.showToast(ClassificationTwoActivity.this, "没有更多!");
                                }
                            }

                        } else if (baseBean.code == 1) {
                            twoCategoryList.get(position).setTrue(false);
                            if (position == threeClasssficationAdapter.getData().size() - 1) {
                                position = 0;
                                twoCategoryList.get(position).setTrue(true);
                                sonList = twoClassificationAdapter.getData().get(position).getSon();
                                updateThree(sonList, twoClassificationAdapter.getData().get(position).getId(),position);
                            } else {
                                position++;
                                twoCategoryList.get(position).setTrue(true);
                                sonList = twoClassificationAdapter.getData().get(position).getSon();
                                updateThree(sonList, twoClassificationAdapter.getData().get(position).getId(),position);
                            }
                            twoClassificationAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        if (refresh != null) {
                            refresh.loadMoreComplete();
                            refresh.refreshComplete();
                        }
                    }
                });
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }


    @OnClick({R.id.class_break, R.id.class_title, R.id.class_pic_gosuo, R.id.class_pic_gwc,
            R.id.im_xiala})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.class_break://退出页面

                finish();

                break;

            case R.id.class_title://名称弹出一级列表
                ivTitle.setImageResource(R.drawable.jiantoushang);
                showHeadPop(view);
                break;

            case R.id.class_pic_gosuo://搜索
                Intent intent = new Intent(ClassificationTwoActivity.this, SearchActivity.class);
                startActivity(intent);
                break;

            case R.id.class_pic_gwc://购物车
                if (TextUtils.isEmpty(new PreferencesHelper(ClassificationTwoActivity.this).getToken())) {
                    ToolUtil.goToLogin(ClassificationTwoActivity.this);
                    return;
                }
                Intent intent1 = new Intent(ClassificationTwoActivity.this, MainActivity.class);
                intent1.putExtra("go_cart", 1);
                startActivity(intent1);
                break;

            case R.id.im_xiala://三级列表
                imXiala.setImageResource(R.drawable.jiantoushang);
                showThreePop(view);
                break;
        }
    }

    /**
     * 三级
     *
     * @param view
     */
    private void showThreePop(View view) {
        View inflate = LayoutInflater.from(ClassificationTwoActivity.this).inflate(
                R.layout.layout_pop_three, null);

        popupWindowThree = new PopWindow(inflate, widths,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindowThree.setFocusable(true);
        popupWindowThree.setOutsideTouchable(true);
        ColorDrawable cd = new ColorDrawable(0xc0cccccc);// 背景颜色
        popupWindowThree.setBackgroundDrawable(cd);
//        popupWindowThree.setWidth(ScreenUtil.getScreenHeight(ClassificationTwoActivity.this) - width);
//        int[] location = new int[2];
//        view.getLocationOnScreen(location);
//        popupWindowOne.showAsDropDown(view);
        popupWindowThree.setAnimationStyle(R.style.style_pop_animation);// 动画效果必须放在showAsDropDown()方法上边，否则无效
//        backgroundAlpha(0.5f);// 设置背景半透明
//        popupWindowOne.showAtLocation(view, Gravity.RIGHT|Gravity.NO_GRAVITY, location[0]+view.getWidth(),location[1]);
//        popupWindowOne.setOnDismissListener(new PopupWindow.OnDismissListener() {
//            @Override
//            public void onDismiss() {
//                popupWindowOne = null;// 当点击屏幕时，使popupWindow消失
//                backgroundAlpha(1.0f);// 当点击屏幕时，使半透明效果取消
//            }
//        });

        RecyclerView recyclerView = inflate.findViewById(R.id.flowlayout);

//        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) recyclerView.getLayoutParams();
//        //获取当前控件的布局对象
//        params.weight = 200;//设置当前控件布局的高度
//        recyclerView.setLayoutParams(params);//将设置好的布局参数应用到控件中

        flowStyleAdapter = new FlowStyleAdapter(ClassificationTwoActivity.this);
        final LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) recyclerView.getLayoutParams();
//        lp.width = getResources().getDisplayMetrics().widthPixels;
        lp.width = widths - 10;
        final MeasureFlowLayoutManager flowLayoutManager = new MeasureFlowLayoutManager(this);
        recyclerView.addItemDecoration(new SpaceItemDecoration((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics())));
        recyclerView.setLayoutManager(flowLayoutManager);
        recyclerView.setAdapter(flowStyleAdapter);

        if (sonList != null && sonList.size() > 0) {
            flowStyleAdapter.setList(sonList);
        }


        flowStyleAdapter.setLisner(new FlowStyleAdapter.OnClickRecyclerTypeListner() {
            @Override
            public void onItemClick(View view, int position) {
                popupWindowThree.dismiss();
                setThreeFalse();
                page = 1;
                threeClasssficationAdapter.getItem(position).setSelect(true);
                threeClasssficationAdapter.notifyDataSetChanged();
                goods(threeClasssficationAdapter.getItem(position).getId(), threeClasssficationAdapter.getItem(position).getParent_id(), page);
                horRecycler.smoothScrollToPosition(position);
                imXiala.setImageResource(R.drawable.jiantouxia);
            }
        });

        popupWindowThree.showAsDropDown(view);
        popupWindowThree.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                imXiala.setImageResource(R.drawable.jiantouxia);
            }
        });
    }

    /**
     * 一级
     */
    private void showHeadPop(View view) {
        View inflate = LayoutInflater.from(ClassificationTwoActivity.this).inflate(
                R.layout.layout_pop_one, null);

        NoScrollGridView list_item = inflate.findViewById(R.id.list_item);
        adapter = new OneClassificationAdapter(list_item);
        list_item.setAdapter(adapter);
        if (listBean != null && listBean.size() > 0) {
            adapter.setData(listBean);
        }


        if (0 == first) {
            for (int i = 0; i < listBean.size(); i++) {
                if (id.equals(listBean.get(i).getId())) {
                    listBean.get(i).setChecked(true);
                } else {
                    listBean.get(i).setChecked(false);
                }
            }
            first = 1;
        }
        list_item.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                classTitle.setText(adapter.getData().get(position).getName());
                two_category(adapter.getData().get(position).getId());
                for (ClassificationBean.ListBean person : listBean) {
                    person.setChecked(false);
                }
                listBean.get(position).setChecked(true);
                adapter.setData(listBean);
                adapter.notifyDataSetChanged();
                ivTitle.setImageResource(R.drawable.jiantoushang);
                popupWindowOne.dismiss();
            }
        });

        popupWindowOne = new PopupWindow(inflate, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindowOne.setFocusable(true);
        popupWindowOne.setOutsideTouchable(false);
        ColorDrawable cd = new ColorDrawable(0x00ffffff);// 背景颜色全透明
        popupWindowOne.setBackgroundDrawable(cd);
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        popupWindowOne.setAnimationStyle(R.style.style_pop_animation);// 动画效果必须放在showAsDropDown()方法上边，否则无效
//        backgroundAlpha(0.5f);// 设置背景半透明
        popupWindowOne.showAsDropDown(view);
        popupWindowOne.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                ivTitle.setImageResource(R.drawable.jiantouxia);
            }
        });
        //popupWindow.showAtLocation(tv_pop, Gravity.NO_GRAVITY, location[0]+tv_pop.getWidth(),location[1]);
//        popupWindowOne.setOnDismissListener(new PopupWindow.OnDismissListener() {
//            @Override
//            public void onDismiss() {
//                popupWindowOne = null;// 当点击屏幕时，使popupWindow消失
//                backgroundAlpha(1.0f);// 当点击屏幕时，使半透明效果取消
//            }
//        });


    }


    private void showAddDialog(final ClassficationGoodsBean bean, float x, float y) {
        xx = x;
        yy = y;
        if ("0".equals(bean.getSpec_type())) {
            //不存在规格

            addShoppingCart(helper.getToken(), String.valueOf(bean.getGoods_id()), "", "", 1, bean.getSpec_type());
        } else {
            //存在规格 选择规格
            cartDialog = new SelectDialog(ClassificationTwoActivity.this, bean.getType());
            SpecDialogBean specDialogBean = new SpecDialogBean();
            specDialogBean.setGoods_id(Integer.parseInt(bean.getGoods_id()));
            specDialogBean.setImg(bean.getImg());
            specDialogBean.setSpec_name(bean.getSpec_key_name());
            specDialogBean.setPlus_price(Double.parseDouble(bean.getPlus_price()));
            specDialogBean.setShop_price(Double.parseDouble(bean.getShop_price()));
            cartDialog.setGoToUseOnclickListener(new SelectDialog.onGoToUseOnclickListener() {
                @Override
                public void onUseClick(String goods_id, String spec_id, String service_id, int num, int s
                        , double price, double vipPrice) {
                    if (s == 0) {
                        ToastUtil.showToast(ClassificationTwoActivity.this, "当前商品库存为0份");
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
    private void addShoppingCart(String token, String goods_id, String service_id, String spec_id, int goods_num, final String type) {
        HttpHelp.getInstance().create(RemoteApi.class).addCart(token, goods_id, service_id, spec_id, goods_num)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<Object>>(ClassificationTwoActivity.this, null) {
                    @Override
                    public void onNext(BaseBean<Object> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            //ToastUtil.showToast(ClassificationTwoActivity.this, baseBean.message);
                            shoppingCartCount(helper.getToken());
                            EventBusUtil.post(new ShoppingCartEvent());
                            //加入动画
                            //不存在规格
                            classifyCircleMoveView.setVisibility(View.VISIBLE);
                            int[] location = new int[2];
                            classPicGwc.getLocationOnScreen(location);
                            classifyCircleMoveView.setMovePath(xx, yy, location[0], location[1]);
                            classifyCircleMoveView.startAnim();

                        } else if (baseBean.code == 4) {
                            ToolUtil.goToLogin(ClassificationTwoActivity.this);
                        } else {
                            ToastUtil.showToast(ClassificationTwoActivity.this, baseBean.message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);

                    }
                });

    }

    // 设置popupWindow背景半透明
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha;// 0.0-1.0
        getWindow().setAttributes(lp);
    }

    @Override
    public void onLoadMore() {
        page++;
        goods(cartId3, cartId2, page,position);
    }

    @Override
    public void onRefresh() {
        page = 1;
        goods(cartId3, cartId2, page,position);

    }

    //购物车数量
    private void shoppingCartCount(String token) {
        HttpHelp.getInstance().create(RemoteApi.class).shoppingCartCount(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<String>>(ClassificationTwoActivity.this, null) {
                    @Override
                    public void onNext(BaseBean<String> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            if (baseBean.data != null && !TextUtils.isEmpty(baseBean.data)) {
                                shopNumber.setVisibility(View.VISIBLE);
                                if (Integer.valueOf(baseBean.data) < 100 && Integer.valueOf(baseBean.data) > 0) {
                                    shopNumber.setText(baseBean.data);
                                } else if (Integer.valueOf(baseBean.data) == 0) {
                                    shopNumber.setVisibility(View.GONE);
                                } else {
                                    shopNumber.setText("99+");
                                }
                            }
                        } else {
                            ToastUtil.showToast(ClassificationTwoActivity.this, baseBean.message);
                            shopNumber.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        shopNumber.setVisibility(View.GONE);
                    }
                });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        shoppingCartCount(helper.getToken());
    }
}
