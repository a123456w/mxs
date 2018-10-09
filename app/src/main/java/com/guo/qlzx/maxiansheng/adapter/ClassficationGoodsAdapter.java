package com.guo.qlzx.maxiansheng.adapter;

import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.guo.qlzx.maxiansheng.R;
import com.guo.qlzx.maxiansheng.bean.ClassficationGoodsBean;
import com.qlzx.mylibrary.base.BaseListAdapter;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.util.GlideUtil;

/**
 * Created by Administrator on 2018/4/25 0025.
 */

public class ClassficationGoodsAdapter extends BaseListAdapter<ClassficationGoodsBean> {
    public ClassficationGoodsAdapter(ListView listView) {
        super(listView, R.layout.item_classfication_goods);
    }

    private OnAddCartOnclickListener onclickListener;

    public void setOnclickListener(OnAddCartOnclickListener onclickListener) {
        this.onclickListener = onclickListener;
    }

    @Override
    public void fillData(ViewHolder holder, final int position, ClassficationGoodsBean model) {
        GlideUtil.display(mContext, Constants.IMG_HOST+model.getImg(),holder.getImageView(R.id.iv_img));
        holder.setText(R.id.tv_title,model.getGoods_name());
        holder.setText(R.id.tv_describe,model.getGoods_remark());
        holder.setText(R.id.tv_vip_unit,"/"+model.getUnit());
        holder.setText(R.id.tv_unit,"/"+model.getUnit());
        if (model.getType() == 0){
            holder.getTextView(R.id.tv_tejia).setVisibility(View.GONE);
            holder.getView(R.id.ll_vip).setVisibility(View.VISIBLE);
            holder.getTextView(R.id.tv_old_price).setVisibility(View.GONE);
            holder.setText(R.id.tv_vip,model.getPlus_price());
            holder.setText(R.id.tv_price,"¥"+model.getShop_price());
        }else if (model.getType() == 1){
            //秒杀
            holder.setText(R.id.tv_tejia,"秒杀");
            holder.getTextView(R.id.tv_tejia).setVisibility(View.VISIBLE);
            holder.getView(R.id.ll_vip).setVisibility(View.GONE);
            holder.getTextView(R.id.tv_old_price).setVisibility(View.VISIBLE);
            holder.setText(R.id.tv_price,"¥"+model.getPlus_price());
            holder.setText(R.id.tv_old_price,"¥"+model.getShop_price());
        }else if (model.getType() == 2){
            //团购
            holder.setText(R.id.tv_tejia,"团购");
            holder.getTextView(R.id.tv_tejia).setVisibility(View.VISIBLE);
            holder.getView(R.id.ll_vip).setVisibility(View.GONE);
            holder.getTextView(R.id.tv_old_price).setVisibility(View.VISIBLE);
            holder.setText(R.id.tv_price,"¥"+model.getPlus_price());
            holder.setText(R.id.tv_old_price,"¥"+model.getShop_price());
        }else if (model.getType() == 3){
            //预售
            holder.setText(R.id.tv_tejia,"预售");
            holder.getTextView(R.id.tv_tejia).setVisibility(View.VISIBLE);
            holder.getView(R.id.ll_vip).setVisibility(View.GONE);
            holder.getTextView(R.id.tv_old_price).setVisibility(View.VISIBLE);
            holder.setText(R.id.tv_price,"¥"+model.getPlus_price());
            holder.setText(R.id.tv_old_price,"¥"+model.getShop_price());
        }

        final ImageView imageView= holder.getImageView(R.id.iv_img);
        holder.getImageView(R.id.iv_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int[] location = new int[2] ;
                imageView.getLocationOnScreen(location);
                if (onclickListener != null){
                    onclickListener.onClick(position,location[0],location[1]);
                }
            }
        });

        if (model.getType() != 0){
            holder.getImageView(R.id.iv_add).setVisibility(View.INVISIBLE);
        }else {
            holder.getImageView(R.id.iv_add).setVisibility(View.VISIBLE);
        }

    }

    public interface OnAddCartOnclickListener {
        void onClick(int position,float x,float y);
    }
}
