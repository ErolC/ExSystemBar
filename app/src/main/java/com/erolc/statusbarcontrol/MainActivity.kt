package com.erolc.statusbarcontrol

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        setStatusBarBackground(R.drawable.status_bar_bg)
//        statusBarColor = Color.RED
        findViewById<TextView>(R.id.text).setOnClickListener {

        }


    }
}
