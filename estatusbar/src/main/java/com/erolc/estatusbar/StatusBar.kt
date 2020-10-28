package com.erolc.estatusbar

import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.lifecycle.MutableLiveData

/**
 * 状态栏
 */
interface StatusBar {

    fun getHeight():Int
    fun getBackgroundColor():Int
    fun setBackgroundColor(@ColorInt color:Int)
    fun setBackground(@DrawableRes drawable:Int)
    fun setBackground(drawable:Drawable)
    fun getBackground():Drawable?
    fun setSysBackgroundColor(color:Int)
    fun getSysBackgroundColor():Int
    fun setTextColor(isDark: Boolean)
    fun hide()
    fun show()
    fun immersive()

    fun curtain(
        alpha: Int = 0,
        red: Int = 255,
        green: Int = 255,
        blue: Int = 255): MutableLiveData<Int>

    fun isCustomizeStatusBar(): Boolean
    fun isShowStatusBar():Boolean
    fun textColorIsDark():Boolean

}