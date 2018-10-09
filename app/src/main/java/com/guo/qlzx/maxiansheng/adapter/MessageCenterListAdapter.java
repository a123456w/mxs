package com.guo.qlzx.maxiansheng.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.guo.qlzx.maxiansheng.R;
import com.guo.qlzx.maxiansheng.bean.MessageCenterListBean;
import com.guo.qlzx.maxiansheng.util.ToolUtil;
import com.qlzx.mylibrary.base.RecyclerViewAdapter;
import com.qlzx.mylibrary.base.ViewHolderHelper;

/**
 * Created by 李 on 2018/4/27.
 */

public class MessageCenterListAdapter extends RecyclerViewAdapter<MessageCenterListBean> {
    public MessageCenterListAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.item_message_center);
    }

    @Override
    protected void fillData(ViewHolderHelper viewHolderHelper, int position, MessageCenterListBean model) {
        viewHolderHelper.setText(R.id.tv_title,model.getTitle());
        if (model.getStatus()==0){
            viewHolderHelper.setVisibility(R.id.iv_new, View.VISIBLE);
        }else {
            viewHolderHelper.setVisibility(R.id.iv_new, View.INVISIBLE);
        }
        viewHolderHelper.setText(R.id.tv_time,ToolUtil.getStrTime(model.getSend_time(),"yyyy/MM/dd"));
        viewHolderHelper.setText(R.id.tv_content,model.getContent());
        viewHolderHelper.setItemChildClickListener(R.id.btn_delete);
        viewHolderHelper.setItemChildClickListener(R.id.rl_inform);
    }
}
