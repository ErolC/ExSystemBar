package com.erolc.exbar

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.erolc.exbar.bar.Bar
import com.erolc.exbar.systemBar.SystemBar
import com.erolc.exbar.systemBar.SystemBar.Companion.NAVIGATION_BAR
import com.erolc.exbar.systemBar.SystemBar.Companion.STATUS_BAR

/**
 * 创建statusBar的几种方式
 */
fun FragmentActivity.getSystemBar(body: (SystemBar.() -> Unit)? = null): SystemBar {
    val statusBar = ExSystemBar.create(this)
    body?.invoke(statusBar)
    return statusBar
}

fun FragmentActivity.getStatusBar(body: (Bar.() -> Unit)? = null): Bar {
    val statusBar = ExSystemBar.createStatusBar(this)
    body?.invoke(statusBar)
    return statusBar
}

fun FragmentActivity.getNavigationBar(body: (Bar.() -> Unit)? = null): Bar {
    val navBar = ExSystemBar.createNavigationBar(this)
    body?.invoke(navBar)
    return navBar
}


fun Fragment.getSystemBar(body: (SystemBar.() -> Unit)? = null): SystemBar {
    val statusBar = ExSystemBar.create(this)
    body?.invoke(statusBar)
    return statusBar
}

fun Fragment.getStatusBar(body: (Bar.() -> Unit)? = null): Bar {
    val statusBar = ExSystemBar.createStatusBar(this)
    body?.invoke(statusBar)
    return statusBar
}

fun Fragment.getNavigationBar(body: (Bar.() -> Unit)? = null): Bar {
    val navBar = ExSystemBar.createNavigationBar(this)
    body?.invoke(navBar)
    return navBar
}

/**
 * 获取StatusBar的委托，在kotlin中通过by的方式获取StatusBar
 * 例如：
 * private val statusBar: StatusBar by statusBar {
 *
 * }
 *
 * @param event 代表[body] 的调用时机，默认是onCreate
 * @param body 是statusBar在创建的时候就会调用的方法，只会调用一次，可以由[event]指定调用时机
 */
fun Fragment.systemBar(
    body: SystemBar.() -> Unit
): SystemBarDelegate {
    return SystemBarDelegate(this, body)
}

/**
 * 同上[systemBar]
 */
fun FragmentActivity.systemBar(
    body: SystemBar.() -> Unit
): SystemBarDelegate {
    return SystemBarDelegate(this, body)
}


fun Fragment.statusBar(
    body: Bar.() -> Unit
): BarDelegate {
    return BarDelegate(this, STATUS_BAR, body)
}


/**
 * 同上[systemBar]

 */
fun FragmentActivity.statusBar(
    body: Bar.() -> Unit
): BarDelegate {
    return BarDelegate(this, STATUS_BAR, body)
}

/**
 * 同上[systemBar]
 */
fun FragmentActivity.navigationBar(
    body: Bar.() -> Unit
): BarDelegate {
    return BarDelegate(this, NAVIGATION_BAR, body)
}


fun Fragment.navigationBar(
    body: Bar.() -> Unit
): BarDelegate {
    return BarDelegate(this, NAVIGATION_BAR, body)
}

/**
 * 状态栏委托对象
 */
class SystemBarDelegate(
    private val owner: LifecycleOwner,
    private val body: (SystemBar.() -> Unit)?
) :
    Lazy<SystemBar> {
    private var systemBar: SystemBar? = null
    override val value: SystemBar
        get() {
            var temp = systemBar
            if (temp == null) {
                temp = ExSystemBar.create(owner)
                body?.invoke(temp)
                systemBar = temp
            }
            return systemBar!!
        }

    init {
        owner.lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                if (event == Lifecycle.Event.ON_RESUME) {
                    value.getHeight()
                }
            }
        })
    }

    override fun isInitialized(): Boolean {
        return systemBar != null
    }

}

class BarDelegate(
    private val owner: LifecycleOwner,
    private val type: Int,
    private val body: (Bar.() -> Unit)?
) :
    Lazy<Bar> {
    private var statusBar: Bar? = null
    override val value: Bar
        get() {
            var temp = statusBar
            if (temp == null) {
                temp = ExSystemBar.create(owner).getBar(type)
                body?.invoke(temp)
                statusBar = temp
            }
            return statusBar!!
        }

    init {
        owner.lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                if (event == Lifecycle.Event.ON_RESUME) {
                    value.isShow()
                }
            }
        })
    }

    override fun isInitialized(): Boolean {
        return statusBar != null
    }

}