package com.guo.qlzx.maxiansheng.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.guo.qlzx.maxiansheng.R;
import com.guo.qlzx.maxiansheng.bean.ShowSearchBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Djr on 2018/1/15.
 * 产地列表
 */

public class FlowStyleProductAdapter extends RecyclerView.Adapter<FlowStyleProductAdapter.FlowStyleHolder> {

    private List<ShowSearchBean.RegionBean> list = new ArrayList<>();
    private ShowSearchBean.RegionBean selectDes;
    private Context context;

    public FlowStyleProductAdapter(Context context) {
        this.context = context;
    }

    public void setList(List<ShowSearchBean.RegionBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public List<ShowSearchBean.RegionBean> getList() {
        return list;
    }

    @Override
    public FlowStyleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FlowStyleHolder(View.inflate(context, R.layout.flow_item, null));
    }

    @Override
    public void onBindViewHolder(FlowStyleHolder holder, final int position) {
        final ShowSearchBean.RegionBean des = list.get(position);
        if (des.isTrue()) {
            holder.text.setBackground(context.getResources().getDrawable(R.drawable.product_item_select_back));
            holder.text.setTextColor(Color.parseColor("#FFFFFF"));
        } else {
            holder.text.setBackground(context.getResources().getDrawable(R.drawable.product_item_back));
            holder.text.setTextColor(Color.parseColor("#606060"));
        }
        holder.text.setText(des.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (des != selectDes) {
                    if (selectDes != null) {
                        selectDes.setTrue(false);
                    }
                }
                for (int i=0;i<list.size();i++){
                    list.get(i).setTrue(false);
                }
                des.setTrue(true);
                selectDes = des;

                if (listner != null) {
                    listner.onItemClick(v, position);
                }
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class FlowStyleHolder extends RecyclerView.ViewHolder {

        private TextView text;

        public FlowStyleHolder(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.flow_text);
        }
    }


    //点击事件；
    private OnClickRecyclerTypeProductListner listner;

    //设置点击事件；
    public void setLisner(OnClickRecyclerTypeProductListner lisner) {
        this.listner = lisner;
    }

    //创建一个接口的内部类 或者直接在外面创建也是可以的
    public interface OnClickRecyclerTypeProductListner {
        //点击事件
        void onItemClick(View view, int position);
    }
}
