package com.example.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material.icons.filled.PlaylistAddCheck
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.components.ActionPlanItemRow
import com.example.model.ActionItem
import com.example.model.Priority
import com.example.ui.theme.DevSignalCyan
import com.example.ui.theme.DevSignalEmerald
import com.example.viewmodel.DevSignalViewModel

@Composable
fun ActionPlanScreen(
    viewModel: DevSignalViewModel,
    modifier: Modifier = Modifier
) {
    val actionItems by viewModel.actionItems.collectAsState()
    val profile by viewModel.currentProfile.collectAsState()
    var selectedFilter by remember { mutableStateOf<Priority?>(null) }

    val completedCount = actionItems.count { it.completed }
    val totalCount = actionItems.size
    val completionRatio = if (totalCount > 0) completedCount.toFloat() / totalCount.toFloat() else 0f
    val animatedProgress by animateFloatAsState(targetValue = completionRatio, label = "plan_progress")

    val potentialBoost = actionItems.filter { !it.completed }.sumOf { it.scoreBoost }

    val filteredItems: List<ActionItem> = if (selectedFilter == null) {
        actionItems
    } else {
        actionItems.filter { it.priority == selectedFilter }
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header Title
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Prioritized Action Plan",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "Step-by-step roadmap to eliminate rubric gaps for ${profile.targetRole.title}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Progress Banner Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("action_plan_progress_card")
                    .border(
                        1.dp,
                        Brush.horizontalGradient(listOf(DevSignalCyan.copy(alpha = 0.5f), DevSignalEmerald.copy(alpha = 0.3f))),
                        RoundedCornerShape(16.dp)
                    )
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(DevSignalEmerald.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PlaylistAddCheck,
                                    contentDescription = null,
                                    tint = DevSignalEmerald,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "$completedCount of $totalCount completed",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.testTag("action_completion_counter")
                                )
                                Text(
                                    text = "${(completionRatio * 100).toInt()}% resolved",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        if (potentialBoost > 0) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(DevSignalEmerald.copy(alpha = 0.15f))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "+$potentialBoost PTS AVAILABLE",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace,
                                    color = DevSignalEmerald
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Progress Bar
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(fraction = animatedProgress)
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(
                                    Brush.horizontalGradient(
                                        listOf(DevSignalCyan, DevSignalEmerald)
                                    )
                                )
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Re-analyze CTA
                    Button(
                        onClick = { viewModel.reanalyze() },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = DevSignalCyan),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .testTag("reanalyze_button_action_plan")
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Autorenew,
                                contentDescription = null,
                                tint = Color.Black,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Re-analyze Profile with Updates",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        }
                    }
                }
            }

            // Priority Filter Chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                FilterChip(
                    label = "All (${actionItems.size})",
                    isSelected = selectedFilter == null,
                    onClick = { selectedFilter = null }
                )
                FilterChip(
                    label = "P0 High (${actionItems.count { it.priority == Priority.P0_HIGH }})",
                    isSelected = selectedFilter == Priority.P0_HIGH,
                    onClick = { selectedFilter = Priority.P0_HIGH }
                )
                FilterChip(
                    label = "P1 Medium (${actionItems.count { it.priority == Priority.P1_MEDIUM }})",
                    isSelected = selectedFilter == Priority.P1_MEDIUM,
                    onClick = { selectedFilter = Priority.P1_MEDIUM }
                )
                FilterChip(
                    label = "P2 Low (${actionItems.count { it.priority == Priority.P2_LOW }})",
                    isSelected = selectedFilter == Priority.P2_LOW,
                    onClick = { selectedFilter = Priority.P2_LOW }
                )
            }

            // Checklist Items
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                filteredItems.forEach { item ->
                    ActionPlanItemRow(
                        item = item,
                        onToggle = { viewModel.toggleActionItem(item.id) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun FilterChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(
                if (isSelected) DevSignalCyan.copy(alpha = 0.2f)
                else MaterialTheme.colorScheme.surfaceVariant
            )
            .border(
                1.dp,
                if (isSelected) DevSignalCyan else Color.Transparent,
                RoundedCornerShape(20.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) DevSignalCyan else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
