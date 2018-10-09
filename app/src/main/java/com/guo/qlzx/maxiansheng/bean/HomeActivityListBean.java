package com.guo.qlzx.maxiansheng.bean;

/**
 * Created by 李 on 2018/4/10.
 */

public class HomeActivityListBean {

    /**
     * goods_id : 62
     * goods_name : 餐桌布
     * img : /public/upload/goods/2018/04-25/49022139e992abafb356a3c50833d904.jpg
     * unit : 只
     * price : 58
     * shop_price : 78.00
     * type : 1
     */

    private int goods_id;
    private String goods_name;
    private String img;
    private String unit;
    private String price;
    private String shop_price;
    private int type;

    public int getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(int goods_id) {
        this.goods_id = goods_id;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getShop_price() {
        return shop_price;
    }

    public void setShop_price(String shop_price) {
        this.shop_price = shop_price;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
