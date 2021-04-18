package com.erolc.exbar


import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
import android.view.View.SYSTEM_UI_FLAG_VISIBLE
import android.view.Window
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.core.graphics.ColorUtils
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer


/**
 *
 * @author erolc 28/10/2020
 * 这是一个设置状态栏的实现类
 *
 */

internal class StatusBarImpl(private val activity: Activity) : StatusBar {
    private val STATUS_BAR = "statusBar"
    private val STATUS_BAR_BG = "statusBarBg"

    private var statusBar: View?

    init {
        /**
         * 得到内容中状态栏部分view，由于这个view是我自己设置的，所以不一定存在，
         */
        initBg()
        statusBar = activity.contentView.findViewWithTag(STATUS_BAR)
            ?: activity.getStatusBarView()//通过一开始就使用自定义状态栏解决在运行时第一次使用的时候会出现布局底部留空
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

    internal val Activity.defStatusBarColor: Int
        get() =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                resources.getColor(R.color.colorPrimaryDark, theme)
            else
                resources.getColor(R.color.colorPrimaryDark)


    /**
     * 该背景解决显示系统状态栏的时候会出现白底的现象
     * 以下四个方法都是
     */
    private fun initBg() {
        val view = View(activity)
        view.tag = STATUS_BAR_BG
        view.setBackgroundColor(activity.defStatusBarColor)
        activity.contentView.addView(view)
        val layoutParams = view.layoutParams as FrameLayout.LayoutParams
        layoutParams.height = getHeight()
        layoutParams.topMargin = -getHeight()
        view.layoutParams = layoutParams
    }

    private fun hideStatusBg() {
        val view = activity.contentView.findViewWithTag<View>(STATUS_BAR_BG)
        view.visibility = View.GONE
    }

    private fun showStatusBg() {
        val view = activity.contentView.findViewWithTag<View>(STATUS_BAR_BG)
        view.visibility = View.VISIBLE
    }

    private fun statusBgColor(color: Int) {
        val view = activity.contentView.findViewWithTag<View>(STATUS_BAR_BG)
        view.setBackgroundColor(color)
    }


    /**
     * 得到内容部分view
     */
    private val Activity.contentView
        get() = window.decorView.findViewById<FrameLayout>(
            Window.ID_ANDROID_CONTENT
        )

    /**
     * 默认的状态栏颜色
     */


    private fun isLightColor(color: Int): Boolean {
        return ColorUtils.calculateLuminance(color) >= 0.5
    }

    /**
     * 自定义StatusBar
     * 该方法被调用的时候就已经替换了系统状态栏
     */
    private fun Activity.getStatusBarView(): View {
        val findStatusBar = findStatusBar()
        val view = findStatusBar ?: View(this)//获取自定义的状态栏，如果没有则新建
        view.tag = STATUS_BAR//设置tag
        view.setBackgroundColor(defStatusBarColor)
        immersive()//隐藏系统状态栏
        updateLayout()//更新布局

        if (findStatusBar == null) {
            contentView.addView(view)//将自定义状态栏添加到内容布局中，由于内容布局是frameLayout，那么会在顶部
            val layoutParams = view.layoutParams as FrameLayout.LayoutParams
            layoutParams.height = getHeight()//设置自定义状态栏高度
            logi("the contentView.paddingTop is ${contentView.paddingTop}")
            layoutParams.topMargin = -contentView.paddingTop
            view.layoutParams = layoutParams//使用布局参数，使其生效
        }

        return view//这里就得到最终的自定义状态栏
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
            log("the statusBar height is $height")
            height
        }


    /**
     * 状态栏字体颜色是否为暗色
     */
    private val Activity.statusBarTextColorIsDark
        get() = run {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR != 0
            } else {
                loge("SDK_INT must be more than M")
                false
            }
        }

