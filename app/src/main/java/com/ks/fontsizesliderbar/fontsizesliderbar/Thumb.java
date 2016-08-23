/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.ks.fontsizesliderbar.fontsizesliderbar;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * 圆形滑块对应的类
 */
public class Thumb {

    /** 最小触摸区范围 50*50px */
    private static final float MINIMUM_TARGET_RADIUS = 50;
    /** 用于计算最终的触摸区范围 */
    private final float mTouchZone;
    /** 标记是否按下 */
    private boolean mIsPressed;
    /** 圆心y坐标 */
    private final float mY;
    /** 当前圆心所在的x坐标 */
    private float mX;

    /** 未按下时的Paint */
    private Paint mPaintNormal;
    /** 按下时的Paint */
    private Paint mPaintPressed;
    /** 阴影描边Paint */
    private Paint mPaintCircle;
    /** 描边线宽 */
    private int mCircleWide;
    /** 滑块半径 */
    private float mRadius;
    /** 未按下时的滑块颜色 */
    private int mColorNormal;
    /** 按下去时的滑块颜色 */
    private int mColorPressed;
    /** 是否显示阴影 */
    private boolean mIsShowShadow;
    /** 阴影颜色 */
    private int mShadowColor;

    /***
     * 获取最终的触摸有效区大小
     * @return
     */
    public float getmTouchZone() {
        return mTouchZone;
    }

    /***
     * 构造函数
     * @param x
     * @param y
     * @param colorNormal
     * @param colorPressed
     * @param colorCircle
     * @param thumbCircleWide
     * @param radius
     */
    public Thumb(float x, float y, int colorNormal, int colorPressed,int colorCircle,int thumbCircleWide, float
            radius,boolean isShowShadow,int ShadowColor) {

        mRadius = radius;
        mIsShowShadow=isShowShadow;
        mShadowColor=ShadowColor;
        mColorNormal = colorNormal;
        mColorPressed = colorPressed;
        mCircleWide=thumbCircleWide;
        mPaintNormal = new Paint();
        mPaintNormal.setColor(mColorNormal);
        mPaintNormal.setAntiAlias(true);

        mPaintPressed = new Paint();
        mPaintPressed.setColor(mColorPressed);
        mPaintPressed.setAntiAlias(true);
        
        mPaintCircle=new Paint();
        mPaintCircle.setStyle(Paint.Style.STROKE);
        mPaintCircle.setColor(colorCircle);
        mPaintCircle.setAntiAlias(true);
        mPaintCircle.setStrokeWidth(mCircleWide);

        if(isShowShadow)
        {
            mPaintNormal.setShadowLayer(3f, 0f, 5f, mShadowColor);
            mPaintPressed.setShadowLayer(3f, 0f, 5f, mShadowColor);
        }
        mTouchZone = (int) Math.max(MINIMUM_TARGET_RADIUS, radius);
        
        mX = x;
        mY = y;
    }

    /***
     * 设置滑块圆心x坐标
     * @param x
     */
    public void setX(float x) {
        mX = x;
    }

    /***
     * 获取滑块当前x坐标
     * @return
     */
    public float getX() {
        return mX;
    }

    /***
     * 滑块是否被按下
     * @return
     */
    public boolean isPressed() {
        return mIsPressed;
    }

    /***
     * 设置按下
     */
    public void press() {
        mIsPressed = true;
    }

    /***
     * 抬起
     */
    public void release() {
        mIsPressed = false;
    }

    /***
     * 计算当前触电是否处在有效触摸区之内
     * @param x
     * @param y
     * @return
     */
    public boolean isInTargetZone(float x, float y) {
        if (Math.abs(x - mX) <= mTouchZone && Math.abs(y - mY) <= mTouchZone) {
            return true;
        }
        return false;
    }

    /***
     * 绘制滑块和描边阴影
     * @param canvas
     */
    public void draw(Canvas canvas) {
        if (mIsPressed) {
            canvas.drawCircle(mX, mY, mRadius, mPaintPressed);
            
        } else {
            canvas.drawCircle(mX, mY, mRadius, mPaintNormal);
        }
        canvas.drawCircle(mX, mY, mRadius, mPaintCircle);
    }

    /***
     * 释放资源
     */
    public void destroyResources() {
        if(null != mPaintNormal) {
            mPaintNormal = null;
        }
        if(null != mPaintPressed) {
            mPaintPressed = null;
        }
        if(null != mPaintCircle) {
            mPaintCircle = null;
        }
    }
}
