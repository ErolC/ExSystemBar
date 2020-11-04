package com.erolc.statusbarcontrol

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.erolc.statusbarcontrol.databinding.ActivityTestBinding

class TestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityTestBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_test)
        binding.pager.adapter = object : FragmentPagerAdapter(
            supportFragmentManager,
            BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        ) {
            override fun getItem(position: Int): Fragment {
                return TestFragment.newInstance(position)
            }

            override fun getCount(): Int {
                return 3
            }
        }
        binding.pager.offscreenPageLimit = 3
    }

}