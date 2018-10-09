package com.guo.qlzx.maxiansheng.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.guo.qlzx.maxiansheng.R;
import com.guo.qlzx.maxiansheng.bean.HomeActivityListBean;
import com.guo.qlzx.maxiansheng.bean.MemberCenterBean;
import com.guo.qlzx.maxiansheng.util.costom.HorizontalListView;
import com.guo.qlzx.maxiansheng.util.dialog.MemberCenterDialog;
import com.qlzx.mylibrary.base.BaseListAdapter;
import com.qlzx.mylibrary.util.GlideUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 李 on 2018/4/16.
 * 会员中心
 */

public class MemberCenterAdapter extends BaseAdapter {
    private List<MemberCenterBean.PlusListBean> list;
    private Context mContext;

    public MemberCenterAdapter(List<MemberCenterBean.PlusListBean> list,Context mContext) {
        this.list=list;
        this.mContext=mContext;
    }


    @Override
    public int getCount() {
        if (list.size()==0||list==null){
            return 0;
        }
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate( R.layout.item_member_center, parent, false);
            holder = new ViewHolder(convertView);
            holder.time=(TextView)convertView.findViewById(R.id.tv_time);
            holder. price=(TextView)convertView.findViewById(R.id.tv_time_left);
            holder. buy=(TextView)convertView.findViewById(R.id.tv_buy);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final MemberCenterBean.PlusListBean listBean=list.get(position);
        holder.time.setText(listBean.getMember_time()+"天");
        holder.price.setText("仅需"+listBean.getPrice()+"元");
        Shader shader =new LinearGradient(0, 0, 0, holder.time.getTextSize(),mContext.getResources().getColor(R.color.gold_d7),
                mContext.getResources().getColor(R.color.gold_9b), Shader.TileMode.CLAMP);
        holder.time.getPaint().setShader(shader);
        holder.time.getPaint().setFakeBoldText(true);
        holder.buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //立即购买 的监听
                if (onChildClickListener!=null){
                    onChildClickListener.onChildClick(listBean.getPrice(),listBean.getId());
                }
            }
        });
        return convertView;
    }

    class ViewHolder{
        @BindView(R.id.tv_time)
        TextView time;
        @BindView(R.id.tv_time_left)
        TextView price;
        @BindView(R.id.tv_buy)
        TextView buy;
        public ViewHolder(View view){
            ButterKnife.bind(this, view);
        }
    }
    OnChildClickListener onChildClickListener;
    public void setOnChildClickListener(OnChildClickListener onChildClickListener){
        this.onChildClickListener=onChildClickListener;
    }
    public interface OnChildClickListener{
        public void onChildClick(String price,int id);
    }
}
