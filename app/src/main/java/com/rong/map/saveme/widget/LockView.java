package com.rong.map.saveme.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.SizeUtils;

/**
 * Created by Administrator on 2017/8/9/009.
 */

public class LockView extends View {

    Paint mPaint;
    int screenWidth,screenHeight;
    int[][] circelX = new int[3][3];//X坐标
    int[][] circelY = new int[3][3];//Y坐标
    int radius = 10;

    public LockView(Context context) {
        this(context, null);
    }

    public LockView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LockView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        screenWidth = ScreenUtils.getScreenWidth();
        screenHeight = ScreenUtils.getScreenHeight();
        int viewWidth = SizeUtils.dp2px(radius) * 9;//图案的宽度
        int viewHeight = viewWidth;//图案的高度

        int startX = screenWidth - viewWidth * 2;
        int startY = screenHeight - startX - viewHeight;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if(j == 0){
                    circelX[i][j] = startX + radius;
                } else {
                    circelX[i][j] += radius * 3;
                }
                LogUtils.d("circelX:", circelX[i][j]);
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);




    }
}
