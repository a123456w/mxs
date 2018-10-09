package com.guo.qlzx.maxiansheng.http;

import com.guo.qlzx.maxiansheng.bean.AboutBean;
import com.guo.qlzx.maxiansheng.bean.ActivityBean;
import com.guo.qlzx.maxiansheng.bean.AddressInfoBean;
import com.guo.qlzx.maxiansheng.bean.AddressManagementListBean;
import com.guo.qlzx.maxiansheng.bean.BalanceListBean;
import com.guo.qlzx.maxiansheng.bean.CartListBean;
import com.guo.qlzx.maxiansheng.bean.CheckVersionBean;
import com.guo.qlzx.maxiansheng.bean.ClassficationGoodsBean;
import com.guo.qlzx.maxiansheng.bean.ClassificationBean;
import com.guo.qlzx.maxiansheng.bean.ComplainPhoneBean;
import com.guo.qlzx.maxiansheng.bean.CouponListBean;
import com.guo.qlzx.maxiansheng.bean.FaqDetailsBean;
import com.guo.qlzx.maxiansheng.bean.DistriTimeBean;
import com.guo.qlzx.maxiansheng.bean.FaqListBean;
import com.guo.qlzx.maxiansheng.bean.EvaluateListBean;
import com.guo.qlzx.maxiansheng.bean.FootMarkListBean;
import com.guo.qlzx.maxiansheng.bean.ForwardBean;
import com.guo.qlzx.maxiansheng.bean.GuessLikeBean;
import com.guo.qlzx.maxiansheng.bean.HeadlineNewsBean;
import com.guo.qlzx.maxiansheng.bean.HomeActivityBean;
import com.guo.qlzx.maxiansheng.bean.HomeClassifyBean;
import com.guo.qlzx.maxiansheng.bean.HomeCouponListBeans;
import com.guo.qlzx.maxiansheng.bean.HomeRecommendListBean;
import com.guo.qlzx.maxiansheng.bean.HomeTopBean;
import com.guo.qlzx.maxiansheng.bean.IndexBean;
import com.guo.qlzx.maxiansheng.bean.InviteBean;
import com.guo.qlzx.maxiansheng.bean.IsAlipayBean;
import com.guo.qlzx.maxiansheng.bean.IsPasswordExitsBean;
import com.guo.qlzx.maxiansheng.bean.IsRegisterBean;
import com.guo.qlzx.maxiansheng.bean.LogisticsBean;
import com.guo.qlzx.maxiansheng.bean.MemberCenterBean;
import com.guo.qlzx.maxiansheng.bean.MessageBean;
import com.guo.qlzx.maxiansheng.bean.MessageCenterBean;
import com.guo.qlzx.maxiansheng.bean.MessageCenterListBean;
import com.guo.qlzx.maxiansheng.bean.NewsDetailsBean;
import com.guo.qlzx.maxiansheng.bean.OrderCouponBean;
import com.guo.qlzx.maxiansheng.bean.OrderCouponListBean;
import com.guo.qlzx.maxiansheng.bean.OrderDetailBean;
import com.guo.qlzx.maxiansheng.bean.OrderNumberBean;
import com.guo.qlzx.maxiansheng.bean.OrdersBean;
import com.guo.qlzx.maxiansheng.bean.PayBean;
import com.guo.qlzx.maxiansheng.bean.PhoneCodeBean;
import com.guo.qlzx.maxiansheng.bean.ProductDetailsBean;
import com.guo.qlzx.maxiansheng.bean.RefundBean;
import com.guo.qlzx.maxiansheng.bean.ServerBean;
import com.guo.qlzx.maxiansheng.bean.SetFeedbackTypeListBean;
import com.guo.qlzx.maxiansheng.bean.SetPersonalBean;
import com.guo.qlzx.maxiansheng.bean.SetSafetyBean;
import com.guo.qlzx.maxiansheng.bean.IdentifyBean;
import com.guo.qlzx.maxiansheng.bean.LoginBean;
import com.guo.qlzx.maxiansheng.bean.ShowSearchBean;
import com.guo.qlzx.maxiansheng.bean.SubmitOrderBean;
import com.guo.qlzx.maxiansheng.bean.TopPayAgainDetailsBean;
import com.guo.qlzx.maxiansheng.bean.TotalSumBean;
import com.guo.qlzx.maxiansheng.bean.TwoCategoryBean;
import com.guo.qlzx.maxiansheng.bean.UserInfoBean;
import com.guo.qlzx.maxiansheng.bean.WeixinPayBean;
import com.qlzx.mylibrary.bean.BaseBean;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

