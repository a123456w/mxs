package com.guo.qlzx.maxiansheng.util.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.guo.qlzx.maxiansheng.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by 李 on 2018/4/27.
 */

public class CouponCenterDialog extends Dialog {
    private static CouponCenterDialog dialog;
    private TextView time;
    private static Timer timer;
    public CouponCenterDialog(@NonNull Context context) {
        super(context, R.style.dialogBase);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_coupon_center);
        setCanceledOnTouchOutside(false);
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.width = ViewGroup.LayoutParams.MATCH_PARENT;
        attributes.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        attributes.dimAmount = 0.5f;
        attributes.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        attributes.gravity = Gravity.CENTER;
        getWindow().setAttributes(attributes);
        initView();
    }

    private void initView() {
        time=findViewById(R.id.tv_time);

    }

    //显示dialog的方法
    public static CouponCenterDialog showDialog(final Context context){
        dialog = new CouponCenterDialog(context);//dialog样式
        dialog.setContentView(R.layout.dialog_load);//dialog布局文件
        return dialog;
    }
    public void setTime(int i){
        time.setText( i+"s后自动返回");
    }
}
