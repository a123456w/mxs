package com.guo.qlzx.maxiansheng.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/4/20 0020.
 */

public class ProductDetailsBean {


    /**
     * goods_id : 1
     * goods_name : 大苹果1
     * goods_sn : M0000001
     * click_count : 1
     * brand_id : 2
     * store_count : 200
     * collect_sum : 0
     * comment_count : 0
     * weight : 300
     * market_price : 12.10
     * shop_price : 10.00
     * plus_price : 9.50
     * cost_price : 9.00
     * key_name :
     * unit : 盒
     * goods_remark : 苹果11
     * goods_content : <p>111111111222</p>
     * original_img : ["/public/upload/goods/2018/04-10/dad003b5d42a47b37c1e91e1998f3419.jpg","/public/upload/goods/2018/04-10/85deb3565d521ee70b6921caaa6984e5.jpg","/public/upload/goods/2018/04-10/9413f6e191717befc75fd64c492114f2.jpg","/public/upload/goods/2018/04-10/9edfaf4b95c4cd76b2686d6031680d31.png","/public/upload/goods/2018/04-10/a64ad04d3228844d002413ad436f0b75.jpg"]
     * video : public/upload/goods/video/20180410/2bf96f34dca9fcd5165f3cdf603a0f4d.mp4
     * video_img
     * is_free_shipping : 0
     * is_hot : 1
     * is_new : 0
     * give_integral : 1
     * sales_sum : 0
     * region_id : 1
     * storage : 冷藏
     * goods_info : {"brand":"jordan","storage":"冷藏","weight":"300g","region":"哈尔滨"}
     * price_description : false
     * delivery_time : false
     * day_start_time : 06:30:00
     * day_end_time : 22:00:00
     * type : 0
     * comment : [{"portrait":"public/images/d32ebcN490d1c3a.jpg","username":"public/images/d32ebcN490d1c3a.jpg","create_time":"public/images/d32ebcN490d1c3a.jpg","content":"public/images/d32ebcN490d1c3a.jpg","score":5,"img":["public/images/d32ebcN490d1c3a.jpg","public/images/d32ebcN490d1c3a.jpg","public/images/d32ebcN490d1c3a.jpg"]},{"portrait":"public/images/d32ebcN490d1c3a.jpg","username":"public/images/d32ebcN490d1c3a.jpg","create_time":"public/images/d32ebcN490d1c3a.jpg","content":"public/images/d32ebcN490d1c3a.jpg","score":4.5,"img":["public/images/d32ebcN490d1c3a.jpg","public/images/d32ebcN490d1c3a.jpg","public/images/d32ebcN490d1c3a.jpg"]}]
     * "share_url": "http://tpshop.com/index.php/Admin/Index/index",
     "is_share": 1
     */

    private int goods_id;
    private String goods_name;
    private String goods_sn;
    private int click_count;
    private int brand_id;
    private int store_count;
    private int collect_sum;
    private int comment_count;
    private int weight;
    private String market_price;
    private String shop_price;
    private String plus_price;
    private String cost_price;
    private String key_name;
    private String unit;
    private String goods_remark;
    private String goods_content;
    private String video;
    private int is_free_shipping;
    private int is_hot;
    private int is_new;
    private int give_integral;
    private int sales_sum;
    private int region_id;
    private String storage;
    private GoodsInfoBean goods_info;
    private boolean price_description;
    private boolean delivery_time;
    private String day_start_time;
    private String day_end_time;
    private int type;
    private String mobile;
    private String presel_time;
    private List<String> original_img;
    private List<CommentListBean> comment_list;
    private String share_url;
    private int is_share;
    private String video_img;

    private String presel_timeSmap;

    private String start_time;
    private String end_time;
    private String number;
    private int seckillnum;

    public int getGroupnum() {
        return groupnum;
    }

    public void setGroupnum(int groupnum) {
        this.groupnum = groupnum;
    }

    private int groupnum;

    public String getPresel_timeSmap() {
        return presel_timeSmap;
    }