public interface RemoteApi {
    /**
     * 支付  微信/支付宝
     *
     * @param token
     * @param paytype
     * @param money
     * @param sid
     * @return
     */
    @POST("Pay/pay_order")
    @FormUrlEncoded
    Observable<BaseBean<PayBean>> payOrder(@Field("token") String token,
                                           @Field("paytype") String paytype,
                                           @Field("money") String money,
                                           @Field("sid") String sid);

    /**
     * 首页顶部信息
     *
     * @param token
     * @return
     */
    @POST("home/top")
    @FormUrlEncoded
    Observable<BaseBean<HomeTopBean>> getHomeTopData(@Field("token") String token);

    /**
     * 首页活动专区
     *
     * @return
     */
    @POST("home/activity")
    Observable<BaseBean<List<HomeActivityBean>>> getHomeActivityData();

    /**
     * 首页分类专区
     *
     * @return
     */
    @POST("home/classify")
    Observable<BaseBean<List<HomeClassifyBean>>> getHomeClassifyData();

    /**
     * 首页 推荐商品列表
     *
     * @param page
     * @return
     */
    @POST("home/recommend")
    @FormUrlEncoded
    Observable<BaseBean<List<HomeRecommendListBean>>> getHomeRecommendData(@Field("page") int page,
                                                                           @Field("type") int type);

    /**
     * 首页 获取弹出优惠券数据
     *
     * @param type
     * @return
     */
    @POST("promotion/coupon")
    @FormUrlEncoded
    Observable<BaseBean<List<HomeCouponListBeans>>> getHomeCouponData(@Field("type") int type,
                                                                      @Field("token") String token);

    /**
     * 头条新闻列表页
     *
     * @return
     */
    @POST("news/index")
    @FormUrlEncoded
    Observable<BaseBean<List<HeadlineNewsBean>>> getHeadlineData(@Field("page") int page);

    /**
     * 热门搜索
     *
     * @return
     */
    @POST("search/hot_words")
    Observable<BaseBean<List<String>>> getSearchHotWords();

    /**
     * 用户信息
     *
     * @param token
     * @return
     */
    @POST("user/get_userinfo")
    @FormUrlEncoded
    Observable<BaseBean<SetPersonalBean>> getUserInfo(@Field("token") String token);

    /**
     * 上传反馈信息
     *
     * @param body
     * @return
     */
    @POST("versions/opinion")
    Observable<BaseBean> sendFeedback(@Body RequestBody body);

    /**
     * 帐号安全 获取本手机号码
     *
     * @param token
     * @return
     */
    @POST("account/user_safe")
    @FormUrlEncoded
    Observable<BaseBean<SetSafetyBean>> getUserPhone(@Field("token") String token);

    /**
     * 获取验证码
     *
     * @param mobile
     * @return
     */
    @POST("user/send_code")
    @FormUrlEncoded
    Observable<BaseBean<PhoneCodeBean>> getPhoneCode(@Field("mobile") String mobile,@Field("type") String type);

    /**
     * 手机号验证
     *
     * @param token
     * @param code
     * @return
     */
    @POST("user/phone_check")
    @FormUrlEncoded
    Observable<BaseBean> verifyPhone(@Field("token") String token, @Field("mobile_code") String code, @Field("mobile") String mobile);


    /**
     * 绑定新手机接口
     *
     * @param token
     * @param new_mobile
     * @param mobile_code
     * @return
     */
    @POST("user/new_mobile")
    @FormUrlEncoded
    Observable<BaseBean> setNewPhone(@Field("token") String token, @Field("new_mobile") String new_mobile, @Field("mobile_code") String mobile_code);


    /**
     * 登陆
     *
     * @param mobile
     * @param password
     * @return
     */
    @POST("user/user_login")
    @FormUrlEncoded
    Observable<BaseBean<LoginBean>> login(@Field("mobile") String mobile,
                                          @Field("password") String password,
                                          @Field("ji_token") String ji_token);


    /**
     * 获取验证码
     *
     * @param mobile
     * @return
     */
    @POST("user/send_code")
    @FormUrlEncoded
    Observable<BaseBean<Object>> ident(@Field("mobile") String mobile,
                                        @Field("type") String type);

    /**
     * 注册
     *
     * @param mobile
     * @param mobile_code
     * @param password
     * @return
     */
    @POST("user/register")
    @FormUrlEncoded
    Observable<BaseBean<Object>> register(@Field("mobile") String mobile,
                                          @Field("mobile_code") String mobile_code,
                                          @Field("password") String password);

