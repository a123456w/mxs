package com.guo.qlzx.maxiansheng.adapter;

import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.guo.qlzx.maxiansheng.R;
import com.guo.qlzx.maxiansheng.activity.OrderDetailActivity;
import com.guo.qlzx.maxiansheng.bean.OrdersBean;
import com.qlzx.mylibrary.base.OnRVItemClickListener;
import com.qlzx.mylibrary.base.RecyclerViewAdapter;
import com.qlzx.mylibrary.base.ViewHolderHelper;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.util.GlideUtil;

/**
 * Created by Administrator on 2018/4/16 0016.
 */

public class OrderAllAdapter extends RecyclerViewAdapter<OrdersBean> {

    private GoodsPicAdapter adapter;

    public OrderAllAdapter(RecyclerView recyclerView) {
        super(recyclerView, R.layout.item_order_all);
    }

    @Override
    protected void fillData(final ViewHolderHelper helper, int position, final OrdersBean model) {
        helper.setText(R.id.tv_order_id, model.getOrder_sn());
        helper.setText(R.id.tv_total_money, "¥"+model.getOrder_amount());
        if (model.getOrder() != null && model.getOrder().size() > 1) {
            //多个商品
            helper.getView(R.id.ll_one_goods).setVisibility(View.GONE);
            helper.getView(R.id.rl_goods).setVisibility(View.VISIBLE);
            helper.setText(R.id.tv_all_goods, "共" + model.getOrder().size() + "件商品");

            RecyclerView  recyclerView= (RecyclerView) helper.getView(R.id.lv_goods);
            LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            recyclerView.setLayoutManager(layoutManager);
            adapter = new GoodsPicAdapter(recyclerView);
            recyclerView.setAdapter(adapter);
            adapter.setData(model.getOrder());
            adapter.setOnRVItemClickListener(new OnRVItemClickListener() {
                @Override
                public void onRVItemClick(ViewGroup parent, View itemView, int i) {
                    Intent intent = new Intent(mContext, OrderDetailActivity.class);
                    intent.putExtra("order_id",model.getOrder_id());
                    mContext.startActivity(intent);
                }
            });

        } else if (model.getOrder() != null && model.getOrder().size() ==1){
            //一个商品
            helper.getView(R.id.rl_goods).setVisibility(View.GONE);
            helper.getView(R.id.ll_one_goods).setVisibility(View.VISIBLE);
            GlideUtil.display(mContext, Constants.IMG_HOST + model.getOrder().get(0).getOriginal_img(), helper.getImageView(R.id.iv_goods));
            helper.setText(R.id.tv_goods_name, model.getOrder().get(0).getGoods_name());
            helper.setText(R.id.tv_spec, model.getOrder().get(0).getSpec_key_name());
            helper.setText(R.id.tv_one_price, "¥" + model.getOrder().get(0).getGoods_price());
            helper.setText(R.id.tv_new_price, "¥" + model.getOrder().get(0).getPrice());
            helper.setText(R.id.tv_old_price, "¥" + model.getOrder().get(0).getMarket_price());
            helper.getTextView(R.id.tv_old_price).getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            helper.setText(R.id.tv_number, "x " + model.getOrder().get(0).getGoods_num());
        }

        setButtonVisible(helper,model.getOrder_type());

        helper.setItemChildClickListener(R.id.tv_bottom1);
        helper.setItemChildClickListener(R.id.tv_bottom2);
        helper.setItemChildClickListener(R.id.tv_bottom3);


    }



    /**
     * 设置下方显示那些按钮
     * @param helper
     * @param type
     */
    private void setButtonVisible(ViewHolderHelper helper,int type){
        switch (type) {
            case 1:
                helper.getView(R.id.tv_bottom3).setVisibility(View.GONE);
                helper.getView(R.id.tv_bottom2).setVisibility(View.VISIBLE);
                helper.getView(R.id.tv_bottom1).setVisibility(View.VISIBLE);
                helper.setText(R.id.tv_bottom2,"查看物流");
                helper.setText(R.id.tv_bottom1,"确认收货");

                helper.setText(R.id.tv_order_state, "卖家已发货");
                break;
            case 2:
                helper.getView(R.id.tv_bottom3).setVisibility(View.VISIBLE);
                helper.getView(R.id.tv_bottom2).setVisibility(View.VISIBLE);
                helper.getView(R.id.tv_bottom1).setVisibility(View.VISIBLE);
                helper.setText(R.id.tv_bottom2,"删除订单");
                helper.setText(R.id.tv_bottom1,"立即评价");
                helper.setText(R.id.tv_bottom3,"查看物流");

                helper.setText(R.id.tv_order_state, "交易成功");
                break;
            case 3:
                helper.getView(R.id.tv_bottom3).setVisibility(View.GONE);
                helper.getView(R.id.tv_bottom2).setVisibility(View.GONE);
                helper.getView(R.id.tv_bottom1).setVisibility(View.VISIBLE);
                helper.setText(R.id.tv_bottom1,"删除订单");

                helper.setText(R.id.tv_order_state, "订单关闭");
                break;
            case 4:
                helper.getView(R.id.tv_bottom3).setVisibility(View.VISIBLE);
                helper.getView(R.id.tv_bottom2).setVisibility(View.VISIBLE);
                helper.getView(R.id.tv_bottom1).setVisibility(View.VISIBLE);
                helper.setText(R.id.tv_bottom3,"联系卖家");
                helper.setText(R.id.tv_bottom2,"取消订单");
                helper.setText(R.id.tv_bottom1,"立即付款");

                helper.setText(R.id.tv_order_state, "等待付款");
                break;
            case 5:
                helper.getView(R.id.tv_bottom3).setVisibility(View.VISIBLE);
                helper.getView(R.id.tv_bottom2).setVisibility(View.VISIBLE);
                helper.getView(R.id.tv_bottom1).setVisibility(View.VISIBLE);
                helper.setText(R.id.tv_bottom3,"退货");
                helper.setText(R.id.tv_bottom2,"联系卖家");
                helper.setText(R.id.tv_bottom1,"提醒发货");

                helper.setText(R.id.tv_order_state, "等待发货");
                break;
            case 6:
                helper.getView(R.id.tv_bottom3).setVisibility(View.GONE);
                helper.getView(R.id.tv_bottom2).setVisibility(View.GONE);
                helper.getView(R.id.tv_bottom1).setVisibility(View.VISIBLE);
                helper.setText(R.id.tv_bottom1,"删除订单");
                helper.setText(R.id.tv_bottom2,"确认收货");

                helper.setText(R.id.tv_order_state, "交易成功");
                break;
            default:
                helper.getView(R.id.tv_bottom3).setVisibility(View.GONE);
                helper.getView(R.id.tv_bottom2).setVisibility(View.GONE);
                helper.getView(R.id.tv_bottom1).setVisibility(View.VISIBLE);
                helper.setText(R.id.tv_bottom1,"删除订单");
        }
    }

}
