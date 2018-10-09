package com.guo.qlzx.maxiansheng.adapter;

import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.guo.qlzx.maxiansheng.R;
import com.guo.qlzx.maxiansheng.bean.HomeNavigationBean;
import com.guo.qlzx.maxiansheng.bean.HomeTopBean;
import com.qlzx.mylibrary.base.BaseListAdapter;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.util.GlideUtil;

/**
 * Created by 李 on 2018/4/10.
 * 首页导航适配器
 */

public class HomeLableAdapter extends BaseListAdapter<HomeTopBean.NavigationBean.ClassifyBean> {
    public HomeLableAdapter(GridView gridView) {
        super(gridView, R.layout.item_home_menu);
    }

    @Override
    public void fillData(ViewHolder holder, int position, HomeTopBean.NavigationBean.ClassifyBean model) {
        holder.setText(R.id.tv_menu,model.getName());
        if (pos!=0){
            if (position<pos){
                GlideUtil.display(mContext, Constants.IMG_HOST+model.getImg(),holder.getImageView(R.id.iv_img));
            }else {
                holder.getImageView(R.id.iv_img).setImageResource(Integer.valueOf(model.getImg()));
            }
        }else {
            holder.getImageView(R.id.iv_img).setImageResource(Integer.valueOf(model.getImg()));
        }
    }
    private int pos=0;

    public void setPos(int pos) {
        this.pos = pos;
    }
}