    /**
     * 页面内容是否入侵到状态栏
     */
    private val Activity.statusBarImmersive
        get() = run {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN != 0
            } else {
                loge("SDK_INT must be more than M")
                false
            }
        }

    /**
     * 让系统的状态栏背景消失，计算一些数值
     * 系统标题栏和内容布局无关，所以你需要重新计算，否则内容布局会被标题栏遮盖
     */
    private fun Activity.updateLayout(defaultTop: Int = -1) {
        val findViewById =
            window.decorView.findViewById<View>(R.id.action_mode_bar_stub)//这个view在标题栏存在时，不存在，
        var paddingTop =//计算并得到标题栏存在或者不存在的时候的上内边距
            if (findViewById == null) {//表明标题栏存在
                actionBarHeight + if (isShowStatusBar) getHeight() else 0//当标题栏存在的时候，上内边距就需要包括标题栏的高度，以及自定义的状态栏的高度，当然前提是这个状态栏存在
            } else {
                if (isShowStatusBar) getHeight() else 0//当标题栏不存在的时候，只需要考虑状态栏的高度
            }
        if (defaultTop != -1) {
            paddingTop = defaultTop//如果有默认高度，那么就使用默认高度
        }
        contentView.clipToPadding = false//让子view可以扩展到padding中去
        contentView.setPadding(0, paddingTop, 0, 0)//设置内容的padding
    }

    @Deprecated("")
    private fun FragmentActivity.statusBarCurtain(
        alpha: Int = 0,
        red: Int = 255,
        green: Int = 255,
        blue: Int = 255
    ): MutableLiveData<Int> {
        immersive()
        val data: MutableLiveData<Int> = MutableLiveData()
        data.value = alpha
        data.observe(this, Observer {
            var iAlpha = it
            if (iAlpha > 255) iAlpha = 255 else if (iAlpha < 0) iAlpha = 0
            setBackgroundColor(Color.argb(iAlpha, red, green, blue))
        })
        return data
    }

    /**
     * StatusBar是否展示
     */
    private val Activity.isShowStatusBar get() = window.attributes.flags and WindowManager.LayoutParams.FLAG_FULLSCREEN == 0

    /**
     * 清除自定义StatusBar
     */
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

    private fun isCustomizeStatusBar(): Boolean {
        return findStatusBar() != null//根据tag查看自定义状态栏是否存在
    }


    override fun isShow(): Boolean {
        return activity.isShowStatusBar
    }


    override fun isDark(): Boolean {
        return activity.statusBarTextColorIsDark
    }


    override fun curtain(
        alpha: Int,
        red: Int,
        green: Int,
        blue: Int
    ): MutableLiveData<Int> {
        if (activity is FragmentActivity) {
            return activity.statusBarCurtain(alpha, red, green, blue)
        } else {
            Log.e(
                "curtain",
                "curtain: this activity isn't extends FragmentActivity,can't use the function"
            )
        }
        return MutableLiveData()
    }

    override fun getHeight(): Int {
        val identifier = activity.resources.getIdentifier("status_bar_height", "dimen", "android")
        val i = if (identifier > 0) activity.resources.getDimensionPixelSize(identifier) else 0
        log("statusBarHeight is $i")
        return i
    }

    override fun getBackgroundColor(): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val findStatusBar = findStatusBar()
            if (findStatusBar == null) {//如果statusBar不为空，那么就是使用了自定义的状态栏，那么就不需要系统状态栏的颜色了
                log("get statusBar color with System")
                activity.window.statusBarColor //系统状态栏的背景颜色
            } else {
                log("get statusBar color with customize")
                val background = findStatusBar.background//自定义的状态栏背景颜色
                if (background is ColorDrawable) background.color else -1//如果这个背景不是颜色，（自定义状态栏的背景可以是drawable），则返回-1
            }
        } else {
            log("get statusBar color with customize")
            val background = activity.getStatusBarView().background//自定义的状态栏背景颜色
            if (background is ColorDrawable) background.color else -1//如果这个背景不是颜色，（自定义状态栏的背景可以是drawable），则返回-1
        }
    }

    override fun setBackgroundColor(color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            log("get statusBar color with System")
            statusBgColor(color)
            activity.window.statusBarColor = color //系统状态栏的背景颜色
            val lightColor = isLightColor(color)
            setTextColor(lightColor)//设置自定义状态栏的字体颜色
            clearStatusBar()
            activity.updateLayout()
        } else {
            activity.getStatusBarView().setBackgroundColor(color)
            val lightColor = isLightColor(color)
            setTextColor(lightColor)//设置自定义状态栏的字体颜色
        }
    }

    override fun setBackground(drawable: Int) {
        activity.getStatusBarView().setBackgroundResource(drawable)

    }

    override fun setBackground(drawable: Drawable) {
        activity.getStatusBarView().background = drawable
    }

    override fun getBackground(): Drawable? {
        return findStatusBar()?.background
    }

    override fun setTextColor(isDark: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val systemUiVisibility = activity.window.decorView.systemUiVisibility
            var option =
                if (isDark) SYSTEM_UI_FLAG_LIGHT_STATUS_BAR else SYSTEM_UI_FLAG_VISIBLE
            option = if (option == SYSTEM_UI_FLAG_VISIBLE) {
                if (activity.statusBarImmersive) {
                    option or (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
                } else option
            } else {
                option or systemUiVisibility
            }
            activity.window.decorView.systemUiVisibility = option
        }
    }

    override fun hide(isAdapterBang: Boolean) {
        adapterBang(isAdapterBang)
        activity.window.clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN)
        activity.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        hideStatusBg()
        findStatusBar()?.visibility = View.GONE
        activity.updateLayout()

    }

    override fun show() {
        activity.window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        activity.window.setFlags(
            WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN
        )
        showStatusBg()
        findStatusBar()?.visibility = View.VISIBLE
        activity.updateLayout()
    }

    override fun immersive() {
        log("immersive")
        val window = activity.window
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        activity.updateLayout(0)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            log("get statusBar color with System")
            activity.window.statusBarColor = Color.TRANSPARENT
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
    }
}
