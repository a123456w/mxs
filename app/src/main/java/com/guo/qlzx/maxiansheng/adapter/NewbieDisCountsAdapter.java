package com.guo.qlzx.maxiansheng.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.guo.qlzx.maxiansheng.R;
import com.guo.qlzx.maxiansheng.bean.HomeRecommendListBean;
import com.qlzx.mylibrary.base.RecyclerViewAdapter;
import com.qlzx.mylibrary.base.ViewHolderHelper;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.util.GlideUtil;

/**
 * Created by 李 on 2018/4/25.
 */

public class NewbieDisCountsAdapter extends RecyclerViewAdapter<HomeRecommendListBean> {
    public NewbieDisCountsAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.item_newbie_discounts);
    }

    @Override
    protected void fillData(ViewHolderHelper viewHolderHelper, int position, HomeRecommendListBean model) {
        viewHolderHelper.setText(R.id.tv_title,model.getGoods_name());
        viewHolderHelper.setText(R.id.tv_price,"¥"+model.getShop_price());
        viewHolderHelper.setText(R.id.tv_unit,"/"+model.getUnit());
        GlideUtil.display(mContext, Constants.IMG_HOST+model.getImg(),viewHolderHelper.getImageView(R.id.iv_img));
        TextView textView=viewHolderHelper.getTextView(R.id.tv_status);
        textView.setVisibility(View.INVISIBLE);
        if (model.getType()==0){
            viewHolderHelper.getImageView(R.id.iv_add).setVisibility(View.VISIBLE);
        }else if (model.getType() == 1){
            viewHolderHelper.getView(R.id.iv_add).setVisibility(View.GONE);
            viewHolderHelper.setText(R.id.tv_status,"秒杀");
            viewHolderHelper.getView(R.id.tv_status).setVisibility(View.VISIBLE);
        }else if (model.getType() == 2){
            viewHolderHelper.getView(R.id.iv_add).setVisibility(View.GONE);
            viewHolderHelper.setText(R.id.tv_status,"团购");
            viewHolderHelper.getView(R.id.tv_status).setVisibility(View.VISIBLE);
        }else if (model.getType() == 3){
            viewHolderHelper.getView(R.id.iv_add).setVisibility(View.GONE);
            viewHolderHelper.setText(R.id.tv_status,"预售");
            viewHolderHelper.getView(R.id.tv_status).setVisibility(View.VISIBLE);
        }
        viewHolderHelper.setItemChildClickListener(R.id.iv_add);
    }
}
