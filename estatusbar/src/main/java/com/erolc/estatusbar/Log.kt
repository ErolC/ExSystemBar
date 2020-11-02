package com.erolc.estatusbar

import android.util.Log

var DEBUG = true

internal inline fun <reified T, R> T.loge(log: R) {
    if (DEBUG)
        Log.e(T::class.java.simpleName, log.toString())
}
internal inline fun <reified T, R> T.log(log: R) {
    if (DEBUG)
        Log.d(T::class.java.simpleName, log.toString())
}

internal inline fun <reified T, R> T.logv(log: R) {
    if (DEBUG)
        Log.v(T::class.java.simpleName, log.toString())
}

internal inline fun <reified T, R> T.logi(log: R) {
    if (DEBUG)
        Log.i(T::class.java.simpleName, log.toString())
}

internal inline fun <reified T, R> T.logw(log: R) {
    if (DEBUG)
        Log.w(T::class.java.simpleName, log.toString())
}

internal inline fun <reified T, R> T.logwtf(log: R) {
    if (DEBUG)
        Log.wtf(T::class.java.simpleName, log.toString())
}