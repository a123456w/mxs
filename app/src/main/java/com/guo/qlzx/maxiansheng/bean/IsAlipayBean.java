package com.guo.qlzx.maxiansheng.bean;

/**
 * Created by 李 on 2018/5/22.
 */

public class IsAlipayBean  {

    /**
     * alipay_status : 1
     * bank_card : 12364985465
     * realname : 张三
     */

    private int alipay_status;
    private String bank_card;
    private String realname;

    public int getAlipay_status() {
        return alipay_status;
    }

    public void setAlipay_status(int alipay_status) {
        this.alipay_status = alipay_status;
    }

    public String getBank_card() {
        return bank_card;
    }

    public void setBank_card(String bank_card) {
        this.bank_card = bank_card;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }
}
