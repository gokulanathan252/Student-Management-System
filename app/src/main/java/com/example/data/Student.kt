package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "students")
data class Student(
    @PrimaryKey val studentId: String,
    val name: String,
    val rollNumber: String,
    val gender: String, // "Male", "Female", "Other"
    val dob: String, // "YYYY-MM-DD"
    val department: String, // "Computer Science", "Information Technology", etc.
    val academicYear: String, // "1st Year", "2nd Year", "3rd Year", "Final Year"
    val phone: String,
    val email: String,
    val address: String,
    val createdAt: Long = System.currentTimeMillis()
)
