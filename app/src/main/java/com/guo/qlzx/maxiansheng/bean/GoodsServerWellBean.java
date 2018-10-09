package com.guo.qlzx.maxiansheng.bean;

/**
 * Created by Administrator on 2018/4/23 0023.
 */

public class GoodsServerWellBean {



    private String key;
    private String name;
    private int service_id;
    private String Service_name;
    private double shop_price;
    private double plus_price;
    private String unit;
    private boolean selected;

    public GoodsServerWellBean() {
    }

    public GoodsServerWellBean(String key, String name, int service_id, String service_name, double shop_price, double plus_price) {
        this.key = key;
        this.name = name;
        this.service_id = service_id;
        Service_name = service_name;
        this.shop_price = shop_price;
        this.plus_price = plus_price;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getService_name() {
        return Service_name;
    }

    public void setService_name(String service_name) {
        Service_name = service_name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
