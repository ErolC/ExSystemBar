package com.erolc.estatusbar


import android.annotation.TargetApi
import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.annotation.ColorInt
import android.support.annotation.DrawableRes
import android.support.v4.app.Fragment
import android.util.TypedValue
import android.view.View
import android.view.Window.ID_ANDROID_CONTENT
import android.view.WindowManager
import android.widget.FrameLayout

const val STATUS_BAR = "statusBar"
/**
 * 状态栏高度
 */
val Activity.statusBarHeight
    get() = run {
        val identifier = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (identifier > 0) resources.getDimensionPixelSize(identifier) else 0
    }
/**
 * 状态栏的背景颜色
 */
var Activity.statusBarColor
    get() = run {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor
        } else {
            val background = getStatusBarView().background
            if (background is ColorDrawable) background.color else -1
        }
    }

    set(value) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = value
            setStatusBarTextColor(value == Color.WHITE)
        } else {
            getStatusBarView().setBackgroundColor(value)

        }
        setStatusBarTextColor(value == Color.WHITE)
    }
/**
 * 状态栏是否显示
 */
val Activity.isShowStatusBar get() = window.attributes.flags and WindowManager.LayoutParams.FLAG_FULLSCREEN == 0

private val Activity.contentView get() = window.decorView.findViewById<FrameLayout>(ID_ANDROID_CONTENT)

/**
 * 系统标题栏高度
 */
val Activity.actionBarHeight
    get() = run {
        val value = TypedValue()
        if (theme.resolveAttribute(android.R.attr.actionBarSize, value, true))
            TypedValue.complexToDimensionPixelSize(value.data, resources.displayMetrics)
        else
            0
    }

/**
 * 状态栏字体颜色是否为暗色
 */
val Activity.statusBarTextColorIsDark
    @TargetApi(Build.VERSION_CODES.M)
    get()=run{
        window.decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR != 0
    }
/**
 * 状态栏字体的颜色
 * @param isDark 由于状态栏字体颜色只有明和暗两种，true为明，false为暗
 * @param isReserved 由于状态栏字体颜色的设置会与之前的其他状态会有冲突，所以该参数是为是否保留之前的状态，true保留，false 不保留
 */
@TargetApi(Build.VERSION_CODES.M)
fun Activity.setStatusBarTextColor(isDark: Boolean, isReserved: Boolean = true) {
    val systemUiVisibility = window.decorView.systemUiVisibility
    var option = if (isDark) View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR else View.SYSTEM_UI_FLAG_VISIBLE
    option = if (isReserved) option or systemUiVisibility else option
    window.decorView.systemUiVisibility = option
}

/**
 * 隐藏状态栏，手动在顶部下滑可重新显示，之后还会自动隐藏，
 */
fun Activity.hideStatusBar() {
    statusBar?.visibility = View.GONE
    window.clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN)
    window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
    if (isCustomizeStatusBar())
        updateLayout()
}

/**
 * 展示状态栏，与hideStatusBar()方法是一套的，
 */
fun Activity.showStatusBar() {
    statusBar?.visibility = View.VISIBLE
    window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
    window.setFlags(
        WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN
    )
    if (isCustomizeStatusBar())
        updateLayout()
}

/**
 * @param drawableRes 状态栏背景的drawable文件资源
 * @param isDark 状态栏字体颜色是否是黑色
 */
fun Activity.setStatusBarBackground(
    @DrawableRes drawableRes: Int,
    isDark: Boolean = true
) {
    getStatusBarView().setBackgroundResource(drawableRes)
    setStatusBarTextColor(isDark)
}

/**
 * 设置状态栏背景
 */
fun Activity.setStatusBarBackground(drawable: Drawable, isDark: Boolean = true) {
    getStatusBarView().background = drawable
    setStatusBarTextColor(isDark)
}

fun Activity.getStatusBarBackground():Drawable {
    return getStatusBarView().background
}

/**
 * 自定义StatusBar
 */
private fun Activity.getStatusBarView(): View {
    val view = statusBar ?: View(this)
    view.tag = STATUS_BAR
    immersive()
    updateLayout()
    contentView.addView(view)
    val layoutParams = view.layoutParams as FrameLayout.LayoutParams
    layoutParams.height = statusBarHeight
    layoutParams.topMargin = -contentView.paddingTop
    view.layoutParams = layoutParams
    return view
}

/**
 * 是否为自定义的状态栏
 */
private fun Activity.isCustomizeStatusBar(): Boolean {
    return contentView.findViewWithTag<View>(STATUS_BAR) != null
}

/**
 * 让系统的状态栏背景消失，计算一些数值
 */
private fun Activity.updateLayout(defaultTop: Int = -1) {
    val findViewById = window.decorView.findViewById<View>(R.id.action_mode_bar_stub)//这个view在标题栏存在时，不存在
    var paddingTop =
        if (findViewById == null) {
            actionBarHeight + if (isShowStatusBar) statusBarHeight else 0
        } else {
            if (isShowStatusBar) statusBarHeight else 0
        }
    if (defaultTop != -1) {
        paddingTop = defaultTop
    }
    contentView.clipToPadding = false
    contentView.setPadding(0, paddingTop, 0, 0)
}

/**
 * 获得自定义状态栏
 */
private val Activity.statusBar :View? get() = contentView.findViewWithTag(STATUS_BAR)

/**
 * 状态栏背景消失，内容层渗透到状态栏的区域里。
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
fun Activity.immersive() {
    window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
    val findViewWithTag = statusBar
    if (findViewWithTag != null) {
        contentView.removeView(findViewWithTag)
        updateLayout(0)
    }
    statusBarColor = Color.TRANSPARENT
}


/*----------------------------fragment------------------------------------*/

fun Fragment.setStatusBarBackground(@DrawableRes res: Int, isDark: Boolean = true) {
    requireActivity().setStatusBarBackground(res, isDark)
}

fun Fragment.setStatusBarBackground(res: Drawable, isDark: Boolean = true) {
    requireActivity().setStatusBarBackground(res, isDark)
}

fun Fragment.showStatusBar() {
    requireActivity().showStatusBar()
}

fun Fragment.hideStatusBar() {
    requireActivity().hideStatusBar()
}

fun Fragment.immersive() {
    requireActivity().immersive()
}

/**
 * 状态栏高度
 */
val Fragment.statusBarHeight
    get() = run {
        requireActivity().statusBarHeight
    }
/**
 * 状态栏的背景颜色
 */
var Fragment.statusBarColor
    get() = run {
        requireActivity().statusBarColor
    }
    set(value) {
        requireActivity().statusBarColor = value
    }

/**
 * 状态栏是否显示
 */
val Fragment.isShowStatusBar
    get() = requireActivity().isShowStatusBar

val Fragment.statusBarTextColorIsDark get() = requireActivity().statusBarTextColorIsDark

