package com.guo.qlzx.maxiansheng.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.guo.qlzx.maxiansheng.R;
import com.guo.qlzx.maxiansheng.bean.SetFeedbackTypeListBean;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;

import java.util.List;

/**
 * Created by 李 on 2018/4/25.
 */

public class SetFeedbackTypeAdapter extends TagAdapter<SetFeedbackTypeListBean> {
    private Context context;
    public SetFeedbackTypeAdapter(List<SetFeedbackTypeListBean> datas, Context context) {
        super(datas);
        this.context=context;
    }

    @Override
    public View getView(FlowLayout parent, int position, SetFeedbackTypeListBean setFeedbackTypeListBean) {
        View view=LayoutInflater.from(context).inflate(R.layout.item_feedback_type,null);
        final CheckBox textView=(CheckBox) view.findViewById(R.id.cb_type);
        textView.setText(setFeedbackTypeListBean.getType_name());
        textView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                textView.setChecked(isChecked);
            }
        });
        return view;
    }
}
