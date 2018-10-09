package com.guo.qlzx.maxiansheng.adapter;

import android.graphics.Color;
import android.widget.ListView;

import com.guo.qlzx.maxiansheng.R;
import com.guo.qlzx.maxiansheng.bean.DistriDayBean;
import com.qlzx.mylibrary.base.BaseListAdapter;

/**
 * Created by Administrator on 2018/4/27 0027.
 */

public class DistriDayAdapter extends BaseListAdapter<DistriDayBean> {
    public DistriDayAdapter(ListView listView) {
        super(listView, R.layout.item_distri_day);
    }

    @Override
    public void fillData(ViewHolder holder, int position, DistriDayBean model) {
        holder.setText(R.id.tv_name,model.getName());
        if (model.isSelected()){
            holder.getTextView(R.id.tv_name).setTextColor(mContext.getResources().getColor(R.color.blue_15));
            holder.getView(R.id.abroad_color).setBackgroundColor(mContext.getResources().getColor(R.color.white));
        }else {
            holder.getTextView(R.id.tv_name).setTextColor(mContext.getResources().getColor(R.color.text_color6));
            holder.getView(R.id.abroad_color).setBackgroundColor(Color.parseColor("#f2f2f2"));
        }
    }
}
