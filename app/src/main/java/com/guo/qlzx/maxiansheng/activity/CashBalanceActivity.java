package com.guo.qlzx.maxiansheng.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.guo.qlzx.maxiansheng.R;
import com.guo.qlzx.maxiansheng.bean.IsPasswordExitsBean;
import com.guo.qlzx.maxiansheng.bean.TotalSumBean;
import com.guo.qlzx.maxiansheng.http.RemoteApi;
import com.guo.qlzx.maxiansheng.util.ToolUtil;
import com.guo.qlzx.maxiansheng.util.costom.AmountEditText;
import com.guo.qlzx.maxiansheng.util.dialog.AddressDeleteDialog;
import com.guo.qlzx.maxiansheng.util.dialog.LoadDialog;
import com.guo.qlzx.maxiansheng.util.dialog.PayPasswordDialog;
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
 * Created by 李 on 2018/4/18.
 * 余额提现界面
 */

public class CashBalanceActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.et_memory)
    AmountEditText etMemory;
    @BindView(R.id.tv_all)
    TextView tvAll;
    @BindView(R.id.tv_btn)
    Button tvBtn;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    private int type = 1;//1微信支付 2支付宝支付
    private PreferencesHelper helper;
    private String mPrice="0.00";
    private String mMemory="";
    private LoadDialog loadDialog;

    @Override
    public int getContentView() {
        return R.layout.activity_cash_balance;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setLeftImageRes(R.drawable.ic_back_gray);
        titleBar.setTitleText("余额提现");
        helper = new PreferencesHelper(this);
        type = getIntent().getIntExtra("PAYWAY", 0);
        loadDialog=new LoadDialog(CashBalanceActivity.this);
        initEvent();
    }

    private void initEvent() {
        etMemory.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString())){
                    tvBtn.setEnabled(true);
                }else {
                    tvBtn.setEnabled(false);
                }
            }
        });
        if (type==1){
            tvTitle.setText("微信钱包");
        }else {
            tvTitle.setText("支付宝钱包");
        }
    }

    @Override
    public void getData() {
        getCashBalanceData(helper.getToken());
    }

    //显示我的余额
    private void getCashBalanceData(String token){
        HttpHelp.getInstance().create(RemoteApi.class).getBalance(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<TotalSumBean>>(this, null) {
                    @Override
                    public void onNext(BaseBean<TotalSumBean> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code==0) {
                            String price=baseBean.data.getUser_money();
                            if (price==null&&"".equals(price)){
                                tvPrice.setText("0.00");
                            }else {
                                tvPrice.setText(price);
                                mPrice=price;
                            }
                        }else if (baseBean.code==4){
                            ToolUtil.goToLogin(CashBalanceActivity.this);
                        }
                    }
                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
    }
    //发起提现
    private void getCashBalance(String token,String memory,int type,String mPassword){
        HttpHelp.getInstance().create(RemoteApi.class).getCashBalance(token,memory,type,mPassword)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean>(this, null) {
                    @Override
                    public void onNext(BaseBean baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code==0) {
                            loadDialog.cancel();
                            Intent intent=new Intent(CashBalanceActivity.this,CashBalanceResultActivity.class);
                            startActivityForResult(intent,100);
                            passwordDialog.dismiss();
                        }else if (baseBean.code==4){
                            ToolUtil.goToLogin(CashBalanceActivity.this);
                        }else {
                            loadDialog.cancel();
                            passwordDialog.dismiss();
                            ToastUtil.showToast(CashBalanceActivity.this,baseBean.message);
                        }
                    }
                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
    }

    //是否未设置支付密码
    private void isPasswordExits(String token) {
        HttpHelp.getInstance().create(RemoteApi.class).isPasswordExits(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<IsPasswordExitsBean>>(this, null) {
                    @Override
                    public void onNext(BaseBean<IsPasswordExitsBean> baseBean) {
                        super.onNext(baseBean);
                        tvBtn.setClickable(true);
                        if (baseBean.code == 0) {
                            if (baseBean.data.getPaypwd_status()==1){
                                //已设置
                                showEditPassword();
                            }else if (baseBean.data.getPaypwd_status()==0){
                                //未设置
                                final AddressDeleteDialog dialog=new AddressDeleteDialog(CashBalanceActivity.this);
                                dialog.setGoToUseOnclickListener(new AddressDeleteDialog.onGoToUseOnclickListener() {
                                    @Override
                                    public void onUseClick() {
                                        startActivity(new Intent(CashBalanceActivity.this,SetPayPasswordActivity.class));
                                    }
                                });
                               dialog.show();
                               dialog.setTitle("尚未设置交易密码,是否前往设置?");
                            }
                        }else if (baseBean.code==4){
                            ToolUtil.goToLogin(CashBalanceActivity.this);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        tvBtn.setClickable(true);
                        super.onError(throwable);
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 200){
            setResult(200);
            finish();
        }
    }

    @OnClick({R.id.tv_all, R.id.tv_btn})
    public void onViewClicked(View view) {

        switch (view.getId()){
            case R.id.tv_all:
                String price=tvPrice.getText().toString();
                etMemory.setText("");
                etMemory.setText(price);
                break;
            case R.id.tv_btn:

                mMemory=etMemory.getText().toString();
//                if ("".equals(mPrice)){
//                    return;
//                }
                if ("0.00".equals(mPrice)){
                    ToastUtil.showToast(CashBalanceActivity.this,"无可提现金额");
                    return;
                }
                float et=Float.valueOf(mMemory);
                float use=Float.valueOf(mPrice);
                if ((et-use)>0){
                    ToastUtil.showToast(CashBalanceActivity.this,"提现金额超限");
                    return;
                }
                view.setClickable(false);
                isPasswordExits(helper.getToken());
                break;
        }
    }
    private PayPasswordDialog passwordDialog;
    //弹出密码/输入密码/完毕后的回调
    private void showEditPassword(){
        passwordDialog= PayPasswordDialog.newInstace(this);
        passwordDialog.setOnFinishClickListener(new PayPasswordDialog.OnFinishClickListener() {
            @Override
            public void onFinish(String psw) {
                loadDialog.show();
                getCashBalance(helper.getToken(),mMemory,type,psw);
            }
        });
        passwordDialog.show(getSupportFragmentManager(),"PassWordFragment");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getCashBalanceData(helper.getToken());
    }
}
