package com.guo.qlzx.maxiansheng.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * author           Alpha58
 * time             2017/2/25 10:48
 * desc	            ${TODO}
 * <p>
 * upDateAuthor     $Author$
 * upDate           $Date$
 * upDateDesc       ${TODO}
 */
public class PoiAddressBean implements Parcelable {

    public String longitude;//经度
    public String latitude;//纬度
    public String text;//详细地址 (搜索的关键字)
    public String detailAddress;//搜索的名字  北京化工大学国家大学科技园
    public String province;//省
    public String city;//城市
    public String district;//区域(宝安区)

    public PoiAddressBean(String lon, String lat, String detailAddress, String text, String province, String city, String district) {
        this.longitude = lon;
        this.latitude = lat;
        this.text = text;
        this.detailAddress = detailAddress;
        this.province = province;
        this.city = city;
        this.district = district;

    }

    protected PoiAddressBean(Parcel in) {
        longitude = in.readString();
        latitude = in.readString();
        text = in.readString();
        detailAddress = in.readString();
        province = in.readString();
        city = in.readString();
        district = in.readString();
    }

    public static final Creator<PoiAddressBean> CREATOR = new Creator<PoiAddressBean>() {
        @Override
        public PoiAddressBean createFromParcel(Parcel in) {
            return new PoiAddressBean(in);
        }

        @Override
        public PoiAddressBean[] newArray(int size) {
            return new PoiAddressBean[size];
        }
    };

    public String getLongitude() {
        return longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getText() {
        return text;
    }

    public String getDetailAddress() {
        return detailAddress;
    }

    public String getProvince() {
        return province;
    }

    public String getCity() {
        return city;
    }

    public String getDistrict() {
        return district;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(longitude);
        dest.writeString(latitude);
        dest.writeString(text);
        dest.writeString(detailAddress);
        dest.writeString(province);
        dest.writeString(city);
        dest.writeString(district);
    }
}
