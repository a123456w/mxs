package com.guo.qlzx.maxiansheng.adapter;

import android.widget.GridView;
import android.widget.TextView;

import com.guo.qlzx.maxiansheng.R;
import com.guo.qlzx.maxiansheng.bean.ClassificationBean;
import com.qlzx.mylibrary.base.BaseListAdapter;

/**
 * Created by Administrator on 2018/4/16.
 */

public class OneClassificationAdapter extends BaseListAdapter<ClassificationBean.ListBean> {

    public OneClassificationAdapter(GridView gridView) {
        super(gridView, R.layout.item_one_classification);
    }

    @Override
    public void fillData(BaseListAdapter.ViewHolder holder, int position, ClassificationBean.ListBean model) {

        TextView textView=holder.getTextView(R.id.name);
        textView.setText(model.getName());
        if (model.isChecked()){
            textView.setTextColor(mContext.getResources().getColor(R.color.blue_15));
        }else {
            textView.setTextColor(mContext.getResources().getColor(R.color.text_color3));
        }
    }

}
