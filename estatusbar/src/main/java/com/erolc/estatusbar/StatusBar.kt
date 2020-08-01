package com.erolc.estatusbar


import android.app.Activity
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData


/**
 * 这是一个设置状态栏的类，用法很简单，唯一一个建议就是，在onResume回调中使用。
 */

class StatusBar(private val activity: Activity) {

    constructor(fragment: Fragment) : this(fragment.requireActivity())

    fun debug(isDebug: Boolean) {
        statusBarDebug = isDebug
    }

    fun isDebug(): Boolean {
        return statusBarDebug
    }

    fun setBackgroundColor(@ColorInt color: Int) {
        activity.statusBarColor = color
    }

    fun setBackground(@DrawableRes res: Int, isBack: Boolean) {
        activity.setStatusBarBackground(res, isBack)
    }

    fun setBackground(drawable: Drawable?, isBack: Boolean) {
        activity.setStatusBarBackground(drawable!!, isBack)
    }

    fun show() {
        activity.showStatusBar()
    }

    fun hide() {
        activity.hideStatusBar()
    }

    fun getHeight(): Int {
        return activity.statusBarHeight
    }

    fun getBackgroundColor(): Int {
        return activity.statusBarColor
    }

    fun getBackground(): Drawable? {
        return activity.getStatusBarBackground()
    }

    fun isShow(): Boolean {
        return activity.isShowStatusBar
    }

    fun textIsDark(): Boolean {
        return activity.statusBarTextColorIsDark
    }

    fun immersive() {
        activity.immersive()
    }

    fun setTextColor(isDark: Boolean) {
        activity.setStatusBarTextColor(isDark)
    }

    @JvmOverloads
    fun curtain(
        alpha: Int = 0,
        red: Int = 255,
        green: Int = 255,
        blue: Int = 255
    ): MutableLiveData<Int>? {
        if (activity is FragmentActivity) {
            return activity.statusBarCurtain(alpha, red, green, blue)
        } else {
            Log.e(
                "curtain",
                "curtain: this activity isn't extends FragmentActivity,can't use the function"
            )
        }
        return null
    }
}
