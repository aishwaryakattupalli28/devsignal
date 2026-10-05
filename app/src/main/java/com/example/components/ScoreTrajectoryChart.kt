package com.example.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ProgressSnapshot
import com.example.ui.theme.DevSignalCyan
import com.example.ui.theme.DevSignalEmerald

@Composable
fun ScoreTrajectoryChart(
    snapshots: List<ProgressSnapshot>,
    modifier: Modifier = Modifier
) {
    // Snapshots chronologically (oldest to newest for the chart)
    val chronological = remember(snapshots) { snapshots.reversed() }
    val animatedProgress = remember { Animatable(0f) }

    LaunchedEffect(snapshots) {
        animatedProgress.snapTo(0f)
        animatedProgress.animateTo(1f, animationSpec = tween(1000))
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        modifier = modifier
            .fillMaxWidth()
            .testTag("score_trajectory_chart_card")
            .border(
                1.dp,
                Brush.linearGradient(
                    colors = listOf(DevSignalCyan.copy(alpha = 0.4f), DevSignalEmerald.copy(alpha = 0.2f))
                ),
                RoundedCornerShape(16.dp)
            )
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(DevSignalCyan.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.TrendingUp,
                            contentDescription = null,
                            tint = DevSignalCyan,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "ROLE FIT TRAJECTORY",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        letterSpacing = 0.8.sp
                    )
                }

                val firstScore = chronological.firstOrNull()?.overallScore ?: 70
                val latestScore = chronological.lastOrNull()?.overallScore ?: 84
                val delta = latestScore - firstScore

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(DevSignalEmerald.copy(alpha = 0.15f))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = "+$delta pts overall",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = DevSignalEmerald,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Line Chart Canvas
            val outlineColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
            val textColor = MaterialTheme.colorScheme.onSurfaceVariant

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            ) {
                Canvas(modifier = Modifier.fillMaxWidth().height(180.dp)) {
                    val w = size.width
                    val h = size.height - 30.dp.toPx()
                    val bottomY = h
                    val topY = 20.dp.toPx()

                    // Horizontal Guide Lines (60, 75, 90, 100)
                    val guideScores = listOf(60, 75, 90, 100)
                    guideScores.forEach { s ->
                        val ratio = (s - 50) / 50f
                        val y = bottomY - (ratio * (bottomY - topY))
                        drawLine(
                            color = outlineColor,
                            start = Offset(0f, y),
                            end = Offset(w, y),
                            strokeWidth = 1.dp.toPx()
                        )
                    }

                    if (chronological.isNotEmpty()) {
                        val stepX = if (chronological.size > 1) w / (chronological.size - 1) else w / 2

                        val points = chronological.mapIndexed { index, snapshot ->
                            val x = if (chronological.size > 1) index * stepX else w / 2
                            val scoreRatio = (snapshot.overallScore - 50) / 50f
                            val y = bottomY - (scoreRatio * (bottomY - topY) * animatedProgress.value)
                            Offset(x, y)
                        }

                        // Gradient Area Path
                        val areaPath = Path().apply {
                            moveTo(points.first().x, bottomY)
                            points.forEach { lineTo(it.x, it.y) }
                            lineTo(points.last().x, bottomY)
                            close()
                        }

                        drawPath(
                            path = areaPath,
                            brush = Brush.verticalGradient(
                                colors = listOf(DevSignalCyan.copy(alpha = 0.35f), DevSignalCyan.copy(alpha = 0.02f)),
                                startY = topY,
                                endY = bottomY
                            )
                        )

                        // Line Path
                        val linePath = Path().apply {
                            moveTo(points.first().x, points.first().y)
                            for (i in 1 until points.size) {
                                lineTo(points[i].x, points[i].y)
                            }
                        }

                        drawPath(
                            path = linePath,
                            brush = Brush.horizontalGradient(
                                colors = listOf(DevSignalCyan, DevSignalEmerald)
                            ),
                            style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
                        )

                        // Draw Point Circles & Glow
                        points.forEachIndexed { i, pt ->
                            val isLatest = i == points.size - 1
                            drawCircle(
                                color = if (isLatest) DevSignalEmerald else DevSignalCyan,
                                radius = if (isLatest) 6.dp.toPx() else 4.dp.toPx(),
                                center = pt
                            )
                            drawCircle(
                                color = Color.White,
                                radius = 2.dp.toPx(),
                                center = pt
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // X-Axis Labels Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                chronological.forEach { snap ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "${snap.overallScore}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            color = if (snap.isCurrent) DevSignalEmerald else MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = snap.scanDate.split(",").firstOrNull() ?: snap.scanDate,
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}
