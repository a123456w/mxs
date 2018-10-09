package com.guo.qlzx.maxiansheng.bean;

/**
 * Created by 李 on 2018/4/28.
 * 一级列表
 *
 */

public class MessageCenterBean {

    /**
     * unread_system_num : 1
     * unread_person_num : 2
     */

    private String unread_system_num;
    private String unread_person_num;

    public String getUnread_system_num() {
        return unread_system_num;
    }

    public void setUnread_system_num(String unread_system_num) {
        this.unread_system_num = unread_system_num;
    }

    public String getUnread_person_num() {
        return unread_person_num;
    }

    public void setUnread_person_num(String unread_person_num) {
        this.unread_person_num = unread_person_num;
    }
}
