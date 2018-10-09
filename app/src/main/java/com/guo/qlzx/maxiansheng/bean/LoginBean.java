package com.guo.qlzx.maxiansheng.bean;

/**
 * Created by Administrator on 2018/4/12 0012.
 * 登陆返回实体
 */

public class LoginBean {

    /**
     * token : eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9
     */

    private String token;
    private int type;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
