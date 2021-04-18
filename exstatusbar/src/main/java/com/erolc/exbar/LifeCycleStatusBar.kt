package com.erolc.exbar

import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
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
    owner: LifecycleOwner,
    private val statusBar: StatusBar
) : StatusBar by statusBar {

    private var color: Int? = null
    private var drawableRes: Int? = null
    private var curtainColor: Int? = null
    private var drawable: Drawable? = null
    private var textColor: Boolean? = null
    private var isHide: Int = 3
    private var immersive: Boolean = false
    private var isSetUserVisibleHint: Boolean? = null
    private val code = owner.hashCode()
    val simpleName = owner.javaClass.simpleName
    private var darkColor:Int? = null

    //创建fragment的StatusBar是需要他所依附的activity的StatusBar
    private val exeStatusBar =
        if (statusBar is LifeCycleStatusBar) statusBar.statusBar else statusBar

    init {
        owner.lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                if (event == Lifecycle.Event.ON_RESUME) {
                    loge("$simpleName == $code")
//                    if (source is Fragment && source.userVisibleHint || source is FragmentActivity) {
                    restore()
//                    }

//                    if (isDark(if (owner is FragmentActivity) owner else (owner as Fragment).requireContext())) {
//                        if (immersive && isHide != 3) {//如果状态栏是沉浸式或隐藏的则无需要设置夜间模式
//                            return
//                        }
//                        if (darkColor == null) {
//                            exeStatusBar.setBackgroundColor(Color.BLACK)
//                        } else {
//                            if (darkColor != Color.WHITE) {
//                                exeStatusBar.setBackgroundColor(darkColor!!)
//                            }
//                        }
//                    }
                } else if (event == Lifecycle.Event.ON_DESTROY) {
                    StatusBarFactory.clear(source.hashCode())
                }
            }
        })
    }

    //是否是夜间模式
    private fun isDark(context: Context): Boolean {
        val uiMode = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return uiMode == Configuration.UI_MODE_NIGHT_YES
    }

    /**
     * 兼容旧版本的fragment的懒加载方式
     */
    private fun canSet(body: () -> Unit) {
        //isSetUserVisibleHint 为空，则是新版本，不为空且为true，则是旧版本懒加载且当前fragment已经显示
        if (isSetUserVisibleHint == null || isSetUserVisibleHint!!) {
            body()
        }
    }

    override fun setBackgroundColor(color: Int) {
        this.color = color
        loge("setColor $simpleName $color")

        updateBgStyle(1)
        canSet {
            exeStatusBar.setBackgroundColor(color)
        }
    }


    override fun setBackground(drawable: Int) {
        drawableRes = drawable
        updateBgStyle(4)
        canSet {
            exeStatusBar.setBackground(drawable)
        }
    }

    override fun setBackground(drawable: Drawable) {
        this.drawable = drawable
        updateBgStyle(3)
        canSet {
            exeStatusBar.setBackground(drawable)
        }
    }


    override fun setTextColor(isDark: Boolean) {
        textColor = isDark
        canSet {
            exeStatusBar.setTextColor(isDark)
        }
    }

    override fun hide(isAdapterBang: Boolean) {
        isHide = if (isAdapterBang) 1 else 2
        immersive = false
        canSet {
            exeStatusBar.hide(isAdapterBang)
        }
    }

    override fun show() {
        isHide = 3
        immersive = false
        canSet {
            exeStatusBar.show()
        }
    }

    override fun immersive() {
        immersive = true
        isHide = 0
        canSet {
            exeStatusBar.immersive()
        }
    }

    /**
     * 更新背景资源，除了刚设置的资源，其他的都需要清除
     */
    private fun updateBgStyle(type: Int) {
        color = if (type == 1) color else null
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
            Log.e("TAG", "$code  restore: $color")
            color?.let { exeStatusBar.setBackgroundColor(it) }
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
    //适配懒加载
    fun setUserVisibleHint(visibleHint: Boolean) {
        isSetUserVisibleHint = visibleHint
        if (visibleHint) {
            restore()
        }
    }


//    fun setDarkModeColor(color:Int?){
//        darkColor = color
//    }


}