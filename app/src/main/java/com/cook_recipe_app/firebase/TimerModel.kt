package com.cook_recipe_app.firebase

import android.os.CountDownTimer

class TimerModel {
    private var countDownTimer: CountDownTimer? = null
    private var pausedTimeLeft: Int = 0

    var onTick: ((Int) -> Unit)? = null
    var onFinish: (() -> Unit)? = null

    fun startTimer(totalSeconds: Int) {
        countDownTimer?.cancel()
        countDownTimer = object : CountDownTimer(totalSeconds * 1000L, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                onTick?.invoke((millisUntilFinished / 1000).toInt())
            }

            override fun onFinish() {
                onFinish?.invoke()
            }
        }.start()
    }

    fun pauseTimer(currentTimeLeft: Int) {
        countDownTimer?.cancel()
        pausedTimeLeft = currentTimeLeft
    }

    fun resumeTimer() {
        if (pausedTimeLeft > 0) {
            startTimer(pausedTimeLeft)
        }
    }

    fun stopTimer() {
        countDownTimer?.cancel()
        pausedTimeLeft = 0
    }
}
