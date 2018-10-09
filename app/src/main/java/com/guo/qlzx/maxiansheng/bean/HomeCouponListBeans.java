package com.guo.qlzx.maxiansheng.bean;

/**
 * Created by 李 on 2018/4/11.
 */

public class HomeCouponListBeans {
    /**
     * id : 1
     * name : 优惠优惠优惠
     * coupon_info : 优惠优惠优惠
     * money : 10
     * condition : 100
     * createnum : 100
     * send_num : 25
     * send_start_time : 1467820800
     * send_end_time : 1467820800
     * use_start_time : 1467820800
     * use_end_time : 1467820800
     * status : 1
     */

    public HomeCouponListBeans(int money,String name,String coupon_info){
        setName(name);
        setName(coupon_info);
        setMoney(money);
    }
    private int id;
    private String name;
    private String coupon_info;
    private int money;
    private int condition;
    private int createnum;
    private int send_num;
    private int send_start_time;
    private int send_end_time;
    private int use_start_time;
    private int use_end_time;
    private int status;

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

    public String getCoupon_info() {
        return coupon_info;
    }

    public void setCoupon_info(String coupon_info) {
        this.coupon_info = coupon_info;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getCondition() {
        return condition;
    }

    public void setCondition(int condition) {
        this.condition = condition;
    }

    public int getCreatenum() {
        return createnum;
    }

    public void setCreatenum(int createnum) {
        this.createnum = createnum;
    }

    public int getSend_num() {
        return send_num;
    }

    public void setSend_num(int send_num) {
        this.send_num = send_num;
    }

    public int getSend_start_time() {
        return send_start_time;
    }

    public void setSend_start_time(int send_start_time) {
        this.send_start_time = send_start_time;
    }

    public int getSend_end_time() {
        return send_end_time;
    }

    public void setSend_end_time(int send_end_time) {
        this.send_end_time = send_end_time;
    }

    public int getUse_start_time() {
        return use_start_time;
    }

    public void setUse_start_time(int use_start_time) {
        this.use_start_time = use_start_time;
    }

    public int getUse_end_time() {
        return use_end_time;
    }

    public void setUse_end_time(int use_end_time) {
        this.use_end_time = use_end_time;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
