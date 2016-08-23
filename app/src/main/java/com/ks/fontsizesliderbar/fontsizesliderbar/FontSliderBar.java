/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.ks.fontsizesliderbar.fontsizesliderbar;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * 
 *  Created by kangsen on 16/8/22.
 *  自定义字体选滑动择按钮类
 *
 */
public class FontSliderBar extends View {

    /**TAG*/
    private static final String TAG = "SliderBar";
    /**默认滑动点个数*/
    private static final int DEFAULT_TICK_COUNT = 3;
    
    private static final String[] DEFAULT_TEXTS={"小","中","大"};
    /**默认标尺线高度,像素*/
    private static final float DEFAULT_TICK_HEIGHT = 24;
    /**背景线条的粗度，像素*/
    private static final float DEFAULT_BAR_LINE_WIDE = 3;
    /**背景线条颜色*/
    private static final int DEFAULT_BAR_LINE_COLOR = Color.GRAY;
    /**默认文案文字大小，像素*/
    private static final int DEFAULT_TEXT_SIZE = 16;
    /**默认文案文字颜色*/
    private static final int DEFAULT_TEXT_COLOR = Color.GRAY;
    /**文案文字和线条之间的距离，像素*/
    private static final int DEFAULT_TEXT_PADDING = 20;
    /**默认圆形滑块的半径，像素*/
    private static final float DEFAULT_THUMB_RADIUS = 20;
    /**圆形滑块默认颜色*/
    private static final int DEFAULT_THUMB_COLOR_NORMAL = Color.WHITE;
    /**圆形滑块按下时的颜色*/
    private static final int DEFAULT_THUMB_COLOR_PRESSED = Color.WHITE;
    /**圆形滑块外层阴影圆环的颜色*/
    private static final int DEFAULT_THUMB_COLOR_CIRCLE = 0x66000000;
    /**圆形滑块阴影环的粗度，像素*/
    private static final int DEFAULT_RHUMB_CIRCLE_WIDE = 3;
    /**滑块阴影颜色*/
    private static final int DEFAULT_SHADOW_COLOR = Color.LTGRAY;
    /**滑动点个数，最少2个*/
    private int mTickCount = DEFAULT_TICK_COUNT;
    /** 默认文案 */
    private String[] mTextArray=DEFAULT_TEXTS;
    /**标尺线高度，不能为负值*/
    private float mTickHeight = DEFAULT_TICK_HEIGHT;
    /**背景线宽*/
    private float mBarLineWide = DEFAULT_BAR_LINE_WIDE;
    /**背景线颜色*/
    private int mBarLineColor = DEFAULT_BAR_LINE_COLOR;
    /**圆形滑块半径*/
    private float mThumbRadius = DEFAULT_THUMB_RADIUS;
    /**圆形滑块默认颜色*/
    private int mThumbColorNormal = DEFAULT_THUMB_COLOR_NORMAL;
    /**圆形滑块按下时的颜色*/
    private int mThumbColorPressed = DEFAULT_THUMB_COLOR_PRESSED;
    /**圆形滑块阴影环颜色*/
    private int mThumbCircleColor = DEFAULT_THUMB_COLOR_CIRCLE;
    /**圆形滑块阴影环线宽*/
    private int mThumbCircleWide = DEFAULT_RHUMB_CIRCLE_WIDE;
    /**文案字体大小*/
    private int mTextSize = DEFAULT_TEXT_SIZE;
    /**文案文字的颜色*/
    private int mTextColor = DEFAULT_TEXT_COLOR;
    /**文案文字和标线之间的距离*/
    private int mTextPadding = DEFAULT_TEXT_PADDING;
    /**控件默认宽度，像素*/
    private int mDefaultWidth = 500;
    /**位置索引*/
    private int mCurrentIndex = 0;
    /**动画控制位*/
    private boolean mAnimation = true;
    /**圆形滑块对象*/
    private Thumb mThumb;
    /**背景标线和文案对象的封装*/
    private Bar mBar;
    /** 属性动画 */
    private ValueAnimator mAnimator;
    /** 移动监听器 */
    private OnSliderBarChangeListener mListener;
    /** 是否显示阴影 */
    private boolean mIsShowShadow=false;
    /** 阴影颜色 */
    private int mShadowColor=DEFAULT_SHADOW_COLOR;

    /***
     * 构造函数
     * @param context 上下文环境
     */
    public FontSliderBar(Context context) {
        super(context);
    }

