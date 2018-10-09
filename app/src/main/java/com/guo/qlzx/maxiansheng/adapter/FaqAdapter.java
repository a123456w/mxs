package com.guo.qlzx.maxiansheng.adapter;

import android.support.v7.widget.RecyclerView;

import com.guo.qlzx.maxiansheng.R;
import com.guo.qlzx.maxiansheng.bean.FaqListBean;
import com.qlzx.mylibrary.base.RecyclerViewAdapter;
import com.qlzx.mylibrary.base.ViewHolderHelper;

/**
 * Created by 李 on 2018/4/26.
 */

public class FaqAdapter extends RecyclerViewAdapter<FaqListBean> {
    public FaqAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.item_faq);
    }

    @Override
    protected void fillData(ViewHolderHelper viewHolderHelper, int position, FaqListBean model) {
        viewHolderHelper.setText(R.id.tv_title,(position+1)+"、"+model.getCat_name());
    }
}
