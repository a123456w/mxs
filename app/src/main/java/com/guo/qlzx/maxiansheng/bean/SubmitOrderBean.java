package com.guo.qlzx.maxiansheng.bean;

/**
 * Created by Administrator on 2018/4/27 0027.
 */

public class SubmitOrderBean {

    /**
     * order_sn : sn123456789
     * pay_time : 2018-4-6
     */

    private String order_sn;
    private String pay_time;

    public String getOrder_sn() {
        return order_sn;
    }

    public void setOrder_sn(String order_sn) {
        this.order_sn = order_sn;
    }

    public String getPay_time() {
        return pay_time;
    }

    public void setPay_time(String pay_time) {
        this.pay_time = pay_time;
    }
}
