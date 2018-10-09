package com.guo.qlzx.maxiansheng.bean;

/**
 * Created by 李 on 2018/4/10.
 */

public class HomeNavigationBean {

    /**
     * id : 1
     * name : 活动专场
     * img : public/images/d32ebcN490d1c3a.jpg
     * type : 1
     */

    private int id;
    private String name;
    private String img;
    private int type;

    public HomeNavigationBean(String name){
        setName(name);
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
