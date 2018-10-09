package com.guo.qlzx.maxiansheng.event;

/**
 * Created by Administrator on 2018/4/19 0019.
 */

public class LoginEvent {
    private boolean login;

    public LoginEvent() {
    }

    public LoginEvent(boolean login) {
        this.login = login;
    }

    public boolean isLogin() {
        return login;
    }

    public void setLogin(boolean login) {
        this.login = login;
    }
}
