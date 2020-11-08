package com.erolc.exbar

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

/**
 * StatusBar入口，注意：所属同一个Activity的所有fragment共用一个StatsBar对象。注意切换fragment时StatusBar样式的恢复
 *
 */
class ExStatusBar private constructor() {
    companion object {

        /**
         * 得到activity对应的StatusBar
         */
        @JvmStatic
        fun create(activity: FragmentActivity): StatusBar {
            return StatusBarFactory.create(activity)
        }

        /**
         * 注意，该方法返回的StatusBar适配新版本的fragment，['androidx.appcompat:appcompat:1.2.0']以上，新的fragment会废弃掉[Fragment.setUserVisibleHint]
         * 而使用新的方式进行懒加载，所以如果不是新版本的fragment不建议使用该方法。建议使用[compatCreate]，
         * 也就是说只有在[Fragment.setUserVisibleHint]尚未被废弃，并且是ViewPager+Fragment的场景才推荐使用[compatCreate]得到的StatusBar
         *
         * 对于fragment来说，是和同一个activity上的所有fragment共享一个statusBar，所以在每次回到fragment的时候都需要对statusBar进行更新，该方法得到的StatusBar就能做到这一点：防止前一个显示fragment
         * 的脏statusBar数据对该页面的影响。
         * 最新：
         */
        @JvmStatic
        fun create(fragment: Fragment): StatusBar {
            val create = StatusBarFactory.create(fragment)
            setUserVisibleHint(fragment)
            return create
        }

        /**
         * 当在Viewpager+Fragment的场景下，并且[Fragment.setUserVisibleHint]并未废弃的时候，使用该方法创建
         * 该方法创建的statusBar对象，需要开发者自己维护。
         */
        @JvmStatic
        @Deprecated("", ReplaceWith("create(fragment)"))
        fun compatCreate(fragment: Fragment): StatusBar {
            return StatusBarFactory.create(fragment.requireActivity())
        }

        /**
         * 当在Viewpager+Fragment的场景下使用，如果是使用最新的懒加载方式，则不需要调用该方法
         */
        fun setUserVisibleHint(fragment: Fragment) {
            val create = StatusBarFactory.findFragmentWithMap(fragment)
            create?.setUserVisibleHint(fragment.userVisibleHint)
        }
    }
}

