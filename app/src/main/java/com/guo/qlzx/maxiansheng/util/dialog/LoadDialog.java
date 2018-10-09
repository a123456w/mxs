package com.guo.qlzx.maxiansheng.util.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.guo.qlzx.maxiansheng.R;

/**
 * Created by 李 on 2018/4/20.
 */

public class LoadDialog extends Dialog {
    private Context context;
    private static LoadDialog dialog;
    private ImageView ivProgress;


    public LoadDialog(Context context) {
        super(context);
        this.context = context;
    }

    public LoadDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;

    }
    //显示dialog的方法
    public static LoadDialog showDialog(Context context){
        dialog = new LoadDialog(context, R.style.MyDialog);//dialog样式
        dialog.setContentView(R.layout.dialog_load);//dialog布局文件
        dialog.setCanceledOnTouchOutside(false);//点击外部不允许关闭dialog
        return dialog;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus && dialog != null){
          /*  ivProgress = (ImageView) dialog.findViewById(R.id.ivProgress);
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.dialog_progress_anim);
            ivProgress.startAnimation(animation);*/
        }
    }
}
