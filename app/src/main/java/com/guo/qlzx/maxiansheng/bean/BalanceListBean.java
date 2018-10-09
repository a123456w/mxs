package com.guo.qlzx.maxiansheng.bean;

/**
 * Created by 李 on 2018/4/18.
 * 余额明细 /菜籽明细
 */

public class BalanceListBean {
    /**
     * status : 1
     * surplus_price : 60.00
     * price : 20.00
     * create_time : 1
     */

    public BalanceListBean(String status,String price,String create_time,String number){
        setCreate_time(create_time);
        setPrice(price);
        setStatus(status);
        setNumber(number);
    }

    @Override
    public String toString() {
        return "BalanceListBean{" +
                "status='" + status + '\'' +
                ", surplus_price='" + surplus_price + '\'' +
                ", price='" + price + '\'' +
                ", create_time='" + create_time + '\'' +
                ", type_name='" + type_name + '\'' +
                ", count='" + count + '\'' +
                ", number='" + number + '\'' +
                '}';
    }

    private String status;
    private String surplus_price;
    private String price;
    private String create_time;
    private String type_name;

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSurplus_price() {
        return surplus_price;
    }

    public void setSurplus_price(String surplus_price) {
        this.surplus_price = surplus_price;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    /**
     * status : 1
     * count : 60.00
     * number : 20.00
     * create_time : 1
     */

    private String count;
    private String number;


    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

}
