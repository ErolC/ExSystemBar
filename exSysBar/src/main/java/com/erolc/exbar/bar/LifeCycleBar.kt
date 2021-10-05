package com.erolc.exbar.bar

import android.app.Activity
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.erolc.exbar.SystemBarFactory
import com.erolc.exbar.loge
import com.erolc.exbar.systemBar.SystemBarImpl

/**
 * 这是所有界面的Bar的对象，无论是Activity还是Fragment
 */
class LifeCycleBar(
    owner: LifecycleOwner,
    val context: Context,
    private val bar: Bar
) : Bar by bar {
    private val TAG = "ExStatusBar"

    private var drawable: Drawable? = ColorDrawable(bar.getDefaultBackgroundColor())
    private var contentColorIsDark: Boolean? = bar.getContentIsDark()
    private var isHide: Int = 3
    private var invasion = false
    private var isSetUserVisibleHint: Boolean? = null

    private val simpleName = owner.javaClass.simpleName
    private var isChange = false

    //创建fragment的StatusBar是需要他所依附的activity的StatusBar
    internal val exeBar = if (bar is LifeCycleBar) bar.bar else bar

    init {
        owner.lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                if (event == Lifecycle.Event.ON_RESUME) {
                    if (owner is Activity && isChange || owner is Fragment) {
                        //如果activity做出了改变，那么会先恢复fragment的bar再恢复activity的Bar，会先恢复statusBar再恢复NavigationBar
                        restore()
                    }
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
        //是否发生更改，对于Fragment来说，会被Activity的操作所覆盖，所以在Activity没有做出任何操作之前，不需要恢复
        isChange = true
        //isSetUserVisibleHint 为空，则是新版本，不为空且为true，则是旧版本懒加载且当前fragment已经显示
        if (isSetUserVisibleHint == null || isSetUserVisibleHint!!) {
            body()
        }
    }

    override fun setBackgroundColor(color: Int) {
        drawable = ColorDrawable(color)
        canSet {
            exeBar.setBackgroundColor(color)
        }
    }


    override fun setBackground(drawable: Int) {
        this.drawable = ContextCompat.getDrawable(context, drawable)
        canSet {
            exeBar.setBackground(drawable)
        }
    }

    override fun setBackground(drawable: Drawable?) {
        this.drawable = drawable
        canSet {
            exeBar.setBackground(drawable)
        }
    }


    override fun setContentColor(isDark: Boolean) {
        contentColorIsDark = isDark
        canSet {
            exeBar.setContentColor(isDark)
        }
    }

    override fun hide(isAdapterBang: Boolean) {
        isHide = if (isAdapterBang) 1 else 2
        canSet {
            exeBar.hide(isAdapterBang)
        }
    }

    override fun show() {
        isHide = 3
        canSet {
            exeBar.show()
        }
    }

    override fun invasion() {
        isHide = 0
        invasion = true
        canSet {
            exeBar.invasion()
        }
    }

    override fun unInvasion() {
        isHide = 0
        invasion = false
        canSet {
            exeBar.unInvasion()
        }
    }

    /**
     * 对资源进行恢复
     */
    internal fun restore() {
        Log.d(
            TAG,
            "$simpleName  ${exeBar.javaClass.simpleName} restore: $invasion "
        )
        if (isHide != 0) {
            if (isHide == 3) {
                exeBar.show()
                drawable?.let {
                    exeBar.setBackground(it)
                    Log.d(
                        TAG,
                        " ${exeBar.javaClass.simpleName} restore setBackground: $it "
                    )
                }
                contentColorIsDark?.let {
                    exeBar.setContentColor(it)
                    Log.d(TAG, "restore setTextColor: $it")
                }
            } else {
                Log.d(TAG, "restore hide:${isHide == 1}")
                exeBar.hide(isHide == 1)
            }
        } else {
            if (invasion) {
                Log.d(TAG, "restore immersive")
                exeBar.invasion()
                contentColorIsDark?.let { exeBar.setContentColor(it) }
            } else {
                Log.d(TAG, "restore unimmersive----------")
                exeBar.unInvasion()
                drawable?.let {
                    exeBar.setBackground(it)
                }
                contentColorIsDark?.let { exeBar.setContentColor(it) }
            }
        }
    }

    override fun onConfigurationChanged() {
        restore()
    }

    //适配懒加载
    fun setUserVisibleHint(visibleHint: Boolean) {
        isSetUserVisibleHint = visibleHint
        if (visibleHint) {
            restore()
        }
    }


}