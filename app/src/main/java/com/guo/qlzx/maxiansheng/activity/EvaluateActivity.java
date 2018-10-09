package com.guo.qlzx.maxiansheng.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.guo.qlzx.maxiansheng.R;
import com.guo.qlzx.maxiansheng.adapter.OrderEvaluateAdapter;
import com.guo.qlzx.maxiansheng.bean.EvaluateSendBean;
import com.guo.qlzx.maxiansheng.bean.OrderEvaluateBean;
import com.guo.qlzx.maxiansheng.bean.OrdersBean;
import com.guo.qlzx.maxiansheng.event.UpdataOrderEvent;
import com.guo.qlzx.maxiansheng.http.RemoteApi;
import com.guo.qlzx.maxiansheng.util.FileSizeUtil;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.EventBusUtil;
import com.qlzx.mylibrary.util.LogUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.SPUtils;
import com.qlzx.mylibrary.util.TakePhotoUtils;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.qlzx.mylibrary.widget.NoScrollListView;
import com.yanzhenjie.alertdialog.AlertDialog;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
 * Created by Administrator on 2018/4/18 0018.
 */

public class EvaluateActivity extends BaseActivity {
    @BindView(R.id.evaluate_nlv)
    NoScrollListView evaluateNlv;
    @BindView(R.id.ratingBar_logistics)
    RatingBar ratingBarLogistics;
    @BindView(R.id.text_logistics)
    TextView textLogistics;
    @BindView(R.id.ratingBar_speed)
    RatingBar ratingBarSpeed;
    @BindView(R.id.text_speed)
    TextView textSpeed;
    @BindView(R.id.ratingBar_manner)
    RatingBar ratingBarManner;
    @BindView(R.id.text_manner)
    TextView textManner;
    @BindView(R.id.evaluate_release)
    Button evaluateRelease;

    private List<OrderEvaluateBean> orderList = new ArrayList<>();


    private Dialog mDialog;
    private File takePhotoFile;
    private StringBuilder data;

    private final int REQUEST_CODE_TAKE_PHOTO = 100;
    private final int REQUEST_CODE_CHOICE_PHOTO = 200;

    private final int REQUIRES_PERMISSION = 0;//申请权限
    private static final int REQUEST_CODE_SETTING = 300;

    private OrderEvaluateAdapter adapter;
    private PreferencesHelper helper;
    private String goods_id = null;
    private Map<String, List<String>> listMap = new LinkedHashMap<>();
    private Map<String, List<File>> listFile = new LinkedHashMap<>();
    private ArrayList<OrderEvaluateBean> goodsList;

    private OrdersBean bean;
    private String logisticsNum = "5", deliverNum = "5", serviceNum = "5";
    private float scoreNum;


