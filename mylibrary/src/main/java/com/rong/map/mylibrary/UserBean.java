package com.rong.map.mylibrary;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableInt;

/**
 * 作者：陈华榕
 * 邮箱:mpa.chen@sportq.com
 * 时间：2017/10/10  14:27
 */

public class UserBean extends BaseObservable {

    private String name;
    private int progress;
    private ObservableBoolean isLike = new ObservableBoolean(false);


    public UserBean(String name) {
        this.name = name;
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(com.rong.map.mylibrary.BR.name);
    }

    @Bindable
    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
        notifyPropertyChanged(com.rong.map.mylibrary.BR.progress);
    }

    public ObservableBoolean getIsLike() {
        return isLike;
    }

    public void setIsLike(boolean isLike) {
        this.isLike.set(isLike);
    }
}
