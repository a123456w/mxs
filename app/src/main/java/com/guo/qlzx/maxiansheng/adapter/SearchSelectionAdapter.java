package com.guo.qlzx.maxiansheng.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.guo.qlzx.maxiansheng.R;
import com.guo.qlzx.maxiansheng.activity.SearchSelectionActivity;
import com.guo.qlzx.maxiansheng.bean.PoiAddressBean;
import com.qlzx.mylibrary.base.RecyclerViewAdapter;
import com.qlzx.mylibrary.base.ViewHolderHelper;


/**
 * author           Alpha58
 * time             2017/2/25 10:48
 * desc	            ${TODO}
 * <p>
 * upDateAuthor     $Author$
 * upDate           $Date$
 * upDateDesc       ${TODO}
 */
public class SearchSelectionAdapter extends RecyclerViewAdapter<PoiAddressBean> {

    public SearchSelectionAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.item_poi_keyword_search_title_and_content);
    }


    @Override
    protected void fillData(ViewHolderHelper holder, final int position, final PoiAddressBean model) {
        holder.setText(R.id.tv_detailAddress, model.getDetailAddress());
        holder.setText(R.id.tv_content, model.getText());
        holder.getView(R.id.ll_item_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((SearchSelectionActivity) mContext).setDetailAddress(model);
            }
        });
    }
}
