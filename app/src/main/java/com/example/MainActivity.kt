package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.components.AppHeader
import com.example.components.AppSidebar
import com.example.components.ConfirmDialog
import com.example.components.StudentDetailsDialog
import com.example.pages.DashboardPage
import com.example.pages.LoginPage
import com.example.pages.ReportsPage
import com.example.pages.SettingsPage
import com.example.pages.StudentFormPage
import com.example.pages.StudentsPage
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate50
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate900
import com.example.viewmodel.Screen
import com.example.viewmodel.StudentViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val viewModel: StudentViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainAppScreen(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MainAppScreen(viewModel: StudentViewModel) {
    val isLoggedIn by viewModel.isLoggedIn.collectAsStateWithLifecycle()
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val userMessage by viewModel.userMessage.collectAsStateWithLifecycle()

    val rawStudents by viewModel.rawStudents.collectAsStateWithLifecycle()
    val filteredStudents by viewModel.filteredStudents.collectAsStateWithLifecycle()
    val filterState by viewModel.filterState.collectAsStateWithLifecycle()

    val viewingStudent by viewModel.viewingStudent.collectAsStateWithLifecycle()
    val editingStudent by viewModel.editingStudent.collectAsStateWithLifecycle()
    val studentPendingDelete by viewModel.studentPendingDelete.collectAsStateWithLifecycle()
    val showClearAllConfirm by viewModel.showClearAllConfirm.collectAsStateWithLifecycle()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Handle back button on sub-screens
    BackHandler(enabled = isLoggedIn && currentScreen != Screen.DASHBOARD) {
        if (drawerState.isOpen) {
            scope.launch { drawerState.close() }
        } else if (currentScreen == Screen.ADD_STUDENT) {
            viewModel.cancelEdit()
        } else {
            viewModel.navigateTo(Screen.DASHBOARD)
        }
    }

    if (!isLoggedIn) {
        LoginPage(
            onLogin = { u, p -> viewModel.login(u, p) },
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
        )
        return
    }

    // Modal Details Dialog
    if (viewingStudent != null) {
        StudentDetailsDialog(
            student = viewingStudent!!,
            onDismiss = { viewModel.closeStudentDetails() }
        )
    }

    // Delete Confirmation Dialog
    if (studentPendingDelete != null) {
        ConfirmDialog(
            title = "Delete Student Record",
            message = "Are you sure you want to delete ${studentPendingDelete!!.name} (${studentPendingDelete!!.studentId})? This action cannot be undone.",
            confirmButtonText = "Delete Student",
            isDestructive = true,
            onConfirm = { viewModel.confirmDeleteStudent() },
            onDismiss = { viewModel.dismissDeleteDialog() }
        )
    }

    // Clear All Confirmation Dialog
    if (showClearAllConfirm) {
        ConfirmDialog(
            title = "Clear All Student Records",
            message = "Are you sure you want to permanently erase ALL student records from the database? You can restore sample records anytime from settings.",
            confirmButtonText = "Clear All",
            isDestructive = true,
            onConfirm = { viewModel.confirmClearAllData() },
            onDismiss = { viewModel.dismissClearAllData() }
        )
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val isWideScreen = maxWidth >= 840.dp

        if (isWideScreen) {
            // Desktop / Tablet layout: Persistent Left Sidebar + Main Content Area
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Slate50)
                    .statusBarsPadding()
                    .navigationBarsPadding()
            ) {
                AppSidebar(
                    currentScreen = currentScreen,
                    onNavigate = { screen -> viewModel.navigateTo(screen) },
                    onLogout = { viewModel.logout() }
                )

                Column(modifier = Modifier.weight(1f).fillMaxSize()) {
                    AppHeader(
                        title = when (currentScreen) {
                            Screen.DASHBOARD -> "Administration Dashboard"
                            Screen.STUDENTS -> "Student Records"
                            Screen.ADD_STUDENT -> if (editingStudent != null) "Edit Student Profile" else "Add New Student"
                            Screen.REPORTS -> "Academic & Enrolment Reports"
                            Screen.SETTINGS -> "Application Settings"
                            Screen.LOGIN -> "Login"
                        },
                        subtitle = "Student Management System • University Information Portal",
                        showMenuButton = false
                    )

                    // User Message Banner
                    AnimatedVisibility(
                        visible = userMessage != null,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        if (userMessage != null) {
                            Surface(
                                color = EmeraldSuccess,
                                shape = RoundedCornerShape(0.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                                ) {
                                    Text(
                                        text = userMessage!!,
                                        color = Color.White,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 13.sp,
                                        modifier = Modifier.weight(1f)
                                    )
                                    IconButton(onClick = { viewModel.clearUserMessage() }) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "Dismiss",
                                            tint = Color.White
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Content Page
                    Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                        when (currentScreen) {
                            Screen.DASHBOARD -> DashboardPage(
                                students = rawStudents,
                                onViewStudent = { viewModel.viewStudentDetails(it) }
                            )
                            Screen.STUDENTS -> StudentsPage(
                                students = filteredStudents,
                                filterState = filterState,
                                onSearchChange = { viewModel.setSearchQuery(it) },
                                onDepartmentFilterChange = { viewModel.setDepartmentFilter(it) },
                                onYearFilterChange = { viewModel.setYearFilter(it) },
                                onGenderFilterChange = { viewModel.setGenderFilter(it) },
                                onToggleSort = { viewModel.toggleSort(it) },
                                onClearFilters = { viewModel.clearFilters() },
                                onAddNewStudent = { viewModel.navigateTo(Screen.ADD_STUDENT) },
                                onViewStudent = { viewModel.viewStudentDetails(it) },
                                onEditStudent = { viewModel.openEditStudent(it) },
                                onDeleteStudent = { viewModel.requestDeleteStudent(it) }
                            )
                            Screen.ADD_STUDENT -> StudentFormPage(
                                editingStudent = editingStudent,
                                onSave = { st, editMode, onSucc, onErr ->
                                    viewModel.saveStudent(st, editMode, onSucc, onErr)
                                },
                                onCancel = { viewModel.cancelEdit() }
                            )
                            Screen.REPORTS -> ReportsPage(
                                students = rawStudents,
                                onShowMessage = { viewModel.showMessage(it) }
                            )
                            Screen.SETTINGS -> SettingsPage(
                                onClearAllData = { viewModel.requestClearAllData() },
                                onRestoreSampleData = { viewModel.restoreSampleData() }
                            )
                            Screen.LOGIN -> {}
                        }
                    }
                }
            }
        } else {
            // Mobile layout: Modal Drawer + Top App Bar + Bottom Navigation
            ModalNavigationDrawer(
                drawerState = drawerState,
                drawerContent = {
                    ModalDrawerSheet {
                        AppSidebar(
                            currentScreen = currentScreen,
                            onNavigate = { screen ->
                                viewModel.navigateTo(screen)
                                scope.launch { drawerState.close() }
                            },
                            onLogout = {
                                viewModel.logout()
                                scope.launch { drawerState.close() }
                            }
                        )
                    }
                }
            ) {
                Scaffold(
                    topBar = {
                        AppHeader(
                            title = when (currentScreen) {
                                Screen.DASHBOARD -> "Dashboard"
                                Screen.STUDENTS -> "Students"
                                Screen.ADD_STUDENT -> if (editingStudent != null) "Edit Student" else "New Student"
                                Screen.REPORTS -> "Reports"
                                Screen.SETTINGS -> "Settings"
                                Screen.LOGIN -> "Login"
                            },
                            subtitle = "StudentHub Admin",
                            showMenuButton = true,
                            onMenuClick = {
                                scope.launch {
                                    if (drawerState.isClosed) drawerState.open() else drawerState.close()
                                }
                            },
                            modifier = Modifier.statusBarsPadding()
                        )
                    },
                    bottomBar = {
                        NavigationBar(
                            containerColor = Color.White,
                            contentColor = Slate900,
                            modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars)
                        ) {
                            NavigationBarItem(
                                selected = currentScreen == Screen.DASHBOARD,
                                onClick = { viewModel.navigateTo(Screen.DASHBOARD) },
                                icon = { Icon(Icons.Default.Dashboard, contentDescription = "Dashboard") },
                                label = { Text("Dashboard", fontSize = 11.sp) },
                                colors = NavigationBarItemDefaults.colors(selectedIconColor = IndigoPrimary),
                                modifier = Modifier.testTag("bottom_nav_dashboard")
                            )
                            NavigationBarItem(
                                selected = currentScreen == Screen.STUDENTS,
                                onClick = { viewModel.navigateTo(Screen.STUDENTS) },
                                icon = { Icon(Icons.Default.Group, contentDescription = "Students") },
                                label = { Text("Students", fontSize = 11.sp) },
                                colors = NavigationBarItemDefaults.colors(selectedIconColor = IndigoPrimary),
                                modifier = Modifier.testTag("bottom_nav_students")
                            )
                            NavigationBarItem(
                                selected = currentScreen == Screen.ADD_STUDENT,
                                onClick = { viewModel.navigateTo(Screen.ADD_STUDENT) },
                                icon = { Icon(Icons.Default.PersonAdd, contentDescription = "Add") },
                                label = { Text("Add", fontSize = 11.sp) },
                                colors = NavigationBarItemDefaults.colors(selectedIconColor = IndigoPrimary),
                                modifier = Modifier.testTag("bottom_nav_add")
                            )
                            NavigationBarItem(
                                selected = currentScreen == Screen.REPORTS,
                                onClick = { viewModel.navigateTo(Screen.REPORTS) },
                                icon = { Icon(Icons.Default.Assessment, contentDescription = "Reports") },
                                label = { Text("Reports", fontSize = 11.sp) },
                                colors = NavigationBarItemDefaults.colors(selectedIconColor = IndigoPrimary),
                                modifier = Modifier.testTag("bottom_nav_reports")
                            )
                            NavigationBarItem(
                                selected = currentScreen == Screen.SETTINGS,
                                onClick = { viewModel.navigateTo(Screen.SETTINGS) },
                                icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
                                label = { Text("Settings", fontSize = 11.sp) },
                                colors = NavigationBarItemDefaults.colors(selectedIconColor = IndigoPrimary),
                                modifier = Modifier.testTag("bottom_nav_settings")
                            )
                        }
                    }
                ) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .background(Slate50)
                    ) {
                        AnimatedVisibility(
                            visible = userMessage != null,
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            if (userMessage != null) {
                                Surface(
                                    color = EmeraldSuccess,
                                    shape = RoundedCornerShape(0.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                                    ) {
                                        Text(
                                            text = userMessage!!,
                                            color = Color.White,
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 12.sp,
                                            modifier = Modifier.weight(1f)
                                        )
                                        IconButton(onClick = { viewModel.clearUserMessage() }) {
                                            Icon(
                                                imageVector = Icons.Default.Close,
                                                contentDescription = "Dismiss",
                                                tint = Color.White
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                            when (currentScreen) {
                                Screen.DASHBOARD -> DashboardPage(
                                    students = rawStudents,
                                    onViewStudent = { viewModel.viewStudentDetails(it) }
                                )
                                Screen.STUDENTS -> StudentsPage(
                                    students = filteredStudents,
                                    filterState = filterState,
                                    onSearchChange = { viewModel.setSearchQuery(it) },
                                    onDepartmentFilterChange = { viewModel.setDepartmentFilter(it) },
                                    onYearFilterChange = { viewModel.setYearFilter(it) },
                                    onGenderFilterChange = { viewModel.setGenderFilter(it) },
                                    onToggleSort = { viewModel.toggleSort(it) },
                                    onClearFilters = { viewModel.clearFilters() },
                                    onAddNewStudent = { viewModel.navigateTo(Screen.ADD_STUDENT) },
                                    onViewStudent = { viewModel.viewStudentDetails(it) },
                                    onEditStudent = { viewModel.openEditStudent(it) },
                                    onDeleteStudent = { viewModel.requestDeleteStudent(it) }
                                )
                                Screen.ADD_STUDENT -> StudentFormPage(
                                    editingStudent = editingStudent,
                                    onSave = { st, editMode, onSucc, onErr ->
                                        viewModel.saveStudent(st, editMode, onSucc, onErr)
                                    },
                                    onCancel = { viewModel.cancelEdit() }
                                )
                                Screen.REPORTS -> ReportsPage(
                                    students = rawStudents,
                                    onShowMessage = { viewModel.showMessage(it) }
                                )
                                Screen.SETTINGS -> SettingsPage(
                                    onClearAllData = { viewModel.requestClearAllData() },
                                    onRestoreSampleData = { viewModel.restoreSampleData() }
                                )
                                Screen.LOGIN -> {}
                            }
                        }
                    }
                }
            }
        }
    }
}
