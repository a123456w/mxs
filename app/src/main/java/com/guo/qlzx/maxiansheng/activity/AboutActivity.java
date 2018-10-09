package com.guo.qlzx.maxiansheng.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.guo.qlzx.maxiansheng.R;
import com.guo.qlzx.maxiansheng.application.MyApplication;
import com.guo.qlzx.maxiansheng.bean.AboutBean;
import com.guo.qlzx.maxiansheng.bean.CheckVersionBean;
import com.guo.qlzx.maxiansheng.http.RemoteApi;
import com.guo.qlzx.maxiansheng.util.ToolUtil;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.http.FileCallBack;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.http.OkHttpDownloadManager;
import com.qlzx.mylibrary.util.LogUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 李 on 2018/4/13.
 * 关于我们
 */

public class AboutActivity extends BaseActivity {
    @BindView(R.id.ll_about_msg)
    LinearLayout llAboutMsg;
    @BindView(R.id.ll_about_agreement)
    LinearLayout llAboutAgreement;
    @BindView(R.id.ll_about_law)
    LinearLayout llAboutLaw;
    @BindView(R.id.ll_about_explain)
    LinearLayout llAboutExplain;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_version_info)
    TextView tvVersionInfo;
    @BindView(R.id.btn_about)
    Button btnAbout;
    @BindView(R.id.tv_copyright)
    TextView tvCopyright;

    private PreferencesHelper helper;
    private String msg = "";
    private String agreement = "";
    private String law = "";
    private String explain = "";

    @Override
    public int getContentView() {
        return R.layout.activity_about;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setLeftImageRes(R.drawable.ic_back_gray);
        titleBar.setTitleText("关于我们");
        tvVersionInfo.setText(this.getString(R.string.version_name) + getVersion());
        helper= new PreferencesHelper(this);
    }
    @Override
    public void getData() {
        getAllUrl();
    }

    /**
     * 获取关于我们的所有url链接
     */
    public void getAllUrl() {
        HttpHelp.getInstance().create(RemoteApi.class).getAboutUs()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<AboutBean>>(this, null) {
                    @Override
                    public void onNext(BaseBean<AboutBean> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            if (baseBean.data != null) {
                                AboutBean bean = baseBean.data;
                                msg = bean.getVer_msg_url();
                                agreement = bean.getVer_agreement_url();
                                law = bean.getVer_law_url();
                                explain = bean.getVer_explain_url();

                                tvName.setText(bean.getCompany());
                                tvCopyright.setText(bean.getVersion_msg());
                            }
                        } else {

                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
    }


    @OnClick({R.id.ll_about_msg, R.id.ll_about_agreement, R.id.ll_about_law, R.id.ll_about_explain, R.id.btn_about})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_about_msg:
                WebActivity.startActivity(AboutActivity.this, "版权信息", msg);
                break;
            case R.id.ll_about_agreement:
                WebActivity.startActivity(AboutActivity.this, "软件使用许可协议", agreement);
                break;
            case R.id.ll_about_law:
                WebActivity.startActivity(AboutActivity.this, "法律声明", law);
                break;
            case R.id.ll_about_explain:
                WebActivity.startActivity(AboutActivity.this, "说明", explain);
                break;
            case R.id.btn_about:
                //  检测更新 不知道为啥会这样取名字= =
                checkVersion(getVersion());
                break;
        }
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

    /**
     * 检查更新
     *
     * @param version
     */
    private void checkVersion(String version) {
        HttpHelp.getInstance().create(RemoteApi.class).checkVersion(1, version)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<CheckVersionBean>>(AboutActivity.this, null) {
                    @Override
                    public void onNext(BaseBean<CheckVersionBean> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            if ("1".equals(baseBean.data.getA_status())) {
                                LogUtil.d("有新版本---" + baseBean.data.getA_version());
                                if (!helper.getIgnoreVersion().equals(baseBean.data.getA_version())){
                                    showNewVersion(baseBean.data);
                                }else {
                                    ToastUtil.showToast(AboutActivity.this,"您已忽略当前版本更新");
                                }
                            } else {
                                ToastUtil.showToast(AboutActivity.this, baseBean.message);
                            }
                        } else {
                            ToastUtil.showToast(AboutActivity.this, "当前版本已为最新版本");
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);

                    }
                });
    }

    /**
     * 新版本dialog
     *
     * @param updateBean
     */
    private void showNewVersion(final CheckVersionBean updateBean) {
        String content = String.format("最新版本：%1$s\n新版本大小：%2$s\n\n更新内容\n%3$s", updateBean.getA_version(), updateBean.getA_size(), updateBean.getA_update_log());

        final AlertDialog dialog = new AlertDialog.Builder(this).create();

        dialog.setTitle("应用更新");
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);


        float density = getResources().getDisplayMetrics().density;
        TextView tv = new TextView(this);
        tv.setMovementMethod(new ScrollingMovementMethod());
        tv.setVerticalScrollBarEnabled(true);
        tv.setTextSize(14);
        tv.setMaxHeight((int) (250 * density));

        dialog.setView(tv, (int) (25 * density), (int) (15 * density), (int) (25 * density), 0);

        tv.setText(content);

        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "立即更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                File mApkFile = new File(getExternalCacheDir(), "maxiansheng_" + updateBean.getA_version() + ".apk");
                if (mApkFile.exists()) {
                    install(AboutActivity.this, mApkFile);
                } else {
                    getDownloadUrl(updateBean.getA_url(), mApkFile);
                }
            }
        });
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "以后再说", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.setButton(DialogInterface.BUTTON_NEUTRAL, "忽略该版", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                helper.saveIgnoreVersion(updateBean.getA_version());
            }
        });


        dialog.show();
    }

    /**
     * 安装
     *
     * @param context
     * @param file
     */
    public void install(Context context, File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        } else {
            Uri uri = FileProvider.getUriForFile(context, "com.ruirong.chefang.fileprovider", file);
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private void getDownloadUrl(final String url, final File mApkFile) {
        final OkHttpClient okHttpClient = new OkHttpClient();
        final Request build = new Request.Builder().url(url).build();
        Call call = okHttpClient.newCall(build);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showToast(AboutActivity.this, "下载失败");
                if (mApkFile.exists()) {
                    mApkFile.delete();
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(string);
                    final String downloadUrl = jsonObject.getString("data");
                    MyApplication.getMainHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            final ProgressDialog dialog = new ProgressDialog(AboutActivity.this);
                            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                            dialog.setMessage("下载中...");
                            dialog.setIndeterminate(false);
                            dialog.setCancelable(false);
                            dialog.show();
                            download(dialog, downloadUrl, mApkFile);
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });
    }

    private void download(final ProgressDialog dialog, String downloadUrl, final File mApkFile) {
        FileCallBack fileCallBack = new FileCallBack(this, mApkFile) {
            @Override
            public void inProgress(long current, long total) {
                int progress = (int) (current * 100 / total);
                LogUtil.d("下载apk---" + "current" + current + "---" + "total " + total);
                dialog.setProgress(progress);
            }

            @Override
            public void onSuccess(File response) {
                install(AboutActivity.this, response);
            }

            @Override
            public void onError(Exception e) {
                ToastUtil.showToast(AboutActivity.this, "下载失败");
                if (mApkFile.exists()) {
                    mApkFile.delete();
                }
            }

            @Override
            public void onAfter() {
                dialog.dismiss();
            }
        };
        OkHttpDownloadManager.getInstance().download(downloadUrl, fileCallBack);
    }

}
