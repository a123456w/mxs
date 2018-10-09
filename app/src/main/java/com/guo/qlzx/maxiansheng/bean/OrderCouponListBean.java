package com.guo.qlzx.maxiansheng.bean;

/**
 * Created by 李 on 2018/4/25.
 */

public class OrderCouponListBean {

    /**
     * id : 28
     * name : 新人优惠15元
     * coupon_info :
     * money : 15
     * condition : 50
     * createnum : 10000
     * send_num : 0
     * use_start_time : 1523894400
     * use_end_time : 1529078400
     * status : 1
     * type : 1
     */

    private int id;
    private String name;
    private String coupon_info;
    private int money;
    private int condition;
    private int createnum;
    private int send_num;
    private int use_start_time;
    private int use_end_time;
    private int status;
    private int type;

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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
