package com.rong.map.saveme.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;
import com.rong.map.saveme.model.CusPoint;
import com.rong.map.saveme.utils.CstUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/9/009.
 */

public class LockView extends View {
    private static final int DEFAULT_SELECT_COLOR = Color.parseColor("#a3e9a4");
    private static final int STATES_NO = -1;
    private static final int STATES_ACTIONDOWN = 0;
    private static final int STATES_ACTIONMOVE = 1;
    private static final int STATES_ACTIONUP = 2;
    private static final int STATES_SUCCEED = 3;
    private static final int STATES_ERROR = 4;

    Paint mSourcePaint;
    Paint mSelectedPaint;
    Paint mLinePaint;
    int viewWidth, viewHeight;
    CusPoint[] points = new CusPoint[9];
    int radius = 20;
    float strokeWidth = 2.5f;
    int selectedColor = DEFAULT_SELECT_COLOR;
    boolean isDrawing = false;//正在画图案
    List<Integer> selectedIndexs = new ArrayList<>();
    OnLockListener onLockListener;

    float eventX, eventY;
    int state = STATES_NO;
    boolean isSetPsd = false;//是否是设置密码

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
        init();
    }

    private void init() {
        mSourcePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSourcePaint.setStyle(Paint.Style.STROKE);
        mSourcePaint.setStrokeWidth(strokeWidth);
        mSelectedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSelectedPaint.setColor(selectedColor);
        mSelectedPaint.setStyle(Paint.Style.STROKE);
        mSelectedPaint.setStrokeWidth(strokeWidth);
        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setColor(selectedColor);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeWidth(radius / 10);
        mLinePaint.setStrokeJoin(Paint.Join.ROUND);
//        screenWidth = ScreenUtils.getScreenWidth();
//        screenHeight = ScreenUtils.getScreenHeight();
        int viewWidth = SizeUtils.dp2px(radius) * 9;//图案的宽度
        int viewHeight = viewWidth;//图案的高度
        int sumX = 0;
        int index = 0;
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
                points[index] = new CusPoint(sumX, sumY);
                index++;
            }
//            circelX[i] = sumX;
//            circelY[i] = sumY;
//            LogUtils.d("circelX:", circelX[i]);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (state == STATES_ERROR) {
            mSourcePaint.setColor(Color.parseColor("#f06292"));
        } else if (state == STATES_NO) {
            mSourcePaint.setColor(Color.BLACK);
        }

        boolean isMoved = false;
        Path path = new Path();
        for (CusPoint point : points) {
            if (point.state == CusPoint.STATE_UNSELECTED) {
                canvas.drawCircle(point.x, point.y, radius / 10, mSourcePaint);
                canvas.drawCircle(point.x, point.y, radius, mSourcePaint);
            } else {
                mSelectedPaint.setStyle(Paint.Style.STROKE);
                canvas.drawCircle(point.x, point.y, radius, mSelectedPaint);
                mSelectedPaint.setStyle(Paint.Style.FILL);
                canvas.drawCircle(point.x, point.y, radius / 10, mSelectedPaint);
            }
        }

        int lastIndex = 0;
        for (Integer selectedIndex : selectedIndexs) {
            if (!isMoved) {
                path.moveTo(points[selectedIndex].x, points[selectedIndex].y);
                isMoved = true;
            } else {
                path.lineTo(points[selectedIndex].x, points[selectedIndex].y);
            }
            lastIndex = selectedIndex;
        }
//
//
//        //解锁图案
//        for (CusPoint selectedPoint : selectedPoints) {
//            canvas.drawCircle(selectedPoint.x, selectedPoint.y, radius, mSelectedPaint);
//            canvas.drawCircle(selectedPoint.x, selectedPoint.y, radius / 3, mSelectedPaint);
//
//        }
//
        if (points[lastIndex] != null
                && getDistance(new CusPoint((int) eventX, (int) eventY), points[lastIndex]) > radius
                && state == STATES_ACTIONMOVE) {
            path.lineTo(eventX, eventY);
        }

//        canvas.translate(prePoint.x, prePoint.y);

//        path.lineTo(selectedPoint.x, selectedPoint.y);
        canvas.save();
        canvas.drawPath(path, mLinePaint);
        canvas.restore();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                eventX = event.getX();
                eventY = event.getY();
                state = STATES_ACTIONDOWN;
                reset();
                selectedIndexs.clear();
                CusPoint selectPoint = getSelectPoint(event);
                if (selectPoint != null) {
                    isDrawing = true;
                    postInvalidate();
                }
                break;

            case MotionEvent.ACTION_MOVE:
                eventX = event.getX();
                eventY = event.getY();
                state = STATES_ACTIONMOVE;
//                if (isDrawing) {
                CusPoint selectPoint1 = getSelectPoint(event);

                postInvalidate();
                //解锁成功
                if (!isSetPsd
                        && isCompleted()
                        && onLockListener != null) {
                    state = STATES_SUCCEED;
                    onLockListener.onSucceed(getResults());
                }
//                }
                break;
            case MotionEvent.ACTION_UP:
                //解锁成功
                if (onLockListener != null) {
                    if(isCompleted()){
                        state = STATES_SUCCEED;
                        onLockListener.onSucceed(getResults());
                    } else {
                        state = STATES_ERROR;
                        onLockListener.onError();
                    }
                }
                postInvalidate();
                state = STATES_ACTIONUP;
                removeCallbacks(actionUpRunnable);
                postDelayed(actionUpRunnable, 500);
                break;

        }
        return true;
    }

    public CusPoint getSelectPoint(MotionEvent event) {
        for (int i = 0; i < points.length; i++) {
            float distance = getDistance(points[i]
                    , new CusPoint((int) event.getX()
                            , (int) event.getY()));
            if (distance <= radius) {
                LogUtils.d("distance:", distance);
                if (!selectedIndexs.contains(i)) {
                    selectedIndexs.add(i);
                }
                points[i].state = CusPoint.STATE_SELECTED;
                return points[i];
            }
        }

        return null;
    }

    public void setOnLockListener(OnLockListener onLockListener) {
        this.onLockListener = onLockListener;
    }

    public void setSetPsd(boolean setPsd) {
        isSetPsd = setPsd;
    }

    /**
     * 获得图案解锁结果
     *
     * @return
     */
    private String getResults() {
        StringBuffer results = new StringBuffer();
        for (Integer selectedIndex : selectedIndexs) {
            results.append(selectedIndex);
        }
        return results.toString();
    }

    private boolean isCompleted() {
        String results = getResults();
        if (StringUtils.isEmpty(results)) {
            return false;
        }

        if (isSetPsd) {//设置密码
            return selectedIndexs.size() > 3;
        } else {
            if (results.equals(SPUtils.getInstance(CstUtils.TABLENAME)
                    .getString(CstUtils.KEY_PASSWORD))) {
                return true;
            }
        }

        return false;
    }

    private float getDistance(CusPoint point1, CusPoint point2) {
        return (float) Math.sqrt(Math.pow(point2.x - point1.x, 2)
                + Math.pow(point2.y - point1.y, 2));
    }

    public Runnable actionUpRunnable = new Runnable() {
        @Override
        public void run() {
            reset();
            selectedIndexs.clear();
            state = STATES_NO;
            postInvalidate();
        }
    };

    private void reset() {
        for (CusPoint point : points) {
            point.state = CusPoint.STATE_UNSELECTED;
        }
    }

    public interface OnLockListener {

        void onSucceed(String result);

        void onError();
    }

}
