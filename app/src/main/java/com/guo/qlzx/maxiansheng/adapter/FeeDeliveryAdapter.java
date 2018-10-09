package com.guo.qlzx.maxiansheng.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.guo.qlzx.maxiansheng.R;
import com.guo.qlzx.maxiansheng.bean.LogisticsBean;
import com.guo.qlzx.maxiansheng.util.ToolUtil;
import com.qlzx.mylibrary.base.RecyclerViewAdapter;
import com.qlzx.mylibrary.base.ViewHolderHelper;

/**
 * Created by 李 on 2018/6/5.
 */

public class FeeDeliveryAdapter extends RecyclerViewAdapter<LogisticsBean.OrderStatusInfoBean>{
    public FeeDeliveryAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.item_fee_delivery);
    }

    @Override
    protected void fillData(ViewHolderHelper viewHolderHelper, int position, LogisticsBean.OrderStatusInfoBean model) {
        viewHolderHelper.getTextView(R.id.tv_state).setText(model.getOrder_status());
        viewHolderHelper.setText(R.id.tv_day, ToolUtil.getDataString(model.getTime()));
        viewHolderHelper.setText(R.id.tv_time,ToolUtil.getStrTime(model.getTime(),"HH:mm"));
        if (mData!=null&&mData.size()==1){
            //只有一个
            viewHolderHelper.getView(R.id.v_up).setVisibility(View.INVISIBLE);
            viewHolderHelper.getView(R.id.v_down).setVisibility(View.INVISIBLE);
            viewHolderHelper.getTextView(R.id.tv_day).setTextColor(mContext.getResources().getColor(R.color.blue_15));
            viewHolderHelper.getTextView(R.id.tv_time).setTextColor(mContext.getResources().getColor(R.color.blue_15));
            viewHolderHelper.getTextView(R.id.tv_state).setTextColor(mContext.getResources().getColor(R.color.blue_15));
            viewHolderHelper.getImageView(R.id.iv_center).setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_down_blue));
        }else if (mData!=null&& mData.size()-1 == position){
            //最后一个
            viewHolderHelper.getView(R.id.v_up).setVisibility(View.VISIBLE);
            viewHolderHelper.getView(R.id.v_down).setVisibility(View.INVISIBLE);
            viewHolderHelper.getTextView(R.id.tv_day).setTextColor(mContext.getResources().getColor(R.color.text_color6));
            viewHolderHelper.getTextView(R.id.tv_time).setTextColor(mContext.getResources().getColor(R.color.text_color6));
            viewHolderHelper.getTextView(R.id.tv_state).setTextColor(mContext.getResources().getColor(R.color.text_color6));
            viewHolderHelper.getImageView(R.id.iv_center).setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_up_gray));
        }else if (mData!=null&& 0==position){
            //第一个
            viewHolderHelper.getView(R.id.v_up).setVisibility(View.INVISIBLE);
            viewHolderHelper.getView(R.id.v_down).setVisibility(View.VISIBLE);
            viewHolderHelper.getTextView(R.id.tv_day).setTextColor(mContext.getResources().getColor(R.color.blue_15));
            viewHolderHelper.getTextView(R.id.tv_time).setTextColor(mContext.getResources().getColor(R.color.blue_15));
            viewHolderHelper.getTextView(R.id.tv_state).setTextColor(mContext.getResources().getColor(R.color.blue_15));
            viewHolderHelper.getImageView(R.id.iv_center).setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_down_blue));
        }else {
            viewHolderHelper.getView(R.id.v_up).setVisibility(View.VISIBLE);
            viewHolderHelper.getView(R.id.v_down).setVisibility(View.VISIBLE);
            viewHolderHelper.getTextView(R.id.tv_day).setTextColor(mContext.getResources().getColor(R.color.text_color6));
            viewHolderHelper.getTextView(R.id.tv_time).setTextColor(mContext.getResources().getColor(R.color.text_color6));
            viewHolderHelper.getTextView(R.id.tv_state).setTextColor(mContext.getResources().getColor(R.color.text_color6));
            viewHolderHelper.getImageView(R.id.iv_center).setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_up_gray));
        }
    }
}
