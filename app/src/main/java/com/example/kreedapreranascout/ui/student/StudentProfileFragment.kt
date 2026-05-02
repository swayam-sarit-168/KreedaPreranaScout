package com.example.kreedapreranascout.ui.student

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.kreedapreranascout.R
import com.example.kreedapreranascout.data.local.AppDatabase
import com.example.kreedapreranascout.data.repository.StudentRepository
import com.example.kreedapreranascout.databinding.FragmentStudentProfileBinding
import com.example.kreedapreranascout.util.ViewModelFactory

class StudentProfileFragment : Fragment(R.layout.fragment_student_profile) {
    private lateinit var binding: FragmentStudentProfileBinding
    private val args: StudentProfileFragmentArgs by navArgs()
    private val viewModel: StudentViewModel by viewModels {
        val db = AppDatabase.getDatabase(requireContext())
        ViewModelFactory(StudentRepository(db.studentDao(), db.performanceDao(), db.attendanceDao(), db.achievementDao()))
    }
    private val performanceAdapter = PerformanceAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentStudentProfileBinding.bind(view)

        setupRecyclerView()
        val studentId = args.studentId

        viewModel.getStudentById(studentId).observe(viewLifecycleOwner) { student ->
            student?.let {
                binding.nameTv.text = it.fullName
                binding.studentDetailsTv.text = "${it.rollNumber} | ${it.college ?: "No College"} | ${it.classGrade}"
                binding.bmiTv.text = String.format("%.2f", it.bmi)
                binding.ageTv.text = it.age.toString()
                binding.genderTv.text = it.gender
                binding.sportChip.text = it.primarySport
            }
        }

        viewModel.getPerformance(studentId).observe(viewLifecycleOwner) { history ->
            performanceAdapter.submitList(history)
        }

        binding.startTrialBtn.setOnClickListener {
            val action = StudentProfileFragmentDirections.actionStudentProfileToTrialLogger(studentId)
            findNavController().navigate(action)
        }

        binding.viewCurveBtn.setOnClickListener {
            val action = StudentProfileFragmentDirections.actionStudentProfileToTalentCurve(studentId)
            findNavController().navigate(action)
        }
    }

    private fun setupRecyclerView() {
        binding.performanceRv.adapter = performanceAdapter
    }
}
