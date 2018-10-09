package com.guo.qlzx.maxiansheng.bean;

/**
 * Created by 李 on 2018/4/10.
 */

public class HomeRecommendListBean {

    /**
     * goods_id : 48
     * img : /Uploads/Picture/2018-01-13/5a59b4da86e77.jpg
     * shop_price : 0.0
     * plus_price : 57.0
     * goods_name : 意义洞庭湖
     * goods_remark : 清脆可口，新鲜，健康
     * unit : 袋
     * spec_key : 1
     * spec_key_name : 送达
     * spec_type : 0
     * type : 0
     */
    public HomeRecommendListBean(String goods_name,String price, String key_name){
        setGoods_name(goods_name);
        setKey_name(key_name);
        setPrice(price);
    }
    public HomeRecommendListBean(){}
    private int id;
    private String price;
    private String key_name;
    private int goods_id;
    private String img;
    private double shop_price;
    private double plus_price;
    private String goods_name;
    private String goods_remark;
    private String unit;
    private String spec_key;
    private String spec_key_name;
    private int spec_type;
    private int type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getKey_name() {
        return key_name;
    }

    public void setKey_name(String key_name) {
        this.key_name = key_name;
    }

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

    public double getPlus_price() {
        return plus_price;
    }

    public void setPlus_price(double plus_price) {
        this.plus_price = plus_price;
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

    public int getSpec_type() {
        return spec_type;
    }

    public void setSpec_type(int spec_type) {
        this.spec_type = spec_type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
