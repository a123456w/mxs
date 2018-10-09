package com.guo.qlzx.maxiansheng.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.guo.qlzx.maxiansheng.R;
import com.guo.qlzx.maxiansheng.bean.SetPersonalBean;
import com.guo.qlzx.maxiansheng.event.ChangeHeaderEvent;
import com.guo.qlzx.maxiansheng.http.RemoteApi;
import com.guo.qlzx.maxiansheng.util.ToolUtil;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.EventBusUtil;
import com.qlzx.mylibrary.util.GlideUtil;
import com.qlzx.mylibrary.util.LogUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.TakePhotoUtils;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.yanzhenjie.alertdialog.AlertDialog;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerActivity;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 李 on 2018/4/12.
 * 设置-个人信息 全部完成
 */

public class SetPersonalDetailsActivity extends BaseActivity {
    @BindView(R.id.tiv_head)
    ImageView tivHead;
    @BindView(R.id.ll_head)
    LinearLayout llHead;
    @BindView(R.id.tv_class)
    TextView tvClass;
    @BindView(R.id.ll_vip)
    LinearLayout llVip;
    @BindView(R.id.ll_address)
    LinearLayout llAddress;
//    @BindView(R.id.tiv_head)
//    RoundedImageView tivHead;
//    @BindView(R.id.ll_head)
//    LinearLayout llHead;
//    @BindView(R.id.tv_class)
//    TextView tvClass;
//    @BindView(R.id.ll_address)
//    LinearLayout llAddress;

    private Dialog mDialog;
    private File takePhotoFile;
    private String filePath;
    private final int REQUEST_CODE_TAKE_PHOTO = 100;
    private final int REQUEST_CODE_CHOICE_PHOTO = 200;
    private final int REQUIRES_PERMISSION = 0;//申请权限
    private static final int REQUEST_CODE_SETTING = 300;

    //请求相机
    private static final int REQUEST_CAPTURE = 100;
    //请求相册
    private static final int REQUEST_PICK = 200;


    //请求截图
    private static final int REQUEST_CROP_PHOTO = 102;
    //请求访问外部存储
    private static final int READ_EXTERNAL_STORAGE_REQUEST_CODE = 103;
    //请求写入外部存储
    private static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 104;

    private PreferencesHelper helper;
    private SetPersonalBean personalBean;

    @Override
    public int getContentView() {
        return R.layout.activity_set_personal;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setLeftImageRes(R.drawable.ic_back_gray);
        titleBar.setTitleText("个人信息");
        helper = new PreferencesHelper(this);
    }

    @Override
    public void getData() {
        getUserInfo(helper.getToken());
    }

