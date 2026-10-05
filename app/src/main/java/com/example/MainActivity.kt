package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.components.DevSignalTopNav
import com.example.ui.screens.ActionPlanScreen
import com.example.ui.screens.AnalyzeScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.ProgressScreen
import com.example.ui.theme.DevSignalCyan
import com.example.ui.theme.DevSignalTheme
import com.example.viewmodel.DevSignalScreen
import com.example.viewmodel.DevSignalViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DevSignalApp()
        }
    }
}

@Composable
fun DevSignalApp(viewModel: DevSignalViewModel = viewModel()) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val isDarkMode by viewModel.isDarkMode.collectAsState()
    val actionItems by viewModel.actionItems.collectAsState()
    val toastMessage by viewModel.toastMessage.collectAsState()

    val completedTasksCount = actionItems.count { it.completed }
    val totalTasksCount = actionItems.size

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(toastMessage) {
        toastMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.dismissToast()
        }
    }

    // Android Hardware / Gesture BackHandler
    BackHandler(enabled = currentScreen != DevSignalScreen.ANALYZE) {
        if (currentScreen == DevSignalScreen.ACTION_PLAN || currentScreen == DevSignalScreen.PROGRESS) {
            viewModel.navigateTo(DevSignalScreen.DASHBOARD)
        } else {
            viewModel.navigateTo(DevSignalScreen.ANALYZE)
        }
    }

    DevSignalTheme(darkTheme = isDarkMode) {
        Scaffold(
            topBar = {
                Box(modifier = Modifier.padding(top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding())) {
                    DevSignalTopNav(
                        currentScreen = currentScreen,
                        onNavigate = { viewModel.navigateTo(it) },
                        isDarkMode = isDarkMode,
                        onToggleDarkMode = { viewModel.toggleDarkMode() },
                        completedTasksCount = completedTasksCount,
                        totalTasksCount = totalTasksCount
                    )
                }
            },
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState) { data ->
                    Box(
                        modifier = Modifier
                            .padding(16.dp)
                            .border(1.dp, DevSignalCyan, RoundedCornerShape(10.dp))
                    ) {
                        Snackbar(
                            containerColor = Color(0xFF0F172A),
                            contentColor = Color(0xFFF8FAFC),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(text = data.visuals.message, fontSize = 13.sp)
                        }
                    }
                }
            },
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = innerPadding.calculateTopPadding())
                    .padding(bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding())
            ) {
                AnimatedContent(
                    targetState = currentScreen,
                    transitionSpec = {
                        (slideInHorizontally { width -> width / 3 } + fadeIn())
                            .togetherWith(slideOutHorizontally { width -> -width / 3 } + fadeOut())
                    },
                    label = "screen_transition"
                ) { screen ->
                    when (screen) {
                        DevSignalScreen.ANALYZE -> AnalyzeScreen(viewModel = viewModel)
                        DevSignalScreen.DASHBOARD -> DashboardScreen(viewModel = viewModel)
                        DevSignalScreen.ACTION_PLAN -> ActionPlanScreen(viewModel = viewModel)
                        DevSignalScreen.PROGRESS -> ProgressScreen(viewModel = viewModel)
                    }
                }
            }
        }
    }
}