    /**
     * 设置支付密码
     *
     * @param paypwd
     * @param mobile
     * @param token
     * @param mobile_code
     * @return
     */
    @POST("account/set_paypwd")
    @FormUrlEncoded
    Observable<BaseBean> setPassword(@Field("paypwd") String paypwd,
                                     @Field("mobile") String mobile,
                                     @Field("mobile_code") String mobile_code,
                                     @Field("token") String token);

    /**
     * 关于我们
     *
     * @return
     */
    @POST("versions/about_us")
    Observable<BaseBean<AboutBean>> getAboutUs();

    /**
     * 地址列表
     *
     * @param token
     * @return
     */
    @POST("address/index")
    @FormUrlEncoded
    Observable<BaseBean<List<AddressManagementListBean>>> getAddressList(@Field("token") String token);

    /**
     * 删除地址
     *
     * @param token
     * @return
     */
    @POST("address/delete")
    @FormUrlEncoded
    Observable<BaseBean<String>> deleteAddressList(@Field("token") String token,
                                                   @Field("address_id") int id);

    /**
     * 设置默认地址
     *
     * @param token
     * @param id
     * @return
     */
    @POST("address/defa")
    @FormUrlEncoded
    Observable<BaseBean<Object>> setDefault(@Field("token") String token,
                                            @Field("address_id") int id);

    /**
     * 添加新的地址
     *
     * @param token
     * @param consignee
     * @param mobile
     * @param city
     * @param district
     * @param address
     * @return
     */
    @POST("address/add")
    @FormUrlEncoded
    Observable<BaseBean<Object>> addAddress(@Field("token") String token,
                                            @Field("consignee") String consignee,
                                            @Field("mobile") String mobile,
                                            @Field("city") String city,
                                            @Field("district") String district,
                                            @Field("address") String address,
                                            @Field("longitude") String longitude,
                                            @Field("latitude") String latitude,
                                            @Field("door_num") String door_num);

    /**
     * 修改地址
     *
     * @param token
     * @param consignee
     * @param mobile
     * @param city
     * @param district
     * @param address
     * @param address_id
     * @return
     */
    @POST("address/edit")
    @FormUrlEncoded
    Observable<BaseBean<Object>> editAddress(@Field("token") String token,
                                             @Field("consignee") String consignee,
                                             @Field("mobile") String mobile,
                                             @Field("city") String city,
                                             @Field("district") String district,
                                             @Field("address") String address,
                                             @Field("address_id") int address_id,
                                             @Field("longitude") String longitude,
                                             @Field("latitude") String latitude,
                                             @Field("door_num") String door_num);

    /**
     * 地址管理-获取单个地址全部数据
     *
     * @param token
     * @param id
     * @return
     */
    @POST("address/get_user_msg")
    @FormUrlEncoded
    Observable<BaseBean<AddressInfoBean>> getAddressInfo(@Field("token") String token,
                                                         @Field("address_id") int id);

    /**
     * 1底部导航分类页 2分类列表页一级分类
     *
     * @param type
     * @return
     */
    @POST("classify/top_category")
    @FormUrlEncoded
    Observable<BaseBean<ClassificationBean>> top_category(@Field("type") String type);


    /**
     * 二级分类接口
     *
     * @param two_category
     * @return
     */
    @POST("classify/two_category")
    @FormUrlEncoded
    Observable<BaseBean<List<TwoCategoryBean>>> two_category(@Field("category_id") String two_category);


    /**
     * 获取活动列表数据
     *
     * @param id
     * @param page
     * @return
     */
    @POST("activity/activity_list")
    @FormUrlEncoded
    Observable<BaseBean<ActivityBean>> getActivityData(@Field("type") String id,
                                                       @Field("page") int page);

    /**
     * 会员中心
     *
     * @param token
     * @return
     */
    @POST("user/index")
    @FormUrlEncoded
    Observable<BaseBean<MemberCenterBean>> getMemberCenter(@Field("token") String token);


    /**
     * 获取订单列表
     *
     * @param token
     * @param type
     * @param page
     * @return
     */
    @POST("order/myorder")
    @FormUrlEncoded
    Observable<BaseBean<List<OrdersBean>>> getMyOrder(@Field("token") String token,
                                                      @Field("type") String type,
                                                      @Field("page") int page);

    /**
     * 手机号登录
     *
     * @param mobile
     * @param mobile_code
     * @return
     */
    @POST("user/mobile_login")
    @FormUrlEncoded
    Observable<BaseBean<LoginBean>> mobileLogin(@Field("mobile") String mobile,
                                                @Field("mobile_code") String mobile_code,
                                                @Field("ji_token") String ji_token);

