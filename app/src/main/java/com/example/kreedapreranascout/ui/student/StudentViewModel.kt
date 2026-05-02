package com.example.kreedapreranascout.ui.student

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.kreedapreranascout.data.model.Student
import com.example.kreedapreranascout.data.repository.StudentRepository
import kotlinx.coroutines.launch

class StudentViewModel(private val repository: StudentRepository) : ViewModel() {
    private val _addStudentStatus = MutableLiveData<Result<Long>>()
    val addStudentStatus: LiveData<Result<Long>> = _addStudentStatus

    fun addStudent(student: Student) {
        viewModelScope.launch {
            try {
                val id = repository.addStudent(student)
                _addStudentStatus.value = Result.success(id)
            } catch (e: Exception) {
                _addStudentStatus.value = Result.failure(e)
            }
        }
    }

    fun getAllStudents(teacherId: Long) = repository.getAllStudents(teacherId).asLiveData()
    fun getStudentById(id: Long) = repository.getStudentById(id).asLiveData()
    fun searchStudents(teacherId: Long, query: String) = repository.searchStudents(teacherId, query).asLiveData()
    
    fun getPerformance(studentId: Long) = repository.getPerformanceForStudent(studentId).asLiveData()
    fun getAchievements(studentId: Long) = repository.getAchievementsForStudent(studentId).asLiveData()
}
