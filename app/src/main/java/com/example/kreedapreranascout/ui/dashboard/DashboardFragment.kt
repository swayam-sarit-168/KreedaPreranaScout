package com.example.kreedapreranascout.ui.dashboard

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.kreedapreranascout.R
import com.example.kreedapreranascout.data.local.AppDatabase
import com.example.kreedapreranascout.data.repository.StudentRepository
import com.example.kreedapreranascout.data.repository.UserRepository
import com.example.kreedapreranascout.databinding.FragmentDashboardBinding
import com.example.kreedapreranascout.ui.auth.AuthViewModel
import com.example.kreedapreranascout.util.SessionManager
import com.example.kreedapreranascout.util.ViewModelFactory

class DashboardFragment : Fragment(R.layout.fragment_dashboard) {
    private lateinit var binding: FragmentDashboardBinding
    private val viewModel: DashboardViewModel by viewModels {
        val db = AppDatabase.getDatabase(requireContext())
        ViewModelFactory(StudentRepository(db.studentDao(), db.performanceDao(), db.attendanceDao(), db.achievementDao()))
    }
    private val authViewModel: AuthViewModel by viewModels {
        val db = AppDatabase.getDatabase(requireContext())
        ViewModelFactory(UserRepository(db.teacherDao()))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDashboardBinding.bind(view)

        val sessionManager = SessionManager(requireContext())
        val teacherId = sessionManager.getTeacherId()

        authViewModel.getTeacherById(teacherId).observe(viewLifecycleOwner) { teacher ->
            teacher?.let {
                binding.welcomeTv.text = "Welcome, ${it.fullName}"
                binding.subtitleTv.text = it.schoolName
            }
        }

        viewModel.getStudentCount(teacherId).observe(viewLifecycleOwner) { count ->
            binding.studentCountTv.text = count.toString()
        }

        binding.addStudentCard.setOnClickListener {
            findNavController().navigate(R.id.action_dashboard_to_addStudent)
        }

        binding.studentListCard.setOnClickListener {
            findNavController().navigate(R.id.action_dashboard_to_studentList)
        }

        binding.leaderboardCard.setOnClickListener {
            findNavController().navigate(R.id.action_dashboard_to_leaderboard)
        }

        binding.attendanceCard.setOnClickListener {
            findNavController().navigate(R.id.action_dashboard_to_attendance)
        }

        binding.settingsCard.setOnClickListener {
            findNavController().navigate(R.id.action_dashboard_to_settings)
        }
    }
}
