package com.guo.qlzx.maxiansheng.util.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.guo.qlzx.maxiansheng.R;
import com.guo.qlzx.maxiansheng.adapter.HomeCouponsAdapter;
import com.guo.qlzx.maxiansheng.bean.HomeCouponListBeans;
import com.guo.qlzx.maxiansheng.bean.HomeTopBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 李 on 2018/4/11.
 */

public class HomeCouponDialog extends Dialog {
    private ImageView cancel;
    private ListView listView;
    private TextView goToUse;

    private TextView name;
    private HomeCouponsAdapter couponsAdapter;
    private List<HomeTopBean.NewCouponBean> listBeans=new ArrayList<>();
    private Context mContext;
    private onCancelOnclickListener cancelOnclickListener;//取消按钮被点击了的监听器
    private onGoToUseOnclickListener onGoToUseOnclickListener;//确定按钮被点击了的监听器
    public HomeCouponDialog(@NonNull Context context, List<HomeTopBean.NewCouponBean> listBeans) {
        super(context, R.style.MyDialog);
        this.mContext=context;
        this.listBeans=listBeans;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_home_coupons);
        //按空白处不能取消动画
        setCanceledOnTouchOutside(false);
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
        attributes.width = (int) (metrics.widthPixels * 0.9);
        attributes.height = (int) (metrics.heightPixels * 0.9);
        attributes.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        attributes.dimAmount = 0.5f;
        getWindow().setAttributes(attributes);
        //初始化界面控件
        initView();
        //初始化界面数据
        initData();
        //初始化界面控件的事件
        initEvent();
    }


    /**
     * 初始化界面的确定和取消监听器
     */
    private void initEvent() {
        //设置确定按钮被点击后，向外界提供监听
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cancelOnclickListener != null) {
                    cancelOnclickListener.onCancelClick();
                }
            }
        });
        //设置取消按钮被点击后，向外界提供监听
        goToUse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onGoToUseOnclickListener != null) {
                    onGoToUseOnclickListener.onUseClick();
                }
            }
        });
    }
    /**
     * 初始化界面控件的显示数据
     */
    private void initData() {
        couponsAdapter=new HomeCouponsAdapter(listView);
        listView.setAdapter(couponsAdapter);
        couponsAdapter.setData(listBeans);
    }
    /**
     * 初始化界面控件
     */
    private void initView() {
        cancel = (ImageView) findViewById(R.id.iv_cancel);
        listView = (ListView) findViewById(R.id.tv_coupons_list);
        goToUse = (TextView) findViewById(R.id.tv_coupons_go);
        name=(TextView) findViewById(R.id.tv_name);
    }
    public void setName(String mName){
        name.setText(mName);
    }
    /**
     * 设置取消按钮的显示内容和监听
     */
    public void setCancelOnclickListener(onCancelOnclickListener onNoOnclickListener) {
        this.cancelOnclickListener = onNoOnclickListener;
    }

    /**
     * 设置去使用按钮的显示内容和监听
     */
    public void setGoToUseOnclickListener( onGoToUseOnclickListener onYesOnclickListener) {
        this.onGoToUseOnclickListener = onYesOnclickListener;
    }

    /**
     * 设置确定按钮和取消被点击的接口
     */
    public interface onCancelOnclickListener {
        public void onCancelClick();
    }

    public interface onGoToUseOnclickListener {
        public void onUseClick();
    }
}
