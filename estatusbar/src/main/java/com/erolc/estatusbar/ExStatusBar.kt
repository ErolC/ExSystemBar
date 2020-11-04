package com.erolc.estatusbar

import android.app.Activity
import androidx.fragment.app.Fragment

/**
 * StatusBar入口，注意：所属同一个Activity的所有fragment共用一个StatsBar对象。注意切换fragment时StatusBar样式的恢复
 */
class ExStatusBar(activity: Activity) : StatusBar by activity.getStatusBar() {
    constructor(fragment: Fragment) : this(fragment.requireActivity())
}

