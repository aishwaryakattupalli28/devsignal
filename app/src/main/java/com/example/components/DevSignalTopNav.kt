package com.example.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DevSignalCyan
import com.example.ui.theme.DevSignalEmerald
import com.example.viewmodel.DevSignalScreen

@Composable
fun DevSignalTopNav(
    currentScreen: DevSignalScreen,
    onNavigate: (DevSignalScreen) -> Unit,
    isDarkMode: Boolean,
    onToggleDarkMode: () -> Unit,
    completedTasksCount: Int,
    totalTasksCount: Int,
    modifier: Modifier = Modifier
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        modifier = modifier
            .fillMaxWidth()
            .border(width = 1.dp, color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            // Main Branding Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Logo & Title
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { onNavigate(DevSignalScreen.ANALYZE) }
                        .padding(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(DevSignalCyan, DevSignalEmerald)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Terminal,
                            contentDescription = "DevSignal Signal Logo",
                            tint = Color.Black,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "DevSignal",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 19.sp,
                                fontFamily = FontFamily.Monospace,
                                color = MaterialTheme.colorScheme.onSurface,
                                letterSpacing = (-0.5).sp
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(DevSignalCyan.copy(alpha = 0.15f))
                                    .border(1.dp, DevSignalCyan.copy(alpha = 0.4f), RoundedCornerShape(4.dp))
                                    .padding(horizontal = 5.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "AUDIT",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DevSignalCyan,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        }
                        Text(
                            text = "GitHub Role Fit Intelligence",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Dark / Light Toggle
                IconButton(
                    onClick = onToggleDarkMode,
                    modifier = Modifier
                        .testTag("theme_toggle_button")
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
                ) {
                    Icon(
                        imageVector = if (isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                        contentDescription = if (isDarkMode) "Switch to Light Mode" else "Switch to Dark Mode",
                        tint = if (isDarkMode) DevSignalCyan else Color(0xFFEAB308),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Navigation Tabs Row (Horizontal Scrollable for responsiveness)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                NavTabItem(
                    title = "Analyze",
                    icon = Icons.Default.Search,
                    isSelected = currentScreen == DevSignalScreen.ANALYZE,
                    onClick = { onNavigate(DevSignalScreen.ANALYZE) },
                    testTag = "nav_analyze_tab"
                )

                NavTabItem(
                    title = "Dashboard",
                    icon = Icons.Default.Assessment,
                    isSelected = currentScreen == DevSignalScreen.DASHBOARD,
                    onClick = { onNavigate(DevSignalScreen.DASHBOARD) },
                    testTag = "nav_dashboard_tab"
                )

                NavTabItem(
                    title = "Action Plan",
                    icon = Icons.Default.CheckCircle,
                    isSelected = currentScreen == DevSignalScreen.ACTION_PLAN,
                    badgeCount = "$completedTasksCount/$totalTasksCount",
                    onClick = { onNavigate(DevSignalScreen.ACTION_PLAN) },
                    testTag = "nav_action_plan_tab"
                )

                NavTabItem(
                    title = "Progress",
                    icon = Icons.Default.ShowChart,
                    isSelected = currentScreen == DevSignalScreen.PROGRESS,
                    onClick = { onNavigate(DevSignalScreen.PROGRESS) },
                    testTag = "nav_progress_tab"
                )
            }
        }
    }
}

@Composable
private fun NavTabItem(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    testTag: String,
    badgeCount: String? = null
) {
    val bgModifier = if (isSelected) {
        Modifier
            .background(
                Brush.horizontalGradient(
                    colors = listOf(DevSignalCyan.copy(alpha = 0.2f), DevSignalEmerald.copy(alpha = 0.15f))
                ),
                shape = RoundedCornerShape(20.dp)
            )
            .border(1.dp, DevSignalCyan.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
    } else {
        Modifier
            .background(Color.Transparent, shape = RoundedCornerShape(20.dp))
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .testTag(testTag)
            .clip(RoundedCornerShape(20.dp))
            .then(bgModifier)
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 7.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (isSelected) DevSignalCyan else MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = title,
            fontSize = 13.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
        )

        if (badgeCount != null) {
            Spacer(modifier = Modifier.width(6.dp))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        if (isSelected) DevSignalEmerald.copy(alpha = 0.25f)
                        else MaterialTheme.colorScheme.surfaceVariant
                    )
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = badgeCount,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) DevSignalEmerald else MaterialTheme.colorScheme.onSurfaceVariant,
                    fontFamily = FontFamily.Monospace
                )
            }
        }
    }
}
