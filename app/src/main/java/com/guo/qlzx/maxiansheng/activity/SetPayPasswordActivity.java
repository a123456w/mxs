package com.guo.qlzx.maxiansheng.activity;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.guo.qlzx.maxiansheng.R;
import com.guo.qlzx.maxiansheng.bean.PhoneCodeBean;
import com.guo.qlzx.maxiansheng.event.OrderNumberEvent;
import com.guo.qlzx.maxiansheng.event.OrderPayPwEvent;
import com.guo.qlzx.maxiansheng.event.SetPasswordEvent;
import com.guo.qlzx.maxiansheng.http.RemoteApi;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.EventBusUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 李 on 2018/4/13.
 * 设置提现密码 接口改页面改 未完成
 */

public class SetPayPasswordActivity extends BaseActivity {

    @BindView(R.id.et_code)
    EditText etOld;
    @BindView(R.id.et_new)
    EditText etNew;
    @BindView(R.id.et_again)
    EditText etAgain;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    @BindView(R.id.btn_get)
    Button btnGet;

    private PreferencesHelper helper;
    private Timer timer;

    @Override
    public int getContentView() {
        return R.layout.activity_set_password;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setLeftImageRes(R.drawable.ic_back_gray);
        helper = new PreferencesHelper(this);
        titleBar.setTitleText("设置交易密码");
        initEvent();

    }

    //三个监听
    private void initEvent() {
        //重新输入密码
        etAgain.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (etOld.getVisibility() == View.VISIBLE) {
                    if (!TextUtils.isEmpty(etNew.getText().toString()) && !TextUtils.isEmpty(etOld.getText().toString()) && !TextUtils.isEmpty(s.toString())) {
                        btnSubmit.setEnabled(true);
                    } else {
                        btnSubmit.setEnabled(false);
                    }
                } else {
                    if (!TextUtils.isEmpty(etNew.getText().toString()) && !TextUtils.isEmpty(s.toString())) {
                        btnSubmit.setEnabled(true);
                    } else {
                        btnSubmit.setEnabled(false);
                    }
                }

            }
        });
        //验证码
        etOld.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(etNew.getText().toString()) && !TextUtils.isEmpty(etAgain.getText().toString()) && !TextUtils.isEmpty(s.toString())) {
                    btnSubmit.setEnabled(true);
                } else {
                    btnSubmit.setEnabled(false);
                }
            }
        });
        //输入密码 第一次
        etNew.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (!TextUtils.isEmpty(etNew.getText().toString()) && !TextUtils.isEmpty(etOld.getText().toString()) && !TextUtils.isEmpty(s.toString())) {
                    btnSubmit.setEnabled(true);
                } else {
                    btnSubmit.setEnabled(false);
                }
            }
        });
        //确定
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String neww = etNew.getText().toString();
                String again = etAgain.getText().toString();
                String code = etOld.getText().toString();
                if (neww.length() < 6) {
                    ToastUtil.showToast(SetPayPasswordActivity.this, "密码不能小于6位");
                    return;
                }
                if (again.length() < 6) {
                    ToastUtil.showToast(SetPayPasswordActivity.this, "密码不能小于6位");
                    return;
                }
                if (!neww.equals(again)) {
                    ToastUtil.showToast(SetPayPasswordActivity.this, "两次输入不一致");
                    return;
                }
                setPassword(neww, helper.getPhoneNum(), code, helper.getToken());

            }
        });
        btnGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPhoneCode(helper.getPhoneNum());
                btnGet.setEnabled(false);
                btnGet.setTextColor(getResources().getColor(R.color.text_color6));
                timer = new Timer();
                final int[] time = {60};
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                btnGet.setText(String.valueOf(time[0])+"s后重新发送");
                                time[0]--;
                                if (time[0] <=0){
                                    btnGet.setEnabled(true);
                                    btnGet.setTextColor(getResources().getColor(R.color.blue_15));
                                    btnGet.setText("获取验证码");
                                    timer.cancel();
                                }
                            }
                        });
                    }
                }, 1000, 1000);
            }
        });
    }

    @Override
    public void getData() {

    }

    private void setPassword(String paypwd, String mobile, String mobile_code, String token) {
        BaseSubscriber<BaseBean> baseSubscriber = new BaseSubscriber<BaseBean>(this, null) {
            @Override
            public void onNext(BaseBean baseBean) {
                super.onNext(baseBean);
                if (baseBean.code == 0) {
                    ToastUtil.showToast(SetPayPasswordActivity.this, baseBean.message);
                    EventBusUtil.post(new SetPasswordEvent());
                    finish();
                } else {
                    ToastUtil.showToast(SetPayPasswordActivity.this, baseBean.message);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
            }
        };
        HttpHelp.getInstance().create(RemoteApi.class).setPassword(paypwd, mobile, mobile_code, token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(baseSubscriber);
    }
    //获取验证码
    private void getPhoneCode( String token) {
        HttpHelp.getInstance().create(RemoteApi.class).getPhoneCode(token,"2")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<PhoneCodeBean>>(this, null) {
                    @Override
                    public void onNext(BaseBean<PhoneCodeBean> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            ToastUtil.showToast(SetPayPasswordActivity.this,baseBean.message);
                        } else {
                        }
                    }
                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBusUtil.post(new OrderPayPwEvent());
    }
}
