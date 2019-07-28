package com.erolc.estatusbar;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.v4.app.Fragment;

import java.lang.ref.SoftReference;

public class StatusBar {
    private SoftReference<Activity> reference;

    public StatusBar(Activity activity){
        this.reference = new SoftReference<>(activity);
    }

    public StatusBar(Fragment fragment) {
        this(fragment.requireActivity());
    }

    public void setBackgroundColor(@ColorInt int color,boolean isBack) {
        StatusBarKt.setStatusBarColor(reference.get(),color,isBack);
    }

    public void setBackground(@DrawableRes int res,boolean isBack){
        StatusBarKt.setStatusBarBackground(reference.get(),res,isBack);
    }

    public void setBackground(Drawable drawable, boolean isBack) {
        StatusBarKt.setStatusBarBackground(reference.get(),drawable,isBack);
    }

    public void setSystemBarColor(@ColorInt int color) {
        StatusBarKt.setStatusBarColor(reference.get(),color);
    }

    public void show(){
        StatusBarKt.showStatusBar(reference.get());
    }

    public void hide() {
        StatusBarKt.hideStatusBar(reference.get());
    }

    public int getHeight(){
        return StatusBarKt.getStatusBarHeight(reference.get());
    }

    public int getSystemColor() {
        return StatusBarKt.getStatusBarColor(reference.get());
    }

    public boolean isShow(){
        return StatusBarKt.isShowStatusBar(reference.get());
    }

    public void immersive() {
        StatusBarKt.immersive(reference.get());
    }
}