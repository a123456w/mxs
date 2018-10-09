package com.guo.qlzx.maxiansheng.util.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.guo.qlzx.maxiansheng.R;
import com.guo.qlzx.maxiansheng.event.ChangeFragmentEvent;
import com.guo.qlzx.maxiansheng.event.WxSuccessChangedEvent;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.util.EventBusUtil;
import com.qlzx.mylibrary.util.ToastUtil;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import cn.jiguang.share.android.api.JShareInterface;
import cn.jiguang.share.android.api.PlatActionListener;
import cn.jiguang.share.android.api.Platform;
import cn.jiguang.share.android.api.ShareParams;
import cn.jiguang.share.qqmodel.QQ;
import cn.jiguang.share.wechat.Wechat;
import cn.jiguang.share.wechat.WechatFavorite;
import cn.jiguang.share.wechat.WechatMoments;
import cn.jiguang.share.weibo.SinaWeibo;

import static com.tencent.mm.opensdk.constants.ConstantsAPI.COMMAND_SENDAUTH;
import static com.tencent.mm.opensdk.constants.ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX;

/**
 * Created by 李 on 2018/5/7.
 */

public class SharedDialog extends Dialog implements View.OnClickListener{

    LinearLayout llWechat;
    LinearLayout llFriend;
    LinearLayout llQq;
//    LinearLayout llWeibo;
    TextView tvCancel;

    private static String sharedUrl="";
    private static String imgUrl="";
    private static String title="";
    private static String content="";
    private Context context;
    public SharedDialog(@NonNull Context context) {
        super(context,R.style.dialogBase);
        this.context=context;
    }
    @Subscribe
    public void onWxSuccessChangedEvent(WxSuccessChangedEvent event) {
        switch (event.getCode()) {
            //根据需要的情况进行处理
            case BaseResp.ErrCode.ERR_OK:
                //正确返回
                //微信分享
                switch (event.getType()) {
                    case COMMAND_SENDAUTH:
                        Toast.makeText(context, "获取授权成功,正在登录，请稍后", Toast.LENGTH_LONG).show();

                        break;
                    case COMMAND_SENDMESSAGE_TO_WX:
                        //分享回调,处理分享成功后的逻辑
                        ToastUtil.showToast(context,"分享成功");
                        if (onSharedSuccessClickListener!=null) {
                            onSharedSuccessClickListener.onSuccess(1);
                        }
                        break;
                    default:
                        Toast.makeText(context, "授权发送返回", Toast.LENGTH_LONG).show();
                        break;
                }

                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                //用户取消
                ToastUtil.showToast(context,"用户取消");
                break;
            case BaseResp.ErrCode.ERR_SENT_FAILED:
                //发送失败
                ToastUtil.showToast(context,"发送失败");
                break;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_shared);
        //按空白处不能取消动画
        setCanceledOnTouchOutside(false);
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.width = ViewGroup.LayoutParams.MATCH_PARENT;
        attributes.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        attributes.dimAmount = 0.5f;
        attributes.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        attributes.gravity = Gravity.BOTTOM;
        getWindow().setAttributes(attributes);

        initView();
        initEvent();
    }

    private void initView() {
        llWechat=findViewById(R.id.ll_wechat);
        llFriend=findViewById(R.id.ll_friend);
//        llWeibo=findViewById(R.id.ll_weibo);
        llQq=findViewById(R.id.ll_qq);
        tvCancel=findViewById(R.id.tv_cancel);
    }

    private void initEvent() {
        tvCancel.setOnClickListener(this);
        llQq.setOnClickListener(this);
        llWechat.setOnClickListener(this);
        llFriend.setOnClickListener(this);
   //     llWeibo.setOnClickListener(this);
        EventBusUtil.register(this);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        EventBusUtil.register(this);
    }

