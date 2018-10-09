package com.guo.qlzx.maxiansheng.adapter;

import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.guo.qlzx.maxiansheng.R;
import com.guo.qlzx.maxiansheng.bean.ActivityListBean;
import com.guo.qlzx.maxiansheng.common.Constants;
import com.qlzx.mylibrary.base.RecyclerViewAdapter;
import com.qlzx.mylibrary.base.ViewHolderHelper;
import com.qlzx.mylibrary.util.GlideUtil;

import java.text.DecimalFormat;

import cn.iwgang.countdownview.CountdownView;

/**
 * Created by 李 on 2018/4/16.
 * 秒杀专场/团购专场/预售专场
 */

public class ActivityListAdapter extends RecyclerViewAdapter<ActivityListBean> {

    private int type=0;

    public ActivityListAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.item_kill_session);
    }

    @Override
    protected void fillData(ViewHolderHelper holderHelper, int position, ActivityListBean model) {
        GlideUtil.display(mContext, com.qlzx.mylibrary.common.Constants.IMG_HOST+model.getImg(),holderHelper.getImageView(R.id.iv_img));
        holderHelper.setText(R.id.tv_title,model.getGoods_name());
        holderHelper.setText(R.id.tv_content,model.getGoods_remark());
        holderHelper.setText(R.id.tv_new_price,"¥"+model.getPrice());
        holderHelper.setText(R.id.tv_new_unit,"/"+model.getUnit());
        ProgressBar progressBar=(ProgressBar) holderHelper.getView(R.id.pb_rate);
        CountdownView countdownView=(CountdownView)holderHelper.getView(R.id.cdv_time);
//        if (type == 3) {
            progressBar.setVisibility(View.VISIBLE);
            countdownView.setVisibility(View.GONE);
            //progressBar
            float num=100;
            String p="0";
            if (model.getStore_count()!=0){
                num=(float)model.getNumber()/model.getStore_count()*100;
                DecimalFormat decimalFormat=new DecimalFormat(".0");//构造方法的字符格式这里如果小数不足2位,会以0补足.
                p=decimalFormat.format(num);
            }
            holderHelper.setText(R.id.tv_rate,"已秒"+p+"%");

            progressBar.setProgress((int)num);


//        }else {
//            progressBar.setVisibility(View.GONE);
//            countdownView.setVisibility(View.VISIBLE);
//            countdownView.start(model.getPresel_time());
//            holderHelper.setText(R.id.tv_rate,"倒计时");
//        }
        //原价
        TextView price=holderHelper.getTextView(R.id.tv_old_price);
        price.setText("¥"+model.getShop_price());
        //设置删除线
        price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        //button点击
        holderHelper.setItemChildClickListener(R.id.tv_btn);
    }
    public void setType(int type){
        this.type=type;
    }
}
