package com.guo.qlzx.maxiansheng.adapter;

import android.widget.ListView;

import com.guo.qlzx.maxiansheng.R;
import com.guo.qlzx.maxiansheng.bean.ShowSearchBean;
import com.qlzx.mylibrary.base.BaseListAdapter;

/**
 * Created by Administrator on 2018/4/18.
 */

public class AllClassificationAdapter extends BaseListAdapter<ShowSearchBean.ClassifyBean> {

    public AllClassificationAdapter(ListView listView) {
        super(listView, R.layout.item_all_fenlei);
    }

    @Override
    public void fillData(ViewHolder holder, int position, ShowSearchBean.ClassifyBean model) {
        holder.setText(R.id.name, model.getName());
    }
}
