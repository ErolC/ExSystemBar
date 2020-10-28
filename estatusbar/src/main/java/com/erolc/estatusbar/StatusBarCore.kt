package com.erolc.estatusbar

import android.app.Activity
import androidx.fragment.app.Fragment


fun Activity.statusBar(body: StatusBar.() -> Unit): StatusBar {
    val statusBar = StatusBarImpl(this)
    statusBar.body()
    return statusBar
}

fun Activity.statusBar(): StatusBar {
    return StatusBarImpl(this)
}

fun Fragment.statusBar(): StatusBar {
    return StatusBarImpl(this)
}

fun Fragment.statusBar(body: StatusBar.() -> Unit): StatusBar {
    val statusBar = StatusBarImpl(this)
    statusBar.body()
    return statusBar
}

