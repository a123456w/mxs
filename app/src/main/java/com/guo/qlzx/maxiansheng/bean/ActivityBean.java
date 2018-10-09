package com.guo.qlzx.maxiansheng.bean;

import java.util.List;

/**
 * Created by 李 on 2018/4/16.
 */

public class ActivityBean {

    /**
     * start_time : 12346579
     * end_time : 12346579
     * goods_list : [{"goods_id":48,"img":"/Uploads/Picture/2018-01-13/5a59b4da86e77.jpg","shop_price":0,"price":57,"goods_name":"意义洞庭湖","goods_remark":"清脆可口，新鲜，健康","unit":"袋","store_count":100,"number":12,"type":1}]
     */

    private int start_time;
    private int end_time;
    private List<ActivityListBean> goods_list;

    public int getStart_time() {
        return start_time;
    }

    public void setStart_time(int start_time) {
        this.start_time = start_time;
    }

    public int getEnd_time() {
        return end_time;
    }

    public void setEnd_time(int end_time) {
        this.end_time = end_time;
    }

    public List<ActivityListBean> getGoods_list() {
        return goods_list;
    }

    public void setGoods_list(List<ActivityListBean> goods_list) {
        this.goods_list = goods_list;
    }

}
