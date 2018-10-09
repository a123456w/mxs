package com.guo.qlzx.maxiansheng.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/4/18.
 */

public class ShowSearchBean {

    private List<ClassifyBean> classify;
    private List<BrandBean> brand;
    private List<RegionBean> region;
    private List<GoodsListBean> goods_list;

    public List<ClassifyBean> getClassify() {
        return classify;
    }

    public void setClassify(List<ClassifyBean> classify) {
        this.classify = classify;
    }

    public List<BrandBean> getBrand() {
        return brand;
    }

    public void setBrand(List<BrandBean> brand) {
        this.brand = brand;
    }

    public List<RegionBean> getRegion() {
        return region;
    }

    public void setRegion(List<RegionBean> region) {
        this.region = region;
    }

    public List<GoodsListBean> getGoods_list() {
        return goods_list;
    }

    public void setGoods_list(List<GoodsListBean> goods_list) {
        this.goods_list = goods_list;
    }

    public static class ClassifyBean {
        /**
         * id : 1
         * name : 水果蔬菜
         */

        private String id;
        private String name;
        public ClassifyBean(String id,String name){
            setId(id);
            setName(name);
        }
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class BrandBean {
        /**
         * id : 1
         * name : 水果蔬菜
         */

        private int id;
        private String name;
        private boolean isTrue;

        public BrandBean() {
            this.isTrue = false;
        }

        public boolean isTrue() {
            return isTrue;
        }

        public void setTrue(boolean aTrue) {
            isTrue = aTrue;
        }

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

    public static class RegionBean {
        /**
         * id : 1
         * name : 水果蔬菜
         */

        private int id;
        private String name;
        private boolean isTrue;

        public RegionBean() {
            this.isTrue = false;
        }

        public boolean isTrue() {
            return isTrue;
        }

        public void setTrue(boolean aTrue) {
            isTrue = aTrue;
        }

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

    public static class GoodsListBean {
        /**
         * goods_id : 48
         * img : /Uploads/Picture/2018-01-13/5a59b4da86e77.jpg
         * shop_price : 0
         * plus_price : 0
         * goods_name : 意义洞庭湖
         * unit : 袋
         * spec_type : 1
         * type : 0
         */

        private int goods_id;
        private String img;
        private String shop_price;
        private String plus_price;
        private String goods_name;
        private String unit;
        private int spec_type;
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
}
