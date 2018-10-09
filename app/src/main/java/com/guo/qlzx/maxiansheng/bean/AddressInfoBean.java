package com.guo.qlzx.maxiansheng.bean;

/**
 * Created by 李 on 2018/4/16.
 * 获取地址信息
 */

public class AddressInfoBean {


    /**
     * consignee : 张三
     * mobile : 13814756398
     * address : 北京市海淀区
     * city : 北京市
     * district : 海淀区
     * longitude : 116.30
     * latitude : 39.95
     * door_num : 2号楼301
     */

    private String consignee;
    private String mobile;
    private String address;
    private String city;
    private String district;
    private String longitude;
    private String latitude;
    private String door_num;

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getDoor_num() {
        return door_num;
    }

    public void setDoor_num(String door_num) {
        this.door_num = door_num;
    }
}
