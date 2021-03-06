package com.erolc.exbar.bar

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.Gravity
import android.view.View
import android.view.Window
import android.widget.FrameLayout
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.core.graphics.Insets
import androidx.core.view.*
import com.erolc.exbar.getWindowInsetsController
import com.erolc.exbar.systemBar.SystemBarImpl
import java.lang.ref.SoftReference

/**
 * create by erolc at 2021/9/3 17:03.
 */
class NavigationBarImpl(
    activity: ComponentActivity
) : Bar {
    private val NAV_BAR = "navBar"
    private val NAV_BAR_BG = "navBarBg"
    private var navBar: View? = null
    private var _height: Int? = null
    val controller by lazy { activity.getWindowInsetsController() }
    private val softReference = SoftReference(activity)
    private var systemBar: SystemBarImpl? = null
    private var lastOrientation = activity.resources.configuration.orientation
    private var inset: Insets? = null
    private var isInvasion = false

    init {
        navBar = activity.contentView.findViewWithTag(NAV_BAR)
            ?: activity.getNavBarView()//通过一开始就使用自定义状态栏解决在运行时第一次使用的时候会出现布局底部留空
        setBackgroundColor(activity.defNavBarColor)
        lastOrientation = activity.resources.configuration.orientation
    }

    internal fun setSystemBar(systemBar: SystemBarImpl) {
        this.systemBar = systemBar
        navBar = softReference.get()?.getNavBarView()
    }

    internal fun updateBar(inset: Insets) {
        this.inset = inset
        softReference.get()?.updateLayout()
        updateNav()
    }

    /**
     * 自定义StatusBar
     * 该方法被调用的时候就已经替换了系统状态栏
     */
    private fun Activity.getNavBarView(): View {
        val findNavBar = findNavBar()
        val view = findNavBar ?: View(this)//获取自定义的状态栏，如果没有则新建
        view.tag = NAV_BAR//设置tag
        updateLayout()//更新布局
        if (findNavBar == null) {
            contentView.addView(view)//将自定义状态栏添加到内容布局中，由于内容布局是frameLayout，那么会在顶部
        }
        updateNav()
        return view//这里就得到最终的自定义状态栏
    }

    private fun updateNav() {
        findNavBar()?.updateLayoutParams<FrameLayout.LayoutParams> {
            inset?.apply {
                if (bottom == right && right == 0) {
                    height = 0
                    width = 0
                    return@apply
                }
                height = if (bottom == 0) FrameLayout.LayoutParams.WRAP_CONTENT else {
                    gravity = Gravity.BOTTOM
                    bottomMargin = -bottom
                    bottom
                }
                width = if (right == 0) FrameLayout.LayoutParams.MATCH_PARENT else {
                    gravity = Gravity.END
                    topMargin = -StatusBarImpl.getHeight(softReference.get())
                    rightMargin = -right
                    right
                }
            }
        }
    }

    /**
     * StatusBar是否展示
     */
    private val isShowNavBar
        get() = findNavBar()?.visibility == View.VISIBLE

    internal fun getLayoutValue(): Pair<Int, Int> {
        if (isInvasion) {
            return 0 to 0
        }
        return if (isShowNavBar) {
            (inset?.right ?: 0) to (inset?.bottom ?: 0)
        } else 0 to 0
    }

    /**
     * 让系统的状态栏背景消失，计算一些数值
     * 系统标题栏和内容布局无关，所以你需要重新计算，否则内容布局会被标题栏遮盖
     */
    private fun Activity.updateLayout(defaultBottom: Int = -1) {
        systemBar?.updateLayout(this, defaultBottom, 1)
    }

    /**
     * 在状态栏位置寻找状态栏，和getStatusBarView 不一样的是，getStatusBarView在状态栏还是系统状态栏的时候，也会先替换，再返回
     * 而这个方法是寻找状态栏，如果没有自定义，那么就返回null
     */
    private fun findNavBar(): View? {
        return navBar ?: softReference.get()?.contentView?.findViewWithTag(NAV_BAR)
    }

    /**
     * 默认的状态栏颜色
     */
    private val Activity.defNavBarColor: Int
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.navigationBarColor
        } else {
            Color.BLACK
        }

    /**
     * 得到内容部分view
     */
    private val Activity.contentView get() = window.decorView.findViewById<FrameLayout>(Window.ID_ANDROID_CONTENT)

    /**
     * 检查颜色是否为亮色
     */
    private fun isLightColor(color: Int) = ColorUtils.calculateLuminance(color) >= 0.5

    private val defHeight: Int
        get() {
            val identifier =
                softReference.get()?.resources?.getIdentifier(
                    "navigation_bar_height",
                    "dimen",
                    "android"
                ) ?: 0
            return if (identifier > 0) softReference.get()?.resources?.getDimensionPixelSize(
                identifier
            ) ?: 0 else 0
        }


    override fun getHeight(): Int {
        return _height ?: defHeight
    }

    override fun getInherentHeight(): Int {
        return defHeight
    }

    override fun getBackgroundColor(): Int {
        val background = softReference.get()?.getNavBarView()?.background//自定义的状态栏背景颜色
        return if (background is ColorDrawable) background.color else -1//如果这个背景不是颜色，（自定义状态栏的背景可以是drawable），则返回-1
    }

    override fun setBackgroundColor(color: Int) = setBackground(ColorDrawable(color))
    override fun setBackground(drawable: Int) {
        softReference.get()?.let {
            setBackground(ContextCompat.getDrawable(it, drawable))
        }
    }

    override fun setBackground(drawable: Drawable?) {
        softReference.get()?.getNavBarView()?.background = drawable
        if (drawable is ColorDrawable) {
            val lightColor = isLightColor(drawable.color)
            setContentColor(lightColor)//设置自定义状态栏的字体颜色
        }
    }

    override fun getBackground(): Drawable? {
        return findNavBar()?.background
    }

    override fun getDefaultBackgroundColor(): Int {
        return softReference.get()?.defNavBarColor ?: Color.TRANSPARENT
    }

    override fun setContentColor(isDark: Boolean) {
        controller?.isAppearanceLightNavigationBars = isDark
    }

    override fun hide(isAdapterBang: Boolean) {
//        controller?.systemBarsBehavior =
//            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        controller?.hide(WindowInsetsCompat.Type.navigationBars())
        findNavBar()?.visibility = View.GONE
        softReference.get()?.updateLayout()
    }

    override fun show() {
        controller?.show(WindowInsetsCompat.Type.navigationBars())
        findNavBar()?.visibility = View.VISIBLE
        softReference.get()?.updateLayout()
    }

    override fun invasion() {
        isInvasion = true
        softReference.get()?.updateLayout(0)
    }

    override fun isInvasion(): Boolean {
        return isInvasion
    }

    override fun unInvasion() {
        isInvasion = false
        softReference.get()?.updateLayout()
    }

    override fun isShow(): Boolean {
        return isShowNavBar
    }

    override fun getContentIsDark(): Boolean {
        return controller?.isAppearanceLightNavigationBars ?: false
    }
}


