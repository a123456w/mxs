package com.guo.qlzx.maxiansheng.activity;

import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.guo.qlzx.maxiansheng.R;
import com.guo.qlzx.maxiansheng.bean.AddressInfoBean;
import com.guo.qlzx.maxiansheng.bean.PoiAddressBean;
import com.guo.qlzx.maxiansheng.http.RemoteApi;
import com.guo.qlzx.maxiansheng.util.ToolUtil;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.LogUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 李 on 2018/4/13.
 * 新建地址
 */

public class AddAddressActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.tv_name)
    EditText tvName;
    @BindView(R.id.et_call)
    EditText etCall;
    @BindView(R.id.tv_city_display)
    TextView tvCityDisplay;
    @BindView(R.id.tv_city_change)
    TextView tvCityChange;
    @BindView(R.id.et_address)
    EditText etAddress;
    @BindView(R.id.tv_doornum)
    TextView tvDoorNum;

    private int type;
    int address_id = 0;

    private PreferencesHelper helper;


    String doorNum = "";
    String city = "";
    String district = "";
    double longitude = 116.256247;
    double latitude = 40.205253;

    private String myLongtitude = "";
    private String myLatitude = "";

    //选择时间
    private OptionsPickerView pvOptions;
    private ArrayList<String> food = new ArrayList<>();

    @Override
    public int getContentView() {
        return R.layout.activity_address_add;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setLeftImageRes(R.drawable.ic_back_gray);
        titleBar.setRightTextRes("保存");
        titleBar.setRightTextClick(this);

        helper = new PreferencesHelper(this);

        type = getIntent().getIntExtra("ISADD", 0);
        if (type == 1) {
            titleBar.setTitleText("编辑收货人");
            address_id = getIntent().getIntExtra("ADDRESSID", 0);
            getAddressInfo(helper.getToken(), address_id);
        } else if (type == 0) {
            titleBar.setTitleText("新建收货人");
        }


    }


    @OnClick({R.id.ll_city})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_city:
                //城市选择
                Intent intent = new Intent(AddAddressActivity.this, MapActivity.class);
                startActivityForResult(intent, 0);
                break;
        }
    }


    @Override
    public void getData() {

    }

    //修改地址
    private void saveAddressInfo(String token, String consignee, String mobile, String city, String district, String address, int addressId, String longitude, String latitude, String doorNum) {
        HttpHelp.getInstance().create(RemoteApi.class).editAddress(token, consignee, mobile, city, district, address, addressId, longitude, latitude, doorNum)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<Object>>(this, null) {
                    @Override
                    public void onNext(BaseBean<Object> baseBean) {
                        super.onNext(baseBean);
                        hideLoadingDialog();
                        if (baseBean.code == 0) {
                            ToastUtil.showToast(AddAddressActivity.this, baseBean.message);
                            finish();
                        } else {
                            ToastUtil.showToast(AddAddressActivity.this, baseBean.message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        hideLoadingDialog();
                    }
                });
    }

    //编辑模式下-获取地址数据
    private void getAddressInfo(String token, int address_id) {
        HttpHelp.getInstance().create(RemoteApi.class).getAddressInfo(token, address_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<AddressInfoBean>>(this, null) {
                    @Override
                    public void onNext(BaseBean<AddressInfoBean> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            AddressInfoBean infoBean = baseBean.data;
                            if (infoBean != null) {
                                tvName.setText(infoBean.getConsignee());
                                etCall.setText(infoBean.getMobile());
                                tvCityDisplay.setVisibility(View.VISIBLE);
                                tvCityChange.setVisibility(View.GONE);
                                tvCityDisplay.setText(infoBean.getCity() + "-" + infoBean.getDistrict());
                                etAddress.setText(infoBean.getAddress());
                                doorNum = infoBean.getDoor_num();
                                tvDoorNum.setText(doorNum);
                                city = infoBean.getCity();
                                district = infoBean.getDistrict();
                                longitude = Double.parseDouble(infoBean.getLongitude());
                                latitude = Double.parseDouble(infoBean.getLatitude());
                            }
                        } else if (baseBean.code == 4) {
                            ToolUtil.goToLogin(AddAddressActivity.this);
                        } else {
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
    }

    //添加新地址
    private void addAddressInfo(String token, String consignee, String mobile, String city, String district, String address, String longitude, String latitude, String doorNum) {
        HttpHelp.getInstance().create(RemoteApi.class).addAddress(token, consignee, mobile, city, district, address, longitude, latitude, doorNum)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<Object>>(this, null) {
                    @Override
                    public void onNext(BaseBean<Object> baseBean) {
                        super.onNext(baseBean);
                        hideLoadingDialog();
                        if (baseBean.code == 0) {
                            ToastUtil.showToast(AddAddressActivity.this, baseBean.message);
                            finish();
                        } else if (baseBean.code == 4) {
                            ToolUtil.goToLogin(AddAddressActivity.this);
                        } else {
                            ToastUtil.showToast(AddAddressActivity.this, baseBean.message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        hideLoadingDialog();
                    }
                });
    }

    //保存键
    @Override
    public void onClick(View v) {
        if (TextUtils.isEmpty(tvName.getText().toString())) {
            ToastUtil.showToast(AddAddressActivity.this, "收货人不能为空");
            return;
        }
        if (tvName.getText().toString().length()>20) {
            ToastUtil.showToast(AddAddressActivity.this, "收货人字数太多(20个字符以内)");
            return;
        }
        if (TextUtils.isEmpty(etCall.getText().toString())) {
            ToastUtil.showToast(AddAddressActivity.this, "联系人电话不能为空");
            return;
        }
        if (TextUtils.isEmpty(city) || TextUtils.isEmpty(district)) {
            ToastUtil.showToast(AddAddressActivity.this, "请选择地区");
            return;
        }
        if (TextUtils.isEmpty(etAddress.getText().toString())) {
            ToastUtil.showToast(AddAddressActivity.this, "详细地址不能为空");
            return;
        }

        if (TextUtils.isEmpty(tvDoorNum.getText().toString())) {
            ToastUtil.showToast(AddAddressActivity.this, "门牌号不能为空");
            return;
        }
        // getLocation(city+district+etAddress.getText().toString());
        if (type == 1) {
            saveAddressInfo(helper.getToken(), tvName.getText().toString(), etCall.getText().toString(), city, district, etAddress.getText().toString(), address_id, myLongtitude, myLatitude, tvDoorNum.getText().toString());
        } else if (type == 0) {
            addAddressInfo(helper.getToken(), tvName.getText().toString(), etCall.getText().toString(), city, district, etAddress.getText().toString(), myLongtitude, myLatitude, tvDoorNum.getText().toString());
        }
    }


    private PoiAddressBean poiAddressBean;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == 0) {//出发点

                poiAddressBean = data.getParcelableExtra("poiAddressBean");

                setAddressData(poiAddressBean);
            } else if (requestCode == 2) {

                poiAddressBean = data.getParcelableExtra("poiAddressBean");
                setAddressData(poiAddressBean);
            }


        }


    }

    private void setAddressData(PoiAddressBean poiAddressBean) {
        myLongtitude = poiAddressBean.getLongitude();
        myLatitude = poiAddressBean.getLatitude();
        city = poiAddressBean.getCity();
        district = poiAddressBean.getDistrict();
        tvCityDisplay.setVisibility(View.VISIBLE);
        tvCityChange.setVisibility(View.GONE);
        tvCityDisplay.setText(city + "-" + district);
        etAddress.setText(poiAddressBean.getDetailAddress());
    }

    /**
     * 获取电话
     *
     * @param intent
     * @return
     */
    private String getPhoneNumber(Intent intent) {
        Cursor cursor = null;
        Cursor phone = null;
        try {
            String[] projections = {ContactsContract.Contacts._ID, ContactsContract.Contacts.HAS_PHONE_NUMBER};
            cursor = getContentResolver().query(intent.getData(), projections, null, null, null);
            if ((cursor == null) || (!cursor.moveToFirst())) {
                return null;
            }
            int _id = cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID);
            String id = cursor.getString(_id);
            int has_phone_number = cursor.getColumnIndexOrThrow(ContactsContract.Contacts.HAS_PHONE_NUMBER);
            int hasPhoneNumber = cursor.getInt(has_phone_number);
            String phoneNumber = null;
            if (hasPhoneNumber > 0) {
                phone = getContentResolver().query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "="
                                + id, null, null);
                while (phone.moveToNext()) {
                    int index = phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                    String number = phone.getString(index);
                    phoneNumber = number;
                }
            }
            return phoneNumber;
        } catch (Exception e) {

        } finally {
            if (cursor != null) cursor.close();
            if (phone != null) phone.close();
        }
        return null;
    }


    /**
     * 根据地址获取经纬度
     *
     * @param address
     */
    private void getLocation(String address) {
        showLoadingDialog("");
        GeocodeSearch geocodeSearch = new GeocodeSearch(AddAddressActivity.this);
        geocodeSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
            @Override
            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {

            }

            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
                if (i == AMapException.CODE_AMAP_SUCCESS) {
                    if (geocodeResult != null && geocodeResult.getGeocodeAddressList() != null
                            && geocodeResult.getGeocodeAddressList().size() > 0) {
                        GeocodeAddress address = geocodeResult.getGeocodeAddressList().get(0);
                        LogUtil.i("经纬度值:" + address.getLatLonPoint() + "\n位置描述:"
                                + address.getFormatAddress());
                        //获取到的经纬度
                        LatLonPoint latLongPoint = address.getLatLonPoint();
                        LogUtil.i("经纬度值：long:" + latLongPoint.getLongitude() + "   lat:" + latLongPoint.getLatitude());
                        longitude = latLongPoint.getLongitude();
                        latitude = latLongPoint.getLatitude();
                    }
                    change();

                } else {
                    ToastUtil.showToast(AddAddressActivity.this, "定位失败！");
                    hideLoadingDialog();
                }
            }
        });
        GeocodeQuery query = new GeocodeQuery(address, address);
        geocodeSearch.getFromLocationNameAsyn(query);
    }

    /**
     * 修改
     */
    private void change() {
        if (type == 1) {
            saveAddressInfo(helper.getToken(), tvName.getText().toString(), etCall.getText().toString(), city, district, etAddress.getText().toString(), address_id, myLongtitude, myLatitude, tvDoorNum.getText().toString());
        } else if (type == 0) {
            addAddressInfo(helper.getToken(), tvName.getText().toString(), etCall.getText().toString(), city, district, etAddress.getText().toString(), myLongtitude, myLatitude, tvDoorNum.getText().toString());
        }
    }
}