    /**
     * 构造函数
     * @param context
     * @param attrs
     */
    public FontSliderBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /***
     * 构造函数
     * @param context
     * @param attrs
     * @param defStyle
     */
    public FontSliderBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width;
        int height;

        final int measureWidthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int measureHeightMode = MeasureSpec.getMode(heightMeasureSpec);
        final int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
        final int measureHeight = MeasureSpec.getSize(heightMeasureSpec);

        if (measureWidthMode == MeasureSpec.AT_MOST) {
            width = Math.min(measureWidth,mDefaultWidth);
        } else if (measureWidthMode == MeasureSpec.EXACTLY) {
            width = measureWidth;
        } else {
            width = mDefaultWidth;
        }

        if (measureHeightMode == MeasureSpec.AT_MOST) {
            height = Math.min(getMinHeight()+getPaddingTop() + getPaddingBottom(), measureHeight);
        } else if (measureHeightMode == MeasureSpec.EXACTLY) {
            height = measureHeight;
        } else {
            height = getMinHeight()+getPaddingTop() + getPaddingBottom();
        }
        setMeasuredDimension(width, height);
    }

    /***
     * 获取控件的最小高度，不包括padding
     * @return
     */
    private int getMinHeight() {
        final float f = getFontHeight();
        return (int) (f + mTextPadding + mThumbRadius * 2);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        createBar();
        createThumbs();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mBar.draw(canvas);
        mThumb.draw(canvas);
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (VISIBLE != visibility) {
            stopAnimation();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        destroyResources();
        super.onDetachedFromWindow();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled() || isAnimationRunning()) {
            return false;
        }
        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
            return onActionDown(event.getX(), event.getY());
        case MotionEvent.ACTION_MOVE:
            this.getParent().requestDisallowInterceptTouchEvent(true);
            return onActionMove(event.getX());
        case MotionEvent.ACTION_UP:
        case MotionEvent.ACTION_CANCEL:
            this.getParent().requestDisallowInterceptTouchEvent(false);
            return onActionUp(event.getX(), event.getY());
        default:
            return true;
        }
    }

    /**
     * 设置监听器
     * @param listener
     * @return
     */
    public FontSliderBar setOnSliderBarChangeListener(OnSliderBarChangeListener listener) {
        mListener = listener;
        return FontSliderBar.this;
    }

    /**
     * 设置滑动点个数和对应的文案，最少2个
     * 务必保证tickCount和textArray的长度一致
     * @param tickCount
     * @param textArray
     * @return
     */
    public FontSliderBar setTickCount(int tickCount,String[] textArray) {
        if (isValidTickCount(tickCount,textArray)) {
            mTickCount = tickCount;
            mTextArray=textArray;
        } else {
            Log.e(TAG, "滑动点数要和文案长度一致");
            throw new IllegalArgumentException("滑动点数要和文案长度一致");
        }
        return FontSliderBar.this;
    }

    /**
     * 设置标线高度，单位像素
     * @param tickHeight
     * @return
     */
    public FontSliderBar setTickHeight(float tickHeight) {
        mTickHeight = tickHeight;
        return FontSliderBar.this;
    }
    /***
     * 设置标线的线宽
     * @param barLineWide
     * @return
     */
    public FontSliderBar setBarLineWide(float barLineWide) {
        mBarLineWide = barLineWide;
        return FontSliderBar.this;
    }
    /**
     * 设置标线颜色
     * @param barLineColor
     * @return
     */
    public FontSliderBar setBarLineColor(int barLineColor) {
        mBarLineColor = barLineColor;
        return FontSliderBar.this;
    }

    /***
     * 设置文案文字大小，像素为单位
     * @param textSize
     * @return
     */
    public FontSliderBar setTextSize(int textSize) {
        mTextSize = textSize;
        return FontSliderBar.this;
    }
    /**
     * 设置文案文字颜色
     * @param textColor
     * @return
     */
    public FontSliderBar setTextColor(int textColor) {
        mTextColor = textColor;
        return FontSliderBar.this;
    }

    /***
     * 设置文案文字和标线之间的距离
     * @param textPadding
     * @return
     */
    public FontSliderBar setTextPadding(int textPadding) {
        mTextPadding = textPadding;
        return FontSliderBar.this;
    }

    /***
     * 设置圆形滑块的半径
     * @param thumbRadius
     * @return
     */
    public FontSliderBar setThumbRadius(float thumbRadius) {
        mThumbRadius = thumbRadius;
        return FontSliderBar.this;
    }

    /***
     * 设置圆形滑块的默认颜色
     * @param thumbColorNormal
     * @return
     */
    public FontSliderBar setThumbColorNormal(int thumbColorNormal) {
        mThumbColorNormal = thumbColorNormal;
        return FontSliderBar.this;
    }

    /**
     * 设置圆形滑块按下的颜色
     * @param thumbColorPressed
     * @return
     */
    public FontSliderBar setThumbColorPressed(int thumbColorPressed) {
        mThumbColorPressed = thumbColorPressed;
        return FontSliderBar.this;
    }
    /***
     * 设置圆形滑块阴影圆的线宽
     * @param thumbCircleWide
     * @return
     */
    public FontSliderBar setThumbCircleWide(int thumbCircleWide) {
        mThumbCircleWide = thumbCircleWide;
        return FontSliderBar.this;
    }
    /***
     * 设置圆形滑块阴影颜色
     * @param thumbColorCircle
     * @return
     */
    public FontSliderBar setThumbColorCircle(int thumbColorCircle) {
        mThumbCircleColor = thumbColorCircle;
        return FontSliderBar.this;
    }

    /***
     * 设置选中索引
     * @param currentIndex
     * @return
     */
    public FontSliderBar setThumbIndex(int currentIndex) {
        if (indexOutOfRange(currentIndex)) {
            throw new IllegalArgumentException(
                    "A thumb index is out of bounds. Check that it is between 0 and mTickCount - 1");
        } else {
            if (mCurrentIndex != currentIndex) {
                mCurrentIndex = currentIndex;
                if (mListener != null) {
                    mListener.onIndexChanged(this, mCurrentIndex);
                }
            }
        }
        return FontSliderBar.this;
    }

    /***
     * 是否开启动画
     * @param animation
     * @return
     */
    public FontSliderBar withAnimation(boolean animation) {
        mAnimation = animation;
        return FontSliderBar.this;
    }

    /***
     * 是否显示阴影
     * @param animation
     * @return
     */
    public FontSliderBar showShadow(boolean isShowShadow) {
        mIsShowShadow = isShowShadow;
        return FontSliderBar.this;
    }

    public FontSliderBar setShadowColor(int shadowColor) {
        mShadowColor = shadowColor;
        return FontSliderBar.this;
    }
    /***
     * 应用设置
     */
    public void apply() {
        createBar();
        createThumbs();
        // 关闭硬件加速，显示阴影
        if(mIsShowShadow) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        requestLayout();
        invalidate();
    }

    /***
     * 获取当前滑块的位置
     * @return
     */
    public int getCurrentIndex() {
        return mCurrentIndex;
    }

    /***
     * 构建bar对象
     */
    private void createBar() {
        mBar = new Bar(getXCoordinate(), getYCoordinate(), getBarLineLength(), mTickCount,mTextArray, mTickHeight, mBarLineWide,
                mBarLineColor, mTextColor, mTextSize, mTextPadding,mThumbRadius);
    }

    /***
     * 构建Thumb对象
     */
    private void createThumbs() {
        mThumb = new Thumb(getXCoordinate(), getYCoordinate(), mThumbColorNormal, mThumbColorPressed,
                mThumbCircleColor,mThumbCircleWide, mThumbRadius,mIsShowShadow,mShadowColor);
        mThumb.setX(getXCoordinate()+mBar.getmTickDistance()*mCurrentIndex);
    }

    /**
     * 获取绘制起始点的x坐标
     * @return
     */
    private float getXCoordinate() {
        return mThumbRadius+getPaddingLeft();
    }

    /***
     * 获取绘制起始点的Y坐标
     * @return
     */
    private float getYCoordinate() {
        return getHeight()-getPaddingBottom()-((getHeight()-getMinHeight()-getPaddingTop()-getPaddingBottom())/2f)-mThumbRadius;
    }

    /***
     * 获取文案字体的高度
     * @return
     */
    private float getFontHeight() {
        if(mTextArray==null || mTextArray.length==0)
            return 0f;
        Paint paint = new Paint();
        paint.setTextSize(mTextSize);
        paint.measureText(mTextArray[0]);
        FontMetrics fontMetrics = paint.getFontMetrics();
        float f = fontMetrics.descent - fontMetrics.ascent;
        return f;
    }

    /***
     * 获取Bar标线的长度
     * @return
     */
    private float getBarLineLength() {
        return getWidth() - 2 * mThumbRadius-getPaddingLeft()-getPaddingRight();
    }

    /***
     * 判断索引点是否合法
     * @param thumbIndex
     * @return
     */
    private boolean indexOutOfRange(int thumbIndex) {
        return (thumbIndex < 0 || thumbIndex >= mTickCount);
    }

    /***
     * 判断滑动点个数是否合法
     * @param tickCount
     * @return
     */
    private boolean isValidTickCount(int tickCount,String[] textArray) {
        return (tickCount >=2 && (textArray==null || textArray.length==0)) || (tickCount>=2 && textArray!=null && textArray.length==tickCount);
    }

    /***
     * 处理触摸事件
     * @param x
     * @param y
     * @return
     */
    private int indexPress=-1;
    private boolean onActionDown(float x, float y) {
        if (!mThumb.isPressed() && mThumb.isInTargetZone(x, y)) {
            pressThumb(mThumb);
        }
        else
        {
            indexPress=getIndexByPoint(x,y);
        }
        return true;
    }
    
    private int getIndexByPoint(float x,float y)
    {
        for(int i=0;i<mTickCount;i++)
        {
            if(isInPoint(x, y, i))
            return i;
        }
        return -1;
    }
    private boolean isInPoint(float x,float y,int i)
    {
        return Math.abs(x-(getXCoordinate()+i*mBar.getmTickDistance())) < mThumb.getmTouchZone() && Math.abs(y-getYCoordinate())<mThumb.getmTouchZone();
    }
    /***
     * 处理触摸移动事件
     * @param x
     * @return
     */
    private boolean onActionMove(float x) {
        if (mThumb.isPressed()) {
            moveThumb(mThumb, x);
        }
        return true;
    }

    /**
     * 处理触摸抬起事件
     * @param x
     * @param y
     * @return
     */
    private boolean onActionUp(float x, float y) {
        if (mThumb.isPressed()) {
            releaseThumb(mThumb);
        }
        else
        {
            if(indexPress==getIndexByPoint(x,y) && indexPress!=-1)
            {
                
                startAnimation(mThumb, mThumb.getX(), getXCoordinate()+indexPress*mBar.getmTickDistance());
                mCurrentIndex = indexPress;
                if (null != mListener) {
                    mListener.onIndexChanged(this, mCurrentIndex);
                }
                
            }
        }
        return true;
    }

    /***
     * 滑块按下，绘制按下颜色
     * @param thumb
     */
    private void pressThumb(Thumb thumb) {
        thumb.press();
        invalidate();
    }

    /***
     * 释放滑块
     * @param thumb
     */
    private void releaseThumb(final Thumb thumb) {
        final int tempIndex = mBar.getNearestTickIndex(thumb);
        if (tempIndex != mCurrentIndex) {
            mCurrentIndex = tempIndex;
            if (null != mListener) {
                mListener.onIndexChanged(this, mCurrentIndex);
            }
        }

        float start = thumb.getX();
        float end = mBar.getNearestTickCoordinate(thumb);
        if (mAnimation) {
            startAnimation(thumb, start, end);
        } else {
            thumb.setX(end);
            invalidate();
        }
        thumb.release();
    }

    /***
     * 开始动画
     * @param thumb
     * @param start
     * @param end
     */
    private void startAnimation(final Thumb thumb, float start, float end) {
        stopAnimation();
        mAnimator = ValueAnimator.ofFloat(start, end);
        mAnimator.setDuration(80);
        mAnimator.addUpdateListener(new AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                final float x = (Float) animation.getAnimatedValue();
                thumb.setX(x);
                invalidate();
            }
        });
        mAnimator.start();
    }

    /***
     * 判断动画是否正在执行
     * @return
     */
    private boolean isAnimationRunning() {
        if (null != mAnimator && mAnimator.isRunning()) {
            return true;
        }
        return false;
    }

    /***
     * 释放资源
     */
    private void destroyResources() {
        stopAnimation();
        if (null != mBar) {
            mBar.destroyResources();
            mBar = null;
        }
        if (null != mThumb) {
            mThumb.destroyResources();
            mThumb = null;
        }
    }

    /***
     * 停止动画
     */
    private void stopAnimation() {
        if (null != mAnimator) {
            mAnimator.cancel();
            mAnimator = null;
        }
    }

    /***
     * 移动滑块
     * @param thumb
     * @param x
     */
    private void moveThumb(Thumb thumb, float x) {
        if (x < mBar.getLeftX() || x > mBar.getRightX()) {
            // Do nothing.
        } else {
            thumb.setX(x);
            invalidate();
        }
    }

    /***
     * 监听器接口
     * @author kangsen
     *
     */
    public static interface OnSliderBarChangeListener {
        public void onIndexChanged(FontSliderBar rangeBar, int index);
    }
    
   
}
