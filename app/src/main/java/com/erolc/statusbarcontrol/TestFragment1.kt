package com.erolc.statusbarcontrol

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.erolc.exbar.*
import com.erolc.statusbarcontrol.databinding.FragmentTest1Binding

class TestFragment1 : Fragment() {
    private val statusBar: StatusBar by statusBar {

    }
    private var index = 0
    private var binding: FragmentTest1Binding? = null

    companion object {
        fun newInstance(index: Int): TestFragment1 {
            val args = Bundle()

            val fragment = TestFragment1()
            args.putInt("index", index)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.e("TAG", "onCreateView: ")
        index = arguments?.getInt("index") ?: 0
        binding = FragmentTest1Binding.inflate(inflater, container, false)
        binding!!.clickHandler = ClickHandler()

//        lifecycle.addObserver(object : LifecycleEventObserver {
//            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
//                Log.e("TAG", "onStateChanged: " + event.name)
//            }
//
//        })

        return binding!!.root
    }

    override fun onResume() {
        super.onResume()
        Log.e("TAG", "onResume: ")
    }

    inner class ClickHandler {
        fun hide(view: View) {
            statusBar.hide()
        }

        fun show(view: View) {
            statusBar.show()
        }

        fun next(view: View) {
            val requireActivity = requireActivity()
            if (requireActivity is NavTestActivity) {
                requireActivity.next(++index)
            }
        }

        fun showWithDrawable(view: View) {
            statusBar.setBackground(R.drawable.status_bar_bg)
        }

        fun getStatusBarHeight(view: View) {
            showToast(statusBar.getHeight())
        }


        fun randomColor(view: View) {
            statusBar.setBackgroundColor(randomColor())
        }

        fun switchTextColor(view: View) {
            val textColorIsDark = statusBar.isDark()
            Log.e("TAG", "switchTextColor: $textColorIsDark")
            statusBar.setTextColor(!textColorIsDark)
        }

        fun immersive(view: View) {
            statusBar.immersive()
        }

        fun randomColor(): Int {
            val r = Math.random() * 255
            val g = Math.random() * 255
            val b = Math.random() * 255
            return Color.rgb(r.toInt(), g.toInt(), b.toInt())
        }
    }
}