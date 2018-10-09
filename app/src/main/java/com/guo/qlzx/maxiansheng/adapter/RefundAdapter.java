package com.guo.qlzx.maxiansheng.adapter;

import android.support.v7.widget.RecyclerView;

import com.guo.qlzx.maxiansheng.R;
import com.guo.qlzx.maxiansheng.bean.ForwardBean;
import com.guo.qlzx.maxiansheng.bean.RefundBean;
import com.guo.qlzx.maxiansheng.util.ToolUtil;
import com.qlzx.mylibrary.base.RecyclerViewAdapter;
import com.qlzx.mylibrary.base.ViewHolderHelper;

/**
 * Created by 李 on 2018/4/18.
 */

public class RefundAdapter extends RecyclerViewAdapter<RefundBean> {
    public RefundAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.item_balance);
    }

    @Override
    protected void fillData(ViewHolderHelper viewHolderHelper, int position, RefundBean model) {

   if("0".equals(model.getReturn_status())){
            viewHolderHelper.setText(R.id.tv_title, "退款中...");
            viewHolderHelper.getTextView(R.id.tv_price).setTextColor(mContext.getResources().getColor(R.color.redColor));
            viewHolderHelper.setText(R.id.tv_price, "+ ¥" + model.getRefund());
        }else if("1".equals(model.getReturn_status())){
            viewHolderHelper.setText(R.id.tv_title, "退款完成");
            viewHolderHelper.getTextView(R.id.tv_price).setTextColor(mContext.getResources().getColor(R.color.redColor));
            viewHolderHelper.setText(R.id.tv_price, "+ ¥" + model.getRefund());
        } else if("2".equals(model.getReturn_status())){
            viewHolderHelper.setText(R.id.tv_title, "退款失败");
            viewHolderHelper.getTextView(R.id.tv_price).setTextColor(mContext.getResources().getColor(R.color.text_color3));
            viewHolderHelper.setText(R.id.tv_price, "+ ¥" + model.getRefund());
        }
        viewHolderHelper.setText(R.id.tv_time, ToolUtil.getStrTime(model.getRefundtime(), "yyyy-MM-dd HH:mm:ss"));
    }
}
