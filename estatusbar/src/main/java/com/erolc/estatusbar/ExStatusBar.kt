package com.erolc.estatusbar

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

/**
 * StatusBar入口，注意：所属同一个Activity的所有fragment共用一个StatsBar对象。注意切换fragment时StatusBar样式的恢复
 *
 */
class ExStatusBar private constructor() {
    companion object {

        @JvmStatic
        fun create(activity: FragmentActivity): StatusBar {
            return StatusBarFactory.create(activity)
        }

        /**
         * 注意，该方法适配新版本的fragment，['androidx.appcompat:appcompat:1.2.0']以上，新的fragment会废弃掉[Fragment.setUserVisibleHint]
         * 而使用新的方式进行懒加载，所以如果不是新版本的fragment不建议使用该方法。建议使用[compatCreate]
         * 对于fragment来说，是和同一个activity上的所有fragment共享一个statusBar，所以在每次回到fragment的时候都需要对statusBar进行更新，防止前一个显示fragment
         * 的脏statusBar数据对该页面的影响。目前只有在该方法中设置的样式才会保留。后面会加入另外的一些设置，让在外部的设置也可以保留
         */
        @JvmStatic
        fun create(fragment: Fragment): StatusBar {
            return StatusBarFactory.create(fragment)
        }

        /**
         * 当在Viewpager+Fragment的场景下，并且[Fragment.setUserVisibleHint]并未废弃的时候，使用该方法创建
         * 该方法创建的statusBar对象，需要开发者自己维护。
         */
        @JvmStatic
        fun compatCreate(fragment: Fragment): StatusBar {
            return StatusBarFactory.create(fragment.requireActivity())
        }
    }
}

