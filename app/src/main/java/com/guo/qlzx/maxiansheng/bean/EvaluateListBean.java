package com.guo.qlzx.maxiansheng.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/4/19 0019.
 */

public class EvaluateListBean {
    /**
     * portrait : public/images/d32ebcN490d1c3a.jpg
     * username : public/images/d32ebcN490d1c3a.jpg
     * create_time : public/images/d32ebcN490d1c3a.jpg
     * content : public/images/d32ebcN490d1c3a.jpg
     * describe_score : 5
     * img : ["public/images/d32ebcN490d1c3a.jpg","public/images/d32ebcN490d1c3a.jpg","public/images/d32ebcN490d1c3a.jpg"]
     */

    private String portrait;
    private String username;
    private String create_time;
    private String content;
    private float describe_score;
    private List<String> img;

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public float getDescribe_score() {
        return describe_score;
    }

    public void setDescribe_score(float describe_score) {
        this.describe_score = describe_score;
    }

    public List<String> getImg() {
        return img;
    }

    public void setImg(List<String> img) {
        this.img = img;
    }
}
