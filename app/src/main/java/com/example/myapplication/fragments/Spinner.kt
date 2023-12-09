package com.example.myapplication.fragments

import android.view.View
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.myapplication.R

class Spinner(fragment: Fragment) {
    private var fragment: Fragment

    init {
        this.fragment = fragment
        this.start()
    }

    fun start() {
        val rotate = RotateAnimation(
            0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f
        ).apply {
            duration = 2000
            repeatCount = Animation.INFINITE
        }

        this.fragment.requireView().findViewById<ImageView>(R.id.loading).startAnimation(rotate)
    }

    fun stop() {
        this.fragment.requireView().findViewById<ImageView>(R.id.loading).clearAnimation()
        this.fragment.requireView().findViewById<ImageView>(R.id.loading).visibility = View.GONE
    }
}