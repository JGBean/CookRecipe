package com.cook_recipe_app.firebase

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.cook_recipe_app.firebase.databinding.FragmentTimerBinding
import com.cook_recipe_app.firebase.ui.TimerViewModel
import java.util.Locale

class TimerFragment : BaseFragment() {
    companion object {
        private const val ARG_TIME_STRING = "arg_time_string"

        fun newInstance(timeString: String): TimerFragment {
            return TimerFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_TIME_STRING, timeString)
                }
            }
        }
    }

    private var _binding: FragmentTimerBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TimerViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTimerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.getString(ARG_TIME_STRING)?.let { timeString ->
            val (hours, minutes, seconds) = parseTime(timeString)
            viewModel.setTime(hours, minutes, seconds)
        }

        setupNumberPickers()
        setupButtons()
        observeViewModel()
    }

    private fun parseTime(timeString: String): Triple<Int, Int, Int> {
        val hours = Regex("(\\d+)시간").find(timeString)?.groupValues?.get(1)?.toIntOrNull() ?: 0
        val minutes = Regex("(\\d+)분").find(timeString)?.groupValues?.get(1)?.toIntOrNull() ?: 0
        val seconds = Regex("(\\d+)초").find(timeString)?.groupValues?.get(1)?.toIntOrNull() ?: 0
        return Triple(hours, minutes, seconds)
    }

    private fun setupNumberPickers() {
        binding.apply {
            hourPicker.apply {
                minValue = 0
                maxValue = 23
                setFormatter { String.format(Locale.US, "%02d", it) }
                value = viewModel.hours.value ?: 0
                setOnValueChangedListener { _, _, newVal ->
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
                viewModel.stopTimer()
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

    private fun updateTimerDisplay(seconds: Int) {
        val h = seconds / 3600
        val m = (seconds % 3600) / 60
        val s = seconds % 60
        binding.timerText.text = String.format(Locale.US, "%02d:%02d:%02d", h, m, s)
    }

    private fun updateProgress(secondsLeft: Int) {
        viewModel.totalTime.value?.let { total ->
            val progress = ((secondsLeft.toFloat() / total) * 100).toInt()
            binding.timerProgress.progress = progress
        }
    }

    private fun showTimeUpDialog() {
        AlertDialog.Builder(requireActivity())
            .setTitle("Timer Finished")
            .setMessage("Time's up!")
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
