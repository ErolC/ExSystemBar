package com.erolc.estatusbar

import android.app.Activity
import androidx.fragment.app.Fragment

/**
 * StatusBar入口，如果是activity，建议在[Activity.onResume]回调中使用
 * 如果是fragment，建议在[Fragment.setUserVisibleHint]中使用
 */
class ExStatusBar(activity: Activity) : StatusBar by activity.getStatusBar() {
    constructor(fragment: Fragment) : this(fragment.requireActivity())
}

