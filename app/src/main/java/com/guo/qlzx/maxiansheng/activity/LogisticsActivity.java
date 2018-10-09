package com.guo.qlzx.maxiansheng.activity;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.DistanceResult;
import com.amap.api.services.route.DistanceSearch;
import com.guo.qlzx.maxiansheng.R;
import com.guo.qlzx.maxiansheng.adapter.FeeDeliveryAdapter;
import com.guo.qlzx.maxiansheng.bean.LogisticsBean;
import com.guo.qlzx.maxiansheng.http.RemoteApi;
import com.guo.qlzx.maxiansheng.util.ToolUtil;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseActivityTwo;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.GlideUtil;
import com.qlzx.mylibrary.util.LogUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 李 on 2018/5/29.
 * 查看物流
 */

public class LogisticsActivity extends BaseActivityTwo implements AMap.OnMarkerClickListener, AMap.InfoWindowAdapter {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_tel)
    ImageView ivTel;
    @BindView(R.id.tv_horseman)
    TextView tvHorseman;
    @BindView(R.id.btn_tel)
    Button btnTel;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_dada)
    TextView tvDada;
    @BindView(R.id.tv_tel)
    TextView tvTel;
    @BindView(R.id.mv_map)
    MapView mMapView;
    @BindView(R.id.iv_img)
    ImageView ivImg;
    @BindView(R.id.fl_dada)
    FrameLayout flDada;
    @BindView(R.id.free_img)
    ImageView freeImg;
    @BindView(R.id.free_state)
    TextView freeState;
    @BindView(R.id.free_msg)
    TextView freeMsg;
    @BindView(R.id.free_tel)
    TextView freeTel;
    @BindView(R.id.rl_list)
    RecyclerView rlList;
    @BindView(R.id.fl_free)
    FrameLayout flFree;

    private PreferencesHelper helper;
    private String orderSn = "";
    private String orderId = "";

    private String phone = "";
    AMap aMap = null;

    private FeeDeliveryAdapter adapter;

    private Timer timer=new Timer();
    private  LatLng move;
    private LatLng  terminus;

    Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what==0){
                logistics(helper.getToken(), orderId);
            }
            return false;
        }
    });
    @Override
    public int getContentView() {
        return R.layout.activity_logistics;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        hideTitleBar();

        helper = new PreferencesHelper(this);
        orderId = getIntent().getStringExtra("ORDERID");

        mMapView.onCreate(savedInstanceState);

        if (aMap == null) {
            aMap = mMapView.getMap();
        }
        adapter = new FeeDeliveryAdapter(rlList);
        rlList.setLayoutManager(new LinearLayoutManager(this));
        rlList.setAdapter(adapter);

        MyLocationStyle myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        myLocationStyle.getStrokeWidth();
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);
        aMap.moveCamera(CameraUpdateFactory.zoomTo(12));
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style

        aMap.getUiSettings().setZoomControlsEnabled(false);//缩放按钮

        aMap.getUiSettings().setScaleControlsEnabled(true);//比例尺

        aMap.getUiSettings().setMyLocationButtonEnabled(false);//设置默认定位按钮是否显示，非必需设置。
        aMap.getUiSettings().setRotateGesturesEnabled(false);//禁止地图旋转手势
        aMap.getUiSettings().setTiltGesturesEnabled(false);//禁止地图旋转手势

        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。

        aMap.getUiSettings().setLogoPosition(AMapOptions.LOGO_POSITION_BOTTOM_LEFT);// 设置地图logo显示在右下方

    }

    @Override
    public void getData() {
        logistics(helper.getToken(), orderId);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Message message=new Message();
                message.what=0;
                handler.sendMessage(message);
            }
        },1000*60,1000*60);
    }

    //查看物流
    private void logistics(String token, String order_sn) {
        HttpHelp.getRetrofit(this).create(RemoteApi.class).logistics(token, order_sn)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<LogisticsBean>>(this, null) {
                    @Override
                    public void onNext(BaseBean<LogisticsBean> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            if ("12".equals(baseBean.data.getOrder_type())) {
                                //自由配送
                                upDataFeeUI(baseBean.data);
                            } else if ("3".equals(baseBean.data.getOrder_type())) {
                                //达达配送
                                LogisticsBean bean = baseBean.data;
                                tvName.setText(bean.getTransporterName());
                                tvTel.setText("联系电话:" + bean.getTransporterPhone());
                                phone = bean.getTransporterPhone();
                                move=new LatLng(Double.valueOf( bean.getTransporterLat()), Double.valueOf( bean.getTransporterLng()));
                                terminus=new LatLng(Double.valueOf(bean.getUserLat()), Double.valueOf( bean.getUserLng()));
                                initMap(bean.getUserLat(), bean.getUserLng(), bean.getTransporterLat(), bean.getTransporterLng());
                            }
                        } else {
                            ToastUtil.showToast(LogisticsActivity.this, baseBean.message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
    }

    private void upDataFeeUI(LogisticsBean data) {
        //GlideUtil.display(LogisticsActivity.this, Constants.IMG_HOST, freeImg);
        //11 自由配送取消 12 自由配送中 13 自由配送完成
        titleBar.setVisibility(View.VISIBLE);
        titleBar.setTitleText("查看物流");
        flFree.setVisibility(View.VISIBLE);
        if ("11".equals(data.getOrder_status())) {
            freeState.setText("自由配送取消");
        } else if ("12".equals(data.getOrder_status())) {
            freeState.setText("自由配送中");
        } else if ("13".equals(data.getOrder_status())) {
            freeState.setText("自由配送完成");
        }
        freeMsg.setText("自由配送：" + data.getStaff_name() + "   " + data.getStaff_iphone());
        freeTel.setText("官方电话：" + data.getTelephone());
        adapter.setData(data.getOrder_status_info());
    }

    private void initMap(String v, String v1, String l, String l2) {
        flDada.setVisibility(View.VISIBLE);
        LatLng move=new LatLng(Double.valueOf(l), Double.valueOf(l2));
        LatLng terminus=new LatLng(Double.valueOf(v), Double.valueOf(v1));
        //移动展示
        aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(move, 10f), 4000, null);

        //LatLng latLng = new LatLng(39.903409, 116.427258);//纬度,精度   latitude,lng
        //第一个点
        MarkerOptions markerOption = new MarkerOptions();
        markerOption.position(move);
       // markerOption.title("距离您"+distance+"千米");
        markerOption.draggable(false);//设置Marker可拖动
        markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                .decodeResource(getResources(), R.drawable.ic_horseman_map)));
        // 将Marker设置为贴地显示，可以双指下拉地图查看效果
        markerOption.setFlat(true);//设置marker平贴地图效果
        //第二个点
        MarkerOptions markerOption2 = new MarkerOptions();
        markerOption2.position(terminus);
        markerOption2.draggable(false);//设置Marker可拖动
        markerOption2.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                .decodeResource(getResources(), R.drawable.ic_user_map)));
        // 将Marker设置为贴地显示，可以双指下拉地图查看效果
        markerOption2.setFlat(false);//设置marker平贴地图效果
        //添加
        ArrayList<MarkerOptions> list=new ArrayList<>();
        list.add(markerOption2);
        list.add(markerOption);
        aMap.addMarkers(list, true);
        aMap.setOnMarkerClickListener(this);
        aMap.setInfoWindowAdapter(this);



    }
    /**
     * 确认订单
     *
     * @param token
     * @param order_id
     */
    private void confirm(String token, String order_id) {
        HttpHelp.getInstance().create(RemoteApi.class).confirmOrder(token, order_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<Object>>(this, null) {
                    @Override
                    public void onNext(BaseBean<Object> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            ToastUtil.showToast(LogisticsActivity.this, baseBean.message);
                            finish();
                        } else if (baseBean.code == 4) {
                            ToolUtil.loseToLogin(LogisticsActivity.this);
                        } else {
                            ToastUtil.showToast(LogisticsActivity.this, baseBean.message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);

                    }
                });
    }

    @OnClick({R.id.iv_back, R.id.iv_tel, R.id.btn_tel, R.id.btn_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                //返回键
                finish();
                break;
            case R.id.iv_tel:
                //右上角 电话
                if (TextUtils.isEmpty(phone)) {
                    ToastUtil.showToast(LogisticsActivity.this, "网络加载中，请稍后");
                    return;
                }
                ToolUtil.goToCall(LogisticsActivity.this, phone);
                break;
            case R.id.btn_tel:
                //按键- 联系骑手
                if (TextUtils.isEmpty(phone)) {
                    ToastUtil.showToast(LogisticsActivity.this, "网络加载中，请稍后");
                    return;
                }
                ToolUtil.goToCall(LogisticsActivity.this, phone);
                break;
            case R.id.btn_submit:
                //按键-确认收货
                confirm(helper.getToken(), orderId);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow(); // 显示改点对应 的infowindow
        return false;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        View infoWindow = getLayoutInflater().inflate(R.layout.map_distance, null);//display为自定义layout文件
        TextView name = (TextView) infoWindow.findViewById(R.id.tv_dis);
        //计算距离
        BigDecimal b=new BigDecimal(AMapUtils.calculateLineDistance(terminus,move)/1000);
        float distance=b.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
        name.setText(distance+"千米");
        //此处省去长篇代码
        return infoWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
