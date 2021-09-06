package com.erolc.statusbarcontrol

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.erolc.exbar.*
import com.erolc.statusbarcontrol.databinding.FragmentTestBinding

class TestFragment : Fragment() {
    private val statusBar by navigationBar {
        binding!!.desc.text = "状态栏是黑色"
        when (index) {
            0 -> {
//                val parseColor = Color.parseColor("#003322")
//                setBackgroundColor(parseColor)
//                binding!!.desc.text = "状态栏是蓝色"
            }
            1 -> {
                setBackgroundColor(Color.RED)
                binding!!.desc.text = "状态栏是红色"
            }
            else -> {
                setBackgroundColor(Color.YELLOW)
                binding!!.desc.text = "状态栏是黄色"
            }
        }
    }
    private var index = 0
    private var binding: FragmentTestBinding? = null

    companion object {
        fun newInstance(index: Int): TestFragment {
            val args = Bundle()

            val fragment = TestFragment()
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
        binding = FragmentTestBinding.inflate(inflater, container, false)
        binding!!.clickHandler = ClickHandler()
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
//            val requireActivity = requireActivity()
//            if (requireActivity is NavTestActivity) {
//                requireActivity.next(++index)
//            }
            findNavController().navigate(R.id.action_testFragment_to_testFragment1)
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


fun <T> Fragment.showToast(t: T) {
    Toast.makeText(requireContext(), "$t", Toast.LENGTH_SHORT).show()
}