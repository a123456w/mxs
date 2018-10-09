package com.guo.qlzx.maxiansheng.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.guo.qlzx.maxiansheng.R;
import com.guo.qlzx.maxiansheng.bean.FootListBean;
import com.guo.qlzx.maxiansheng.bean.FootMarkListBean;
import com.guo.qlzx.maxiansheng.util.ToolUtil;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.util.GlideUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 李 on 2018/4/17.
 */

public class FootMarkAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private List<FootMarkListBean> data=new ArrayList<>();
    private OnItemChildClickListener onItemChildClickListener;
    private OnItemChildLongClickListener onItemChildLongClickListener;
    private Map<Integer,Map<Integer,Boolean>> map=new HashMap<>();
    private Map<Integer,Boolean> headMap=new HashMap<>();
    public FootMarkAdapter(Context mContext){
        this.mContext=mContext;
    }
    @Override
    public int getGroupCount() {
        return data.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return data.get(groupPosition).getFootlist().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return data.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return data.get(groupPosition).getFootlist().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        FootMarkListBean footMarkListBean = data.get(groupPosition);
        HeadViewHolder holder = null;
        if (convertView==null){
            holder=new HeadViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_head_footmark,parent,false);
            holder.textView= (TextView) convertView.findViewById(R.id.tv_name);
            holder.checkBox=(CheckBox)convertView.findViewById(R.id.cb_head);
            convertView.setTag(holder);
        }else {
            holder= (HeadViewHolder) convertView.getTag();
        }
        holder.checkBox.setChecked(headMap.get(groupPosition));
        holder.textView.setText(footMarkListBean.getTime());
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(buttonView.isPressed()) {
                    Map<Integer,Boolean>  booleanMap=new HashMap<>();
                    for (int i=0;i<data.get(groupPosition).getFootlist().size();i++){
                        booleanMap.put(i,isChecked);
                    }
                    map.put(groupPosition, booleanMap);
                    headMap.put(groupPosition,isChecked);
                    notifyDataSetChanged();
                }
            }
        });
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        FootListBean goodsBean = data.get(groupPosition).getFootlist().get(childPosition);

        BodyViewHolder holder = null;
        if (convertView==null){
            holder=new BodyViewHolder();
            convertView= LayoutInflater.from(mContext).inflate(R.layout.item_body_footmark,parent,false);
            holder.content= (TextView) convertView.findViewById(R.id.tv_content);
            holder.price= (TextView) convertView.findViewById(R.id.tv_price);
            holder.title= (TextView) convertView.findViewById(R.id.tv_title);
            holder.checkBox= (CheckBox) convertView.findViewById(R.id.cb_body);
            holder.img= (ImageView) convertView.findViewById(R.id.iv_img);
            holder.btn= (ImageView) convertView.findViewById(R.id.iv_add_btn);
            holder.states=(TextView)convertView.findViewById(R.id.tv_status);
            convertView.setTag(holder);
        }else {
            holder= (BodyViewHolder) convertView.getTag();
        }

        holder.content.setText("规格："+goodsBean.getKey_name());
        if (goodsBean.getType()==0){
            holder.price.setText("¥"+goodsBean.getShop_price()+"/"+goodsBean.getUnit());
            holder.states.setVisibility(View.GONE);
            holder.content.setVisibility(View.VISIBLE);
            holder.btn.setVisibility(View.VISIBLE );
        }else {
            holder.price.setText("¥"+goodsBean.getPlus_price()+"/"+goodsBean.getUnit());
            holder.states.setVisibility(View.VISIBLE);
            holder.content.setVisibility(View.GONE);
            holder.btn.setVisibility(View.GONE );
            if (goodsBean.getType()==1){
                holder.states.setText("秒杀");
            }else if (goodsBean.getType()==2){
                holder.states.setText("团购");
            }else if (goodsBean.getType()==3){
                holder.states.setText("预售");
            }
        }
        holder.title.setText(goodsBean.getGoods_name());
        Map<Integer ,Boolean> map1=map.get(groupPosition);

        GlideUtil.display(mContext, Constants.IMG_HOST+goodsBean.getImg(),holder.img);
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                map.get(groupPosition).put(childPosition,isChecked);
                if (headMap.get(groupPosition)){
                    headMap.put(groupPosition,false);
                }
                boolean isTrue=false;
                for (int i=0;i<map.get(groupPosition).size();i++){
                    if (!map.get(groupPosition).get(i)){
                        isTrue=false;
                       break;
                    }else {
                        isTrue=true;
                    }
                }
                if (isTrue){
                    headMap.put(groupPosition,isTrue);
                }
                /*if (!isChecked){
                    headMap.put(groupPosition,isChecked);
                }*/
                notifyDataSetChanged();
            }
        });
        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onAddGoodsClickListener!=null){
                    onAddGoodsClickListener.onAddGoods(groupPosition,childPosition);
                }
            }
        });
        holder.checkBox.setChecked(map1.get(childPosition));
        setClick(convertView,groupPosition,childPosition);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
    class HeadViewHolder{
        CheckBox checkBox;
        TextView textView;
    }
    class BodyViewHolder{
        CheckBox checkBox;
        TextView title;
        ImageView img;
        ImageView btn;
        TextView price;
        TextView content;
        TextView states;
    }


    public List<FootMarkListBean> getData() {
        return data;
    }

    public void setData(List<FootMarkListBean> data) {
        if (data!=null){
            this.data.clear();
            this.data.addAll(data);
            headMap.clear();
            map.clear();
            if (this.data.size()>0){
                for(int i=0;i<this.data.size();i++){

                    Map<Integer,Boolean> booleanMap=new HashMap<>();

                        for (int j=0;j<this.data.get(i).getFootlist().size();j++){
                            booleanMap.put(j,false);
                         }

                    map.put(i, booleanMap);
                    headMap.put(i,false);
                }
            }

            notifyDataSetChanged();
        }else {
            this.data.clear();
            map.clear();
            notifyDataSetChanged();
        }
    }

    public void addData(List<FootMarkListBean> data,List<Integer> headYears){
        this.data.addAll(data);
        for(int i=0;i<this.data.size();i++){
            headMap.put(i,false);
            for (int j=0;j<this.data.get(i).getFootlist().size();j++){
                Map<Integer,Boolean> booleanMap=new HashMap<>();
                booleanMap.put(j,false);
                map.put(i, booleanMap);
            }
        }
        notifyDataSetChanged();
    }
    private void setClick(final View view, final int groupPosition, final int childPosition){
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemChildClickListener!=null){
                    onItemChildClickListener.onItemChildClick(view,groupPosition,childPosition);
                }
            }
        });
    }
    public Map<Integer,Map<Integer,Boolean>> getMap(){
        return map;
    }
    public void clearMap(){
        return;
    }


    public interface OnItemChildClickListener{
        void onItemChildClick(View view, int groupPosition, int childPosition);
    }


    public void setOnItemChildClickListener(OnItemChildClickListener onItemChildClickListener){
        this.onItemChildClickListener=onItemChildClickListener;
    }

    public interface OnItemChildLongClickListener{
        void onItemLongChildClick(View view, int groupPosition, int childPosition);
    }

    public void setOnItemChildLongClickListener(OnItemChildLongClickListener onItemChildLongClickListener){
        this.onItemChildLongClickListener=onItemChildLongClickListener;
    }
    private OnAddGoodsClickListener onAddGoodsClickListener;
    public void setOnAddGoodsClickListener(OnAddGoodsClickListener onAddGoodsClickListener){
        this.onAddGoodsClickListener=onAddGoodsClickListener;
    }
    public interface OnAddGoodsClickListener{
        public void onAddGoods(int groupPos,int childPos);
    }
}
