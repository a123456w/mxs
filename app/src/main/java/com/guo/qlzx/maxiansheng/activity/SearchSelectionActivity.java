package com.guo.qlzx.maxiansheng.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.canyinghao.canrefresh.CanRefreshLayout;
import com.canyinghao.canrefresh.classic.ClassicRefreshView;
import com.google.gson.Gson;

import com.guo.qlzx.maxiansheng.R;
import com.guo.qlzx.maxiansheng.adapter.SearchSelectionAdapter;
import com.guo.qlzx.maxiansheng.bean.PoiAddressBean;
import com.qlzx.mylibrary.util.DensityUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.StringUtil;
import com.qlzx.mylibrary.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/2/22.
 * 输入框搜索文本(地图的)
 */

public class SearchSelectionActivity extends AppCompatActivity implements PoiSearch.OnPoiSearchListener, CanRefreshLayout.OnLoadMoreListener, CanRefreshLayout.OnRefreshListener {
    @BindView(R.id.tv_ok)
    TextView tvOk;
    @BindView(R.id.et_manual_selection_location)
    EditText etManualSelectionLocation;
    @BindView(R.id.can_refresh_header)
    ClassicRefreshView canRefreshHeader;
    @BindView(R.id.can_refresh_footer)
    ClassicRefreshView canRefreshFooter;
    @BindView(R.id.can_content_view)
    RecyclerView canContentView;
    @BindView(R.id.refresh)
    CanRefreshLayout refresh;
    @BindView(R.id.rl_empty)
    RelativeLayout rlEmpty;


    private String keyWord = "";// 要输入的poi搜索关键字
    private PoiResult poiResult; // poi返回的结果
    private int currentPage = 1;// 当前页面，从0开始计数
    private PoiSearch.Query query;// Poi查询条件类
    private PoiSearch poiSearch;// POI搜索
    private SearchSelectionAdapter adapter;
    private PoiAddressBean detailAddress;
//    private String locationCity = "";
    private String mapCenterPoint;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_selection);
        ButterKnife.bind(this);

        refresh.setOnLoadMoreListener(this);
        refresh.setOnRefreshListener(this);
        mapCenterPoint = getIntent().getStringExtra("mapCenterPoint");

        refresh.setMaxFooterHeight(DensityUtil.dp2px(SearchSelectionActivity.this, 150));
        refresh.setStyle(0, 0);
        canRefreshHeader.setPullStr("下拉刷新");
        canRefreshHeader.setReleaseStr("松开刷新");
        canRefreshHeader.setRefreshingStr("加载中");
        canRefreshHeader.setCompleteStr("");

        canRefreshFooter.setPullStr("上拉加载更多");
        canRefreshFooter.setReleaseStr("松开刷新");
        canRefreshFooter.setRefreshingStr("加载中");
        canRefreshFooter.setCompleteStr("");

        canContentView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SearchSelectionAdapter(canContentView);
        canContentView.setAdapter(adapter);


        etManualSelectionLocation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                keyWord = String.valueOf(charSequence);
                if ("".equals(keyWord)) {
                    return;
                } else {
                    currentPage = 1;
                    doSearchQuery();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().length() == 0) {
                    tvOk.setText("取消");
                } else {
                    tvOk.setText("确定");
                }
            }
        });
