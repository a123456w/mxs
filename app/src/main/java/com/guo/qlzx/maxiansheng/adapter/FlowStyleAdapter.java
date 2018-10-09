package com.guo.qlzx.maxiansheng.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.guo.qlzx.maxiansheng.R;
import com.guo.qlzx.maxiansheng.bean.TwoCategoryBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Djr on 2018/1/15.
 */

public class FlowStyleAdapter extends RecyclerView.Adapter<FlowStyleAdapter.FlowStyleHolder> {

    private List<TwoCategoryBean.SonBean> list = new ArrayList<>();
    private TwoCategoryBean.SonBean selectDes = new TwoCategoryBean.SonBean();
    private Context context;

    public FlowStyleAdapter(Context context) {
        this.context = context;
    }

    public void setList(List<TwoCategoryBean.SonBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public List<TwoCategoryBean.SonBean> getlist(){
        return list;
    }

    @Override
    public FlowStyleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FlowStyleHolder(View.inflate(context, R.layout.flow_item, null));
    }

    @Override
    public void onBindViewHolder(FlowStyleHolder holder, final int position) {
        final TwoCategoryBean.SonBean des = list.get(position);
        if (des.isSelect()) {
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
//                if (des != selectDes) {
//                    if (selectDes != null) {
//                        selectDes.setSelect(false);
//                    }
//                }
//                des.setSelect(true);
//                selectDes = des;
//                notifyDataSetChanged();
                if (listner != null) {
                    listner.onItemClick(v, position);
                }
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
    private OnClickRecyclerTypeListner listner;

    //设置点击事件；
    public void setLisner(OnClickRecyclerTypeListner lisner) {
        this.listner = lisner;
    }

    //创建一个接口的内部类 或者直接在外面创建也是可以的
    public interface OnClickRecyclerTypeListner {
        //点击事件
        void onItemClick(View view, int position);
    }
}
