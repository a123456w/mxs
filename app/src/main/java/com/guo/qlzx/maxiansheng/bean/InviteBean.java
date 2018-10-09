package com.guo.qlzx.maxiansheng.bean;

import java.util.List;

/**
 * Created by 李 on 2018/4/16.
 */

public class InviteBean {
    /**
     * get_img : /Uploads/Picture/2018-01-13/5a59b4da86e77.jpg
     * new_img : /Uploads/Picture/2018-01-13/5a59b4da86e77.jpg
     * share_img : /Uploads/Picture/2018-01-13/5a59b4da86e77.jpg
     * title : 马鲜生火爆上市啦，现在注册立项50元新人大礼包
     * content : 马鲜生火爆上市啦，现在注册立项50元新人大礼包
     * share_url : http://tpshop.com/index.php/Admin/Index/index
     * invitation : 10
     * already_ordered : 1
     * reward : 10
     * list : ["aj**as成功邀请ds**daf获得6元","aj**as成功邀请ds**daf获得6元","aj**as成功邀请ds**daf获得6元","aj**as成功邀请ds**daf获得6元","aj**as成功邀请ds**daf获得6元","aj**as成功邀请ds**daf获得6元"]
     */

    private String get_img;
    private String new_img;
    private String share_img;
    private String title;
    private String content;
    private String share_url;
    private int invitation;
    private int already_ordered;
    private int reward;
    private List<String> list;

    public String getGet_img() {
        return get_img;
    }

    public void setGet_img(String get_img) {
        this.get_img = get_img;
    }

    public String getNew_img() {
        return new_img;
    }

    public void setNew_img(String new_img) {
        this.new_img = new_img;
    }

    public String getShare_img() {
        return share_img;
    }

    public void setShare_img(String share_img) {
        this.share_img = share_img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getShare_url() {
        return share_url;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }

    public int getInvitation() {
        return invitation;
    }

    public void setInvitation(int invitation) {
        this.invitation = invitation;
    }

    public int getAlready_ordered() {
        return already_ordered;
    }

    public void setAlready_ordered(int already_ordered) {
        this.already_ordered = already_ordered;
    }

    public int getReward() {
        return reward;
    }

    public void setReward(int reward) {
        this.reward = reward;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }
}
