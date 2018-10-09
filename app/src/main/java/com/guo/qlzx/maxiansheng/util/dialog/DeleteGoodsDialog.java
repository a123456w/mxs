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

import butterknife.BindView;

/**
 * Created by 李 on 2018/5/7.
 */

public class DeleteGoodsDialog extends Dialog {
    TextView cancel;
    TextView tvGo;
    private Context mContext;
    private onGoToUseOnclickListener onGoToUseOnclickListener;//确定按钮被点击了的监听器

    public DeleteGoodsDialog(@NonNull Context context) {
        super(context, R.style.MyDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_delete_goods);
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

    private void initView() {
        cancel=findViewById(R.id.tv_cancel);
        tvGo=findViewById(R.id.tv_go);
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
              /*  if (cancelOnclickListener != null) {
                    cancelOnclickListener.onCancelClick();
                }*/
            }
        });
        //设置取消按钮被点击后，向外界提供监听
        tvGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (onGoToUseOnclickListener!=null){
                    onGoToUseOnclickListener.onUseClick();
                }
            }
        });
    }


    public void setOnGoToUseOnclickListener(DeleteGoodsDialog.onGoToUseOnclickListener onGoToUseOnclickListener) {
        this.onGoToUseOnclickListener = onGoToUseOnclickListener;
    }

    /**
     * 设置确定按钮和取消被点击的接口
     */
    public interface onGoToUseOnclickListener {
        public void onUseClick();
    }
}
