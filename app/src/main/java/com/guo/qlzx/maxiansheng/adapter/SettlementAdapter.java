package com.guo.qlzx.maxiansheng.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;

import com.guo.qlzx.maxiansheng.R;
import com.guo.qlzx.maxiansheng.bean.IndexBean;
import com.qlzx.mylibrary.base.BaseListAdapter;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.util.GlideUtil;

/**
 * Created by Administrator on 2018/4/19.
 */

public class SettlementAdapter extends BaseListAdapter<IndexBean.OrderListBean> {
    public SettlementAdapter(ListView listView) {
        super(listView, R.layout.item_settlement);
    }

    @Override
    public void fillData(ViewHolder holder, int position, IndexBean.OrderListBean model) {
        GlideUtil.display(mContext, Constants.IMG_HOST + model.getGoods_img(), holder.getImageView(R.id.goods_pic));
        holder.setText(R.id.goods_name, model.getGoods_name());
        holder.setText(R.id.tv_new_price, "¥" + model.getPlus_price());
        holder.setText(R.id.goods_spec, "规格：" + model.getSpec_key_name() + "/" + model.getUnit());
        holder.setText(R.id.goods_price, "单价：¥" + model.getGoods_price() + "/" + model.getUnit());
        holder.setText(R.id.goods_num, "数量：" + model.getGoods_num());
        holder.setText(R.id.goods_total_price, "¥" + model.getOrder_price());

        if (TextUtils.isEmpty(model.getService_name())) {
            holder.getView(R.id.value_added_service).setVisibility(View.GONE);
        } else {
            holder.getView(R.id.value_added_service).setVisibility(View.VISIBLE);
            holder.setText(R.id.tv_service, model.getService_name() + "：" + model.getService_price() + "元");
        }
    }
}
