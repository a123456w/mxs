package com.guo.qlzx.maxiansheng.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.Projection;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.Polygon;
import com.amap.api.maps.model.PolygonOptions;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.maps.model.animation.Animation;
import com.amap.api.maps.model.animation.TranslateAnimation;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.district.DistrictItem;
import com.amap.api.services.district.DistrictResult;
import com.amap.api.services.district.DistrictSearch;
import com.amap.api.services.district.DistrictSearchQuery;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.canyinghao.canrefresh.CanRefreshLayout;
import com.canyinghao.canrefresh.classic.ClassicRefreshView;
import com.google.gson.Gson;
import com.guo.qlzx.maxiansheng.R;
import com.guo.qlzx.maxiansheng.adapter.ChooseOriginByMapAdapter;
import com.guo.qlzx.maxiansheng.bean.PoiAddressBean;
import com.guo.qlzx.maxiansheng.util.PointCompare;
import com.guo.qlzx.maxiansheng.util.dialog.AlertDialog;
import com.qlzx.mylibrary.base.BaseActivityTwo;
import com.qlzx.mylibrary.util.DensityUtil;
import com.qlzx.mylibrary.util.LogUtil;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2018/4/28 0028.
 * destribe 地图页面
 */

public class MapActivity extends BaseActivityTwo implements PoiSearch.OnPoiSearchListener, CanRefreshLayout.OnLoadMoreListener, DistrictSearch.OnDistrictSearchListener {
    //    ChooseOriginMapChooseAdapter
    @BindView(R.id.map)
    MapView map;
    @BindView(R.id.tv_ok)
    TextView tvOk;
    @BindView(R.id.et_manual_selection_location)
    EditText etManualSelectionLocation;
    @BindView(R.id.choice_location)
    ImageView choiceLocation;
    @BindView(R.id.can_refresh_footer)
    ClassicRefreshView canRefreshFooter;
    @BindView(R.id.can_content_view)
    RecyclerView canContentView;
    @BindView(R.id.refresh)
    CanRefreshLayout refresh;
    private float zoom = 17;
    private boolean isLoaded;

    private Unbinder unbinder;
    private AMap aMap;
    private UiSettings mUiSettings;

    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = new AMapLocationClientOption();

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    private double longitude;
    private double latitude;
    private boolean isAlreadyLocation = false;
    private boolean isFirst = true;
    private LatLng latLng;
    private Marker screenMarker = null;
    private ChooseOriginByMapAdapter adapter;

    private PoiResult poiResult; // poi返回的结果
    private int currentPage = 1;// 当前页面，从0开始计数
    private PoiSearch.Query query;// Poi查询条件类
    private PoiSearch poiSearch;// POI搜索
    private PoiAddressBean poiAddressBean;
//    private String driverTypeId;
//    private String orType="1";


    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new AMapLocationListener() {

        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            //        首先，可以判断AMapLocation对象不为空，当定位错误码类型为0时定位成功。
            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
                    //可在其中解析amapLocation获取相应内容。

                    latitude = amapLocation.getLatitude();//获取纬度
                    longitude = amapLocation.getLongitude();//获取经度
                    latLng = new LatLng(latitude, longitude);

                    if (!isAlreadyLocation) {
                        isAlreadyLocation = true;

                        mLocationListener = null;
                        mLocationClient = null;
                        if (mLocationClient != null) {
                            mLocationClient.stopLocation();
                            mLocationClient.onDestroy();
                        }

                        if (isFirst) {
                            //改变可视区域为指定位置
                            //CameraPosition4个参数分别为位置，缩放级别，目标可视区域倾斜度，可视区域指向方向（正北逆时针算起，0-360）
                            CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(latLng, 8, 0, 0));
                            aMap.moveCamera(cameraUpdate);//地图移向指定区域
//                            addMarkerInScreenCenter();
                            isFirst = false;
                        }

                        //然后可以移动到定位点,使用animateCamera就有动画效果
                        aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

                        currentPage = 1;
                        doSearchQuery(latLng);

                        LogUtil.e("定位成功");
                    }

                } else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。

                    LogUtil.e("高德定位失败" + amapLocation.getErrorInfo());
