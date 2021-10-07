package com.erolc.exbar.bar


import android.app.Activity
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.TypedValue
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.Window
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.activity.ComponentActivity
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.updateLayoutParams
import com.erolc.exbar.*
import com.erolc.exbar.log
import com.erolc.exbar.loge
import com.erolc.exbar.systemBar.SystemBarImpl


/**
 *
 * @author erolc 28/10/2020
 * 这是一个设置状态栏的实现类
 *
 */

internal class StatusBarImpl(
    private val activity: ComponentActivity
) : Bar {
    private val STATUS_BAR = "statusBar"
    private var systemBar: SystemBarImpl? = null

    private val insetsController =
        WindowCompat.getInsetsController(activity.window, activity.window.decorView)

    private var statusBar: View?
    private var offset = 0
    private var isInvasion = false


    init {
        /**
         * 得到内容中状态栏部分view，由于这个view是我自己设置的，所以不一定存在，
         */
        statusBar = activity.contentView.findViewWithTag(STATUS_BAR)
            ?: activity.getStatusBarView()//通过一开始就使用自定义状态栏解决在运行时第一次使用的时候会出现布局底部留空
        setBackgroundColor(activity.defStatusBarColor)
    }

    /**
     * 设置该方法，在调用[hide]的时候，是刘海屏，内容部分布局也会侵入到状态栏部分，如果刘海挡住了你的部分内容，可以将上边距设置为[getHeight]的高度避免
     */
    private fun adapterBang(isAdapterBang: Boolean = true) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val lp: WindowManager.LayoutParams = activity.window.attributes
            lp.layoutInDisplayCutoutMode = if (isAdapterBang) {
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
            } else {
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_DEFAULT
            }

            activity.window.attributes = lp
        }
    }

    internal fun setSystemBar(systemBar: SystemBarImpl) {
        this.systemBar = systemBar
        statusBar = activity.getStatusBarView()
    }

    /**
     * 默认的状态栏颜色
     */
    private val Activity.defStatusBarColor: Int
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor
        } else {
            ContextCompat.getColor(this, R.color.colorPrimaryDark)
        }

    /**
     * 得到内容部分view
     */
    private val Activity.contentView get() = window.decorView.findViewById<FrameLayout>(Window.ID_ANDROID_CONTENT)

    /**
     * 检查颜色是否为亮色
     */
    private fun isLightColor(color: Int) = ColorUtils.calculateLuminance(color) >= 0.5


    fun View.computeVisibleDisplayHeight(): Int {
        val r = Rect()
        getWindowVisibleDisplayFrame(r)
        loge(r.bottom)
        return r.bottom - r.top
    }

    /**
     * 自定义StatusBar
     * 该方法被调用的时候就已经替换了系统状态栏
     */
    private fun Activity.getStatusBarView(): View {
        val findStatusBar = findStatusBar()
        val view = findStatusBar ?: View(this)//获取自定义的状态栏，如果没有则新建
        view.tag = STATUS_BAR//设置tag
        updateLayout()//更新布局
        if (findStatusBar == null) {
            contentView.addView(view)//将自定义状态栏添加到内容布局中，由于内容布局是frameLayout，那么会在顶部
        }
        updateStatus()
        return view//这里就得到最终的自定义状态栏
    }

    private fun updateStatus() {
        findStatusBar()?.updateLayoutParams<FrameLayout.LayoutParams> {
            height = getHeight()//设置自定义状态栏高度
            val hasSoftMode =
                activity.window.containSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
            if (hasSoftMode) {
                topMargin = -activity.contentView.paddingTop + offset
            } else {
                topMargin = -activity.contentView.paddingTop
            }
            loge("update ${-activity.contentView.paddingTop} + $isInvasion")
        }
    }

    /**
     * 在状态栏位置寻找状态栏，和getStatusBarView 不一样的是，getStatusBarView在状态栏还是系统状态栏的时候，也会先替换，再返回
     * 而这个方法是寻找状态栏，如果没有自定义，那么就返回null
     */
    private fun findStatusBar(): View? {
        return statusBar ?: activity.contentView.findViewWithTag(STATUS_BAR)
    }


    /**
     * 系统标题栏高度
     */
    private val Activity.actionBarHeight
        get() = run {
            val value = TypedValue()
            val height =
                if (theme.resolveAttribute(android.R.attr.actionBarSize, value, true))//判断标题栏是否存在
                    TypedValue.complexToDimensionPixelSize(value.data, resources.displayMetrics)
                else
                    0
            height
        }

    /**
     * 状态栏字体颜色是否为暗色
     */
    private val statusBarTextColorIsDark
        get() = insetsController?.isAppearanceLightStatusBars ?: false

    internal fun getLayoutValue(): Int {
        val findViewById =
            activity.window.decorView.findViewById<View>(R.id.action_mode_bar_stub)//这个view在标题栏存在时，不存在，
        //计算并得到标题栏存在或者不存在的时候的上内边距
        return if (findViewById == null || findViewById.visibility != GONE) {//表明标题栏存在
            activity.actionBarHeight + if (isShowStatusBar) getHeight() else 0//当标题栏存在的时候，上内边距就需要包括标题栏的高度，以及自定义的状态栏的高度，当然前提是这个状态栏存在
        } else {
            if (isShowStatusBar) getHeight() else 0//当标题栏不存在的时候，只需要考虑状态栏的高度
        }
    }

    /**
     * 让系统的状态栏背景消失，计算一些数值
     * 系统标题栏和内容布局无关，所以你需要重新计算，否则内容布局会被标题栏遮盖
     */
    private fun Activity.updateLayout(defaultTop: Int = -1) {
        systemBar?.updateLayout(this, defaultTop, 0)
    }

    /**
     * StatusBar是否展示
     */
    private val isShowStatusBar get() = findStatusBar()?.visibility == VISIBLE

    /**
     * 清除自定义StatusBar
     */
    @Deprecated("已经完全有自定义的代替，所以没必要清除")
    private fun clearStatusBar() {
        findStatusBar()?.apply {
            background = null
            activity.contentView.removeView(this)
            statusBar = null
        }
    }


    /**
     * 是否为自定义的状态栏
     */
    @Deprecated("已经完全由自定义的代替")
    private fun isCustomizeStatusBar(): Boolean {
        return findStatusBar() != null//根据tag查看自定义状态栏是否存在
    }


    override fun isShow(): Boolean {
        return isShowStatusBar
    }

    override fun getContentIsDark(): Boolean {
        return statusBarTextColorIsDark
    }

