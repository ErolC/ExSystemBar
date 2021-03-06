package com.erolc.exbar.bar

import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes

/**
 * create by erolc at 2021/9/3 16:21.
 * 状态栏和导航栏都应该实现该接口
 */
interface Bar {
    /**
     * 栏的当前高度，会随着栏的隐藏而改变（所以该方法的值只有0和[getInherentHeight]）
     */
    fun getHeight(): Int

    /**
     * 栏固有高度
     */
    fun getInherentHeight():Int

    /**
     * 栏的背景色，如果状态栏的背景并非颜色，那么将返回-1
     */
    fun getBackgroundColor(): Int

    fun setBackgroundColor(color: Int)

    /**
     * 设置状态栏的背景
     */
    fun setBackground(@DrawableRes drawable: Int)

    /**
     * 设置状态栏的背景
     */
    fun setBackground(drawable: Drawable?)

    /**
     * 获取状态栏的背景
     */
    fun getBackground(): Drawable?

    /**
     * 获取默认的背景颜色
     */
    fun getDefaultBackgroundColor(): Int

    /**
     * 设置栏内容是否是暗系
     * @param isDark true-黑色，false-白色
     */
    fun setContentColor(isDark: Boolean)

    /**
     * 隐藏栏
     * @param behavior 是隐藏栏的时候的行为
     * @param isAdapterBang 是否适配刘海屏，如果[true]，那么内容会完全入侵到刘海位置，如果[false]则刘海部分会由黑色填充 如果是导航栏则忽略
     */
    fun hide(isAdapterBang: Boolean = false)

    /**
     * 显示状态栏
     */
    fun show()

    /**
     *内容入侵栏
     */
    fun invasion()

    fun isInvasion(): Boolean

    /**
     * 内容从状态栏中退出，回到原本的状态
     */
    fun unInvasion()

    /**
     * 栏是否展示
     */
    fun isShow(): Boolean

    /**
     * 栏字体颜色是否是暗系
     */
    fun getContentIsDark(): Boolean
}