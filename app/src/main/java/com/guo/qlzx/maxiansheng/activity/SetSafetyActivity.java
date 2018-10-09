package com.guo.qlzx.maxiansheng.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.guo.qlzx.maxiansheng.R;
import com.guo.qlzx.maxiansheng.bean.IsAlipayBean;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 李 on 2018/4/12.
 * 设置-账户安全
 */

public class SetSafetyActivity extends BaseActivity {
    @BindView(R.id.tv_safety_info)
    TextView tvSafetyInfo;
    @BindView(R.id.ll_safety_info)
    LinearLayout llSafetyInfo;
    @BindView(R.id.ll_safety_password)
    LinearLayout llSafetyPassword;
    @BindView(R.id.tv_set)
    TextView tvSet;

    private PreferencesHelper helper;
    //修改密码/设置
    private int  type=1;

    @Override
    public int getContentView() {
        return R.layout.activity_set_safety;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setLeftImageRes(R.drawable.ic_back_gray);
        titleBar.setTitleText("账户安全");
        helper=new PreferencesHelper(this);
    }

    @Override
    public void getData() {
        getInfo(helper.getToken());
    }

    private void getInfo(String token){
        HttpHelp.getInstance().create(RemoteApi.class).getUserPhone(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<SetSafetyBean>>(this, null) {
                    @Override
                    public void onNext(BaseBean<SetSafetyBean> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            if (baseBean.data != null) {
                                tvSafetyInfo.setText(ToolUtil.setTingPhone(baseBean.data.getMobile()));
                                type=baseBean.data.getType();
                                if (type==2){
                                    tvSet.setText("设置");
                                }
                            }
                        }else if (baseBean.code==4){
                            ToolUtil.goToLogin(SetSafetyActivity.this);
                        } else {
                            ToastUtil.showToast(SetSafetyActivity.this,baseBean.message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
    }
    private void isAlipay(String token){
        HttpHelp.getInstance().create(RemoteApi.class).isAlipay(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<IsAlipayBean>>(this, null) {
                    @Override
                    public void onNext(BaseBean<IsAlipayBean> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            if (baseBean.data.getAlipay_status() == 1) {
                                Intent intent = new Intent(SetSafetyActivity.this, ALiPayActivity.class);
                                startActivity(intent);
                            }else {
                                Intent intent = new Intent(SetSafetyActivity.this, SetSafetyInfoActivity.class);
                                intent.putExtra("CODETYPE", 2);
                                startActivity(intent);
                            }
                        }else if (baseBean.code==4){
                            ToolUtil.goToLogin(SetSafetyActivity.this);
                        } else {
                            ToastUtil.showToast(SetSafetyActivity.this,baseBean.message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
    }
    @OnClick({R.id.ll_safety_info, R.id.ll_safety_password,R.id.ll_login_password,R.id.ll_alipay})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.ll_safety_info:
                if (TextUtils.isEmpty(new PreferencesHelper(this).getToken())){
                    ToolUtil.goToLogin(SetSafetyActivity.this);
                    return;
                }
                intent = new Intent(SetSafetyActivity.this, SetSafetyInfoActivity.class);
                intent.putExtra("CODETYPE", 4);
                startActivity(intent);
                //手机号
                break;
            case R.id.ll_safety_password:
                if (TextUtils.isEmpty(new PreferencesHelper(this).getToken())){
                    ToolUtil.goToLogin(SetSafetyActivity.this);
                    return;
                }
                intent = new Intent(SetSafetyActivity.this, SetPayPasswordActivity.class);
                intent.putExtra("SETPASSWORD", type);
                startActivity(intent);
                //密码
                break;
            case R.id.ll_login_password:
                //修改登录密码
                if (TextUtils.isEmpty(new PreferencesHelper(this).getToken())){
                    ToolUtil.goToLogin(SetSafetyActivity.this);
                    return;
                }
                intent = new Intent(SetSafetyActivity.this, ChangePasswordActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_alipay:
                //绑定支付宝帐号
                if (TextUtils.isEmpty(new PreferencesHelper(this).getToken())){
                    ToolUtil.goToLogin(SetSafetyActivity.this);
                    return;
                }
                isAlipay(helper.getToken());
                break;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getInfo(helper.getToken());
    }
}
