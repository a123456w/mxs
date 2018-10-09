package com.guo.qlzx.maxiansheng.bean;

import java.util.List;

/**
 * Created by 李 on 2018/5/29.
 */

public class LogisticsBean {
    /**
     * order_sn : 1
     * transporterName : 郑国梁
     * transporterPhone : 18311071025
     * transporterLng : 经度
     * transporterLat : 纬度
     * userName : 收货人姓名
     * userPhone : 18311071025
     * userLng : 用户的经度
     * userLat : 用户的纬度
     */

    /**
     * staff_name : silence
     * staff_iphone : 13220133987
     * order_status_info : [{"time":"1528189250","order_status":"配送中"},{"time":"1528189244","order_status":"已确认"}]
     * telephone : 13118914190
     */

    /**
     * order_status : 12
     * order_type : 12
     */
    private String order_sn;
    private String transporterName;
    private String transporterPhone;
    private String transporterLng;
    private String transporterLat;
    private String userName;
    private String userPhone;
    private String userLng;
    private String userLat;

    private String staff_name;
    private String staff_iphone;
    private String telephone;
    private List<OrderStatusInfoBean> order_status_info;

    private String order_status;
    private String order_type;


    public String getOrder_sn() {
        return order_sn;
    }

    public void setOrder_sn(String order_sn) {
        this.order_sn = order_sn;
    }

    public String getTransporterName() {
        return transporterName;
    }

    public void setTransporterName(String transporterName) {
        this.transporterName = transporterName;
    }

    public String getTransporterPhone() {
        return transporterPhone;
    }

    public void setTransporterPhone(String transporterPhone) {
        this.transporterPhone = transporterPhone;
    }

    public String getTransporterLng() {
        return transporterLng;
    }

    public void setTransporterLng(String transporterLng) {
        this.transporterLng = transporterLng;
    }

    public String getTransporterLat() {
        return transporterLat;
    }

    public void setTransporterLat(String transporterLat) {
        this.transporterLat = transporterLat;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserLng() {
        return userLng;
    }

    public void setUserLng(String userLng) {
        this.userLng = userLng;
    }

    public String getUserLat() {
        return userLat;
    }

    public void setUserLat(String userLat) {
        this.userLat = userLat;
    }

    public String getStaff_name() {
        return staff_name;
    }

    public void setStaff_name(String staff_name) {
        this.staff_name = staff_name;
    }

    public String getStaff_iphone() {
        return staff_iphone;
    }

    public void setStaff_iphone(String staff_iphone) {
        this.staff_iphone = staff_iphone;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public List<OrderStatusInfoBean> getOrder_status_info() {
        return order_status_info;
    }

    public void setOrder_status_info(List<OrderStatusInfoBean> order_status_info) {
        this.order_status_info = order_status_info;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getOrder_type() {
        return order_type;
    }

    public void setOrder_type(String order_type) {
        this.order_type = order_type;
    }

    public static class OrderStatusInfoBean {
        /**
         * time : 1528189250
         * order_status : 配送中
         */

        private String time;
        private String order_status;

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getOrder_status() {
            return order_status;
        }

        public void setOrder_status(String order_status) {
            this.order_status = order_status;
        }
    }
}