    //    //分享监听回调
//    private PlatActionListener mPlatActionListener = new PlatActionListener() {
//        @Override
//        public void onComplete(Platform platform, int action, HashMap<String, Object> data) {
//            Log.i("TAG",platform.getName());
//            ToastUtil.showToast(context,"分享成功");
//            if (onSharedSuccessClickListener!=null){
//                if ("Wechat".equals(platform.getName())){
//                    //微信分享
//                    onSharedSuccessClickListener.onSuccess(1);
//                }else if ("WechatMoments".equals(platform.getName())){
//                    //微信朋友圈
//                    onSharedSuccessClickListener.onSuccess(2);
//                }else if ("QQ".equals(platform.getName())){
//                    onSharedSuccessClickListener.onSuccess(3);
//                }else {
//                    //微博
//                    onSharedSuccessClickListener.onSuccess(4);
//                    //QQ
//                }
//            }
//        }
//
//        @Override
//        public void onError(Platform platform, int action, int errorCode, Throwable error) {
//            ToastUtil.showToast(context,"分享失败"+errorCode);
//            //QQ返回50002
//        }
//
//        @Override
//        public void onCancel(Platform platform, int action) {
//            ToastUtil.showToast(context,"分享取消");
//            Log.i("TAG",action+"");
//        }
//    };
    //分享
    private void initData(String name,String sharedImg,String sharedUrl,String title,String content){
        ShareParams shareParams = new ShareParams();
        if (name!= SinaWeibo.Name){
            shareParams.setTitle(title);
        }
        shareParams.setText(content);
        shareParams.setShareType(Platform.SHARE_WEBPAGE);
        shareParams.setUrl(sharedUrl);//必须
//        shareParams.setUrl("https://blog.csdn.net/yzpbright/article/details/48475869");
        Bitmap  bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_icon);
        shareParams.setImageData(bitmap);
        JShareInterface.share(name, shareParams, new PlatActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                Log.i("TAG",platform.getName());
                ToastUtil.showToast(context,"分享成功");
                if (onSharedSuccessClickListener!=null){
                    if ("Wechat".equals(platform.getName())){
                        //微信分享
                        onSharedSuccessClickListener.onSuccess(1);
                    }else if ("WechatMoments".equals(platform.getName())){
                        //微信朋友圈
                        onSharedSuccessClickListener.onSuccess(2);
                    }else if ("QQ".equals(platform.getName())){
                        onSharedSuccessClickListener.onSuccess(3);
                    }else {
                        //微博
                        onSharedSuccessClickListener.onSuccess(4);
                        //QQ
                    }
                }
            }

            @Override
            public void onError(Platform platform, int action, int errorCode, Throwable error) {
                Looper.prepare();
                ToastUtil.showToast(context,"分享失败"+errorCode);
                Looper.loop();
                //QQ返回50002
            }

            @Override
            public void onCancel(Platform platform, int action) {
                ToastUtil.showToast(context,"分享取消");
                Log.i("TAG",action+"");
            }
        });
    }

    //四个按键监听
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_qq:
                if(isQQClientAvailable()){
                    if(content.length()>30){
                      String myContent = content.substring(0,28);
                        initData(QQ.Name,imgUrl,sharedUrl,title,myContent+"...");
                    }else {
                        initData(QQ.Name,imgUrl,sharedUrl,title,content);
                    }

                }else {
                    ToastUtil.showToast(context,"请安装QQ");
                }

                dismiss();
                break;
            case R.id.ll_wechat:
                if(isWeixinAvilible()){
                    initData(Wechat.Name,imgUrl,sharedUrl,title,content);
                }else {
                    ToastUtil.showToast(context,"请安装微信");
                }
               // initData(Wechat.Name,imgUrl,sharedUrl,title,content);

                dismiss();
                break;
//            case R.id.ll_weibo:
//                ToastUtil.showToast(context,"暂无开通");
//              //  initData(SinaWeibo.Name,imgUrl,sharedUrl,title,content);
//               // dismiss();
//                break;
            case R.id.ll_friend:
                if(isWeixinAvilible()) {
                    initData(WechatMoments.Name, imgUrl, sharedUrl, title, content);
                }else {
                    ToastUtil.showToast(context,"请安装微信");
                }
                dismiss();
                break;
            case R.id.tv_cancel:
                dismiss();
                break;
        }
    }
    private  static SharedDialog sharedDialog;
    public static SharedDialog showDialog(String img,String url,String tit,String con,Context context){
        sharedDialog=new SharedDialog(context);
        imgUrl=img;
        sharedUrl=url;
        title=tit;
        content=con;
        return sharedDialog;
    }
    private OnSharedSuccessClickListener onSharedSuccessClickListener;

    public void setOnSharedSuccessClickListener(OnSharedSuccessClickListener onSharedSuccessClickListener) {
        this.onSharedSuccessClickListener = onSharedSuccessClickListener;
    }

    public interface OnSharedSuccessClickListener{
        void onSuccess(int type);
    }
    /**
     * 判断qq是否可用
     *
     *
     @return
     */

    public  boolean isQQClientAvailable() {

        final PackageManager packageManager = context.getPackageManager();

        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);

        if (pinfo != null) {

            for (int i = 0; i < pinfo.size(); i++) {

                String pn = pinfo.get(i).packageName;

                if (pn.equals("com.tencent.mobileqq")) {

                    return true;

                }

            }

        }

        return false;

    }

//判断微信是否可用

    public  boolean isWeixinAvilible() {

        final PackageManager packageManager = context.getPackageManager();

        // 获取packagemanager

        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);

        // 获取所有已安装程序的包信息

        if (pinfo != null) {

            for (int i = 0; i < pinfo.size(); i++) {

                String pn = pinfo.get(i).packageName;

                if (pn.equals("com.tencent.mm")) {

                    return true;

                }

            }

        }

        return false;

    }
}
