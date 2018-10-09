package com.guo.qlzx.maxiansheng.adapter;

import android.support.v7.widget.RecyclerView;

import com.guo.qlzx.maxiansheng.R;
import com.guo.qlzx.maxiansheng.bean.TwoCategoryBean;
import com.qlzx.mylibrary.base.RecyclerViewAdapter;
import com.qlzx.mylibrary.base.ViewHolderHelper;

/**
 * Created by Administrator on 2018/4/25 0025.
 */

public class ThreeClasssficationAdapter extends RecyclerViewAdapter<TwoCategoryBean.SonBean> {

    public ThreeClasssficationAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.flow_item);
    }

    @Override
    protected void fillData(ViewHolderHelper helper, int position, TwoCategoryBean.SonBean model) {
        if (model.isSelect()){
            helper.getTextView(R.id.flow_text).setTextColor(mContext.getResources().getColor(R.color.blue_15));
        }else {
            helper.getTextView(R.id.flow_text).setTextColor(mContext.getResources().getColor(R.color.text_color6));
        }
        helper.setText(R.id.flow_text,model.getName());
    }
}
