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
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.erolc.estatusbar.*

class MainActivity : AppCompatActivity() {
    lateinit var data: MutableLiveData<Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        findViewById<TextView>(R.id.text).systemUiVisibility = View.STATUS_BAR_VISIBLE
//        hideStatusBar()
//        setStatusBarTextColor(false)
        data = statusBarCurtain()
        val statusBar = statusBar {
            this.debug(true)

        }

    }

    fun hide(view: View) {
//        hideStatusBar()
        data.value = data.value?.plus(10)
    }

    fun showWithDrawable(view: View) {
        data.value = data.value?.minus(10)

    }

    fun showWithColor(view: View) {
        showStatusBar()
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
    }
}


fun <T> Activity.showToast(t: T) {
    Toast.makeText(this, "$t", Toast.LENGTH_SHORT).show()
}