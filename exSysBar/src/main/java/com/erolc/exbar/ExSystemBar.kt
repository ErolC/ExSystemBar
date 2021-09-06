package com.erolc.exbar

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import com.erolc.exbar.systemBar.SystemBar

/**
 * StatusBar入口，注意：所属同一个Activity的所有fragment共用一个StatsBar对象。注意切换fragment时StatusBar样式的恢复
 *
 */
object ExSystemBar {
    /**
     * 得到activity对应的StatusBar
     */
    @JvmStatic
    fun create(activity: FragmentActivity): SystemBar {
        return SystemBarFactory.create(activity)
    }

    /**

     * 对于fragment来说，是和同一个activity上的所有fragment共享一个statusBar，所以在每次回到fragment的时候都需要对statusBar进行更新，该方法得到的StatusBar就能做到这一点：防止前一个显示fragment
     * 的脏statusBar数据对该页面的影响。
     * 最新：
     */
    @JvmStatic
    fun create(fragment: Fragment): SystemBar {
        return SystemBarFactory.create(fragment)
    }

    fun create(owner: LifecycleOwner): SystemBar {
        return SystemBarFactory.create(owner)
    }

    /**
     * 适配fragment懒加载
     * 当在Viewpager+Fragment的场景下使用，如果是使用最新的懒加载方式，则不需要调用该方法
     */
    @Deprecated("")
    @JvmStatic
    fun setUserVisibleHint(fragment: Fragment) {
        val create = SystemBarFactory.findStatusBar(fragment)
        create?.setUserVisibleHint(fragment.userVisibleHint)
    }
}

