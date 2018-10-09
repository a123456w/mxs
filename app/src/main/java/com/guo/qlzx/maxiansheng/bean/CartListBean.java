package com.guo.qlzx.maxiansheng.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/4/17.
 */

public class CartListBean {

    /**
     * total_fee : 5
     * goods_num : 1
     * showcartList : [{"id":12,"goods_id":160,"goods_sn":"TP0000160","goods_name":"猪肉啊","market_price":11,"goods_price":5,"member_goods_price":5,"goods_num":1,"service_id":165,"service_name":"切片","service_price":123,"service_unit":"单位","spec_key":165,"spec_key_name":"切片:1斤-1.5斤","selected":1,"add_time":1522906780,"prom_type":0,"prom_id":0,"shop_id":1,"total_fee":5,"goods_fee":5}]
     */

    private String total_fee;
    private int goods_num;
    private List<ShowcartListBean> showcartList;

    public String getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(String total_fee) {
        this.total_fee = total_fee;
    }

    public int getGoods_num() {
        return goods_num;
    }

    public void setGoods_num(int goods_num) {
        this.goods_num = goods_num;
    }

    public List<ShowcartListBean> getShowcartList() {
        return showcartList;
    }

    public void setShowcartList(List<ShowcartListBean> showcartList) {
        this.showcartList = showcartList;
    }

    public static class ShowcartListBean {
        /**
         * id : 12
         * goods_id : 160
         * goods_sn : TP0000160
         * goods_name : 猪肉啊
         * market_price : 11
         * goods_price : 5
         * member_goods_price : 5
         * goods_num : 1
         * service_id : 165
         * service_name : 切片
         * service_price : 123
         * service_unit : 单位
         * spec_key : 165
         * spec_key_name : 切片:1斤-1.5斤
         * selected : 1
         * add_time : 1522906780
         * prom_type : 0
         * prom_id : 0
         * shop_id : 1
         * total_fee : 5
         * goods_fee : 5
         * img:
         * store_count:100
         */

        private int id;
        private int goods_id;
        private String goods_sn;
        private String goods_name;
        private String market_price;
        private String goods_price;
        private String member_goods_price;
        private int goods_num;
        private int service_id;
        private String service_name;
        private String service_price;
        private String service_unit;
        private String spec_key;
        private String spec_key_name;
        private int selected;
        private int add_time;
        private int prom_type;
        private int prom_id;
        private int shop_id;
        private String total_fee;
        private int goods_fee;
        private String img;
        private String unit;
        private int store_count;
        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getGoods_id() {
            return goods_id;
        }

        public void setGoods_id(int goods_id) {
            this.goods_id = goods_id;
        }

        public String getGoods_sn() {
            return goods_sn;
        }

        public void setGoods_sn(String goods_sn) {
            this.goods_sn = goods_sn;
        }

        public String getGoods_name() {
            return goods_name;
        }

        public void setGoods_name(String goods_name) {
            this.goods_name = goods_name;
        }

        public String getMarket_price() {
            return market_price;
        }

        public void setMarket_price(String market_price) {
            this.market_price = market_price;
        }

        public String getGoods_price() {
            return goods_price;
        }

        public void setGoods_price(String goods_price) {
            this.goods_price = goods_price;
        }

        public String getMember_goods_price() {
            return member_goods_price;
        }

        public void setMember_goods_price(String member_goods_price) {
            this.member_goods_price = member_goods_price;
        }

        public int getGoods_num() {
            return goods_num;
        }

        public void setGoods_num(int goods_num) {
            this.goods_num = goods_num;
        }

        public int getService_id() {
            return service_id;
        }

        public void setService_id(int service_id) {
            this.service_id = service_id;
        }

        public String getService_name() {
            return service_name;
        }

        public void setService_name(String service_name) {
            this.service_name = service_name;
        }

        public String getService_price() {
            return service_price;
        }

        public void setService_price(String service_price) {
            this.service_price = service_price;
        }

        public String getService_unit() {
            return service_unit;
        }

        public void setService_unit(String service_unit) {
            this.service_unit = service_unit;
        }

        public String getSpec_key() {
            return spec_key;
        }

        public void setSpec_key(String spec_key) {
            this.spec_key = spec_key;
        }

        public String getSpec_key_name() {
            return spec_key_name;
        }

        public void setSpec_key_name(String spec_key_name) {
            this.spec_key_name = spec_key_name;
        }

        public int getSelected() {
            return selected;
        }

        public void setSelected(int selected) {
            this.selected = selected;
        }

        public int getAdd_time() {
            return add_time;
        }

        public void setAdd_time(int add_time) {
            this.add_time = add_time;
        }

        public int getProm_type() {
            return prom_type;
        }

        public void setProm_type(int prom_type) {
            this.prom_type = prom_type;
        }

        public int getProm_id() {
            return prom_id;
        }

        public void setProm_id(int prom_id) {
            this.prom_id = prom_id;
        }

        public int getShop_id() {
            return shop_id;
        }

        public void setShop_id(int shop_id) {
            this.shop_id = shop_id;
        }

        public String getTotal_fee() {
            return total_fee;
        }

        public void setTotal_fee(String total_fee) {
            this.total_fee = total_fee;
        }

        public int getGoods_fee() {
            return goods_fee;
        }

        public void setGoods_fee(int goods_fee) {
            this.goods_fee = goods_fee;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public int getStore_count() {
            return store_count;
        }

        public void setStore_count(int store_count) {
            this.store_count = store_count;
        }
    }
}
