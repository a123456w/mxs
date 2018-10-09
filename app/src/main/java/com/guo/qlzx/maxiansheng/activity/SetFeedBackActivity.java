package com.guo.qlzx.maxiansheng.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.guo.qlzx.maxiansheng.R;
import com.guo.qlzx.maxiansheng.adapter.CommentPicListAdapter;
import com.guo.qlzx.maxiansheng.adapter.SetFeedbackTypeAdapter;
import com.guo.qlzx.maxiansheng.bean.ComplainPhoneBean;
import com.guo.qlzx.maxiansheng.bean.SetFeedbackTypeListBean;
import com.guo.qlzx.maxiansheng.http.RemoteApi;
import com.guo.qlzx.maxiansheng.util.ToolUtil;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.http.HttpHelp;
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
import com.zhy.view.flowlayout.TagFlowLayout;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerActivity;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * Created by 李 on 2018/4/12.
 * 设置-意见反馈  调完
 */

public class SetFeedBackActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {
    @BindView(R.id.rg_experience)
    RadioButton rgExperience;
    @BindView(R.id.rb_complain)
    RadioButton rbComplain;
    @BindView(R.id.rg_tab)
    RadioGroup rgTab;
    @BindView(R.id.ll_tell)
    LinearLayout llTell;
    @BindView(R.id.fl_complain)
    FrameLayout flComplain;
    @BindView(R.id.et_enter)
    EditText etEnter;
    @BindView(R.id.tv_enter_words)
    TextView tvEnterWords;
    @BindView(R.id.gv_camera)
    GridView gvCamera;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    @BindView(R.id.fl_experience)
    FrameLayout flExperience;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tfl_type)
    TagFlowLayout tflType;
    private String typeWords = "";

    private Dialog mDialog;
    private File takePhotoFile;

    private final int REQUEST_CODE_TAKE_PHOTO = 100;
    private final int REQUEST_CODE_CHOICE_PHOTO = 200;

    private final int REQUIRES_PERMISSION = 0;//申请权限
    private static final int REQUEST_CODE_SETTING = 300;
    private List<String> stringList = new ArrayList<>();
    private Map<String, List<File>> listFile = new LinkedHashMap<>();

    private PreferencesHelper helper;
    private String phone = "";

    //图片选择
    private CommentPicListAdapter adapter;
    private SetFeedbackTypeAdapter typeAdapter;
    private List<SetFeedbackTypeListBean> listBeans = new ArrayList<>();

    @Override
    public int getContentView() {
        return R.layout.activity_set_feedback;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setLeftImageRes(R.drawable.ic_back_gray);
        titleBar.setTitleText("意见反馈");
        rgTab.setOnCheckedChangeListener(this);
        etEnter.addTextChangedListener(watcher);
        helper = new PreferencesHelper(this);

        initEvent();
    }

    private void initEvent() {
        //提交照片
        adapter = new CommentPicListAdapter(this);
        gvCamera.setAdapter(adapter);
        adapter.setList(stringList);
        gvCamera.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (stringList != null) {
                    if (stringList.size() <= position) {
                        DialogPic(4 - stringList.size());
                    }
                } else {
                    DialogPic(4);
                }

            }
        });
        //问题类型
        tflType.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet) {
                if (!selectPosSet.isEmpty()){
                    Iterator<Integer> it = selectPosSet.iterator();
                    Integer a=it.next();
                    typeWords=String.valueOf(listBeans.get(a).getId());
                }else {
                    typeWords="";
                }
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
//        if(ev.getAction() == MotionEvent.ACTION_MOVE){
//            return true;//禁止Gridview进行滑动
//        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void getData() {
        //获取电话号码
        getPhone(helper.getToken());
        //获取问题的种类
        getFeedbackType(helper.getToken());
    }

    //获取电话号码
    private void getPhone(String token) {
        HttpHelp.getInstance().create(RemoteApi.class).getPhone(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<ComplainPhoneBean>>(SetFeedBackActivity.this, null) {
                    @Override
                    public void onNext(BaseBean<ComplainPhoneBean> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            phone = baseBean.data.getPhone();
                            tvPhone.setText(phone);
                        }else if (baseBean.code==4){
                            ToolUtil.goToLogin(SetFeedBackActivity.this);
                        } else {
                            ToastUtil.showToast(SetFeedBackActivity.this, baseBean.message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
    }

    //提交图片
    private void sendFeedback(MultipartBody.Builder builder) {
        HttpHelp.getInstance().create(RemoteApi.class).sendFeedback(builder.build())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean>(this, null) {
                    @Override
                    public void onNext(BaseBean baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            ToastUtil.showToast(SetFeedBackActivity.this, "" + baseBean.message);
//                            adapter.onClear();
//                            etEnter.setText("");
                            finish();
                        }else {
                            ToastUtil.showToast(SetFeedBackActivity.this, "" + baseBean.message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        adapter.onClear();
                        etEnter.setText("");
                        ToastUtil.showToast(SetFeedBackActivity.this, "网络连接错误");
                    }
                });

    }

    //获取问题的种类
    private void getFeedbackType(String token) {
        HttpHelp.getInstance().create(RemoteApi.class).getFeedbackType(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<List<SetFeedbackTypeListBean>>>(SetFeedBackActivity.this, null) {
                    @Override
                    public void onNext(BaseBean<List<SetFeedbackTypeListBean>> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            listBeans = baseBean.data;
                            typeAdapter = new SetFeedbackTypeAdapter(listBeans, SetFeedBackActivity.this);
                            tflType.setAdapter(typeAdapter);
                        }else if (baseBean.code==4){
                            ToolUtil.goToLogin(SetFeedBackActivity.this);
                        } else {
                         ToastUtil.showToast(SetFeedBackActivity.this,baseBean.message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
    }

    /**
     * 监听tab 两个标题切换
     */
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_complain:
                flComplain.setVisibility(View.VISIBLE);
                flExperience.setVisibility(View.GONE);

                break;
            case R.id.rg_experience:
                flExperience.setVisibility(View.VISIBLE);
                flComplain.setVisibility(View.GONE);
                break;
        }
    }

    @SuppressLint("MissingPermission")
    @OnClick({R.id.btn_submit, R.id.ll_tell})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_submit:
                if (TextUtils.isEmpty(typeWords)) {
                    ToastUtil.showToast(SetFeedBackActivity.this, "请选择您遇到的问题种类");
                    return;
                }
                if (TextUtils.isEmpty(etEnter.getText().toString())) {
                    ToastUtil.showToast(SetFeedBackActivity.this, "请输入您遇到的问题");
                    return;
                }
                if (count>100){
                    ToastUtil.showToast(SetFeedBackActivity.this, "字数超出");
                    return;
                }
                //上传图片不知所谓
                final MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
                builder.addFormDataPart("token", new PreferencesHelper(SetFeedBackActivity.this).getToken())
                        .addFormDataPart("msg_type", typeWords)
                        .addFormDataPart("msg_content", etEnter.getText().toString())
                        .addFormDataPart("type", "android");
                if (stringList.size() > 0 && stringList != null) {
                    final List<File> fileList = new ArrayList<>();
                    loadingLayout.setStatus(LoadingLayout.Loading);
                    for (int i = 0; i < stringList.size(); i++) {
                        Luban.with(SetFeedBackActivity.this)
                                .load(new File(stringList.get(i)))//传人要压缩的图片
                                .setCompressListener(new OnCompressListener() {
                                    @Override
                                    public void onStart() {
                                        //  压缩开始前调用，可以在方法内启动 loading UI
                                    }

                                    @Override
                                    public void onSuccess(File file) {
                                        //  压缩成功后调用，返回压缩后的图片文件
                                        fileList.add(file);

                                        RequestBody IDFrontBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                                        builder.addFormDataPart("message_img[]", file.getName(), IDFrontBody);
                                        if(fileList.size() ==stringList.size() ){
                                            loadingLayout.setStatus(LoadingLayout.Success);
                                            sendFeedback(builder);
                                        }
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        //  当压缩过程出现问题时调用
                                    }
                                }).launch();    //启动压缩

                    }
                }else {
                    sendFeedback(builder);
                }


                break;
            case R.id.ll_tell:
                //拨打电话
                ToolUtil.goToCall(SetFeedBackActivity.this, phone);
                break;
        }
    }

    int count = 0;
    private TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //输入文本之前的状态
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //输入文字中的状态，count是输入字符数
        }

        @Override
        public void afterTextChanged(Editable s) {
            //输入文字后的状态
            if (!TextUtils.isEmpty(s.toString())) {
                btnSubmit.setEnabled(true);
            } else {
                btnSubmit.setEnabled(false);
            }
            count = s.length();
            if (count > 100) {
                tvEnterWords.setTextColor(getResources().getColor(R.color.orangeRed));
                tvEnterWords.setText(count + "/100");
            } else if (count <= 0) {
                tvEnterWords.setTextColor(getResources().getColor(R.color.text_color9));
                tvEnterWords.setText("0/100");
            } else {
                tvEnterWords.setTextColor(getResources().getColor(R.color.text_color9));
                tvEnterWords.setText(count + "/100");
            }
        }
    };

    /**
     * 弹框
     */
    public void DialogPic(final int count) {


        if (!AndPermission.hasPermission(SetFeedBackActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)) {
            requestPermission();
            return;
        }


        mDialog = new Dialog(SetFeedBackActivity.this, R.style.ActionSheetDialogStyle);
        View inflate = LayoutInflater.from(SetFeedBackActivity.this).inflate(R.layout.dialog_pic_gain, null);

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
                try {
                    takePhotoFile = TakePhotoUtils.takePhoto((Activity) SetFeedBackActivity.this, "maxianshengPhotos", REQUEST_CODE_TAKE_PHOTO);
                } catch (IOException e) {
                    ToastUtil.showToast(SetFeedBackActivity.this, "拍照失败");
                    e.printStackTrace();
                }
            }
        });
        photo_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//相册
                mDialog.dismiss();
                //从相册选择
                startActivityForResult(BGAPhotoPickerActivity.newIntent(SetFeedBackActivity.this, null, count, null, false), REQUEST_CODE_CHOICE_PHOTO);
            }
        });

    }

    public void setStringList(List<String> stringList) {
        if (stringList != null) {
            this.stringList = stringList;
        }
        adapter.setList(stringList);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtil.d("回调成功  onActivityResult");
        String path;
        //mBaseDialog.dismiss();

        //拍照回调
        if (requestCode == REQUEST_CODE_TAKE_PHOTO && resultCode == RESULT_OK) {
            LogUtil.d("拍照回调成功  onActivityResult");
            if (takePhotoFile != null) {
                List<String> list = new ArrayList<>();

                if (stringList != null) {
                    list.addAll(stringList);
                }

                path = takePhotoFile.getAbsolutePath();
                list.add(path);
                setStringList(list);
                /*listMap.put(goods_id, list);
                setListMap(listMap);*/

            }
        }

        //选择图片回调
        if (requestCode == REQUEST_CODE_CHOICE_PHOTO && resultCode == RESULT_OK) {
            LogUtil.d("选择回调成功  onActivityResult");
            try {

                List<String> lists = new ArrayList<>();

                List<String> listImg = BGAPhotoPickerActivity.getSelectedImages(data);
                if (stringList != null) {
                    lists.addAll(stringList);
                }

                lists.addAll(listImg);
                setStringList(lists);
              /*  listMap.put(goods_id + "", lists);

                setListMap(listMap);*/

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
                    ToastUtil.showToast(SetFeedBackActivity.this, "权限申请成功");
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
                    if (AndPermission.hasAlwaysDeniedPermission(SetFeedBackActivity.this, deniedPermissions)) {
                        //用自定义的提示语。
                        AndPermission.defaultSettingDialog(SetFeedBackActivity.this, REQUEST_CODE_SETTING)
                                .setTitle("权限申请失败")
                                .setMessage("我们需要的一些权限被您拒绝或者系统发生错误申请失败，请您到设置页面手动授权，否则功能无法正常使用！")
                                .setPositiveButton("好，去设置")
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ToastUtil.showToast(SetFeedBackActivity.this, "权限申请失败，无法评论");
                                        finish();
                                    }
                                })
                                .show();
                    } else {
                        ToastUtil.showToast(SetFeedBackActivity.this, "权限申请失败，无法评论");
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
            AlertDialog.newBuilder(SetFeedBackActivity.this)
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
