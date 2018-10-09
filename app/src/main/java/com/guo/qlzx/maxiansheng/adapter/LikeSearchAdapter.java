package com.guo.qlzx.maxiansheng.adapter;

import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;

import com.guo.qlzx.maxiansheng.R;
import com.guo.qlzx.maxiansheng.bean.GuessLikeBean;
import com.guo.qlzx.maxiansheng.bean.ShowSearchBean;
import com.qlzx.mylibrary.base.BaseListAdapter;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.util.GlideUtil;

/**
 * Created by Administrator on 2018/4/17 0017.
 *
 */

public class LikeSearchAdapter extends BaseListAdapter<ShowSearchBean.GoodsListBean> {
    public LikeSearchAdapter(GridView gridView) {
        super(gridView, R.layout.item_guess_like_goods_search);
    }

    @Override
    public void fillData(ViewHolder holder, int position, final ShowSearchBean.GoodsListBean model) {
        GlideUtil.display(mContext, Constants.IMG_HOST + model.getImg(), holder.getImageView(R.id.iv_img));
        holder.setText(R.id.tv_title, model.getGoods_name());
        holder.setText(R.id.tv_vip, "会员价 ¥" + model.getPlus_price() + "/" + model.getUnit());
        holder.setText(R.id.tv_price, "¥" + model.getShop_price()+ "/" + model.getUnit());
        if (model.getType()==0){
            holder.getTextView(R.id.tv_vip).setVisibility(View.VISIBLE);
            holder.getTextView(R.id.tv_status).setVisibility(View.GONE);
            holder.getImageView(R.id.iv_add).setVisibility(View.VISIBLE);
        }else if (model.getType()==1){
            holder.getTextView(R.id.tv_vip).setVisibility(View.INVISIBLE);
            holder.getImageView(R.id.iv_add).setVisibility(View.GONE);
            holder.getTextView(R.id.tv_status).setVisibility(View.VISIBLE);
            holder.setText(R.id.tv_status,"秒杀价");
            holder.setText(R.id.tv_price, "¥" + model.getPlus_price()+ "/" + model.getUnit());
        }else if (model.getType()==2){
            holder.getTextView(R.id.tv_vip).setVisibility(View.INVISIBLE);
            holder.getTextView(R.id.tv_status).setVisibility(View.VISIBLE);
            holder.getImageView(R.id.iv_add).setVisibility(View.GONE);
            holder.setText(R.id.tv_status,"团购价");
            holder.setText(R.id.tv_price, "¥" + model.getPlus_price()+ "/" + model.getUnit());
        }else if (model.getType()==3){
            holder.getTextView(R.id.tv_vip).setVisibility(View.INVISIBLE);
            holder.getTextView(R.id.tv_status).setVisibility(View.VISIBLE);
            holder.getImageView(R.id.iv_add).setVisibility(View.GONE);
            holder.setText(R.id.tv_status,"预售价");
            holder.setText(R.id.tv_price, "¥" + model.getPlus_price()+ "/" + model.getUnit());
        }

        final ImageView  imageView=holder.getImageView(R.id.iv_add);
        holder.getImageView(R.id.iv_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int[] location = new int[2] ;
                imageView.getLocationOnScreen(location);
                if (onAddClickListener!=null){
                    onAddClickListener.onAddClick(model,location[0],location[1]);
                }
            }
        });
    }
    private OnAddClickListener onAddClickListener;

    public void setOnAddClickListener(OnAddClickListener onAddClickListener) {
        this.onAddClickListener = onAddClickListener;
    }

    public interface OnAddClickListener{
        void onAddClick(ShowSearchBean.GoodsListBean model,float x,float y);
    }
}
