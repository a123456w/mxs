package com.guo.qlzx.maxiansheng.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/4/23 0023.
 */

public class ServerBean {


    /**
     * unit : 盒
     * servicelist : [{"id":1,"name":"切片"}]
     * speclist : [{"key":"167_173","key_name":"重量:300g","shop_price":9.9,"plus_price":9.9,"store_count":100}]
     * service_price : [{"key":"167_173","service_id":1,"shop_price":9.9,"plus_price":9.9}]
     */

    private String unit;
    private List<ServicelistBean> servicelist;
    private List<SpeclistBean> speclist;
    private List<ServicePriceBean> service_price;

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public List<ServicelistBean> getServicelist() {
        return servicelist;
    }

    public void setServicelist(List<ServicelistBean> servicelist) {
        this.servicelist = servicelist;
    }

    public List<SpeclistBean> getSpeclist() {
        return speclist;
    }

    public void setSpeclist(List<SpeclistBean> speclist) {
        this.speclist = speclist;
    }

    public List<ServicePriceBean> getService_price() {
        return service_price;
    }

    public void setService_price(List<ServicePriceBean> service_price) {
        this.service_price = service_price;
    }

    public static class ServicelistBean {
        /**
         * id : 1
         * name : 切片
         */

        private int id;
        private String name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class SpeclistBean {
        /**
         * key : 167_173
         * key_name : 重量:300g
         * shop_price : 9.9
         * plus_price : 9.9
         * store_count : 100
         */

        private String key;
        private String key_name;
        private double shop_price;
        private double plus_price;
        private int store_count;
        private boolean selected;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getKey_name() {
            return key_name;
        }

        public void setKey_name(String key_name) {
            this.key_name = key_name;
        }

        public double getShop_price() {
            return shop_price;
        }

        public void setShop_price(double shop_price) {
            this.shop_price = shop_price;
        }

        public double getPlus_price() {
            return plus_price;
        }

        public void setPlus_price(double plus_price) {
            this.plus_price = plus_price;
        }

        public int getStore_count() {
            return store_count;
        }

        public void setStore_count(int store_count) {
            this.store_count = store_count;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }
    }

    public static class ServicePriceBean {
        /**
         * key : 167_173
         * service_id : 1
         * shop_price : 9.9
         * plus_price : 9.9
         *
         */

        private String key;
        private int service_id;
        private String shop_price;
        private String plus_price;
        private String service_name;
        private boolean selected;
        private int seckillnum;
        public int getSeckillnum() {
            return seckillnum;
        }

        public void setSeckillnum(int seckillnum) {
            this.seckillnum = seckillnum;
        }


        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public int getService_id() {
            return service_id;
        }

        public void setService_id(int service_id) {
            this.service_id = service_id;
        }

        public String getShop_price() {
            return shop_price;
        }

        public void setShop_price(String shop_price) {
            this.shop_price = shop_price;
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

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }
    }
}
