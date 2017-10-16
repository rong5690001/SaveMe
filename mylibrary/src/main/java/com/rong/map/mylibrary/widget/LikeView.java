package com.rong.map.mylibrary.widget;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.rong.map.mylibrary.R;

/**
 * 作者：陈华榕
 * 邮箱:mpa.chen@sportq.com
 * 时间：2017/10/13  10:38
 */

public class LikeView extends ViewGroup {

    private boolean isLike;
    private String preValue;
    private String lastValue;
    private TextView leftTv;
    private TextView rightTopTv;
    private TextView rightCenterTv;
    private TextView rightBottomTv;
    private String currentValue;


    public LikeView(Context context) {
        this(context, null);
    }

    public LikeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LikeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        int measureWidth = measureWidthHeight(widthMeasureSpec);
//        int measureHeight = measureWidthHeight(heightMeasureSpec);

        measureChildren(widthMeasureSpec, heightMeasureSpec);

        int measureWidth = getChildAt(0).getMeasuredWidth() + getChildAt(1).getMeasuredWidth();
        int measureHeight = getChildAt(0).getMeasuredHeight() * 3;

        setMeasuredDimension(measureWidth, measureHeight);
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        int startX = 0;
        int startY = 0;
        for (int i4 = 0; i4 < getChildCount(); i4++) {
            View childView = getChildAt(i4);

            int measureWidth = childView.getMeasuredWidth();
            int measureHeight = childView.getMeasuredHeight();

            if (i4 == 0) {
                childView.layout(startX, measureHeight, startX + measureWidth, 2 * measureHeight);
            } else {
                childView.layout(startX, startY, startX + measureWidth, startY + measureHeight);
            }

            if (i4 == 0) {
                startX += measureWidth;
            }
            if (i4 != 0) {
                startY += measureHeight;
            }
        }
    }

    private int measureWidthHeight(int measureSpec) {
        int result = 0;
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);

        switch (mode) {
            case MeasureSpec.EXACTLY:
            case MeasureSpec.AT_MOST:
                result = size;
                break;
        }

        return result;
    }

    private void init() {
        leftTv = new TextView(getContext());
        leftTv.setText("left");
        leftTv.setTextColor(Color.WHITE);
        rightTopTv = new TextView(getContext());
        rightTopTv.setText("rightTop");
        rightTopTv.setTextColor(Color.WHITE);
        rightCenterTv = new TextView(getContext());
        rightCenterTv.setText("rightCenter");
        rightCenterTv.setTextColor(Color.WHITE);
        rightBottomTv = new TextView(getContext());
        rightBottomTv.setText("rightBottom");
        rightBottomTv.setTextColor(Color.WHITE);
        addView(leftTv);
        addView(rightTopTv);
        addView(rightCenterTv);
        addView(rightBottomTv);

        animationUp = AnimationUtils.loadAnimation(getContext(), R.anim.likeview_up);
        animationUp.setAnimationListener(animationListener);
        animationDown = AnimationUtils.loadAnimation(getContext(), R.anim.likeview_down);
        animationDown.setAnimationListener(animationListener);
    }

    public void setText(String text) {
        if (TextUtils.isEmpty(text)) return;
        //截取最后一位数
        String lastestValue = text.substring(text.length() - 1, text.length());
        if (Integer.parseInt(lastestValue) == 9) {//有两位数需要做滚动
            preValue = text.substring(0, text.length() - 2);
            lastValue = text.substring(text.length() - 2, text.length());
        } else {//有一位数需要做滚动
            preValue = text.substring(0, text.length() - 1);
            lastValue = lastestValue;
        }
        int lastValueInt = Integer.parseInt(lastValue);
        leftTv.setText(preValue);
        rightTopTv.setText(String.valueOf(lastValueInt - 1));
        rightCenterTv.setText(lastValue);
        rightBottomTv.setText(String.valueOf(lastValueInt + 1));
    }

    public void likeOrUnLike(boolean like) {
        rightTopTv.clearAnimation();
        rightCenterTv.clearAnimation();
        rightBottomTv.clearAnimation();
        if (like) {
            currentValue = rightBottomTv.getText().toString();
            rightCenterTv.startAnimation(animationUp);
            rightBottomTv.setVisibility(VISIBLE);
            rightBottomTv.startAnimation(animationUp);
        } else {
            currentValue = rightTopTv.getText().toString();
            rightTopTv.startAnimation(animationDown);
            rightTopTv.setVisibility(VISIBLE);
            rightCenterTv.startAnimation(animationDown);
        }
        isLike = like;
    }

    private Animation animationUp;
    private Animation animationDown;

    private Animation.AnimationListener animationListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            int currentValueInt = Integer.parseInt(currentValue);
            rightTopTv.setText(String.valueOf(currentValueInt - 1));
            rightCenterTv.setText(currentValue);
            rightBottomTv.setText(String.valueOf(currentValueInt + 1));
            rightTopTv.setVisibility(INVISIBLE);
            rightBottomTv.setVisibility(INVISIBLE);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };

    public boolean isLike() {
        return isLike;
    }
}
