package com.guo.qlzx.maxiansheng.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.guo.qlzx.maxiansheng.R;
import com.guo.qlzx.maxiansheng.adapter.FaqAdapter;
import com.guo.qlzx.maxiansheng.bean.FaqListBean;
import com.guo.qlzx.maxiansheng.http.RemoteApi;
import com.guo.qlzx.maxiansheng.util.ToolUtil;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.base.OnRVItemClickListener;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.widget.LoadingLayout;
import com.qlzx.mylibrary.widget.RecycleViewDivider;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 李 on 2018/4/26.
 *  //常见问题   没有接口
 */

public class FaqActivity extends BaseActivity implements OnRVItemClickListener {
    @BindView(R.id.can_content_view)
    RecyclerView canContentView;
    private FaqAdapter adapter;

    private List<FaqListBean> beans=new ArrayList<>();
    private PreferencesHelper helper;
    @Override
    public int getContentView() {
        return R.layout.activity_faq;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setTitleText("常见问题");
        adapter = new FaqAdapter(canContentView);
        canContentView.setLayoutManager(new LinearLayoutManager(this));
        canContentView.addItemDecoration(new RecycleViewDivider(this));
        canContentView.setAdapter(adapter);
        adapter.setOnRVItemClickListener(this);
        helper=new PreferencesHelper(this);

    }

    @Override
    public void getData() {
        getDatas(helper.getToken());
    }

    private void getDatas(String token){
        HttpHelp.getInstance().create(RemoteApi.class).getFaqList(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<List<FaqListBean>>>(this, null) {
                    @Override
                    public void onNext(BaseBean<List<FaqListBean>> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code==0){
                            beans=baseBean.data;
                            adapter.setData(beans);
                        }else if (baseBean.code==4){
                            ToolUtil.goToLogin(FaqActivity.this);
                        }
                    }
                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
    }



    @Override
    public void onRVItemClick(ViewGroup parent, View itemView, int position) {
        //点击条目
        Intent intent=new Intent(FaqActivity.this,FaqDetailsActivity.class);
        intent.putExtra("ID",adapter.getItem(position).getCat_id());
        startActivity(intent);
    }
}
