package com.erolc.estatusbar

import android.app.Activity
import androidx.fragment.app.Fragment

/**
 * 对全部的statuBar对象做留存，一定要做清除工作，在该对象不存在的时候
 * 如果fragment使用了statusBar，就会被动创建了父activity的statusBar，如果fragment退出了，那么fragment会被从内存中剔除掉，但是activity有可能剔除不了。
 * 最佳的做法是，在activity对应的fragment没有全部退出，那么activity的statusBar就有存在的必要。
 */
internal class StatusBarFactory private constructor() {
    //以hashCode为key，保证唯一
    val map: MutableMap<Int, StatusBar> = mutableMapOf()

    companion object {
        private val factory: StatusBarFactory by lazy { StatusBarFactory() }

        /**
         * @param activity activity的名字，
         */
        fun create(activity: Activity): StatusBar {
            val key = activity.hashCode()
            var core = factory.map[key]
            if (core == null) {
                synchronized(StatusBarFactory::class.java) {
                    factory.map[key] = StatusBarImpl(activity)
                    core = factory.map[key]
                }
            }

            return core!!
        }

        fun create(fragment: Fragment): StatusBar {

            val fragmentKey = fragment.hashCode()
            val activityKey = fragment.requireActivity().hashCode()

            //查看是否有fragment对应的statusBar对象
            var core = factory.map[fragmentKey]
            if (core == null) {
                //如果没有，则看一下有没有其Activity的statusBar对象
                var statusbar = factory.map[activityKey]
                //如果都没有，那么就需要创建
                if (statusbar == null) {
                    synchronized(StatusBarFactory::class.java) {
                        //先创建其Activity的statusBar
                        val statusBarImpl = StatusBarImpl(fragment.requireActivity())
                        //保存
                        factory.map[activityKey] = statusBarImpl
                        //再创fragment的statusBar，并保存
                        factory.map[fragmentKey] =
                            FragmentStatusBar(fragment.requireActivity(), statusBarImpl)
                        //返回fragment的statusBar
                        core = factory.map[fragmentKey]
                    }
                } else {
                    //如果存在，那么只需要创建fragment的statusBar对象就可以了
                    core = FragmentStatusBar(fragment.requireActivity(), statusbar)
                    core?.apply {
                        factory.map[fragmentKey] = this
                    }
                }
            }

            return core!!
        }

        fun clear(fragment: Fragment) {
            factory.map.remove(fragment.hashCode())
        }

        fun clear(activity: Activity) {
            factory.map.remove(activity.hashCode())
        }
    }
}