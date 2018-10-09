package com.guo.qlzx.maxiansheng.adapter;

import android.support.v7.widget.RecyclerView;
import android.widget.ListView;

import com.guo.qlzx.maxiansheng.R;
import com.guo.qlzx.maxiansheng.bean.OrdersBean;
import com.qlzx.mylibrary.base.BaseListAdapter;
import com.qlzx.mylibrary.base.RecyclerViewAdapter;
import com.qlzx.mylibrary.base.ViewHolderHelper;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.util.GlideUtil;

/**
 * Created by Administrator on 2018/5/3 0003.
 */

public class GoodsPicAdapter extends RecyclerViewAdapter<OrdersBean.OrderBean> {
    public GoodsPicAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.item_goods_pic);
    }

    @Override
    protected void fillData(ViewHolderHelper helper, int position, OrdersBean.OrderBean model) {
        GlideUtil.display(mContext, Constants.IMG_HOST + model.getOriginal_img(), helper.getImageView(R.id.iv_goods));
    }

}
