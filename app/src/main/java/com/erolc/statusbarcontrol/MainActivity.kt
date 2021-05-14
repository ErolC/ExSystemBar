package com.erolc.statusbarcontrol

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import com.erolc.exbar.*
import com.erolc.statusbarcontrol.databinding.ActivityMainBinding

/**
 *  demo 在一个activity使用多个fragment，然后让每个fragment的状态栏都不一样，然后还有是一个次级activity
 */
class MainActivity : AppCompatActivity() {
    private  val statusBar: StatusBar by statusBar { //默认指定
        //实现状态栏的设置
        setBackground(R.color.colorAccent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding =
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
//        statusBar = getStatusBar()
//        AndroidBug5497Workaround.assistActivity(this)
//        或者
//        val exStatusBar:StatusBar = ExStatusBar(this)
//        或者
    }

    fun next(view: View) {
        val intent = Intent(this, NavTestActivity::class.java)
        startActivity(intent)
    }

    fun hide(view: View) {
//        statusBar.hide()
    }

    fun show(view: View) {
//        statusBar.show()


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

    fun switchTextColor(view: View) {
        val textColorIsDark = statusBar.isDark()
        Log.e("TAG", "switchTextColor: $textColorIsDark")
        statusBar.setTextColor(!textColorIsDark)
    }

    fun immersive(view: View) {
        statusBar.invasion()
    }

}


fun <T> Activity.showToast(t: T) {
    Toast.makeText(this, "$t", Toast.LENGTH_SHORT).show()
}