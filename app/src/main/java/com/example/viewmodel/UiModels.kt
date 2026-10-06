package com.example.viewmodel

enum class SortField {
    NAME, ID, ROLL, DEPARTMENT, YEAR
}

enum class SortDirection {
    ASC, DESC
}

data class StudentFilterState(
    val searchQuery: String = "",
    val departmentFilter: String = "All",
    val yearFilter: String = "All",
    val genderFilter: String = "All",
    val sortField: SortField = SortField.NAME,
    val sortDirection: SortDirection = SortDirection.ASC
)

enum class Screen {
    LOGIN,
    DASHBOARD,
    STUDENTS,
    ADD_STUDENT,
    REPORTS,
    SETTINGS
}
