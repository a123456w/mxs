package com.guo.qlzx.maxiansheng.adapter;

import android.graphics.Color;
import android.view.View;
import android.widget.ListView;

import com.guo.qlzx.maxiansheng.R;
import com.guo.qlzx.maxiansheng.bean.DistriDayBean;
import com.qlzx.mylibrary.base.BaseListAdapter;
import com.qlzx.mylibrary.util.DateUtil;

import java.util.Date;

import static com.qlzx.mylibrary.util.DateUtil.FORMAT_HM;

/**
 * Created by Administrator on 2018/4/27 0027.
 */

public class DistriTimeAdapter extends BaseListAdapter<DistriDayBean> {

    public DistriTimeAdapter(ListView listView) {
        super(listView, R.layout.item_distri_time);
    }

    @Override
    public void fillData(ViewHolder holder, int position, DistriDayBean model) {
        holder.setText(R.id.tv_name, DateUtil.date2Str(new Date(model.getStartTime()),FORMAT_HM)+"-"+DateUtil.date2Str(new Date(model.getEndTime()),FORMAT_HM));
        if (model.isSelected()){
            holder.getTextView(R.id.tv_name).setTextColor(mContext.getResources().getColor(R.color.blue_15));
            holder.getImageView(R.id.iv_check).setVisibility(View.VISIBLE);
        }else {
            holder.getTextView(R.id.tv_name).setTextColor(mContext.getResources().getColor(R.color.text_color6));
            holder.getImageView(R.id.iv_check).setVisibility(View.GONE);
        }
    }
}