//    override fun recovery() {
//        setBackgroundColor(activity.defStatusBarColor)
//    }


    override fun getHeight(): Int {
        if (isInvasion) {
            return 0
        }
        return getHeight(activity)
    }

    companion object {
        internal fun getHeight(activity: Activity): Int {
            val identifier =
                activity.resources.getIdentifier("status_bar_height", "dimen", "android")
            val i = if (identifier > 0) activity.resources.getDimensionPixelSize(identifier) else 0
            return i
        }
    }

    override fun getBackgroundColor(): Int {
        val background = activity.getStatusBarView().background//自定义的状态栏背景颜色
        return if (background is ColorDrawable) background.color else -1//如果这个背景不是颜色，（自定义状态栏的背景可以是drawable），则返回-1
    }

    /**
     * 设置背景
     */
    override fun setBackgroundColor(color: Int) = setBackground(ColorDrawable(color))
    override fun setBackground(@DrawableRes drawable: Int) =
        setBackground(ContextCompat.getDrawable(activity, drawable))

    override fun setBackground(drawable: Drawable?) {
        activity.getStatusBarView().background = drawable
        if (drawable is ColorDrawable) {
            val lightColor = isLightColor(drawable.color)
            setContentColor(lightColor)//设置自定义状态栏的字体颜色
        }
    }

    override fun getBackground(): Drawable? {
        return findStatusBar()?.background
    }

    override fun getDefaultBackgroundColor(): Int {
        return activity.defStatusBarColor
    }

    /**
     * 设置内容颜色，是否是暗色，目前只能设置暗色和亮色
     */
    override fun setContentColor(isDark: Boolean) {
        insetsController?.isAppearanceLightStatusBars = isDark
    }

    /**
     * 隐藏状态栏，[isAdapterBang]决定内容是否入侵到刘海内，true为入侵
     */
    override fun hide(isAdapterBang: Boolean) {
        insetsController?.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        insetsController?.hide(WindowInsetsCompat.Type.statusBars())
        val hasNotchInScreen = hasNotchInScreen(activity)
        if (hasNotchInScreen) {
            adapterBang(true)
            if (isAdapterBang)
                findStatusBar()?.visibility = GONE
            else {
                setBackground(ColorDrawable(Color.BLACK))
            }
        } else {
            adapterBang(isAdapterBang)
            findStatusBar()?.visibility = GONE
        }
        activity.updateLayout()
    }

    override fun show() {
        insetsController?.show(WindowInsetsCompat.Type.statusBars())
        findStatusBar()?.visibility = VISIBLE
        activity.updateLayout()
    }

    /**
     * 入侵
     */
    override fun invasion() {
        isInvasion = true
        activity.updateLayout(0)
    }

    override fun isInvasion(): Boolean {
        return isInvasion
    }

    override fun unInvasion() {
        isInvasion = false
        activity.updateLayout()
    }
}
