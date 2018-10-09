package com.guo.qlzx.maxiansheng.util.costom;

import android.animation.TypeEvaluator;
import android.graphics.PointF;

/**
 * Created by 李 on 2018/6/6.
 */

public class MoveEvalutor implements TypeEvaluator<PointF> {

    private PointF mFlagPoint1;

    public MoveEvalutor(PointF flagPoint) {
        mFlagPoint1=flagPoint;
    }
    @Override
    public PointF evaluate(float fraction, PointF startValue, PointF endValue) {
        return CalculateBezierPointForQuadratic(fraction,startValue,mFlagPoint1,endValue);
    }
    /**
     * B(t) = (1 - t)^2 * P0 + 2t * (1 - t) * P1 + t^2 * P2, t ∈ [0,1]
     *计算贝塞尔曲线的运动点，这个代码在网上可以找到，二阶的三阶的。
     * @param t  曲线长度比例
     * @param p0 起始点
     * @param p1 控制点
     * @param p2 终止点
     * @return t对应的点
     */
    public static PointF CalculateBezierPointForQuadratic(float t, PointF p0, PointF p1, PointF p2) {
        PointF point = new PointF();
        float temp = 1 - t;
        point.x = temp * temp * p0.x + 2 * t * temp * p1.x + t * t * p2.x;
        point.y = temp * temp * p0.y + 2 * t * temp * p1.y + t * t * p2.y;
        return point;
    }
}
