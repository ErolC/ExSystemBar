package com.erolc.estatusbar

import android.app.Activity
import androidx.fragment.app.Fragment


fun Activity.statusBar(body: StatusBar.() -> Unit): StatusBar {
    val statusBar = getStatusBar()
    statusBar.body()
    return statusBar
}

fun Fragment.statusBar(body: StatusBar.() -> Unit): StatusBar {
    val statusBar = getStatusBar()
    statusBar.body()
    return statusBar
}


fun Activity.getStatusBar(): StatusBar {
    return StatusBarImpl(this)
}

fun Fragment.getStatusBar(): StatusBar {
    return StatusBarImpl(this)
}

