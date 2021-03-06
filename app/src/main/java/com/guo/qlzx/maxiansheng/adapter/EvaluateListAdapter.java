package com.guo.qlzx.maxiansheng.adapter;

import android.widget.GridView;
import android.widget.ListView;
import android.widget.RatingBar;

import com.guo.qlzx.maxiansheng.R;
import com.guo.qlzx.maxiansheng.bean.EvaluateListBean;
import com.qlzx.mylibrary.base.BaseListAdapter;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.util.DateUtil;
import com.qlzx.mylibrary.util.GlideUtil;

import static com.qlzx.mylibrary.util.DateUtil.FORMAT_YMD;

/**
 * Created by Administrator on 2018/4/19 0019.
 * 评价列表适配器
 */

public class EvaluateListAdapter extends BaseListAdapter<EvaluateListBean> {

    private EvaluateListPicAdapter adapter;


    public EvaluateListAdapter(ListView listView) {
        super(listView, R.layout.item_list_evaluate);
    }

    @Override
    public void fillData(ViewHolder holder, int position, EvaluateListBean model) {
        GlideUtil.display(mContext, Constants.IMG_HOST + model.getPortrait(), holder.getImageView(R.id.icon_user));
        holder.setText(R.id.tv_user_name, model.getUsername());
        holder.setText(R.id.tv_time, DateUtil.toDate(model.getCreate_time(),FORMAT_YMD));
        ((RatingBar) holder.getView(R.id.rb_grade)).setRating(model.getDescribe_score());
        holder.setText(R.id.tv_number_of_orders, model.getContent());

        adapter = new EvaluateListPicAdapter((GridView) holder.getView(R.id.evaluate_gv));
        ((GridView) holder.getView(R.id.evaluate_gv)).setAdapter(adapter);
        adapter.setData(model.getImg());
    }
}
