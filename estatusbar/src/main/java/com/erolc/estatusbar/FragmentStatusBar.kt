package com.erolc.estatusbar

import android.app.Activity
import android.graphics.drawable.Drawable

/**
 * 这个对象应该与fragment相关，fragment归属不同的StatusBar，
 *
 * 如何做到，设置的手动设置的样式，在回到页面还能恢复呢？
 * 只能将手动设置的参数进行保存，然后在回到界面的时候重新设置，这里要对这些保存的样式数据进行管理，该类的生命长度在fragmnet的生命长度。
 * 在原本的statusBar包裹一层FragmentStatusBar，作为中间层，用作保存fragment特有的样式，
 *
 * 由于各种设置之间都有牵绊，比如设置了背景色，那么如果原本有设置背景资源，那么背景资源就得失效
 */
class FragmentStatusBar(activity: Activity, val statusBar: StatusBar) : StatusBar by statusBar {
    private var color: Int? = null
    private var sysColor: Int? = activity.defStatusBarColor
    private var drawableRes: Int? = null
    private var drawable: Drawable? = null
    private var textColor: Boolean? = null
    private var isHide: Int = 3
    private var immersive: Boolean = false


    override fun setBackgroundColor(color: Int) {
        this.color = color
        updateBgStyle(1)
        statusBar.setBackgroundColor(color)
    }

    override fun setBackground(drawable: Int) {
        drawableRes = drawable
        updateBgStyle(4)

        statusBar.setBackground(drawable)
    }

    override fun setBackground(drawable: Drawable) {
        this.drawable = drawable
        updateBgStyle(3)
        statusBar.setBackground(drawable)
    }


    override fun setSysBackgroundColor(color: Int) {
        sysColor = color
        updateBgStyle(2)
        statusBar.setSysBackgroundColor(color)
    }

    override fun setTextColor(isDark: Boolean) {
        textColor = isDark
        statusBar.setTextColor(isDark)
    }

    override fun hide(isAdapterBang: Boolean) {
        isHide = if (isAdapterBang) 1 else 2
        immersive = false
        statusBar.hide(isAdapterBang)
    }

    override fun show() {
        isHide = 3
        immersive = false
        statusBar.show()
    }

    override fun immersive() {
        immersive = true
        isHide = 0
        statusBar.immersive()
    }

    private fun updateBgStyle(type: Int) {
        color = if (type == 1) color else null
        sysColor = if (type == 2) sysColor else null
        drawable = if (type == 3) drawable else null
        drawableRes = if (type == 4) drawableRes else null

        immersive = false
    }

    fun restore() {
        if (isHide == 3) {
            statusBar.show()
            color?.let { statusBar.setBackgroundColor(it) }
            sysColor?.let { statusBar.setSysBackgroundColor(it) }
            drawableRes?.let { statusBar.setBackground(it) }
            drawable?.let { statusBar.setBackground(it) }
            textColor?.let { statusBar.setTextColor(it) }
        } else if (!immersive) {
            statusBar.hide(isHide == 1)
        }

        if (immersive) {
            statusBar.immersive()
            textColor?.let { statusBar.setTextColor(it) }
        }
    }


}