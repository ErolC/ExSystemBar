package com.erolc.exbar.bar

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.erolc.exbar.SystemBarFactory
import com.erolc.exbar.systemBar.SystemBarImpl

/**
 * 生命周期感知的StatusBar，该对象，每个activity和fragment都会有各自的一个，使得各个页面之间的设置都相互隔离，并且可以在页面销毁的时候，销毁相应的对象。
 *
 * 注意：当在fragment+ViewPager模式下使用的时候，确保是新版本的fragment，['androidx.appcompat:appcompat:1.2.0']以上,如果[Fragment.setUserVisibleHint]已经废弃，则是新的fragment
 * 否则会在切换页面的时候statusBar表现出不一样的效果。
 *
 */
class LifeCycleBar(
    owner: LifecycleOwner,
    val context: Context,
    private val bar: Bar
) : Bar by bar {
    private val TAG = "ExStatusBar"

    private var drawable: Drawable? = null
    private var contentColorIsDark: Boolean? = null
    private var isHide: Int = 3
    private var immersive: Boolean = false
    private var isSetUserVisibleHint: Boolean? = null

    private val simpleName = owner.javaClass.simpleName

    //创建fragment的StatusBar是需要他所依附的activity的StatusBar
    internal val exeBar = if (bar is LifeCycleBar) bar.bar else bar

    init {
        exeBar.recovery()
        owner.lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                if (event == Lifecycle.Event.ON_RESUME) {
                    restore()
                } else if (event == Lifecycle.Event.ON_PAUSE) {
                    exeBar.recovery()
                } else if (event == Lifecycle.Event.ON_DESTROY) {
                    SystemBarFactory.clear(source.hashCode())
                }
            }
        })
    }

    internal fun setSystemBar(systemBar: SystemBarImpl) {
        if (exeBar is NavigationBarImpl) {
            exeBar.setSystemBar(systemBar)
        } else if (exeBar is StatusBarImpl) {
            exeBar.setSystemBar(systemBar)
        }
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
        drawable = ColorDrawable(color)
        canSet {
            restore()
        }
    }


    override fun setBackground(drawable: Int) {
        this.drawable = ContextCompat.getDrawable(context, drawable)
        canSet {
            restore()
        }
    }

    override fun setBackground(drawable: Drawable?) {
        this.drawable = drawable
        canSet {
            restore()
        }
    }


    override fun setContentColor(isDark: Boolean) {
        contentColorIsDark = isDark
        canSet {
            restore()
        }
    }

    override fun hide(isAdapterBang: Boolean) {
        isHide = if (isAdapterBang) 1 else 2
        immersive = false
        canSet {
            restore()
        }
    }

    override fun show() {
        isHide = 3
        immersive = false
        canSet {
            restore()
        }
    }

    override fun invasion() {
        immersive = true
        isHide = 0
        canSet {
            restore()
        }
    }

    /**
     * 对资源进行恢复
     */
    private fun restore() {
        Log.d(TAG, "$simpleName  ${exeBar.javaClass.simpleName} restore:")
        if (isHide == 3) {
            exeBar.show()
            drawable?.let {
                exeBar.setBackground(it)
                Log.d(TAG, "restore setBackground: $it")
            }
            contentColorIsDark?.let {
                exeBar.setContentColor(it)
                Log.d(TAG, "restore setTextColor: $it")
            }
        } else if (!immersive) {
            Log.d(TAG, "restore hide:${isHide == 1}")

            exeBar.hide(isHide == 1)
        }

        if (immersive) {
            Log.d(TAG, "restore immersive")
            exeBar.invasion()
            contentColorIsDark?.let { exeBar.setContentColor(it) }
        }
    }

    //适配懒加载
    fun setUserVisibleHint(visibleHint: Boolean) {
        isSetUserVisibleHint = visibleHint
        if (visibleHint) {
            restore()
        }
    }


}