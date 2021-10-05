package com.erolc.statusbarcontrol

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.databinding.DataBindingUtil
import com.erolc.exbar.*
import com.erolc.statusbarcontrol.databinding.ActivityMainBinding

/**
 *  demo 在一个activity使用多个fragment，然后让每个fragment的状态栏都不一样，然后还有是一个次级activity
 */
class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding =
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
//        ViewCompat.setOnApplyWindowInsetsListener(binding.root){view,insets->
//            val inset = insets.getInsets(WindowInsetsCompat.Type.ime())
//            val height = contentView.computeVisibleDisplayHeight()
//            Log.e("TAG", "onCreate: $inset  -- $height" )
//            insets
//        }
//        window.statusBarColor = Color.BLUE
//        statusBar.invasion()
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

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        Log.e("TAG", "onConfigurationChanged: ")
    }

    fun showRedColor(view: View) {
//        statusBar.setBackgroundColor(Color.RED)
    }

    fun showWithDrawable(view: View) {
//        statusBar.setBackground(R.drawable.status_bar_bg)
    }

    fun getStatusBarHeight(view: View) {
//        showToast(statusBar.getHeight())
    }

    fun switchTextColor(view: View) {
//        val textColorIsDark = statusBar.getContentIsDark()
//        Log.e("TAG", "switchTextColor: $textColorIsDark")
//        statusBar.setContentColor(!textColorIsDark)
    }

    fun immersive(view: View) {
//        statusBar.invasion()
    }

}


fun <T> Activity.showToast(t: T) {
    Toast.makeText(this, "$t", Toast.LENGTH_SHORT).show()
}

private val Activity.contentView get() = window.decorView.findViewById<FrameLayout>(Window.ID_ANDROID_CONTENT)
