package com.guo.qlzx.maxiansheng.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.guo.qlzx.maxiansheng.R;
import com.guo.qlzx.maxiansheng.bean.PhoneCodeBean;
import com.guo.qlzx.maxiansheng.bean.SetSafetyBean;
import com.guo.qlzx.maxiansheng.http.RemoteApi;
import com.guo.qlzx.maxiansheng.util.ToolUtil;
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
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 李 on 2018/4/12.
 * 设置-账户-更换手机号码
 */

public class SetSafetyInfoActivity extends BaseActivity {
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.et_enter)
    EditText etEnter;
    @BindView(R.id.btn_get)
    Button btnGet;
    @BindView(R.id.btn_submit)
    Button btnSubmit;

    private String phoneNumber="";


    private String token="";
    private PreferencesHelper helper;
    private int type=0;
    @Override
    public int getContentView() {
        return R.layout.activity_safety_info;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setLeftImageRes(R.drawable.ic_back_gray);
        titleBar.setTitleText("手机号验证");

        etEnter.addTextChangedListener(watcher);
        helper=new PreferencesHelper(this);
        token=new PreferencesHelper(this).getToken();
        type=getIntent().getIntExtra("CODETYPE",0);
    }

    @Override
    public void getData() {

        HttpHelp.getInstance().create(RemoteApi.class).getUserPhone(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<SetSafetyBean>>(this, null) {
                    @Override
                    public void onNext(BaseBean<SetSafetyBean> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code==0){
                            if (baseBean.data!=null){
                                tvPhone.setText(ToolUtil.setTingPhone(baseBean.data.getMobile()));
                                phoneNumber=baseBean.data.getMobile();
                            }
                        }else if (baseBean.code==4){
                            ToolUtil.goToLogin(SetSafetyInfoActivity.this);
                        }else {
                           ToastUtil.showToast(SetSafetyInfoActivity.this,baseBean.message);
                        }
                    }
                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
    }

    private void getPhoneCode( String token) {
        HttpHelp.getInstance().create(RemoteApi.class).getPhoneCode(token,"2")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<PhoneCodeBean>>(this, null) {
                    @Override
                    public void onNext(BaseBean<PhoneCodeBean> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                                ToastUtil.showToast(SetSafetyInfoActivity.this,baseBean.message);
                        } else {
                            ToastUtil.showToast(SetSafetyInfoActivity.this,baseBean.message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
    }

    private void verifyPhone(String mobile,String code,String token){

        HttpHelp.getInstance().create(RemoteApi.class).verifyPhone(token,code, mobile)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean>(this, null) {
                    @Override
                    public void onNext(BaseBean baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            if (type==2){
                                Intent intent = new Intent(SetSafetyInfoActivity.this, ALiPayActivity.class);
                                startActivity(intent);
                                finish();
                            }else if (type==3){
                                Intent intent = new Intent(SetSafetyInfoActivity.this, ALiPayActivity.class);
                                intent.putExtra("ISTYPE",3);
                                startActivity(intent);
                                finish();
                            }
                            else {
                                Intent intent=new Intent(SetSafetyInfoActivity.this,VerifyPhoneActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }else {
                            ToastUtil.showToast(SetSafetyInfoActivity.this,baseBean.message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });

    }

    private Timer timer;
    private TextWatcher watcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (!TextUtils.isEmpty(s.toString())){
                btnSubmit.setEnabled(true);
            }else {
                btnSubmit.setEnabled(false);
            }
        }
    };
    @OnClick({R.id.btn_submit,R.id.btn_get})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.btn_submit:
                if (phoneNumber==null||"".equals(phoneNumber)){
                    ToastUtil.showToast(SetSafetyInfoActivity.this,"获取手机号码出错！");
                    return;
                }
                verifyPhone(phoneNumber,etEnter.getText().toString(),helper.getToken());
                break;
            case  R.id.btn_get:
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
                break;
        }
    }


    //跳转回来后 重新清除正在运行的timer
    @Override
    protected void onStop() {
        super.onStop();
        if (timer!=null){
            btnGet.setEnabled(true);
            btnGet.setTextColor(getResources().getColor(R.color.blue_15));
            btnGet.setText("获取验证码");
            timer.cancel();
            etEnter.setText("");
        }
    }
}
