package com.guo.qlzx.maxiansheng.event;

/**
 * create by xuxx on 2018/9/14
 */
public class WxSuccessChangedEvent {
    private int code;
    private int type;
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
