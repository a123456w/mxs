package com.guo.qlzx.maxiansheng.view;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;

/**
 * Created by Administrator on 2018/5/4 0004.
 */

public class MyScrollView extends NestedScrollView {

    private MyScrollChangeListener myScrollChangeListener;

    public MyScrollView(Context context) {
        super(context);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (myScrollChangeListener != null){
            myScrollChangeListener.onScrollChange(l,t,oldl,oldt);
        }
    }

    public void setMyScrollChangeListener(MyScrollChangeListener myScrollChangeListener) {
        this.myScrollChangeListener = myScrollChangeListener;
    }

    public interface MyScrollChangeListener{
        void onScrollChange(int l,int t,int oldl,int oldt);
    }

}
