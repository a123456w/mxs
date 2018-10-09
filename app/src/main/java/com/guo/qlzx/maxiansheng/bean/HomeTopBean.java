package com.guo.qlzx.maxiansheng.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by 李 on 2018/4/10.
 */

public class HomeTopBean {

    /**
     * banner : ["/public/upload/ad/2018/04-26/e9acf68139ef35ce97676d0bf3698ae5.jpg","/public/upload/ad/2018/04-26/bd4da449d20d9ba294d23e634229e83c.jpg","/public/upload/ad/2018/04-26/69342625d39860c36ad29026434c62b5.jpg"]
     * navigation : {"classify":[{"id":3,"name":"家居用品","img":"/public/upload/category/2018/04-24/81d5c68675b22e484e8049207612e605.png"},{"id":13,"name":"水果专区","img":"/public/upload/category/2018/04-25/2aef3d0c7fc93034fd011595058aac9c.png"},{"id":14,"name":"清真专区","img":"/public/upload/category/2018/04-25/de2790f9bb6a3a2fe82a5a535c4138f9.png"},{"id":15,"name":"蔬菜专区","img":"/public/upload/category/2018/04-25/c496209d9090ef188b954b925839c483.png"},{"id":23,"name":"主食厨房","img":"/public/upload/category/2018/04-25/2ff7f890a56f73d127d164966bd6eda5.png"}],"function":[]}
     * news : [{"id":1,"title":"马先生的强势之处哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈","img":"/public/upload/ad/2018/04-28/adefac6aac168f4132f43e3a9e245ede.jpg"},{"id":3,"title":"齐力众信--马鲜生项目组","img":"/public/upload/ad/2018/04-28/09b77b60b9457b4e9e1256ea85a5b6c8.jpg"},{"id":2,"title":"饿了吗","img":"/public/upload/ad/2018/04-28/27f3c60a9594b34330c524b8c879ef2d.jpg"}]
     * coupon : {"ad_code":"/public/upload/ad/2018/05-08/bec751330dc3450503c4808ac62e7d01.jpg","type":0}
     * new_coupon : [{"id":28,"name":"新人优惠15元","money":"15.00","condition":"50.00"},{"id":29,"name":"新人优惠卷6元","money":"6.00","condition":"7.00"}]
     */

    private NavigationBean navigation;
    private CouponBean coupon;
    private List<BannerBean> banner;
    private List<NewsBean> news;
    private List<NewCouponBean> new_coupon;


    public NavigationBean getNavigation() {
        return navigation;
    }

    public void setNavigation(NavigationBean navigation) {
        this.navigation = navigation;
    }

    public CouponBean getCoupon() {
        return coupon;
    }

    public void setCoupon(CouponBean coupon) {
        this.coupon = coupon;
    }

    public List<BannerBean> getBanner() {
        return banner;
    }

    public void setBanner(List<BannerBean> banner) {
        this.banner = banner;
    }

    public List<NewsBean> getNews() {
        return news;
    }

    public void setNews(List<NewsBean> news) {
        this.news = news;
    }

    public List<NewCouponBean> getNew_coupon() {
        return new_coupon;
    }

    public void setNew_coupon(List<NewCouponBean> new_coupon) {
        this.new_coupon = new_coupon;
    }


    public static class NavigationBean {
        private List<ClassifyBean> classify;
        private List<?> function;

        public List<ClassifyBean> getClassify() {
            return classify;
        }

        public void setClassify(List<ClassifyBean> classify) {
            this.classify = classify;
        }

        public List<?> getFunction() {
            return function;
        }

        public void setFunction(List<?> function) {
            this.function = function;
        }

        public static class ClassifyBean {
            /**
             * id : 3
             * name : 家居用品
             * img : /public/upload/category/2018/04-24/81d5c68675b22e484e8049207612e605.png
             */

            public ClassifyBean(int id,String name,String img){
                setId(id);
                setImg(img);
                setName(name);
            }
            private int id;
            private String name;
            private String img;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
            }
        }
    }

    public static class CouponBean {
        /**
         * ad_code : /public/upload/ad/2018/05-08/bec751330dc3450503c4808ac62e7d01.jpg
         * type : 0
         */

        private String ad_code;
        private int type;

        public String getAd_code() {
            return ad_code;
        }

        public void setAd_code(String ad_code) {
            this.ad_code = ad_code;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }

    public static class NewsBean {
        /**
         * id : 1
         * title : 马先生的强势之处哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈
         * img : /public/upload/ad/2018/04-28/adefac6aac168f4132f43e3a9e245ede.jpg
         */

        private int id;
        private String title;
        private String img;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
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
    }

    public static class NewCouponBean {
        /**
         * id : 28
         * name : 新人优惠15元
         * money : 15.00
         * condition : 50.00
         */

        private int id;
        private String name;
        private String money;
        private String condition;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getCondition() {
            return condition;
        }

        public void setCondition(String condition) {
            this.condition = condition;
        }
    }

    public static class BannerBean {
        /**
         * ad_code : /public/upload/ad/2018/07-30/c303a7c0895119033c1b9638e3df8156.jpg
         * ad_link : http://www.baidu.com
         */

        private String ad_code;
        private String ad_link;

        public String getAd_code() {
            return ad_code;
        }

        public void setAd_code(String ad_code) {
            this.ad_code = ad_code;
        }

        public String getAd_link() {
            return ad_link;
        }

        public void setAd_link(String ad_link) {
            this.ad_link = ad_link;
        }
    }
}
