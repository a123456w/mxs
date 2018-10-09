package com.guo.qlzx.maxiansheng.adapter;

import android.support.v7.widget.RecyclerView;

import com.guo.qlzx.maxiansheng.R;
import com.guo.qlzx.maxiansheng.bean.HomeActivityListBean;
import com.qlzx.mylibrary.base.RecyclerViewAdapter;
import com.qlzx.mylibrary.base.ViewHolderHelper;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.util.GlideUtil;

/**
 * Created by 李 on 2018/4/17.
 */

public class HomeActionSecondsKillAdapter  extends RecyclerViewAdapter<HomeActivityListBean> {

    public HomeActionSecondsKillAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.item_action_seconds_kill);
    }

    @Override
    protected void fillData(ViewHolderHelper viewHolderHelper, int position, HomeActivityListBean model) {
        GlideUtil.display(mContext, Constants.IMG_HOST+model.getImg(),viewHolderHelper.getImageView(R.id.iv_kill_img));
        viewHolderHelper.setText(R.id.tv_kill_title,model.getGoods_name());
        viewHolderHelper.setText(R.id.tv_kill_price,"¥"+model.getPrice());
        viewHolderHelper.setText(R.id.tv_kill_unit,"/"+model.getUnit());
    }
}