    /**
     * 邀请好友
     *
     * @param token
     * @return
     */
    @POST("home/invitation")
    @FormUrlEncoded
    Observable<BaseBean<InviteBean>> getInviteInfo(@Field("token") String token);

    /**
     * 优惠券
     *
     * @param type  0 未使用 1 已使用
     * @param token
     * @return
     */
    @POST("user/user_coupon")
    @FormUrlEncoded
    Observable<BaseBean<List<CouponListBean>>> getUserCoupon(@Field("type") int type,
                                                             @Field("token") String token);

    /**
     * 取消订单
     *
     * @param token
     * @param order_id
     * @return
     */
    @POST("order/cancel_order")
    @FormUrlEncoded
    Observable<BaseBean<Object>> cancelOrder(@Field("token") String token,
                                             @Field("order_id") String order_id);

    /**
     * 删除订单
     *
     * @param token
     * @param order_id
     * @return
     */
    @POST("order/delorder")
    @FormUrlEncoded
    Observable<BaseBean<Object>> delOrder(@Field("token") String token,
                                          @Field("order_id") String order_id);

    /**
     * 我的足迹
     *
     * @param token
     * @return
     */
    @POST("user/footstep")
    @FormUrlEncoded
    Observable<BaseBean<List<FootMarkListBean>>> getMyFootMark(@Field("token") String token);

    /**
     * 删除足迹
     *
     * @param token
     * @param id
     * @return
     */
    @POST("user/foot_del")
    @FormUrlEncoded
    Observable<BaseBean> deleteMyFootMark(@Field("token") String token, @Field("id") String id);

    /**
     * 订单详情
     *
     * @param token
     * @param id
     * @return
     */
    @POST("order/detail")
    @FormUrlEncoded
    Observable<BaseBean<OrderDetailBean>> getOrderDetail(@Field("token") String token,
                                                         @Field("order_id") String id);

    /**
     * 猜你喜欢
     *
     * @param page
     * @return
     */
    @POST("home/recommend")
    @FormUrlEncoded
    Observable<BaseBean<List<GuessLikeBean>>> getGuessLikeData(@Field("page") int page,
                                                               @Field("type") String type);

    /**
     * 推荐商品
     *
     * @param page
     * @return
     */
    @POST("home/recommend")
    @FormUrlEncoded
    Observable<BaseBean<List<GuessLikeBean>>> recommend(@Field("page") int page,
                                                        @Field("type") String type,
                                                        @Field("goods_id") String goods_id);

    /**
     * 搜索----结果页
     *
     * @param title
     * @param category_id
     * @param sort
     * @param min_price
     * @param max_price
     * @param brand_id
     * @param region_id
     * @param page
     * @return
     */
    @POST("search/show_search")
    @FormUrlEncoded
    Observable<BaseBean<ShowSearchBean>> showSearch(@Field("title") String title,
                                                    @Field("category_id") String category_id,
                                                    @Field("sort") String sort,
                                                    @Field("min_price") String min_price,
                                                    @Field("max_price") String max_price,
                                                    @Field("brand_id") String brand_id,
                                                    @Field("region_id") String region_id,
                                                    @Field("page") int page);

    /**
     * 购物车----列表
     *
     * @param token
     * @return
     */
    @POST("cart/cart_list")
    @FormUrlEncoded
    Observable<BaseBean<CartListBean>> cart_list(@Field("token") String token);

    /**
     * 立即购买时请求的接口----用来检测是否已购买过该秒杀商品
     *
     * @param token
     * @return
     */
    @POST("order/diatelyBuy")
    @FormUrlEncoded
    Observable<BaseBean<CartListBean>> isExistenceOrders(@Field("token") String token,
                                                @Field("goods_id") String goods_id,
                                                @Field("service_id") String service_id,
                                                @Field("spec_key") String spec_key,
                                                @Field("goods_num") String goods_num
                                                );

    /**
     * 我的余额
     *
     * @param token
     * @param page
     * @return
     */
    @POST("account/change_money")
    @FormUrlEncoded
    Observable<BaseBean<List<BalanceListBean>>> getBalanceData(@Field("token") String token,
                                                               @Field("page") String page);

    /**
     * 我的菜籽
     *
     * @param token
     * @param page
     * @return
     */
    @POST("account/change_rapeseed")
    @FormUrlEncoded
    Observable<BaseBean<List<BalanceListBean>>> getRapeseedData(@Field("token") String token,
                                                                @Field("page") String page);

    /**
     * 账户总额
     *
     * @param token
     * @return
     */
    @POST("account/all_money")
    @FormUrlEncoded
    Observable<BaseBean<TotalSumBean>> getBalance(@Field("token") String token);

