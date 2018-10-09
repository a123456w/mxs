package com.guo.qlzx.maxiansheng.bean;

/**
 * Created by Administrator on 2018/4/27 0027.
 */

public class DistriTimeBean {

    /**
     * begin_time : 06:30:00
     * end_start : 23:59
     * now_time : 1524808382
     * times : 1800
     */

    public DistriTimeBean(){}
    private String begin_time;
    private String end_start;
    private String now_time;
    private int times;

    public String getBegin_time() {
        return begin_time;
    }

    public void setBegin_time(String begin_time) {
        this.begin_time = begin_time;
    }

    public String getEnd_start() {
        return end_start;
    }

    public void setEnd_start(String end_start) {
        this.end_start = end_start;
    }

    public String getNow_time() {
        return now_time;
    }

    public void setNow_time(String now_time) {
        this.now_time = now_time;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }
}
