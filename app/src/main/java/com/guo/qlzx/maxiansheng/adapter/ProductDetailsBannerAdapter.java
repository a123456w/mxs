package com.guo.qlzx.maxiansheng.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.guo.qlzx.maxiansheng.R;
import com.guo.qlzx.maxiansheng.activity.ProductDetailsActivity;
import com.guo.qlzx.maxiansheng.util.CreateBitmap;
import com.qlzx.mylibrary.common.Constants;
import com.qlzx.mylibrary.util.GlideUtil;

import java.util.ArrayList;

import cn.bingoogolapple.photopicker.activity.BGAPhotoPreviewActivity;
import cn.jzvd.JZMediaManager;
import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;

/**
 * Created by Administrator on 2018/4/26 0026.
 */

public class ProductDetailsBannerAdapter extends PagerAdapter {

    private Context mContext;
    private ArrayList<View> viewLists = new ArrayList<>();
    private String video_url;
    private ArrayList<String> img_list;
    private String img;
    private JZVideoPlayerStandard jzVideoPlayerStandard = null;

    public ProductDetailsBannerAdapter(Context mContext, ArrayList<View> viewLists, String video_url, ArrayList<String> img_list,String img) {
        this.mContext = mContext;
        this.viewLists = viewLists;
        this.video_url = video_url;
        this.img_list = img_list;
        this.img = img;
    }

    @Override
    public int getCount() {
        return viewLists.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        container.addView(viewLists.get(position));
        if (position == 0) {
            jzVideoPlayerStandard = (JZVideoPlayerStandard) viewLists.get(0).findViewById(R.id.videoplayer);
            jzVideoPlayerStandard.setUp(Constants.IMG_HOST + video_url
                    , JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, "");
            jzVideoPlayerStandard.thumbImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            ViewGroup.LayoutParams params = jzVideoPlayerStandard.getLayoutParams();
            WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            final Display display = wm.getDefaultDisplay();
            Bitmap bitmap=CreateBitmap.createBitmapFromVideoPath(Constants.IMG_HOST + video_url, MediaStore.Images.Thumbnails.MINI_KIND);
            jzVideoPlayerStandard.thumbImageView.setImageBitmap(bitmap);
            //GlideUtil.display(mContext, Constants.IMG_HOST + img, jzVideoPlayerStandard.thumbImageView);

        } else {
            ImageView imageView = viewLists.get(position).findViewById(R.id.iv_banner);
            GlideUtil.display(mContext,  img_list.get(position - 1), imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mContext.startActivity((BGAPhotoPreviewActivity.newIntent(mContext, null, img_list, position-1)));
                }
            });
        }
        return viewLists.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(viewLists.get(position));
    }

    public JZVideoPlayerStandard getJzVideoPlayerStandard() {
        return jzVideoPlayerStandard;
    }
}