    /**
     * 提现记录
     *
     * @param user_id
     * @return
     */
    @POST("account/forward_status")
    @FormUrlEncoded
    Observable<BaseBean<List<ForwardBean>>> forward_status(@Field("token") String token
            , @Field("page") int page);

    /**
     * 退出登录
     *
     * @param token
     * @return
     */
    @POST("user/logout")
    @FormUrlEncoded
    Observable<BaseBean<Object>> logout(@Field("token") String token);


    /**
     * 获得投诉电话
     *
     * @param token
     * @return
     */
    @POST("seller/complain")
    @FormUrlEncoded
    Observable<BaseBean<ComplainPhoneBean>> getPhone(@Field("token") String token);

    /**
     * 用户结算接口
     *
     * @param token
     * @param rapeseed_selected
     * @param goods_id
     * @param spec_key
     * @param service_id
     * @param goods_num
     * @return
     */
    @POST("order/index")
    @FormUrlEncoded
    Observable<BaseBean<IndexBean>> getIndex(@Field("token") String token,
                                             @Field("rapeseed_selected") int rapeseed_selected,
                                             @Field("goods_id") String goods_id,
                                             @Field("spec_key") String spec_key,
                                             @Field("service_id") String service_id,
                                             @Field("goods_num") int goods_num);

    /**
     * 个人中心 - 获取用户信息
     *
     * @param token
     * @return
     */
    @POST("user/userinfo")
    @FormUrlEncoded
    Observable<BaseBean<UserInfoBean>> getInfo(@Field("token") String token);

    /**
     * 提现
     *
     * @param token
     * @param money
     * @param type
     * @param cash_pwd
     * @return
     */
    @POST("account/cash")
    @FormUrlEncoded
    Observable<BaseBean> getCashBalance(@Field("token") String token,
                                        @Field("money") String money,
                                        @Field("type") int type,
                                        @Field("paypwd") String cash_pwd);

    /**
     * 商品详情
     *
     * @param goods_id
     * @param token
     * @return
     */
    @POST("goods/goods_info")
    @FormUrlEncoded
    Observable<BaseBean<ProductDetailsBean>> getDetails(@Field("goods_id") String goods_id,
                                                        @Field("token") String token);


    /**
     * 商品的规格和增值服务
     *
     * @param goods_id
     * @return
     */
    @POST("goods/goods_spec")
    @FormUrlEncoded
    Observable<BaseBean<ServerBean>> getServer(@Field("goods_id") String goods_id);


    /**
     * 加入购物车
     *
     * @param token
     * @param goods_id
     * @param service_id
     * @param spec_key
     * @param goods_num
     * @return
     */
    @POST("cart/add_cart")
    @FormUrlEncoded
    Observable<BaseBean<Object>> addCart(@Field("token") String token,
                                         @Field("goods_id") String goods_id,
                                         @Field("service_id") String service_id,
                                         @Field("spec_key") String spec_key,
                                         @Field("goods_num") int goods_num);


    /**
     * 购物车商品选择
     *
     * @param token
     * @param id
     * @param selected
     * @param type
     * @return
     */
    @POST("cart/choice_cart")
    @FormUrlEncoded
    Observable<BaseBean<Object>> choiceCartGoods(@Field("token") String token,
                                                 @Field("id") int id,
                                                 @Field("selected") String selected,
                                                 @Field("type") int type);


    /**
     * 删除购物车商品
     *
     * @param token
     * @return
     */
    @POST("cart/del_cart")
    @FormUrlEncoded
    Observable<BaseBean<Object>> delCartGoods(@Field("token") String token);


    /**
     * 购物车加减数量
     *
     * @param token
     * @param id
     * @param goods_num
     * @param selected
     * @return
     */
    @POST("cart/num_cart")
    @FormUrlEncoded
    Observable<BaseBean<Object>> numCart(@Field("token") String token,
                                         @Field("id") int id,
                                         @Field("goods_num") int goods_num,
                                         @Field("selected") String selected);

    /**
     * 获取反馈信息类型
     *
     * @param token
     * @return
     */
    @POST("versions/feed_type")
    @FormUrlEncoded
    Observable<BaseBean<List<SetFeedbackTypeListBean>>> getFeedbackType(@Field("token") String token);

    /**
     * 修改登陆密码
     *
     * @param token
     * @param mobile
     * @param mobile_code
     * @param password
     * @return
     */
    @POST("user/update_pwd")
    @FormUrlEncoded
    Observable<BaseBean> changePassword(@Field("token") String token,
                                        @Field("mobile") String mobile,
                                        @Field("mobile_code") String mobile_code,
                                        @Field("password") String password);

