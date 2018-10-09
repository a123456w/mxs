package com.guo.qlzx.maxiansheng.bean;

/**
 * Created by 李 on 2018/4/25.
 */

public class SetFeedbackTypeListBean {
    /**
     * type_name : asd
     * id : 1
     */

    private String type_name;
    private int id;

    public SetFeedbackTypeListBean(String type_name,int id){
        setType_name(type_name);
        setId(id);
    }
    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
