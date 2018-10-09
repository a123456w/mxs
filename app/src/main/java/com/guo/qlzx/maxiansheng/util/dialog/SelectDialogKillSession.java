package com.guo.qlzx.maxiansheng.util.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.guo.qlzx.maxiansheng.R;
import com.guo.qlzx.maxiansheng.adapter.ServerListAdapter;
import com.guo.qlzx.maxiansheng.adapter.SpecAdapter;
import com.guo.qlzx.maxiansheng.bean.ServerBean;
import com.guo.qlzx.maxiansheng.bean.SpecDialogBean;
import com.guo.qlzx.maxiansheng.http.RemoteApi;
import com.guo.qlzx.maxiansheng.util.MeasureFlowLayoutManager;
import com.guo.qlzx.maxiansheng.util.SpaceItemDecoration;
import com.guo.qlzx.maxiansheng.util.ToolUtil;
import com.qlzx.mylibrary.base.BaseSubscriber;
import com.qlzx.mylibrary.base.OnRVItemClickListener;
import com.qlzx.mylibrary.bean.BaseBean;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.http.HttpHelp;
import com.qlzx.mylibrary.util.GlideUtil;
import com.qlzx.mylibrary.util.PreferencesHelper;
import com.qlzx.mylibrary.util.ToastUtil;

import java.util.ArrayList;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 李 on 2018/4/24.
 */

public class SelectDialogKillSession extends Dialog implements TextWatcher {
    private ImageView cancel;
    private TextView goToUse;
    private RecyclerView specGrid;
    private GridView serviceGrid;
    private ImageView ivImg;
    private TextView tvPrice;
    private TextView tvUnit;
    private TextView storeCount;
    private TextView tvChangeNumber;
    private TextView tvService;
    private TextView tvOneSpec;
    private TextView tvAdd;
    private TextView tvLess;
    private EditText edNumber;
    private TextView tvShopPrice;
    private TextView tvServiceHint;


    private SpecAdapter specAdapter;
    private ServerListAdapter serverListAdapter;


    private ServerBean bean;
    private SpecDialogBean specBean;
    private ArrayList<ServerBean.ServicePriceBean> serverList = new ArrayList<>();//用于显示的服务集合

    private String spec_id;
    private String spec_name = "";
    private String serviceId;
    private String serviceName = "";
    private int goodsNum = 1;


    private Context mContext;
    private onCancelOnclickListener cancelOnclickListener;//取消按钮被点击了的监听器
    private onGoToUseOnclickListener onGoToUseOnclickListener;//确定按钮被点击了的监听器

    private int type = 0;
    private int pos = 0;
    private int store_count;
    private LinearLayout lncount;

    public SelectDialogKillSession(Context context, int type) {
        super(context, R.style.PopUpBottomAnimation);
        this.mContext = context;
        this.type = type;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_select);
        //按空白处不能取消动画
        setCanceledOnTouchOutside(false);
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
        attributes.width = ViewGroup.LayoutParams.MATCH_PARENT;
        attributes.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        attributes.dimAmount = 0.5f;
        attributes.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        attributes.gravity = Gravity.BOTTOM;

        getWindow().setAttributes(attributes);

