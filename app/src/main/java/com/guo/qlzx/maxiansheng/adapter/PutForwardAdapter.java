package com.guo.qlzx.maxiansheng.adapter;

import android.support.v7.widget.RecyclerView;

import com.guo.qlzx.maxiansheng.R;
import com.guo.qlzx.maxiansheng.bean.BalanceListBean;
import com.guo.qlzx.maxiansheng.bean.ForwardBean;
import com.guo.qlzx.maxiansheng.util.ToolUtil;
import com.qlzx.mylibrary.base.RecyclerViewAdapter;
import com.qlzx.mylibrary.base.ViewHolderHelper;

/**
 * Created by 李 on 2018/4/18.
 */

public class PutForwardAdapter extends RecyclerViewAdapter<ForwardBean> {
    public PutForwardAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.item_balance);
    }

    @Override
    protected void fillData(ViewHolderHelper viewHolderHelper, int position, ForwardBean model) {

        if ("-2".equals(model.getStatus())) {
            viewHolderHelper.setText(R.id.tv_title, "删除作废");
            viewHolderHelper.getTextView(R.id.tv_price).setTextColor(mContext.getResources().getColor(R.color.redColor));
            viewHolderHelper.setText(R.id.tv_price, "- ¥" + model.getMoney());
        }else if("-1".equals(model.getStatus())){
            viewHolderHelper.setText(R.id.tv_title, "审核失败");
            viewHolderHelper.getTextView(R.id.tv_price).setTextColor(mContext.getResources().getColor(R.color.redColor));
            viewHolderHelper.setText(R.id.tv_price, "- ¥" + model.getMoney());
        }else if("0".equals(model.getStatus())){
            viewHolderHelper.setText(R.id.tv_title, "提现中...");
            viewHolderHelper.getTextView(R.id.tv_price).setTextColor(mContext.getResources().getColor(R.color.redColor));
            viewHolderHelper.setText(R.id.tv_price, "- ¥" + model.getMoney());
        }else if("1".equals(model.getStatus())){
            viewHolderHelper.setText(R.id.tv_title, "审核通过");
            viewHolderHelper.getTextView(R.id.tv_price).setTextColor(mContext.getResources().getColor(R.color.redColor));
            viewHolderHelper.setText(R.id.tv_price, "- ¥" + model.getMoney());
        } else if("2".equals(model.getStatus())){
            viewHolderHelper.setText(R.id.tv_title, "提现成功");
            viewHolderHelper.getTextView(R.id.tv_price).setTextColor(mContext.getResources().getColor(R.color.text_color3));
            viewHolderHelper.setText(R.id.tv_price, "- ¥" + model.getMoney());
        } else {
            viewHolderHelper.setText(R.id.tv_title,  "提现失败");
            viewHolderHelper.getTextView(R.id.tv_price).setTextColor(mContext.getResources().getColor(R.color.redColor));
            viewHolderHelper.setText(R.id.tv_price, "- ¥" + model.getMoney());
        }
        viewHolderHelper.setText(R.id.tv_time, ToolUtil.getStrTime(model.getCreate_time(), "yyyy-MM-dd HH:mm:ss"));
    }
}
