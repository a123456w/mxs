package com.guo.qlzx.maxiansheng.view;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.PopupWindow;

/**
 * @author Chengguo on 2017/12/20.
 */
public class PopWindow extends PopupWindow {
    public PopWindow(Context context) {
        super(context);
    }

    public PopWindow(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PopWindow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public PopWindow(View contentView) {
        super(contentView);
    }

    public PopWindow(int width, int height) {
        super(width, height);
    }

    public PopWindow(View contentView, int width, int height) {
        super(contentView, width, height);
    }

    /**
     *  在android7.0上，如果不主动约束PopuWindow的大小，比如，设置布局大小为 MATCH_PARENT,那么PopuWindow会变得尽可能大，
     *  以至于 view下方无空间完全显示PopuWindow，而且view又无法向上滚动，此时PopuWindow会主动上移位置，直到可以显示完全。
     *　解决办法：主动约束PopuWindow的内容大小，重写showAsDropDown方法：
     * @param anchor
     */
    @Override
    public void showAsDropDown(View anchor) {

        if (Build.VERSION.SDK_INT >= 24){
            Rect visibleFrame = new Rect();
            anchor.getGlobalVisibleRect(visibleFrame);
            int height = anchor.getResources().getDisplayMetrics().heightPixels - visibleFrame.bottom;
            setHeight(height);
        }
        super.showAsDropDown(anchor);
    }
}