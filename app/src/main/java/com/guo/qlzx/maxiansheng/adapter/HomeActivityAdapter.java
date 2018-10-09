package com.guo.qlzx.maxiansheng.adapter;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.yzl.lib.PullLeftToRefreshLayout;
import com.guo.qlzx.maxiansheng.R;
import com.guo.qlzx.maxiansheng.activity.KillSessionActivity;
import com.guo.qlzx.maxiansheng.activity.ProductDetailsActivity;
import com.guo.qlzx.maxiansheng.bean.HomeActivityBean;
import com.guo.qlzx.maxiansheng.bean.HomeActivityListBean;
import com.guo.qlzx.maxiansheng.util.callback.OnActivityLoadClickListener;
import com.qlzx.mylibrary.base.BaseListAdapter;
import com.qlzx.mylibrary.base.OnRVItemClickListener;
import com.qlzx.mylibrary.base.RecyclerViewAdapter;
import com.qlzx.mylibrary.base.ViewHolderHelper;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.util.GlideUtil;
import com.qlzx.mylibrary.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 李 on 2018/4/10.
 */

public class HomeActivityAdapter extends BaseListAdapter<HomeActivityBean> {
    public HomeActivityAdapter(ListView listView) {
        super(listView, R.layout.layout_action_session);
    }

    private OnActivityLoadClickListener onActivityLoadClickListener;

    public void setOnActivityLoadClickListener(OnActivityLoadClickListener onActivityLoadClickListener) {
        this.onActivityLoadClickListener = onActivityLoadClickListener;
    }

    @Override
    public void fillData(ViewHolder holder, final int position, final HomeActivityBean model) {
        List<HomeActivityListBean> homeList = new ArrayList<>();

        holder.setText(R.id.tv_title, model.getName());
        GlideUtil.display(mContext, Constants.IMG_HOST + model.getImg(), holder.getImageView(R.id.iv_ad));

        RecyclerView horizontalListView = (RecyclerView) holder.getView(R.id.hlv_goods);
        PullLeftToRefreshLayout plrv  = (PullLeftToRefreshLayout) holder.getView(R.id.plrl);

        final HomeActionSecondsKillAdapter adapter = new HomeActionSecondsKillAdapter(horizontalListView);
        horizontalListView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        horizontalListView.setAdapter(adapter);
        adapter.setData(model.getList());
        adapter.setOnRVItemClickListener(new OnRVItemClickListener() {
            @Override
            public void onRVItemClick(ViewGroup parent, View itemView, int position) {
                Intent intent = new Intent(mContext, ProductDetailsActivity.class);
                intent.putExtra("goods_id",String.valueOf(adapter.getItem(position).getGoods_id()));
                mContext.startActivity(intent);
            }
        });
        plrv.setOnRefreshListener(new PullLeftToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
              if (onActivityLoadClickListener!=null){
                  onActivityLoadClickListener.onActivityLoad(position);
              }
            }
        });

        holder.getImageView(R.id.iv_ad).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, KillSessionActivity.class);
                intent.putExtra("TYPEID",model.getType());
                mContext.startActivity(intent);
            }
        });


    }


    static class HomeActionSecondsKillAdapter extends RecyclerViewAdapter<HomeActivityListBean>  {
        private GestureDetector gestureDetector;
        public HomeActionSecondsKillAdapter(RecyclerView recyclerView) {
            super(recyclerView, R.layout.item_action_list);


        }

        @Override
        protected void fillData(ViewHolderHelper viewHolderHelper, int position, HomeActivityListBean model) {
            GlideUtil.display(mContext, Constants.IMG_HOST + model.getImg(), viewHolderHelper.getImageView(R.id.iv_kill_img));
            viewHolderHelper.setText(R.id.tv_kill_title, model.getGoods_name());
            viewHolderHelper.setText(R.id.tv_kill_price, "¥" + model.getPrice());
            viewHolderHelper.setText(R.id.tv_kill_unit, "/" + model.getUnit());

        }


    }
}

