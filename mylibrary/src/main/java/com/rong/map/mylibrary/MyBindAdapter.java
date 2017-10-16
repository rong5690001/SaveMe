package com.rong.map.mylibrary;

import android.databinding.BindingAdapter;
import android.widget.TextView;

/**
 * 作者：陈华榕
 * 邮箱:mpa.chen@sportq.com
 * 时间：2017/10/10  16:01
 */

public class MyBindAdapter {

    @BindingAdapter("name")
    public static void setName111(TextView textView, String name) {
        textView.setText(name);
    }

}
