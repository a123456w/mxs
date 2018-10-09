package com.guo.qlzx.maxiansheng.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.guo.qlzx.maxiansheng.R;
import com.guo.qlzx.maxiansheng.bean.HomeRecommendListBean;
import com.qlzx.mylibrary.base.RecyclerViewAdapter;
import com.qlzx.mylibrary.base.ViewHolderHelper;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.util.GlideUtil;

/**
 * Created by 李 on 2018/4/26.
 */

public class MemberCenterRecommendAdapter extends RecyclerViewAdapter<HomeRecommendListBean> {
    public MemberCenterRecommendAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.item_member_center_recommend);
    }

    @Override
    protected void fillData(ViewHolderHelper viewHolderHelper, int position, HomeRecommendListBean model) {
        GlideUtil.display(mContext, Constants.IMG_HOST+model.getImg(),viewHolderHelper.getImageView(R.id.iv_img));
        viewHolderHelper.setText(R.id.tv_title,model.getGoods_name());
        viewHolderHelper.setText(R.id.tv_vip,"会员价  "+model.getPlus_price()+"/"+model.getUnit());
        viewHolderHelper.setText(R.id.tv_price,"¥"+model.getShop_price());
        viewHolderHelper.setText(R.id.tv_unit,"/"+model.getUnit());

        if(model.getType() == 0){
            viewHolderHelper.getView(R.id.tv_status).setVisibility(View.GONE);
        }else if (model.getType() == 1){
            viewHolderHelper.getView(R.id.iv_add).setVisibility(View.INVISIBLE);
            viewHolderHelper.setText(R.id.tv_status,"秒杀");
            viewHolderHelper.getView(R.id.tv_status).setVisibility(View.VISIBLE);
            viewHolderHelper.getTextView(R.id.tv_vip).setVisibility(View.INVISIBLE);
            viewHolderHelper.setText(R.id.tv_price,"¥"+model.getPlus_price());
        }else if (model.getType() == 2){
            viewHolderHelper.getView(R.id.iv_add).setVisibility(View.INVISIBLE);
            viewHolderHelper.setText(R.id.tv_status,"团购");
            viewHolderHelper.getView(R.id.tv_status).setVisibility(View.VISIBLE);
            viewHolderHelper.getTextView(R.id.tv_vip).setVisibility(View.INVISIBLE);
            viewHolderHelper.setText(R.id.tv_price,"¥"+model.getPlus_price());
        }else if (model.getType() == 3){
            viewHolderHelper.getView(R.id.iv_add).setVisibility(View.INVISIBLE);
            viewHolderHelper.setText(R.id.tv_status,"预售");
            viewHolderHelper.getView(R.id.tv_status).setVisibility(View.VISIBLE);
            viewHolderHelper.getTextView(R.id.tv_vip).setVisibility(View.INVISIBLE);
            viewHolderHelper.setText(R.id.tv_price,"¥"+model.getPlus_price());
        }
        viewHolderHelper.setItemChildClickListener(R.id.iv_add);
    }
}
