package com.guo.qlzx.maxiansheng.bean;

/**
 * Created by 李 on 2018/4/10.
 */

public class HomeClassifyListBean {
    /**
     * goods_id : 48
     * img : /Uploads/Picture/2018-01-13/5a59b4da86e77.jpg
     * shop_price : 0.0
     * plus_price : 0.0
     * goods_name : 意义洞庭湖
     * unit : 袋
     */

    public HomeClassifyListBean(double shop_price,double plus_price,String goods_name,String unit){
        setGoods_name(goods_name);
        setUnit(unit);
        setShop_price(shop_price);
        setPlus_price(plus_price);
    }
    private int goods_id;
    private String img;
    private double shop_price;
    private double plus_price;
    private String goods_name;
    private String unit;

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

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
