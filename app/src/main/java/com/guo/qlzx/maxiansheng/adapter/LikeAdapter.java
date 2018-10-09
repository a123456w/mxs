package com.guo.qlzx.maxiansheng.adapter;

import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.guo.qlzx.maxiansheng.R;
import com.guo.qlzx.maxiansheng.bean.GuessLikeBean;
import com.qlzx.mylibrary.base.BaseListAdapter;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.util.GlideUtil;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by Administrator on 2018/4/17 0017.
 * 猜你喜欢
 */

public class LikeAdapter extends BaseListAdapter<GuessLikeBean> {

    private OnAddCartOnclickListener onclickListener;

    public LikeAdapter(GridView gridView) {
        super(gridView, R.layout.item_guess_like_goods);
    }

    public void setOnclickListener(OnAddCartOnclickListener onclickListener) {
        this.onclickListener = onclickListener;
    }

    @Override
    public void fillData(ViewHolder holder, final int position, GuessLikeBean model) {
        Glide.with(mContext)
                .load(Constants.IMG_HOST+model.getImg())
                .bitmapTransform(new RoundedCornersTransformation(mContext, 30, 0,
                        RoundedCornersTransformation.CornerType.TOP))
                .into(holder.getImageView(R.id.iv_img));
    //    GlideUtil.display(mContext, Constants.IMG_HOST+model.getImg(),holder.getImageView(R.id.iv_img));
        holder.setText(R.id.tv_title,model.getGoods_name());
        holder.setText(R.id.tv_price,"¥"+model.getShop_price()+"/"+model.getUnit());
        if(model.getType() == 0){
            holder.getView(R.id.ll_vip).setVisibility(View.VISIBLE);
            holder.getTextView(R.id.tv_status).setVisibility(View.INVISIBLE);
            holder.setText(R.id.tv_vip,model.getPlus_price()+"");
            holder.setText(R.id.tv_price_unit,"/"+model.getUnit());
        }else if (model.getType() == 1){
            holder.getView(R.id.ll_vip).setVisibility(View.INVISIBLE);
            holder.getTextView(R.id.tv_status).setVisibility(View.VISIBLE);
            holder.setText(R.id.tv_price,"¥"+model.getPlus_price());
            holder.setText(R.id.tv_status,"秒杀");
        }else if (model.getType() == 2){
            holder.getView(R.id.ll_vip).setVisibility(View.INVISIBLE);
            holder.getTextView(R.id.tv_status).setVisibility(View.VISIBLE);
            holder.setText(R.id.tv_price,"¥"+model.getPlus_price());
            holder.setText(R.id.tv_status,"团购");
        }else if (model.getType() == 3){
            holder.getView(R.id.ll_vip).setVisibility(View.INVISIBLE);
            holder.getTextView(R.id.tv_status).setVisibility(View.VISIBLE);
            holder.setText(R.id.tv_price,"¥"+model.getPlus_price());
            holder.setText(R.id.tv_status,"预售");
        }



         final ImageView add = holder.getImageView(R.id.iv_add);

        add.setTag(position);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int[] location = new int[2] ;
                add.getLocationOnScreen(location);
                if (onclickListener != null){
                    onclickListener.onClick(position,location[0],location[1]);
                }
            }
        });
        if (model.getType() !=0){
            add.setVisibility(View.INVISIBLE);
        }else {
            add.setVisibility(View.VISIBLE);
        }

    }


    public interface OnAddCartOnclickListener {
         void onClick(int position,float x,float y);
    }
}
