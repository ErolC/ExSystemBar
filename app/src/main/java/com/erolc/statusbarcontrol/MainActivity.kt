package com.erolc.statusbarcontrol

import android.app.Activity
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.erolc.estatusbar.*

/**
 *
 */
class MainActivity : AppCompatActivity() {
    private lateinit var statusBar: StatusBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        statusBar = getStatusBar()
//        AndroidBug5497Workaround.assistActivity(this)
//        或者
//        val exStatusBar:StatusBar = ExStatusBar(this)
//        或者
        statusBar = statusBar {
//            setBackground(R.drawable.status_bar_bg)
        }

    }

    override fun onResume() {
        super.onResume()
    }

    fun hide(view: View) {
        statusBar.hide()
    }

    fun show(view: View) {
        statusBar.show()
    }

    fun showRedColor(view: View) {
        statusBar.setBackgroundColor(Color.RED)
    }

    /**
     * 在界面完全加载完成之后，第一次使用自定义状态栏，会有问题
     */
    fun showWithDrawable(view: View) {
        statusBar.setBackground(R.drawable.status_bar_bg)
    }

    fun getStatusBarHeight(view: View) {
        showToast(statusBar.getHeight())
    }

    fun showSysBg(view: View) {
        statusBar.setSysBackgroundColor(Color.GRAY)
    }

    /**
     * 这里存在两个问题，
     * 第二个，setTextColor是否正确
     */
    fun switchTextColor(view: View) {
        val textColorIsDark = statusBar.textColorIsDark()
        Log.e("TAG", "switchTextColor: $textColorIsDark" )
        statusBar.setTextColor(!textColorIsDark)
    }

    fun immersive(view: View) {
        statusBar.immersive()
    }

}


fun <T> Activity.showToast(t: T) {
    Toast.makeText(this, "$t", Toast.LENGTH_SHORT).show()
}