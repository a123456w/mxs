package com.guo.qlzx.maxiansheng.bean;

/**
 * Created by 李 on 2018/6/6.
 */

public class OrderNumberBean  {
    /**
     * payment : 2
     * distribution : 2
     * collect : 2
     * comment : 2
     */

    private int payment;
    private int distribution;
    private int collect;
    private int comment;

    public int getPayment() {
        return payment;
    }

    public void setPayment(int payment) {
        this.payment = payment;
    }

    public int getDistribution() {
        return distribution;
    }

    public void setDistribution(int distribution) {
        this.distribution = distribution;
    }

    public int getCollect() {
        return collect;
    }

    public void setCollect(int collect) {
        this.collect = collect;
    }

    public int getComment() {
        return comment;
    }

    public void setComment(int comment) {
        this.comment = comment;
    }
}
