package com.guo.qlzx.maxiansheng.bean;

/**
 * Created by Administrator on 2018/5/14 0014.
 */

public class CheckVersionBean {

    /**
     * a_version : 1.0
     * a_url : asda
     * a_update_log : 阿德
     * a_size : 10M
     * a_status : 1
     */

    private String a_version;
    private String a_url;
    private String a_update_log;
    private String a_size;
    private String a_status;

    public CheckVersionBean(String a_url){
        setA_url(a_url);
    }
    public String getA_version() {
        return a_version;
    }

    public void setA_version(String a_version) {
        this.a_version = a_version;
    }

    public String getA_url() {
        return a_url;
    }

    public void setA_url(String a_url) {
        this.a_url = a_url;
    }

    public String getA_update_log() {
        return a_update_log;
    }

    public void setA_update_log(String a_update_log) {
        this.a_update_log = a_update_log;
    }

    public String getA_size() {
        return a_size;
    }

    public void setA_size(String a_size) {
        this.a_size = a_size;
    }

    public String getA_status() {
        return a_status;
    }

    public void setA_status(String a_status) {
        this.a_status = a_status;
    }
}
