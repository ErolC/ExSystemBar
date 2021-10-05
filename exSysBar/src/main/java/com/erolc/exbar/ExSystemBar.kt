package com.erolc.exbar

import androidx.lifecycle.LifecycleOwner
import com.erolc.exbar.bar.Bar
import com.erolc.exbar.systemBar.SystemBar

/**
 * StatusBar入口，注意：所属同一个Activity的所有fragment共用一个StatsBar对象。注意切换fragment时StatusBar样式的恢复
 *
 */
object ExSystemBar {
    /**
     * 由于setDecorFitsSystemWindows的原因，所以需要对ime做适配，否则adjustReSize会有问题
     */
    @Deprecated("")
    var adapterIme = true

    fun create(owner: LifecycleOwner): SystemBar {
        return SystemBarFactory.create(owner)
    }

    fun createStatusBar(owner: LifecycleOwner): Bar {
        return create(owner).getBar(SystemBar.STATUS_BAR)
    }

    fun createNavigationBar(owner: LifecycleOwner): Bar {
        return create(owner).getBar(SystemBar.NAVIGATION_BAR)
    }
}

