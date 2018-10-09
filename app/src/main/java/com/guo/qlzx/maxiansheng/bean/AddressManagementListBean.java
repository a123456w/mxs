package com.guo.qlzx.maxiansheng.bean;

/**
 * Created by 李 on 2018/4/13.
 */

public class AddressManagementListBean {

    /**
     * city : 北京市
     * district : 昌平区
     * consignee : 拒绝
     * address : 北京化工大学国家大学科技园
     * mobile : 13321393307
     * is_default : 1
     * address_id : 55
     * door_num : 来来来
     * longitude : 116.256247
     * latitude : 40.205253
     */

    private String city;
    private String district;
    private String consignee;
    private String address;
    private String mobile;
    private int is_default;
    private int address_id;
    private String door_num;
    private String longitude;
    private String latitude;

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

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getIs_default() {
        return is_default;
    }

    public void setIs_default(int is_default) {
        this.is_default = is_default;
    }

    public int getAddress_id() {
        return address_id;
    }

    public void setAddress_id(int address_id) {
        this.address_id = address_id;
    }

    public String getDoor_num() {
        return door_num;
    }

    public void setDoor_num(String door_num) {
        this.door_num = door_num;
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
}