//        locationCity = new PreferencesHelper(SearchSelectionActivity.this).getLocationCity();
    }

    @OnClick(R.id.tv_ok)
    public void onViewClicked() {
        String s = tvOk.getText().toString();
        if (!StringUtil.isEmpty(s)) {
            if (s.equals("取消")) {
                finish();
            }
        }
    }


    /**
     * 开始进行poi搜索
     */
    protected void doSearchQuery() {
        //不输入城市名称有些地方搜索不到
//        query = new PoiSearch.Query(keyWord, "", locationCity);// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
//        query.setDistanceSort(true);//设置是否按距离排序
//        query.setPageSize(10);// 设置每页最多返回多少条poiitem
//        query.setPageNum(currentPage);// 设置查第几页
//
//        poiSearch = new PoiSearch(this, query);
//        poiSearch.setOnPoiSearchListener(this);
//        poiSearch.searchPOIAsyn();

        query = new PoiSearch.Query(keyWord, "", "");// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query.setDistanceSort(true);//设置是否按距离排序
        query.setPageSize(10);// 设置每页最多返回多少条poiitem
        query.setPageNum(currentPage);// 设置查第几页

//        intent.putExtra("mapCenterPoint", mapCenterPoint.latitude + "," + mapCenterPoint.longitude);
        LatLonPoint lp = new LatLonPoint(Double.parseDouble(mapCenterPoint.split(",")[0]), Double.parseDouble(mapCenterPoint.split(",")[1]));
        poiSearch = new PoiSearch(this, query);
        poiSearch.setOnPoiSearchListener(this);
//        poiSearch.setBound(new PoiSearch.SearchBound(lp, 10000, true));//设置周边搜索的中心点以及半径
        poiSearch.searchPOIAsyn();

    }


    public void setDetailAddress(PoiAddressBean detailAddress) {
        this.detailAddress = detailAddress;
        if (detailAddress == null) {
            ToastUtil.showToast(SearchSelectionActivity.this, "选择错误");
            return;
        }

        String poiAddressBean = new Gson().toJson(detailAddress);

        Intent intent = new Intent();
        intent.putExtra("poiAddressJson", poiAddressBean);
//        intent.putExtra("DetailAddress", detailAddress.getDetailAddress());
//        intent.putExtra("Latitude", detailAddress.getLatitude());
//        intent.putExtra("Longitude", detailAddress.getLongitude());
        setResult(200, intent);
        finish();
    }

    @Override
    public void onPoiSearched(PoiResult result, int rCode) {
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getQuery() != null) {  // 搜索poi的结果
                if (result.getQuery().equals(query)) {  // 是否是同一条
                    poiResult = result;
                    List<PoiAddressBean> data = new ArrayList<PoiAddressBean>();//自己创建的数据集合
                    // 取得搜索到的poiitems有多少页
                    List<PoiItem> poiItems = poiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始
                    List<SuggestionCity> suggestionCities = poiResult
                            .getSearchSuggestionCitys();// 当搜索不到poiitem数据时，会返回含有搜索关键字的城市信息
                    for (PoiItem item : poiItems) {
                        //获取经纬度对象
                        LatLonPoint llp = item.getLatLonPoint();
                        double lon = llp.getLongitude();
                        double lat = llp.getLatitude();

                        String title = item.getTitle();
                        String text = item.getSnippet();
                        String provinceName = item.getProvinceName();
                        String cityName = item.getCityName();
                        String adName = item.getAdName();
                        data.add(new PoiAddressBean(String.valueOf(lon), String.valueOf(lat), title, text, provinceName,
                                cityName, adName));
                    }

                    if (currentPage == 1) {
                        if (data != null && data.size() > 0) {
                            adapter.clear();
                            adapter.setData(data);
                        } else {

                        }

                    } else {
                        if (data != null && data.size() > 0) {
                            adapter.addMoreData(data);
                        } else {
                            ToastUtil.showToast(SearchSelectionActivity.this, getResources().getString(R.string.no_more));
                        }
                    }

                }
            } else {
                ToastUtil.showToast(SearchSelectionActivity.this, "对不起，没有搜索到相关数据！");
            }
        } else {
            ToastUtil.showToast(SearchSelectionActivity.this, "对不起，没有搜索到相关数据！");

        }
        refresh.loadMoreComplete();
        refresh.refreshComplete();
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    @Override
    public void onLoadMore() {
        currentPage++;
        doSearchQuery();
    }

    @Override
    public void onRefresh() {
        currentPage = 1;
        doSearchQuery();
    }
}