    /**
     * 分类中的商品
     *
     * @param cat_id3
     * @param cat_id2
     * @param page
     * @return
     */
    @POST("classify/goods_list")
    @FormUrlEncoded
    Observable<BaseBean<List<ClassficationGoodsBean>>> classficationGoods(@Field("cat_id3") String cat_id3,
                                                                          @Field("cat_id2") String cat_id2,
                                                                          @Field("page") int page);

    /**
     * 获取全部评论
     *
     * @param goods_id
     * @param page
     * @return
     */
    @POST("goods/goods_comment")
    @FormUrlEncoded
    Observable<BaseBean<List<EvaluateListBean>>> getCommentList(@Field("goods_id") String goods_id,
                                                                @Field("page") int page);

    /**
     * 会员中心-立即购买
     *
     * @param token
     * @param type
     * @param id
     * @return
     */
    @POST("pay/user_pay")
    @FormUrlEncoded
    Observable<BaseBean<String>> gotoPayVip(@Field("token") String token,
                                            @Field("type") int type,
                                            @Field("id") int id,
                                            @Field("pay_password") String password);

    /**
     * 会员中心-立即购买  用微信
     *
     * @param token
     * @param type
     * @param id
     * @return
     */
    @POST("pay/user_pay")
    @FormUrlEncoded
    Observable<BaseBean<WeixinPayBean>> gotoPayVipByWeixin(@Field("token") String token,
                                                           @Field("type") int type,
                                                           @Field("id") int id,
                                                           @Field("paypwd") String password);

    /**
     * 会员中心-立即购买  用支付宝
     *
     * @param token
     * @param type
     * @param id
     * @return
     */
    @POST("pay/user_pay")
    @FormUrlEncoded
    Observable<BaseBean<WeixinPayBean>> gotoPayVipByAli(@Field("token") String token,
                                                        @Field("type") int type,
                                                        @Field("id") int id,
                                                        @Field("paypwd") String password);

    /**
     * 修改头像
     *
     * @param body
     * @return
     */
    @POST("user/upd_img")
    Observable<BaseBean<List>> changeHead(@Body RequestBody body);


    /**
     * 订单-使用优惠券列表
     *
     * @param token
     * @param order_money
     * @return
     */
    @POST("order/coupon")
    @FormUrlEncoded
    Observable<BaseBean<List<OrderCouponBean>>> useCoupon(@Field("token") String token,
                                                          @Field("order_money") String order_money);


    /**
     * 配送时间
     *
     * @return
     */
    @GET("order/distri_time")
    Observable<BaseBean<DistriTimeBean>> getDistriTime();
    /**
     * 退款记录
     *
     * @return
     */
    @GET("order/user_return_order_list")
    @FormUrlEncoded
    Observable<BaseBean<List<RefundBean>>> getUserReturnOrderList(@Field("token") String token,
                                                                  @Field("page") int page);
    /**
     * 非新人进入-领券中心
     *
     * @param token
     * @param page
     * @return
     */
    @POST("promotion/coupon")
    @FormUrlEncoded
    Observable<BaseBean<List<OrderCouponListBean>>> getCouponCenterData(@Field("token") String token,
                                                                        @Field("type") int type,
                                                                        @Field("page") int page);

    /**
     * 领券中心- 领取优惠券
     *
     * @param token
     * @param id
     * @return
     */
    @POST("promotion/receive_coupon")
    @FormUrlEncoded
    Observable<BaseBean> getCoupons(@Field("token") String token, @Field("coupon_id") String id);


    /**
     * 常见问题
     *
     * @param token
     * @return
     */
    @POST("versions/quest")
    @FormUrlEncoded
    Observable<BaseBean<List<FaqListBean>>> getFaqList(@Field("token") String token);

    /**
     * 常见问题-详情
     *
     * @param id
     * @return
     */
    @POST("versions/quest_details")
    @FormUrlEncoded
    Observable<BaseBean<FaqDetailsBean>> getFaqDetailsData(@Field("token") String token, @Field("cat_id") int id);

    /**
     * 消息中心
     *
     * @param token
     * @param type
     * @return
     */
    @POST("message/messageinfo")
    @FormUrlEncoded
    Observable<BaseBean<List<MessageCenterListBean>>> getMessageData(@Field("token") String token, @Field("type") int type, @Field("page") int page);

