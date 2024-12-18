package com.cook_recipe_app.firebase.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cook_recipe_app.firebase.TimerModel

class TimerViewModel : ViewModel() {
    private val timerModel = TimerModel()

    private val _timeLeft = MutableLiveData<Int>()
    val timeLeft: LiveData<Int> = _timeLeft

    private val _isRunning = MutableLiveData(false)
    val isRunning: LiveData<Boolean> = _isRunning

    private val _totalTime = MutableLiveData<Int>()
    val totalTime: LiveData<Int> = _totalTime

    private val _hours = MutableLiveData(0)
    val hours: LiveData<Int> = _hours

    private val _minutes = MutableLiveData(0)
    val minutes: LiveData<Int> = _minutes

    private val _seconds = MutableLiveData(0)
    val seconds: LiveData<Int> = _seconds

    private val _showTimePickerView = MutableLiveData(true)
    val showTimePickerView: LiveData<Boolean> = _showTimePickerView

    init {
        timerModel.onTick = { seconds ->
            _timeLeft.value = seconds
        }
        timerModel.onFinish = {
            stopTimer()
            _timeLeft.value = 0
        }
    }

    fun setTime(hours: Int, minutes: Int, seconds: Int) {
        _hours.value = hours
        _minutes.value = minutes
        _seconds.value = seconds
    }

    fun startTimer() {
        val hoursValue = hours.value ?: 0
        val minutesValue = minutes.value ?: 0
        val secondsValue = seconds.value ?: 0

        val totalSeconds = (hoursValue * 3600) + (minutesValue * 60) + secondsValue
        if (totalSeconds <= 0) return

        _totalTime.value = totalSeconds
        _isRunning.value = true
        _showTimePickerView.value = false

        timerModel.startTimer(totalSeconds)
    }

    fun pauseTimer() {
        timerModel.pauseTimer(_timeLeft.value ?: 0)
        _isRunning.value = false
    }

    fun resumeTimer() {
        timerModel.resumeTimer()
        _isRunning.value = true
    }

    fun stopTimer() {
        timerModel.stopTimer()
        _isRunning.value = false
        _showTimePickerView.value = true
        _timeLeft.value = _totalTime.value
    }
}
