package com.rong.map.saveme.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SizeUtils;

/**
 * Created by Administrator on 2017/8/9/009.
 */

public class LockView extends View {

    Paint mPaint;
    int viewWidth,viewHeight;
    int[] circelX = new int[3];//X坐标
    int[] circelY = new int[3];//Y坐标
    int radius = 20;
    float strokeWidth = 2.5f;

    public LockView(Context context) {
        this(context, null);
    }

    public LockView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LockView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(width, width);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, top + right - left);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewWidth = w;
        viewHeight = h;
        radius = viewWidth / 10;
    }

    private void init(){
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(strokeWidth);
//        screenWidth = ScreenUtils.getScreenWidth();
//        screenHeight = ScreenUtils.getScreenHeight();
        int viewWidth = SizeUtils.dp2px(radius) * 9;//图案的宽度
        int viewHeight = viewWidth;//图案的高度
        int sumX = 0;
        int sumY = 0;
        for (int i = 0; i < 3; i++) {
            if(i == 0){
                sumX = radius * 2;
                sumY = radius * 2;
            } else {
                sumX += radius * 3;
                sumY += radius * 3;
            }
            circelX[i] = sumX;
            circelY[i] = sumY;
            LogUtils.d("circelX:", circelX[i]);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        init();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                canvas.drawCircle(circelX[i],circelX[j], radius, mPaint);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:

        }
        return true;
    }
}
