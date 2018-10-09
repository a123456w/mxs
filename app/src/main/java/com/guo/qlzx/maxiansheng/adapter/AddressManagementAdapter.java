package com.guo.qlzx.maxiansheng.adapter;

import android.support.v7.widget.RecyclerView;

import com.guo.qlzx.maxiansheng.R;
import com.guo.qlzx.maxiansheng.bean.AddressManagementListBean;
import com.qlzx.mylibrary.base.RecyclerViewAdapter;
import com.qlzx.mylibrary.base.ViewHolderHelper;

/**
 * Created by 李 on 2018/4/13.
 */

public class AddressManagementAdapter extends RecyclerViewAdapter<AddressManagementListBean> {
    public AddressManagementAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.item_address_manger);
    }

    @Override
    protected void fillData(ViewHolderHelper viewHolderHelper, int position, AddressManagementListBean model) {
        viewHolderHelper.setText(R.id.tv_name,model.getConsignee());
        viewHolderHelper.setText(R.id.tv_number,model.getMobile());
        viewHolderHelper.setText(R.id.tv_address,model.getCity()+model.getDistrict()+model.getAddress());
        if (model.getIs_default()==1){
            viewHolderHelper.setChecked(R.id.cb_default,true);
        }else {
            viewHolderHelper.setChecked(R.id.cb_default,false);
        }
        viewHolderHelper.setItemChildClickListener(R.id.cb_default);
        viewHolderHelper.setItemChildClickListener(R.id.tv_edit);
        viewHolderHelper.setItemChildClickListener(R.id.tv_delete);
    }

}
