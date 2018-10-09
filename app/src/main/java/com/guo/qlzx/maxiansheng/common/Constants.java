package com.guo.qlzx.maxiansheng.common;

import com.tencent.mm.opensdk.openapi.IWXAPI;

/**
 * Created by Administrator on 2018/4/12 0012.
 */

public class Constants {
    public static String IDENTFIY_CODE_REGISTER ="1"; //注册时 获取验证码 type
    public static String IDENTFIY_CODE_LOGIN ="2";  //手机号登陆时 获取验证码 type
    public static String IDENTFIY_CODE_CHANG_PASSWORD ="3";   //修改密码时 获取验证码 type

    //支付宝
    public static final int SDK_PAY_FLAG = 1;
    public static final int SDK_AUTH_FLAG = 2;
    public static IWXAPI iwxapi;
}
