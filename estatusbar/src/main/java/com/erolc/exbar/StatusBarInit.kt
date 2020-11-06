package com.erolc.exbar

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner



/**
 * 使用这种方式，可以在任何地方使用，因为内部保证了都是会在onResume回调中调用
 */
fun FragmentActivity.statusBar(body: StatusBar.() -> Unit): StatusBar {
    val statusBar = getStatusBar()
    lifecycle.addObserver(object : LifecycleEventObserver {
        override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
            if (event == Lifecycle.Event.ON_RESUME) {
                statusBar.body()
            }
        }
    })
    return statusBar
}

fun Fragment.statusBar(body: StatusBar.() -> Unit): StatusBar {
    val statusBar = getStatusBar()
    statusBar.body()
    return statusBar
}



typealias StatusBarRestore = () -> StatusBar

/**
 *  适配旧版本的fragment，以达到自动保持当前状态栏样式
 *
 *  使用方法和[statusBar]类似，不同的是，该方法会得到一个[StatusBarRestore]
 *  需要在[Fragment.setUserVisibleHint]回调中调用一下其invoke方法即可
 *  建议：将fragment版本升到['androidx.appcompat:appcompat:1.2.0']以上，以使用[Fragment.statusBar]方法
 *
 *  该方法后面可能会废弃
 */
fun Fragment.compatStatusBar(body: StatusBar.() -> Unit): StatusBarRestore {
    val statusBar = getCompatStatusBar()
    return {
        statusBar.body()
        statusBar
    }
}

fun FragmentActivity.getStatusBar(): StatusBar {
    return ExStatusBar.create(this)
}


fun Fragment.getCompatStatusBar(): StatusBar {
    return ExStatusBar.compatCreate(this)
}

fun Fragment.getStatusBar(): StatusBar {
    return ExStatusBar.create(this)
}