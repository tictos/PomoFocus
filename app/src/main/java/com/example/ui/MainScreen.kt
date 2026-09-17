package com.example.ui

import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.CheckCircleOutline
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.StatsScreen
import com.example.ui.screens.TasksScreen
import com.example.ui.screens.TimerScreen
import com.example.ui.theme.AmberGoldLight
import com.example.ui.theme.AmberGoldPrimary
import com.example.ui.theme.ObsidianBg
import com.example.ui.theme.SurfaceCardElevated
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.PomodoroViewModel
import com.example.ui.viewmodel.UiEvent
import kotlinx.coroutines.flow.collectLatest

enum class MainTab(
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val tag: String
) {
    TIMER("Timer", Icons.Filled.Timer, Icons.Outlined.Timer, "tab_timer"),
    TASKS("Tasks", Icons.Filled.CheckCircle, Icons.Outlined.CheckCircleOutline, "tab_tasks"),
    STATS("Stats", Icons.Filled.BarChart, Icons.Outlined.BarChart, "tab_stats"),
    SETTINGS("Settings", Icons.Filled.Settings, Icons.Outlined.Settings, "tab_settings")
}

@Composable
fun MainScreen(
    viewModel: PomodoroViewModel = viewModel()
) {
    var selectedTab by remember { mutableStateOf(MainTab.TIMER) }
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.uiEvents.collectLatest { event ->
            when (event) {
                is UiEvent.ShowToast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
                is UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(
                        message = event.message,
                        actionLabel = event.actionLabel
                    )
                }
                is UiEvent.SessionFinished -> {
                    // Handled gracefully with audio/vibration & notification
                }
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = ObsidianBg,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier
                    .padding(bottom = 80.dp)
                    .testTag("app_snackbar_host")
            ) { data ->
                Snackbar(
                    snackbarData = data,
                    containerColor = SurfaceCardElevated,
                    contentColor = TextPrimary,
                    actionColor = AmberGoldPrimary
                )
            }
        },
        bottomBar = {
            NavigationBar(
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .testTag("bottom_navigation_bar"),
                containerColor = SurfaceDark,
                contentColor = TextSecondary,
                tonalElevation = 8.dp
            ) {
                MainTab.entries.forEach { tab ->
                    val isSelected = selectedTab == tab
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { selectedTab = tab },
                        icon = {
                            Icon(
                                imageVector = if (isSelected) tab.selectedIcon else tab.unselectedIcon,
                                contentDescription = tab.label,
                                modifier = Modifier.size(22.dp)
                            )
                        },
                        label = {
                            Text(
                                text = tab.label,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = AmberGoldPrimary,
                            selectedTextColor = AmberGoldLight,
                            indicatorColor = AmberGoldPrimary.copy(alpha = 0.18f),
                            unselectedIconColor = TextMuted,
                            unselectedTextColor = TextMuted
                        ),
                        modifier = Modifier.testTag(tab.tag)
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AnimatedContent(
                targetState = selectedTab,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "TabContentAnimation"
            ) { targetTab ->
                when (targetTab) {
                    MainTab.TIMER -> TimerScreen(
                        viewModel = viewModel,
                        onNavigateToTasks = { selectedTab = MainTab.TASKS }
                    )
                    MainTab.TASKS -> TasksScreen(
                        viewModel = viewModel,
                        onStartFocusOnTask = { selectedTab = MainTab.TIMER }
                    )
                    MainTab.STATS -> StatsScreen(
                        viewModel = viewModel
                    )
                    MainTab.SETTINGS -> SettingsScreen(
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}

