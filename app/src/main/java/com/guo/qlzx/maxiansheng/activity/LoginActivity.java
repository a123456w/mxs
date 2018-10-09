package com.guo.qlzx.maxiansheng.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.guo.qlzx.maxiansheng.R;
import com.guo.qlzx.maxiansheng.bean.IsRegisterBean;
import com.guo.qlzx.maxiansheng.bean.LoginBean;
import com.guo.qlzx.maxiansheng.event.LoginEvent;
import com.guo.qlzx.maxiansheng.event.ShoppingCartEvent;
import com.guo.qlzx.maxiansheng.http.RemoteApi;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.EventBusUtil;
import com.qlzx.mylibrary.util.LogUtil;
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
 * describe: 登陆页面
 */

public class LoginActivity extends BaseActivity {
    @BindView(R.id.iv_head)
    ImageView ivHead;
    @BindView(R.id.edit_account)
    EditTextClearAble editAccount;
    @BindView(R.id.edit_password)
    EditTextClearAble editPassword;
    @BindView(R.id.tv_hint)
    TextView tvHint;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.tv_item_login)
    TextView tvItemLogin;
    @BindView(R.id.tv_item_register)
    TextView tvItemRegister;
    @BindView(R.id.edit_identify)
    EditTextClearAble editIdentify;
    @BindView(R.id.tv_send_code)
    TextView tvSendCode;
    @BindView(R.id.ll_identify)
    LinearLayout llIdentify;
    @BindView(R.id.edit_password_re)
    EditTextClearAble editPasswordRe;
    @BindView(R.id.ll_re_password)
    LinearLayout llRePassword;
    @BindView(R.id.iv_left)
    ImageView ivLeft;

    private SmsButtonUtil mSmsButtonUtil;
    private PreferencesHelper helper;
    private int type = 0;// 0 为登陆   1 为注册


    @Override
    public int getContentView() {
        return R.layout.activity_login;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        hideTitleBar();

        mSmsButtonUtil = new SmsButtonUtil(tvSendCode);
        mSmsButtonUtil.setCountDownText("重新获取（%ds）");
    }

    @Override
    public void getData() {
        tvItemLogin.setSelected(true);
        helper = new PreferencesHelper(this);

    }


    @OnClick({R.id.tv_hint, R.id.btn_login, R.id.tv_item_login, R.id.tv_item_register, R.id.tv_send_code,R.id.iv_left})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_hint:
                startActivityForResult(new Intent(LoginActivity.this, MobileLoginActivity.class), 100);
                break;
            case R.id.btn_login:

                if (type == 0) {
                    //登陆
                    if (TextUtils.isEmpty(editAccount.getText().toString().trim())) {
                        ToastUtil.showToast(LoginActivity.this, "手机号不能为空");
                        return;
                    }

                    if (TextUtils.isEmpty(editPassword.getText().toString().trim())) {
                        ToastUtil.showToast(LoginActivity.this, "密码不能为空");
                        return;
                    }
                    view.setClickable(false);
                    login(editAccount.getText().toString().trim(), editPassword.getText().toString().trim(), new PreferencesHelper(LoginActivity.this).getRegistrationID());
                } else if (type == 1) {
                    //注册
                    if (TextUtils.isEmpty(editAccount.getText().toString().trim())) {
                        ToastUtil.showToast(LoginActivity.this, "手机号不能为空");
                        return;
                    }

                    if (TextUtils.isEmpty(editIdentify.getText().toString().trim())) {
                        ToastUtil.showToast(LoginActivity.this, "验证码不能为空");
                        return;
                    }

                    if (TextUtils.isEmpty(editPassword.getText().toString().trim())) {
                        ToastUtil.showToast(LoginActivity.this, "密码不能为空");
                        return;
                    }

                    if (TextUtils.isEmpty(editPasswordRe.getText().toString().trim())) {
                        ToastUtil.showToast(LoginActivity.this, "重复密码不能为空");
                        return;
                    }

                    if (!editPassword.getText().toString().trim().equals(editPasswordRe.getText().toString().trim())) {
                        ToastUtil.showToast(LoginActivity.this, "两次密码输入不一致");
                        return;
                    }

                    if (editPassword.getText().toString().trim().length() < 6) {
                        ToastUtil.showToast(LoginActivity.this, "密码长度不能少于6位");
                        return;
                    }
                    view.setClickable(false);
                    register(editAccount.getText().toString().trim(), editIdentify.getText().toString().trim(), editPassword.getText().toString().trim());
                }
                break;
            case R.id.tv_item_login:
                if (!tvItemLogin.isSelected()) {
                    tvItemRegister.setSelected(false);
                    tvItemLogin.setSelected(true);
                    tvHint.setVisibility(View.VISIBLE);

                    type = 0;
                    llIdentify.setVisibility(View.GONE);
                    llRePassword.setVisibility(View.GONE);
                    btnLogin.setText("确定");
                }
                break;
            case R.id.tv_item_register:
                if (!tvItemRegister.isSelected()) {
                    tvItemLogin.setSelected(false);
                    tvItemRegister.setSelected(true);
                    tvHint.setVisibility(View.GONE);

                    type = 1;
                    llIdentify.setVisibility(View.VISIBLE);
                    llRePassword.setVisibility(View.VISIBLE);
                    btnLogin.setText("确定");
                }
                break;
            case R.id.tv_send_code:    //发送验证码
                String phone = editAccount.getText().toString().trim();
                if (TextUtils.isEmpty(phone)) {
                    ToastUtil.showToast(LoginActivity.this, "手机号不能为空");
                    return;
                }
                if (!ToolUtil.isChinaPhoneLegal(phone)) {
                    ToastUtil.showToast(LoginActivity.this, "手机号格式不正确");
                    return;
                }

                //isRegister(phone);
                getIdentifyCode(phone);

                break;
            case R.id.iv_left:
                finish();
                break;
        }
    }

    //是否注册过
    private void isRegister(final String mobile) {
        HttpHelp.getRetrofit(LoginActivity.this).create(RemoteApi.class).isRegister(mobile)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<IsRegisterBean>>(LoginActivity.this, null) {
                    @Override
                    public void onNext(BaseBean<IsRegisterBean> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            if (baseBean.data.getType() == 1) {
                                getIdentifyCode(mobile);
                            } else {
                                ToastUtil.showToast(LoginActivity.this, baseBean.message);
                            }
                        } else {
                            ToastUtil.showToast(LoginActivity.this, baseBean.message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
    }

    /**
     * 获取验证码
     *
     * @param moble
     */
    private void getIdentifyCode(String moble) {
        HttpHelp.getRetrofit(LoginActivity.this).create(RemoteApi.class).ident(moble,"1")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<Object>>(LoginActivity.this, null) {
                    @Override
                    public void onNext(BaseBean<Object> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            ToastUtil.showToast(LoginActivity.this, baseBean.message);
                            mSmsButtonUtil.startCountDown();
                        } else {
                            ToastUtil.showToast(LoginActivity.this, baseBean.message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        Log.i("identify", "onError: " + throwable.getMessage());
                        ToastUtil.showToast(LoginActivity.this, "发送失败");
                    }
                });
    }

    /**
     * 登陆
     *
     * @param mobile
     * @param password
     */
    private void login(String mobile, String password, String ji_token) {
        showLoadingDialog("登陆中");
        HttpHelp.getInstance().create(RemoteApi.class).login(mobile, password, ji_token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<LoginBean>>(LoginActivity.this, null) {
                    @Override
                    public void onNext(BaseBean<LoginBean> baseBean) {
                        super.onNext(baseBean);
                        btnLogin.setClickable(true);
                        if (baseBean.code == 0) {
                            helper.saveToken(baseBean.data.getToken());
                            helper.setVip(baseBean.data.getType());
                            hideLoadingDialog();
                            ToastUtil.showToast(LoginActivity.this, baseBean.message);
                            EventBusUtil.post(new LoginEvent(true));
                            EventBusUtil.post(new ShoppingCartEvent());
                            finish();

                        } else {
                            hideLoadingDialog();
                            ToastUtil.showToast(LoginActivity.this, baseBean.message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        btnLogin.setClickable(true);
                        super.onError(throwable);

                    }
                });
    }

    /**
     * 注册
     *
     * @param mobile
     * @param code
     * @param password
     */
    private void register(String mobile, String code, String password) {
        LogUtil.i("mobile: " + mobile + "  code: " + code + "  password: " + password);
        HttpHelp.getInstance().create(RemoteApi.class).register(mobile, code, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<Object>>(LoginActivity.this, null) {
                    @Override
                    public void onNext(BaseBean<Object> baseBean) {
                        super.onNext(baseBean);
                        btnLogin.setClickable(true);
                        if (baseBean.code == 0) {
                            ToastUtil.showToast(LoginActivity.this, baseBean.message);
                            login(editAccount.getText().toString().trim(), editPassword.getText().toString().trim(), new PreferencesHelper(LoginActivity.this).getRegistrationID());
                            editPasswordRe.setText("");
                            editAccount.setText("");
                            editPassword.setText("");
                            tvItemLogin.performClick();
                        } else {

                            ToastUtil.showToast(LoginActivity.this, baseBean.message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        btnLogin.setClickable(true);
                        super.onError(throwable);

                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == 100) {
            //电话号码登陆成功回调
            EventBusUtil.post(new LoginEvent(true));
            finish();
        }
    }
}
