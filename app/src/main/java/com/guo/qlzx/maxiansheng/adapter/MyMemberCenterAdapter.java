package com.guo.qlzx.maxiansheng.adapter;

import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.guo.qlzx.maxiansheng.R;
import com.guo.qlzx.maxiansheng.bean.MemberCenterBean;
import com.qlzx.mylibrary.base.RecyclerViewAdapter;
import com.qlzx.mylibrary.base.ViewHolderHelper;

public class MyMemberCenterAdapter extends RecyclerViewAdapter<MemberCenterBean.PlusListBean> {
    public MyMemberCenterAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.item_member_center);
    }

    @Override
    protected void fillData(ViewHolderHelper viewHolderHelper, int position, MemberCenterBean.PlusListBean model) {
        viewHolderHelper.setText(R.id.tv_time, model.getMember_time() + "天");
        viewHolderHelper.setText(R.id.tv_time_left, "仅需"+model.getPrice() + "元");
        TextView tvTime = viewHolderHelper.getTextView(R.id.tv_time);
        Shader shader = new LinearGradient(0, 0, 0, tvTime.getTextSize(), mContext.getResources().getColor(R.color.gold_d7),
                mContext.getResources().getColor(R.color.gold_9b), Shader.TileMode.CLAMP);
        tvTime.getPaint().setShader(shader);
        viewHolderHelper.setItemChildClickListener(R.id.tv_buy);


    }
}
