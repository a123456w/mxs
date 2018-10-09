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
import com.guo.qlzx.maxiansheng.bean.IsAlipayBean;
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
 * Created by 李 on 2018/5/22.
 */

public class ALiPayActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_accounts)
    EditText etAccounts;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_accounts)
    TextView tvAccounts;
    private PreferencesHelper helper;

    private int type=0;
    private int isType=0;
    @Override
    public int getContentView() {
        return R.layout.activity_alipay;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("绑定支付宝账号");
        helper = new PreferencesHelper(this);
        isType=getIntent().getIntExtra("ISTYPE",0);
        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString()) && !TextUtils.isEmpty(etAccounts.getText().toString())) {
                    btnSubmit.setEnabled(true);
                } else {
                    btnSubmit.setEnabled(false);
                }
            }
        });
        etAccounts.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString()) && !TextUtils.isEmpty(etName.getText().toString())) {
                    btnSubmit.setEnabled(true);
                } else {
                    btnSubmit.setEnabled(false);
                }
            }
        });
    }

    @Override
    public void getData() {
        if (isType==0){
            isAlipay(helper.getToken());
        }
    }

    private void isAlipay(String token) {
        HttpHelp.getInstance().create(RemoteApi.class).isAlipay(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<IsAlipayBean>>(this, null) {
                    @Override
                    public void onNext(BaseBean<IsAlipayBean> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            type=baseBean.data.getAlipay_status();
                            if (type==1){
                                etName.setVisibility(View.GONE);
                                etAccounts.setVisibility(View.GONE);
                                tvName.setVisibility(View.VISIBLE);
                                tvAccounts.setVisibility(View.VISIBLE);
                                tvName.setText(baseBean.data.getRealname());
                                tvAccounts.setText(baseBean.data.getBank_card());
                                btnSubmit.setText("更改绑定");
                                btnSubmit.setEnabled(true);
                            }else {
                                etName.setVisibility(View.VISIBLE);
                                etAccounts.setVisibility(View.VISIBLE);
                                tvName.setVisibility(View.GONE);
                                tvAccounts.setVisibility(View.GONE);
                            }
                        } else if (baseBean.code == 4) {
                            ToolUtil.goToLogin(ALiPayActivity.this);
                        } else {
                            ToastUtil.showToast(ALiPayActivity.this, baseBean.message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
    }

    @OnClick({R.id.btn_submit})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.btn_submit:
                if (0==isType) {
                    if (type==0) {
                    //绑定
                    String name = etName.getText().toString();
                    String accounts = etAccounts.getText().toString();
                    if (TextUtils.isEmpty(name)) {
                        ToastUtil.showToast(ALiPayActivity.this, "真实姓名不可为空");
                        return;
                    }
                    if (TextUtils.isEmpty(accounts)) {
                        ToastUtil.showToast(ALiPayActivity.this, "支付宝帐号不可为空");
                        return;
                    }
                    bindingAlipay(helper.getToken(), accounts, name);
                     }else {

                        //更改绑定-先绑定手机号码
                        intent = new Intent(ALiPayActivity.this, SetSafetyInfoActivity.class);
                        intent.putExtra("CODETYPE", 3);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    //更改绑定
                    String name = etName.getText().toString();
                    String accounts = etAccounts.getText().toString();
                    if (TextUtils.isEmpty(name)) {
                        ToastUtil.showToast(ALiPayActivity.this, "真实姓名不可为空");
                        return;
                    }
                    if (TextUtils.isEmpty(accounts)) {
                        ToastUtil.showToast(ALiPayActivity.this, "支付宝帐号不可为空");
                        return;
                    }
                    changeBindingAlipay(helper.getToken(), accounts, name);
                }
                break;

        }
    }

    private void bindingAlipay(String token, String bank_card, String realName) {
        HttpHelp.getInstance().create(RemoteApi.class).bindingAlipay(token, bank_card, realName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<Object>>(this, null) {
                    @Override
                    public void onNext(BaseBean<Object> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            ToastUtil.showToast(ALiPayActivity.this, baseBean.message);
                            finish();
                        } else if (baseBean.code == 4) {
                            ToolUtil.goToLogin(ALiPayActivity.this);
                        } else {
                            ToastUtil.showToast(ALiPayActivity.this, baseBean.message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        ToastUtil.showToast(ALiPayActivity.this, "网络链接失败");
                    }
                });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        setResult(200);
    }

    private void changeBindingAlipay(String token, String bank_card, String realName) {
        HttpHelp.getInstance().create(RemoteApi.class).changeBindingAlipay(token, bank_card, realName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<Object>>(this, null) {
                    @Override
                    public void onNext(BaseBean<Object> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            ToastUtil.showToast(ALiPayActivity.this, baseBean.message);
                            finish();
                        } else if (baseBean.code == 4) {
                            ToolUtil.goToLogin(ALiPayActivity.this);
                        } else {
                            ToastUtil.showToast(ALiPayActivity.this, baseBean.message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        ToastUtil.showToast(ALiPayActivity.this, "网络连接失败");
                    }
                });
    }
}
