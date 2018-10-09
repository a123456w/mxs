package com.guo.qlzx.maxiansheng.util.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.guo.qlzx.maxiansheng.R;
import com.guo.qlzx.maxiansheng.adapter.HomeCouponsAdapter;

/**
 * Created by 李 on 2018/4/13.
 */

public class AddressDeleteDialog extends Dialog {
    private TextView cancel;
    private TextView goToUse;
    private TextView title;

    private HomeCouponsAdapter couponsAdapter;
    private Context mContext;
    private onCancelOnclickListener cancelOnclickListener;//取消按钮被点击了的监听器
    private onGoToUseOnclickListener onGoToUseOnclickListener;//确定按钮被点击了的监听器
    public AddressDeleteDialog(Context context) {
        super(context, R.style.MyDialog);
        this.mContext=context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_address_delete);
        //按空白处不能取消动画
        setCanceledOnTouchOutside(false);
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.width = ViewGroup.LayoutParams.MATCH_PARENT;
        attributes.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        attributes.dimAmount = 0.5f;
        attributes.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        attributes.gravity = Gravity.CENTER;
        getWindow().setAttributes(attributes);
        //初始化界面控件
        initView();
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
                dismiss();
                if (cancelOnclickListener != null) {
                    cancelOnclickListener.onCancelClick();
                }
            }
        });
        //设置取消按钮被点击后，向外界提供监听
        goToUse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (onGoToUseOnclickListener != null) {
                    onGoToUseOnclickListener.onUseClick();
                }
            }
        });
    }

    /**
     * 初始化界面控件
     */
    private void initView() {
        cancel = (TextView) findViewById(R.id.tv_cancel);
        goToUse = (TextView) findViewById(R.id.tv_go);
        title=(TextView)findViewById(R.id.tv_title);
    }

    public void setTitle(String tit) {
        title=(TextView)findViewById(R.id.tv_title);
        title.setText(tit);
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
