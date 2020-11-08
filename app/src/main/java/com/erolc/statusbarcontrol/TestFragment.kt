package com.erolc.statusbarcontrol

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.erolc.exbar.*
import com.erolc.statusbarcontrol.databinding.FragmentTestBinding

class TestFragment : Fragment() {
    private lateinit var statusBar: StatusBar
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
        Log.e("TAG", "onCreateView: " )
        index = arguments?.getInt("index") ?: 0
        binding = FragmentTestBinding.inflate(inflater, container, false)
        binding!!.clickHandler = ClickHandler()
        statusBar = statusBar {
            when (index) {
                0 -> {
                    setBackgroundColor(Color.BLUE)
                    Log.e("TAG", "onCreateView: color:蓝色 int:${Color.BLACK}" )
                    binding!!.desc.text = "状态栏是蓝色"
                }
                1 -> {
                    setBackgroundColor(Color.RED)
                    Log.e("TAG", "onCreateView: color:红色 int:${Color.RED}" )
                    binding!!.desc.text = "状态栏是红色"
                }
                else -> {
                    setBackgroundColor(Color.YELLOW)
                    Log.e("TAG", "onCreateView: color:黄色 int:${Color.YELLOW}" )
                    binding!!.desc.text = "状态栏是黄色"
                }
            }
        }

        return binding!!.root
    }


    inner class ClickHandler {
        fun hide(view: View) {
            statusBar.hide()
        }

        fun show(view: View) {
            statusBar.show()
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
            val textColorIsDark = statusBar.textColorIsDark()
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


fun <T> Fragment.showToast(t: T) {
    Toast.makeText(requireContext(), "$t", Toast.LENGTH_SHORT).show()
}