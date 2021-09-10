package com.erolc.exbar.systemBar

import android.app.Activity
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.activity.ComponentActivity
import androidx.core.view.*
import com.erolc.exbar.*

import com.erolc.exbar.bar.Bar
import com.erolc.exbar.bar.LifeCycleBar
import com.erolc.exbar.bar.NavigationBarImpl
import com.erolc.exbar.bar.StatusBarImpl

/**
 * create by erolc at 2021/9/3 16:53.
 */
class SystemBarImpl(
    activity: Activity, val navigationBar: LifeCycleBar,
    val statusBar: LifeCycleBar
) : SystemBar {

    init {
        //将内容入侵到状态栏和导航栏中
        WindowCompat.setDecorFitsSystemWindows(activity.window, false)
//        //将状态栏和背景栏变透明
        ViewCompat.setOnApplyWindowInsetsListener(activity.window.decorView) { v, insets ->
            val nBar = insets.getInsets(WindowInsetsCompat.Type.navigationBars())
            val navBar = navigationBar.exeBar as NavigationBarImpl
            navBar.updateBar(nBar)
            insets
        }
//        imeAnim(activity)
        statusBar.setSystemBar(this)
        navigationBar.setSystemBar(this)
    }

    private val Activity.contentView get() = window.decorView.findViewById<FrameLayout>(Window.ID_ANDROID_CONTENT)

    fun updateLayout(activity: Activity, defaultValue: Int, type: Int) {
        var paddingTop = (statusBar.exeBar as StatusBarImpl).getLayoutValue()
        val navBar = (navigationBar.exeBar as NavigationBarImpl)
        val (right, bottom) = navBar.getLayoutValue()
        var paddingRight = right
        var paddingBottom = bottom
        if (defaultValue != -1) {
            when (type) {
                STATUS_BAR -> paddingTop = defaultValue
                else -> {
                    if (paddingBottom != 0) {
                        paddingBottom = defaultValue
                    } else {
                        paddingRight = defaultValue
                    }
                }
            }
        }
        activity.contentView.clipToPadding = false//让子view可以扩展到padding中去

        activity.contentView.setPadding(0, paddingTop, paddingRight, paddingBottom)//设置内容的padding
    }

    override fun fullScreen(isAdapterBang: Boolean) {
        statusBar.hide(isAdapterBang)
        navigationBar.hide()
    }

    override fun setBackgroundColor(color: Int) {
        statusBar.setBackgroundColor(color)
        navigationBar.setBackgroundColor(color)
    }

    override fun setBackground(drawable: Int) {
        statusBar.setBackground(drawable)
        navigationBar.setBackground(drawable)
    }

    override fun setBackground(drawable: Drawable) {
        statusBar.setBackground(drawable)
        navigationBar.setBackground(drawable)
    }

    override fun setContentColor(isDark: Boolean) {
        navigationBar.setContentColor(isDark)
        statusBar.setContentColor(isDark)
    }

    override fun getHeight(type: Int): Int {
        return getBar(type).getHeight()
    }

    override fun getBackground(type: Int): Drawable? {
        return getBar(type).getBackground()
    }

    override fun getBackgroundColor(type: Int): Int {
        return getBar(type).getBackgroundColor()
    }

    override fun getContentIsDark(type: Int): Boolean {
        return getBar(type).getContentIsDark()
    }


    override fun getBar(type: Int): Bar {
        return when (type) {
            STATUS_BAR -> statusBar
            else -> navigationBar
        }
    }

    override fun show() {
        navigationBar.show()
        statusBar.show()
    }

    private fun imeAnim(activity: Activity) {
        if (activity.window.containSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)) {
            ViewCompat.setOnApplyWindowInsetsListener(activity.contentView,
                object : OnApplyWindowInsetsListener {
                    private var isChange = false
                    private var isFirst = true

                    override fun onApplyWindowInsets(
                        view: View,
                        insets: WindowInsetsCompat
                    ): WindowInsetsCompat {
                        val isVisible = insets.isVisible(WindowInsetsCompat.Type.ime())
                        if (!ExSystemBar.adapterIme) {
                            return insets
                        }
                        if (isFirst) {
                            view.viewTreeObserver.addOnGlobalLayoutListener {
                                if (isFirst) {
                                    view.updateLayoutParams { height = view.measuredHeight }
                                    isFirst = false
                                }
                            }
                        }

                        if (isChange != isVisible) {
                            val contentHeight = view.computeVisibleDisplayHeight()
                            view.heightAnim(contentHeight, 100)
                            isChange = isVisible
                        }
                        return insets
                    }
                })
        }

    }
}