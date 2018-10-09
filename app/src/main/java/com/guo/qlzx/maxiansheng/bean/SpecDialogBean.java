package com.guo.qlzx.maxiansheng.bean;

/**
 * Created by Administrator on 2018/4/24 0024.
 */

public class SpecDialogBean {
    private int goods_id;
    private String img;
    private double shop_price;
    private double plus_price;
    private String spec_name;
    private int num = 1;
    private int seckillNum = -1;

    public SpecDialogBean() {
    }

    public SpecDialogBean(int goods_id, String img, double shop_price, double plus_price, String spec_name) {
        this.goods_id = goods_id;
        this.img = img;
        this.shop_price = shop_price;
        this.plus_price = plus_price;
        this.spec_name = spec_name;
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
    public int getShop_Seckillnum(){
        return seckillNum;
    }
    /*
     * 限购数量
     * */
    public void setShop_Seckillnum(int seckillNum) {
        this.seckillNum = seckillNum;
    }

    public double getPlus_price() {
        return plus_price;
    }

    public void setPlus_price(double plus_price) {
        this.plus_price = plus_price;
    }

    public String getSpec_name() {
        return spec_name;
    }

    public void setSpec_name(String spec_name) {
        this.spec_name = spec_name;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
