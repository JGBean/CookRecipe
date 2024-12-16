package com.cook_recipe_app.firebase

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.cook_recipe_app.firebase.ui.TimerViewModel
import com.cook_recipe_app.firebase.databinding.FragmentTimerBinding
import java.util.Locale

class TimerFragment : BaseFragment() {
    private var _binding: FragmentTimerBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TimerViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTimerBinding.inflate(inflater, container, false)
        return binding.root // 루트 뷰 반환
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupNumberPickers() // NumberPicker 설정 메서드 호출
        setupButtons() // 버튼 설정 메서드 호출
        observeViewModel() // ViewModel 관찰 메서드 호출
    }

    private fun setupNumberPickers() { // NumberPicker 설정 메서드
        binding.apply { // binding 객체에 apply 함수 적용
            hourPicker.apply { // 시간 NumberPicker 설정
                minValue = 0 // 최소값 설정
                maxValue = 23 // 최대값 설정
                setFormatter { String.format(Locale.US, "%02d", it) } // 포맷 설정
                value = viewModel.hours.value ?: 0 // 초기값 설정
                setOnValueChangedListener { _, _, newVal -> // 값 변경 리스너 설정
                    viewModel.setTime(newVal, minutePicker.value, secondPicker.value)
                }
            }

            minutePicker.apply {
                minValue = 0
                maxValue = 59
                setFormatter { String.format(Locale.US, "%02d", it) }
                value = viewModel.minutes.value ?: 0
                setOnValueChangedListener { _, _, newVal ->
                    viewModel.setTime(hourPicker.value, newVal, secondPicker.value)
                }
            }

            secondPicker.apply {
                minValue = 0
                maxValue = 59
                setFormatter { String.format(Locale.US, "%02d", it) }
                value = viewModel.seconds.value ?: 0
                setOnValueChangedListener { _, _, newVal ->
                    viewModel.setTime(hourPicker.value, minutePicker.value, newVal)
                }
            }
        }
    }

    private fun observeViewModel() {
        viewModel.timeLeft.observe(viewLifecycleOwner) { seconds ->
            updateTimerDisplay(seconds)
            updateProgress(seconds)
            if (seconds == 0 && viewModel.isRunning.value == true) {
                showTimeUpDialog()
                viewModel.stopTimer() // 타이머 종료 시 자동으로 정지
            }
        }

        viewModel.isRunning.observe(viewLifecycleOwner) { isRunning ->
            binding.startButton.text = when {
                isRunning -> "Pause"
                viewModel.totalTime.value != null && viewModel.timeLeft.value == 0 -> "Start"
                viewModel.timeLeft.value != viewModel.totalTime.value -> "Resume"
                else -> "Start"
            }
        }

        viewModel.showTimePickerView.observe(viewLifecycleOwner) { showPicker ->
            binding.timePickerGroup.visibility = if (showPicker) View.VISIBLE else View.GONE
            binding.progressGroup.visibility = if (showPicker) View.GONE else View.VISIBLE
        }
    }

    private fun setupButtons() {
        binding.startButton.setOnClickListener {
            when {
                viewModel.isRunning.value == true -> viewModel.pauseTimer()
                viewModel.timeLeft.value == 0 -> viewModel.startTimer()
                viewModel.timeLeft.value != viewModel.totalTime.value -> viewModel.resumeTimer()
                else -> viewModel.startTimer()
            }
        }

        binding.resetButton.setOnClickListener {
            viewModel.stopTimer()
        }
    }

    private fun updateTimerDisplay(seconds: Int) { // 타이머 디스플레이 업데이트 메서드
        val h = seconds / 3600 // 시간 계산
        val m = (seconds % 3600) / 60 // 분 계산
        val s = seconds % 60 // 초 계산
        binding.timerText.text = String.format(Locale.US, "%02d:%02d:%02d", h, m, s) // 타이머 텍스트 설정
    }

    private fun updateProgress(secondsLeft: Int) { // 진행 상황 업데이트 메서드
        viewModel.totalTime.value?.let { total -> // 총 시간이 null이 아니면
            val progress = ((secondsLeft.toFloat() / total) * 100).toInt() // 진행률 계산
            binding.timerProgress.progress = progress // 프로그레스 바 업데이트
        }
    }

    private fun showTimeUpDialog() { // 시간 종료 다이얼로그 표시 메서드
        AlertDialog.Builder(requireActivity()) // AlertDialog 생성
            .setTitle("Timer Finished") // 제목 설정
            .setMessage("Time's up!") // 메시지 설정
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() } // 확인 버튼 설정
            .show() // 다이얼로그 표시
    }

    override fun onDestroyView() { // Fragment View가 파괴될 때 호출되는 메서드
        super.onDestroyView() // 상위 클래스의 메서드 호출
        _binding = null // binding 객체 해제
    }
}