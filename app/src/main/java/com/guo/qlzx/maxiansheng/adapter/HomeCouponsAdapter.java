package com.guo.qlzx.maxiansheng.adapter;

import android.widget.ListView;

import com.guo.qlzx.maxiansheng.R;
import com.guo.qlzx.maxiansheng.bean.HomeCouponListBeans;
import com.guo.qlzx.maxiansheng.bean.HomeTopBean;
import com.qlzx.mylibrary.base.BaseListAdapter;

/**
 * Created by 李 on 2018/4/11.
 */

public class HomeCouponsAdapter extends BaseListAdapter<HomeTopBean.NewCouponBean> {
    public HomeCouponsAdapter(ListView listView) {
        super(listView, R.layout.item_home_dialog);
    }

    @Override
    public void fillData(ViewHolder holder, int position, HomeTopBean.NewCouponBean model) {
        holder.setText(R.id.tv_price,"¥"+model.getMoney()+"");
        holder.setText(R.id.tv_info,"满"+model.getCondition()+"元使用");
        holder.setText(R.id.tv_name,model.getName());
    }
}
