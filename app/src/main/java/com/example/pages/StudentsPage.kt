package com.example.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.components.StudentTable
import com.example.data.SampleData
import com.example.data.Student
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.IndigoSecondary
import com.example.ui.theme.Slate100
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate50
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate900
import com.example.viewmodel.SortDirection
import com.example.viewmodel.SortField
import com.example.viewmodel.StudentFilterState

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun StudentsPage(
    students: List<Student>,
    filterState: StudentFilterState,
    onSearchChange: (String) -> Unit,
    onDepartmentFilterChange: (String) -> Unit,
    onYearFilterChange: (String) -> Unit,
    onGenderFilterChange: (String) -> Unit,
    onToggleSort: (SortField) -> Unit,
    onClearFilters: () -> Unit,
    onAddNewStudent: () -> Unit,
    onViewStudent: (Student) -> Unit,
    onEditStudent: (Student) -> Unit,
    onDeleteStudent: (Student) -> Unit,
    modifier: Modifier = Modifier
) {
    var deptMenuExpanded by remember { mutableStateOf(false) }
    var yearMenuExpanded by remember { mutableStateOf(false) }
    var genderMenuExpanded by remember { mutableStateOf(false) }
    var sortMenuExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Slate50)
            .padding(20.dp)
            .testTag("page_students")
    ) {
        // Top Action Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Student Directory",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Slate900
                )
                Text(
                    text = "Showing ${students.size} enrolled students",
                    fontSize = 12.sp,
                    color = Slate500
                )
            }

            Button(
                onClick = onAddNewStudent,
                colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.testTag("btn_add_student_top")
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Student",
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text("Add Student", fontWeight = FontWeight.SemiBold)
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Search & Filter Card
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Search Input
                OutlinedTextField(
                    value = filterState.searchQuery,
                    onValueChange = onSearchChange,
                    placeholder = { Text("Search by ID, Name, Roll No, Email, or Department...") },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = "Search", tint = Slate500)
                    },
                    trailingIcon = {
                        if (filterState.searchQuery.isNotBlank()) {
                            IconButton(onClick = { onSearchChange("") }) {
                                Icon(Icons.Default.Clear, contentDescription = "Clear search", tint = Slate500)
                            }
                        }
                    },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = IndigoPrimary,
                        unfocusedBorderColor = Slate200
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("search_students_input")
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Filters & Sort Controls
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Department Dropdown
                    Box {
                        OutlinedButton(
                            onClick = { deptMenuExpanded = true },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.testTag("dropdown_filter_dept")
                        ) {
                            Text(
                                text = "Dept: ${filterState.departmentFilter}",
                                fontSize = 12.sp,
                                color = Slate700
                            )
                        }
                        DropdownMenu(
                            expanded = deptMenuExpanded,
                            onDismissRequest = { deptMenuExpanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("All Departments") },
                                onClick = {
                                    onDepartmentFilterChange("All")
                                    deptMenuExpanded = false
                                }
                            )
                            SampleData.departments.forEach { dept ->
                                DropdownMenuItem(
                                    text = { Text(dept) },
                                    onClick = {
                                        onDepartmentFilterChange(dept)
                                        deptMenuExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    // Academic Year Dropdown
                    Box {
                        OutlinedButton(
                            onClick = { yearMenuExpanded = true },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.testTag("dropdown_filter_year")
                        ) {
                            Text(
                                text = "Year: ${filterState.yearFilter}",
                                fontSize = 12.sp,
                                color = Slate700
                            )
                        }
                        DropdownMenu(
                            expanded = yearMenuExpanded,
                            onDismissRequest = { yearMenuExpanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("All Years") },
                                onClick = {
                                    onYearFilterChange("All")
                                    yearMenuExpanded = false
                                }
                            )
                            SampleData.academicYears.forEach { yr ->
                                DropdownMenuItem(
                                    text = { Text(yr) },
                                    onClick = {
                                        onYearFilterChange(yr)
                                        yearMenuExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    // Gender Dropdown
                    Box {
                        OutlinedButton(
                            onClick = { genderMenuExpanded = true },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.testTag("dropdown_filter_gender")
                        ) {
                            Text(
                                text = "Gender: ${filterState.genderFilter}",
                                fontSize = 12.sp,
                                color = Slate700
                            )
                        }
                        DropdownMenu(
                            expanded = genderMenuExpanded,
                            onDismissRequest = { genderMenuExpanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("All Genders") },
                                onClick = {
                                    onGenderFilterChange("All")
                                    genderMenuExpanded = false
                                }
                            )
                            SampleData.genders.forEach { gen ->
                                DropdownMenuItem(
                                    text = { Text(gen) },
                                    onClick = {
                                        onGenderFilterChange(gen)
                                        genderMenuExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    // Sort Field Dropdown
                    Box {
                        OutlinedButton(
                            onClick = { sortMenuExpanded = true },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.testTag("btn_sort_selector")
                        ) {
                            Icon(Icons.Default.SwapVert, contentDescription = "Sort", modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            val dirSymbol = if (filterState.sortDirection == SortDirection.ASC) "▲" else "▼"
                            Text(
                                text = "Sort: ${filterState.sortField.name} $dirSymbol",
                                fontSize = 12.sp,
                                color = Slate700
                            )
                        }
                        DropdownMenu(
                            expanded = sortMenuExpanded,
                            onDismissRequest = { sortMenuExpanded = false }
                        ) {
                            SortField.values().forEach { field ->
                                DropdownMenuItem(
                                    text = { Text("Sort by ${field.name}") },
                                    onClick = {
                                        onToggleSort(field)
                                        sortMenuExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    // Clear Filters
                    if (filterState.searchQuery.isNotBlank() ||
                        filterState.departmentFilter != "All" ||
                        filterState.yearFilter != "All" ||
                        filterState.genderFilter != "All"
                    ) {
                        OutlinedButton(
                            onClick = onClearFilters,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.testTag("btn_clear_filters")
                        ) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear", modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Clear Filters", fontSize = 12.sp, color = IndigoPrimary)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Student Table
        StudentTable(
            students = students,
            onView = onViewStudent,
            onEdit = onEditStudent,
            onDelete = onDeleteStudent,
            modifier = Modifier.weight(1f, fill = false)
        )
    }
}
