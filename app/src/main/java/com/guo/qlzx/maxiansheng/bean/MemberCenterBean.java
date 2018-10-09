package com.guo.qlzx.maxiansheng.bean;

import java.util.List;

/**
 * Created by 李 on 2018/4/16.
 */

public class MemberCenterBean {

    /**
     * mobile : 13845878632
     * img : 0
     * member_name : plus会员
     * new_people : 1
     * rapeseed : 456
     * plus_last_time :
     * plus_list : [{"id":1,"member_time":"30","price":"30","original_price":"30"},{"id":2,"member_time":"30","price":"30","original_price":"30"}]
     */

    private String mobile;
    private String img;
    private String member_name;
    private int new_people;
    private String rapeseed;
    private String plus_last_time;
    private List<PlusListBean> plus_list;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getMember_name() {
        return member_name;
    }

    public void setMember_name(String member_name) {
        this.member_name = member_name;
    }

    public int getNew_people() {
        return new_people;
    }

    public void setNew_people(int new_people) {
        this.new_people = new_people;
    }

    public String getRapeseed() {
        return rapeseed;
    }

    public void setRapeseed(String rapeseed) {
        this.rapeseed = rapeseed;
    }

    public String getPlus_last_time() {
        return plus_last_time;
    }

    public void setPlus_last_time(String plus_last_time) {
        this.plus_last_time = plus_last_time;
    }

    public List<PlusListBean> getPlus_list() {
        return plus_list;
    }

    public void setPlus_list(List<PlusListBean> plus_list) {
        this.plus_list = plus_list;
    }

    public static class PlusListBean {
        /**
         * id : 1
         * member_time : 30
         * price : 30
         * original_price : 30
         */

        private int id;
        private String member_time;
        private String price;
        private String original_price;

        public PlusListBean(int id,String member_time,String price,String original_price){
            setPrice(price);
            setId(id);
            setMember_time(member_time);
            setOriginal_price(original_price);
        }
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getMember_time() {
            return member_time;
        }

        public void setMember_time(String member_time) {
            this.member_time = member_time;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getOriginal_price() {
            return original_price;
        }

        public void setOriginal_price(String original_price) {
            this.original_price = original_price;
        }
    }
}
