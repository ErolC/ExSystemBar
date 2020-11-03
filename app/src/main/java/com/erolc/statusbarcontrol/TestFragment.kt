package com.erolc.statusbarcontrol

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.erolc.estatusbar.getStatusBar
import com.erolc.estatusbar.statusBar
import com.erolc.statusbarcontrol.databinding.ActivityTestBinding

class TestFragment : Fragment() {

    companion object {
        fun newInstance(): TestFragment {
            val args = Bundle()

            val fragment = TestFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = ActivityTestBinding.inflate(inflater, container, false)
        val statusBar = statusBar {
            setBackgroundColor(Color.BLUE)
        }
        binding.showColor.setOnClickListener {
            statusBar.setBackground(R.drawable.status_bar_bg)
        }
        return binding.root
    }
}