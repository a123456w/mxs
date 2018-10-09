package com.guo.qlzx.maxiansheng.adapter;

import android.widget.GridView;

import com.guo.qlzx.maxiansheng.R;
import com.guo.qlzx.maxiansheng.bean.ClassificationBean;
import com.qlzx.mylibrary.base.BaseListAdapter;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.util.GlideUtil;

/**
 * Created by Administrator on 2018/4/16.
 * 分类首页的适配器
 */

public class ClassificationAdapter extends BaseListAdapter<ClassificationBean.ListBean> {

    public ClassificationAdapter(GridView gridView) {
        super(gridView, R.layout.item_classification);
    }

    @Override
    public void fillData(ViewHolder holder, int position, ClassificationBean.ListBean model) {
        GlideUtil.display(mContext, Constants.IMG_HOST+model.getImg(),holder.getImageView(R.id.class_pic));
        holder.setText(R.id.class_name,model.getName());
    }
}
