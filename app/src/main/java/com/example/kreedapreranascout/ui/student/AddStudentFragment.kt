package com.example.kreedapreranascout.ui.student

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.kreedapreranascout.R
import com.example.kreedapreranascout.data.local.AppDatabase
import com.example.kreedapreranascout.data.model.Student
import com.example.kreedapreranascout.data.repository.StudentRepository
import com.example.kreedapreranascout.databinding.FragmentAddStudentBinding
import com.example.kreedapreranascout.util.SessionManager
import com.example.kreedapreranascout.util.ViewModelFactory

class AddStudentFragment : Fragment(R.layout.fragment_add_student) {
    private lateinit var binding: FragmentAddStudentBinding
    private val viewModel: StudentViewModel by viewModels {
        val db = AppDatabase.getDatabase(requireContext())
        ViewModelFactory(StudentRepository(db.studentDao(), db.performanceDao(), db.attendanceDao(), db.achievementDao()))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAddStudentBinding.bind(view)

        val sessionManager = SessionManager(requireContext())
        val teacherId = sessionManager.getTeacherId()

        binding.saveBtn.setOnClickListener {
            val name = binding.nameEdit.text.toString()
            val age = binding.ageEdit.text.toString().toIntOrNull() ?: 0
            val gender = binding.genderEdit.text.toString()
            val height = binding.heightEdit.text.toString().toDoubleOrNull() ?: 0.0
            val weight = binding.weightEdit.text.toString().toDoubleOrNull() ?: 0.0
            val sport = binding.sportEdit.text.toString()
            val rollNo = binding.rollNoEdit.text.toString()
            val college = binding.collegeEdit.text.toString()
            val classGrade = binding.classEdit.text.toString()
            val semester = binding.semEdit.text.toString()

            if (name.isNotEmpty() && age > 0 && gender.isNotEmpty() && height > 0 && weight > 0 && sport.isNotEmpty() && rollNo.isNotEmpty()) {
                val bmi = weight / ((height / 100) * (height / 100))
                val student = Student(
                    fullName = name,
                    age = age,
                    gender = gender,
                    classGrade = classGrade,
                    semester = semester,
                    section = "A", // Default section
                    rollNumber = rollNo,
                    usn = rollNo, // Using rollNo as USN if needed
                    college = college,
                    primarySport = sport,
                    secondarySport = null,
                    height = height,
                    weight = weight,
                    bmi = bmi,
                    guardianName = "Not specified",
                    guardianContact = "0000000000",
                    address = "Not specified",
                    medicalNotes = null,
                    createdByTeacherId = teacherId
                )
                viewModel.addStudent(student)
            } else {
                Toast.makeText(context, "Please fill all fields correctly", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.addStudentStatus.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                Toast.makeText(context, "Student added successfully", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            }.onFailure {
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
