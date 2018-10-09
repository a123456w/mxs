package com.guo.qlzx.maxiansheng.adapter;

import android.widget.GridView;

import com.guo.qlzx.maxiansheng.R;
import com.guo.qlzx.maxiansheng.bean.GoodsServerWellBean;
import com.guo.qlzx.maxiansheng.bean.ServerBean;
import com.qlzx.mylibrary.base.BaseListAdapter;
import com.qlzx.mylibrary.util.PreferencesHelper;

/**
 * Created by Administrator on 2018/4/23 0023.
 */

public class ServerListAdapter extends BaseListAdapter<ServerBean.ServicePriceBean> {


    public ServerListAdapter(GridView gridView) {
        super(gridView, R.layout.item_server_list);
    }


    @Override
    public void fillData(ViewHolder holder, int position, ServerBean.ServicePriceBean model) {
        if (new PreferencesHelper(mContext).getVip() == 2){
            holder.setText(R.id.tv_name,model.getService_name()+"："+model.getPlus_price());
        }else {
            holder.setText(R.id.tv_name,model.getService_name()+"："+model.getShop_price());
        }
        if (model.isSelected()){
            holder.getImageView(R.id.iv_check).setImageResource(R.drawable.ic_checked);
        }else {
            holder.getImageView(R.id.iv_check).setImageResource(R.drawable.ic_uncheck);
        }
    }
    private int selector=-1;

    public int getSelector() {
        return selector;
    }

    public void setSelector(int selector) {
        this.selector = selector;
    }
}
