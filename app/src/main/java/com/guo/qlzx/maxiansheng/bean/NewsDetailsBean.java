package com.guo.qlzx.maxiansheng.bean;

/**
 * Created by 李 on 2018/5/15.
 */

public class NewsDetailsBean {
    /**
     * title : 马鲜生上市了
     * img :
     * content : 马鲜生上市了马鲜生上市了马鲜生上市了马鲜生上市了马鲜生上市了马鲜生上市了马鲜生上市了
     * create_time : 2018-04-04
     * share_url : http://www.maxiansheng.com/newsDetail?key=76fe97759aa27a0c&&id=3
     */

    private String title;
    private String img;
    private String content;
    private String create_time;
    private String share_url;
    private String head_content;

    public String getHead_content() {
        return head_content;
    }

    public void setHead_content(String head_content) {
        this.head_content = head_content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getShare_url() {
        return share_url;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }
}
