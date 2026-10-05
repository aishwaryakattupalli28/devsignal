package com.example.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DevSignalCyan
import com.example.ui.theme.DevSignalCyanBright
import com.example.ui.theme.DevSignalEmerald
import com.example.ui.theme.DevSignalEmeraldBright
import com.example.ui.theme.ScoreAverage
import com.example.ui.theme.ScoreExcellent
import com.example.ui.theme.ScoreGood
import com.example.ui.theme.ScoreWarning
import kotlin.math.cos
import kotlin.math.sin

/**
 * Calculates dynamic color stops for the gauge based on fit percentage.
 */
fun getDynamicFitColors(percentage: Float): Pair<Color, Color> {
    return when {
        percentage >= 0.85f -> Pair(DevSignalEmeraldBright, DevSignalCyanBright) // Top tier (>85%)
        percentage >= 0.70f -> Pair(DevSignalCyan, Color(0xFF38BDF8))          // Strong/Good (70-84%)
        percentage >= 0.50f -> Pair(ScoreAverage, Color(0xFFF97316))            // Moderate/Average (50-69%)
        else -> Pair(ScoreWarning, Color(0xFFE11D48))                           // Low/Critical (<50%)
    }
}

/**
 * Returns a human-readable fit label based on the score percentage.
 */
fun getFitTierLabel(percentage: Float): String {
    return when {
        percentage >= 0.90f -> "Exceptional Fit"
        percentage >= 0.80f -> "Strong Fit"
        percentage >= 0.70f -> "Competitive"
        percentage >= 0.50f -> "Moderate Alignment"
        else -> "Action Required"
    }
}

/**
 * A standalone, highly customizable gauge composable that renders the fit score
 * using Compose Canvas API, supporting dynamic colors based on the fit percentage.
 *
 * @param score The current score (0 to [maxScore]).
 * @param modifier Modifier for styling and layout positioning.
 * @param maxScore The maximum possible score (defaults to 100).
 * @param gaugeSize The diameter of the circular gauge.
 * @param strokeWidth The thickness of the gauge stroke.
 * @param startAngle Starting angle in degrees for the arc (140° for standard bottom opening).
 * @param sweepAngle Total sweep angle in degrees (260° provides standard speedometer feel).
 * @param showLabels Whether to display the centered score and label text.
 * @param customLabel Optional custom label beneath the score (defaults to dynamic tier label).
 * @param showTicks Whether to render radial tick marks on the background track.
 */
@Composable
fun FitScoreGauge(
    score: Int,
    modifier: Modifier = Modifier,
    maxScore: Int = 100,
    gaugeSize: Dp = 170.dp,
    strokeWidth: Dp = 14.dp,
    startAngle: Float = 140f,
    sweepAngle: Float = 260f,
    showLabels: Boolean = true,
    customLabel: String? = null,
    showTicks: Boolean = true
) {
    val clampedScore = score.coerceIn(0, maxScore)
    val targetPercentage = clampedScore.toFloat() / maxScore.toFloat()

    val animatedProgress = remember { Animatable(0f) }

    LaunchedEffect(clampedScore) {
        animatedProgress.snapTo(0f)
        animatedProgress.animateTo(
            targetValue = targetPercentage,
            animationSpec = tween(durationMillis = 1100, easing = FastOutSlowInEasing)
        )
    }

    // Dynamic colors based on fit percentage
    val (primaryColor, secondaryColor) = getDynamicFitColors(targetPercentage)
    val dynamicTier = customLabel ?: getFitTierLabel(targetPercentage)

    val trackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.22f)
    val tickColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(gaugeSize)
            .testTag("fit_score_gauge")
    ) {
        Canvas(modifier = Modifier.size(gaugeSize)) {
            val strokePx = strokeWidth.toPx()
            val diameter = size.minDimension - strokePx - 4.dp.toPx()
            val topLeft = Offset(
                (size.width - diameter) / 2f,
                (size.height - diameter) / 2f
            )
            val arcSize = Size(diameter, diameter)
            val center = Offset(size.width / 2f, size.height / 2f)
            val radius = diameter / 2f

            // 1. Optional Radial Tick Marks
            if (showTicks) {
                val tickCount = 20
                for (i in 0..tickCount) {
                    val angleDeg = startAngle + (sweepAngle * (i.toFloat() / tickCount))
                    val angleRad = Math.toRadians(angleDeg.toDouble())
                    val innerR = radius - (strokePx / 2f) - 6.dp.toPx()
                    val outerR = radius - (strokePx / 2f) - 2.dp.toPx()

                    val startX = (center.x + innerR * cos(angleRad)).toFloat()
                    val startY = (center.y + innerR * sin(angleRad)).toFloat()
                    val endX = (center.x + outerR * cos(angleRad)).toFloat()
                    val endY = (center.y + outerR * sin(angleRad)).toFloat()

                    drawLine(
                        color = tickColor,
                        start = Offset(startX, startY),
                        end = Offset(endX, endY),
                        strokeWidth = 1.5.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                }
            }

            // 2. Background Track Arc
            drawArc(
                color = trackColor,
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokePx, cap = StrokeCap.Round)
            )

            // 3. Dynamic Progress Sweep Arc
            val activeSweep = sweepAngle * animatedProgress.value
            if (activeSweep > 0f) {
                val gradientBrush = Brush.sweepGradient(
                    colors = listOf(
                        primaryColor,
                        secondaryColor,
                        primaryColor
                    ),
                    center = center
                )

                drawArc(
                    brush = gradientBrush,
                    startAngle = startAngle,
                    sweepAngle = activeSweep,
                    useCenter = false,
                    topLeft = topLeft,
                    size = arcSize,
                    style = Stroke(width = strokePx, cap = StrokeCap.Round)
                )

                // Leading Indicator Glow Dot at the head of the arc
                val currentAngleDeg = startAngle + activeSweep
                val currentAngleRad = Math.toRadians(currentAngleDeg.toDouble())
                val headX = (center.x + radius * cos(currentAngleRad)).toFloat()
                val headY = (center.y + radius * sin(currentAngleRad)).toFloat()

                // Glow ring
                drawCircle(
                    color = Color.White.copy(alpha = 0.5f),
                    radius = (strokePx / 2f) + 1.5.dp.toPx(),
                    center = Offset(headX, headY)
                )
                // Center pip
                drawCircle(
                    color = Color.White,
                    radius = (strokePx / 4f),
                    center = Offset(headX, headY)
                )
            }
        }

        // Center Labels (Score & Dynamic Tier)
        if (showLabels) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(bottom = 6.dp)
            ) {
                Row(verticalAlignment = Alignment.Bottom) {
                    val currentDisplayedScore = (clampedScore * animatedProgress.value).toInt()
                    Text(
                        text = "$currentDisplayedScore",
                        fontSize = (gaugeSize.value * 0.25f).sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace,
                        color = MaterialTheme.colorScheme.onSurface,
                        letterSpacing = (-1).sp
                    )
                    Text(
                        text = "/$maxScore",
                        fontSize = (gaugeSize.value * 0.08f).sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = 6.dp, start = 2.dp)
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = dynamicTier,
                    fontSize = (gaugeSize.value * 0.07f).coerceAtLeast(10f).sp,
                    fontWeight = FontWeight.Bold,
                    color = primaryColor
                )
            }
        }
    }
}