        //初始化界面控件
        initView();
        //初始化界面控件的事件
        initEvent();
    }


    /**
     * 初始化界面的确定和取消监听器
     */
    private void initEvent() {
        //设置确定按钮被点击后，向外界提供监听
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(spec_id)) {
                    ToastUtil.showToast(mContext, "请选择规格");
                    return;
                }
                if (cancelOnclickListener != null) {
                    cancelOnclickListener.onCancelClick(spec_id, spec_name, serviceId, serviceName, goodsNum);
                }
                dismiss();
            }
        });
        setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (TextUtils.isEmpty(spec_id)) {
                    ToastUtil.showToast(mContext, "请选择规格");
                    return;
                }
                if (cancelOnclickListener != null) {
                    cancelOnclickListener.onCancelClick(spec_id, spec_name, serviceId, serviceName, goodsNum);
                }
            }
        });
        //设置取消按钮被点击后，向外界提供监听
        goToUse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (store_count==0){
                    ToastUtil.showToast(getContext(),"库存为0份");
                    dismiss();
                  return;
                }
                if (TextUtils.isEmpty(new PreferencesHelper(mContext).getToken())) {
                    ToolUtil.goToLogin(mContext);
                    return;
                } else {
                    if (TextUtils.isEmpty(spec_id)) {
                        ToastUtil.showToast(mContext, "请选择规格");
                        return;
                    }
                    if (onGoToUseOnclickListener != null) {
                        onGoToUseOnclickListener.onUseClick(String.valueOf(specBean.getGoods_id()), spec_id, serviceId, goodsNum);
                    }
                    dismiss();
                }
            }
        });

        tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int store_count = bean.getSpeclist().get(pos).getStore_count();
                if(store_count ==0){
                    return;
                }

                edNumber.removeTextChangedListener(SelectDialogKillSession.this);
                int i = Integer.parseInt(edNumber.getText().toString());
                if (i < bean.getSpeclist().get(pos).getStore_count()) {
                    i++;
                }
                edNumber.setText(String.valueOf(i));
                tvChangeNumber.setText("已选：" + i + "份");
                goodsNum = i;
            }
        });
        edNumber.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int store_count = bean.getSpeclist().get(pos).getStore_count();
                if(store_count !=0){

                    edNumber.addTextChangedListener(SelectDialogKillSession.this);
                }else {
                    edNumber.removeTextChangedListener(SelectDialogKillSession.this);
                    edNumber.setEnabled(false);
                }
                return false;

            }
        });
        edNumber.setCursorVisible(false);
        tvLess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edNumber.removeTextChangedListener(SelectDialogKillSession.this);
                int i = Integer.parseInt(edNumber.getText().toString());
                if (i > 1) {
                    i--;
                }
                edNumber.setText(String.valueOf(i));
                tvChangeNumber.setText("已选：" + i + "份");
                goodsNum = i;
            }
        });
        specAdapter.setOnRVItemClickListener(new OnRVItemClickListener() {
            @Override
            public void onRVItemClick(ViewGroup parent, View itemView, int position) {
                spec_id = specAdapter.getData().get(position).getKey();
                spec_name = specAdapter.getData().get(position).getKey_name();
                selector = serverListAdapter.getSelector();
                selectServer(spec_id);
                pos = position;
                storeCount.setText("库存：" + bean.getSpeclist().get(position).getStore_count() + "份");
                if (new PreferencesHelper(mContext).getVip() == 2) {
                    tvPrice.setText("¥" + bean.getSpeclist().get(position).getPlus_price());
                    tvShopPrice.setText("¥" + bean.getSpeclist().get(position).getShop_price());
                } else {
                    if (type == 0) {
                        tvPrice.setText("¥" + bean.getSpeclist().get(position).getShop_price());
                    } else {
                        tvPrice.setText("¥" + bean.getSpeclist().get(position).getPlus_price());
                    }

                }
                Log.i("TAG", "type" + type);
                tvService.setVisibility(View.GONE);
                for (int i = 0; i < serverList.size(); i++) {
                    if (serverList.get(i).isSelected()) {
                        tvService.setVisibility(View.VISIBLE);
                        if (new PreferencesHelper(mContext).getVip() == 2) {
                            serviceName = serverList.get(i).getService_name() + "：" + serverList.get(i).getPlus_price();
                            tvService.setText("已选增值服务：" + serverList.get(i).getService_name() + "：" + serverList.get(i).getPlus_price());
                        } else {
                            serviceName = serverList.get(i).getService_name() + "：" + serverList.get(i).getShop_price();
                            tvService.setText("已选增值服务：" + serverList.get(i).getService_name() + "：" + serverList.get(i).getShop_price());
                        }
                    } else {

                    }
                }

                if (bean.getSpeclist().get(position).isSelected()) {
                    bean.getSpeclist().get(position).setSelected(false);
                    spec_id = "";
                    spec_name = "";
                } else {
                    for (int j = 0; j < bean.getSpeclist().size(); j++) {
                        bean.getSpeclist().get(j).setSelected(false);
                    }
                    bean.getSpeclist().get(position).setSelected(true);
                    spec_id = bean.getSpeclist().get(position).getKey();
                    spec_name = bean.getSpeclist().get(position).getKey_name();
                }
                if (Integer.valueOf(edNumber.getText().toString()) > bean.getSpeclist().get(position).getStore_count()) {
                    edNumber.setText("" + bean.getSpeclist().get(position).getStore_count());
                }
                specAdapter.notifyDataSetChanged();
            }

        });


        serviceGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (serverList.get(i).isSelected()) {
                    serviceId = "";
                    serviceName = "";
                    serverList.get(i).setSelected(false);
                    tvService.setText("已选增值服务：");
                    serverListAdapter.setSelector(-1);
                } else {
                    tvService.setVisibility(View.VISIBLE);
                    for (int j = 0; j < serverList.size(); j++) {
                        serverList.get(j).setSelected(false);
                    }
                    serverListAdapter.setSelector(i);
                    serverList.get(i).setSelected(true);
                    serviceId = String.valueOf(serverList.get(i).getService_id());
                    if (new PreferencesHelper(mContext).getVip() == 2) {
                        serviceName = serverList.get(i).getService_name() + "：" + serverList.get(i).getPlus_price();
                        tvService.setText("已选增值服务：" + serverList.get(i).getService_name() + "：" + serverList.get(i).getPlus_price());
                    } else {
                        serviceName = serverList.get(i).getService_name() + "：" + serverList.get(i).getShop_price();
                        tvService.setText("已选增值服务：" + serverList.get(i).getService_name() + "：" + serverList.get(i).getShop_price());
                    }

                }

                serverListAdapter.notifyDataSetChanged();
            }
        });
    }

    private int selector = -1;

    /**
     * 初始化界面控件
     */
    private void initView() {
        cancel = (ImageView) findViewById(R.id.im_cancel);
        goToUse = (TextView) findViewById(R.id.btn_submit);
        // 所有的类型都有确定按钮。
/*        if (type!=0){
            goToUse.setVisibility(View.GONE);
        }else {
            goToUse.setVisibility(View.VISIBLE);
        }*/
        lncount = findViewById(R.id.ln_count);
        ivImg = findViewById(R.id.iv_img);
        tvPrice = findViewById(R.id.tv_price);
        tvUnit = findViewById(R.id.tv_unit);
        storeCount = findViewById(R.id.store_count);
        tvChangeNumber = findViewById(R.id.tv_change_number);
        tvService = findViewById(R.id.tv_service);
        tvOneSpec = findViewById(R.id.tv_one_spec);
        tvAdd = findViewById(R.id.tv_add);
        tvLess = findViewById(R.id.tv_less);
        edNumber = findViewById(R.id.ed_number);

        tvShopPrice = findViewById(R.id.tv_shop_price);
        tvServiceHint = findViewById(R.id.tv_service_hint);

        serviceGrid = findViewById(R.id.grid_service);
        serverListAdapter = new ServerListAdapter(serviceGrid);
        serviceGrid.setAdapter(serverListAdapter);

        specGrid = findViewById(R.id.rl_spec);
        specAdapter = new SpecAdapter(specGrid);
        LinearLayout.LayoutParams lp1 = (LinearLayout.LayoutParams) specGrid.getLayoutParams();
        lp1.width = mContext.getResources().getDisplayMetrics().widthPixels;
        MeasureFlowLayoutManager flowLayoutManager = new MeasureFlowLayoutManager(mContext);
        specGrid.addItemDecoration(new SpaceItemDecoration((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, mContext.getResources().getDisplayMetrics())));
        specGrid.setLayoutManager(new GridLayoutManager(mContext, 4));
        specGrid.setAdapter(specAdapter);

    }


    /**
     * 设置规格和服务数据
     */
    public void loadDataWithGoodsId(String goods_id) {
        HttpHelp.getInstance().create(RemoteApi.class).getServer(goods_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<BaseBean<ServerBean>>(mContext, null) {
                    @Override
                    public void onNext(BaseBean<ServerBean> baseBean) {
                        super.onNext(baseBean);
                        if (baseBean.code == 0) {
                            bean = baseBean.data;
                            int pos = 0;
                            if (spec_id != null && !"".equals(spec_id)) {
                                for (int i = 0; i < bean.getSpeclist().size(); i++) {
                                    if (spec_id.equals(bean.getSpeclist().get(i).getKey())) {
                                        pos = i;
                                        break;
                                    }
                                }
                            }
                            if (bean.getSpeclist().size() == 0) {
                                spec_id = "";
                                spec_name = specBean.getSpec_name();
                                specGrid.setVisibility(View.GONE);
                                tvOneSpec.setVisibility(View.VISIBLE);
                                tvOneSpec.setText(spec_name);
                                selectServer(spec_id);

                                tvUnit.setText("/" + bean.getUnit());
                                tvChangeNumber.setText("已选：1份");
                                if (new PreferencesHelper(mContext).getVip() == 2) {
                                    tvPrice.setText("¥" + specBean.getPlus_price());
                                    tvShopPrice.setText("¥" + specBean.getShop_price());
                                } else {
                                    if (type == 0) {
                                        tvPrice.setText("¥" + bean.getSpeclist().get(0).getShop_price());
                                    } else {
                                        tvPrice.setText("¥" + bean.getSpeclist().get(0).getPlus_price());
                                    }
                                }

                                return;
                            } /*else if (bean.getSpeclist().size() == 1) {
                                spec_id = bean.getSpeclist().get(0).getKey();
                                spec_name = bean.getSpeclist().get(0).getKey_name();
                                specGrid.setVisibility(View.GONE);
                                tvOneSpec.setVisibility(View.VISIBLE);
                                tvOneSpec.setText(spec_name);
                                selectServer(spec_id);
                            }*/ else {
                                specGrid.setVisibility(View.VISIBLE);
                                tvOneSpec.setVisibility(View.GONE);
                                spec_id = bean.getSpeclist().get(pos).getKey();
                                spec_name = bean.getSpeclist().get(pos).getKey_name();
                                bean.getSpeclist().get(pos).setSelected(true);
                                specAdapter.setData(bean.getSpeclist());
                                selectServer(spec_id);
                            }


                            if (new PreferencesHelper(mContext).getVip() == 2) {
                                tvPrice.setText("¥" + bean.getSpeclist().get(pos).getPlus_price());
                                tvShopPrice.setText("¥" + bean.getSpeclist().get(pos).getShop_price());
                            } else {
                                if (type == 0) {
                                    tvPrice.setText("¥" + bean.getSpeclist().get(pos).getShop_price());
                                } else {
                                    tvPrice.setText("¥" + bean.getSpeclist().get(pos).getPlus_price());
                                }
                            }
                            tvUnit.setText("/" + bean.getUnit());
                            store_count = bean.getSpeclist().get(pos).getStore_count();
                            storeCount.setText("库存：" +  store_count+ "份");

                            tvChangeNumber.setText("已选：" + goodsNum + "份");
                        } else {
                            ToastUtil.showToast(mContext, baseBean.message);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);

                    }
                });
    }

    public void setData(SpecDialogBean specBean) {
        this.specBean = specBean;
        GlideUtil.display(mContext, Constants.IMG_HOST + specBean.getImg(), ivImg);
        tvPrice.setText(specBean.getPlus_price() + "");
        Log.i("TAG", specBean.getPlus_price() + "0.0");
        loadDataWithGoodsId(String.valueOf(specBean.getGoods_id()));
    }

    public void setServiceId(String specId, String serviceId, int goodsNum) {
        this.spec_id = specId;
        this.serviceId = serviceId;
        this.goodsNum = goodsNum;
        edNumber.setText(String.valueOf(goodsNum) + "");
    }

    /**
     * 设置取消按钮的显示内容和监听
     */
    public void setCancelOnclickListener(onCancelOnclickListener onNoOnclickListener) {
        this.cancelOnclickListener = onNoOnclickListener;
    }

    /**
     * 设置去使用按钮的显示内容和监听
     */
    public void setGoToUseOnclickListener(onGoToUseOnclickListener onYesOnclickListener) {
        this.onGoToUseOnclickListener = onYesOnclickListener;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

        int store_count = bean.getSpeclist().get(pos).getStore_count();
        if(store_count ==0){

            return;
        }

        if (bean == null) {
            return;
        }
        int i;
        if (null == s.toString() || "".equals(s.toString())) {
            i = 1;
            edNumber.setTag(false);
        } else {
            i = Integer.parseInt(s.toString());
        }
        edNumber.setSelection(edNumber.getText().toString().length());
        if (i > bean.getSpeclist().get(pos).getStore_count()) {
            ToastUtil.showToast(getContext(), "不可超过最大库存");
            i = bean.getSpeclist().get(pos).getStore_count();
            edNumber.setTag(false);
        } else if (i < 1) {
            i = 1;
            ToastUtil.showToast(getContext(), "不可小于一件");
            edNumber.setTag(false);
        }
        if (edNumber.getTag() != null && !(Boolean) edNumber.getTag()) {
            edNumber.setTag(null);
            edNumber.setText(String.valueOf(i) + "");
        }


        tvChangeNumber.setText("已选：" + i + "份");
        goodsNum = i;
    }

    /**
     * 设置确定按钮和取消被点击的接口
     */
    public interface onCancelOnclickListener {
        public void onCancelClick(String spec_id, String spec_name, String service_id, String service_name, int num);
    }

    public interface onGoToUseOnclickListener {
        public void onUseClick(String goods_id, String spec_id, String service_id, int num);
    }

    private void selectServer(String specId) {
        serverList.clear();
        if (bean.getService_price() == null || bean.getService_price().size() == 0) {
            tvServiceHint.setVisibility(View.GONE);
        }
        if ("".equals(specId)) {
            for (int i = 0; i < bean.getService_price().size(); i++) {
                serverList.add(bean.getService_price().get(i));
            }
        } else {
            for (int i = 0; i < bean.getService_price().size(); i++) {
                if (specId.equals(bean.getService_price().get(i).getKey())) {
                    serverList.add(bean.getService_price().get(i));
                }
            }
        }
        if (selector != -1) {
            for (int i = 0; i < serverList.size(); i++) {
                if (i == selector) {
                    serverList.get(i).setSelected(true);
                } else {
                    serverList.get(i).setSelected(false);
                }
            }

        }
        serverListAdapter.setData(serverList);
    }
}
