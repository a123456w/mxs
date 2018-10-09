package com.guo.qlzx.maxiansheng.activity;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.guo.qlzx.maxiansheng.R;
import com.guo.qlzx.maxiansheng.bean.InviteBean;
import com.guo.qlzx.maxiansheng.http.RemoteApi;
import com.guo.qlzx.maxiansheng.util.dialog.SharedDialog;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.GlideUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 李 on 2018/4/16.
 * 邀请好友  分享未做
 */

public class InviteActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.iv_get)
    ImageView ivGet;
    @BindView(R.id.vf_get)
    ViewFlipper vfGet;
    @BindView(R.id.iv_new_img)
    ImageView ivNewImg;
    @BindView(R.id.iv_new)
    ImageView ivNew;
    @BindView(R.id.btn_shared)
    Button btnShared;
    @BindView(R.id.tv_invitation)
    TextView tvInvitation;
    @BindView(R.id.tv_reward)
    TextView tvReward;

    private List<String> list=new ArrayList<>();
    private InviteBean inviteBean;
    private PreferencesHelper helper;
    private String sharedUrl="";
    private String sharedImg="";
    private Boolean isLoad=false;
    @Override
    public int getContentView() {
        return R.layout.activity_invite;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Loading);
        titleBar.setTitleText("邀请好友");
        titleBar.setRightImageRes(R.drawable.ic_shared);
        helper=new PreferencesHelper(this);
        titleBar.setRightImageClick(this);
        btnShared.setOnClickListener(this);
    }

    @Override
    public void getData() {
        getInviteData(helper.getToken());
    }
    private void getInviteData(String token){
        HttpHelp.getInstance().create(RemoteApi.class).getInviteInfo(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<InviteBean>>(this, null) {
                    @Override
                    public void onNext(BaseBean<InviteBean> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            loadingLayout.setStatus(LoadingLayout.Success);
                            isLoad=true;
                            inviteBean=baseBean.data;
                            list=inviteBean.getList();
                            GlideUtil.display(InviteActivity.this, Constants.IMG_HOST+inviteBean.getGet_img(),ivGet);
                            GlideUtil.display(InviteActivity.this, Constants.IMG_HOST+inviteBean.getNew_img(),ivNew);
                            tvInvitation.setText(inviteBean.getInvitation()+"");
                            tvReward.setText(inviteBean.getReward()+"");
                            sharedUrl=inviteBean.getShare_url();
                            sharedImg=inviteBean.getShare_img();
                            initViews();
                        } else {
                            ToastUtil.showToast(InviteActivity.this, baseBean.message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        ToastUtil.showToast(InviteActivity.this,"数据加载失败，请重试");
                        finish();
                        super.onError(throwable);
                    }
                });
    }

    //滚动
    private List<View> views=new ArrayList<>();
    private void initViews() {
        for (int i = 0; i < list.size(); i++) {
            //设置滚动的单个布局
            //初始化布局的控件
            LinearLayout moreView = (LinearLayout) LayoutInflater.from(InviteActivity.this).inflate(R.layout.item_textview, null);
            TextView tvTitle = (TextView) moreView.findViewById(R.id.tv_content);
            tvTitle.setText(list.get(i)+"");
            //添加到循环滚动数组里面去
            views.add(moreView);
        }
        for (int i = 0; i < views.size(); i++) {
            vfGet.addView(views.get(i));
        }
    }

    @Override
    public void onClick(View v) {
        //右上角分享和按钮分享
        if (TextUtils.isEmpty(sharedUrl)||sharedUrl==null){
            ToastUtil.showToast(InviteActivity.this,"正在加载中，请稍后");
            return;
        }
        if (isLoad){
            SharedDialog dialog=SharedDialog.showDialog(sharedImg,sharedUrl,inviteBean.getTitle(),inviteBean.getContent(),this);
            dialog.show();
        }

    }
}
