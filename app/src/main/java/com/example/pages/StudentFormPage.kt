package com.example.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import com.example.data.SampleData
import com.example.data.Student
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.RoseError
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate50
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate900

@Composable
fun StudentFormPage(
    editingStudent: Student?,
    onSave: (Student, Boolean, () -> Unit, (String) -> Unit) -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isEditMode = editingStudent != null

    var studentId by remember { mutableStateOf(editingStudent?.studentId ?: "") }
    var name by remember { mutableStateOf(editingStudent?.name ?: "") }
    var rollNumber by remember { mutableStateOf(editingStudent?.rollNumber ?: "") }
    var gender by remember { mutableStateOf(editingStudent?.gender ?: "Male") }
    var dob by remember { mutableStateOf(editingStudent?.dob ?: "2004-01-15") }
    var department by remember { mutableStateOf(editingStudent?.department ?: "Computer Science") }
    var academicYear by remember { mutableStateOf(editingStudent?.academicYear ?: "1st Year") }
    var phone by remember { mutableStateOf(editingStudent?.phone ?: "") }
    var email by remember { mutableStateOf(editingStudent?.email ?: "") }
    var address by remember { mutableStateOf(editingStudent?.address ?: "") }

    var errorMessage by remember { mutableStateOf<String?>(null) }
    var deptExpanded by remember { mutableStateOf(false) }
    var yearExpanded by remember { mutableStateOf(false) }
    var genderExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Slate50)
            .padding(20.dp)
            .verticalScroll(rememberScrollState())
            .testTag("page_student_form")
    ) {
        // Top Back Row
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(
                onClick = onCancel,
                modifier = Modifier.testTag("btn_form_back")
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Slate900
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = if (isEditMode) "Edit Student Information" else "Enroll New Student",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Slate900
                )
                Text(
                    text = if (isEditMode) "Update existing student profile and academic details" else "Fill in all required fields to register a student in the system",
                    fontSize = 12.sp,
                    color = Slate500
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Form Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                if (errorMessage != null) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = RoseError.copy(alpha = 0.1f),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                    ) {
                        Text(
                            text = errorMessage!!,
                            color = RoseError,
                            fontSize = 13.sp,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }

                // Row 1: Student ID & Roll Number
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        FormFieldLabel("Student ID *")
                        OutlinedTextField(
                            value = studentId,
                            onValueChange = { if (!isEditMode) studentId = it.trim() },
                            enabled = !isEditMode,
                            placeholder = { Text("e.g. STU1009") },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = IndigoPrimary,
                                unfocusedBorderColor = Slate200
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("input_student_id")
                        )
                        if (isEditMode) {
                            Text(
                                text = "Student ID cannot be modified after registration",
                                fontSize = 11.sp,
                                color = Slate500,
                                modifier = Modifier.padding(top = 2.dp)
                            )
                        }
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        FormFieldLabel("Roll Number *")
                        OutlinedTextField(
                            value = rollNumber,
                            onValueChange = { rollNumber = it.trim() },
                            placeholder = { Text("e.g. CS-2025-10") },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = IndigoPrimary,
                                unfocusedBorderColor = Slate200
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("input_roll_number")
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Row 2: Full Name & Date of Birth
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        FormFieldLabel("Full Name *")
                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            placeholder = { Text("e.g. Jane Doe") },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = IndigoPrimary,
                                unfocusedBorderColor = Slate200
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("input_student_name")
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        FormFieldLabel("Date of Birth (YYYY-MM-DD) *")
                        OutlinedTextField(
                            value = dob,
                            onValueChange = { dob = it },
                            placeholder = { Text("2004-05-18") },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = IndigoPrimary,
                                unfocusedBorderColor = Slate200
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("input_dob")
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Row 3: Department, Year, Gender Dropdowns
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Department
                    Column(modifier = Modifier.weight(1f)) {
                        FormFieldLabel("Department *")
                        Box {
                            OutlinedButton(
                                onClick = { deptExpanded = true },
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("dropdown_form_dept")
                            ) {
                                Text(department, color = Slate900, fontSize = 13.sp)
                            }
                            DropdownMenu(
                                expanded = deptExpanded,
                                onDismissRequest = { deptExpanded = false }
                            ) {
                                SampleData.departments.forEach { d ->
                                    DropdownMenuItem(
                                        text = { Text(d) },
                                        onClick = {
                                            department = d
                                            deptExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    // Academic Year
                    Column(modifier = Modifier.weight(1f)) {
                        FormFieldLabel("Academic Year *")
                        Box {
                            OutlinedButton(
                                onClick = { yearExpanded = true },
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("dropdown_form_year")
                            ) {
                                Text(academicYear, color = Slate900, fontSize = 13.sp)
                            }
                            DropdownMenu(
                                expanded = yearExpanded,
                                onDismissRequest = { yearExpanded = false }
                            ) {
                                SampleData.academicYears.forEach { yr ->
                                    DropdownMenuItem(
                                        text = { Text(yr) },
                                        onClick = {
                                            academicYear = yr
                                            yearExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    // Gender
                    Column(modifier = Modifier.weight(1f)) {
                        FormFieldLabel("Gender *")
                        Box {
                            OutlinedButton(
                                onClick = { genderExpanded = true },
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("dropdown_form_gender")
                            ) {
                                Text(gender, color = Slate900, fontSize = 13.sp)
                            }
                            DropdownMenu(
                                expanded = genderExpanded,
                                onDismissRequest = { genderExpanded = false }
                            ) {
                                SampleData.genders.forEach { g ->
                                    DropdownMenuItem(
                                        text = { Text(g) },
                                        onClick = {
                                            gender = g
                                            genderExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Row 4: Phone & Email
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        FormFieldLabel("Email Address *")
                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it.trim() },
                            placeholder = { Text("student@university.edu") },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = IndigoPrimary,
                                unfocusedBorderColor = Slate200
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("input_email")
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        FormFieldLabel("Phone Number *")
                        OutlinedTextField(
                            value = phone,
                            onValueChange = { phone = it.trim() },
                            placeholder = { Text("555-012-3456") },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = IndigoPrimary,
                                unfocusedBorderColor = Slate200
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("input_phone")
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Address
                FormFieldLabel("Residential Address")
                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    placeholder = { Text("123 University Ave, City, State") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = IndigoPrimary,
                        unfocusedBorderColor = Slate200
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(90.dp)
                        .testTag("input_address")
                )

                Spacer(modifier = Modifier.height(28.dp))

                // Submit & Cancel Buttons
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedButton(
                        onClick = onCancel,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.testTag("btn_form_cancel")
                    ) {
                        Text("Cancel", color = Slate700)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Button(
                        onClick = {
                            val newOrUpdated = Student(
                                studentId = studentId,
                                name = name,
                                rollNumber = rollNumber,
                                gender = gender,
                                dob = dob,
                                department = department,
                                academicYear = academicYear,
                                phone = phone,
                                email = email,
                                address = address
                            )
                            onSave(newOrUpdated, isEditMode, {
                                errorMessage = null
                            }, { err ->
                                errorMessage = err
                            })
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.testTag("btn_form_submit")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Save,
                            contentDescription = "Save",
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(if (isEditMode) "Update Student" else "Save Student Record")
                    }
                }
            }
        }
    }
}

@Composable
private fun FormFieldLabel(label: String) {
    Text(
        text = label,
        fontSize = 12.sp,
        fontWeight = FontWeight.SemiBold,
        color = Slate700,
        modifier = Modifier.padding(bottom = 6.dp)
    )
}
