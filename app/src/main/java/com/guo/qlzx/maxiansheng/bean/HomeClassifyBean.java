package com.guo.qlzx.maxiansheng.bean;

import java.util.List;

/**
 * Created by 李 on 2018/4/10.
 */

public class HomeClassifyBean {


    /**
     * id : 1
     * name : 蔬菜专场
     * img : public/images/d32ebcN490d1c3a.jpg
     * list : [{"goods_id":48,"img":"/Uploads/Picture/2018-01-13/5a59b4da86e77.jpg","shop_price":0,"plus_price":0,"goods_name":"意义洞庭湖","unit":"袋"}]
     */

    private int id;
    private String name;
    private String img;
    private List<HomeClassifyListBean> list;

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

    public List<HomeClassifyListBean> getList() {
        return list;
    }

    public void setList(List<HomeClassifyListBean> list) {
        this.list = list;
    }

}
