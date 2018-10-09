package com.guo.qlzx.maxiansheng.bean;

import java.util.List;

/**
 * Created by 李 on 2018/4/10.
 */

public class HomeActivityBean {
    /**
     * type : 1
     * name : 秒杀专场
     * img : public/images/d32ebcN490d1c3a.jpg
     * list : [{"goods_id":48,"img":"/Uploads/Picture/2018-01-13/5a59b4da86e77.jpg","price":0,"goods_name":"意义洞庭湖","unit":"袋"}]
     */

    public HomeActivityBean(int type,String name,List<HomeActivityListBean> listBeans){
        setType(type);
        setName(name);
        setList(listBeans);
    }
    private int type;
    private String name;
    private String img;
    private List<HomeActivityListBean> list;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public List<HomeActivityListBean> getList() {
        return list;
    }

    public void setList(List<HomeActivityListBean> list) {
        this.list = list;
    }

}
