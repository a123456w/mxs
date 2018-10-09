package com.guo.qlzx.maxiansheng.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.guo.qlzx.maxiansheng.R;
import com.guo.qlzx.maxiansheng.bean.HomeActivityListBean;
import com.guo.qlzx.maxiansheng.bean.HomeClassifyListBean;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.util.GlideUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 李 on 2018/4/11.
 * 首页横向滚动列表
 */

public class HomeClassifyListAdapter extends BaseAdapter{
    private List<HomeClassifyListBean> list;
    private Context mContext;
    public HomeClassifyListAdapter( List<HomeClassifyListBean> list, Context mContext) {
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
            convertView = LayoutInflater.from(mContext).inflate( R.layout.item_action_session, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        HomeClassifyListBean listBean=list.get(position);
        holder.title.setText(listBean.getGoods_name());
        holder.price.setText("¥"+listBean.getShop_price());
        if (!TextUtils.isEmpty(listBean.getUnit())){
            holder.unit.setText("/"+listBean.getUnit());
        }

        GlideUtil.display(mContext, Constants.IMG_HOST+listBean.getImg(),holder.img);

        return convertView;
    }
    public class ViewHolder {

        @BindView(R.id.tv_title)
        TextView title;
        @BindView(R.id.tv_price)
        TextView price;
        @BindView(R.id.tv_unit)
        TextView unit;
        @BindView(R.id.iv_img)
        ImageView img;
        public ViewHolder(View view){
            ButterKnife.bind(this, view);
        }
    }
}
