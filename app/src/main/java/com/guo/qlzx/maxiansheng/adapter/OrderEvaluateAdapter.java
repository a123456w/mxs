package com.guo.qlzx.maxiansheng.adapter;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RatingBar;

import com.guo.qlzx.maxiansheng.R;
import com.guo.qlzx.maxiansheng.activity.EvaluateActivity;
import com.guo.qlzx.maxiansheng.bean.OrderEvaluateBean;
import com.qlzx.mylibrary.base.BaseListAdapter;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.util.GlideUtil;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/4/18 0018.
 */

public class OrderEvaluateAdapter extends BaseListAdapter<OrderEvaluateBean> {

    private CommentPicListAdapter adapter;
    private Map<String, List<String>> listMap = new LinkedHashMap<>();
    public static String scoreNum;


    public OrderEvaluateAdapter(ListView listView) {
        super(listView, R.layout.my_evaluate_listview_item);
    }

    @Override
    public void fillData(ViewHolder holder, int position, final OrderEvaluateBean model) {
        GlideUtil.display(mContext, Constants.IMG_HOST+model.getPic(), holder.getImageView(R.id.evaluate_goods_pic));

        adapter = new CommentPicListAdapter(mContext);
        GridView gridView = holder.getView(R.id.evaluate_goods_gr);
        gridView.setAdapter(adapter);
        adapter.setList(listMap.get(model.getId()));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EvaluateActivity mCon = (EvaluateActivity) OrderEvaluateAdapter.this.mContext;
                if (listMap.get(model.getId()) != null) {
                    if (listMap.get(model.getId()).size() <= position) {
                        mCon.DialogPic(model.getId(), 4 - listMap.get(model.getId()).size());
                    }
                } else {
                    mCon.DialogPic(model.getId(), 4);
                }

            }
        });
        RatingBar ratingBar = holder.getView(R.id.ratingBar);
        String score = model.getScore();
        if (score==null||"".equals(score)){
            ratingBar.setRating(0);
        }else {
            ratingBar.setRating(Float.valueOf(score));
        }

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                model.setScore(String.valueOf(v));
                scoreNum=String.valueOf(v);




            }
        });

        EditText editText = holder.getView(R.id.evaluate_goods_et);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                model.setContent(editable.toString());
            }
        });


    }

    public void setListMap(Map<String, List<String>> listMap) {
        if (listMap != null) {
            this.listMap = listMap;
        }
        notifyDataSetChanged();
    }

}
