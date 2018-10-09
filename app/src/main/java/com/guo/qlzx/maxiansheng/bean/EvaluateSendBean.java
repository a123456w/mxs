package com.guo.qlzx.maxiansheng.bean;

/**
 * Created by Administrator on 2018/5/3 0003.
 * 评价时 传参数实体类
 */

public class EvaluateSendBean {

    /**
     * goods_id : 1
     * describe_score : 5
     * content : 大苹果
     */

    private String goods_id;
    private String describe_score;
    private String content;

    public EvaluateSendBean(String goods_id, String describe_score, String content) {
        this.goods_id = goods_id;
        this.describe_score = describe_score;
        this.content = content;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getDescribe_score() {
        return describe_score;
    }

    public void setDescribe_score(String describe_score) {
        this.describe_score = describe_score;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
