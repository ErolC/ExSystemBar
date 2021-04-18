package com.erolc.exbar

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner

/**
 * StatusBar的对象工厂
 * 管理StatusBar对象
 */
internal class StatusBarFactory private constructor() {
    //以hashCode为key，保证唯一
    private val map: MutableMap<Int, StatusBar> = mutableMapOf()

    companion object {
        /**
         * 得到一个工厂对象
         */
        private val factory: StatusBarFactory by lazy { StatusBarFactory() }

        /**
         * 创建一个生命周期感知的statusBar对象
         * @param activity activity
         */
        fun create(activity: FragmentActivity): StatusBar {
            val key = activity.hashCode()
            var statusBar = factory.map[key]
            if (statusBar == null) {
                val realStatusBar = StatusBarImpl(activity)
                synchronized(realStatusBar) {
                    statusBar =
                        LifeCycleStatusBar(activity, realStatusBar)
                    factory.map[key] = statusBar!!
                }
            }

            return statusBar!!
        }

        /**
         * 创建一个生命周期感知的statusBar对象
         */
        fun create(fragment: Fragment): StatusBar {
            val key = fragment.hashCode()
            var statusBar = factory.map[key]
            if (statusBar == null) {
                //如果这里获取不到activity，那么证明当前的fragment不适合操作statusbar
                val activityStatusBar = create(fragment.requireActivity())
                synchronized(activityStatusBar) {
                    statusBar =
                        LifeCycleStatusBar(fragment, activityStatusBar)
                    factory.map[key] = statusBar!!
                }

            }
            return statusBar!!
        }

        internal fun create(owner: LifecycleOwner): StatusBar {
            return if (owner is Fragment) {
                create(owner)
            } else {
                create(owner as FragmentActivity)
            }
        }

        fun findStatusBar(owner: LifecycleOwner): LifeCycleStatusBar? {
            return factory.map[owner.hashCode()] as? LifeCycleStatusBar
        }

        /**
         * 清除特定的状态栏对象
         */
        fun clear(hashCode: Int) {
            factory.map.remove(hashCode)
        }
    }
}
