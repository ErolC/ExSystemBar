package com.erolc.exbar

import android.graphics.drawable.Drawable
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner

/**
 * 生命周期感知的StatusBar，该对象，每个activity和fragment都会有各自的一个，使得各个页面之间的设置都相互隔离，并且可以在页面销毁的时候，销毁相应的对象。
 *
 * 注意：当在fragment+ViewPager模式下使用的时候，确保是新版本的fragment，['androidx.appcompat:appcompat:1.2.0']以上,如果[Fragment.setUserVisibleHint]已经废弃，则是新的fragment
 * 否则会在切换页面的时候statusBar表现出不一样的效果。
 *
 */
internal class LifeCycleStatusBar(
    lifecycle: Lifecycle,
    val statusBar: StatusBar
    ) : StatusBar by statusBar {

    private var color: Int? = null
    private var sysColor: Int? = null
    private var drawableRes: Int? = null
    private var curtainColor: Int? = null
    private var drawable: Drawable? = null
    private var textColor: Boolean? = null
    private var isHide: Int = 3
    private var immersive: Boolean = false

    private val exeStatusBar = if(statusBar is LifeCycleStatusBar) statusBar.statusBar else statusBar

    init {
        lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                if (event == Lifecycle.Event.ON_RESUME) {
                    restore()
                } else if (event == Lifecycle.Event.ON_DESTROY) {
                    StatusBarFactory.clear(source.hashCode())
                }
            }
        })
    }

    override fun setBackgroundColor(color: Int) {
        this.color = color
        updateBgStyle(1)
        exeStatusBar.setBackgroundColor(color)
    }

    override fun setBackground(drawable: Int) {
        drawableRes = drawable
        updateBgStyle(4)

        exeStatusBar.setBackground(drawable)
    }

    override fun setBackground(drawable: Drawable) {
        this.drawable = drawable
        updateBgStyle(3)
        exeStatusBar.setBackground(drawable)
    }


    override fun setSysBackgroundColor(color: Int) {
        sysColor = color
        updateBgStyle(2)
        exeStatusBar.setSysBackgroundColor(color)
    }

    override fun setTextColor(isDark: Boolean) {
        textColor = isDark
        exeStatusBar.setTextColor(isDark)
    }

    override fun hide(isAdapterBang: Boolean) {
        isHide = if (isAdapterBang) 1 else 2
        immersive = false
        exeStatusBar.hide(isAdapterBang)
    }

    override fun show() {
        isHide = 3
        immersive = false
        exeStatusBar.show()
    }

    override fun immersive() {
        immersive = true
        isHide = 0
        exeStatusBar.immersive()
    }

    /**
     * 更新背景资源，除了刚设置的资源，其他的都需要清除
     */
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

    /**
     * 对资源进行恢复
     */
    private fun restore() {
        if (isHide == 3) {
            exeStatusBar.show()
            color?.let { exeStatusBar.setBackgroundColor(it) }
            sysColor?.let { exeStatusBar.setSysBackgroundColor(it) }
            drawableRes?.let { exeStatusBar.setBackground(it) }
            drawable?.let { exeStatusBar.setBackground(it) }
            textColor?.let { exeStatusBar.setTextColor(it) }
        } else if (!immersive) {
            exeStatusBar.hide(isHide == 1)
        }

        if (immersive) {
            exeStatusBar.immersive()
            textColor?.let { exeStatusBar.setTextColor(it) }
        }
    }


}