//                    Toast.makeText(mContext, "高德定位失败。", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };
    private DistrictSearch districtSearch;
    private DistrictSearchQuery fromQuery;
    private PolygonOptions pOptionOne;
    private PolygonOptions pOptionTwo;


    @Override
    public int getContentView() {
        return R.layout.activity_map;
    }
//    map

    @Override
    public void initView(Bundle savedInstanceState) {
        unbinder = ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        inits();

        map.onCreate(savedInstanceState);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        //显示地图
        if (aMap == null) {
            aMap = map.getMap();
            mUiSettings = aMap.getUiSettings();
        }

//        mUiSettings.setLogoPosition(AMapOptions.LOGO_POSITION_BOTTOM_CENTER);// 设置地图logo显示在底部居中

        MyLocationStyle myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。

        myLocationStyle.getStrokeWidth();
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);
        /*
         * 取消蓝圈 teambition的bug要求 0_0
         */
        myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));
        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));

        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style

        aMap.getUiSettings().setZoomControlsEnabled(false);//缩放按钮

        aMap.getUiSettings().setScaleControlsEnabled(true);//比例尺

        aMap.moveCamera(CameraUpdateFactory.zoomTo(zoom));

        aMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {

            private LatLng mapCenterPoint;

            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
            }

            @Override
            public void onCameraChangeFinish(CameraPosition cameraPosition) {
                if (!isLoaded && cameraPosition.zoom != zoom) {
                    aMap.moveCamera(CameraUpdateFactory.zoomTo(zoom));
                    isLoaded = true;
                }
                //屏幕中心的Marker跳动
                startJumpAnimation();

                mapCenterPoint = getMapCenterPoint();

                currentPage = 1;
                doSearchQuery(mapCenterPoint);

                LogUtil.e("经纬度==" + mapCenterPoint.latitude + "--" + mapCenterPoint.longitude);

            }
        });
        aMap.setOnMapLoadedListener(new AMap.OnMapLoadedListener() {
            @Override
            public void onMapLoaded() {
                addMarkerInScreenCenter();
            }
        });

        aMap.getUiSettings().setMyLocationButtonEnabled(false);//设置默认定位按钮是否显示，非必需设置。
        aMap.getUiSettings().setRotateGesturesEnabled(false);//禁止地图旋转手势
        aMap.getUiSettings().setTiltGesturesEnabled(false);//禁止地图旋转手势

        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。

        aMap.getUiSettings().setLogoPosition(AMapOptions.LOGO_POSITION_BOTTOM_LEFT);// 设置地图logo显示在右下方

        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
        mLocationOption.setInterval(3000);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        mLocationOption.setHttpTimeOut(20000);

//        初始化定位
        mLocationClient = new AMapLocationClient(MapActivity.this);
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);

        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
//---------------------------------------------------------------------
        //初始化区域搜索
        districtSearch = new DistrictSearch(getApplicationContext());
        districtSearch.setOnDistrictSearchListener(this);//绑定监听器;

//        110112通州区
        //110105朝阳区
        String[] keyword = {"110112"};//"通州区",
        for (int i = 0; i < keyword.length; i++) {
            fromQuery = new DistrictSearchQuery();
            //設置關鍵字
            fromQuery.setKeywords(keyword[i]);
            //設置是否返回邊界值
            fromQuery.setShowBoundary(true);
            //不显示子区域边界
            fromQuery.setShowChild(false);
            districtSearch.setQuery(fromQuery);
            //开启异步搜索
            districtSearch.searchDistrictAsyn();
        }
//---------------------------------------------------------------------

        //初始化区域搜索
        DistrictSearch districtSearchTwo = new DistrictSearch(getApplicationContext());
        districtSearchTwo.setOnDistrictSearchListener(new DistrictSearch.OnDistrictSearchListener() {
            @Override
            public void onDistrictSearched(DistrictResult districtResult) {
                if (districtResult != null && districtResult.getDistrict() != null) {
                    if (districtResult.getAMapException().getErrorCode() == AMapException.CODE_AMAP_SUCCESS) {

                        ArrayList<DistrictItem> district = districtResult.getDistrict();
                        if (district != null && district.size() > 0) {
                            for (int i = 0; i < district.size(); i++) {

                                //adcode 440106
                                //获取对应的行政区划的item
                                DistrictItem districtItem = district.get(i);
                                if (districtItem == null) {
                                    return;
                                }
                                //创建划线子线程
                                PolygonRunnableTwo fromRunnable = new PolygonRunnableTwo(districtItem, handler);
                                //线程池执行
                                new Thread(fromRunnable).start();
                            }
                        }
                    }
                }
            }
        });//绑定监听器;

