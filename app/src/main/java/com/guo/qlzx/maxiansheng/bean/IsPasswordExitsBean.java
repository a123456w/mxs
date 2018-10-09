package com.guo.qlzx.maxiansheng.bean;

/**
 * Created by 李 on 2018/5/4.
 * 支付密码是否存在
 */

public class IsPasswordExitsBean {
    /**
     * paypwd_status : 0
     * 0 已设置交易密码 1没有交易密码
     */

    private int paypwd_status;

    public int getPaypwd_status() {
        return paypwd_status;
    }

    public void setPaypwd_status(int paypwd_status) {
        this.paypwd_status = paypwd_status;
    }
}
