package com.example.kreedapreranascout.ui.student

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kreedapreranascout.R
import com.example.kreedapreranascout.data.local.AppDatabase
import com.example.kreedapreranascout.data.repository.StudentRepository
import com.example.kreedapreranascout.databinding.FragmentStudentListBinding
import com.example.kreedapreranascout.util.SessionManager
import com.example.kreedapreranascout.util.ViewModelFactory

class StudentListFragment : Fragment(R.layout.fragment_student_list) {
    private lateinit var binding: FragmentStudentListBinding
    private val viewModel: StudentViewModel by viewModels {
        val db = AppDatabase.getDatabase(requireContext())
        ViewModelFactory(StudentRepository(db.studentDao(), db.performanceDao(), db.attendanceDao(), db.achievementDao()))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentStudentListBinding.bind(view)

        val sessionManager = SessionManager(requireContext())
        val teacherId = sessionManager.getTeacherId()

        val adapter = StudentAdapter { student ->
            val action = StudentListFragmentDirections.actionStudentListToStudentProfile(student.id)
            findNavController().navigate(action)
        }

        binding.studentRv.layoutManager = LinearLayoutManager(context)
        binding.studentRv.adapter = adapter

        viewModel.getAllStudents(teacherId).observe(viewLifecycleOwner) { students ->
            adapter.submitList(students)
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false
            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    viewModel.searchStudents(teacherId, newText).observe(viewLifecycleOwner) {
                        adapter.submitList(it)
                    }
                }
                return true
            }
        })
    }
}