    public void setPresel_timeSmap(String presel_timeSmap) {
        this.presel_timeSmap = presel_timeSmap;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPresel_time() {
        return presel_time;
    }

    public void setPresel_time(String presel_time) {
        this.presel_time = presel_time;
    }

    public int getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(int goods_id) {
        this.goods_id = goods_id;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public String getGoods_sn() {
        return goods_sn;
    }

    public void setGoods_sn(String goods_sn) {
        this.goods_sn = goods_sn;
    }

    public int getClick_count() {
        return click_count;
    }

    public void setClick_count(int click_count) {
        this.click_count = click_count;
    }

    public int getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(int brand_id) {
        this.brand_id = brand_id;
    }

    public int getStore_count() {
        return store_count;
    }

    public void setStore_count(int store_count) {
        this.store_count = store_count;
    }

    public int getCollect_sum() {
        return collect_sum;
    }

    public void setCollect_sum(int collect_sum) {
        this.collect_sum = collect_sum;
    }

    public int getComment_count() {
        return comment_count;
    }

    public void setComment_count(int comment_count) {
        this.comment_count = comment_count;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getMarket_price() {
        return market_price;
    }

    public void setMarket_price(String market_price) {
        this.market_price = market_price;
    }

    public String getShop_price() {
        return shop_price;
    }

    public void setShop_price(String shop_price) {
        this.shop_price = shop_price;
    }

    public String getPlus_price() {
        return plus_price;
    }

    public void setPlus_price(String plus_price) {
        this.plus_price = plus_price;
    }

    public String getCost_price() {
        return cost_price;
    }

    public void setCost_price(String cost_price) {
        this.cost_price = cost_price;
    }

    public String getKey_name() {
        return key_name;
    }

    public void setKey_name(String key_name) {
        this.key_name = key_name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getGoods_remark() {
        return goods_remark;
    }

    public void setGoods_remark(String goods_remark) {
        this.goods_remark = goods_remark;
    }

    public String getGoods_content() {
        return goods_content;
    }

    public void setGoods_content(String goods_content) {
        this.goods_content = goods_content;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public int getIs_free_shipping() {
        return is_free_shipping;
    }

    public void setIs_free_shipping(int is_free_shipping) {
        this.is_free_shipping = is_free_shipping;
    }

    public int getIs_hot() {
        return is_hot;
    }

    public void setIs_hot(int is_hot) {
        this.is_hot = is_hot;
    }

    public int getIs_new() {
        return is_new;
    }

    public void setIs_new(int is_new) {
        this.is_new = is_new;
    }

    public int getGive_integral() {
        return give_integral;
    }

    public void setGive_integral(int give_integral) {
        this.give_integral = give_integral;
    }

    public int getSales_sum() {
        return sales_sum;
    }

    public void setSales_sum(int sales_sum) {
        this.sales_sum = sales_sum;
    }

    public int getRegion_id() {
        return region_id;
    }

    public void setRegion_id(int region_id) {
        this.region_id = region_id;
    }

    public String getStorage() {
        return storage;
    }

    public void setStorage(String storage) {
        this.storage = storage;
    }

    public GoodsInfoBean getGoods_info() {
        return goods_info;
    }

    public void setGoods_info(GoodsInfoBean goods_info) {
        this.goods_info = goods_info;
    }

    public boolean isPrice_description() {
        return price_description;
    }

    public void setPrice_description(boolean price_description) {
        this.price_description = price_description;
    }

    public boolean isDelivery_time() {
        return delivery_time;
    }

    public void setDelivery_time(boolean delivery_time) {
        this.delivery_time = delivery_time;
    }

    public String getDay_start_time() {
        return day_start_time;
    }

    public void setDay_start_time(String day_start_time) {
        this.day_start_time = day_start_time;
    }

    public String getDay_end_time() {
        return day_end_time;
    }

    public void setDay_end_time(String day_end_time) {
        this.day_end_time = day_end_time;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<String> getOriginal_img() {
        return original_img;
    }

    public void setOriginal_img(List<String> original_img) {
        this.original_img = original_img;
    }

    public List<CommentListBean> getComment_list() {
        return comment_list;
    }

    public void setComment_list(List<CommentListBean> comment_list) {
        this.comment_list = comment_list;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getShare_url() {
        return share_url;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }

    public int getIs_share() {
        return is_share;
    }

    public void setIs_share(int is_share) {
        this.is_share = is_share;
    }

    public String getVideo_img() {
        return video_img;
    }

    public void setVideo_img(String video_img) {
        this.video_img = video_img;
    }

    public int getSeckillnum() {
        return seckillnum;
    }

    public void setSeckillnum(int seckillnum) {
        this.seckillnum = seckillnum;
    }

    public static class GoodsInfoBean {
        /**
         * brand : jordan
         * storage_type : 冷藏
         * weight : 300g
         * region : 哈尔滨
         */

        private String brand;
        private String storage_type;
        private String weight;
        private String region;

        public String getBrand() {
            return brand;
        }

        public void setBrand(String brand) {
            this.brand = brand;
        }

        public String getStorage_type() {
            return storage_type;
        }

        public void setStorage_type(String storage_type) {
            this.storage_type = storage_type;
        }

        public String getWeight() {
            return weight;
        }

        public void setWeight(String weight) {
            this.weight = weight;
        }

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }
    }

    public static class CommentListBean {
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
        private int describe_score;
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

        public int getDescribe_score() {
            return describe_score;
        }

        public void setDescribe_score(int describe_score) {
            this.describe_score = describe_score;
        }

        public List<String> getImg() {
            return img;
        }

        public void setImg(List<String> img) {
            this.img = img;
        }
    }
}
