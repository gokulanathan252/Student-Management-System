package com.example.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Student
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.IndigoLight
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.IndigoSecondary
import com.example.ui.theme.RoseError
import com.example.ui.theme.SkyInfo
import com.example.ui.theme.Slate100
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate50
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate900

@Composable
fun StudentTable(
    students: List<Student>,
    onView: (Student) -> Unit,
    onEdit: (Student) -> Unit,
    onDelete: (Student) -> Unit,
    modifier: Modifier = Modifier
) {
    if (students.isEmpty()) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "No Student Records Found",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Slate900
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Try adjusting your search query or department/year filters.",
                    fontSize = 13.sp,
                    color = Slate500
                )
            }
        }
        return
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(scrollState)
        ) {
            // Table Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .background(Slate50)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                TableHeaderCell("Student ID", 100.dp)
                TableHeaderCell("Name", 160.dp)
                TableHeaderCell("Roll No", 120.dp)
                TableHeaderCell("Department", 160.dp)
                TableHeaderCell("Year", 100.dp)
                TableHeaderCell("Email", 200.dp)
                TableHeaderCell("Phone", 130.dp)
                TableHeaderCell("Actions", 130.dp, Alignment.Center)
            }

            HorizontalDivider(color = Slate200)

            // Table Rows
            students.forEachIndexed { index, student ->
                val rowBg = if (index % 2 == 1) Slate50.copy(alpha = 0.5f) else Color.White

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .background(rowBg)
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                        .testTag("student_row_${student.studentId}")
                ) {
                    // ID
                    Box(modifier = Modifier.width(100.dp)) {
                        Text(
                            text = student.studentId,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = IndigoPrimary
                        )
                    }

                    // Name
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.width(160.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .clip(CircleShape)
                                .background(IndigoLight),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = student.name.take(1).uppercase(),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = IndigoPrimary
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = student.name,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = Slate900
                        )
                    }

                    // Roll No
                    Box(modifier = Modifier.width(120.dp)) {
                        Text(
                            text = student.rollNumber,
                            fontSize = 13.sp,
                            color = Slate700
                        )
                    }

                    // Department badge
                    Box(modifier = Modifier.width(160.dp)) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Slate100,
                            modifier = Modifier.padding(vertical = 2.dp)
                        ) {
                            Text(
                                text = student.department,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = Slate700,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    // Academic Year badge
                    Box(modifier = Modifier.width(100.dp)) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = IndigoLight,
                            modifier = Modifier.padding(vertical = 2.dp)
                        ) {
                            Text(
                                text = student.academicYear,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = IndigoPrimary,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    // Email
                    Box(modifier = Modifier.width(200.dp)) {
                        Text(
                            text = student.email,
                            fontSize = 13.sp,
                            color = Slate500
                        )
                    }

                    // Phone
                    Box(modifier = Modifier.width(130.dp)) {
                        Text(
                            text = student.phone,
                            fontSize = 13.sp,
                            color = Slate500
                        )
                    }

                    // Action Buttons
                    Row(
                        modifier = Modifier.width(130.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = { onView(student) },
                            modifier = Modifier
                                .size(32.dp)
                                .testTag("btn_view_${student.studentId}")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Visibility,
                                contentDescription = "View Details",
                                tint = SkyInfo,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        IconButton(
                            onClick = { onEdit(student) },
                            modifier = Modifier
                                .size(32.dp)
                                .testTag("btn_edit_${student.studentId}")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit Student",
                                tint = IndigoSecondary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        IconButton(
                            onClick = { onDelete(student) },
                            modifier = Modifier
                                .size(32.dp)
                                .testTag("btn_delete_${student.studentId}")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete Student",
                                tint = RoseError,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
                HorizontalDivider(color = Slate200.copy(alpha = 0.5f))
            }
        }
    }
}

@Composable
private fun TableHeaderCell(
    text: String,
    width: androidx.compose.ui.unit.Dp,
    alignment: Alignment = Alignment.CenterStart
) {
    Box(
        modifier = Modifier.width(width),
        contentAlignment = alignment
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Slate500
        )
    }
}