//        keywords - 关键词，支持：行政区名称、城市编码、区域编码。
        //获取行政区划边界--最多只能到街道，而街道是没有边界数据的，所以不能使用街道，需要使用区
        String[] keywordTwo = {"110105"};//"通州区",
        for (int i = 0; i < keywordTwo.length; i++) {
            fromQuery = new DistrictSearchQuery();
            //設置關鍵字
            fromQuery.setKeywords(keywordTwo[i]);
            //設置是否返回邊界值
            fromQuery.setShowBoundary(true);
            //不显示子区域边界
            fromQuery.setShowChild(false);
            districtSearchTwo.setQuery(fromQuery);
            //开启异步搜索
            districtSearchTwo.searchDistrictAsyn();
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    /**
     * 行政区划异步搜索的回调
     *
     * @param districtResult
     */
    @Override
    public void onDistrictSearched(DistrictResult districtResult) {
        if (districtResult != null && districtResult.getDistrict() != null) {
            if (districtResult.getAMapException().getErrorCode() == AMapException.CODE_AMAP_SUCCESS) {

                ArrayList<DistrictItem> district = districtResult.getDistrict();
                if (district != null && district.size() > 0) {
                    for (int i = 0; i < district.size(); i++) {

                        //adcode 440106
                        //获取对应的行政区划的item
                        DistrictItem districtItem = district.get(i);
                        if (districtItem == null) {
                            return;
                        }
                        //创建划线子线程
                        PolygonRunnableOne fromRunnable = new PolygonRunnableOne(districtItem, handler);
                        //线程池执行
                        new Thread(fromRunnable).start();
                    }
                }
            }
        }
    }

    private void addMarkerInScreenCenter() {
        LatLng latLng = aMap.getCameraPosition().target;
        Point screenPosition = aMap.getProjection().toScreenLocation(latLng);
        screenMarker = aMap.addMarker(new MarkerOptions()
                .anchor(0.5f, 0.5f)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.purple_pin)));
        //设置Marker在屏幕上,不跟随地图移动
        screenMarker.setPositionByPixels(screenPosition.x, screenPosition.y);
    }


    /**
     * 屏幕中心marker 跳动
     */
    public void startJumpAnimation() {

        if (screenMarker != null) {
            //根据屏幕距离计算需要移动的目标点
            final LatLng latLng = screenMarker.getPosition();
            Point point = aMap.getProjection().toScreenLocation(latLng);
            point.y -= dip2px(this, 125);
            LatLng target = aMap.getProjection()
                    .fromScreenLocation(point);
            //使用TranslateAnimation,填写一个需要移动的目标点
            Animation animation = new TranslateAnimation(target);
            animation.setInterpolator(new Interpolator() {
                @Override
                public float getInterpolation(float input) {
                    // 模拟重加速度的interpolator
                    if (input <= 0.5) {
                        return (float) (0.5f - 2 * (0.5 - input) * (0.5 - input));
                    } else {
                        return (float) (0.5f - Math.sqrt((input - 0.5f) * (1.5f - input)));
                    }
                }
            });
            //整个移动所需要的时间
            animation.setDuration(600);
            //设置动画
            screenMarker.setAnimation(animation);
            //开始动画
            screenMarker.startAnimation();

        } else {
            Log.e("amap", "screenMarker is null");
        }
    }
    // TODO: 2018/5/25 0025
