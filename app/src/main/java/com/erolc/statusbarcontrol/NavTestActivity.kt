package com.erolc.statusbarcontrol

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.erolc.statusbarcontrol.databinding.ActivityNavBinding

class NavTestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityNavBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_nav)
        next(0)
    }

    fun next(index:Int){
        supportFragmentManager.beginTransaction()
            .replace(R.id.layout, TestFragment.newInstance(index))
            .addToBackStack("index$index")
            .commit()
    }



    override fun onBackPressed() {
        super.onBackPressed()
        supportFragmentManager.beginTransaction().disallowAddToBackStack().commit()
    }

}