    @Override
    public int getContentView() {
        return R.layout.activity_evaluate;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setLeftImageRes(R.drawable.ic_back_gray);
        titleBar.setTitleText("订单评价");

        adapter = new OrderEvaluateAdapter(evaluateNlv);
        evaluateNlv.setAdapter(adapter);
        helper = new PreferencesHelper(EvaluateActivity.this);

        bean = (OrdersBean) getIntent().getSerializableExtra("info");


        ratingBarLogistics.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                logisticsNum = String.valueOf(rating);
                if ((int) rating == 0) {
                    ratingBar.setRating(1);
                }
                /*textLogistics.setVisibility(View.VISIBLE);
                switch (num) {
                    case 0:
                        textLogistics.setVisibility(View.GONE);
                        break;
                    case 1:
                        textLogistics.setText("非常差");
                        break;
                    case 2:
                        textLogistics.setText("差");
                        break;
                    case 3:
                        textLogistics.setText("一般");
                        break;
                    case 4:
                        textLogistics.setText("好");
                        break;
                    case 5:
                        textLogistics.setText("非常好"
                        );
                        break;
                }*/
            }
        });

        ratingBarManner.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                deliverNum = String.valueOf(rating);
                if ((int) rating == 0) {
                    ratingBar.setRating(1);
                }
                /*textManner.setVisibility(View.VISIBLE);
                switch (num) {
                    case 0:
                        textManner.setVisibility(View.GONE);
                        break;
                    case 1:
                        textManner.setText("非常差");
                        break;
                    case 2:
                        textManner.setText("差");
                        break;
                    case 3:
                        textManner.setText("一般");
                        break;
                    case 4:
                        textManner.setText("好");
                        break;
                    case 5:
                        textManner.setText("非常好");
                        break;
                }*/
            }
        });

        ratingBarSpeed.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean b) {
                serviceNum = String.valueOf(rating);
                if ((int) rating == 0) {
                    ratingBar.setRating(1);
                }
                /*textSpeed.setVisibility(View.VISIBLE);
                switch (num) {
                    case 0:
                        textManner.setVisibility(View.GONE);
                        break;
                    case 1:
                        textManner.setText("非常差");
                        break;
                    case 2:
                        textManner.setText("差");
                        break;
                    case 3:
                        textManner.setText("一般");
                        break;
                    case 4:
                        textManner.setText("好");
                        break;
                    case 5:
                        textManner.setText("非常好");
                        break;
                }*/
            }

        });

    }

    @Override
    public void getData() {
        String scoreNum=OrderEvaluateAdapter.scoreNum;
        goodsList = new ArrayList<>();
        for (OrdersBean.OrderBean orderBean : bean.getOrder()) {
            OrderEvaluateBean eva = new OrderEvaluateBean();
            eva.setId(orderBean.getGoods_id());
            eva.setPic(orderBean.getOriginal_img());
            eva.setTitle(orderBean.getGoods_name());
            eva.setScore(scoreNum);
            goodsList.add(eva);
        }
        adapter.setData(goodsList);
    }


    @OnClick({R.id.evaluate_release})
    public void onViewClick(View view) {
        data = new StringBuilder("[");
        for (int i = 0; i < goodsList.size(); i++) {
            if (TextUtils.isEmpty(goodsList.get(i).getScore())) {
                ToastUtil.showToast(EvaluateActivity.this, "评分不能为空");
                return;
            }
            if (TextUtils.isEmpty(goodsList.get(i).getContent())) {
                ToastUtil.showToast(EvaluateActivity.this, "评价不能为空");
                return;
            }
            data.append(toJson(new EvaluateSendBean(goodsList.get(i).getId(), goodsList.get(i).getScore(), goodsList.get(i).getContent())));
            if (i != goodsList.size() - 1) {
                data.append(",");
            }
        }
        data.append("]");
        LogUtil.i("评价的data参数" + data.toString());

        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        builder.addFormDataPart("token", helper.getToken())
                .addFormDataPart("order_id", bean.getOrder_id())
                .addFormDataPart("logistics_score", logisticsNum)
                .addFormDataPart("deliver_goods", deliverNum)
                .addFormDataPart("service_attitude", serviceNum)
                .addFormDataPart("type", "android")
                .addFormDataPart("data", data.toString());

        for (OrderEvaluateBean eva :goodsList) {
            /*if (listMap.get(eva.getId()) != null && listMap.get(eva.getId()).size() > 0) {
                for (int i =0;i<listMap.get(eva.getId()).size();i++){
                    RequestBody IDFrontBody = RequestBody.create(MediaType.parse("multipart/form-data"), new File(listMap.get(eva.getId()).get(i)));
                    builder.addFormDataPart("img"+eva.getId()+"["+i+"]", "img"+eva.getId()+i, IDFrontBody);
                    LogUtil.i("图片大小："+ FileSizeUtil.getFileOrFilesSize(listMap.get(eva.getId()).get(i),2));
                }
            }*/

            if (listFile.get(eva.getId()) != null && listFile.get(eva.getId()).size() > 0) {
                for (int i =0;i<listFile.get(eva.getId()).size();i++){
                    RequestBody IDFrontBody = RequestBody.create(MediaType.parse("multipart/form-data"), listFile.get(eva.getId()).get(i));
                    builder.addFormDataPart("img"+eva.getId()+"["+i+"]", listFile.get(eva.getId()).get(i).getName(), IDFrontBody);
                    LogUtil.i("图片大小："+ FileSizeUtil.getFileOrFilesSize(listFile.get(eva.getId()).get(i).getPath(),2));
                }
            }
        }
        evaluate(builder);

    }


    private void evaluate(MultipartBody.Builder builder) {
        showLoadingDialog("提交中。。。");
        HttpHelp.getInstance().create(RemoteApi.class).evaluate(builder.build())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<Object>>(EvaluateActivity.this, null) {
                    @Override
                    public void onNext(BaseBean<Object> baseBean) {
                        super.onNext(baseBean);
                        hideLoadingDialog();
                        if (baseBean.code == 0) {
                            EventBusUtil.post(new UpdataOrderEvent());
                            finish();
                            ToastUtil.showToast(EvaluateActivity.this, baseBean.message);
                        } else {
                            ToastUtil.showToast(EvaluateActivity.this, baseBean.message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        hideLoadingDialog();
                        Log.i("XXX", throwable.getLocalizedMessage());

                    }
                });
    }


    /**
     * 弹框
     *
     * @param uid
     */

    public void DialogPic(String uid, final int count) {
        if (uid == null) {
            return;
        }


        if (!AndPermission.hasPermission(EvaluateActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)) {
            requestPermission();
            return;
        }


        goods_id = uid;
        mDialog = new Dialog(EvaluateActivity.this, R.style.ActionSheetDialogStyle);
        View inflate = LayoutInflater.from(EvaluateActivity.this).inflate(R.layout.dialog_pic_gain, null);

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
                    takePhotoFile = TakePhotoUtils.takePhoto((Activity) EvaluateActivity.this, "maxianshengPhotos", REQUEST_CODE_TAKE_PHOTO);
                } catch (IOException e) {
                    ToastUtil.showToast(EvaluateActivity.this, "拍照失败");
                    e.printStackTrace();
                }
            }
        });
        photo_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//相册
                mDialog.dismiss();
                //从相册选择
                startActivityForResult(BGAPhotoPickerActivity.newIntent(EvaluateActivity.this, null, count, null, false), REQUEST_CODE_CHOICE_PHOTO);
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String path;
//        mBaseDialog.dismiss();

        //拍照回调
        if (requestCode == REQUEST_CODE_TAKE_PHOTO && resultCode == RESULT_OK) {
            if (takePhotoFile != null) {
                List<String> list = new ArrayList<>();

                if (listMap.get(goods_id) != null) {
                    list.addAll(listMap.get(goods_id));
                }

                path = takePhotoFile.getAbsolutePath();
                list.add(path);
                listMap.put(goods_id, list);
                modifyAvatar(list, goods_id);
                adapter.setListMap(listMap);

            }
        }

        //选择图片回调
        if (requestCode == REQUEST_CODE_CHOICE_PHOTO && resultCode == RESULT_OK) {
            try {

                List<String> lists = new ArrayList<>();

                List<String> listImg = BGAPhotoPickerActivity.getSelectedImages(data);
                if (listMap.get(goods_id) != null) {
                    lists.addAll(listMap.get(goods_id));
                }

                lists.addAll(listImg);

                listMap.put(goods_id, lists);
                modifyAvatar(lists, goods_id);
                adapter.setListMap(listMap);

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
                    ToastUtil.showToast(EvaluateActivity.this, "权限申请成功");
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
                    if (AndPermission.hasAlwaysDeniedPermission(EvaluateActivity.this, deniedPermissions)) {
                        //用自定义的提示语。
                        AndPermission.defaultSettingDialog(EvaluateActivity.this, REQUEST_CODE_SETTING)
                                .setTitle("权限申请失败")
                                .setMessage("我们需要的一些权限被您拒绝或者系统发生错误申请失败，请您到设置页面手动授权，否则功能无法正常使用！")
                                .setPositiveButton("好，去设置")
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ToastUtil.showToast(EvaluateActivity.this, "权限申请失败，无法评论");
                                        finish();
                                    }
                                })
                                .show();
                    } else {
                        ToastUtil.showToast(EvaluateActivity.this, "权限申请失败，无法评论");
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
            AlertDialog.newBuilder(EvaluateActivity.this)
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


    /**
     * 对象转json
     *
     * @param obj
     * @return
     */
    private String toJson(Object obj) {

        Gson gson = new Gson();
        String obj2 = gson.toJson(obj);
        return obj2;
    }

    /**
     * 压缩图片
     */
    private void modifyAvatar(List<String> list, String goods_id) {
        if (list != null && list.size() > 0 && goods_id != null) {
            final List<File> fileList = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                Luban.with(EvaluateActivity.this)
                        .load(new File(list.get(i)))//传人要压缩的图片
                        .setCompressListener(new OnCompressListener() {
                            @Override
                            public void onStart() {
                                //  压缩开始前调用，可以在方法内启动 loading UI
                            }

                            @Override
                            public void onSuccess(File file) {
                                //  压缩成功后调用，返回压缩后的图片文件
                                fileList.add(file);
                            }

                            @Override
                            public void onError(Throwable e) {
                                //  当压缩过程出现问题时调用
                            }
                        }).launch();    //启动压缩
            }
            listFile.put(goods_id, fileList);
        }
    }


}
