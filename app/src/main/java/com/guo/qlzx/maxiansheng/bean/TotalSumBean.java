package com.guo.qlzx.maxiansheng.bean;

/**
 * Created by 李 on 2018/4/18.
 */

public class TotalSumBean
{
    /**
     * user_money : 456
     * rapeseed : 50.00
     */

    public TotalSumBean(String user_money,String rapeseed){
        setRapeseed(rapeseed);
        setUser_money(user_money);
    }
    private String user_money;
    private String rapeseed;

    public String getUser_money() {
        return user_money;
    }

    public void setUser_money(String user_money) {
        this.user_money = user_money;
    }

    public String getRapeseed() {
        return rapeseed;
    }

    public void setRapeseed(String rapeseed) {
        this.rapeseed = rapeseed;
    }
}
