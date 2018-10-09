package com.guo.qlzx.maxiansheng.activity;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.guo.qlzx.maxiansheng.R;
import com.guo.qlzx.maxiansheng.bean.CheckVersionBean;
import com.guo.qlzx.maxiansheng.event.ChangeFragmentEvent;
import com.guo.qlzx.maxiansheng.event.ChangeHeaderEvent;
import com.guo.qlzx.maxiansheng.event.ShoppingCartNumEvent;
import com.guo.qlzx.maxiansheng.fragment.ClassifyFragment;
import com.guo.qlzx.maxiansheng.fragment.HomePageFragment;
import com.guo.qlzx.maxiansheng.fragment.MineFragment;
import com.guo.qlzx.maxiansheng.fragment.ShoppingCarFragment;
import com.guo.qlzx.maxiansheng.http.RemoteApi;
import com.guo.qlzx.maxiansheng.util.DownloadManagerUtil;
import com.guo.qlzx.maxiansheng.util.ToolUtil;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.EventBusUtil;
import com.qlzx.mylibrary.util.LogUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.StringUtil;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.yanzhenjie.alertdialog.AlertDialog;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {

    @BindView(R.id.rg_bottom_nav)
    RadioGroup rgBottomNav;
    @BindView(R.id.rb_shoppingcar)
    RadioButton rbShoppingCar;
    @BindView(R.id.tv_number)
    public TextView tvNumber;


    private FragmentManager fragmentManager;
    private Fragment f1, f2, f3, f4;
    private double exitTime;
    private PreferencesHelper helper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            fragmentManager = getSupportFragmentManager();
            f1 = (HomePageFragment) fragmentManager.findFragmentByTag("home");
            f2 = (ClassifyFragment) fragmentManager.findFragmentByTag("category");
            f3 = (ShoppingCarFragment) fragmentManager.findFragmentByTag("Shop");
            f4 = (MineFragment) fragmentManager.findFragmentByTag("mine");
        }
        super.onCreate(savedInstanceState);
        helper = new PreferencesHelper(this);
        if (helper.getIsDownload()) {
            initDown();
        }
        shoppingCartCount(helper.getToken());
        EventBusUtil.register(this);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        hideTitleBar();
        rgBottomNav.setOnCheckedChangeListener(this);
        if (fragmentManager == null) {
            fragmentManager = getSupportFragmentManager();
        }
        if (f1 == null) {
            f1 = new HomePageFragment();//首页
            fragmentManager.beginTransaction().add(R.id.main_content, f1, "home").commit();
        } else {
            hideAllFragment(fragmentManager.beginTransaction());
            fragmentManager.beginTransaction().show(f1);
        }


        requestPermission();
    }

    @Override
    public void getData() {

    }



    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFragmentChangedEvent(ChangeFragmentEvent event) {
        rgBottomNav.check(R.id.rb_home);
        hideAllFragment(fragmentManager.beginTransaction());
        fragmentManager.beginTransaction().show(f1);
    }
    /**
     * 隐藏所有Fragment
     *
     * @param transaction
     */
    public void hideAllFragment(FragmentTransaction transaction) {
        if (f1 != null) {
            transaction.hide(f1);
        }
        if (f2 != null) {
            transaction.hide(f2);
        }
        if (f3 != null) {
            transaction.hide(f3);
        }
        if (f4 != null) {
            transaction.hide(f4);
        }
    }


    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        hideAllFragment(fragmentTransaction);
        switch (checkedId) {
            //首页
            case R.id.rb_home:
                fragmentTransaction.show(f1);
                break;
            //分类
            case R.id.rb_classify:
                if (f2 == null) {
                    f2 = new ClassifyFragment();
                    fragmentTransaction.add(R.id.main_content, f2, "category");
                } else {
                    fragmentTransaction.show(f2);
                }

                break;
            //购物车
            case R.id.rb_shoppingcar:
                if (TextUtils.isEmpty(new PreferencesHelper(MainActivity.this).getToken())){
                    ToolUtil.goToLogin(MainActivity.this);
                    rgBottomNav.check(R.id.rb_home);
                    return;
                }
                if (f3 == null) {
                    f3 = new ShoppingCarFragment();
                    fragmentTransaction.add(R.id.main_content, f3, "Shop");
                } else {
                    fragmentTransaction.show(f3);
                }

                break;
            //我的
            case R.id.rb_me:
                if (f4 == null) {
                    f4 = new MineFragment();
                    fragmentTransaction.add(R.id.main_content, f4, "mine");
                } else {
                    fragmentTransaction.show(f4);
                }

                break;
        }
        fragmentTransaction.commit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        if (f1 == null && fragment instanceof HomePageFragment)
            f1 = fragment;
        if (f2 == null && fragment instanceof ClassifyFragment)
            f2 = fragment;
        if (f3 == null && fragment instanceof ShoppingCarFragment)
            f3 = fragment;
        if (f4 == null && fragment instanceof MineFragment)
            f4 = fragment;
    }


    @Override
    public void onReload(View v) {
        getData();
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (1 == intent.getIntExtra("go_cart", 0)) {
            rgBottomNav.check(R.id.rb_shoppingcar);
        }
        if (2 == intent.getIntExtra("go_home", 0)) {
            rgBottomNav.check(R.id.rb_home);
        }
    }


    /**
     * Rationale支持，这里自定义对话框。
     */
    private RationaleListener rationaleListener = new RationaleListener() {
        @Override
        public void showRequestPermissionRationale(int requestCode, final Rationale rationale) {
            AlertDialog.newBuilder(MainActivity.this)
                    .setTitle("友好提醒")
                    .setMessage("为保证APP的正常运行，需要以下权限:\n1.访问SD卡（选择图片等功能）\n2.调用摄像头（扫码下单等功能）")
                    .setPositiveButton("好，给你", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            rationale.resume();
                        }
                    })
                    .setNegativeButton("依然拒绝", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            rationale.cancel();
                        }
                    }).show();
        }
    };

    private void requestPermission() {
        // 申请多个权限。
        AndPermission.with(this)
                .requestCode(REQUIRES_PERMISSION)
                .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.ACCESS_COARSE_LOCATION)
                .callback(permissionListener)
                // rationale作用是：用户拒绝一次权限，再次申请时先征求用户同意，再打开授权对话框；
                // 这样避免用户勾选不再提示，导致以后无法申请权限。
                // 你也可以不设置。
                .rationale(rationaleListener)
                .start();
    }

    private final int REQUIRES_PERMISSION = 0;
    private static final int REQUEST_CODE_SETTING = 300;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBusUtil.unregister(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        shoppingCartCount(helper.getToken());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void addNumber(ShoppingCartNumEvent event) {
        shoppingCartCount(helper.getToken());
    }

    //购物车数量
    private void shoppingCartCount(String token){
        HttpHelp.getInstance().create(RemoteApi.class).shoppingCartCount(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<String>>(this, null) {
                    @Override
                    public void onNext(BaseBean<String> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            if (StringUtil.isNumeric(baseBean.data)&&baseBean.data!=null&& !TextUtils.isEmpty(baseBean.data)){
                                tvNumber.setVisibility(View.VISIBLE);

                                if (Integer.valueOf(baseBean.data)<100&&Integer.valueOf(baseBean.data)>0){
                                    tvNumber.setText(baseBean.data);
                                }else if (Integer.valueOf(baseBean.data)==0){
                                    tvNumber.setVisibility(View.GONE);
                                }else {
                                    tvNumber.setText("99+");
                                }
                            }else {
                                tvNumber.setVisibility(View.GONE);
                            }
                        } else {
                            //ToastUtil.showToast(MainActivity.this, baseBean.message);
                            tvNumber.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        tvNumber.setVisibility(View.GONE);
                    }
                });
    }

    /**
     * 回调监听。
     */
    private PermissionListener permissionListener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, List<String> grantPermissions) {
            LogUtil.d("权限申请成功  onSucceed");
            switch (requestCode) {

                case REQUIRES_PERMISSION: {

                }
            }
        }


        @Override
        public void onFailed(int requestCode, List<String> deniedPermissions) {
            LogUtil.d("权限申请成功  onFailed");
            switch (requestCode) {

                case REQUIRES_PERMISSION: {
                    // 用户否勾选了不再提示并且拒绝了权限，那么提示用户到设置中授权。
                    if (AndPermission.hasAlwaysDeniedPermission(MainActivity.this, deniedPermissions)) {
                        // 第一种：用默认的提示语。
//                AndPermission.defaultSettingDialog(ListenerActivity.this, REQUEST_CODE_SETTING).show();

                        // 第二种：用自定义的提示语。
                        AndPermission.defaultSettingDialog(MainActivity.this, REQUEST_CODE_SETTING)
                                .setTitle("权限申请失败")
                                .setMessage("我们需要的一些权限被您拒绝或者系统发生错误申请失败，请您到设置页面手动授权，否则功能无法正常使用！")
                                .setPositiveButton("好，去设置")
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ToastUtil.showToast(MainActivity.this, "权限申请失败，无法进入app");
                                        finish();
                                    }
                                })
                                .show();

//            第三种：自定义dialog样式。
//            SettingService settingService = AndPermission.defineSettingDialog(this, REQUEST_CODE_SETTING);
//            你的dialog点击了确定调用：
//            settingService.execute();
//            你的dialog点击了取消调用：
//            settingService.cancel();
                    } else {
                        ToastUtil.showToast(MainActivity.this, "权限申请失败，无法进入app");
                        finish();
                    }

                    break;
                }
            }


        }
    };
    DownloadManagerUtil downloadManagerUtil;
    private void initDown() {
        downloadManagerUtil=new DownloadManagerUtil(MainActivity.this);
        checkVersion(getVersion());
    }
    /**
     * 检查更新
     *
     * @param version
     */
    private void checkVersion(String version) {
        HttpHelp.getInstance().create(RemoteApi.class).checkVersion(1, version)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<CheckVersionBean>>(MainActivity.this, null) {
                    @Override
                    public void onNext(BaseBean<CheckVersionBean> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            if ("1".equals(baseBean.data.getA_status())) {
                                LogUtil.d("有新版本---" + baseBean.data.getA_version());
                                if (!helper.getIgnoreVersion().equals(baseBean.data.getA_version())){
                                    downloadManagerUtil.download(baseBean.data.getA_url(),baseBean.data.getA_update_log(),"");
                                }
                            } else {
                                //ToastUtil.showToast(MainActivity.this, baseBean.message);
                            }
                        } else {
                            //ToastUtil.showToast(MainActivity.this, baseBean.message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
    }






    /**
     * 2  * 获取版本号
     * 3  * @return 当前应用的版本号
     * 4
     */
    public String getVersion() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            String version = info.versionName;
            return  version;
        } catch (Exception e) {
            e.printStackTrace();
            return this.getString(R.string.can_not_find_version_name);
        }
    }
}
