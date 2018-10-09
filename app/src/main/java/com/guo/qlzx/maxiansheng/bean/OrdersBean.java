package com.guo.qlzx.maxiansheng.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/4/16 0016.
 * 订单实体类
 */

public class OrdersBean implements Serializable{


    /**
     * order_id : 256
     * order_sn : sn987456321
     * phone : 13220166988
     * count : 8
     * end_amount : 150.00
     * pay_status : 1
     * order_status : 1
     * shipping_status : 1
     * is_comment : 1
     * order_type : 1
     * order_message : 卖家已发货
     * order : [{"goods_name":"草鱼","goods_num":"2","goods_price":"30.00","market_price":"20.00","price":"15.000","spec_key_name":"380g-500g","service_name":"切丁","original_img":"/public/home/images/1.jpg"},{"goods_name":"猪肉","goods_num":"4","goods_price":"30.00","market_price":"20.00","price":"15.00","spec_key_name":"380g-500g","service_name":"切丁","original_img":"/public/home/images/1.jpg"}]
     */

    private String order_id;
    private String order_sn;
    private String phone;
    private String count;
    private String end_amount;
    private String pay_status;
    private String order_status;
    private String shipping_status;
    private String is_comment;
    private int order_type;
    private String order_message;
    private List<OrderBean> order;
    private String staff_iphone;
    private String order_amount;

    public String getOrder_amount() {
        return order_amount;
    }

    public void setOrder_amount(String order_amount) {
        this.order_amount = order_amount;
    }

    public String getStaff_iphone() {
        return staff_iphone;
    }

    public void setStaff_iphone(String staff_iphone) {
        this.staff_iphone = staff_iphone;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getOrder_sn() {
        return order_sn;
    }

    public void setOrder_sn(String order_sn) {
        this.order_sn = order_sn;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getEnd_amount() {
        return end_amount;
    }

    public void setEnd_amount(String end_amount) {
        this.end_amount = end_amount;
    }

    public String getPay_status() {
        return pay_status;
    }

    public void setPay_status(String pay_status) {
        this.pay_status = pay_status;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getShipping_status() {
        return shipping_status;
    }

    public void setShipping_status(String shipping_status) {
        this.shipping_status = shipping_status;
    }

    public String getIs_comment() {
        return is_comment;
    }

    public void setIs_comment(String is_comment) {
        this.is_comment = is_comment;
    }

    public int getOrder_type() {
        return order_type;
    }

    public void setOrder_type(int order_type) {
        this.order_type = order_type;
    }

    public String getOrder_message() {
        return order_message;
    }

    public void setOrder_message(String order_message) {
        this.order_message = order_message;
    }

    public List<OrderBean> getOrder() {
        return order;
    }

    public void setOrder(List<OrderBean> order) {
        this.order = order;
    }

    public static class OrderBean implements Serializable{
        /**
         * goods_name : 草鱼
         * goods_num : 2
         * goods_price : 30.00
         * market_price : 20.00
         * price : 15.00
         * spec_key_name : 380g-500g
         * service_name : 切丁
         * original_img : /public/home/images/1.jpg
         * goods_id
         */

        private String goods_name;
        private String goods_num;
        private String goods_price;
        private String market_price;
        private String price;
        private String spec_key_name;
        private String service_name;
        private String original_img;
        private String goods_id;

        public String getGoods_name() {
            return goods_name;
        }

        public void setGoods_name(String goods_name) {
            this.goods_name = goods_name;
        }

        public String getGoods_num() {
            return goods_num;
        }

        public void setGoods_num(String goods_num) {
            this.goods_num = goods_num;
        }

        public String getGoods_price() {
            return goods_price;
        }

        public void setGoods_price(String goods_price) {
            this.goods_price = goods_price;
        }

        public String getMarket_price() {
            return market_price;
        }

        public void setMarket_price(String market_price) {
            this.market_price = market_price;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getSpec_key_name() {
            return spec_key_name;
        }

        public void setSpec_key_name(String spec_key_name) {
            this.spec_key_name = spec_key_name;
        }

        public String getService_name() {
            return service_name;
        }

        public void setService_name(String service_name) {
            this.service_name = service_name;
        }

        public String getOriginal_img() {
            return original_img;
        }

        public void setOriginal_img(String original_img) {
            this.original_img = original_img;
        }

        public String getGoods_id() {
            return goods_id;
        }

        public void setGoods_id(String goods_id) {
            this.goods_id = goods_id;
        }
    }
}
