package com.cook_recipe_app.firebase.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.os.CountDownTimer

class TimerViewModel : ViewModel() { // ViewModel을 상속받는 TimerViewModel 클래스 정의
    private var countDownTimer: CountDownTimer? = null // CountDownTimer 객체를 nullable로 선언

    private val _timeLeft = MutableLiveData<Int>() // 남은 시간을 저장하는 MutableLiveData
    val timeLeft: LiveData<Int> = _timeLeft // 외부에서 접근 가능한 읽기 전용 LiveData

    private val _isRunning = MutableLiveData(false) // 타이머 실행 상태를 저장하는 MutableLiveData
    val isRunning: LiveData<Boolean> = _isRunning

    private val _totalTime = MutableLiveData<Int>() // 총 시간을 저장하는 MutableLiveData
    val totalTime: LiveData<Int> = _totalTime

    private val _hours = MutableLiveData(0) // 시를 저장하는 MutableLiveData
    val hours: LiveData<Int> = _hours

    private val _minutes = MutableLiveData(0) // 분을 저장하는 MutableLiveData
    val minutes: LiveData<Int> = _minutes

    private val _seconds = MutableLiveData(0) // 초를 저장하는 MutableLiveData
    val seconds: LiveData<Int> = _seconds

    private val _showTimePickerView = MutableLiveData(true) // TimePicker 표시 여부를 저장하는 MutableLiveData
    val showTimePickerView: LiveData<Boolean> = _showTimePickerView

    private val _timerFinished = MutableLiveData<Boolean>() // 타이머 종료 여부를 저장하는 MutableLiveData

    private var pausedTimeLeft: Int = 0 // 일시정지 시 남은 시간을 저장하는 변수

    fun setTime(hours: Int, minutes: Int, seconds: Int) { // 시간을 설정하는 메서드
        _hours.value = hours // 시 설정
        _minutes.value = minutes // 분 설정
        _seconds.value = seconds // 초 설정
    }

    fun startTimer() {
        val hours = _hours.value ?: 0
        val minutes = _minutes.value ?: 0
        val seconds = _seconds.value ?: 0

        val totalSeconds = (hours * 3600) + (minutes * 60) + seconds
        if (totalSeconds <= 0) return

        _totalTime.value = totalSeconds // 총 시간 설정
        startTimerFromSeconds(totalSeconds)
    }

    private fun startTimerFromSeconds(seconds: Int) { // 주어진 초로부터 타이머를 시작하는 private 메서드
        _timeLeft.value = seconds // 남은 시간 설정
        _isRunning.value = true // 실행 상태로 설정
        _showTimePickerView.value = false // TimePicker 숨기기

        countDownTimer = object : CountDownTimer(seconds * 1000L, 1000) { // CountDownTimer 객체 생성
            override fun onTick(millisUntilFinished: Long) { // 매 틱마다 실행되는 메서드
                _timeLeft.value = (millisUntilFinished / 1000).toInt() // 남은 시간 업데이트
            }

            override fun onFinish() { // 타이머가 종료되면 실행되는 메서드
                stopTimer() // 타이머 정지
                _timeLeft.value = 0 // 남은 시간을 0으로 설정
                _timerFinished.value = true // 타이머 종료 상태로 설정
            }
        }.start() // CountDownTimer 시작
    }

    fun pauseTimer() { // 타이머를 일시정지하는 메서드
        countDownTimer?.cancel() // CountDownTimer 취소
        _isRunning.value = false // 실행 상태를 false로 설정
        pausedTimeLeft = _timeLeft.value ?: 0 // 현재 남은 시간 저장
    }

    fun resumeTimer() { // 타이머를 재개하는 메서드
        if (pausedTimeLeft > 0) { // 일시정지된 시간이 있으면
            startTimerFromSeconds(pausedTimeLeft) // 해당 시간부터 타이머 재시작
        }
    }

    fun stopTimer() { // 타이머를 정지하는 메서드
        countDownTimer?.cancel() // CountDownTimer 취소
        _isRunning.value = false // 실행 상태를 false로 설정
        _showTimePickerView.value = true // TimePicker 표시
        pausedTimeLeft = 0 // 일시정지 시간 초기화
        _timeLeft.value = _totalTime.value // 남은 시간을 총 시간으로 재설정
    }

    override fun onCleared() { // ViewModel이 파괴될 때 호출되는 메서드
        super.onCleared() // 상위 클래스의 메서드 호출
        countDownTimer?.cancel() // CountDownTimer 취소
    }
}