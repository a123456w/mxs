package com.guo.qlzx.maxiansheng.bean;

import java.util.List;

/**
 * Created by 李 on 2018/4/17.
 */

public class FootMarkListBean {

    /**
     * time : 2018-04-28
     * footlist : [{"date_time":"2018-04-28","goods_id":49,"goods_name":"遮光窗帘","price":374,"img":"/public/upload/goods/2018/04-25/d95f4c09c2318c761767f295176f7747.jpg","key_name":"遮光功能: 装饰+全遮光","id":159,"type":0,"spec_type":0,"status":"0"}]
     */

    private String time;
    private List<FootListBean> footlist;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<FootListBean> getFootlist() {
        return footlist;
    }

    public void setFootlist(List<FootListBean> footlist) {
        this.footlist = footlist;
    }


}
