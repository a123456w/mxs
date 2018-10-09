package com.guo.qlzx.maxiansheng.util.costom;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;

import com.guo.qlzx.maxiansheng.R;

/**
 * Created by 李 on 2018/6/6.
 */

public class CircleMove extends View  {
    private float mCircleX,mCircleY;    //小球的圆心
    private float mCircleRadius; //大小

    private Path mMovePath;         //这是用来画贝塞尔曲线的
    private Paint mPaintCircle;    //设置画贝塞尔曲线和园的画笔

    private float mStartPointX,mStartPointY;    //二阶贝塞尔曲线的起始点
    private float mEndPointX,mEndPointY;        //二阶贝塞尔曲线的终点
    private float mFlagPointX,mFlagPointY;      //二阶贝塞尔曲线的支撑点


    public CircleMove(Context context) {
        super(context);
    }

    public CircleMove(Context context, AttributeSet attrs) {
        super(context, attrs);

        //初始化画笔
  /*      mPaintBezier=new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintBezier.setStrokeWidth(8);
        mPaintBezier.setStyle(Paint.Style.STROKE);
        mPaintBezier.setColor(Color.BLUE);*/

        mPaintCircle=new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintCircle.setStrokeWidth(2);
        mPaintCircle.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaintCircle.setColor(getResources().getColor(R.color.blue_08));

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        /**
         * 系统每一次需要计算大小时都会执行这个方法，比如view改变的时候。所以我们在这里
         * 获得起始点、终点和支撑点
         */
        //二阶贝塞尔曲线的起始点
        mStartPointX = w / 2;
        mStartPointY = h ;
        //二阶贝塞尔曲线的终点
        mEndPointX = w  / 3.5f;
        mEndPointY = h;
        //二阶贝塞尔曲线的支撑点
        mFlagPointX = w /2.5f;
        mFlagPointY = h/4;

        mCircleX=mStartPointX;
        mCircleY=mStartPointY+100;
        mCircleRadius=30;
        mMovePath=new Path();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //画圆
        canvas.drawCircle(mCircleX,mCircleY,mCircleRadius,mPaintCircle);

        //给Path设置贝塞尔曲线，然后canvas画
        mMovePath.reset();
        mMovePath.moveTo(mStartPointX,mStartPointY);
        /**
         * quadTo()绘制二阶贝塞尔曲线
         * cubicTo()绘制三阶贝塞尔曲线
         */
        /*mMovePath.quadTo(mFlagPointX,mFlagPointY,mEndPointX,mEndPointY);
        canvas.drawPath(mMovePath);
*/
    }
    public  void  startAnim(){
        PointF flagPoint1=new PointF(mFlagPointX,mFlagPointY);

        PointF startPoint=new PointF(mStartPointX,mStartPointY);
        PointF endPoint=new PointF(mEndPointX,mEndPointY+100);

        /**
         * 在startPoint和endPoint之间运动时返回一个ValueAnimator的值
         * MoveEvalutor在它的文件里详细解释
         */
        ValueAnimator mValueAnimator=ValueAnimator.ofObject(new MoveEvalutor(flagPoint1),
                startPoint,endPoint);
        mValueAnimator.setDuration(1000);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            float i=30;
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                /**
                 * 把小球的圆心设置到运动点
                 */
                PointF point=(PointF) animation.getAnimatedValue();
                i=i-0.3f;
                mCircleX=point.x;
                mCircleY=point.y;
                mCircleRadius=i;
                invalidate();
            }
        });
        mValueAnimator.start();

    }
}
