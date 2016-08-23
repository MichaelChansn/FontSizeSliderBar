/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.ks.fontsizesliderbar.fontsizesliderbar;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextUtils;

/***
 * 用户封装文字文案和标线
 * @author kangsen
 *
 */
public class Bar {

    /** 绘制标线的paint */
    private Paint mBarLinePaint;
    /** 绘制文字的Paint */
    private Paint mTextPaint;

    /** 绘制起点x坐标 */
    private final float mLeftX;
    /** 绘制终点x坐标 */
    private final float mRightX;
    /** 标线起始点y坐标 */
    private final float mLineY;
    /** 文字和标线之间的距离 */
    private final float mPadding;
    /** 分段数目，等于滑动点个数减去一 */
    private int mSegments;
    /** 两滑动点之间的距离，像素 */
    private float mTickDistance;
    /** 纵向标线高度，像素 */
    private final float mTickHeight;
    /** 纵向标线绘制时的x坐标 */
    private final float mTickStartY;
    /** 纵向标线绘制时的y坐标 */
    private final float mTickEndY;
    /** 文案文字 */
    private String[] mTextArray;
    /**滑块半径，用于绘制文字时，间隔计算*/
    private float mThumbRadius;
    
    
    /**
     * 构造函数
     * @param x
     * @param y
     * @param width
     * @param tickCount
     * @param tickHeight
     * @param barWidth
     * @param barColor
     * @param textColor
     * @param textSize
     * @param padding
     */
    public Bar(float x, float y, float width, int tickCount, String[] textArray, float tickHeight,
               float barWidth, int barColor, int textColor, int textSize, int padding, float thumbRadius) {
        
        mLeftX = x;
        mRightX = x + width;
        mLineY = y;
        mPadding = padding;
        
        mSegments = tickCount - 1;
        mTextArray=textArray;
        mTickDistance = width / mSegments;
        mTickHeight = tickHeight;
        mTickStartY = mLineY - mTickHeight / 2f;
        
        mTickEndY = mLineY + mTickHeight / 2f;
        mThumbRadius=thumbRadius;
        mBarLinePaint = new Paint();
        mBarLinePaint.setColor(barColor);
        mBarLinePaint.setStrokeWidth(barWidth);
        mBarLinePaint.setAntiAlias(true);
        
        mTextPaint = new Paint();
        mTextPaint.setColor(textColor);
        mTextPaint.setTextSize(textSize);
        mTextPaint.setAntiAlias(true);
    }
    /** 获取线段之间的间隔大小 */
    public float getmTickDistance() {
        return mTickDistance;
    }

    /***
     * 绘制函数
     * @param canvas
     */
    public void draw(Canvas canvas) {
        drawLine(canvas);
        drawTicks(canvas);
    }

    /** 获取起点x坐标 */
    public float getLeftX() {
        return mLeftX;
    }

    /** 获取终点x坐标 */
    public float getRightX() {
        return mRightX;
    }

    /** 计算当前坐标距离最近的Tick坐标 */
    public float getNearestTickCoordinate(Thumb thumb) {
        final int nearestTickIndex = getNearestTickIndex(thumb);
        final float nearestTickCoordinate = mLeftX + (nearestTickIndex * mTickDistance);
        return nearestTickCoordinate;
    }
    /** 根据坐标计算最近索引 */
    public int getNearestTickIndex(Thumb thumb) {
        return getNearestTickIndex(thumb.getX());
    }
    /** 根据坐标计算索引 */
    public int getNearestTickIndex(float x) {
        return (int) ((x - mLeftX + mTickDistance / 2f) / mTickDistance);
    }
    /** 绘制线条 */
    private void drawLine(Canvas canvas) {
        canvas.drawLine(mLeftX, mLineY, mRightX, mLineY, mBarLinePaint);
    }

    /** 绘制纵向线标和文案文字 */
    private void drawTicks(Canvas canvas) {
        for (int i = 0; i <= mSegments; i++) {
            final float x = i * mTickDistance + mLeftX;
            canvas.drawLine(x, mTickStartY, x, mTickEndY, mBarLinePaint);
            if(mTextArray!=null && mTextArray.length>0)
            {
            //String text = 0 == i ? "小" : mSegments == i ? "大" : "";
                String text=mTextArray[i];
            if(!TextUtils.isEmpty(text)) {
                canvas.drawText(text, x - getTextWidth(text) / 2, /*Math.min(mTickStartY,mLineY-mThumbRadius)*/ mTickStartY-
                        mPadding-getTextBaseLine(), mTextPaint);
            }
            }
        }
    }
    /** 由于文字绘制是从baseLine开始往两边绘制,所以提供此方法获取baseline */
    float getTextBaseLine()
    {
        return mTextPaint.getFontMetrics().descent;
    }
    /** 测量文字宽度 */
    float getTextWidth(String text) {
        return mTextPaint.measureText(text);
    }
    /** 释放资源 */
    public void destroyResources() {
        if(null != mBarLinePaint) {
            mBarLinePaint = null;
        }
        if(null != mTextPaint) {
            mTextPaint = null;
        }
    }
}

