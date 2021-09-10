package com.erolc.exbar

import android.graphics.Rect
import android.view.View
import android.view.Window

/**
 * create by erolc at 2021/9/6 18:11.
 */
fun Window.containSoftInputMode(softMode: Int): Boolean {
    return softInputMode and softMode == softMode
}

var Window.softInputMode
    get() = attributes.softInputMode
    set(value) = setSoftInputMode(value)

fun View.computeVisibleDisplayHeight(): Int {
    val r = Rect()
    getWindowVisibleDisplayFrame(r)
    return r.bottom - r.top
}
