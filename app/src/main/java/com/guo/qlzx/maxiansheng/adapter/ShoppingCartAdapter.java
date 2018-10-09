package com.guo.qlzx.maxiansheng.adapter;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.guo.qlzx.maxiansheng.R;
import com.guo.qlzx.maxiansheng.bean.CartListBean;
import com.guo.qlzx.maxiansheng.util.SoftKeyBoardListener;
import com.guo.qlzx.maxiansheng.view.TextEditTextView;
import com.qlzx.mylibrary.base.BaseListAdapter;
import com.qlzx.mylibrary.base.RecyclerViewAdapter;
import com.qlzx.mylibrary.base.ViewHolderHelper;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.util.GlideUtil;
import com.qlzx.mylibrary.util.ToastUtil;

import java.text.DecimalFormat;

/**
 * Created by Administrator on 2018/4/17.
 */

public class ShoppingCartAdapter extends RecyclerViewAdapter<CartListBean.ShowcartListBean> {

    private float increment;
    private float unit_price;
    UpDataNum upDataNum;
    RecyclerView recyclerView;
    public ShoppingCartAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.item_shopping_cart);
        this.recyclerView = recyclerView;
    }


    @Override
    protected void fillData(final ViewHolderHelper helper, final int position, final CartListBean.ShowcartListBean model) {
        increment = 0;
        unit_price = 1;
        helper.setText(R.id.goods_content, model.getGoods_num() + "");
        ((EditText) helper.getView(R.id.goods_content)).setSelection(((EditText) helper.getView(R.id.goods_content)).getText().toString().length());
        helper.setText(R.id.goods_name, model.getGoods_name());
        helper.setText(R.id.goods_spec, "规格:" + model.getSpec_key_name() + "/" + model.getUnit());
        final CheckBox id_cb_select_child = helper.getView(R.id.id_cb_select_child);
        if (model.getSelected() == 1) {
            id_cb_select_child.setChecked(true);
        } else {
            id_cb_select_child.setChecked(false);
        }
        final TextWatcher textWatcher =  new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int i;
                if (null == s.toString() || "".equals(s.toString())) {
                    return;
                } else {
                    i = Integer.parseInt(s.toString());
                }
                ((EditText) helper.getView(R.id.goods_content)).setSelection(((EditText) helper.getView(R.id.goods_content)).getText().toString().length());
                if (i > model.getStore_count()) {
                    ToastUtil.showToast(mContext, "不可超过最大库存");
                    i = model.getStore_count();
                    ((EditText) helper.getView(R.id.goods_content)).setTag(false);
                }

                if (((EditText) helper.getView(R.id.goods_content)).getTag() != null && !(Boolean) ((EditText) helper.getView(R.id.goods_content)).getTag()) {
                    ((EditText) helper.getView(R.id.goods_content)).setTag(null);
                    ((EditText) helper.getView(R.id.goods_content)).setText(String.valueOf(i) + "");
                }
                if(upDataNum != null){
                    upDataNum.getNum(i,position);
                }
            }
        };

        SoftKeyBoardListener.setListener((Activity) mContext, new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
            }

            @Override
            public void keyBoardHide(int height) {
                if (((EditText) helper.getView(R.id.goods_content)).getText().toString().equals("") ||
                        Integer.parseInt(((EditText) helper.getView(R.id.goods_content)).getText().toString()) <= 0) {
                    ((EditText) helper.getView(R.id.goods_content)).setText(1 + "");
                }
            }
        });
        (helper.getView(R.id.goods_content)).setOnFocusChangeListener(new android.view.View.
                OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 此处为得到焦点时的处理内容
                    ((TextEditTextView) helper.getView(R.id.goods_content)).addTextChangedListener(textWatcher);
                    upDataNum.getPosition(position);
                } else {
                    // 此处为失去焦点时的处理内容
                    if (((EditText) helper.getView(R.id.goods_content)).getText().toString().equals("") ||
                            Integer.parseInt(((EditText) helper.getView(R.id.goods_content)).getText().toString()) <= 0) {
                        ((EditText) helper.getView(R.id.goods_content)).setText(1 + "");
                    }
                    ((TextEditTextView) helper.getView(R.id.goods_content)).removeTextChangedListener(textWatcher);
                }
            }
        });
        ((TextEditTextView) helper.getView(R.id.goods_content)).setOnKeyBoardHideListener(new TextEditTextView.OnKeyBoardHideListener() {
            @Override
            public void onKeyHide(int keyCode, KeyEvent event) {
                if (((EditText) helper.getView(R.id.goods_content)).getText().toString().equals("") ||
                        Integer.parseInt(((EditText) helper.getView(R.id.goods_content)).getText().toString()) <= 0) {
                    ((EditText) helper.getView(R.id.goods_content)).setText(1 + "");
                }
            }
        });

        if (model.getService_id() == 0) {
            helper.getView(R.id.value_added_service).setVisibility(View.GONE);
        } else {
            helper.getView(R.id.value_added_service).setVisibility(View.VISIBLE);
            helper.setText(R.id.tv_service, model.getService_name() + "：" + model.getService_price() + "元");
            increment = Float.parseFloat(model.getService_price());
        }

        if (!TextUtils.isEmpty(model.getMember_goods_price())) {

            if (model.getProm_type() == 0) {
                helper.setText(R.id.goods_price, "会员价：¥" + model.getMember_goods_price() + "/" + model.getUnit());
            } else if (model.getProm_type() == 1) {
                helper.setText(R.id.goods_price, "秒杀价：¥" + model.getMember_goods_price() + "/" + model.getUnit());
            } else if (model.getProm_type() == 2) {
                helper.setText(R.id.goods_price, "团购价：¥" + model.getMember_goods_price() + "/" + model.getUnit());
            }

            unit_price = Float.parseFloat(model.getMember_goods_price());

        } else {

            helper.setText(R.id.goods_price, "¥" + model.getGoods_price() + "/" + model.getUnit());

            unit_price = Float.parseFloat(model.getGoods_price());

        }

        float zv = increment * model.getGoods_num() + unit_price * model.getGoods_num();
        DecimalFormat decimalFormat = new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
        String resultString = decimalFormat.format(zv);//format 返回的是字符串
        helper.setText(R.id.goods_total_price, "¥" + resultString);
        GlideUtil.display(mContext, Constants.IMG_HOST + model.getImg(), helper.getImageView(R.id.goods_pic));

        helper.setItemChildClickListener(R.id.goods_add);
        helper.setItemChildClickListener(R.id.goods_reduce);
        helper.setItemChildClickListener(R.id.id_cb_select_child);

        helper.setItemChildClickListener(R.id.btn_delete);
        helper.setItemChildClickListener(R.id.rl_goodstotal);
        helper.setItemChildClickListener(R.id.value_added_service);
        helper.setItemChildClickListener(R.id.ll__item_shopcar);
    }
    public void setUpDataNum(UpDataNum upDataNum){
        this.upDataNum = upDataNum;
    }
    public interface UpDataNum{
        void getNum(int num,int position);
        void getPosition(int position);
    }
}
