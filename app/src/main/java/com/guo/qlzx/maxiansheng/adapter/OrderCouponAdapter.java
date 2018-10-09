package com.guo.qlzx.maxiansheng.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.guo.qlzx.maxiansheng.R;
import com.guo.qlzx.maxiansheng.bean.OrderCouponBean;
import com.guo.qlzx.maxiansheng.bean.OrderCouponListBean;
import com.qlzx.mylibrary.base.RecyclerViewAdapter;
import com.qlzx.mylibrary.base.ViewHolderHelper;

/**
 * Created by 李 on 2018/4/27.
 * 订单-使用优惠券
 */

public class OrderCouponAdapter extends RecyclerViewAdapter<OrderCouponBean> {
    public OrderCouponAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.item_order_coupon);
    }

    @Override
    protected void fillData(ViewHolderHelper viewHolderHelper, int position, OrderCouponBean model) {
        if ("1".equals(model.getSelected())){
            viewHolderHelper.setVisibility(R.id.tv_use, View.GONE);
            viewHolderHelper.setVisibility(R.id.tv_selected,View.VISIBLE);
            viewHolderHelper.getView(R.id.tv_selected).setEnabled(false);
        }else {
            viewHolderHelper.setVisibility(R.id.tv_selected, View.GONE);
            viewHolderHelper.setVisibility(R.id.tv_use,View.VISIBLE);
        }

        viewHolderHelper.setText(R.id.tv_price,"¥"+model.getMoney());
        if (!TextUtils.isEmpty(model.getCondition())){
            viewHolderHelper.setText(R.id.tv_condition,"满"+model.getCondition()+"元使用");
        }

        viewHolderHelper.setText(R.id.tv_title,model.getName());
        viewHolderHelper.setText(R.id.tv_content,"有效期"+model.getUse_start_time()+"至"+model.getUse_end_time());

        viewHolderHelper.setItemChildClickListener(R.id.tv_use);
    }
}
