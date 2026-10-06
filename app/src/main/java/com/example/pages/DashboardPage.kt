package com.example.pages

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
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
import com.example.components.StatCard
import com.example.data.Student
import com.example.ui.theme.AmberWarning
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
import com.example.ui.theme.VioletAccent

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DashboardPage(
    students: List<Student>,
    onViewStudent: (Student) -> Unit,
    modifier: Modifier = Modifier
) {
    val totalStudents = students.size
    val totalDepartments = students.map { it.department }.distinct().size

    val firstYearCount = students.count { it.academicYear.contains("1st", ignoreCase = true) }
    val secondYearCount = students.count { it.academicYear.contains("2nd", ignoreCase = true) }
    val thirdYearCount = students.count { it.academicYear.contains("3rd", ignoreCase = true) }
    val finalYearCount = students.count { it.academicYear.contains("Final", ignoreCase = true) }

    val maleCount = students.count { it.gender.equals("Male", ignoreCase = true) }
    val femaleCount = students.count { it.gender.equals("Female", ignoreCase = true) }
    val otherCount = totalStudents - maleCount - femaleCount

    val departmentCounts = students.groupBy { it.department }.mapValues { it.value.size }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Slate50)
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
            .testTag("page_dashboard")
    ) {
        // Welcome Banner
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = IndigoPrimary),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(22.dp)) {
                Text(
                    text = "Welcome to Campus Administration",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Overview of active student enrollments, academic progression, and department capacity.",
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = 13.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Top Statistics Row
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            maxItemsInEachRow = 3
        ) {
            StatCard(
                title = "Total Students",
                value = "$totalStudents",
                icon = Icons.Default.Group,
                iconBgColor = IndigoPrimary,
                subtitle = "Active enrolments",
                modifier = Modifier.weight(1f, fill = false).widthIn(min = 180.dp),
                testTag = "stat_total_students"
            )
            StatCard(
                title = "Total Departments",
                value = "$totalDepartments",
                icon = Icons.Default.Business,
                iconBgColor = SkyInfo,
                subtitle = "Active faculties",
                modifier = Modifier.weight(1f, fill = false).widthIn(min = 180.dp),
                testTag = "stat_total_departments"
            )
            StatCard(
                title = "Final Year",
                value = "$finalYearCount",
                icon = Icons.Default.School,
                iconBgColor = EmeraldSuccess,
                subtitle = "Graduating class",
                modifier = Modifier.weight(1f, fill = false).widthIn(min = 180.dp),
                testTag = "stat_final_year"
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Academic Year Progression Row
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            maxItemsInEachRow = 4
        ) {
            StatCard(
                title = "1st Year",
                value = "$firstYearCount",
                icon = Icons.Default.Timeline,
                iconBgColor = IndigoSecondary,
                modifier = Modifier.weight(1f, fill = false).widthIn(min = 140.dp),
                testTag = "stat_year_1"
            )
            StatCard(
                title = "2nd Year",
                value = "$secondYearCount",
                icon = Icons.Default.Timeline,
                iconBgColor = VioletAccent,
                modifier = Modifier.weight(1f, fill = false).widthIn(min = 140.dp),
                testTag = "stat_year_2"
            )
            StatCard(
                title = "3rd Year",
                value = "$thirdYearCount",
                icon = Icons.Default.Timeline,
                iconBgColor = AmberWarning,
                modifier = Modifier.weight(1f, fill = false).widthIn(min = 140.dp),
                testTag = "stat_year_3"
            )
            StatCard(
                title = "Final Year",
                value = "$finalYearCount",
                icon = Icons.Default.Timeline,
                iconBgColor = EmeraldSuccess,
                modifier = Modifier.weight(1f, fill = false).widthIn(min = 140.dp),
                testTag = "stat_year_4"
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Visualizations Section (Department Distribution & Gender Ratio)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Department-wise Stats Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .weight(1.3f)
                    .fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Department Distribution",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Slate900
                    )
                    Text(
                        text = "Student count across academic departments",
                        fontSize = 12.sp,
                        color = Slate500
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    if (departmentCounts.isEmpty()) {
                        Text("No department data available.", color = Slate500, fontSize = 13.sp)
                    } else {
                        departmentCounts.entries.forEach { entry ->
                            val percent = if (totalStudents > 0) entry.value.toFloat() / totalStudents.toFloat() else 0f
                            Column(modifier = Modifier.padding(vertical = 6.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = entry.key,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Slate700
                                    )
                                    Text(
                                        text = "${entry.value} students (${(percent * 100).toInt()}%)",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = IndigoPrimary
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                LinearProgressIndicator(
                                    progress = { percent },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(8.dp)
                                        .clip(RoundedCornerShape(4.dp)),
                                    color = IndigoPrimary,
                                    trackColor = Slate100
                                )
                            }
                        }
                    }
                }
            }

            // Gender Distribution Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .weight(0.9f)
                    .fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Gender Demographics",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Slate900
                    )
                    Text(
                        text = "Diversity ratio across campus",
                        fontSize = 12.sp,
                        color = Slate500
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    GenderDemographicRow(
                        label = "Female",
                        count = femaleCount,
                        total = totalStudents,
                        color = IndigoSecondary
                    )
                    GenderDemographicRow(
                        label = "Male",
                        count = maleCount,
                        total = totalStudents,
                        color = SkyInfo
                    )
                    if (otherCount > 0) {
                        GenderDemographicRow(
                            label = "Other",
                            count = otherCount,
                            total = totalStudents,
                            color = VioletAccent
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Recent Student Records
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "Recent Student Records",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Slate900
                )
                Text(
                    text = "Latest students admitted to the registry",
                    fontSize = 12.sp,
                    color = Slate500
                )

                Spacer(modifier = Modifier.height(14.dp))

                val recentList = students.take(5)
                if (recentList.isEmpty()) {
                    Text("No records available.", fontSize = 13.sp, color = Slate500)
                } else {
                    recentList.forEachIndexed { idx, st ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(IndigoLight),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = st.name.take(1).uppercase(),
                                    fontWeight = FontWeight.Bold,
                                    color = IndigoPrimary,
                                    fontSize = 14.sp
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = st.name,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 14.sp,
                                    color = Slate900
                                )
                                Text(
                                    text = "${st.studentId} • ${st.department} • ${st.academicYear}",
                                    fontSize = 12.sp,
                                    color = Slate500
                                )
                            }
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = IndigoLight,
                                modifier = Modifier.padding(end = 4.dp)
                            ) {
                                Text(
                                    text = st.rollNumber,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 11.sp,
                                    color = IndigoPrimary,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                        if (idx < recentList.lastIndex) {
                            HorizontalDivider(color = Slate100)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))
    }
}

@Composable
private fun GenderDemographicRow(
    label: String,
    count: Int,
    total: Int,
    color: Color
) {
    val fraction = if (total > 0) count.toFloat() / total.toFloat() else 0f
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(label, fontSize = 13.sp, color = Slate700, fontWeight = FontWeight.Medium)
            Text(
                "$count (${(fraction * 100).toInt()}%)",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = Slate900
            )
        }
        Spacer(modifier = Modifier.height(5.dp))
        LinearProgressIndicator(
            progress = { fraction },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = color,
            trackColor = Slate100
        )
    }
}
