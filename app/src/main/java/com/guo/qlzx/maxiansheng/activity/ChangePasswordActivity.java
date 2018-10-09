package com.guo.qlzx.maxiansheng.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.guo.qlzx.maxiansheng.R;
import com.guo.qlzx.maxiansheng.bean.PhoneCodeBean;
import com.guo.qlzx.maxiansheng.event.OrderNumberEvent;
import com.guo.qlzx.maxiansheng.http.RemoteApi;
import com.guo.qlzx.maxiansheng.util.ToolUtil;
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
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 李 on 2018/4/25.
 * 修改登陆密码
 */

public class ChangePasswordActivity extends BaseActivity {
    @BindView(R.id.et_tel)
    TextView etTel;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.et_again)
    EditText etAgain;
    @BindView(R.id.btn_code)
    Button btnCode;

    private PreferencesHelper helper;
    private Timer timer;

    @Override
    public int getContentView() {
        return R.layout.activity_change_password;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("修改密码");
        helper = new PreferencesHelper(this);
        etTel.setText(ToolUtil.setTingPhone(helper.getPhoneNum()));
    }

    @Override
    public void getData() {

    }

    //获取验证码
    private void getPhoneCode(String mobile) {
        HttpHelp.getInstance().create(RemoteApi.class).getPhoneCode(mobile,"2")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<PhoneCodeBean>>(this, null) {
                    @Override
                    public void onNext(BaseBean<PhoneCodeBean> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            ToastUtil.showToast(ChangePasswordActivity.this, baseBean.message);
                        } else {
                            if (timer!=null){
                                btnCode.setEnabled(true);
                                btnCode.setTextColor(getResources().getColor(R.color.blue_15));
                                btnCode.setText("获取验证码");
                                timer.cancel();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
    }
    //修改密码
    private void changePassword(String token,String mobile,String code,String password) {
        HttpHelp.getInstance().create(RemoteApi.class).changePassword(token,mobile,code,password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean>(this, null) {
                    @Override
                    public void onNext(BaseBean baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            ToastUtil.showToast(ChangePasswordActivity.this, baseBean.message);
                            finish();
                        }else if (baseBean.code==4){
                            ToolUtil.goToLogin(ChangePasswordActivity.this);
                        } else {
                            ToastUtil.showToast(ChangePasswordActivity.this, baseBean.message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
    }
    @OnClick({R.id.btn_submit, R.id.btn_code})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_submit:
                String pass=etPassword.getText().toString();
                String word=etAgain.getText().toString();
                String tel=helper.getPhoneNum();
                String code=etCode.getText().toString();
                if (TextUtils.isEmpty(code)){
                    ToastUtil.showToast(ChangePasswordActivity.this,"验证码不能为空");
                    return;
                }
                if (TextUtils.isEmpty(pass)){
                    ToastUtil.showToast(ChangePasswordActivity.this,"密码不能为空");
                    return;
                }
                if (TextUtils.isEmpty(word)){
                    ToastUtil.showToast(ChangePasswordActivity.this,"密码不能为空");
                    return;
                }
                if (!pass.equals(word)){
                    ToastUtil.showToast(ChangePasswordActivity.this,"两次输入密码不一致");
                    return;
                }
                changePassword(helper.getToken(),tel,code,pass);
                //确定
                break;
            case R.id.btn_code:
                //获取验证码
                getPhoneCode(helper.getPhoneNum());
                btnCode.setEnabled(false);
                btnCode.setTextColor(getResources().getColor(R.color.text_color6));
                timer = new Timer();
                final int[] time = {60};
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                btnCode.setText(String.valueOf(time[0]) + "s后重新发送");
                                time[0]--;
                                if (time[0] <= 0) {
                                    btnCode.setEnabled(true);
                                    btnCode.setText("获取验证码");
                                    timer.cancel();
                                }
                            }
                        });
                    }
                }, 1000, 1000);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer!=null){
            timer.cancel();
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        if (timer!=null){
            timer.cancel();
            btnCode.setEnabled(true);
            btnCode.setText("获取验证码");
        }
    }
}
