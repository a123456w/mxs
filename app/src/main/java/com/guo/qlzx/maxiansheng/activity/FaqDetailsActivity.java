package com.guo.qlzx.maxiansheng.activity;

import android.widget.TextView;

import com.guo.qlzx.maxiansheng.R;
import com.guo.qlzx.maxiansheng.bean.FaqDetailsBean;
import com.guo.qlzx.maxiansheng.http.RemoteApi;
import com.guo.qlzx.maxiansheng.util.ToolUtil;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.widget.LoadingLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 李 on 2018/4/26.
 * 常见问题 -详情   没有接口
 */

public class FaqDetailsActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_content)
    TextView tvContent;

    private int id=0;
    private FaqDetailsBean bean;
    private PreferencesHelper helper;
    @Override
    public int getContentView() {
        return R.layout.activity_faq_details;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("常见问题");
        id=getIntent().getIntExtra("ID",0);
        helper=new PreferencesHelper(this);
    }

    @Override
    public void getData() {
        getDatas(helper.getToken(),id);
    }

    private void getDatas(String token,int id){
        HttpHelp.getInstance().create(RemoteApi.class).getFaqDetailsData(token,id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<FaqDetailsBean>>(this, null) {
                    @Override
                    public void onNext(BaseBean<FaqDetailsBean> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code==0){
                            bean=baseBean.data;
                           tvContent.setText("   "+bean.getContent());
                           tvTitle.setText(bean.getTitle());
                        }else if (baseBean.code==4){
                            ToolUtil.goToLogin(FaqDetailsActivity.this);
                        }
                    }
                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
    }
}
