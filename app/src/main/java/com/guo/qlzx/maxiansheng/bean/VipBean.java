package com.guo.qlzx.maxiansheng.bean;

/**
 * Created by chenlipeng on 2018/5/14 0014.
 * describe :
 */

public class VipBean {
    private Boolean isPaySucceed=false;

    public Boolean getPaySucceed() {
        return isPaySucceed;
    }

    public void setPaySucceed(Boolean paySucceed) {
        isPaySucceed = paySucceed;
    }

    public VipBean(boolean b) {
        isPaySucceed=b;
    }
}