    //获取用户信息
    private void getUserInfo(String token) {
        HttpHelp.getInstance().create(RemoteApi.class).getUserInfo(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<SetPersonalBean>>(this, null) {
                    @Override
                    public void onNext(BaseBean<SetPersonalBean> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            personalBean = baseBean.data;
                            GlideUtil.displayAvatar(SetPersonalDetailsActivity.this, Constants.IMG_HOST + personalBean.getImg(), tivHead);
                            if ("1".equals(personalBean.getPlus())) {
                                tvClass.setText("Plus会员");
                            } else {
                                tvClass.setText("普通会员");
                            }
                        } else if (baseBean.code == 4) {
                            ToolUtil.goToLogin(SetPersonalDetailsActivity.this);
                            tvClass.setText("");
                        } else {
                            ToastUtil.showToast(SetPersonalDetailsActivity.this, baseBean.message);
                            tvClass.setText("");
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
    }

    /**
     * 获取用户信息
     *
     * @param token 用户token
     */
    public void getUserBean(String token) {

    }

    @OnClick({R.id.ll_address, R.id.tiv_head, R.id.ll_vip})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.ll_address:
                //收货地址管理.
                if (TextUtils.isEmpty(new PreferencesHelper(this).getToken())) {
                    ToolUtil.goToLogin(SetPersonalDetailsActivity.this);
                    return;
                }
                intent = new Intent(SetPersonalDetailsActivity.this, AddressManagementActivity.class);
                startActivity(intent);
                break;
            case R.id.tiv_head:
                //修改头像
                if (TextUtils.isEmpty(new PreferencesHelper(this).getToken())) {
                    ToolUtil.goToLogin(SetPersonalDetailsActivity.this);
                    return;
                }
                DialogPic();
                break;
            case R.id.ll_vip:
                if (TextUtils.isEmpty(new PreferencesHelper(this).getToken())) {
                    ToolUtil.goToLogin(SetPersonalDetailsActivity.this);
                    return;
                }
                intent = new Intent(SetPersonalDetailsActivity.this, MemberCenterActivity.class);
                startActivity(intent);
                break;
        }
    }


    /**
     * 弹框
     */
    public void DialogPic() {
        if (!AndPermission.hasPermission(SetPersonalDetailsActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)) {
            requestPermission();
            return;
        }


        mDialog = new Dialog(SetPersonalDetailsActivity.this, R.style.ActionSheetDialogStyle);
        View inflate = LayoutInflater.from(SetPersonalDetailsActivity.this).inflate(R.layout.dialog_pic_gain, null);

        mDialog.setContentView(inflate);
        Window dialogwindow = mDialog.getWindow();

        dialogwindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogwindow.getAttributes();
        lp.windowAnimations = R.style.PopUpBottomAnimation;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;

        dialogwindow.setAttributes(lp);
        mDialog.show();

        TextView tv_close, pat_pic, photo_select;
        tv_close = inflate.findViewById(R.id.tv_close);
        photo_select = inflate.findViewById(R.id.photo_select);
        pat_pic = inflate.findViewById(R.id.pat_pic);
        tv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
            }
        });

        pat_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//照片
                mDialog.dismiss();

                gotoCarema();
                /*try {
                    takePhotoFile = TakePhotoUtils.takePhoto((Activity) SetPersonalDetailsActivity.this, "maxianshengPhotos", REQUEST_CODE_TAKE_PHOTO);
                } catch (IOException e) {
                    ToastUtil.showToast(SetPersonalDetailsActivity.this, "拍照失败");
                    e.printStackTrace();
                }*/
            }
        });
        photo_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//相册
                mDialog.dismiss();
                //从相册选择
                startActivityForResult(BGAPhotoPickerActivity.newIntent(SetPersonalDetailsActivity.this, null, 1, null, false), REQUEST_CODE_CHOICE_PHOTO);
            }
        });

    }

    /**
     * 跳转到照相机
     */
    private void gotoCarema() {
        try {
            takePhotoFile = TakePhotoUtils.takePhoto(SetPersonalDetailsActivity.this, "tempFile", REQUEST_CAPTURE);
        } catch (IOException e) {
            ToastUtil.showToast(SetPersonalDetailsActivity.this, "拍照失败");
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String path;
//        mBaseDialog.dismiss();

        //拍照回调
     /*   if (requestCode == REQUEST_CODE_TAKE_PHOTO && resultCode == RESULT_OK) {
            if (takePhotoFile != null) {
                filePath = takePhotoFile.getAbsolutePath();
                changIcon(new File(filePath));
            }
        }*/
        if (requestCode==REQUEST_CAPTURE&&resultCode == RESULT_OK) {
            gotoClipActivity(Uri.fromFile(takePhotoFile));
        }
        if (requestCode==REQUEST_CROP_PHOTO&&resultCode == RESULT_OK) {
            final Uri uri = data.getData();
            if (uri == null) {
                return;
            }
            String cropImagePath = getRealFilePathFromUri(getApplicationContext(), uri);
            Bitmap bitMap = BitmapFactory.decodeFile(cropImagePath);
            changIcon(new File(cropImagePath));
        }
        //选择图片回调
        if (requestCode == REQUEST_CODE_CHOICE_PHOTO && resultCode == RESULT_OK) {
            try {

                List<String> listImg = BGAPhotoPickerActivity.getSelectedImages(data);
                if (listImg.size() > 0) {
                    filePath = listImg.get(0);
                    changIcon(new File(filePath));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * 打开截图界面
     *
     * @param uri
     */
    public void gotoClipActivity(Uri uri) {
        if (uri == null) {
            return;
        }
        Intent intent = new Intent();
        intent.setClass(this, ClipImageActivity.class);
        intent.setData(uri);
        startActivityForResult(intent, REQUEST_CROP_PHOTO);
    }
    /**
     * 根据Uri返回文件绝对路径
     * 兼容了file:///开头的 和 content://开头的情况
     *
     * @param context
     * @param uri
     * @return the file path or null
     */
    public static String getRealFilePathFromUri(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }
    private void changIcon(File file) {
        showLoadingDialog("上传中...");
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        builder.addFormDataPart("token", new PreferencesHelper(SetPersonalDetailsActivity.this).getToken())
                .addFormDataPart("type", "android");
        RequestBody IDFrontBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        builder.addFormDataPart("img", file.getName(), IDFrontBody);

        HttpHelp.getInstance().create(RemoteApi.class).changeHead(builder.build())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<List>>(SetPersonalDetailsActivity.this, null) {
                    @Override
                    public void onNext(BaseBean<List> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            ToastUtil.showToast(SetPersonalDetailsActivity.this, baseBean.message);
                            //GlideUtil.display(SetPersonalDetailsActivity.this, filePath, tivHead);
                            getUserInfo(helper.getToken());
                            EventBusUtil.post(new ChangeHeaderEvent());
                        } else if (baseBean.code == 4) {
                            ToolUtil.goToLogin(SetPersonalDetailsActivity.this);
                        } else {
                            ToastUtil.showToast(SetPersonalDetailsActivity.this, baseBean.message);
                            getUserInfo(helper.getToken());
                        }
                        hideLoadingDialog();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        ToastUtil.showToast(SetPersonalDetailsActivity.this, "网络连接失败");
                        hideLoadingDialog();
                    }
                });
    }


    private void requestPermission() {
        // 申请多个权限。
        AndPermission.with(this)
                .requestCode(REQUIRES_PERMISSION)
                .permission(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .callback(permissionListener)
                // rationale作用是：用户拒绝一次权限，再次申请时先征求用户同意，再打开授权对话框；
                // 这样避免用户勾选不再提示，导致以后无法申请权限。
                // 你也可以不设置。
                .rationale(rationaleListener)
                .start();
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
                    ToastUtil.showToast(SetPersonalDetailsActivity.this, "权限申请成功");
                    break;
                }
            }
        }


        @Override
        public void onFailed(int requestCode, List<String> deniedPermissions) {
            LogUtil.d("权限申请成功  onFailed");
            switch (requestCode) {

                case REQUIRES_PERMISSION: {
                    // 用户否勾选了不再提示并且拒绝了权限，那么提示用户到设置中授权。
                    if (AndPermission.hasAlwaysDeniedPermission(SetPersonalDetailsActivity.this, deniedPermissions)) {
                        //用自定义的提示语。
                        AndPermission.defaultSettingDialog(SetPersonalDetailsActivity.this, REQUEST_CODE_SETTING)
                                .setTitle("权限申请失败")
                                .setMessage("我们需要的一些权限被您拒绝或者系统发生错误申请失败，请您到设置页面手动授权，否则功能无法正常使用！")
                                .setPositiveButton("好，去设置")
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ToastUtil.showToast(SetPersonalDetailsActivity.this, "权限申请失败");
                                        finish();
                                    }
                                })
                                .show();
                    } else {
                        ToastUtil.showToast(SetPersonalDetailsActivity.this, "权限申请失败，无法评论");
                        finish();
                    }

                    break;
                }
            }


        }
    };

    /**
     * Rationale支持，这里自定义对话框。
     */
    private RationaleListener rationaleListener = new RationaleListener() {
        @Override
        public void showRequestPermissionRationale(int requestCode, final Rationale rationale) {
            AlertDialog.newBuilder(SetPersonalDetailsActivity.this)
                    .setTitle("友好提醒")
                    .setMessage("为保证APP的正常运行，需要以下权限:\n1.访问SD卡（选择图片等功能）\n2.调用摄像头（拍照功能）")
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
