package com.guo.qlzx.maxiansheng.util.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.guo.qlzx.maxiansheng.R;

/**
 * Created by Administrator on 2018/5/9 0009.
 */

public class AlertDialog extends Dialog {

    private TextView ok;
    private TextView message;
    private Context mContext;
    private OkClickListener okClickListener;

    public AlertDialog(@NonNull Context context) {
        super(context, R.style.MyDialog);
        this.mContext=context;
    }

    public void setOkClickListener(OkClickListener okClickListener) {
        this.okClickListener = okClickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_alert);
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

        //设置取消按钮被点击后，向外界提供监听
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (okClickListener !=null){
                   okClickListener.onClick();
               }
               dismiss();
            }
        });
    }

    /**
     * 初始化界面控件
     */
    private void initView() {
        ok = (TextView) findViewById(R.id.tv_ok);
        message=(TextView)findViewById(R.id.tv_message);
    }


    public void setMessage(String tit) {
        message=(TextView)findViewById(R.id.tv_message);
        message.setText(tit);
    }

    public interface OkClickListener {
        public void onClick();
    }

}
