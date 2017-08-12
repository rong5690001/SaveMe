package com.rong.map.saveme.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SizeUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2017/8/9/009.
 */

public class LockView extends View {

    private static final int DEFAULT_SELECT_COLOR = Color.GREEN;

    Paint mSourcePaint;
    Paint mSelectedPaint;
    Paint mLinePaint;
    int viewWidth, viewHeight;
    //    int[] circelX = new int[3];//X坐标
//    int[] circelY = new int[3];//Y坐标
    Point[][] points = new Point[3][3];
    int radius = 20;
    float strokeWidth = 2.5f;
    List<Point> selectedPoints = new ArrayList<>();
    int selectedColor = DEFAULT_SELECT_COLOR;
    boolean isDrawing = false;//正在画图案
    Set<Integer> selectedIndexs = new HashSet<>();


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

    private void init() {
        mSourcePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSourcePaint.setStyle(Paint.Style.STROKE);
        mSourcePaint.setStrokeWidth(strokeWidth);
        mSelectedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSelectedPaint.setColor(selectedColor);
        mSelectedPaint.setStyle(Paint.Style.FILL);
        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setColor(selectedColor);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeWidth(radius / 3 * 2);
//        screenWidth = ScreenUtils.getScreenWidth();
//        screenHeight = ScreenUtils.getScreenHeight();
        int viewWidth = SizeUtils.dp2px(radius) * 9;//图案的宽度
        int viewHeight = viewWidth;//图案的高度
        int sumX = 0;
        for (int i = 0; i < 3; i++) {
            int sumY = 0;
            if (i == 0) {
                sumX = radius * 2;
//                sumY = radius * 2;
            } else {
                sumX += radius * 3;
//                sumY += radius * 3;
            }
            for (int j = 0; j < 3; j++) {
                if (j == 0) {
                    sumY = radius * 2;
                } else {
                    sumY += radius * 3;

                }
                points[i][j] = new Point(sumX, sumY);
            }
//            circelX[i] = sumX;
//            circelY[i] = sumY;
//            LogUtils.d("circelX:", circelX[i]);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        init();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                canvas.drawCircle(points[i][j].x, points[i][j].y, radius, mSourcePaint);
                canvas.drawCircle(points[i][j].x, points[i][j].y, radius / 3, mSourcePaint);
            }
        }

        Point prePoint = null;
        Path path = new Path();

        //解锁图案
        for (Point selectedPoint : selectedPoints) {
            canvas.drawCircle(selectedPoint.x, selectedPoint.y, radius, mSelectedPaint);
            if (prePoint != null) {

            }
            path.moveTo();
            prePoint = selectedPoint;
        }

        canvas.save();
        canvas.translate(prePoint.x, prePoint.y);

        path.lineTo(selectedPoint.x, selectedPoint.y);
        canvas.drawPath(path, mLinePaint);
        canvas.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Point selectPoint = getSelectPoint(event);
                if (selectPoint != null) {
                    selectedPoints.add(selectPoint);
                    isDrawing = true;
                    postInvalidate();
                }
                break;

            case MotionEvent.ACTION_MOVE:
                if (isDrawing) {
                    Point selectPoint1 = getSelectPoint(event);
                    if (selectPoint1 != null
                            && !selectedPoints.contains(selectPoint1)) {
                        selectedPoints.add(selectPoint1);
                        postInvalidate();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                removeCallbacks(actionUpRunnable);
                postDelayed(actionUpRunnable, 100);
                break;

        }
        return true;
    }

    public Point getSelectPoint(MotionEvent event) {
        int eventX = (int) event.getX();
        int eventY = (int) event.getY();
        int selectedIndex = 0;
        for (Point[] point1 : points) {
            for (Point point2 : point1) {
                float distance = (float) Math.sqrt(Math.pow(point2.x - eventX, 2)
                        + Math.pow(point2.y - eventY, 2));
                if (distance <= radius) {
                    LogUtils.d("distance:", distance);
                    selectedIndexs.add(selectedIndex);
                    return point2;
                }
                selectedIndex++;
            }
        }

        return null;
    }

    public Runnable actionUpRunnable = new Runnable() {
        @Override
        public void run() {
            if (selectedPoints != null) {
                selectedPoints.clear();
                selectedIndexs.clear();
                postInvalidate();
            }
        }
    };

}
