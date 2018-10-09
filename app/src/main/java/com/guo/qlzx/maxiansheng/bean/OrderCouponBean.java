package com.guo.qlzx.maxiansheng.bean;

/**
 * Created by 李 on 2018/5/9.
 */

public class OrderCouponBean   {
    /**
     * id : 32
     * name : 5元优惠卷
     * use_start_time : 2018-04-27
     * use_end_time : 2018-06-27
     * money : 5.00
     * condition :
     * selected : 0
     */

    private int id;
    private String name;
    private String use_start_time;
    private String use_end_time;
    private String money;
    private String condition;
    private String selected;

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

    public String getUse_start_time() {
        return use_start_time;
    }

    public void setUse_start_time(String use_start_time) {
        this.use_start_time = use_start_time;
    }

    public String getUse_end_time() {
        return use_end_time;
    }

    public void setUse_end_time(String use_end_time) {
        this.use_end_time = use_end_time;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }
}
