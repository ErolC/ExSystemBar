package com.erolc.exbar

import android.app.Activity
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsAnimationCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentActivity


@RequiresApi(Build.VERSION_CODES.M)
fun Activity.getWindowInsets() = window.decorView.rootWindowInsets

internal val Activity.contentView get() = window.decorView.findViewById<FrameLayout>(Window.ID_ANDROID_CONTENT)

fun Activity.getWindowInsetsController() = WindowCompat.getInsetsController(window, contentView)


fun View.applyWindowInsetsListener(body: ((View, WindowInsetsCompat) -> Unit)? = null) {
    ViewCompat.setOnApplyWindowInsetsListener(this) { v, insets ->
        body?.invoke(v, insets)
        insets
    }
}


fun View.onWindowInsetsAnimationCallback(callBack: ViewInsetsAnimCall) {
    callBack.view = this
    ViewCompat.setWindowInsetsAnimationCallback(this, callBack)
}

abstract class ViewInsetsAnimCall(@DispatchMode dispatchMode: Int) :
    WindowInsetsAnimationCompat.Callback(dispatchMode) {
    internal var view: View? = null
    fun getView() = view!!
}


internal fun navigationBar() = WindowInsetsCompat.Type.navigationBars()


internal fun statusBar() = WindowInsetsCompat.Type.statusBars()


internal fun captionBar() = WindowInsetsCompat.Type.captionBar()


internal fun ime() = WindowInsetsCompat.Type.ime()


internal fun systemBar() = WindowInsetsCompat.Type.systemBars()
