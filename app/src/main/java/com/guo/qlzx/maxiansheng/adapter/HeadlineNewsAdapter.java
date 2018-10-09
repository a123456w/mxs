package com.guo.qlzx.maxiansheng.adapter;

import android.support.v7.widget.RecyclerView;

import com.guo.qlzx.maxiansheng.R;
import com.guo.qlzx.maxiansheng.bean.HeadlineNewsBean;
import com.qlzx.mylibrary.base.RecyclerViewAdapter;
import com.qlzx.mylibrary.base.ViewHolderHelper;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.util.GlideUtil;

/**
 * Created by 李 on 2018/4/11.
 */

public class HeadlineNewsAdapter extends RecyclerViewAdapter<HeadlineNewsBean> {
    public HeadlineNewsAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.item_headline);
    }

    @Override
    protected void fillData(ViewHolderHelper viewHolderHelper, int position, HeadlineNewsBean model) {
        //无接口 先写这个试试
        viewHolderHelper.setText(R.id.tv_title,model.getTitle());
        viewHolderHelper.setText(R.id.tv_info,model.getContent());
        GlideUtil.display(mContext, Constants.IMG_HOST+ model.getImg(),viewHolderHelper.getImageView(R.id.riv_img));
    }
}
