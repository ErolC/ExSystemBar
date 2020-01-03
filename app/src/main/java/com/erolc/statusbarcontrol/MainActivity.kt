package com.erolc.statusbarcontrol

import android.app.Activity
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import com.erolc.estatusbar.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        findViewById<TextView>(R.id.text).systemUiVisibility = View.STATUS_BAR_VISIBLE
//        hideStatusBar()
//        setStatusBarTextColor(false)

        setStatusBarBackground(R.drawable.status_bar_bg,false)
    }

    fun hide(view:View){
        hideStatusBar()
    }
    fun showWithDrawable(view: View) {
        showToast(isShowStatusBar)
    }
    fun showWithColor(view: View) {
        showStatusBar()
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
    }
}


fun <T> Activity.showToast(t:T){
    Toast.makeText(this,"$t",Toast.LENGTH_SHORT).show()
}