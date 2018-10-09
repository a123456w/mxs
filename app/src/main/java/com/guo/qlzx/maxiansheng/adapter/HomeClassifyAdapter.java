package com.guo.qlzx.maxiansheng.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.yzl.lib.PullLeftToRefreshLayout;
import com.guo.qlzx.maxiansheng.R;
import com.guo.qlzx.maxiansheng.activity.ClassificationTwoActivity;
import com.guo.qlzx.maxiansheng.activity.ProductDetailsActivity;
import com.guo.qlzx.maxiansheng.bean.HomeActivityListBean;
import com.guo.qlzx.maxiansheng.bean.HomeClassifyBean;
import com.guo.qlzx.maxiansheng.bean.HomeClassifyListBean;
import com.guo.qlzx.maxiansheng.util.callback.OnLoadClickListener;
import com.guo.qlzx.maxiansheng.util.costom.HorizontalListView;
import com.qlzx.mylibrary.base.BaseListAdapter;
import com.qlzx.mylibrary.base.OnRVItemClickListener;
import com.qlzx.mylibrary.base.RecyclerViewAdapter;
import com.qlzx.mylibrary.base.ViewHolderHelper;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.util.GlideUtil;
import com.qlzx.mylibrary.util.ToastUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 李 on 2018/4/11.
 */

public class HomeClassifyAdapter extends BaseListAdapter<HomeClassifyBean> {

    public static int type=0;
    public HomeClassifyAdapter(ListView listView) {
        super(listView, R.layout.layout_action_session);
    }

    private OnLoadClickListener onLoadClickListener;

    public void setOnLoadClickListener(OnLoadClickListener onLoadClickListener) {
        this.onLoadClickListener = onLoadClickListener;
    }

    public void setType(int type){
        this.type=type;
    }
    @Override
    public void fillData(ViewHolder holder, final int position, final HomeClassifyBean model) {


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
                 intent.putExtra("goods_id", ""+adapter.getItem(position).getGoods_id());
                 mContext.startActivity(intent);
             }
         });
        plrv.setOnRefreshListener(new PullLeftToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (onLoadClickListener!=null){
                    onLoadClickListener.onLoadClick(position);
                }
            }
        });
        holder.getImageView(R.id.iv_ad).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ClassificationTwoActivity.class);
                intent.putExtra("name", model.getName());
                intent.putExtra("id",  ""+model.getId());
                mContext.startActivity(intent);
            }
        });
    }


    static class HomeActionSecondsKillAdapter extends RecyclerViewAdapter<HomeClassifyListBean> {
        private GestureDetector gestureDetector;
        public HomeActionSecondsKillAdapter(RecyclerView recyclerView) {
            super(recyclerView, R.layout.item_action_list);


        }

        @Override
        protected void fillData(ViewHolderHelper viewHolderHelper, int position, HomeClassifyListBean model) {
            GlideUtil.display(mContext, Constants.IMG_HOST + model.getImg(), viewHolderHelper.getImageView(R.id.iv_kill_img));
            viewHolderHelper.setText(R.id.tv_kill_title, model.getGoods_name());
            if (type==2){
                viewHolderHelper.setText(R.id.tv_kill_price, "¥" + model.getPlus_price());
            }else {
                viewHolderHelper.setText(R.id.tv_kill_price, "¥" + model.getShop_price());
            }
            viewHolderHelper.setText(R.id.tv_kill_unit, "/" + model.getUnit());
        }
    }
}
