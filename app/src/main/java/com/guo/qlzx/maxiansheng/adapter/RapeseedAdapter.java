package com.guo.qlzx.maxiansheng.adapter;

import android.support.v7.widget.RecyclerView;

import com.guo.qlzx.maxiansheng.R;
import com.guo.qlzx.maxiansheng.bean.BalanceListBean;
import com.guo.qlzx.maxiansheng.util.ToolUtil;
import com.qlzx.mylibrary.base.RecyclerViewAdapter;
import com.qlzx.mylibrary.base.ViewHolderHelper;

/**
 * Created by 李 on 2018/4/18.
 */

public class RapeseedAdapter extends RecyclerViewAdapter<BalanceListBean> {
    private int type=0;
    public RapeseedAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.item_balance);
    }

    public void setType(int type){
        this.type=type;
    }
    @Override
    protected void fillData(ViewHolderHelper viewHolderHelper, int position, BalanceListBean model) {
        if (type == 0) {
            if ("1".equals(model.getStatus())) {
                viewHolderHelper.setText(R.id.tv_title, model.getType_name());
                viewHolderHelper.getTextView(R.id.tv_price).setTextColor(mContext.getResources().getColor(R.color.redColor));
                viewHolderHelper.setText(R.id.tv_price, "- ¥" + model.getPrice());
            } else {
                viewHolderHelper.setText(R.id.tv_title,  model.getType_name());
                viewHolderHelper.getTextView(R.id.tv_price).setTextColor(mContext.getResources().getColor(R.color.text_color3));
                viewHolderHelper.setText(R.id.tv_price, "+ ¥" + model.getPrice());
            }
        }else {
            if ("1".equals(model.getStatus())) {
                viewHolderHelper.setText(R.id.tv_title,  model.getType_name());
                viewHolderHelper.setText(R.id.tv_price, "-" + model.getNumber());
                viewHolderHelper.getTextView(R.id.tv_price).setTextColor(mContext.getResources().getColor(R.color.redColor));
            } else {
                viewHolderHelper.setText(R.id.tv_title,  model.getType_name());
                viewHolderHelper.getTextView(R.id.tv_price).setTextColor(mContext.getResources().getColor(R.color.text_color3));
                viewHolderHelper.setText(R.id.tv_price, "+" + model.getNumber());
            }

        }
        viewHolderHelper.setText(R.id.tv_time, ToolUtil.getStrTime(model.getCreate_time(), "yyyy-MM-dd HH:mm:ss"));
    }
}
