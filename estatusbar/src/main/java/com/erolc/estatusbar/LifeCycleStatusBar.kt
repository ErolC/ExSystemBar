package com.erolc.estatusbar

import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData

/**
 * 生命周期感知的StatusBar，该对象，每个activity和fragment都会有各自的一个，使得各个页面之间的设置都相互隔离，并且可以在页面销毁的时候，销毁相应的对象。
 *
 * 注意：当在fragment+ViewPager模式下使用的时候，确保是新版本的fragment，['androidx.appcompat:appcompat:1.2.0']以上,如果[Fragment.setUserVisibleHint]已经废弃，则是新的fragment
 * 否则会在切换页面的时候statusBar表现出不一样的效果。
 *
 */
class LifeCycleStatusBar(
    lifecycle: Lifecycle,
    hashCode: Int,
    private val statusBar: StatusBar
    ) : StatusBar by statusBar {

    private var color: Int? = null
    private var sysColor: Int? = null
    private var drawableRes: Int? = null
    private var curtainColor: Int? = null
    private var drawable: Drawable? = null
    private var textColor: Boolean? = null
    private var isHide: Int = 3
    private var immersive: Boolean = false


    init {
        lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                if (event == Lifecycle.Event.ON_RESUME) {
                    restore()
                } else if (event == Lifecycle.Event.ON_DESTROY) {
                    StatusBarFactory.clear(hashCode)
                }
            }
        })
    }

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

//    override fun curtain(alpha: Int, red: Int, green: Int, blue: Int): MutableLiveData<Int> {
//        curtainColor = Color.argb(alpha, red, green, blue)
//
//        return statusBar.curtain(alpha, red, green, blue)
//    }

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
        curtainColor = if (type == 5) curtainColor else null
        if (isHide == 0) {
            isHide = 3
        }
        immersive = false
    }

    private fun restore() {
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