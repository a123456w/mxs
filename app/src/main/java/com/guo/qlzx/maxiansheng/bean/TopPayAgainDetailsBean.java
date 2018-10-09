package com.guo.qlzx.maxiansheng.bean;

import java.util.List;

/**
 * Created by 李 on 2018/5/24.
 */

public class TopPayAgainDetailsBean  {

    /**
     * address : {"name":"阿龙","mobile":"13012345678","province":"北京市","city":"昌平区","address":"北京化工大学国家大学科技园"}
     * order : {"order_id":1202,"end_amount":110.3,"is_pay":"0","pay_code":"yue_pay","compen_price":300.5}
     * goods : [{"rec_id":2009,"goods_id":48,"goods_name":"苹果","goods_num":3,"end_price":"49.80","service_name":"","service_price":"0.00","spec_name":"3000g","change":2,"price":"49.80","member_goods_price":"16.60","goods_price":"16.60","spec_key_name":"3000g","original_img":"/public/upload/goods/2018/04-25/8b6eaff04d78fe21b7c0bc975d9306b1.jpg","change_price":0,"market_price":"19.60","children_id":2010,"compen_price":0},{"rec_id":2010,"goods_id":8,"goods_name":"猪头肉","goods_num":3,"end_price":"240.00","service_name":"","service_price":"0.00","spec_name":"200g","change":6,"price":297,"goods_price":"99.00","spec_key_name":"200g","original_img":"/public/upload/goods/2018/04-24/71fb15eb71b0c86d2c66474fcc9183ff.png","change_price":0,"market_price":0,"children_id":0,"compen_price":190.2}]
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
         * name : 阿龙
         * mobile : 13012345678
         * province : 北京市
         * city : 昌平区
         * address : 北京化工大学国家大学科技园
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
         * order_id : 1202
         * end_amount : 110.3
         * is_pay : 0
         * pay_code : yue_pay
         * compen_price : 300.5
         * totle_amount
         */

        private String order_id;
        private String end_amount;
        private String is_pay;
        private String pay_code;
        private String compen_price;
        private String order_sn;
        private String totle_amount;
        private String change_price;

        public String getChange_price() {
            return change_price;
        }

        public void setChange_price(String change_price) {
            this.change_price = change_price;
        }
        public String getTotle_amount() {
            return totle_amount;
        }

        public void setTotle_amount(String totle_amount) {
            this.totle_amount = totle_amount;
        }

        public String getOrder_sn() {
            return order_sn;
        }

        public void setOrder_sn(String order_sn) {
            this.order_sn = order_sn;
        }

        public String getOrder_id() {
            return order_id;
        }

        public void setOrder_id(String order_id) {
            this.order_id = order_id;
        }

        public String getEnd_amount() {
            return end_amount;
        }

        public void setEnd_amount(String end_amount) {
            this.end_amount = end_amount;
        }

        public String getIs_pay() {
            return is_pay;
        }

        public void setIs_pay(String is_pay) {
            this.is_pay = is_pay;
        }

        public String getPay_code() {
            return pay_code;
        }

        public void setPay_code(String pay_code) {
            this.pay_code = pay_code;
        }

        public String getCompen_price() {
            return compen_price;
        }

        public void setCompen_price(String compen_price) {
            this.compen_price = compen_price;
        }
    }

    public static class GoodsBean {
        /**
         * rec_id : 2009
         * goods_id : 48
         * goods_name : 苹果
         * goods_num : 3
         * end_price : 49.80
         * service_name :
         * service_price : 0.00
         * spec_name : 3000g
         * change : 2
         * price : 49.80
         * member_goods_price : 16.60
         * goods_price : 16.60
         * spec_key_name : 3000g
         * original_img : /public/upload/goods/2018/04-25/8b6eaff04d78fe21b7c0bc975d9306b1.jpg
         * change_price : 0
         * market_price : 19.60
         * children_id : 2010
         * compen_price : 0
         */

        private String rec_id;
        private String goods_id;
        private String goods_name;
        private String goods_num;
        private String end_price;
        private String service_name;
        private String service_price;
        private String spec_name;
        private String change;
        private String price;
        private String member_goods_price;
        private String goods_price;
        private String spec_key_name;
        private String original_img;
        private String change_price;
        private String market_price;
        private String children_id;
        private String compen_price;

        public String getRec_id() {
            return rec_id;
        }

        public void setRec_id(String rec_id) {
            this.rec_id = rec_id;
        }

        public String getGoods_id() {
            return goods_id;
        }

        public void setGoods_id(String goods_id) {
            this.goods_id = goods_id;
        }

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

        public String getEnd_price() {
            return end_price;
        }

        public void setEnd_price(String end_price) {
            this.end_price = end_price;
        }

        public String getService_name() {
            return service_name;
        }

        public void setService_name(String service_name) {
            this.service_name = service_name;
        }

        public String getService_price() {
            return service_price;
        }

        public void setService_price(String service_price) {
            this.service_price = service_price;
        }

        public String getSpec_name() {
            return spec_name;
        }

        public void setSpec_name(String spec_name) {
            this.spec_name = spec_name;
        }

        public String getChange() {
            return change;
        }

        public void setChange(String change) {
            this.change = change;
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

        public String getChildren_id() {
            return children_id;
        }

        public void setChildren_id(String children_id) {
            this.children_id = children_id;
        }

        public String getCompen_price() {
            return compen_price;
        }

        public void setCompen_price(String compen_price) {
            this.compen_price = compen_price;
        }
    }
}
