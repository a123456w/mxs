package com.guo.qlzx.maxiansheng.adapter;

import android.widget.GridView;

import com.guo.qlzx.maxiansheng.R;
import com.qlzx.mylibrary.base.BaseListAdapter;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.util.GlideUtil;

/**
 * Created by Administrator on 2018/4/19 0019.
 */

public class EvaluateListPicAdapter extends BaseListAdapter<String> {

    public EvaluateListPicAdapter(GridView gridView) {
        super(gridView, R.layout.item_evaluate_list_pic);
    }

    @Override
    public void fillData(ViewHolder holder, int position, String model) {
        GlideUtil.display(mContext, Constants.IMG_HOST+model,holder.getImageView(R.id.evaluate_pic));
    }
}
