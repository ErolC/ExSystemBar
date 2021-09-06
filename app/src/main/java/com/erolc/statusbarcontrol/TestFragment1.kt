package com.erolc.statusbarcontrol

import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.erolc.exbar.*
import com.erolc.statusbarcontrol.databinding.FragmentTest1Binding

class TestFragment1 : Fragment() {
    private val statusBar by statusBar {
        setBackgroundColor(Color.RED)
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

    inner class ClickHandler {
        fun hide(view: View) {
            statusBar.hide()
        }

        fun show(view: View) {
            statusBar.show()
        }

        fun next(view: View) {
//            val requireActivity = requireActivity()
//            if (requireActivity is NavTestActivity) {
//                requireActivity.next(++index)
//            }
            requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
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
            val textColorIsDark = statusBar.getContentIsDark()
            statusBar.setContentColor(!textColorIsDark)
        }

        fun immersive(view: View) {
            statusBar.invasion()
        }

        fun randomColor(): Int {
            val r = Math.random() * 255
            val g = Math.random() * 255
            val b = Math.random() * 255
            return Color.rgb(r.toInt(), g.toInt(), b.toInt())
        }
    }
}