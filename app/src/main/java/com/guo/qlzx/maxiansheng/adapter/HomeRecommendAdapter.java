package com.guo.qlzx.maxiansheng.adapter;

import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.guo.qlzx.maxiansheng.R;
import com.guo.qlzx.maxiansheng.bean.HomeRecommendListBean;
import com.qlzx.mylibrary.base.BaseListAdapter;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.util.GlideUtil;
import com.qlzx.mylibrary.util.ToastUtil;

/**
 * Created by 李 on 2018/4/11.
 * 首页推荐商品
 */

public class HomeRecommendAdapter extends BaseListAdapter<HomeRecommendListBean> {
    public HomeRecommendAdapter(ListView listView) {
        super(listView, R.layout.item_home_recommed);
    }


    @Override
    public void fillData(ViewHolder viewHolderHelper, final int position, final HomeRecommendListBean model) {
        viewHolderHelper.setText(R.id.tv_title,model.getGoods_name());
        viewHolderHelper.setText(R.id.tv_taste,model.getGoods_remark());
        viewHolderHelper.setText(R.id.tv_price,"¥"+model.getShop_price());
        viewHolderHelper.setText(R.id.tv_unit,"/"+model.getUnit());

        GlideUtil.display(mContext, Constants.IMG_HOST+model.getImg(),viewHolderHelper.getImageView(R.id.iv_img));
        ImageView ivadd =(ImageView)viewHolderHelper.getView(R.id.iv_add);
        viewHolderHelper.setText(R.id.tv_price,"¥"+model.getShop_price());
        viewHolderHelper.setText(R.id.tv_unit,"/"+model.getUnit());
        if(model.getType() == 0){
            viewHolderHelper.getView(R.id.tv_status).setVisibility(View.GONE);
            viewHolderHelper.getView(R.id.ll_content).setVisibility(View.VISIBLE);
            viewHolderHelper.getView(R.id.iv_add).setVisibility(View.VISIBLE);
            viewHolderHelper.setText(R.id.tv_vip,"会员价  ¥"+model.getPlus_price()+"/"+model.getUnit());
        }else if (model.getType() == 1){
            viewHolderHelper.getView(R.id.iv_add).setVisibility(View.GONE);
            viewHolderHelper.getView(R.id.tv_status).setVisibility(View.VISIBLE);
            viewHolderHelper.getView(R.id.ll_content).setVisibility(View.GONE);
            viewHolderHelper.setText(R.id.tv_status,"秒杀");
            viewHolderHelper.setText(R.id.tv_vip,"秒杀价 ¥"+model.getPlus_price()+"/"+model.getUnit());
        }else if (model.getType() == 2){
            viewHolderHelper.getView(R.id.iv_add).setVisibility(View.GONE);
            viewHolderHelper.getView(R.id.ll_content).setVisibility(View.GONE);
            viewHolderHelper.getView(R.id.tv_status).setVisibility(View.VISIBLE);
            viewHolderHelper.setText(R.id.tv_status,"团购");
            viewHolderHelper.setText(R.id.tv_vip,"团购价  ¥"+model.getPlus_price()+"/"+model.getUnit());
        }else if (model.getType() == 3){
            viewHolderHelper.getView(R.id.iv_add).setVisibility(View.GONE);
            viewHolderHelper.getView(R.id.ll_content).setVisibility(View.GONE);
            viewHolderHelper.getView(R.id.tv_status).setVisibility(View.VISIBLE);
            viewHolderHelper.setText(R.id.tv_status,"预售");
            viewHolderHelper.setText(R.id.tv_vip,"预售价  ¥"+model.getPlus_price()+"/"+model.getUnit());
        }
        ivadd.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if (onAddGoodsClickListener!=null){
                     onAddGoodsClickListener.onAdd(model);
                 }
             }
         });
        if(getCount()-1 == position){
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) viewHolderHelper.getView(R.id.rl_relative).getLayoutParams();
            params.setMargins(0,0,0,30);
        }else {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) viewHolderHelper.getView(R.id.rl_relative).getLayoutParams();
            params.setMargins(0,0,0,0);
        }
    }
    private OnAddGoodsClickListener onAddGoodsClickListener;

    public void setOnAddGoodsClickListener(OnAddGoodsClickListener onAddGoodsClickListener) {
        this.onAddGoodsClickListener = onAddGoodsClickListener;
    }

    public interface OnAddGoodsClickListener{
        void onAdd(HomeRecommendListBean model);
    }
}
