package com.erolc.exbar

import android.animation.ObjectAnimator
import android.util.Pair
import android.view.View

object ViewUtils {
    fun getPosition(view: View): Pair<Int, Int> {
        val position = IntArray(2)
        view.getLocationInWindow(position)
        return Pair(position[0], position[1])
    }
}

fun View.heightAnim(value: Int, duration: Int){
    val viewWrapper = ViewWrapper(this)
    ObjectAnimator.ofInt(viewWrapper, "height", value).setDuration(duration.toLong()).start()
}

internal class ViewWrapper(private val rView: View) {
    var width: Int
        get() = rView.layoutParams.width
        set(width) {
            rView.layoutParams.width = width
            rView.requestLayout()
        }
    var height: Int
        get() = rView.layoutParams.height
        set(height) {
            rView.layoutParams.height = height
            rView.requestLayout()
        }
}