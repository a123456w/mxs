package com.guo.qlzx.maxiansheng.event;

/**
 * Created by guo on 2017/7/13.
 */

public class WxPayEvent {
    public String tag;
    public int code;

    public WxPayEvent(String tag, int code) {
        this.tag = tag;
        this.code = code;
    }
}
