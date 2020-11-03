package com.erolc.estatusbar

import android.app.Activity

internal class StatusBarFactory private constructor() {
    val map: MutableMap<String, StatusBar> = mutableMapOf()

    companion object {
        private val factory: StatusBarFactory by lazy { StatusBarFactory() }

        /**
         * @param activity activity的名字，
         */
        fun create(activity: Activity): StatusBar {
            var core = factory.map[activity::class.qualifiedName.toString()]
            if (core == null) {
                synchronized(StatusBarFactory::class.java) {
                    factory.map[activity::class.qualifiedName.toString()] = StatusBarImpl(activity)
                    core = factory.map[activity::class.qualifiedName]
                }
            }

            return core!!
        }
    }
}