    /**
     * 提交订单
     *
     * @param address_id
     * @param number
     * @param goods_id
     * @param rapeseed
     * @param coupon_price
     * @param counpon_id
     * @param end_price
     * @param begin_time
     * @param end_time
     * @return
     */
    @POST("order/add_order")
    @FormUrlEncoded
    Observable<BaseBean<SubmitOrderBean>> addOrder(@Field("token") String token,
                                                   @Field("address_id") String address_id,
                                                   @Field("number") int number,
                                                   @Field("goods_id") String goods_id,
                                                   @Field("rapeseed") String rapeseed,
                                                   @Field("coupon_price") String coupon_price,
                                                   @Field("counpon_id") String counpon_id,
                                                   @Field("end_price") String end_price,
                                                   @Field("begin_time") String begin_time,
                                                   @Field("end_time") String end_time,
                                                   @Field("fee") String fee,
                                                   @Field("spec_key") String spec_key,
                                                   @Field("service_id") String service_id,
                                                   @Field("type") int type);

    /**
     * 余额支付
     *
     * @param token
     * @param order_sn
     * @param pay_password
     * @param type
     * @return
     */
    @POST("pay/index")
    @FormUrlEncoded
    Observable<BaseBean<Object>> pay(@Field("token") String token,
                                     @Field("order_sn") String order_sn,
                                     @Field("pay_password") String pay_password,
                                     @Field("type") String type);

    /**
     * 二次支付 - 余额支付
     *
     * @param token
     * @param order_sn
     * @param pay_password
     * @param type
     * @param employee_status
     * @return
     */
    @POST("pay/employee_pay")
    @FormUrlEncoded
    Observable<BaseBean<Object>> pay(@Field("token") String token,
                                     @Field("order_sn") String order_sn,
                                     @Field("pay_password") String pay_password,
                                     @Field("type") String type,
                                     @Field("employee_status") String employee_status);

    /**
     * 二次支付 - 阿里支付
     *
     * @param token
     * @param order_sn
     * @param type
     * @param employee_status
     * @return
     */
    @POST("pay/employee_pay")
    @FormUrlEncoded
    Observable<BaseBean<String>> alipayAgain(@Field("token") String token,
                                             @Field("order_sn") String order_sn,
                                             @Field("type") String type,
                                             @Field("employee_status") String employee_status);

    @POST("pay/employee_pay")
    @FormUrlEncoded
    Observable<BaseBean<WeixinPayBean>> wechatpayAgain(@Field("token") String token,
                                                       @Field("order_sn") String order_sn,
                                                       @Field("type") String type,
                                                       @Field("employee_status") String employee_status);

    /**
     * 发表评论
     *
     * @param body
     * @return
     */
    @POST("order/talk")
    Observable<BaseBean<Object>> evaluate(@Body RequestBody body);

    /**
     * 消息中心-删除消息
     *
     * @param token
     * @return
     */
    @POST("message/delete_msg")
    @FormUrlEncoded
    Observable<BaseBean> deleteMessageItem(@Field("token") String token, @Field("message_id") String message_id);

    /**
     * 消息中心 一级列表
     *
     * @param token
     * @return
     */
    @POST("message/unread_num")
    @FormUrlEncoded
    Observable<BaseBean<MessageCenterBean>> getMessageData(@Field("token") String token);

    /**
     * 是否存在支付密码
     *
     * @param token
     * @return
     */
    @POST("account/is_paypwd")
    @FormUrlEncoded
    Observable<BaseBean<IsPasswordExitsBean>> isPasswordExits(@Field("token") String token);


    /**
     * 订单使用优惠卷
     *
     * @param token
     * @return
     */
    @POST("order/use_coupon")
    @FormUrlEncoded
    Observable<BaseBean<Object>> selectCoupon(@Field("token") String token,
                                              @Field("cid") String cid);


    /**
     * 消息中心-详情
     *
     * @param token
     * @param id
     * @return
     */
    @POST("message/message_details")
    @FormUrlEncoded
    Observable<BaseBean<MessageCenterListBean>> getMessageDetails(@Field("token") String token, @Field("message_id") String id);


    /**
     * 确认收货
     *
     * @return
     */
    @POST("order/confirm_order")
    @FormUrlEncoded
    Observable<BaseBean<Object>> confirmOrder(@Field("token") String token, @Field("order_id") String order_id);


    /**
     * 支付宝支付获取订单信息
     *
     * @param token
     * @param order_sn
     * @param type     3为支付宝
     * @return
     */
    @POST("pay/index")
    @FormUrlEncoded
    Observable<BaseBean<String>> aliPay(@Field("token") String token,
                                        @Field("order_sn") String order_sn,
                                        @Field("type") String type);

