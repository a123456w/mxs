package com.guo.qlzx.maxiansheng.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.guo.qlzx.maxiansheng.R;
import com.guo.qlzx.maxiansheng.adapter.AddressManagementAdapter;
import com.guo.qlzx.maxiansheng.bean.AddressManagementListBean;
import com.guo.qlzx.maxiansheng.http.RemoteApi;
import com.guo.qlzx.maxiansheng.util.ToolUtil;
import com.guo.qlzx.maxiansheng.util.dialog.AddressDeleteDialog;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.base.OnItemChildClickListener;
import com.qlzx.mylibrary.base.OnRVItemClickListener;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.http.HttpHelp;
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
 * Created by 李 on 2018/4/13.
 * <p>
 * 地址管理
 */

public class AddressManagementActivity extends BaseActivity implements OnItemChildClickListener, View.OnClickListener, OnRVItemClickListener {
    @BindView(R.id.rl_list)
    RecyclerView rlList;
    @BindView(R.id.tv_add)
    TextView tvAdd;
    @BindView(R.id.rl_empty)
    LinearLayout rlEmpty;

    private AddressManagementAdapter adapter;
    private PreferencesHelper helper;
    private List<AddressManagementListBean> listBeans = new ArrayList<>();

    private AddressDeleteDialog deleteDialog;
    private int type = 0;//0为从个人中心进入，1为从订单进入

    @Override
    public int getContentView() {
        return R.layout.activity_address_management;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setLeftImageRes(R.drawable.ic_back_gray);
        titleBar.setTitleText("地址管理");
        helper = new PreferencesHelper(this);

        type = getIntent().getIntExtra("type", 0);

        adapter = new AddressManagementAdapter(rlList);
        rlList.setLayoutManager(new LinearLayoutManager(this));
        rlList.setAdapter(adapter);
        adapter.setOnItemChildClickListener(this);
        adapter.setOnRVItemClickListener(this);
        tvAdd.setOnClickListener(this);
    }

    @Override
    public void getData() {
        getAddressList(helper.getToken());
    }

    //获取地址列表
    private void getAddressList(String token) {
        HttpHelp.getInstance().create(RemoteApi.class).getAddressList(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<List<AddressManagementListBean>>>(this, null) {
                    @Override
                    public void onNext(BaseBean<List<AddressManagementListBean>> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            listBeans = baseBean.data;
                            if (listBeans != null && listBeans.size() > 0) {
                                rlEmpty.setVisibility(View.GONE);
                                rlList.setVisibility(View.VISIBLE);
                                adapter.setData(listBeans);
                            } else {
                                adapter.clear();
                                rlEmpty.setVisibility(View.VISIBLE);
                                rlList.setVisibility(View.GONE);
                                ToastUtil.showToast(AddressManagementActivity.this, baseBean.message);
                            }
                        } else if (baseBean.code == 4) {
                            ToolUtil.goToLogin(AddressManagementActivity.this);
                        } else {
                            ToastUtil.showToast(AddressManagementActivity.this, baseBean.message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
    }

    //删除地址
    private void deleteAddress(final String token, int id) {
        HttpHelp.getInstance().create(RemoteApi.class).deleteAddressList(token, id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<String>>(this, null) {
                    @Override
                    public void onNext(BaseBean<String> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            ToastUtil.showToast(AddressManagementActivity.this, baseBean.message);
                        } else if (baseBean.code == 4) {
                            ToolUtil.goToLogin(AddressManagementActivity.this);
                        } else {
                            ToastUtil.showToast(AddressManagementActivity.this, baseBean.message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
    }

    //设置默认地址
    private void setDefault(String token, int id) {
        HttpHelp.getInstance().create(RemoteApi.class).setDefault(token, id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<Object>>(this, null) {
                    @Override
                    public void onNext(BaseBean<Object> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            if (type == 1) {
                                //下单时 进入
                                finish();
                            }
                        } else if (baseBean.code == 4) {
                            ToolUtil.goToLogin(AddressManagementActivity.this);
                        } else {

                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }
                });
    }

    //设置默认
    private void setFalse() {
        for (int i = 0; i < adapter.getData().size(); i++) {
            adapter.getData().get(i).setIs_default(0);
        }
    }

    @Override
    public void onItemChildClick(ViewGroup parent, View childView, final int position) {
        switch (childView.getId()) {
            case R.id.cb_default:
                setFalse();
                adapter.getData().get(position).setIs_default(1);
                adapter.notifyDataSetChanged();
                setDefault(helper.getToken(), adapter.getItem(position).getAddress_id());
                break;
            case R.id.tv_edit:
                Intent intent = new Intent(AddressManagementActivity.this, AddAddressActivity.class);
                intent.putExtra("ISADD", 1);
                intent.putExtra("ADDRESSID", adapter.getData().get(position).getAddress_id());
                startActivity(intent);
                break;
            case R.id.tv_delete:
                deleteDialog = new AddressDeleteDialog(this);
                deleteDialog.setGoToUseOnclickListener(new AddressDeleteDialog.onGoToUseOnclickListener() {
                    @Override
                    public void onUseClick() {
                        deleteAddress(helper.getToken(), adapter.getData().get(position).getAddress_id());
                        getAddressList(helper.getToken());
                        deleteDialog.cancel();
                    }
                });
                deleteDialog.show();
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_add:

                Intent intent = new Intent(AddressManagementActivity.this, AddAddressActivity.class);
                intent.putExtra("ISADD", 0);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAddressList(helper.getToken());
    }

    /**
     * 点击地址
     *
     * @param parent
     * @param itemView
     * @param position
     */
    @Override
    public void onRVItemClick(ViewGroup parent, View itemView, int position) {
        if (type == 1) {
            //设为默认
            setFalse();
            adapter.getData().get(position).setIs_default(1);
            adapter.notifyDataSetChanged();
            setDefault(helper.getToken(), adapter.getItem(position).getAddress_id());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
