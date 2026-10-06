package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.SampleData
import com.example.data.Student
import com.example.data.StudentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class StudentViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: StudentRepository

    init {
        val database = AppDatabase.getDatabase(application)
        repository = StudentRepository(database.studentDao())
    }

    // Auth State
    private val _isLoggedIn = MutableStateFlow(true) // Pre-authenticated for instant demo, can log out and back in
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _currentScreen = MutableStateFlow(Screen.DASHBOARD)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    // Notification banner / Toast message
    private val _userMessage = MutableStateFlow<String?>(null)
    val userMessage: StateFlow<String?> = _userMessage.asStateFlow()

    // Active detail modal student
    private val _viewingStudent = MutableStateFlow<Student?>(null)
    val viewingStudent: StateFlow<Student?> = _viewingStudent.asStateFlow()

    // Active student being edited (null for add new)
    private val _editingStudent = MutableStateFlow<Student?>(null)
    val editingStudent: StateFlow<Student?> = _editingStudent.asStateFlow()

    // Confirmation dialog state
    private val _studentPendingDelete = MutableStateFlow<Student?>(null)
    val studentPendingDelete: StateFlow<Student?> = _studentPendingDelete.asStateFlow()

    private val _showClearAllConfirm = MutableStateFlow(false)
    val showClearAllConfirm: StateFlow<Boolean> = _showClearAllConfirm.asStateFlow()

    // Filter & Search state
    private val _filterState = MutableStateFlow(StudentFilterState())
    val filterState: StateFlow<StudentFilterState> = _filterState.asStateFlow()

    // Raw list of students from Room
    val rawStudents: StateFlow<List<Student>> = repository.allStudents
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Filtered and sorted students
    val filteredStudents: StateFlow<List<Student>> = combine(rawStudents, _filterState) { students, filter ->
        var list = students

        // Search query across ID, Name, Roll, Email, Department
        if (filter.searchQuery.isNotBlank()) {
            val q = filter.searchQuery.trim().lowercase()
            list = list.filter {
                it.studentId.lowercase().contains(q) ||
                it.name.lowercase().contains(q) ||
                it.rollNumber.lowercase().contains(q) ||
                it.email.lowercase().contains(q) ||
                it.department.lowercase().contains(q)
            }
        }

        // Department filter
        if (filter.departmentFilter != "All") {
            list = list.filter { it.department.equals(filter.departmentFilter, ignoreCase = true) }
        }

        // Academic Year filter
        if (filter.yearFilter != "All") {
            list = list.filter { it.academicYear.equals(filter.yearFilter, ignoreCase = true) }
        }

        // Gender filter
        if (filter.genderFilter != "All") {
            list = list.filter { it.gender.equals(filter.genderFilter, ignoreCase = true) }
        }

        // Sorting
        list = when (filter.sortField) {
            SortField.NAME -> if (filter.sortDirection == SortDirection.ASC) list.sortedBy { it.name.lowercase() } else list.sortedByDescending { it.name.lowercase() }
            SortField.ID -> if (filter.sortDirection == SortDirection.ASC) list.sortedBy { it.studentId } else list.sortedByDescending { it.studentId }
            SortField.ROLL -> if (filter.sortDirection == SortDirection.ASC) list.sortedBy { it.rollNumber } else list.sortedByDescending { it.rollNumber }
            SortField.DEPARTMENT -> if (filter.sortDirection == SortDirection.ASC) list.sortedBy { it.department } else list.sortedByDescending { it.department }
            SortField.YEAR -> if (filter.sortDirection == SortDirection.ASC) list.sortedBy { it.academicYear } else list.sortedByDescending { it.academicYear }
        }

        list
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun login(user: String, pass: String): Boolean {
        if (user.trim() == "admin" && pass.trim() == "admin123") {
            _isLoggedIn.value = true
            _currentScreen.value = Screen.DASHBOARD
            showMessage("Welcome back, Administrator!")
            return true
        }
        return false
    }

    fun logout() {
        _isLoggedIn.value = false
        _currentScreen.value = Screen.LOGIN
        showMessage("Logged out successfully.")
    }

    fun navigateTo(screen: Screen) {
        if (screen == Screen.ADD_STUDENT) {
            _editingStudent.value = null
        }
        _currentScreen.value = screen
    }

    fun setSearchQuery(query: String) {
        _filterState.value = _filterState.value.copy(searchQuery = query)
    }

    fun setDepartmentFilter(dept: String) {
        _filterState.value = _filterState.value.copy(departmentFilter = dept)
    }

    fun setYearFilter(year: String) {
        _filterState.value = _filterState.value.copy(yearFilter = year)
    }

    fun setGenderFilter(gender: String) {
        _filterState.value = _filterState.value.copy(genderFilter = gender)
    }

    fun toggleSort(field: SortField) {
        val current = _filterState.value
        if (current.sortField == field) {
            val newDir = if (current.sortDirection == SortDirection.ASC) SortDirection.DESC else SortDirection.ASC
            _filterState.value = current.copy(sortDirection = newDir)
        } else {
            _filterState.value = current.copy(sortField = field, sortDirection = SortDirection.ASC)
        }
    }

    fun clearFilters() {
        _filterState.value = StudentFilterState()
    }

    fun viewStudentDetails(student: Student) {
        _viewingStudent.value = student
    }

    fun closeStudentDetails() {
        _viewingStudent.value = null
    }

    fun openEditStudent(student: Student) {
        _editingStudent.value = student
        _currentScreen.value = Screen.ADD_STUDENT
    }

    fun cancelEdit() {
        _editingStudent.value = null
        _currentScreen.value = Screen.STUDENTS
    }

    fun requestDeleteStudent(student: Student) {
        _studentPendingDelete.value = student
    }

    fun dismissDeleteDialog() {
        _studentPendingDelete.value = null
    }

    fun confirmDeleteStudent() {
        val student = _studentPendingDelete.value ?: return
        viewModelScope.launch {
            repository.delete(student)
            _studentPendingDelete.value = null
            if (_viewingStudent.value?.studentId == student.studentId) {
                _viewingStudent.value = null
            }
            showMessage("Student '${student.name}' was successfully removed.")
        }
    }

    fun requestClearAllData() {
        _showClearAllConfirm.value = true
    }

    fun dismissClearAllData() {
        _showClearAllConfirm.value = false
    }

    fun confirmClearAllData() {
        viewModelScope.launch {
            repository.clearAll()
            _showClearAllConfirm.value = false
            showMessage("All student records have been cleared.")
        }
    }

    fun restoreSampleData() {
        viewModelScope.launch {
            repository.restoreSamples()
            showMessage("Sample students restored successfully.")
        }
    }

    fun saveStudent(
        student: Student,
        isEditMode: Boolean,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            // Validation
            if (student.studentId.isBlank()) {
                onError("Student ID is required.")
                return@launch
            }
            if (student.name.isBlank()) {
                onError("Student Name is required.")
                return@launch
            }
            if (student.rollNumber.isBlank()) {
                onError("Roll Number is required.")
                return@launch
            }
            if (student.email.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(student.email).matches()) {
                onError("Please provide a valid email address.")
                return@launch
            }
            if (student.phone.isBlank() || student.phone.length < 7) {
                onError("Please provide a valid phone number (at least 7 digits).")
                return@launch
            }

            // Uniqueness check for new or changed ID/Roll
            val currentList = rawStudents.value
            if (!isEditMode) {
                if (currentList.any { it.studentId.equals(student.studentId, ignoreCase = true) }) {
                    onError("A student with ID '${student.studentId}' already exists.")
                    return@launch
                }
                if (currentList.any { it.rollNumber.equals(student.rollNumber, ignoreCase = true) }) {
                    onError("A student with Roll Number '${student.rollNumber}' already exists.")
                    return@launch
                }
                repository.insert(student)
                _editingStudent.value = null
                _currentScreen.value = Screen.STUDENTS
                showMessage("Student '${student.name}' added successfully!")
                onSuccess()
            } else {
                // If ID cannot be changed or if roll changed, ensure roll doesn't clash with others
                val clashingRoll = currentList.any {
                    it.studentId != student.studentId && it.rollNumber.equals(student.rollNumber, ignoreCase = true)
                }
                if (clashingRoll) {
                    onError("Roll Number '${student.rollNumber}' is already used by another student.")
                    return@launch
                }
                repository.update(student)
                _editingStudent.value = null
                _currentScreen.value = Screen.STUDENTS
                showMessage("Student '${student.name}' updated successfully!")
                onSuccess()
            }
        }
    }

    fun showMessage(msg: String) {
        _userMessage.value = msg
    }

    fun clearUserMessage() {
        _userMessage.value = null
    }
}
