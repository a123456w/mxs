package com.guo.qlzx.maxiansheng.activity;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.guo.qlzx.maxiansheng.R;
import com.guo.qlzx.maxiansheng.bean.PhoneCodeBean;
import com.guo.qlzx.maxiansheng.http.RemoteApi;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.http.HttpHelp;
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
 * Created by 李 on 2018/4/12.
 * 绑定新手机号
 */

public class VerifyPhoneActivity extends BaseActivity {
    @BindView(R.id.et_enter)
    EditText etEnter;
    @BindView(R.id.btn_get)
    Button btnGet;
    @BindView(R.id.et_enter_code)
    EditText etEnterCode;
    @BindView(R.id.btn_submit)
    Button btnSubmit;

    private String phoneNumber="";
    private String token="";
    private int type=5;
    private PreferencesHelper helper;
    @Override
    public int getContentView() {
        return R.layout.activity_verify_phone;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setLeftImageRes(R.drawable.ic_back_gray);
        titleBar.setTitleText("绑定新手机");
        helper=new PreferencesHelper(this);
        token=new PreferencesHelper(this).getToken();
        etEnterCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString())&&!TextUtils.isEmpty(etEnter.getText().toString())){
                    btnSubmit.setEnabled(true);
                }else {
                    btnSubmit.setEnabled(false);
                }
            }
        });
        etEnter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString())&&!TextUtils.isEmpty(etEnterCode.getText().toString())){
                    btnSubmit.setEnabled(true);
                }else {
                    btnSubmit.setEnabled(false);
                }
            }
        });
        btnGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNumber=etEnter.getText().toString();
                if (phoneNumber==null||TextUtils.isEmpty(phoneNumber)){
                    ToastUtil.showToast(VerifyPhoneActivity.this,"请输入手机号码");
                    return;
                }
                getPhoneCode(phoneNumber);
                btnGet.setEnabled(false);
                btnGet.setTextColor(getResources().getColor(R.color.text_color6));
                final Timer timer = new Timer();
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
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNumber=etEnter.getText().toString();
                if (phoneNumber==null||TextUtils.isEmpty(phoneNumber)){
                    ToastUtil.showToast(VerifyPhoneActivity.this,"请输入手机号码");
                    return;
                }
                setNewPhoneNumber(token,etEnter.getText().toString(),etEnterCode.getText().toString());
            }
        });
    }

    @Override
    public void getData() {
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
                            ToastUtil.showToast(VerifyPhoneActivity.this,baseBean.message);
                        } else {
                            ToastUtil.showToast(VerifyPhoneActivity.this,baseBean.message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
    }

    //绑定新手机号码
    private void setNewPhoneNumber(String  token,String  new_mobile, String  mobile_code){
        HttpHelp.getInstance().create(RemoteApi.class).setNewPhone(token, new_mobile,mobile_code)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean>(this, null) {
                    @Override
                    public void onNext(BaseBean baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            ToastUtil.showToast(VerifyPhoneActivity.this,baseBean.message);
                            helper.savePhoneNum(etEnter.getText().toString());
                            finish();
                        } else {
                            ToastUtil.showToast(VerifyPhoneActivity.this,baseBean.message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });

    }

}
