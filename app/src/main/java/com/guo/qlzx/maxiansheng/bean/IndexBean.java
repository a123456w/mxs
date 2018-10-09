package com.guo.qlzx.maxiansheng.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/4/19.
 */

public class IndexBean {

    /**
     * receiving : {"status":1,"consignee":"小红","mobile":"13635656565","province":"河南省","city":"邓州市","district":"桑庄镇","address":"沙河高教园5栋2号"}
     * orderList : [{"goods_id":"23","goods_name":"密云慢蛋","goods_num":"1","goods_price":"20","plus_price":"18","service_name":"清洗:1元/盒","goods_img":"/public/img/1.jpg","spec_key_name":"380g/盒"},{"goods_id":"24","goods_name":"黑猪肉","goods_num":"2","goods_price":"20","plus_price":"20","service_name":"清洗:1元/盒","goods_img":"/public/img/1.jpg","spec_key_name":"100g/元"}]
     * "food":{"rapeseed":589,"user_money":"97619.47","goods_money":399,"order_money":451.5,"fee":52.5}
     */

    private ReceivingBean receiving;
    private FoodBean food;
    private List<OrderListBean> orderList;

    public ReceivingBean getReceiving() {
        return receiving;
    }

    public void setReceiving(ReceivingBean receiving) {
        this.receiving = receiving;
    }

    public FoodBean getFood() {
        return food;
    }

    public void setFood(FoodBean food) {
        this.food = food;
    }

    public List<OrderListBean> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<OrderListBean> orderList) {
        this.orderList = orderList;
    }

    public static class ReceivingBean {
        /**
         * status : 1
         * consignee : 小红
         * mobile : 13635656565
         * province : 河南省
         * city : 邓州市
         * district : 桑庄镇
         * address : 沙河高教园5栋2号
         * address_id:""
         */

        private int status;
        private String consignee;
        private String mobile;
        private String province;
        private String city;
        private String district;
        private String address;
        private String address_id;

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
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

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
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

    public static class FoodBean {
        /**
         * rapeseed : 1000
         * user_money : 200
         * order_money : 29
         * fee : 10
         */

        private String rapeseed;
        private String user_money;
        private String goods_money;
        private String order_money;
        private String fee;
        private String user_rapeseed;
        private String differ_money;

        public String getDiffer_money() {
            return differ_money;
        }

        public void setDiffer_money(String differ_money) {
            this.differ_money = differ_money;
        }

        public String getRapeseed() {
            return rapeseed;
        }

        public void setRapeseed(String rapeseed) {
            this.rapeseed = rapeseed;
        }

        public String getUser_money() {
            return user_money;
        }

        public void setUser_money(String user_money) {
            this.user_money = user_money;
        }

        public String getOrder_money() {
            return order_money;
        }

        public void setOrder_money(String order_money) {
            this.order_money = order_money;
        }

        public String getFee() {
            return fee;
        }

        public void setFee(String fee) {
            this.fee = fee;
        }

        public String getGoods_money() {
            return goods_money;
        }

        public void setGoods_money(String goods_money) {
            this.goods_money = goods_money;
        }

        public String getUser_rapeseed() {
            return user_rapeseed;
        }

        public void setUser_rapeseed(String user_rapeseed) {
            this.user_rapeseed = user_rapeseed;
        }
    }

    public static class OrderListBean {
        /**
         * goods_id : 23
         * goods_name : 密云慢蛋
         * goods_num : 1
         * goods_price : 20
         * plus_price : 18
         * service_name : 清洗:1元/盒
         * service_price:"0.00"
         * goods_img : /public/img/1.jpg
         * spec_key_name : 380g
         * unit : 盒
         * order_price:""
         * price :50
         */

        private String goods_id;
        private String goods_name;
        private String goods_num;
        private String goods_price;
        private String plus_price;
        private String service_name;
        private String goods_img;
        private String spec_key_name;
        private String unit;
        private String order_price;
        private String service_price;
        private String price;

        public String getGoods_id() {
            return goods_id;
        }

        public void setGoods_id(String goods_id) {
            this.goods_id = goods_id;
        }

        public String getGoods_name() {
            return goods_name;
        }

        public void setGoods_name(String goods_name) {
            this.goods_name = goods_name;
        }

        public String getGoods_num() {
            return goods_num;
        }

        public void setGoods_num(String goods_num) {
            this.goods_num = goods_num;
        }

        public String getGoods_price() {
            return goods_price;
        }

        public void setGoods_price(String goods_price) {
            this.goods_price = goods_price;
        }

        public String getPlus_price() {
            return plus_price;
        }

        public void setPlus_price(String plus_price) {
            this.plus_price = plus_price;
        }

        public String getService_name() {
            return service_name;
        }

        public void setService_name(String service_name) {
            this.service_name = service_name;
        }

        public String getGoods_img() {
            return goods_img;
        }

        public void setGoods_img(String goods_img) {
            this.goods_img = goods_img;
        }

        public String getSpec_key_name() {
            return spec_key_name;
        }

        public void setSpec_key_name(String spec_key_name) {
            this.spec_key_name = spec_key_name;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public String getOrder_price() {
            return order_price;
        }

        public void setOrder_price(String order_price) {
            this.order_price = order_price;
        }

        public String getService_price() {
            return service_price;
        }

        public void setService_price(String service_price) {
            this.service_price = service_price;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }
    }
}