//    boolean b1 = PtInPolygon(mapCenterPoint, currentPolyLineOne);

    //dip和px转换
    private static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    @Override
    public void getData() {
        //启动定位
        mLocationClient.startLocation();
    }

    public void inits() {

        titleBar.setTitleText("选择收货地址");
//        titleBar.getTitle().setTextColor(Color.parseColor("#FFFFFF"));

        canContentView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ChooseOriginByMapAdapter(canContentView);
        canContentView.setAdapter(adapter);

        refresh.setOnLoadMoreListener(this);
        refresh.setMaxFooterHeight(DensityUtil.dp2px(MapActivity.this, 150));
        refresh.setStyle(0, 0);

        canRefreshFooter.setPullStr("上拉加载更多");
        canRefreshFooter.setReleaseStr("松开刷新");
        canRefreshFooter.setRefreshingStr("加载中");
        canRefreshFooter.setCompleteStr("");

//移动到定位点
        choiceLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (latLng != null) {
                    //然后可以移动到定位点,使用animateCamera就有动画效果
                    aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
                }
            }
        });

        etManualSelectionLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng mapCenterPoint = getMapCenterPoint();

                Intent intent = new Intent(MapActivity.this, SearchSelectionActivity.class);
                intent.putExtra("mapCenterPoint", mapCenterPoint.latitude + "," + mapCenterPoint.longitude);
                startActivityForResult(intent, 100);
            }
        });

        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (poiAddressBean == null) {
                    ToastUtil.showToast(MapActivity.this, "请选择所在地区");
                    return;
                }
                if (poiAddressBean.getDetailAddress().equals(etManualSelectionLocation.getText().toString().trim())) {
                    if ("通州区".equals(poiAddressBean.getDistrict()) || "朝阳区".equals(poiAddressBean.getDistrict())) {
                        Intent intentChoiceHome = new Intent();
                        intentChoiceHome.putExtra("poiAddressBean", poiAddressBean);
                        setResult(RESULT_OK, intentChoiceHome);
                        finish();
                    } else {
                        //不支持改地址
                        AlertDialog alertDialog = new AlertDialog(MapActivity.this);
                        alertDialog.show();
                        alertDialog.setMessage("配送范围仅限北京通州区和朝阳区");
                    }

//                    Intent intentChoiceHome = new Intent();
//                    intentChoiceHome.putExtra("poiAddressBean", poiAddressBean);
//                    setResult(RESULT_OK, intentChoiceHome);
//                    finish();
                }

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 200) {
            String poiAddressJson = data.getStringExtra("poiAddressJson");
            if (poiAddressJson != null) {
                PoiAddressBean poiAddressBean = new Gson().fromJson(poiAddressJson, PoiAddressBean.class);
                if (poiAddressBean != null) {
                    this.poiAddressBean = poiAddressBean;

                    etManualSelectionLocation.setText(poiAddressBean.getDetailAddress());

                    LatLng latLngs = new LatLng(Double.parseDouble(poiAddressBean.getLatitude()), Double.parseDouble(poiAddressBean.getLongitude()));

                    //然后可以移动到定位点,使用animateCamera就有动画效果
                    aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLngs, zoom));


                }
            }

