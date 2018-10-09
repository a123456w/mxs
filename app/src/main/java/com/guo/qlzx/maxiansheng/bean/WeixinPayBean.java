package com.guo.qlzx.maxiansheng.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 描述
 * <p>
 * Created by liyujiang on 2017/9/4 15:53.
 */
public class WeixinPayBean implements Serializable {


    /**
     * appid : wx29c790d4f2786062
     * prepayid : wx2018031315365671451e8be00007015096
     * partnerid : 1499807162
     * package : Sign=WXPay
     * noncestr : 3Va5qWVXrqQpJqcF
     * timestamp : 1520926593
     * sign : 41862202D250CA05B07F08939973CB89
     */

    private String appid;
    private String prepayid;
    private String partnerid;
    @SerializedName("package")
    private String packageX;
    private String noncestr;
    private int timestamp;
    private String sign;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getPrepayid() {
        return prepayid;
    }

    public void setPrepayid(String prepayid) {
        this.prepayid = prepayid;
    }

    public String getPartnerid() {
        return partnerid;
    }

    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }

    public String getPackageX() {
        return packageX;
    }

    public void setPackageX(String packageX) {
        this.packageX = packageX;
    }

    public String getNoncestr() {
        return noncestr;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
