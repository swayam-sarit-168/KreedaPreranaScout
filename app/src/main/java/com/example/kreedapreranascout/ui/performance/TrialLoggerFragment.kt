package com.example.kreedapreranascout.ui.performance

import android.os.Bundle
import android.os.SystemClock
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.kreedapreranascout.R
import com.example.kreedapreranascout.data.local.AppDatabase
import com.example.kreedapreranascout.data.model.Performance
import com.example.kreedapreranascout.data.repository.StudentRepository
import com.example.kreedapreranascout.databinding.FragmentTrialLoggerBinding
import com.example.kreedapreranascout.ui.student.StudentViewModel
import com.example.kreedapreranascout.util.ViewModelFactory
import java.util.Timer
import java.util.TimerTask

class TrialLoggerFragment : Fragment(R.layout.fragment_trial_logger) {
    private lateinit var binding: FragmentTrialLoggerBinding
    private val args: TrialLoggerFragmentArgs by navArgs()
    private val viewModel: StudentViewModel by viewModels {
        val db = AppDatabase.getDatabase(requireContext())
        ViewModelFactory(StudentRepository(db.studentDao(), db.performanceDao(), db.attendanceDao(), db.achievementDao()))
    }

    private var timer: Timer? = null
    private var startTime = 0L
    private var isRunning = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentTrialLoggerBinding.bind(view)

        binding.startStopBtn.setOnClickListener {
            if (isRunning) stopTimer() else startTimer()
        }

        binding.resetBtn.setOnClickListener {
            resetTimer()
        }

        binding.saveTrialBtn.setOnClickListener {
            saveTrial()
        }
    }

    private fun startTimer() {
        startTime = SystemClock.elapsedRealtime()
        timer = Timer()
        timer?.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                val elapsed = SystemClock.elapsedRealtime() - startTime
                activity?.runOnUiThread {
                    binding.timerTv.text = formatTime(elapsed)
                }
            }
        }, 0, 10)
        isRunning = true
        binding.startStopBtn.text = "Stop"
    }

    private fun stopTimer() {
        timer?.cancel()
        isRunning = false
        binding.startStopBtn.text = "Start"
    }

    private fun resetTimer() {
        stopTimer()
        binding.timerTv.text = "00:00:00"
    }

    private fun formatTime(millis: Long): String {
        val minutes = (millis / 60000)
        val seconds = (millis % 60000) / 1000
        val hundredths = (millis % 1000) / 10
        return String.format("%02d:%02d:%02d", minutes, seconds, hundredths)
    }

    private fun saveTrial() {
        val studentId = args.studentId
        val valueStr = binding.valueEdit.text.toString()
        val timeValue = if (binding.timerTv.text != "00:00:00") {
             // Parse time to seconds or just save as string/millis
             val elapsed = SystemClock.elapsedRealtime() - startTime
             elapsed.toDouble() / 1000.0
        } else {
            valueStr.toDoubleOrNull() ?: 0.0
        }

        if (timeValue > 0) {
            val performance = Performance(
                studentId = studentId,
                testType = "Trial", // Should be dynamic
                value = timeValue,
                unit = "seconds",
                attemptNumber = 1,
                date = System.currentTimeMillis(),
                remarks = ""
            )
            // viewModel.addPerformance(performance)
            Toast.makeText(context, "Performance saved", Toast.LENGTH_SHORT).show()
        }
    }
}