//            String latitude = data.getStringExtra("Latitude");
//            String longitude = data.getStringExtra("Longitude");
//            String detailAddress = data.getStringExtra("DetailAddress");
//            if (latitude != null && longitude != null) {
//
//                etManualSelectionLocation.setText(detailAddress);
//
//                LatLng latLngs = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
//
//                //然后可以移动到定位点,使用animateCamera就有动画效果
//                aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLngs, zoom));
//
//            }
        }

    }

    /**
     * 开始进行poi搜索
     */
    protected void doSearchQuery(LatLng latLng) {
        //不输入城市名称有些地方搜索不到
        query = new PoiSearch.Query("", "", "");// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query.setPageSize(10);// 设置每页最多返回多少条poiitem
        query.setPageNum(currentPage);// 设置查第几页

        LatLonPoint lp = new LatLonPoint(latLng.latitude, latLng.longitude);
        poiSearch = new PoiSearch(this, query);
        poiSearch.setOnPoiSearchListener(this);
        poiSearch.setBound(new PoiSearch.SearchBound(lp, 10000, true));//设置周边搜索的中心点以及半径
        poiSearch.searchPOIAsyn();

//        _aroundReq.radius = 10000;//半径(米)
//        aroundReq.offset = 10;//每页数量
//        _aroundReq.keywords = txf.text;//关键字
//        _aroundReq.location
////中心点坐标
//        _aroundReq.page = page;//页码
    }

    /**
     * POI检索回调数据
     *
     * @param result
     * @param rCode
     */
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
                        String provinceName = item.getProvinceName();
                        String cityName = item.getCityName();
                        String adName = item.getAdName();//昌平区
                        String text = item.getSnippet();//昌怀路157号北京私立汇佳学校内


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
                            ToastUtil.showToast(MapActivity.this, getResources().getString(R.string.no_more));
                        }
                    }

                } else {
                    adapter.clear();
                }
                refresh.loadMoreComplete();
                refresh.refreshComplete();
            } else {
                ToastUtil.showToast(MapActivity.this, "对不起，没有搜索到相关数据！");
                adapter.clear();
            }
        } else {
            ToastUtil.showToast(MapActivity.this, "对不起，没有搜索到相关数据！");
            adapter.clear();
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    @Override
    public void onLoadMore() {
        currentPage++;
        doSearchQuery(getMapCenterPoint());
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        map.onDestroy();
        unbinder.unbind();
        //退出界面的时候停止定位
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        map.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        map.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        map.onSaveInstanceState(outState);
    }

    public LatLng getMapCenterPoint() {
        int left = map.getLeft();
        int top = map.getTop();
        int right = map.getRight();
        int bottom = map.getBottom();
        // 获得屏幕点击的位置
        int x = (int) (map.getX() + (right - left) / 2);
        int y = (int) (map.getY() + (bottom - top) / 2);
        Projection projection = aMap.getProjection();
        LatLng pt = projection.fromScreenLocation(new Point(x, y));

        return pt;
    }

    public void setDetailAddress(PoiAddressBean detailAddress) {
        this.poiAddressBean = detailAddress;
        if (poiAddressBean == null) {
            ToastUtil.showToast(MapActivity.this, "对不起，选择失败请重新选择");
            return;
        }

        LatLng latLng = new LatLng(Double.parseDouble(poiAddressBean.getLatitude()), Double.parseDouble(poiAddressBean.getLongitude()));
        boolean flagOne = false;
        if (null != polygonOne)
            flagOne = polygonOne.contains(latLng);

        boolean flagTwo = false;
        if (null != polygonTwo)
            flagTwo = polygonTwo.contains(latLng);

//        if ("通州区".equals(poiAddressBean.getDistrict()) || "朝阳区".equals(poiAddressBean.getDistrict())) {
        if (flagOne || flagTwo) {
            etManualSelectionLocation.setText(poiAddressBean.getDetailAddress());
            LatLng latLngs = new LatLng(Double.parseDouble(poiAddressBean.getLatitude()), Double.parseDouble(poiAddressBean.getLongitude()));
            //然后可以移动到定位点,使用animateCamera就有动画效果
            aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLngs, zoom));

        } else {
            //不支持改地址
            AlertDialog alertDialog = new AlertDialog(MapActivity.this);
            alertDialog.show();
            alertDialog.setMessage("配送范围仅限北京通州区和朝阳区");
        }

    }


    private Polygon polygonOne;
    private Polygon polygonTwo;
    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if (18 == msg.what) {

                PolylineOptions polylineOption = (PolylineOptions) msg.obj;
                aMap.addPolyline(polylineOption);

                if (null != pOptionOne) {
                    polygonOne = aMap.addPolygon(pOptionOne.strokeColor(Color.BLUE).fillColor(Color.argb(0, 0, 0, 0)));//重画区域边界
                }
                if (null != pOptionTwo) {
                    polygonTwo = aMap.addPolygon(pOptionTwo.strokeColor(Color.BLUE).fillColor(Color.argb(0, 0, 0, 0)));//重画区域边界
                }
            }
            if (18 == msg.what) {
                PolylineOptions polylineOption = (PolylineOptions) msg.obj;
                aMap.addPolyline(polylineOption);
            }

        }
    };

    private class PolygonRunnableOne implements Runnable {
        private DistrictItem districtItem;
        private Handler handler;
        private boolean isCancel = false;

        /**
         * districtBoundary()
         * 以字符串数组形式返回行政区划边界值。
         * 字符串拆分规则： 经纬度，经度和纬度之间用","分隔，坐标点之间用";"分隔。
         * 例如：116.076498,40.115153;116.076603,40.115071;116.076333,40.115257;116.076498,40.115153。
         * 字符串数组由来： 如果行政区包括的是群岛，则坐标点是各个岛屿的边界，各个岛屿之间的经纬度使用"|"分隔。
         * 一个字符串数组可包含多个封闭区域，一个字符串表示一个封闭区域
         */
        public PolygonRunnableOne(DistrictItem districtItem, Handler handler) {

            this.districtItem = districtItem;
            this.handler = handler;
        }

        public void cancel() {
            isCancel = true;
        }

        /**
         * Starts executing the active part of the class' code. This method is
         * called when a thread is started that has been created with a class which
         * implements {@code Runnable}.
         */
        @Override
        public void run() {
//            utils.logD("---------------------------------preview------------------5");
            if (!isCancel) {
                try {
                    String[] boundary = districtItem.districtBoundary();
                    if (boundary != null && boundary.length > 0) {
//                        utils.logD("---------------------------------preview------------------11");
//                        utils.logD("----------- " + boundary.toString());
                        for (String b : boundary) {
                            if (!b.contains("|")) {
                                String[] split = b.split(";");
                                PolylineOptions polylineOptions = new PolylineOptions();
                                boolean isFirst = true;
                                LatLng firstLatLng = null;
                                for (String s : split) {
                                    String[] ll = s.split(",");
                                    if (isFirst) {
                                        isFirst = false;
                                        firstLatLng = new LatLng(Double.parseDouble(ll[1]), Double.parseDouble(ll[0]));
                                    }
                                    polylineOptions.add(new LatLng(Double.parseDouble(ll[1]), Double.parseDouble(ll[0])));

                                }
                                if (firstLatLng != null) {
                                    polylineOptions.add(firstLatLng);

                                }
//                                utils.logD("---------------------------------preview------------------6");
                                polylineOptions.width(10).color(Color.BLUE).setDottedLine(true);
                                Message message = handler.obtainMessage();
                                message.what = 18;
                                message.obj = polylineOptions;
                                handler.sendMessage(message);

                                pOptionOne = new PolygonOptions();
                                pOptionOne.addAll(polylineOptions.getPoints());//转换成PolygonOptions类型，为了判断marker是否在区域内
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
//                    utils.logD("---------------------------------preview------------------7");
                }
            }
        }
    }

    private class PolygonRunnableTwo implements Runnable {
        private DistrictItem districtItem;
        private Handler handler;
        private boolean isCancel = false;


        public PolygonRunnableTwo(DistrictItem districtItem, Handler handler) {

            this.districtItem = districtItem;
            this.handler = handler;
        }

        public void cancel() {
            isCancel = true;
        }

        @Override
        public void run() {
//            utils.logD("---------------------------------preview------------------5");
            if (!isCancel) {
                try {
                    String[] boundary = districtItem.districtBoundary();
                    if (boundary != null && boundary.length > 0) {
//                        utils.logD("---------------------------------preview------------------11");
//                        utils.logD("----------- " + boundary.toString());
                        for (String b : boundary) {
                            if (!b.contains("|")) {
                                String[] split = b.split(";");
                                PolylineOptions polylineOptions = new PolylineOptions();
                                boolean isFirst = true;
                                LatLng firstLatLng = null;
                                for (String s : split) {
                                    String[] ll = s.split(",");
                                    if (isFirst) {
                                        isFirst = false;
                                        firstLatLng = new LatLng(Double.parseDouble(ll[1]), Double.parseDouble(ll[0]));
                                    }
                                    polylineOptions.add(new LatLng(Double.parseDouble(ll[1]), Double.parseDouble(ll[0])));

                                }
                                if (firstLatLng != null) {
                                    polylineOptions.add(firstLatLng);

                                }
//                                utils.logD("---------------------------------preview------------------6");
                                polylineOptions.width(10).color(Color.BLUE).setDottedLine(true);
                                Message message = handler.obtainMessage();
                                message.what = 18;
                                message.obj = polylineOptions;
                                handler.sendMessage(message);

                                pOptionTwo = new PolygonOptions();
                                pOptionTwo.addAll(polylineOptions.getPoints());//转换成PolygonOptions类型，为了判断marker是否在区域内
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
//                    utils.logD("---------------------------------preview------------------7");
                }
            }
        }
    }

}
