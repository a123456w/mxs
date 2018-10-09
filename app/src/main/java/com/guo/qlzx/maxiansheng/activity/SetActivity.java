package com.guo.qlzx.maxiansheng.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.guo.qlzx.maxiansheng.R;
import com.guo.qlzx.maxiansheng.event.ChangeHeaderEvent;
import com.guo.qlzx.maxiansheng.event.LoginEvent;
import com.guo.qlzx.maxiansheng.event.OrderNumberEvent;
import com.guo.qlzx.maxiansheng.http.RemoteApi;
import com.guo.qlzx.maxiansheng.util.CleanMessageUtil;
import com.guo.qlzx.maxiansheng.util.ToolUtil;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.EventBusUtil;
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
 * 设置
 */

public class SetActivity extends BaseActivity {
    @BindView(R.id.ll_user)
    LinearLayout llUser;
    @BindView(R.id.ll_safety)
    LinearLayout llSafety;
    @BindView(R.id.cb_down)
    CheckBox cbDown;
    @BindView(R.id.ll_down)
    LinearLayout llDown;
    @BindView(R.id.ll_about)
    LinearLayout llAbout;
    @BindView(R.id.tv_go_out)
    TextView tvGoOut;
    @BindView(R.id.tv_cache)
    TextView tvCache;
    private PreferencesHelper helper;

    @Override
    public int getContentView() {
        return R.layout.activity_set;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setLeftImageRes(R.drawable.ic_back_gray);
        titleBar.setTitleText("设置");

        helper = new PreferencesHelper(this);
        cbDown.setChecked(helper.getIsDownload());
        initCheck();
    }

    private void initCheck() {
        cbDown.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                cbDown.setChecked(isChecked);
                helper.saveIsDownload(isChecked);
            }
        });
        try {
            tvCache.setText(CleanMessageUtil.getTotalCacheSize((Context) this));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getData() {


    }

    @OnClick({R.id.ll_user, R.id.ll_safety, R.id.ll_down, R.id.ll_about, R.id.tv_go_out, R.id.ll_cache})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.ll_user:
                //个人信息
                if (TextUtils.isEmpty(new PreferencesHelper(this).getToken())) {
                    ToolUtil.goToLogin(SetActivity.this);
                    return;
                }
                intent = new Intent(SetActivity.this, SetPersonalDetailsActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_safety:
                //账户安全
                if (TextUtils.isEmpty(new PreferencesHelper(this).getToken())) {
                    ToolUtil.goToLogin(SetActivity.this);
                    return;
                }
                intent = new Intent(SetActivity.this, SetSafetyActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_down:

                //下载
                break;
            case R.id.ll_about:
                if (TextUtils.isEmpty(new PreferencesHelper(this).getToken())) {
                    ToolUtil.goToLogin(SetActivity.this);
                    return;
                }
                //关于我们
                intent = new Intent(SetActivity.this, AboutActivity.class);
                startActivity(intent);
                break;

            case R.id.tv_go_out:
                //退出
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setMessage("确认退出登录?");
                dialog.setTitle("提示");
                dialog.setNegativeButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        logout();
                    }
                });
                dialog.setPositiveButton("取消", null);
                dialog.create().show();
                break;
            case R.id.ll_cache:
                //退出
                dialog = new AlertDialog.Builder(this);
                dialog.setMessage("是否清除缓存?");
                dialog.setTitle("提示");
                dialog.setNegativeButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CleanMessageUtil.clearAllCache(SetActivity
                                .this);
                        try {
                            tvCache.setText(CleanMessageUtil.getTotalCacheSize(SetActivity.this));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                dialog.setPositiveButton("取消", null);
                dialog.create().show();
                break;
        }
    }

    private void logout() {
        HttpHelp.getInstance().create(RemoteApi.class).logout(helper.getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<Object>>(SetActivity.this, null) {
                    @Override
                    public void onNext(BaseBean<Object> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            helper.clear();
                            finish();
                            EventBusUtil.post(new LoginEvent(false));
                            EventBusUtil.post(new ChangeHeaderEvent());
                            EventBusUtil.post(new OrderNumberEvent());
                            helper.setVip(0);
                        } else if (baseBean.code == 4) {
                            ToolUtil.goToLogin(SetActivity.this);
                        } else {
                            ToastUtil.showToast(SetActivity.this, baseBean.message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);

                    }
                });
    }

}
