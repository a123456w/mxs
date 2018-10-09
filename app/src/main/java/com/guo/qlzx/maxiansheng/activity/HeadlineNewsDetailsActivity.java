package com.guo.qlzx.maxiansheng.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.guo.qlzx.maxiansheng.R;
import com.guo.qlzx.maxiansheng.bean.NewsDetailsBean;
import com.guo.qlzx.maxiansheng.http.RemoteApi;
import com.guo.qlzx.maxiansheng.util.dialog.SharedDialog;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.LogUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 李 on 2018/4/11.
 * 头条新闻详情 已完成
 */

public class HeadlineNewsDetailsActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.web_view)
    WebView webView;
    @BindView(R.id.tv_back)
    ImageView tvBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_shared)
    ImageView ivShared;
    private String title = "";
    private String url = "";
    private String id = "";
    private String sharedUrl = "";
    private String sharedTitle = "";
    private String sharedContent = "";
    private String sharedImg = "";
    private PreferencesHelper helper;

    @Override
    public int getContentView() {
        return R.layout.activity_headline_news_details;
    }

    public static void startActivity(Context context, String title, String url, String id) {
        Intent intent = new Intent(context, HeadlineNewsDetailsActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("url", url);
        intent.putExtra("id", id);
        LogUtil.i("title:" + title + "  url:" + url);
        context.startActivity(intent);
    }


    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        hideTitleBar();
        helper = new PreferencesHelper(this);
        url = getIntent().getStringExtra("url");
        title = getIntent().getStringExtra("title");
        id = getIntent().getStringExtra("id");
        if (TextUtils.isEmpty(title)) {
            tvTitle.setText("新闻详情");
        } else {
            tvTitle.setText(title);
        }
        tvBack.setOnClickListener(this);
        ivShared.setOnClickListener(this);



    }

    @Override
    public void getData() {
        getNewsData(helper.getToken(), id);
    }

    private void getNewsData(String token, String id) {
        HttpHelp.getInstance().create(RemoteApi.class).getNewsData(token, id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<NewsDetailsBean>>(this, null) {
                    @Override
                    public void onNext(BaseBean<NewsDetailsBean> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            sharedUrl = baseBean.data.getShare_url();
                            sharedImg = baseBean.data.getImg();
                            sharedContent = baseBean.data.getHead_content();
                            sharedTitle = baseBean.data.getTitle();
                            Log.d("TAG",""+sharedUrl+baseBean.message);
                            initWeb(sharedUrl);
                        } else {
                            ToastUtil.showToast(HeadlineNewsDetailsActivity.this, baseBean.message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
    }

    private void initWeb(String url) {
        //设置webview的屏幕适配
        WebSettings settings = webView.getSettings();
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);//开启DOM
        settings.getJavaScriptCanOpenWindowsAutomatically();
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                showLoadingDialog("加载中");
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                hideLoadingDialog();
                view.loadUrl("javascript: document.getElementById(\"web\").style.display=\"none\";" +
                        "document.getElementById(\"newsMain\").style.paddingTop=\"3%\";void(0)");
            }
        });

        webView.loadUrl(url);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_shared:
                if (sharedUrl == null) {
                    ToastUtil.showToast(HeadlineNewsDetailsActivity.this, "页面尚未加载成功，请稍后再试");
                    return;
                }

                SharedDialog sharedDialog = SharedDialog.showDialog(sharedImg, sharedUrl, sharedTitle, sharedContent, HeadlineNewsDetailsActivity.this);
                sharedDialog.show();
                break;
            case R.id.tv_back:
                finish();
                break;
        }
    }
}
