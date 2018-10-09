package com.guo.qlzx.maxiansheng.adapter;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.guo.qlzx.maxiansheng.R;
import com.guo.qlzx.maxiansheng.bean.CouponListBean;
import com.guo.qlzx.maxiansheng.util.ToolUtil;
import com.qlzx.mylibrary.base.RecyclerViewAdapter;
import com.qlzx.mylibrary.base.ViewHolderHelper;

/**
 * Created by 李 on 2018/4/17.
 */

public class CouponLoseAdapter  extends RecyclerViewAdapter<CouponListBean> {
    public CouponLoseAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.item_coupon);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void fillData(ViewHolderHelper viewHolderHelper, int position, CouponListBean model) {
        viewHolderHelper.setText(R.id.tv_price,"¥"+model.getMoney());
        viewHolderHelper.setText(R.id.tv_condition,"满"+model.getCondition()+"元使用");
        viewHolderHelper.setText(R.id.tv_title,model.getName());
        viewHolderHelper.setText(R.id.tv_content,"有效期 "+ ToolUtil.getStrTime(model.getUse_start_time(),"yyyy.MM.dd")+"-"+ToolUtil.getStrTime(model.getUse_end_time(),"yyyy.MM.dd"));
        TextView textView=viewHolderHelper.getTextView(R.id.tv_btn);
        viewHolderHelper.setItemChildClickListener(R.id.tv_btn);
        TextView tv=viewHolderHelper.getTextView(R.id.tv_lose);
        LinearLayout  layout=(LinearLayout) viewHolderHelper.getView(R.id.ll_lose);
        textView.setVisibility(View.GONE);
        tv.setVisibility(View.VISIBLE);
        layout.setBackground(mContext.getResources().getDrawable(R.drawable.bg_coupon_lose));
    }
}
