package com.rong.map.saveme.model;

import android.graphics.Point;

/**
 * Created by Administrator on 2017/8/13/013.
 */

public class CusPoint extends Point{

    public static final int STATE_UNSELECTED = 0;
    public static final int STATE_SELECTED = 1;

    public int state = STATE_UNSELECTED;

    public CusPoint(int state) {
        this.state = state;
    }

    public CusPoint(int x, int y, int state) {
        super(x, y);
        this.state = state;
    }

    public CusPoint(Point src, int state) {
        super(src);
        this.state = state;
    }

    public CusPoint() {

    }

    public CusPoint(int x, int y) {
        super(x, y);
    }

    public CusPoint(Point src) {
        super(src);
    }
}
