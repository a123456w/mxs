package com.guo.qlzx.maxiansheng.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;

import com.guo.qlzx.maxiansheng.R;
import com.guo.qlzx.maxiansheng.activity.ClassificationTwoActivity;
import com.guo.qlzx.maxiansheng.activity.SearchActivity;
import com.guo.qlzx.maxiansheng.activity.WebActivity;
import com.guo.qlzx.maxiansheng.adapter.ClassificationAdapter;
import com.guo.qlzx.maxiansheng.bean.ClassificationBean;
import com.guo.qlzx.maxiansheng.http.RemoteApi;
import com.qlzx.mylibrary.base.BaseFragment;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.GlideUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.widget.LoadingLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.guo.qlzx.maxiansheng.application.MyApplication.getContext;

/**
 * 分类页面
 * Created by dillon on 2018/4/10.
 */

public class ClassifyFragment extends BaseFragment {
    @BindView(R.id.classification_pic)
    ImageView classificationPic;
    @BindView(R.id.classification_grid)
    GridView classificationGrid;
    Unbinder unbinder;
    private PreferencesHelper helper;

    private ClassificationAdapter adapter;
    private List<ClassificationBean.ListBean> listBean = new ArrayList<>();
    private String type = "1";//1底部导航分类页


    @Override
    public View getContentView(FrameLayout frameLayout) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_classify, frameLayout, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void initView() {
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setVisibility(View.VISIBLE);

        titleBar.setTitleText("分类");
        titleBar.getTitle().setTextColor(Color.WHITE);
        titleBar.setRightImageRes(R.drawable.ic_search_white);
        titleBar.getRl_title().setBackgroundColor(getResources().getColor(R.color.colorAccent));
        titleBar.setLeftGone();
        titleBar.setRightImageClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, SearchActivity.class);
                startActivity(intent);
            }
        });
        helper = new PreferencesHelper(mContext);


        adapter = new ClassificationAdapter(classificationGrid);
        classificationGrid.setAdapter(adapter);
        classificationGrid.setFocusable(false);
        /**
         * 跳转到二级分类页面
         */
        classificationGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mContext, ClassificationTwoActivity.class);
                intent.putExtra("name", adapter.getData().get(position).getName());
                intent.putExtra("id", adapter.getData().get(position).getId());
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public void getData() {
        top_category(type);
    }

    private void top_category(String type) {
        HttpHelp.getInstance().create(RemoteApi.class).top_category(type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<ClassificationBean>>(mContext, null) {
                    @Override
                    public void onNext(BaseBean<ClassificationBean> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            if (baseBean.data != null) {
                                GlideUtil.display(mContext, Constants.IMG_HOST + baseBean.data.getImg().getAd_code(), classificationPic);
                                final String ad_link = baseBean.data.getImg().getAd_link();
                                classificationPic.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        startActivity(new Intent(getActivity(), WebActivity.class).putExtra("title", "详情")
                                                .putExtra("url", ad_link));
                                    }
                                });

                                listBean.clear();
                                listBean = baseBean.data.getList();
                                if (listBean != null && listBean.size() > 0) {
                                    adapter.setData(listBean);
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
