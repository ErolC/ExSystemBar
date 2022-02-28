package com.erolc.exbar

import android.app.Activity
import android.graphics.Rect
import android.os.Build
import android.util.Log
import android.view.DisplayCutout
import android.view.View
import android.view.Window
import androidx.core.view.WindowInsetsCompat
import java.lang.Exception
import java.lang.reflect.Method

/**
 * create by erolc at 2021/9/6 18:11.
 */
fun Window.containSoftInputMode(softMode: Int): Boolean {
    return softInputMode and softMode == softMode
}

var Window.softInputMode
    get() = attributes.softInputMode
    set(value) = setSoftInputMode(value)
