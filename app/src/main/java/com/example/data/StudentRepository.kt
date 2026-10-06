package com.example.data

import kotlinx.coroutines.flow.Flow

class StudentRepository(private val studentDao: StudentDao) {
    val allStudents: Flow<List<Student>> = studentDao.getAllStudents()

    suspend fun getById(id: String): Student? = studentDao.getStudentById(id)
    suspend fun getByRoll(roll: String): Student? = studentDao.getStudentByRoll(roll)

    suspend fun insert(student: Student) {
        studentDao.insertStudent(student)
    }

    suspend fun update(student: Student) {
        studentDao.updateStudent(student)
    }

    suspend fun delete(student: Student) {
        studentDao.deleteStudent(student)
    }

    suspend fun deleteById(id: String) {
        studentDao.deleteStudentById(id)
    }

    suspend fun clearAll() {
        studentDao.clearAllStudents()
    }

    suspend fun restoreSamples() {
        studentDao.insertAll(SampleData.initialStudents)
    }
}
