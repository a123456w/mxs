package com.guo.qlzx.maxiansheng.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.guo.qlzx.maxiansheng.R;
import com.guo.qlzx.maxiansheng.http.RemoteApi;
import com.guo.qlzx.maxiansheng.util.ListDataSave;
import com.guo.qlzx.maxiansheng.util.costom.FlowViewGroup;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 李 on 2018/4/11.
 * 搜索 - 第一层
 */

public class SearchActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.tv_search)
    TextView tvSearch;
    @BindView(R.id.fvg_hot)
    FlowViewGroup fvgHot;
    @BindView(R.id.iv_delete)
    ImageView ivDelete;
    @BindView(R.id.fvg_history)
    FlowViewGroup fvgHistory;
    @BindView(R.id.ll_empty)
    LinearLayout llEmpty;

    private List<String> hotBeans = new ArrayList<>();
    private List<String> historyBeans = new ArrayList<>();
    private ListDataSave dataSave;
    ;

    @Override
    public int getContentView() {
        return R.layout.activity_search;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        hideTitleBar();
        //fvgHistory = findViewById(R.id.fvg_history);
        dataSave = new ListDataSave(this, "SEARCHWORDS");
        fvgHistory.setVisibility(View.GONE);
        llEmpty.setVisibility(View.VISIBLE);
        getHistoryData();
        etSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {//修改回车键功能
                    if (etSearch.getText().toString() != null && !"".equals(etSearch.getText().toString())) {
                        historyBeans.add(etSearch.getText().toString());
                        dataSave.setDataList("SEARCHWORDS", historyBeans);
                        //搜索按钮
                        Intent intent = new Intent(SearchActivity.this, SearchListActivity.class);
                        intent.putExtra("search_txt", etSearch.getText().toString());
                        startActivity(intent);

                    }
                }
                return false;
            }
        });

    }

    @Override
    public void getData() {
        getSearchHotWords();
    }

    //获取搜索热词
    private void getSearchHotWords() {
        HttpHelp.getInstance().create(RemoteApi.class).getSearchHotWords()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<List<String>>>(this, null) {
                    @Override
                    public void onNext(BaseBean<List<String>> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            hotBeans = baseBean.data;
                            setHotViews(hotBeans);
                        } else {

                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
    }


    //搜索热词放置
    private void setHotViews(final List<String> list) {
        fvgHot.removeAllViews();
        //FlowViewGroup fvgHot = findViewById(R.id.fvg_hot);
        for (int i = 0; i < list.size(); i++) {
            TextView tv = (TextView) LayoutInflater.from(this).inflate(R.layout.item_search_hot, fvgHot, false);
            tv.setText(list.get(i));
            final int finalI = i;
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //跳转
                    Intent intent = new Intent(SearchActivity.this, SearchListActivity.class);
                    intent.putExtra("search_txt", list.get(finalI));
                    startActivity(intent);
                }
            });
            fvgHot.addView(tv);
        }
    }

    //获取历史搜索
    public void getHistoryData() {
        historyBeans.clear();
        historyBeans = dataSave.getDataList("SEARCHWORDS");
        setHistoryViews(historyBeans);
    }

    //历史搜索放置
    private void setHistoryViews(final List<String> stringList) {
        fvgHistory.removeAllViews();
        if (stringList == null || stringList.size() <= 0) {
            fvgHistory.setVisibility(View.GONE);
            llEmpty.setVisibility(View.VISIBLE);
            return;
        }
        llEmpty.setVisibility(View.GONE);
        fvgHistory.setVisibility(View.VISIBLE);
        for (int i = 0; i < stringList.size(); i++) {
            final TextView tv = (TextView) LayoutInflater.from(this).inflate(R.layout.item_search_hot, fvgHistory, false);
            tv.setText(stringList.get(i));
            final int finalI = i;
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SearchActivity.this, SearchListActivity.class);
                    intent.putExtra("search_txt", stringList.get(finalI));
                    startActivity(intent);
                }
            });
            final int finalI1 = i;
            tv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (finalI1==0){
                        historyBeans.clear();
                        dataSave.clearDataList("SEARCHWORDS");
                        fvgHistory.removeAllViews();
                        fvgHistory.setVisibility(View.GONE);
                        llEmpty.setVisibility(View.VISIBLE);
                    }
                    if (stringList.indexOf(finalI)!=-1)stringList.remove(finalI1);
                    dataSave.setDataList("SEARCHWORDS",stringList);
                    fvgHistory.removeView(tv);
                    return true;
                }
            });
            fvgHistory.addView(tv);
        }
    }

    @OnClick({R.id.iv_back, R.id.et_search, R.id.tv_search, R.id.iv_delete,R.id.iv_delete_input})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_delete_input:
                etSearch.setText("");
                break;
            case R.id.iv_back:
                //返回首页
                finish();
                break;
            case R.id.et_search:
                //搜索框
                break;
            case R.id.tv_search:
                if (etSearch.getText().toString() != null && !"".equals(etSearch.getText().toString())) {
                    historyBeans.add(etSearch.getText().toString());
                    dataSave.setDataList("SEARCHWORDS", historyBeans);
                    //搜索按钮
                    Intent intent = new Intent(SearchActivity.this, SearchListActivity.class);
                    intent.putExtra("search_txt", etSearch.getText().toString());
                    startActivity(intent);
                }else {
                    ToastUtil.showToast(this,"请输入关键字");
                }
                break;
            case R.id.iv_delete:
                historyBeans.clear();
                dataSave.clearDataList("SEARCHWORDS");
                fvgHistory.removeAllViews();
                fvgHistory.setVisibility(View.GONE);
                llEmpty.setVisibility(View.VISIBLE);
                //删除历史记录
                break;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getSearchHotWords();
        getHistoryData();
    }

}
