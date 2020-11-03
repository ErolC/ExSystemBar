package com.erolc.estatusbar

import android.app.Activity
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
    lifecycle.addObserver(object : LifecycleEventObserver {
        override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
            if (event == Lifecycle.Event.ON_RESUME) {
                statusBar.body()
            }
        }
    })
    return statusBar
}


fun Activity.getStatusBar(): StatusBar {
    return StatusBarFactory.create(this)
}

fun Fragment.getStatusBar(): StatusBar {
    return StatusBarFactory.create(requireActivity())
}

