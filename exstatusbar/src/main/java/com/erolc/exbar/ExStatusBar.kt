package com.erolc.exbar

import android.graphics.Color
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner

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

         * 对于fragment来说，是和同一个activity上的所有fragment共享一个statusBar，所以在每次回到fragment的时候都需要对statusBar进行更新，该方法得到的StatusBar就能做到这一点：防止前一个显示fragment
         * 的脏statusBar数据对该页面的影响。
         * 最新：
         */
        @JvmStatic
        fun create(fragment: Fragment): StatusBar {
            return StatusBarFactory.create(fragment)
        }

        fun create(owner: LifecycleOwner):StatusBar{
            return StatusBarFactory.create(owner)
        }

        /**
         * 适配fragment懒加载
         * 当在Viewpager+Fragment的场景下使用，如果是使用最新的懒加载方式，则不需要调用该方法
         */
        @JvmStatic
        fun setUserVisibleHint(fragment: Fragment) {
            val create = StatusBarFactory.findStatusBar(fragment)
            create?.setUserVisibleHint(fragment.userVisibleHint)
        }

//        /**
//         * 设置夜间模式适配
//         * @param color 如果color为白色，那么将不会做夜间适配，如果为null，则使用默认的黑色作为夜间模式时状态栏的颜色，如果是白色以外的其他颜色，则夜间会使用该颜色
//          */
//        @JvmStatic
//        fun setDarkMode(owner: LifecycleOwner,color: Int? = Color.WHITE){
//            val findStatusBar = StatusBarFactory.findStatusBar(owner)
//            findStatusBar?.setDarkModeColor(color)
//        }
    }
}

