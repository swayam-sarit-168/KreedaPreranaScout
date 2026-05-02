package com.example.kreedapreranascout.ui.talent

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.kreedapreranascout.R
import com.example.kreedapreranascout.data.local.AppDatabase
import com.example.kreedapreranascout.data.repository.StudentRepository
import com.example.kreedapreranascout.databinding.FragmentTalentCurveBinding
import com.example.kreedapreranascout.ui.student.StudentViewModel
import com.example.kreedapreranascout.util.ViewModelFactory
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

class TalentCurveFragment : Fragment(R.layout.fragment_talent_curve) {
    private lateinit var binding: FragmentTalentCurveBinding
    private val args: TalentCurveFragmentArgs by navArgs()
    private val viewModel: StudentViewModel by viewModels {
        val db = AppDatabase.getDatabase(requireContext())
        ViewModelFactory(StudentRepository(db.studentDao(), db.performanceDao(), db.attendanceDao(), db.achievementDao()))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentTalentCurveBinding.bind(view)

        val studentId = args.studentId

        viewModel.getPerformance(studentId).observe(viewLifecycleOwner) { performances ->
            if (performances.isNotEmpty()) {
                val entries = performances.reversed().mapIndexed { index, perf ->
                    Entry(index.toFloat(), perf.value.toFloat())
                }

                val dataSet = LineDataSet(entries, "Performance")
                val lineData = LineData(dataSet)
                binding.lineChart.data = lineData
                binding.lineChart.invalidate()
            }
        }
    }
}
