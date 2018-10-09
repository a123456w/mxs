package com.guo.qlzx.maxiansheng.adapter;

import android.support.v7.widget.RecyclerView;
import android.widget.GridView;

import com.guo.qlzx.maxiansheng.R;
import com.guo.qlzx.maxiansheng.bean.HomeCouponListBeans;
import com.qlzx.mylibrary.base.BaseListAdapter;
import com.qlzx.mylibrary.base.RecyclerViewAdapter;
import com.qlzx.mylibrary.base.ViewHolderHelper;

/**
 * Created by 李 on 2018/4/25.
 */

public class NewbieCouponsAdapter extends BaseListAdapter<HomeCouponListBeans> {
    public NewbieCouponsAdapter(GridView recyclerView) {
        super(recyclerView, R.layout.item_home_dialog);
    }



    @Override
    public void fillData(ViewHolder holder, int position, HomeCouponListBeans model) {
        holder.setText(R.id.tv_price,"¥"+model.getMoney());
        holder.setText(R.id.tv_info,model.getCoupon_info());
        holder.setText(R.id.tv_name,model.getName());
    }
}
