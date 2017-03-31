package com.aotuman.linechart;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;


import com.aotuman.linechart.util.UtilApp;

import java.util.ArrayList;

/**
 * @author aotuman
 */
public class LineChartView extends View{
    private static final String TAG = "LineChartView";

    //Y轴  每个刻度的间距间距
    private int myInterval;
    //X轴  每个刻度的间距间距
    private int mxInterval;
    //Y轴距离view长度
    private int mLeftInterval;
    //X轴距离view长度
    private int mBottomInterval;
    //X轴距离view顶部长度
    private int mTopInterval;
    //Y轴字体的大小
    private float mYAxisFontSize;
    //View 的宽和高
    private int mWidth, mHeight;
    //线的颜色
    private int mLineColor;
    //线条的宽度
    private float mStrokeWidth = 4.0f;
    //X轴的文字
    private ArrayList<String> mXAxis;
    //点 (温度)
    private ArrayList<Integer> mYAxis;
    //纵轴最大值
    private int maxYValue;
    //纵轴分割数量
    private int dividerCount;
    //画坐标线的轴
    private Paint axisPaint;
    //画X轴文字
    private Paint axisTextPaint;
    //连接线条
    private Paint linePaint;
    //小圆点内环
    private Paint innerCirclePaint;
    //小圆点中间环
    private Paint middleCiclePaint;
    //小圆点外环
    private Paint outterCiclePaint;
    //折线路径
    private Path mpolylinePath;
    //小圆点内环半径
    private int innerCircleRadius;
    //小圆点中间环半径
    private int middleRadius;
    //小圆点外环半径
    private int outerRadius;

    public LineChartView(Context context) {
        this(context, null);
        init(context);
    }

