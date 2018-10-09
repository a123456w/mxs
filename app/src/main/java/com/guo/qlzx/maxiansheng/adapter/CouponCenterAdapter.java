package com.guo.qlzx.maxiansheng.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.guo.qlzx.maxiansheng.R;
import com.guo.qlzx.maxiansheng.bean.OrderCouponListBean;
import com.guo.qlzx.maxiansheng.util.ToolUtil;
import com.qlzx.mylibrary.base.RecyclerViewAdapter;
import com.qlzx.mylibrary.base.ViewHolderHelper;

/**
 * Created by 李 on 2018/4/27.
 */

public class CouponCenterAdapter extends RecyclerViewAdapter<OrderCouponListBean> {
    public CouponCenterAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.item_coupon);
    }

    private int type=0;
    @Override
    protected void fillData(ViewHolderHelper viewHolderHelper, int position, OrderCouponListBean model) {
        TextView lose=viewHolderHelper.getTextView(R.id.tv_lose);
        TextView btn=viewHolderHelper.getTextView(R.id.tv_btn);
        if (type==1){
           btn.setText("立即领取");
            if (model.getType()==1){
                btn.setVisibility(View.GONE);
                lose.setVisibility(View.VISIBLE);
                lose.setEnabled(false);
                lose.setText("已领取");
                if (model.getStatus()==2){
                    btn.setVisibility(View.GONE);
                    lose.setVisibility(View.VISIBLE);
                    lose.setEnabled(false);
                    lose.setText("已失效");}
            }else {
                btn.setVisibility(View.VISIBLE);
                lose.setVisibility(View.GONE);
                if (model.getStatus()==2){
                    btn.setVisibility(View.GONE);
                    lose.setVisibility(View.VISIBLE);
                    lose.setEnabled(false);
                    lose.setText("已失效");}
            }
        }else {
            viewHolderHelper.setText(R.id.tv_btn,"立即使用");
            if (model.getStatus()==2){
                btn.setVisibility(View.GONE);
                lose.setVisibility(View.VISIBLE);
                lose.setEnabled(false);
                lose.setText("已失效");}
        }

        viewHolderHelper.setText(R.id.tv_price,"¥"+model.getMoney());
        viewHolderHelper.setText(R.id.tv_condition,"满"+model.getCondition()+"元使用");
        viewHolderHelper.setText(R.id.tv_title,model.getName());
        viewHolderHelper.setText(R.id.tv_content,"有效期"+ ToolUtil.getStrTime(model.getUse_start_time()+"","yyyy.MM.dd")+"-"+ToolUtil.getStrTime(model.getUse_end_time()+"","yyyy.MM.dd"));

        viewHolderHelper.setItemChildClickListener(R.id.tv_btn);
    }

    // 1为领券中心进入，0为其他
    public void setType(int type) {
        this.type = type;
    }
}
