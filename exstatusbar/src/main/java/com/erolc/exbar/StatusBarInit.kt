package com.erolc.exbar

import android.graphics.Color
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner

/**
 * 创建statusBar的几种方式
 */
fun FragmentActivity.getStatusBar(body: StatusBar.() -> Unit): StatusBar {
    val statusBar = getStatusBar()
    statusBar.body()
    return statusBar
}

/**
 * 适配旧版本的fragment懒加载
 */
fun Fragment.setUserVisibleHint() {
    ExStatusBar.setUserVisibleHint(this)
}

/**
 * 获取StatusBar
 */
fun FragmentActivity.getStatusBar(): StatusBar {
    return ExStatusBar.create(this)
}

/**
 * 获取StatusBar
 */
fun Fragment.getStatusBar(): StatusBar {
    return ExStatusBar.create(this)
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
fun Fragment.statusBar(
    body: StatusBar.() -> Unit
): StatusBarDelegate {
    return StatusBarDelegate(this, body)
}

/**
 * 同上[statusBar]
 * @param event 已经没有作用了，仅为了兼容以前
 */
fun FragmentActivity.statusBar(
    event:Lifecycle.Event = Lifecycle.Event.ON_RESUME,
    body: StatusBar.() -> Unit
): StatusBarDelegate {
    return StatusBarDelegate(this, body)
}

/**
 * 状态栏委托对象
 */
class StatusBarDelegate(
    private val owner: LifecycleOwner,
    private val body: StatusBar.() -> Unit
) :
    Lazy<StatusBar> {
    private var statusBar: StatusBar? = null
    override val value: StatusBar
        get() {
            var temp = statusBar
            if (temp == null) {
                temp = ExStatusBar.create(owner)
                temp.body()
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