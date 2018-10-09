package com.guo.qlzx.maxiansheng.adapter;

import android.support.v7.widget.RecyclerView;
import android.widget.GridView;
import android.widget.ListView;

import com.guo.qlzx.maxiansheng.R;
import com.guo.qlzx.maxiansheng.bean.ServerBean;
import com.qlzx.mylibrary.base.BaseListAdapter;
import com.qlzx.mylibrary.base.RecyclerViewAdapter;
import com.qlzx.mylibrary.base.ViewHolderHelper;

/**
 * Created by Administrator on 2018/4/23 0023.
 */

public class SpecAdapter extends RecyclerViewAdapter<ServerBean.SpeclistBean> {
    public SpecAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.item_goods_spec);
    }

    @Override
    protected void fillData(ViewHolderHelper helper, int position, ServerBean.SpeclistBean model) {
        helper.setText(R.id.tv_spec,model.getKey_name());
        helper.getTextView(R.id.tv_spec).setSelected(model.isSelected());
    }

//    public SpecAdapter(GridView gridView) {
//        super(gridView, R.layout.item_goods_spec);
//    }
//
//    @Override
//    public void fillData(ViewHolder holder, int position, ServerBean.SpeclistBean model) {
//        holder.setText(R.id.tv_spec,model.getKey_name());
//        holder.getTextView(R.id.tv_spec).setSelected(model.isSelected());
//    }
}
