package com.guo.qlzx.maxiansheng.adapter;

import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.guo.qlzx.maxiansheng.R;
import com.guo.qlzx.maxiansheng.bean.CouponListBean;
import com.guo.qlzx.maxiansheng.util.ToolUtil;
import com.qlzx.mylibrary.base.RecyclerViewAdapter;
import com.qlzx.mylibrary.base.ViewHolderHelper;

/**
 * Created by 李 on 2018/4/17.
 */

public class CouponAdapter extends RecyclerViewAdapter<CouponListBean> {
    public CouponAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.item_coupon);
    }

    @Override
    protected void fillData(ViewHolderHelper viewHolderHelper, int position, CouponListBean model) {
        viewHolderHelper.setText(R.id.tv_price,"¥"+model.getMoney());
        viewHolderHelper.setText(R.id.tv_condition,"满"+model.getCondition()+"元使用");
        viewHolderHelper.setText(R.id.tv_title,model.getName());
        viewHolderHelper.setText(R.id.tv_content,"有效期 "+ ToolUtil.getStrTime(model.getUse_start_time(),"yyyy.MM.dd")+"-"+ToolUtil.getStrTime(model.getUse_end_time(),"yyyy.MM.dd"));
        viewHolderHelper.setItemChildClickListener(R.id.tv_btn);
    }
}
