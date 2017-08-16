package com.rong.map.saveme.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.StringUtils
import com.rong.map.saveme.model.CusPoint
import com.rong.map.saveme.utils.CstUtils

import java.util.ArrayList

/**
 * Created by Administrator on 2017/8/9/009.
 */

class LockView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    internal var mSourcePaint: Paint? = null
    internal var mSelectedPaint: Paint? = null
    internal var mLinePaint: Paint? = null
    internal var viewWidth: Int = 0
    internal var viewHeight: Int = 0
    internal var points = arrayOfNulls<CusPoint>(9)
    internal var radius = 20
    internal var strokeWidth = 2.5f
    internal var selectedColor = DEFAULT_SELECT_COLOR
    internal var isDrawing = false//正在画图案
    internal var selectedIndexs: MutableList<Int> = ArrayList()
    internal var onLockListener: OnLockListener? = null

    internal var eventX: Float = 0.toFloat()
    internal var eventY: Float = 0.toFloat()
    internal var state = STATES_NO
    internal var isSetPsd = false//是否是设置密码

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = View.MeasureSpec.getSize(widthMeasureSpec)
        setMeasuredDimension(width, width)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, top + right - left)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        viewWidth = w
        viewHeight = h
        radius = viewWidth / 10
        init()
    }

    private fun init() {
        mSourcePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mSourcePaint!!.style = Paint.Style.STROKE
        mSourcePaint!!.strokeWidth = strokeWidth
        mSelectedPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mSelectedPaint!!.color = selectedColor
        mSelectedPaint!!.style = Paint.Style.STROKE
        mSelectedPaint!!.strokeWidth = strokeWidth
        mLinePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mLinePaint!!.color = selectedColor
        mLinePaint!!.style = Paint.Style.STROKE
        mLinePaint!!.strokeWidth = (radius / 10).toFloat()
        mLinePaint!!.strokeJoin = Paint.Join.ROUND
        //        screenWidth = ScreenUtils.getScreenWidth();
        //        screenHeight = ScreenUtils.getScreenHeight();
        val viewWidth = SizeUtils.dp2px(radius.toFloat()) * 9//图案的宽度
        val viewHeight = viewWidth//图案的高度
        var sumX = 0
        var index = 0
        for (i in 0..2) {
            var sumY = 0
            if (i == 0) {
                sumX = radius * 2
                //                sumY = radius * 2;
            } else {
                sumX += radius * 3
                //                sumY += radius * 3;
            }
            for (j in 0..2) {
                if (j == 0) {
                    sumY = radius * 2
                } else {
                    sumY += radius * 3

                }
                points[index] = CusPoint(sumX, sumY)
                index++
            }
            //            circelX[i] = sumX;
            //            circelY[i] = sumY;
            //            LogUtils.d("circelX:", circelX[i]);
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (state == STATES_ERROR) {
            mSourcePaint!!.color = Color.parseColor("#f06292")
        } else if (state == STATES_NO) {
            mSourcePaint!!.color = Color.BLACK
        }

        var isMoved = false
        val path = Path()
        for (point in points) {
            if (point!!.state == CusPoint.STATE_UNSELECTED) {
                canvas.drawCircle(point.x.toFloat(), point.y.toFloat(), (radius / 10).toFloat(), mSourcePaint)
                canvas.drawCircle(point.x.toFloat(), point.y.toFloat(), radius.toFloat(), mSourcePaint)
            } else {
                mSelectedPaint!!.style = Paint.Style.STROKE
                canvas.drawCircle(point.x.toFloat(), point.y.toFloat(), radius.toFloat(), mSelectedPaint)
                mSelectedPaint!!.style = Paint.Style.FILL
                canvas.drawCircle(point.x.toFloat(), point.y.toFloat(), (radius / 10).toFloat(), mSelectedPaint)
            }
        }

        var lastIndex = 0
        for (selectedIndex in selectedIndexs) {
            if (!isMoved) {
                path.moveTo(points[selectedIndex]!!.x.toFloat(), points[selectedIndex]!!.y.toFloat())
                isMoved = true
            } else {
                path.lineTo(points[selectedIndex]!!.x.toFloat(), points[selectedIndex]!!.y.toFloat())
            }
            lastIndex = selectedIndex
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
        if (getDistance(CusPoint(eventX.toInt(), eventY.toInt()), points[lastIndex]!!) > radius
                && state == STATES_ACTIONMOVE) {
            path.lineTo(eventX, eventY)
        }

        //        canvas.translate(prePoint.x, prePoint.y);

        //        path.lineTo(selectedPoint.x, selectedPoint.y);
        canvas.save()
        canvas.drawPath(path, mLinePaint)
        canvas.restore()

    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                eventX = event.x
                eventY = event.y
                state = STATES_ACTIONDOWN
                reset()
                selectedIndexs.clear()
                val selectPoint = getSelectPoint(event)
                if (selectPoint != null) {
                    isDrawing = true
                    postInvalidate()
                }
            }

            MotionEvent.ACTION_MOVE -> {
                eventX = event.x
                eventY = event.y
                state = STATES_ACTIONMOVE
                //                if (isDrawing) {
                val selectPoint1 = getSelectPoint(event)

                postInvalidate()
//                //解锁成功
//                if (!isSetPsd
//                        && isCompleted
//                        && onLockListener != null) {
//                    state = STATES_SUCCEED
//                    onLockListener!!.onSucceed(results)
//                }
            }
            MotionEvent.ACTION_UP -> {
                //解锁成功
                if (onLockListener != null) {
                    if (isCompleted) {
                        state = STATES_SUCCEED
                        onLockListener!!.onSucceed(results)
                    } else {
                        state = STATES_ERROR
                        onLockListener!!.onError()
                    }
                }
                postInvalidate()
                state = STATES_ACTIONUP
                removeCallbacks(actionUpRunnable)
                postDelayed(actionUpRunnable, 500)
            }
        }//                }
        return true
    }

    fun getSelectPoint(event: MotionEvent): CusPoint? {
        for (i in points.indices) {
            val distance = getDistance(points[i]!!, CusPoint(event.x.toInt(), event.y.toInt()))
            if (distance <= radius) {
                LogUtils.d("distance:", distance)
                if (!selectedIndexs.contains(i)) {
                    selectedIndexs.add(i)
                }
                points[i]!!.state = CusPoint.STATE_SELECTED
                return points[i]
            }
        }

        return null
    }

    fun setOnLockListener(onLockListener: OnLockListener) {
        this.onLockListener = onLockListener
    }

    fun setSetPsd(setPsd: Boolean) {
        isSetPsd = setPsd
    }

    /**
     * 获得图案解锁结果
     *
     * @return
     */
    private val results: String
        get() {
            val results = StringBuffer()
            for (selectedIndex in selectedIndexs) {
                results.append(selectedIndex)
            }
            return results.toString()
        }

    private //设置密码
    val isCompleted: Boolean
        get() {
            val results = results
            if (StringUtils.isEmpty(results)) {
                return false
            }

            if (isSetPsd) {
                return selectedIndexs.size > 3
            } else {
                if (results == SPUtils.getInstance(CstUtils.TABLENAME)
                        .getString(CstUtils.KEY_PASSWORD)
                        || results == SPUtils.getInstance(CstUtils.TABLENAME)
                        .getString(CstUtils.KEY_PSDSAVEME)) {
                    return true
                }
            }

            return false
        }

    private fun getDistance(point1: CusPoint, point2: CusPoint): Float {
        return Math.sqrt(Math.pow((point2.x - point1.x).toDouble(), 2.0) + Math.pow((point2.y - point1.y).toDouble(), 2.0)).toFloat()
    }

    var actionUpRunnable: Runnable = Runnable {
        reset()
        selectedIndexs.clear()
        state = STATES_NO
        postInvalidate()
    }

    private fun reset() {
        for (point in points) {
            point!!.state = CusPoint.STATE_UNSELECTED
        }
    }

    interface OnLockListener {

        fun onSucceed(result: String)

        fun onError()
    }

    companion object {
        private val DEFAULT_SELECT_COLOR = Color.parseColor("#a3e9a4")
        private val STATES_NO = -1
        private val STATES_ACTIONDOWN = 0
        private val STATES_ACTIONMOVE = 1
        private val STATES_ACTIONUP = 2
        private val STATES_SUCCEED = 3
        private val STATES_ERROR = 4
    }

}
