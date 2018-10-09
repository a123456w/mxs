package com.guo.qlzx.maxiansheng.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/4/17 0017.
 */

public class OrderDetailBean {

    /**
     * address : {"consignee":"张三","mobile":"132201339874","province":"河北省","city":"石家庄","district":"长廊","address":"长廊大街122号"}
     * order : {"order_id":"123","total_amount":"150.00","order_amount":"150.00","change_money":"5.00","shipping_price":"10.00","rapeseed":"50","coupon_price":"10.00","order_sn":"sn123456789","add_time":"2017-08-03","pay_time":"2017-09-05","shipping_time":"2017-09-06","order_type":1,"integral":"100"}
     * goods : [{"goods_name":"密云曼鸡蛋","goods_num":"2","price":"10.00","member_goods_price":"5.00","goods_price":"15.00","spec_key_name":"30g/盒","service_name":"","original_img":"/public/home/images/2.jpg"},{"goods_name":"黑猪肉","goods_num":"3","price":"10.00","member_goods_price":"5.00","goods_price":"150.00","spec_key_name":"100g-300g","service_name":"切片","original_img":"/public/home/images/2.jpg"}]
     */

    private AddressBean address;
    private OrderBean order;
    private List<GoodsBean> goods;

    public AddressBean getAddress() {
        return address;
    }

    public void setAddress(AddressBean address) {
        this.address = address;
    }

    public OrderBean getOrder() {
        return order;
    }

    public void setOrder(OrderBean order) {
        this.order = order;
    }

    public List<GoodsBean> getGoods() {
        return goods;
    }

    public void setGoods(List<GoodsBean> goods) {
        this.goods = goods;
    }

    public static class AddressBean {
        /**
         * name : 张三
         * mobile : 132201339874
         * province : 河北省
         * city : 石家庄
         * address : 长廊大街122号
         */

        private String name;
        private String mobile;
        private String province;
        private String city;
        private String address;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }
    }

    public static class OrderBean {
        /**
         * order_id : 123
         * total_amount : 150.00
         * order_amount : 150.00
         * change_money : 5.00
         * shipping_price : 10.00
         * rapeseed : 50
         * coupon_price : 10.00
         * order_sn : sn123456789
         * add_time : 2017-08-03
         * pay_time : 2017-09-05
         * shipping_time : 2017-09-06
         * order_type : 1
         * integral : 100
         *pay_code
         * order_message :
         *
         *   "begin_time":"1524023140",
         *    "end_time":'1524023140'
         */

        private String order_id;
        private String total_amount;
        private String order_amount;
        private String change_money;
        private String shipping_price;
        private String rapeseed;
        private String coupon_price;
        private String order_sn;
        private String add_time;
        private String pay_time;
        private String shipping_time;
        private int order_type;
        private String integral;
        private String order_message;
        private String pay_code;
        private String begin_time;
        private String end_time;

        private String cargonumber;
        private String com_price;
        private String end_amount;
        private String compen_money;

        public String getCompen_money() {
            return compen_money;
        }

        public void setCompen_money(String compen_money) {
            this.compen_money = compen_money;
        }

        public String getEnd_amount() {
            return end_amount;
        }

        public void setEnd_amount(String end_amount) {
            this.end_amount = end_amount;
        }

        public String getCom_price() {
            return com_price;
        }

        public void setCom_price(String com_price) {
            this.com_price = com_price;
        }

        public String getCargonumber() {
            return cargonumber;
        }

        public void setCargonumber(String cargonumber) {
            this.cargonumber = cargonumber;
        }

        public String getBegin_time() {
            return begin_time;
        }

        public void setBegin_time(String begin_time) {
            this.begin_time = begin_time;
        }

        public String getEnd_time() {
            return end_time;
        }

        public void setEnd_time(String end_time) {
            this.end_time = end_time;
        }

        public String getPay_code() {
            return pay_code;
        }

        public void setPay_code(String pay_code) {
            this.pay_code = pay_code;
        }

        public String getOrder_id() {
            return order_id;
        }

        public void setOrder_id(String order_id) {
            this.order_id = order_id;
        }

        public String getTotal_amount() {
            return total_amount;
        }

        public void setTotal_amount(String total_amount) {
            this.total_amount = total_amount;
        }

        public String getOrder_amount() {
            return order_amount;
        }

        public void setOrder_amount(String order_amount) {
            this.order_amount = order_amount;
        }

        public String getChange_money() {
            return change_money;
        }

        public void setChange_money(String change_money) {
            this.change_money = change_money;
        }

        public String getShipping_price() {
            return shipping_price;
        }

        public void setShipping_price(String shipping_price) {
            this.shipping_price = shipping_price;
        }

        public String getRapeseed() {
            return rapeseed;
        }

        public void setRapeseed(String rapeseed) {
            this.rapeseed = rapeseed;
        }

        public String getCoupon_price() {
            return coupon_price;
        }

        public void setCoupon_price(String coupon_price) {
            this.coupon_price = coupon_price;
        }

        public String getOrder_sn() {
            return order_sn;
        }

        public void setOrder_sn(String order_sn) {
            this.order_sn = order_sn;
        }

        public String getAdd_time() {
            return add_time;
        }

        public void setAdd_time(String add_time) {
            this.add_time = add_time;
        }

        public String getPay_time() {
            return pay_time;
        }

        public void setPay_time(String pay_time) {
            this.pay_time = pay_time;
        }

        public String getShipping_time() {
            return shipping_time;
        }

        public void setShipping_time(String shipping_time) {
            this.shipping_time = shipping_time;
        }

        public int getOrder_type() {
            return order_type;
        }

        public void setOrder_type(int order_type) {
            this.order_type = order_type;
        }

        public String getIntegral() {
            return integral;
        }

        public void setIntegral(String integral) {
            this.integral = integral;
        }

        public String getOrder_message() {
            return order_message;
        }

        public void setOrder_message(String order_message) {
            this.order_message = order_message;
        }
    }

    public static class GoodsBean {
        /**
         * goods_name : 密云曼鸡蛋
         * goods_num : 2
         * price : 10.00
         * member_goods_price : 5.00
         * goods_price : 15.00
         * spec_key_name : 30g/盒
         * service_name :
         * service_price :
         * original_img : /public/home/images/2.jpg
         * change_price: 0.05
         * market_price: 20.0
         * change:1
         * price :40
         */

        private String goods_name;
        private String goods_num;
        private String price;
        private String member_goods_price;
        private String goods_price;
        private String spec_key_name;
        private String service_name;
        private String service_price;
        private String original_img;
        private String change_price;
        private String market_price;
        private int change;

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

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getMember_goods_price() {
            return member_goods_price;
        }

        public void setMember_goods_price(String member_goods_price) {
            this.member_goods_price = member_goods_price;
        }

        public String getGoods_price() {
            return goods_price;
        }

        public void setGoods_price(String goods_price) {
            this.goods_price = goods_price;
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

        public String getChange_price() {
            return change_price;
        }

        public void setChange_price(String change_price) {
            this.change_price = change_price;
        }

        public String getMarket_price() {
            return market_price;
        }

        public void setMarket_price(String market_price) {
            this.market_price = market_price;
        }

        public int getChange() {
            return change;
        }

        public void setChange(int change) {
            this.change = change;
        }

        public String getService_price() {
            return service_price;
        }

        public void setService_price(String service_price) {
            this.service_price = service_price;
        }
    }
}
