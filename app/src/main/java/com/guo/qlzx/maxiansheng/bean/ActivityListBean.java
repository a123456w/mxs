package com.guo.qlzx.maxiansheng.bean;

/**
 * Created by 李 on 2018/4/16.
 */

public class ActivityListBean {


    /**
     * goods_id : 48
     * img : /Uploads/Picture/2018-01-13/5a59b4da86e77.jpg
     * shop_price : 0.0
     * price : 57.0
     * goods_name : 意义洞庭湖
     * goods_remark : 清脆可口，新鲜，健康
     * unit : 袋
     * store_count : 100
     * number : 12
     * type : 2
     */

    public ActivityListBean(int shop_price,String goods_name,String goods_remark,Double price,String unit,int store_count,int number,int presel_time){
        setGoods_name(goods_name);
        setShop_price(shop_price);
        setGoods_remark(goods_remark);
        setPrice(price);
        setUnit(unit);
        setStore_count(store_count);
        setNumber(number);
        setPresel_time(presel_time);
    }

    private int presel_time;
    private int goods_id;
    private String img;
    private double shop_price;
    private double price;
    private String goods_name;
    private String goods_remark;
    private String unit;
    private int store_count;
    private int number;
    private int type;

    public int getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(int goods_id) {
        this.goods_id = goods_id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public double getShop_price() {
        return shop_price;
    }

    public void setShop_price(double shop_price) {
        this.shop_price = shop_price;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public String getGoods_remark() {
        return goods_remark;
    }

    public void setGoods_remark(String goods_remark) {
        this.goods_remark = goods_remark;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getStore_count() {
        return store_count;
    }

    public void setStore_count(int store_count) {
        this.store_count = store_count;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
    public int getPresel_time() {
        return presel_time;
    }

    public void setPresel_time(int presel_time) {
        this.presel_time = presel_time;
    }
}
