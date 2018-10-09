package com.guo.qlzx.maxiansheng.adapter;

import android.view.View;
import android.widget.GridView;
import android.widget.ListView;

import com.guo.qlzx.maxiansheng.R;
import com.guo.qlzx.maxiansheng.bean.ClassificationBean;
import com.guo.qlzx.maxiansheng.bean.TwoCategoryBean;
import com.qlzx.mylibrary.base.BaseListAdapter;

/**
 * Created by Administrator on 2018/4/16.
 */

public class TwoClassificationAdapter extends BaseListAdapter<TwoCategoryBean> {


    public TwoClassificationAdapter(ListView listView) {
        super(listView, R.layout.item_two_classification);
    }

    @Override
    public void fillData(ViewHolder holder, int position, TwoCategoryBean model) {
        /*if (position == 0) {
            holder.getImageView(R.id.rexiao_pic).setVisibility(View.VISIBLE);
        } else {
            holder.getImageView(R.id.rexiao_pic).setVisibility(View.GONE);
        }*/
        holder.getImageView(R.id.rexiao_pic).setVisibility(View.GONE);
        if(model.isTrue()){
            holder.getView(R.id.abroad_color).setBackgroundResource(R.color.white);
        }else{
            holder.getView(R.id.abroad_color).setBackgroundResource(R.color.background_color);
        }
        holder.setText(R.id.tv_two_name,model.getName());
    }
}
