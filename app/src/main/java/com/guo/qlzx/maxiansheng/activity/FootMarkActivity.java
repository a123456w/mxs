package com.guo.qlzx.maxiansheng.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.guo.qlzx.maxiansheng.R;
import com.guo.qlzx.maxiansheng.adapter.FootMarkAdapter;
import com.guo.qlzx.maxiansheng.bean.FootListBean;
import com.guo.qlzx.maxiansheng.bean.FootMarkListBean;
import com.guo.qlzx.maxiansheng.bean.SpecDialogBean;
import com.guo.qlzx.maxiansheng.event.ShoppingCartEvent;
import com.guo.qlzx.maxiansheng.http.RemoteApi;
import com.guo.qlzx.maxiansheng.util.ToolUtil;
import com.guo.qlzx.maxiansheng.util.dialog.SelectDialog;
import com.qlzx.mylibrary.base.BaseActivity;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.EventBusUtil;
import com.qlzx.mylibrary.util.LogUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.ToastUtil;
import com.qlzx.mylibrary.widget.LoadingLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 李 on 2018/4/17.
 *  我的足迹
 */

public class FootMarkActivity extends BaseActivity  implements
        FootMarkAdapter.OnItemChildClickListener ,ExpandableListView.OnGroupClickListener,View.OnClickListener, FootMarkAdapter.OnAddGoodsClickListener {
    @BindView(R.id.can_content_view)
    ExpandableListView canContentView;
    @BindView(R.id.rl_empty)
    RelativeLayout rlEmpty;
    @BindView(R.id.tv_skip)
    TextView tvSkip;
    private PreferencesHelper helper;
    private FootMarkAdapter adapter;
    private List<FootMarkListBean> listBeans=new ArrayList<>();
    @Override
    public int getContentView() {
        return R.layout.activity_foot_mark;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        loadingLayout.setStatus(LoadingLayout.Success);
        titleBar.setLeftImageRes(R.drawable.ic_back_gray);
        titleBar.setTitleText("我的足迹");
        titleBar.setRightTextRes("删除");
        titleBar.setRightTextClick(this);
        helper=new PreferencesHelper(this);

        adapter = new FootMarkAdapter(this);
        canContentView.setAdapter(adapter);
        adapter.setOnItemChildClickListener(this);
        canContentView.setGroupIndicator(null); // 去掉默认带的箭头
        canContentView.setSelection(0);// 设置默认选中项

        canContentView.setOnGroupClickListener(this);
        adapter.setOnAddGoodsClickListener(this);
        tvSkip.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG );
        tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(FootMarkActivity.this, MainActivity.class);
                intent1.putExtra("go_home", 2);
                startActivity(intent1);
            }
        });
    }

    @Override
    public void getData() {
        showLoadingDialog("加载中...");
        getFootMark(new PreferencesHelper(FootMarkActivity.this).getToken());
    }
    //我的足迹 列表数据
    private void getFootMark(String token){
        HttpHelp.getInstance().create(RemoteApi.class).getMyFootMark(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<List<FootMarkListBean>>>(this, null) {
                    @Override
                    public void onNext(BaseBean<List<FootMarkListBean>> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            listBeans=baseBean.data;
                            adapter.setData(listBeans);
                            // 遍历所有group,将所有项设置成默认展开
                            int groupCount = adapter.getGroupCount();
                            for (int i = 0; i < groupCount; i++) {
                                canContentView.expandGroup(i);
                            }
                            if (listBeans.size()==0) {
                                rlEmpty.setVisibility(View.VISIBLE);
                                canContentView.setVisibility(View.GONE);
                            }else {
                                rlEmpty.setVisibility(View.GONE);
                                canContentView.setVisibility(View.VISIBLE);
                            }
                        }else if (baseBean.code==4){
                           ToolUtil.goToLogin(FootMarkActivity.this);
                            rlEmpty.setVisibility(View.VISIBLE);
                            canContentView.setVisibility(View.GONE);
                        } else {
                            ToastUtil.showToast(FootMarkActivity.this, baseBean.message);
                            rlEmpty.setVisibility(View.VISIBLE);
                            canContentView.setVisibility(View.GONE);
                        }
                        hideLoadingDialog();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        rlEmpty.setVisibility(View.VISIBLE);
                        canContentView.setVisibility(View.GONE);
                        hideLoadingDialog();
                    }
                });
    }
    //删除足迹
    private void deleteFootMark(String token,String id){
        HttpHelp.getInstance().create(RemoteApi.class).deleteMyFootMark(token,id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean>(this, null) {
                    @Override
                    public void onNext(BaseBean baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            ToastUtil.showToast(FootMarkActivity.this, baseBean.message);
                            getFootMark(helper.getToken());
                            adapter.notifyDataSetChanged();
                        }else if (baseBean.code==4){
                            ToolUtil.goToLogin(FootMarkActivity.this);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        ToastUtil.showToast(FootMarkActivity.this, "删除失败");
                    }
                });
    }


    @Override
    public void onItemChildClick(View view, int groupPosition, int childPosition) {
        //点击子标题
        Intent intent=new Intent(FootMarkActivity.this,ProductDetailsActivity.class);
        intent.putExtra("goods_id",listBeans.get(groupPosition).getFootlist().get(childPosition).getGoods_id()+"");
        startActivity(intent);
    }

    @Override
    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
        //返回true则父item不可点击，否则点击父item字布局会折叠
        //点击父标题
        return true;
    }

    @Override
    public void onClick(View v) {
     //删除
        List<String> data=new ArrayList();
        Map<Integer,Map<Integer,Boolean>> map=adapter.getMap();
        int count=0;
        for (int i=0;i<map.size();i++){
            for (int j=0;j<map.get(i).size();j++){
                Map<Integer,Boolean> map1=map.get(i);
                if (map1.size()>0&&map1!=null){
                    if (map1.get(j)){
                        count++;
                        data.add(listBeans.get(i).getFootlist().get(j).getId()+"");
                    }else {

                    }
                }
            }
        }
        if (data.size()==0){
            ToastUtil.showToast(FootMarkActivity.this,"未选中商品");
            return;
        }
        deleteFootMark(helper.getToken(), ToolUtil.listToString(data));
    }


    @Override
    public void onAddGoods(int groupPos, int childPos) {
        FootListBean bean=listBeans.get(groupPos).getFootlist().get(childPos);
        showAddDialog(bean);
    }
    private SelectDialog cartDialog;
    private void showAddDialog(FootListBean bean) {
        if (bean.getSpec_type() == 0) {
            //不存在规格
            addShoppingCart(helper.getToken(), String.valueOf(bean.getGoods_id()), "", "", 1);
        } else {
            //存在规格 选择规格
            cartDialog = new SelectDialog(FootMarkActivity.this,bean.getType());
            SpecDialogBean specDialogBean = new SpecDialogBean();
            specDialogBean.setGoods_id(bean.getGoods_id());
            specDialogBean.setImg(bean.getImg());
            specDialogBean.setSpec_name(bean.getKey_name());
            specDialogBean.setPlus_price(Double.valueOf(bean.getPlus_price()));
            specDialogBean.setShop_price(Double.valueOf(bean.getShop_price()));
            cartDialog.setGoToUseOnclickListener(new SelectDialog.onGoToUseOnclickListener() {
                @Override
                public void onUseClick(String goods_id, String spec_id, String service_id, int num,int s
                        ,double price,double vipPrice) {
                    if(s == 0){
                        ToastUtil.showToast(FootMarkActivity.this,"当前商品库存为0份");
                        return;
                    }
                    addShoppingCart(helper.getToken(), goods_id, service_id, spec_id, num);
                }
            });
            cartDialog.show();
            cartDialog.setData(specDialogBean);
        }
    }


    /**
     * 加入购物车
     *
     * @param token
     * @param goods_id
     * @param service_id
     * @param spec_id
     * @param goods_num
     */
    private void addShoppingCart(String token, String goods_id, String service_id, String spec_id, int goods_num) {
        HttpHelp.getInstance().create(RemoteApi.class).addCart(token, goods_id, service_id, spec_id, goods_num)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<Object>>(FootMarkActivity.this, null) {
                    @Override
                    public void onNext(BaseBean<Object> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            EventBusUtil.post(new ShoppingCartEvent());
                            ToastUtil.showToast(FootMarkActivity.this, baseBean.message);
                        }else if (baseBean.code==4){
                            ToolUtil.goToLogin(FootMarkActivity.this);
                        } else {
                            ToastUtil.showToast(FootMarkActivity.this, baseBean.message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);

                    }
                });
    }


}
