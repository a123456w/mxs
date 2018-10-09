package com.guo.qlzx.maxiansheng.application;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.guo.qlzx.maxiansheng.util.crash.CrashHandlerUtil;
import com.qlzx.mylibrary.util.LogUtil;

import cn.jiguang.share.android.api.JShareInterface;
import cn.jiguang.share.android.api.PlatformConfig;
import cn.jpush.android.api.JPushInterface;

/**声明的application
 * Created by dillon on 2018/4/10.
 */

public class MyApplication extends MultiDexApplication {
    private static Handler mainHandler;//全局的handler
    private static Context mContext;
    public static Boolean isDebug=true;

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = this;

        //初始化mainHandler
        mainHandler = new Handler();

        //初始化极光推送
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        String id = JPushInterface.getRegistrationID(this);
        LogUtil.i("极光id:"+ id);
        PlatformConfig platformConfig = new PlatformConfig()
                .setWechat("wx31005ccd063e38f6", "7a67d4dc2cc7596cfa70c699ed618c72")
                .setQQ("1106750245", "ElXfTjSjZDCdpRQt")
                .setSinaWeibo("374535501", "baccd12c166f1df96736b51ffbf600a2", "https://www.jiguang.cn");
        /**
         * since 1.5.0，1.5.0版本后增加API，支持在代码中设置第三方appKey等信息，当PlatformConfig为null时，或者使用JShareInterface.init(Context)时需要配置assets目录下的JGShareSDK.xml
         **/
        //*
        JShareInterface.init(this, platformConfig);
        //初始化mainHandler
        mainHandler = new Handler();
        /**
         * 崩溃日志收集初始化
         */
        CrashHandlerUtil.getInstance().init(mContext);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static Context getContext() {
        return mContext;
    }

    public static Handler getMainHandler() {
        return mainHandler;
    }
}
