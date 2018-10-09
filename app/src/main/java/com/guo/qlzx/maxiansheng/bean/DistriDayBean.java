package com.guo.qlzx.maxiansheng.bean;

/**
 * Created by Administrator on 2018/4/27 0027.
 */

public class DistriDayBean {
    private String name;
    private boolean selected;
    private long startTime;
    private long endTime;


    public DistriDayBean() {
    }

    public DistriDayBean(String name, boolean selected) {
        this.name = name;
        this.selected = selected;
    }

    public DistriDayBean(long startTime, long endTime) {
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }
}
