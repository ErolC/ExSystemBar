package com.erolc.statusbarcontrol

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
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
        setStatusBarTextColor(false)
    }

    fun hide(view:View){
        showToast(statusBarTextColorIsDark)
    }
    fun showWithDrawable(view: View) {
        showToast(isShowStatusBar)
    }
    fun showWithColor(view: View) {

    }
}


fun <T> Activity.showToast(t:T){
    Toast.makeText(this,"$t",Toast.LENGTH_SHORT).show()
}