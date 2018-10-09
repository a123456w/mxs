package com.guo.qlzx.maxiansheng.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.guo.qlzx.maxiansheng.R;
import com.guo.qlzx.maxiansheng.util.dialog.LoadDialog;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.util.LogUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 李 on 2018/4/13.
 * 网页
 */

public class WebActivity extends BaseActivity {

    @BindView(R.id.tv_back)
    ImageView tvBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.web_view)
    WebView webView;
    private String title = "";
    private String url = "";


    private LoadDialog loadDialog;

    public static void startActivity(Context context, String title, String url) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("url", url);
        LogUtil.i("title:" + title + "  url:" + url);
        context.startActivity(intent);
    }




    @Override
    public int getContentView() {
        return R.layout.activity_web;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        loadDialog = LoadDialog.showDialog(this);
        hideTitleBar();
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        title = intent.getStringExtra("title");
        if (TextUtils.isEmpty(title)) {
            rlTitle.setVisibility(View.GONE);
        } else {
            tvBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            tvTitle.setText(title);
        }
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
                loadDialog.show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                loadDialog.cancel();
                view.loadUrl("javascript: document.getElementsByClassName(\"iosHeader\")[0].style.display=\"none\";" +
                        "document.getElementsByClassName(\"iosText\")[0].style.paddingTop=\"0\";void(0)");
            }
        });

        webView.loadUrl(url);
    }

    @Override
    public void getData() {

    }
}
