package com.erolc.exbar

import android.app.Activity
import android.os.Build
import android.view.DisplayCutout
import android.view.View
import android.view.Window
import android.widget.FrameLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import java.lang.Exception
import java.lang.ref.SoftReference
import java.lang.ref.WeakReference
import java.lang.reflect.Method


/**
 * 是否有刘海屏
 *
 * @return
 */


object DisplayCutoutHandler {
    private var hasNotchInScreen: Boolean? = null
    private var hasNotchInScreenBody: ((Boolean?) -> Unit)? = null

    private val Activity.hasNotchInScreen: Boolean
        get() {
            // android  P 以上有标准 API 来判断是否有刘海屏
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val displayCutout: DisplayCutout? = window.decorView.rootWindowInsets.displayCutout
                if (displayCutout != null) {
                    // 说明有刘海屏
                    return true
                }
            } else {
                // 通过其他方式判断是否有刘海屏  目前官方提供有开发文档的就 小米，vivo，华为（荣耀），oppo
                val manufacturer: String = Build.MANUFACTURER
                return when {
                    manufacturer.equals("HUAWEI", ignoreCase = true) -> {
                        hasNotchHw(this)
                    }
                    manufacturer.equals("xiaomi", ignoreCase = true) -> {
                        hasNotchXiaoMi(this)
                    }
                    manufacturer.equals("oppo", ignoreCase = true) -> {
                        hasNotchOPPO(this)
                    }
                    manufacturer.equals("vivo", ignoreCase = true) -> {
                        hasNotchVIVO(this)
                    }
                    else -> {
                        false
                    }
                }
            }
            return false
        }

    fun hasNotchInScreen(body: (Boolean?) -> Unit) {
        hasNotchInScreenBody = body
        if (hasNotchInScreen != null) {
            body.invoke(hasNotchInScreen)
        }
    }

    fun checkHasNotchInScreen(activity: Activity) {
        if (hasNotchInScreen == null) {
            hasNotchInScreen = WeakReference(activity).get()?.hasNotchInScreen
            hasNotchInScreenBody?.invoke(hasNotchInScreen)
        }
    }

    /**
     * 判断vivo是否有刘海屏
     * https://swsdl.vivo.com.cn/appstore/developer/uploadfile/20180328/20180328152252602.pdf
     *
     * @param activity
     * @return
     */
    private fun hasNotchVIVO(activity: Activity): Boolean {
        return try {
            val c = Class.forName("android.util.FtFeature")
            val get: Method = c.getMethod("isFeatureSupport", Int::class.javaPrimitiveType)
            get.invoke(c, 0x20) as Boolean
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * 判断oppo是否有刘海屏
     * https://open.oppomobile.com/wiki/doc#id=10159
     *
     * @param activity
     * @return
     */
    private fun hasNotchOPPO(activity: Activity): Boolean {
        return activity.packageManager.hasSystemFeature("com.oppo.feature.screen.heteromorphism")
    }

    /**
     * 判断xiaomi是否有刘海屏
     * https://dev.mi.com/console/doc/detail?pId=1293
     *
     * @param activity
     * @return
     */
    private fun hasNotchXiaoMi(activity: Activity): Boolean {
        return try {
            val c = Class.forName("android.os.SystemProperties")
            val get: Method =
                c.getMethod("getInt", String::class.java, Int::class.javaPrimitiveType)
            get.invoke(c, "ro.miui.notch", 0) as Int == 1
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * 判断华为是否有刘海屏
     * https://devcenter-test.huawei.com/consumer/cn/devservice/doc/50114
     *
     * @param activity
     * @return
     */
    private fun hasNotchHw(activity: Activity): Boolean {
        return try {
            val cl: ClassLoader = activity.classLoader
            val hwNotchSizeUtil = cl.loadClass("com.huawei.android.util.HwNotchSizeUtil")
            val get: Method = hwNotchSizeUtil.getMethod("hasNotchInScreen")
            get.invoke(hwNotchSizeUtil) as Boolean
        } catch (e: Exception) {
            false
        }
    }

}



