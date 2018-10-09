package com.guo.qlzx.maxiansheng.adapter;

import android.graphics.Paint;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;

import com.guo.qlzx.maxiansheng.R;
import com.guo.qlzx.maxiansheng.bean.OrderDetailBean;
import com.qlzx.mylibrary.base.BaseListAdapter;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.util.GlideUtil;

/**
 * Created by Administrator on 2018/4/17 0017.
 */

public class OrderGoodsAdapter extends BaseListAdapter<OrderDetailBean.GoodsBean> {

    public OrderGoodsAdapter(ListView listView) {
        super(listView, R.layout.item_order_good);
    }

    @Override
    public void fillData(ViewHolder holder, int position, OrderDetailBean.GoodsBean model) {
        GlideUtil.display(mContext, Constants.IMG_HOST+model.getOriginal_img(),holder.getImageView(R.id.iv_goods));
        holder.setText(R.id.tv_goods_name,model.getGoods_name());
        holder.setText(R.id.tv_spec,model.getSpec_key_name());
        holder.setText(R.id.tv_one_price,model.getGoods_price());
        holder.setText(R.id.tv_new_price,"¥"+model.getMember_goods_price());
        holder.setText(R.id.tv_old_price,"¥"+model.getMarket_price());
        holder.getTextView(R.id.tv_old_price).getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        holder.setText(R.id.tv_number,"x"+model.getGoods_num());
        if (TextUtils.isEmpty(model.getService_name())){
            holder.getView(R.id.ll_server).setVisibility(View.GONE);
        }else {
            holder.getView(R.id.ll_server).setVisibility(View.VISIBLE);
            holder.setText(R.id.tv_server,model.getService_name()+" "+model.getService_price());
        }
        if (model.getChange()==3){
            holder.setText(R.id.tv_change,"退货");
        }else  if (model.getChange()==5){
            holder.setText(R.id.tv_change,"已换");
        }else  if (model.getChange()==6){
            if (!"0".equals(model.getChange_price())){
                holder.setText(R.id.tv_change,"退差价：¥"+model.getChange_price());
            }
        }
    }
}
