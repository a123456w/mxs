package com.guo.qlzx.maxiansheng.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.guo.qlzx.maxiansheng.R;
import com.guo.qlzx.maxiansheng.bean.MessageCenterListBean;
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
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 李 on 2018/4/28.
 * 消息详情
 */

public class MessageDetailsActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.ll_details)
    LinearLayout llDetails;

    private MessageCenterListBean bean;
    private String id = "";
    private PreferencesHelper helper;
    private String orderId="";
    private String order_goods;


    @Override
    public int getContentView() {
        return R.layout.activity_message_details;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("消息详情");
        id = getIntent().getStringExtra("MESSAGEID");
        helper = new PreferencesHelper(this);
        llDetails.setOnClickListener(this);
    }

    @Override
    public void getData() {
        id = getIntent().getStringExtra("MESSAGEID");
        getMessageData(helper.getToken(), id);
    }

    private void getMessageData(String token, String id) {
        HttpHelp.getInstance().create(RemoteApi.class).getMessageDetails(token, id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<MessageCenterListBean>>(this, null) {
                    @Override
                    public void onNext(BaseBean<MessageCenterListBean> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            bean = baseBean.data;
                            order_goods = bean.getOrder_goods();

                            tvTitle.setText(bean.getTitle());
                            tvContent.setText(bean.getContent());
                            orderId=bean.getOrder_id();
                            tvTime.setText(ToolUtil.getStrTime(bean.getSend_time(), "yyyy/MM/dd HH:mm:ss"));
                        } else if (baseBean.code == 4) {
                            ToolUtil.goToLogin(MessageDetailsActivity.this);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
    }

    @Override
    public void onClick(View v) {
        //点击跳转
        if (!"0".equals(orderId)&&!TextUtils.isEmpty(orderId)){

                Intent intent=new Intent(MessageDetailsActivity.this,ToPayAgainDetailsActivity.class);
                intent.putExtra("ORDERID",orderId);
                intent.putExtra("ORDERGOODS",order_goods);
                startActivity(intent);


        }
    }
}
