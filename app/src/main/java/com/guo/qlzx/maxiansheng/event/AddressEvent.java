package com.guo.qlzx.maxiansheng.event;

/**
 * Created by Administrator on 2018/5/7 0007.
 */

public class AddressEvent {


    private String consignee;
    private String mobile;
    private String address;
    private String address_id;

    public AddressEvent() {
    }

    public AddressEvent(String consignee, String mobile, String address, String address_id) {
        this.consignee = consignee;
        this.mobile = mobile;
        this.address = address;
        this.address_id = address_id;
    }

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

    public String getAddress_id() {
        return address_id;
    }

    public void setAddress_id(String address_id) {
        this.address_id = address_id;
    }
}
