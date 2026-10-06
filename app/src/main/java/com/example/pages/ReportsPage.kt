package com.example.pages

import android.content.Context
import android.content.Intent
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Student
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.IndigoLight
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.IndigoSecondary
import com.example.ui.theme.SkyInfo
import com.example.ui.theme.Slate100
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate50
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate900
import com.example.ui.theme.VioletAccent

@Composable
fun ReportsPage(
    students: List<Student>,
    onShowMessage: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val totalCount = students.size
    val deptMap = students.groupBy { it.department }.mapValues { it.value.size }
    val yearMap = students.groupBy { it.academicYear }.mapValues { it.value.size }
    val genderMap = students.groupBy { it.gender }.mapValues { it.value.size }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Slate50)
            .padding(20.dp)
            .verticalScroll(rememberScrollState())
            .testTag("page_reports")
    ) {
        // Header & Actions
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Institutional Reports",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Slate900
                )
                Text(
                    text = "Aggregated statistics, demographic counts, and export tools",
                    fontSize = 12.sp,
                    color = Slate500
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                // Export CSV Button
                Button(
                    onClick = {
                        val csv = generateCsv(students)
                        shareOrSaveFile(context, "students_report.csv", csv, "text/csv")
                        onShowMessage("CSV report generated with ${students.size} student records.")
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.testTag("btn_export_csv")
                ) {
                    Icon(
                        imageVector = Icons.Default.Download,
                        contentDescription = "Export CSV",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Export CSV", fontWeight = FontWeight.SemiBold)
                }

                // Print Report Button
                OutlinedButton(
                    onClick = {
                        val summaryText = buildSummaryText(students, deptMap, yearMap, genderMap)
                        shareOrSaveFile(context, "student_summary_report.txt", summaryText, "text/plain")
                        onShowMessage("Report ready for printing / sharing.")
                    },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.testTag("btn_print_report")
                ) {
                    Icon(
                        imageVector = Icons.Default.Print,
                        contentDescription = "Print Report",
                        tint = Slate700,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Print / Share", color = Slate700, fontWeight = FontWeight.SemiBold)
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Total Students Summary Bar
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(IndigoPrimary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.School,
                        contentDescription = "Graduation",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Total Active Enrolment",
                        fontSize = 13.sp,
                        color = Slate500,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "$totalCount Students Registered",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate900
                    )
                }
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = EmeraldSuccess.copy(alpha = 0.12f)
                ) {
                    Text(
                        text = "Verified Active",
                        color = EmeraldSuccess,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Breakdown Cards
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Department breakdown
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Department Breakdown",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Slate900
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    if (deptMap.isEmpty()) {
                        Text("No data available", color = Slate500, fontSize = 12.sp)
                    } else {
                        deptMap.entries.forEach { entry ->
                            val pct = if (totalCount > 0) entry.value.toFloat() / totalCount.toFloat() else 0f
                            Column(modifier = Modifier.padding(vertical = 4.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(entry.key, fontSize = 12.sp, color = Slate700)
                                    Text("${entry.value} (${(pct * 100).toInt()}%)", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = IndigoPrimary)
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                LinearProgressIndicator(
                                    progress = { pct },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(6.dp)
                                        .clip(RoundedCornerShape(3.dp)),
                                    color = IndigoPrimary,
                                    trackColor = Slate100
                                )
                            }
                        }
                    }
                }
            }

            // Year Breakdown
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Academic Year Breakdown",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Slate900
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    if (yearMap.isEmpty()) {
                        Text("No data available", color = Slate500, fontSize = 12.sp)
                    } else {
                        yearMap.entries.forEach { entry ->
                            val pct = if (totalCount > 0) entry.value.toFloat() / totalCount.toFloat() else 0f
                            Column(modifier = Modifier.padding(vertical = 4.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(entry.key, fontSize = 12.sp, color = Slate700)
                                    Text("${entry.value} (${(pct * 100).toInt()}%)", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = IndigoSecondary)
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                LinearProgressIndicator(
                                    progress = { pct },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(6.dp)
                                        .clip(RoundedCornerShape(3.dp)),
                                    color = IndigoSecondary,
                                    trackColor = Slate100
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Gender Demographic Breakdown
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "Gender Demographics",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Slate900
                )
                Spacer(modifier = Modifier.height(12.dp))

                genderMap.entries.forEach { entry ->
                    val pct = if (totalCount > 0) entry.value.toFloat() / totalCount.toFloat() else 0f
                    Column(modifier = Modifier.padding(vertical = 4.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(entry.key, fontSize = 12.sp, color = Slate700)
                            Text("${entry.value} (${(pct * 100).toInt()}%)", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = SkyInfo)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        LinearProgressIndicator(
                            progress = { pct },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = SkyInfo,
                            trackColor = Slate100
                        )
                    }
                }
            }
        }
    }
}

private fun generateCsv(students: List<Student>): String {
    val sb = StringBuilder()
    sb.append("Student ID,Name,Roll Number,Gender,DOB,Department,Academic Year,Phone,Email,Address\n")
    students.forEach { s ->
        val safeAddress = "\"${s.address.replace("\"", "\"\"")}\""
        sb.append("${s.studentId},\"${s.name}\",${s.rollNumber},${s.gender},${s.dob},\"${s.department}\",${s.academicYear},${s.phone},${s.email},$safeAddress\n")
    }
    return sb.toString()
}

private fun buildSummaryText(
    students: List<Student>,
    deptMap: Map<String, Int>,
    yearMap: Map<String, Int>,
    genderMap: Map<String, Int>
): String {
    val sb = StringBuilder()
    sb.append("=========================================\n")
    sb.append("STUDENT INFORMATION MANAGEMENT SYSTEM\n")
    sb.append("OFFICIAL ENROLMENT & ACADEMIC REPORT\n")
    sb.append("=========================================\n\n")
    sb.append("Total Registered Students: ${students.size}\n\n")

    sb.append("--- DEPARTMENT SUMMARY ---\n")
    deptMap.forEach { (dept, cnt) ->
        sb.append("• $dept: $cnt students\n")
    }
    sb.append("\n--- ACADEMIC YEAR BREAKDOWN ---\n")
    yearMap.forEach { (yr, cnt) ->
        sb.append("• $yr: $cnt students\n")
    }
    sb.append("\n--- GENDER DISTRIBUTION ---\n")
    genderMap.forEach { (g, cnt) ->
        sb.append("• $g: $cnt students\n")
    }
    sb.append("\n=========================================\n")
    return sb.toString()
}

private fun shareOrSaveFile(context: Context, filename: String, content: String, mimeType: String) {
    val sendIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, content)
        putExtra(Intent.EXTRA_TITLE, filename)
        type = mimeType
    }
    val shareIntent = Intent.createChooser(sendIntent, "Export or Share Report")
    shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    context.startActivity(shareIntent)
}
