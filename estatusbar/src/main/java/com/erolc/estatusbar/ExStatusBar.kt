package com.erolc.estatusbar

import android.app.Activity
import androidx.fragment.app.Fragment

/**
 * StatusBar入口
 */
class ExStatusBar(activity: Activity) : StatusBar by activity.getStatusBar() {
    constructor(fragment: Fragment) : this(fragment.requireActivity())
}

