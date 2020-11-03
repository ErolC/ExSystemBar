package com.erolc.statusbarcontrol

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.erolc.estatusbar.StatusBar
import com.erolc.estatusbar.statusBar

class TestActivity : AppCompatActivity() {
    private lateinit var statusBar: StatusBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

//        statusBar = getStatusBar()
//        AndroidBug5497Workaround.assistActivity(this)
//        或者
//        val exStatusBar:StatusBar = ExStatusBar(this)
//        或者
        statusBar = statusBar {
//            setBackground(R.drawable.status_bar_bg)
        }
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

    fun showWithDrawable(view: View) {
        statusBar.setBackground(R.drawable.status_bar_bg)
    }

    fun getStatusBarHeight(view: View) {
        showToast(statusBar.getHeight())
    }

    fun showSysBg(view: View) {
        statusBar.setSysBackgroundColor(Color.GRAY)
    }

    fun switchTextColor(view: View) {
        val textColorIsDark = statusBar.textColorIsDark()
        Log.e("TAG", "switchTextColor: $textColorIsDark")
        statusBar.setTextColor(!textColorIsDark)
    }

    fun immersive(view: View) {
        statusBar.immersive()
    }
}