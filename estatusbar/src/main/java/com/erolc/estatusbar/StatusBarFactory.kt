package com.erolc.estatusbar

import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

/**
 * StatusBar的对象工厂
 *
 */
internal class StatusBarFactory private constructor() {
    //以hashCode为key，保证唯一
    private val map: MutableMap<Int, StatusBar> = mutableMapOf()
    private val MAIN_STATUS_BAR_KEY = "mainStatusBarKey".hashCode()

    companion object {
        private val factory: StatusBarFactory by lazy { StatusBarFactory() }

        /**
         * 创建一个唯一的处理状态栏的对象。
         */
        private fun createMainStatusBar(activity: Activity): StatusBar {
            var statusBar = factory.map[factory.MAIN_STATUS_BAR_KEY]
            if (statusBar == null) {
                statusBar = StatusBarImpl(activity)
                factory.map[factory.MAIN_STATUS_BAR_KEY] = statusBar

            }
            return statusBar
        }

        /**
         * 创建一个生命周期感知的statusBar副本对象
         * @param activity activity
         */
        fun create(activity: FragmentActivity): StatusBar {
            val key = activity.hashCode()
            var statusBar = factory.map[key]
            if (statusBar == null) {
                val createMainStatusBar = createMainStatusBar(activity)
                synchronized(createMainStatusBar) {
                    statusBar =
                        LifeCycleStatusBar(activity.lifecycle, key, createMainStatusBar)
                    factory.map[key] = statusBar!!
                }
            }

            return statusBar!!
        }

        /**
         * 创建一个生命周期感知的statusBar副本对象
         */
        fun create(fragment: Fragment): StatusBar {
            val key = fragment.hashCode()
            var statusBar = factory.map[key]
            if (statusBar == null) {
                //如果这里获取不到activity，那么证明当前的fragment不适合操作statusbar
                val createMainStatusBar = createMainStatusBar(fragment.requireActivity())
                synchronized(createMainStatusBar) {
                    statusBar =
                        LifeCycleStatusBar(fragment.lifecycle, key, createMainStatusBar)
                    factory.map[key] = statusBar!!
                }

            }
            return statusBar!!
        }

        /**
         * 清除特定的状态栏对象
         */
        fun clear(hashCode: Int) {
            factory.map.remove(hashCode)
        }
    }
}
