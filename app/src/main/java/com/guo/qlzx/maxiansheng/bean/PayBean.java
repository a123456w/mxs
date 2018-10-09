package com.guo.qlzx.maxiansheng.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by chenlipeng on 2018/5/14 0014.
 * describe :
 */

public class PayBean {
    /**
     * prepayid : wx2018031315365671451e8be00007015096
     * appid : wx29c790d4f2786062
     * partnerid : 1499807162
     * package : Sign=WXPay
     * noncestr : 3Va5qWVXrqQpJqcF
     * timestamp : 1520926593
     * sign : 41862202D250CA05B07F08939973CB89
     * order_number : alipay_sdk=alipay-sdk-php-20161101&amp;app_id=2018020202131253&amp;biz_content=%7B%22body%22%3A%22%5Cu5546%5Cu54c1%5Cu540d%5Cu79f0%22%2C%22subject%22%3A%22%5Cu5546%5Cu54c1%5Cu540d%5Cu79f0%22%2C%22out_trade_no%22%3A%222018024080504%22%2C%22timeout_express%22%3A%2230m%22%2C%22total_amount%22%3A%22100%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%7D&amp;charset=UTF-8&amp;format=json&amp;method=alipay.trade.app.pay&amp;notify_url=http%3A%2F%2Fgc.qlzhx.cn%2Findex.php%2Fapi%2FAlipay%2Fnotify_url&amp;sign_type=RSA2&amp;timestamp=2018-02-06+16%3A30%3A40&amp;version=1.0&amp;sign=fGXkpTVGTLN2JLxjowJ4tBxZpqcfT8eME1BJje2PYRr1HQL3X2%2BjlyT37zjC4CDmuC4iZRU8FsW1DYpRNBhbzLpAIfcjpfq4YQZoqgfAq4gq1Z%2FJnUfD0inRKwewbB1F3kBz32LdtCGCI1zrloLIyzD0xrT71rGamzZkW2lumFpV04DEcoaelNkNdNvdSvJ%2F1JKW9WBZxHrQ7Vkl4pOb95altfXArc%2BpvlGEYMKBfWFIOo4votdiG6PAzwZekh9vwWNK0WpnSZGFtcHT4PWisf%2Bu%2FzE%2FnwAQdpiUlkaQ3JK1bFcWKoX2vl3Za%2BnEuKrbA0fk1uonuXiaugxR6bHxvg%3D%3D
     */

    private String prepayid;
    private String appid;
    private String partnerid;
    @SerializedName("package")
    private String packageX;
    private String noncestr;
    private String timestamp;
    private String sign;
    private String order_number;

    @Override
    public String toString() {
        return "PayBean{" +
                "prepayid='" + prepayid + '\'' +
                ", appid='" + appid + '\'' +
                ", partnerid='" + partnerid + '\'' +
                ", packageX='" + packageX + '\'' +
                ", noncestr='" + noncestr + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", sign='" + sign + '\'' +
                ", order_number='" + order_number + '\'' +
                '}';
    }

    public String getPrepayid() {
        return prepayid;
    }

    public void setPrepayid(String prepayid) {
        this.prepayid = prepayid;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
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

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getOrder_number() {
        return order_number;
    }

    public void setOrder_number(String order_number) {
        this.order_number = order_number;
    }
}