    /**
     * 获取未读消息数量
     *
     * @param token
     * @return
     */
    @POST("message/all_numeber")
    @FormUrlEncoded
    Observable<BaseBean<MessageBean>> getMessageCount(@Field("token") String token);

    /**
     * 微信支付
     *
     * @param token
     * @param order_sn
     * @param type     支付方式 1 余额支付 2 app微信支付 3 app支付宝支付 4 微信公众号支付
     * @return
     */
    @POST("pay/index")
    @FormUrlEncoded
    Observable<BaseBean<WeixinPayBean>> weixinPay(@Field("token") String token,
                                                  @Field("order_sn") String order_sn,
                                                  @Field("type") String type);

    /**
     * 分享成功
     *
     * @param token
     * @param goods_id
     * @param type
     * @return
     */
    @POST("goods/already_share")
    @FormUrlEncoded
    Observable<BaseBean<Object>> alreadyShare(@Field("token") String token,
                                              @Field("goods_id") String goods_id,
                                              @Field("type") int type);

    /**
     * 提醒发货
     *
     * @param token
     * @param order_sn
     * @return
     */
    @POST("order/reminshop")
    @FormUrlEncoded
    Observable<BaseBean<Object>> remind(@Field("token") String token,
                                        @Field("order_id") String order_sn);


    /**
     * 用户退货
     *
     * @param token
     * @param order_sn
     * @return
     */
    @POST("order/returngoods")
    @FormUrlEncoded
    Observable<BaseBean<Object>> returnGoods(@Field("token") String token,
                                             @Field("order_id") String order_sn);

    /**
     * 检查更新
     *
     * @param type
     * @param version
     * @return
     */
    @POST("versions/check_upd")
    @FormUrlEncoded
    Observable<BaseBean<CheckVersionBean>> checkVersion(@Field("type") int type,
                                                        @Field("version") String version);


    /**
     * 获取购物车已选商品数量
     *
     * @param token
     * @return
     */
    @POST("cart/cart_goods_num")
    @FormUrlEncoded
    Observable<BaseBean<String>> shoppingCartCount(@Field("token") String token);


    /**
     * 新闻分享数据
     *
     * @param token
     * @param id
     * @return
     */
    @POST("news/news_details")
    @FormUrlEncoded
    Observable<BaseBean<NewsDetailsBean>> getNewsData(@Field("token") String token, @Field("id") String id);


    /**
     * 手机号码是否已经注册过
     *
     * @param mobile
     * @return
     */
    @POST("user/check_mobile_reg")
    @FormUrlEncoded
    Observable<BaseBean<IsRegisterBean>> isRegister(@Field("mobile") String mobile);

    /**
     * 购物车 清优惠券
     *
     * @param token
     * @return
     */
    @POST("order/settlement")
    @FormUrlEncoded
    Observable<BaseBean<Object>> settlement(@Field("token") String token);

    /**
     * 支付宝绑定
     *
     * @param token
     * @param bank_card
     * @param realname
     * @return
     */
    @POST("account/lock_alipay")
    @FormUrlEncoded
    Observable<BaseBean<Object>> bindingAlipay(@Field("token") String token, @Field("bank_card") String bank_card, @Field("realname") String realname);

    /**
     * 是否绑定过阿里帐号
     *
     * @param token
     * @return
     */
    @POST("account/is_alipay")
    @FormUrlEncoded
    Observable<BaseBean<IsAlipayBean>> isAlipay(@Field("token") String token);

    /**
     * 更改绑定阿里帐号
     *
     * @param token
     * @param bank_card
     * @param realname
     * @return
     */
    @POST("account/change_alipay")
    @FormUrlEncoded
    Observable<BaseBean<Object>> changeBindingAlipay(@Field("token") String token, @Field("bank_card") String bank_card, @Field("realname") String realname);

    /**
     * 二次支付 订单详情
     *
     * @param token
     * @param order_id
     * @return
     */
    @POST("order/employee")
    @FormUrlEncoded
    Observable<BaseBean<TopPayAgainDetailsBean>> toPayAgainDetails(@Field("token") String token, @Field("order_id") String order_id, @Field("order_goods") String order_goods);

    /**
     * @param token
     * @param order_sn
     * @return
     */
    @POST("order/order_status_query")
    @FormUrlEncoded
    Observable<BaseBean<LogisticsBean>> logistics(@Field("token") String token, @Field("order_id") String order_sn);

    @POST("order/user_order_status")
    @FormUrlEncoded
    Observable<BaseBean<OrderNumberBean>> getOrderNumber(@Field("token") String token);
}