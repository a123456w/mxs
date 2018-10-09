package com.guo.qlzx.maxiansheng.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/4/16.
 * 分类首页的bean
 */

public class ClassificationBean {

    /**
     * img : {"ad_code":"/public/upload/ad/2018/04-25/842ef21f21a8c1f0f6466dceeeb3a7a2.png","ad_link":""}
     * list : [{"id":1,"name":"海鲜肉类","img":"/public/upload/category/2018/08-13/5ca56bfa79d4dceacce7d09926aaa586.jpg"},{"id":3,"name":"果蔬调料","img":"/public/upload/category/2018/08-13/5ea3c3d721ae7fd31dd149c5f3615b95.jpg"},{"id":2,"name":"酒水饮料","img":"/public/upload/category/2018/08-13/56f61e3a78a5c5210157ffb56ebb4532.jpg"},{"id":4,"name":"主食杂粮","img":"/public/upload/category/2018/08-13/dbda6c6ecedf6a0c059a3f05c2e961ed.jpg"},{"id":5,"name":"熟食干果","img":"/public/upload/category/2018/08-13/475b3dd5b719f8be098e95d6074bf591.jpg"}]
     */

    private ImgBean img;
    private List<ListBean> list;

    public ImgBean getImg() {
        return img;
    }

    public void setImg(ImgBean img) {
        this.img = img;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ImgBean {
        /**
         * ad_code : /public/upload/ad/2018/04-25/842ef21f21a8c1f0f6466dceeeb3a7a2.png
         * ad_link :
         */

        private String ad_code;
        private String ad_link;

        public String getAd_code() {
            return ad_code;
        }

        public void setAd_code(String ad_code) {
            this.ad_code = ad_code;
        }

        public String getAd_link() {
            return ad_link;
        }

        public void setAd_link(String ad_link) {
            this.ad_link = ad_link;
        }
    }

    public static class ListBean {
        /**
         * id : 1
         * name : 海鲜肉类
         * img : /public/upload/category/2018/08-13/5ca56bfa79d4dceacce7d09926aaa586.jpg
         */

        private String id;
        private String name;
        private String img;
        private boolean isChecked;
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

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }
    }
}
