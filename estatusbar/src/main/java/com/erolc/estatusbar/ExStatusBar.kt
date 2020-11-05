package com.erolc.estatusbar

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

/**
 * StatusBar入口，注意：所属同一个Activity的所有fragment共用一个StatsBar对象。注意切换fragment时StatusBar样式的恢复
 */
class ExStatusBar private constructor() {
    companion object {

        @JvmStatic
        fun create(activity: FragmentActivity): StatusBar {
            return activity.getStatusBar()
        }

        @JvmStatic
        fun create(fragment: Fragment): StatusBar {
            return fragment.getFragmentStatusBar()
        }

        /**
         * 当在Viewpager+Fragment的场景下，并且[Fragment.setUserVisibleHint]并未废弃的时候，使用该方法创建
         * 该方法创建的statusBar对象，需要开发者自己维护。
         */
        @JvmStatic
        fun compatCreate(fragment: Fragment): StatusBar {
            return fragment.getStatusBar()
        }
    }
}

