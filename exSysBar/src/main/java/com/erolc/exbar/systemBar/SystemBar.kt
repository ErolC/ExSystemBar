package com.erolc.exbar.systemBar

import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.core.view.WindowInsetsControllerCompat
import com.erolc.exbar.bar.Bar
/**
 * create by erolc at 2021/9/3 16:18.
 * 系统栏，代表状态栏和导航栏
 */
interface SystemBar {

    /**
     * 全屏
     * @param isAdapterBang 是否适配刘海屏，是否将内容入侵到刘海内
     */
    fun fullScreen(isAdapterBang:Boolean = true)

    /**
     * 设置系统栏背景颜色
     */
    fun setBackgroundColor(color: Int)

    /**
     * 设置系统栏背景
     */
    fun setBackground(@DrawableRes drawable: Int)
    /**
     * 设置系统栏背景
     */
    fun setBackground(drawable: Drawable)
    /**
     * 设置系统栏内容颜色 ，是否是暗色
     */
    fun setContentColor(isDark:Boolean)
    /**
     * 获取[type]类型的系统栏高度，其中[type]包含[STATUS_BAR]和[NAVIGATION_BAR]
     */
    fun getHeight(type: Int = STATUS_BAR): Int
    /**
     * 获取[type]类型的系统栏背景，其中[type]包含[STATUS_BAR]和[NAVIGATION_BAR]
     */
    fun getBackground(type: Int = STATUS_BAR): Drawable?
    /**
     * 获取[type]类型的系统栏背景颜色，其中[type]包含[STATUS_BAR]和[NAVIGATION_BAR]
     */
    fun getBackgroundColor(type: Int = STATUS_BAR): Int
    /**
     * 获取[type]类型的系统栏内容颜色是否为暗色，其中[type]包含[STATUS_BAR]和[NAVIGATION_BAR]
     */
    fun getContentIsDark(type: Int = STATUS_BAR): Boolean
    /**
     * 获取[type]类型的系统栏，其中[type]包含[STATUS_BAR]和[NAVIGATION_BAR]
     */
    fun getBar(type: Int = STATUS_BAR): Bar
    /**
     * 展示系统栏
     */
    fun show()

    fun onConfigurationChanged()

    companion object{
        /**
         * 代表状态栏
         */
        const val STATUS_BAR = 0

        /**
         * 代表导航栏
         */
        const val NAVIGATION_BAR = 1

    }
}