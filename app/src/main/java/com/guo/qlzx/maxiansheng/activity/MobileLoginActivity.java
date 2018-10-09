package com.guo.qlzx.maxiansheng.activity;


import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.guo.qlzx.maxiansheng.R;
import com.guo.qlzx.maxiansheng.bean.IsRegisterBean;
import com.guo.qlzx.maxiansheng.bean.LoginBean;
import com.guo.qlzx.maxiansheng.http.RemoteApi;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.SmsButtonUtil;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.util.ToolUtil;
import com.qlzx.mylibrary.widget.EditTextClearAble;
import com.qlzx.mylibrary.widget.LoadingLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/4/12 0012.
 * describe  手机号登录
 */

public class MobileLoginActivity extends BaseActivity {

    @BindView(R.id.edit_phone)
    EditTextClearAble editPhone;
    @BindView(R.id.edit_identify)
    EditTextClearAble editIdentify;
    @BindView(R.id.tv_send_code)
    TextView tvSendCode;
    @BindView(R.id.btn_login)
    Button btnLogin;

    private SmsButtonUtil mSmsButtonUtil;
    private PreferencesHelper helper;
    private String jpushId="";

    @Override
    public int getContentView() {
        return R.layout.activity_mobile_login;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setLeftImageRes(R.drawable.ic_back_gray);
        titleBar.setTitleText("手机号登录");
        helper = new PreferencesHelper(this);
        mSmsButtonUtil = new SmsButtonUtil(tvSendCode);
        mSmsButtonUtil.setCountDownText("重新获取（%ds）");

        jpushId = JPushInterface.getRegistrationID(this);
    }

    @Override
    public void getData() {

    }


    @OnClick({R.id.tv_send_code, R.id.btn_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_send_code:
                String phone = editPhone.getText().toString().trim();
                if (TextUtils.isEmpty(phone)) {
                    ToastUtil.showToast(MobileLoginActivity.this, "手机号不能为空");
                    return;
                }
                if (!ToolUtil.isChinaPhoneLegal(phone)) {
                    ToastUtil.showToast(MobileLoginActivity.this, "手机号格式不正确");
                    return;
                }
                tvSendCode.setEnabled(false);
                isRegister(phone);



                break;
            case R.id.btn_login:
                String phone1 = editPhone.getText().toString().trim();
                String code = editIdentify.getText().toString().trim();
                if (TextUtils.isEmpty(phone1)) {
                    ToastUtil.showToast(MobileLoginActivity.this, "手机号不能为空");
                    return;
                }
                if (TextUtils.isEmpty(code)) {
                    ToastUtil.showToast(MobileLoginActivity.this, "验证码不能为空");
                    return;
                }
                if (!ToolUtil.isChinaPhoneLegal(phone1)) {
                    ToastUtil.showToast(MobileLoginActivity.this, "手机号格式不正确");
                    return;
                }

                if (!TextUtils.isEmpty(jpushId)){
                    login(phone1, code,jpushId);
                }else {
                    ToastUtil.showToast(MobileLoginActivity.this,"服务器错误！");
                    jpushId = JPushInterface.getRegistrationID(MobileLoginActivity.this);
                }
                break;
        }
    }
    //是否注册过
    private void isRegister(final String mobile){
        getIdentifyCode(mobile);

    }


    /**
     * 获取验证码
     *
     * @param moble
     */
    private void getIdentifyCode(String moble) {
        HttpHelp.getInstance().create(RemoteApi.class).ident(moble,"2")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<Object>>(MobileLoginActivity.this, null) {
                    @Override
                    public void onNext(BaseBean<Object> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            ToastUtil.showToast(MobileLoginActivity.this, baseBean.message);
                            mSmsButtonUtil.startCountDown();
                        } else {
                            ToastUtil.showToast(MobileLoginActivity.this, baseBean.message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        mSmsButtonUtil.startCountDown();
                    }
                });
    }


    private void login(String mobile, String code,String jpush_id) {
        showLoadingDialog("登录中");
        HttpHelp.getInstance().create(RemoteApi.class).mobileLogin(mobile, code,jpush_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<LoginBean>>(MobileLoginActivity.this, null) {
                    @Override
                    public void onNext(BaseBean<LoginBean> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            helper.saveToken(baseBean.data.getToken());
                            hideLoadingDialog();
                            ToastUtil.showToast(MobileLoginActivity.this, baseBean.message);
                            setResult(100);
                            finish();

                        } else {
                            hideLoadingDialog();
                            ToastUtil.showToast(MobileLoginActivity.this, baseBean.message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        hideLoadingDialog();
                    }
                });
    }
}