    public LineChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        init(context);
    }

    public LineChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        /**
         * 获得我们所定义的自定义样式属性
         */
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.LineChartView, defStyleAttr, 0);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++){
            int index = a.getIndex(i);
            switch (index)
            {
                // 折线颜色
                case R.styleable.LineChartView_lineColor:
                    mLineColor = a.getColor(index, Color.BLACK);
                    break;
                // X轴每个刻度的间距间距
                case R.styleable.LineChartView_dividerCount:
                    dividerCount = a.getInt(index, 5);
                    break;
                // X轴每个刻度的间距间距
                case R.styleable.LineChartView_xInterval:
                    mxInterval = a.getDimensionPixelSize(index, (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP, 16, getResources().getDisplayMetrics()));
                    break;
                // Y轴距离view长度
                case R.styleable.LineChartView_leftInterval:
                    mLeftInterval = a.getDimensionPixelSize(index, (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP, 16, getResources().getDisplayMetrics()));
                    break;
                // X轴距离view底部的高度
                case R.styleable.LineChartView_bottomInterval:
                    mBottomInterval = a.getDimensionPixelSize(index, (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP, 16, getResources().getDisplayMetrics()));
                    break;
                // X轴距离view顶部长度
                case R.styleable.LineChartView_topInterval:
                    mTopInterval = a.getDimensionPixelSize(index, (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP, 16, getResources().getDisplayMetrics()));
                    break;
                // Y轴字体的大小
                case R.styleable.LineChartView_yAxisFontSize:
                    // 默认设置为16sp，TypeValue也可以把sp转化为px
                    mYAxisFontSize = a.getDimensionPixelSize(index, (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));
                    break;
            }

        }
        a.recycle();
    }

    private void init(Context context){
        // 画坐标线的轴
        axisPaint = new Paint();
        axisPaint.setTextSize(mYAxisFontSize);
        axisPaint.setColor(Color.parseColor("#D9D9D9"));

        // 画X轴文字
        axisTextPaint = new Paint();
        axisTextPaint.setTextSize(mYAxisFontSize);
        axisTextPaint.setColor(Color.parseColor("#878787"));

        // 连接线条
        linePaint = new Paint();
        linePaint.setColor(mLineColor);
        linePaint.setAntiAlias(true);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(mStrokeWidth);

        // 小圆点内环
        innerCirclePaint = new Paint();
        innerCirclePaint.setStyle(Paint.Style.FILL);
        innerCirclePaint.setAntiAlias(true);
        innerCirclePaint.setColor(Color.WHITE);
        innerCirclePaint.setStrokeWidth(UtilApp.dip2px(context,2));

        // 小圆点中间环
        middleCiclePaint = new Paint();
        middleCiclePaint.setStyle(Paint.Style.STROKE);
        middleCiclePaint.setAntiAlias(true);
        middleCiclePaint.setColor(mLineColor);
        middleCiclePaint.setStrokeWidth(UtilApp.dip2px(context,2));

        // 小圆点外环
        outterCiclePaint = new Paint();
        outterCiclePaint.setStyle(Paint.Style.STROKE);
        outterCiclePaint.setAntiAlias(true);
        outterCiclePaint.setColor(Color.WHITE);
        outterCiclePaint.setStrokeWidth(UtilApp.dip2px(context,2));

        // 折线路径
        mpolylinePath = new Path();

        //小圆点内环半径
        innerCircleRadius = UtilApp.dip2px(context,3);
        //小圆点中间环半径
        middleRadius = UtilApp.dip2px(context,4);
        //小圆点外环半径
        outerRadius = UtilApp.dip2px(context,6);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        myInterval = (getHeight()-mBottomInterval-mTopInterval)/dividerCount;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        Log.d(TAG,"widthSize:"+widthSize+",heightSize:"+heightSize );
        mHeight =heightSize;
        if(mXAxis == null){
            Log.d(TAG,"mWidth:"+mWidth+",mHeight:"+mHeight +"mXAxis:"+mXAxis);
            return;
        }
        //宽度通过数组长度计算
        mWidth = mxInterval*(mXAxis.size()-1) + mLeftInterval*2;
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(mXAxis.size() ==0 || mYAxis.size()==0){
            Log.e(TAG,"数据异常");
            return;
        }

        // 画横线
        for(int i = 0;i <= dividerCount;i++){
            canvas.drawLine(mLeftInterval, mHeight - mBottomInterval - i * myInterval, (mXAxis.size()-1)*mxInterval+ mLeftInterval,
                    mHeight - mBottomInterval - myInterval * i, axisPaint);
        }

        // x轴的刻度集合
        int[] xPoints = new int[mXAxis.size()];
        for (int i = 0; i < mXAxis.size(); i++) {
            float xTextWidth = axisPaint.measureText(mXAxis.get(i))/2 ; //文字宽度一半
            float xfloat  = i * mxInterval + mLeftInterval - xTextWidth;
            // 画X轴的文字
            canvas.drawText(mXAxis.get(i), xfloat, mHeight - mBottomInterval + mYAxisFontSize, axisTextPaint);
            xPoints[i] = (int) (xfloat+xTextWidth);

            // 画竖线
            float xvfloat =  i * mxInterval + mLeftInterval;
            canvas.drawLine(xvfloat,mHeight - mBottomInterval, xvfloat,
                    mHeight - mBottomInterval - myInterval*dividerCount, axisPaint);
        }

        /**
         * 画轨迹
         */
        int y = myInterval * (dividerCount - 1); // 只拿纵轴的dividerCount-1/dividerCount画图
        axisPaint.setColor(mLineColor); // 设置坐标值的颜色
        for (int i = 0;i<mYAxis.size();i++){
            int h = mHeight - (mBottomInterval + y * mYAxis.get(i)/ maxYValue);
            float textWidth = axisPaint.measureText(String.valueOf(mYAxis.get(i)))/2 ; //文字宽度一半
            if (i==0){
                mpolylinePath.moveTo(mLeftInterval,h);
                canvas.drawText(mYAxis.get(i) + "", mLeftInterval - textWidth, h - mYAxisFontSize, axisPaint);
            }else{
                mpolylinePath.lineTo(mLeftInterval + i*mxInterval,h);
                canvas.drawText(mYAxis.get(i) + "", mLeftInterval+i*mxInterval- textWidth, h - mYAxisFontSize, axisPaint);
            }
        }
        canvas.drawPath(mpolylinePath,linePaint);

        /**
         * 画小圆圈
         */
        for (int i = 0;i<mYAxis.size();i++){
            int h = mHeight - (mBottomInterval + y * mYAxis.get(i)/ maxYValue);
            if (i==0){
                canvas.drawCircle(mLeftInterval,h,innerCircleRadius,innerCirclePaint);
                canvas.drawCircle(mLeftInterval,h,middleRadius,middleCiclePaint);
                canvas.drawCircle(mLeftInterval,h,outerRadius,outterCiclePaint);
            }else{
                canvas.drawCircle(mLeftInterval + i*mxInterval,h,innerCircleRadius,innerCirclePaint);
                canvas.drawCircle(mLeftInterval + i*mxInterval,h,middleRadius,middleCiclePaint);
                canvas.drawCircle(mLeftInterval + i*mxInterval,h,outerRadius,outterCiclePaint);
            }
        }

    }

    /**
     * 设置Y轴文字
     * @param yItem
     */
    public void setYItem(ArrayList<Integer> yItem){
        mYAxis = yItem;
    }

    /**
     * 设置X轴文字
     * @param xItem
     */
    public void setXItem(ArrayList xItem){
        mXAxis = xItem;
    }


    public void setMaxYValue(int maxYValue) {
        this.maxYValue = maxYValue;
    }
}
