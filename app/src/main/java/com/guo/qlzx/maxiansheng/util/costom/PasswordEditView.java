package com.guo.qlzx.maxiansheng.util.costom;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Selection;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.guo.qlzx.maxiansheng.R;
import com.jungly.gridpasswordview.GridPasswordView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 李 on 2018/4/19.
 */

public class PasswordEditView extends RelativeLayout{

    private View mView;
    private ImageView mClose;
    private TextView mTitle;
    private TextView mForgetPwd;
    private GridPasswordView mPassword;
    private PasswordKeyboardView mKeyboard;
    private List<String> passwordList;//记录键盘输入的值
    private StringBuilder mValue;//最后保存的密码

    public PasswordEditView(Context context) {
        this(context, null);
    }

    public PasswordEditView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PasswordEditView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mView = View.inflate(context, R.layout.layout_password, null);

        initView();
        initEvent();

        addView(mView);
    }

    private void initView(){
        mPassword = (GridPasswordView) mView.findViewById(R.id.view_password);
        mClose = (ImageView) mView.findViewById(R.id.iv_cancel);
        mTitle = (TextView) mView.findViewById(R.id.tv_title);
        mForgetPwd = (TextView) mView.findViewById(R.id.tv_forget);
        mKeyboard = (PasswordKeyboardView) mView.findViewById(R.id.view_keyboard);
        //打乱数字的位置
        //mKeyboard.shuffleKeyboard();
    }

    private void initEvent(){
        mValue = new StringBuilder();
        passwordList = new ArrayList<>();
        mKeyboard.setIOnKeyboardListener(new PasswordKeyboardView.IOnKeyboardListener() {

            @Override
            public void onInsertKeyEvent(String text) {
                mValue.setLength(0);
                passwordList.add(text);
                for (int i = 0; i < passwordList.size(); i++) {
                    mValue.append(passwordList.get(i));
                }
                mPassword.setPassword(mValue.toString());
            }

            @Override
            public void onDeleteKeyEvent() {
                mValue.setLength(0);
                if(passwordList.size() != 0){
                    passwordList.remove(passwordList.size()-1);
                    for (int i = 0; i < passwordList.size(); i++) {
                        mValue.append(passwordList.get(i));
                    }
                    mPassword.setPassword(mValue.toString());
                }
            }
        });
    }

    //获取输入的密码
    public String getPassword(){
        return mValue.toString();
    }

    //取消
    public ImageView getCloseImageView(){
        return mClose;
    }

    //标题
    public TextView getTitleTextView(){
        return mTitle;
    }

    //忘记密码
    public TextView getForgetTextView() {
        return mForgetPwd;
    }

    //输入密码控件
    public GridPasswordView getPswView(){
        return mPassword;
    }
    //清楚密码
    public void clearPassword(){
        if (mValue!=null){
            mValue.delete(0,5);
        }
        passwordList.clear();
        mPassword.clearPassword();
    }
}
