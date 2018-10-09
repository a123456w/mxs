package com.guo.qlzx.maxiansheng.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.guo.qlzx.maxiansheng.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/18 0018.
 */

public class CommentPicListAdapter extends BaseAdapter {
    private List<String> list = new ArrayList<>();
    private Context context;
    private boolean show = true;

    public CommentPicListAdapter(Context context) {
        this.context = context;
    }

    public void setList(List<String> list) {
        if (list != null) {
            this.list = list;
        }
        notifyDataSetChanged();
    }
    public void onClear() {
        if (list != null) {
            this.list .clear();
        }
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        if (list != null) {
            return list.size() + 1;
        }
        return 1;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.item_publish_pic, null);
        ImageView item_publish_pic_img = convertView.findViewById(R.id.item_publish_pic_img);
        ImageView item_publish_pic_del = convertView.findViewById(R.id.item_publish_pic_del);
        ImageView item_publish_pic_img_occupied = convertView.findViewById(R.id.item_publish_pic_img_occupied);
        TextView item_publish_pic_text_occupied = convertView.findViewById(R.id.item_publish_pic_text_occupied);



        if (position < list.size()) {
            item_publish_pic_img.setVisibility(View.VISIBLE);
            Glide.with(context).load(list.get(position)).asBitmap().dontAnimate().fitCenter()
                    .placeholder(com.qlzx.mylibrary.R.drawable.ic_placeload)
                    .error(com.qlzx.mylibrary.R.drawable.ic_load_error)
                    .into(item_publish_pic_img);
            item_publish_pic_del.setVisibility(View.VISIBLE);
            item_publish_pic_del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    list.remove(position);
                    notifyDataSetChanged();
                }
            });
        } else {
            item_publish_pic_img_occupied.setVisibility(View.VISIBLE);
            item_publish_pic_text_occupied.setVisibility(View.VISIBLE);
            item_publish_pic_text_occupied.setText("添加图片");
            if (show) {
                item_publish_pic_img_occupied.setVisibility(View.GONE);
                item_publish_pic_text_occupied.setVisibility(View.GONE);
            } else {
                item_publish_pic_img_occupied.setVisibility(View.VISIBLE);
                item_publish_pic_text_occupied.setVisibility(View.VISIBLE);
            }
        }
        if (list.size() >= 9) {
            show = true;
        } else {
            show = false;
        }


        return convertView;
    }
}
