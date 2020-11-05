package com.erolc.estatusbar

import android.app.Activity
import android.os.Build
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner


typealias StatusBarRestore = () -> StatusBar

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

/**
 * 注意，该方法适配新版本的fragment，['androidx.appcompat:appcompat:1.2.0']以上，新的fragment会废弃掉[Fragment.setUserVisibleHint]
 * 而使用新的方式进行懒加载，所以如果不是新版本的fragment不建议使用该方法。建议使用[restoreStatusBar]
 * 对于fragment来说，是和同一个activity上的所有fragment共享一个statusBar，所以在每次回到fragment的时候都需要对statusBar进行更新，防止前一个显示fragment
 * 的脏statusBar数据对该页面的影响。目前只有在该方法中设置的样式才会保留。后面会加入另外的一些设置，让在外部的设置也可以保留
 */
fun Fragment.statusBar(body: StatusBar.() -> Unit): StatusBar {
    val statusBar = getFragmentStatusBar()
    statusBar.body()
    return statusBar
}

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
    val statusBar = getStatusBar()
    return {
        statusBar.body()
        statusBar
    }
}

internal fun FragmentActivity.getStatusBar(): StatusBar {
    return StatusBarFactory.create(this)
}


internal fun Fragment.getStatusBar(): StatusBar {
    return StatusBarFactory.create(requireActivity())
}

internal fun Fragment.getFragmentStatusBar(): StatusBar {
    return StatusBarFactory.create(this)
}

internal val Activity.defStatusBarColor: Int
    get() =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            resources.getColor(R.color.colorPrimaryDark, theme)
        else
            resources.getColor(R.color.colorPrimaryDark)
