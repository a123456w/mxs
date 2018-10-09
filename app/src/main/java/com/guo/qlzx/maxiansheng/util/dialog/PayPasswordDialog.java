package com.guo.qlzx.maxiansheng.util.dialog;

import android.app.Activity;
import android.app.Dialog;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.guo.qlzx.maxiansheng.R;
import com.guo.qlzx.maxiansheng.activity.SetPayPasswordActivity;
import com.guo.qlzx.maxiansheng.util.costom.PasswordEditView;
import com.guo.qlzx.maxiansheng.util.costom.PasswordKeyboardView;
import com.jungly.gridpasswordview.GridPasswordView;

/**
 * Created by 李 on 2018/4/28.
 */

public class PayPasswordDialog extends DialogFragment {

    private PasswordEditView mPassword;

    private static Context mContext;
    public static PayPasswordDialog newInstace(Context context){
        PayPasswordDialog passWordFragment = new PayPasswordDialog();
        mContext=context;
        return passWordFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    //继承BottomSheetDialogFragment时onStart()可注释掉
    @Override
    public void onStart() {
        super.onStart();

        Window win = getDialog().getWindow();
        win.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparent)));
        WindowManager.LayoutParams layoutParams = getDialog().getWindow().getAttributes();

        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        layoutParams.width = (int) (dm.widthPixels);
        win.setAttributes(layoutParams);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.dialog_password, container);
        init(view);
        return view;
    }

    private void init(View view){
        mPassword = (PasswordEditView) view.findViewById(R.id.view_password);
        mPassword.getCloseImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                if (onCancelClickListener !=null){
                    onCancelClickListener.onCancel();
                }
            }
        });

        mPassword.getForgetTextView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               //忘记密码
                dismiss();
                if (onForgetClickListener !=null){
                    onForgetClickListener.onForget();
                }
                startActivity(new Intent(mContext, SetPayPasswordActivity.class));
            }
        });

        mPassword.getPswView().setOnPasswordChangedListener(new GridPasswordView.OnPasswordChangedListener() {
            @Override
            public void onTextChanged(String psw) {
                //输入6位密码后
                if (mPassword.getPassword().length() == 6){
                    if (onFinishClickListener!=null){
                        onFinishClickListener.onFinish(mPassword.getPassword());
                    }
                }
            }

            @Override
            public void onInputFinish(String psw) {

            }
        });
    }
    public void clear(){
        mPassword.clearPassword();
    }
    private OnForgetClickListener onForgetClickListener;
    private OnFinishClickListener onFinishClickListener;
    private OnCancelClickListener onCancelClickListener;

    public void setOnCancelClickListener(OnCancelClickListener onCancelClickListener) {
        this.onCancelClickListener = onCancelClickListener;
    }

    public void setOnFinishClickListener(OnFinishClickListener onFinishClickListener) {
        this.onFinishClickListener = onFinishClickListener;
    }

    public void setOnForgetClickListener(OnForgetClickListener onForgetClickListener) {
        this.onForgetClickListener = onForgetClickListener;
    }

    public interface OnForgetClickListener{ void onForget();}
    public interface OnFinishClickListener{ void onFinish(String psw);}
    public interface OnCancelClickListener{ void onCancel();}
}
