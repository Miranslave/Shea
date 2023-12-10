package com.example.myapplication.fragments

import android.view.View
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.ImageView

class Spinner(loadingElem: ImageView, autoStart: Boolean = true) {
    private var loadingElement: ImageView
    private var started = false

    init {
        this.loadingElement = loadingElem
        if (autoStart) {
            this.start()
        }
    }

    fun start() {
        if (this.started) {
            return
        }

        this.started = true
        val rotate = RotateAnimation(
            0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f
        ).apply {
            duration = 2000
            repeatCount = Animation.INFINITE
        }

        this.loadingElement.startAnimation(rotate)
    }

    fun stop() {
        try {
            this.loadingElement.clearAnimation()
            this.loadingElement.visibility = View.GONE
        } catch (_: Exception) {

        }
    }
}