package com.erolc.exbar

import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.lifecycle.MutableLiveData

/**
 * @author ErolC 28/10/2020
 * 状态栏,该接口是对状态栏进行规范
 *
 *  可以加上一些对于状态栏的处理方案，比如在入侵的时候给外界反馈一个高度，以实现内容入侵但是内容和状态栏不会重叠
 *
 */
interface StatusBar {
    /**
     * 状态栏的高度
     */
    fun getHeight():Int

    /**
     * 状态栏的背景色
     */
    fun getBackgroundColor():Int

    /**
     * 设置状态栏的背景色
     */
    fun setBackgroundColor(color:Int)

    /**
     * 设置状态栏的背景
     */
    fun setBackground(@DrawableRes drawable:Int)

    /**
     * 设置状态栏的背景
     */
    fun setBackground(drawable:Drawable)

    /**
     * 获取状态栏的背景
     */
    fun getBackground():Drawable?




    /**
     * 设置状态栏文字是否是暗系
     * @param isDark true-黑色，false-白色
     */
    fun setTextColor(isDark: Boolean)

    /**
     * 隐藏状态栏
     * @param isAdapterBang 是否适配刘海屏，如果[true]，那么内容会完全入侵到刘海位置，如果[false]则刘海部分会由黑色填充
     */
    fun hide(isAdapterBang:Boolean = true)

    /**
     * 显示状态栏
     */
    fun show()

    /**
     *沉浸式，内容入侵状态栏
     */
    fun immersive()

    /**
     * 幕布，像是在状态栏盖了一层幕布，事实上也是设置状态栏背景颜色，但是这里是通过设置argb设置，并且可以得到一个实时变化的监听
     */
    @Deprecated("已废弃")
    fun  curtain(
        alpha: Int = 0,
        red: Int = 255,
        green: Int = 255,
        blue: Int = 255): MutableLiveData<Int>

    /**
     * 状态栏是否展示
     */
    fun isShow():Boolean

    /**
     * 状态栏字体颜色是否是暗系
     */
    fun isDark():Boolean

}