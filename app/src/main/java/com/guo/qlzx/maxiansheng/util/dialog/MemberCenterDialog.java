package com.guo.qlzx.maxiansheng.util.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.guo.qlzx.maxiansheng.R;

/**
 * Created by 李 on 2018/4/25.
 *  支付
 */

public class MemberCenterDialog extends Dialog implements CompoundButton.OnCheckedChangeListener {
    private ImageView cancel;
    private TextView balancePrice;
    private CheckBox balance;
    private CheckBox wechat;
    private CheckBox alipay;
    private TextView price;
    private Button submit;

    private Context mContext;

    private String mBalancePrice="";
    private String mPrice="";
    private int state=1;
    private OnGotoPayClickListener onGotoPayClickListener;
    private OnCancelClickListener onCancelClickListener;
    public MemberCenterDialog(@NonNull Context context,String mBalancePrice,String mPrice) {
        super(context,R.style.dialogBase);
        this.mContext=context;
        this.mBalancePrice=mBalancePrice;
        this.mPrice=mPrice;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_member_center);
        //按空白处不能取消动画
        setCanceledOnTouchOutside(false);
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.width = ViewGroup.LayoutParams.MATCH_PARENT;
        attributes.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        attributes.dimAmount = 0.5f;
        attributes.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        attributes.gravity = Gravity.BOTTOM;
        getWindow().setAttributes(attributes);
        //初始化界面控件
        initView();
        //初始化界面控件的事件
        initEvent();
        //放置数据
        initData();
    }

    private void initData() {
        price.setText("￥"+mPrice);
        balancePrice.setText(mBalancePrice+"元");
    }

    private void initEvent() {
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (onCancelClickListener!=null){
                    onCancelClickListener.onCancelListener();
                }
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onGotoPayClickListener!=null){
                    onGotoPayClickListener.onGotoPayListener(state);
                }
            }
        });
        wechat.setOnCheckedChangeListener(this);
        alipay.setOnCheckedChangeListener(this);
        balance.setOnCheckedChangeListener(this);
    }

    private void initView() {
        cancel=findViewById(R.id.iv_cancel);
        balancePrice=findViewById(R.id.tv_balance_price);
        balance=findViewById(R.id.cb_balance);
        wechat=findViewById(R.id.cb_wechat);
        alipay=findViewById(R.id.cb_alipay);
        price=findViewById(R.id.tv_price);
        submit=findViewById(R.id.btn_submit);
    }
    public void setOnGotoPayClickListener(OnGotoPayClickListener onGotoPayClickListener){
        this.onGotoPayClickListener=onGotoPayClickListener;
    }
    public void setOnCancelClickListener(OnCancelClickListener onCancelClickListener){
        this.onCancelClickListener=onCancelClickListener;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()){
            case R.id.cb_wechat:
                if (isChecked){
                    //顺序不可变
                    alipay.setChecked(false);
                    balance.setChecked(false);
                    state=2;
                }else {
                    state=0;
                }
                break;
            case R.id.cb_alipay:
                if (isChecked){
                    wechat.setChecked(false);
                    balance.setChecked(false);
                    state=3;
                }else {
                    state=0;
                }
                break;
            case R.id.cb_balance:
                if (isChecked){
                    alipay.setChecked(false);
                    wechat.setChecked(false);
                    state=1;
                }else {
                    state=0;
                }
                break;
        }
    }

    public interface OnGotoPayClickListener{
        public void onGotoPayListener(int state);
    }
    public interface OnCancelClickListener{
        public void onCancelListener();
    }
}
