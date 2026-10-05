package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.components.ContributionHeatmap
import com.example.components.LanguageDonutChart
import com.example.components.RecruiterViewCard
import com.example.components.RepositoryDeepDiveDialog
import com.example.components.RoleFitScoreGauge
import com.example.components.SubScoreCard
import com.example.components.TopReposTable
import com.example.model.TargetRole
import com.example.ui.theme.DevSignalCyan
import com.example.ui.theme.DevSignalEmerald
import com.example.viewmodel.DevSignalScreen
import com.example.viewmodel.DevSignalViewModel

@Composable
fun DashboardScreen(
    viewModel: DevSignalViewModel,
    modifier: Modifier = Modifier
) {
    val profile by viewModel.currentProfile.collectAsState()
    val selectedRepo by viewModel.selectedRepo.collectAsState()
    var roleMenuExpanded by remember { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Candidate Info Card
            CandidateProfileHeader(
                profile = profile,
                onReanalyze = { viewModel.reanalyze() },
                onChangeRoleClick = { roleMenuExpanded = true }
            )

            // Role selector dropdown
            DropdownMenu(
                expanded = roleMenuExpanded,
                onDismissRequest = { roleMenuExpanded = false }
            ) {
                TargetRole.values().forEach { role ->
                    DropdownMenuItem(
                        text = {
                            Column {
                                Text(role.title, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                Text(role.subtitle, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        },
                        onClick = {
                            viewModel.setTargetRole(role)
                            roleMenuExpanded = false
                        }
                    )
                }
            }

            // 2. Role Fit Score Gauge
            RoleFitScoreGauge(
                score = profile.overallScore,
                percentile = profile.percentile,
                fitLabel = profile.fitLabel,
                roleTitle = profile.targetRole.title
            )

            // 3. Sub-score Cards (4 key pillars)
            Text(
                text = "EVALUATION PILLARS",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                letterSpacing = 0.8.sp,
                modifier = Modifier.padding(top = 4.dp)
            )

            SubScoreCard(subScore = profile.cadenceScore)
            SubScoreCard(subScore = profile.architectureScore)
            SubScoreCard(subScore = profile.productionScore)
            SubScoreCard(subScore = profile.documentationScore)

            // 4. Action Plan Prompt Banner
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.navigateTo(DevSignalScreen.ACTION_PLAN) }
                    .border(
                        1.dp,
                        Brush.horizontalGradient(listOf(DevSignalCyan, DevSignalEmerald)),
                        RoundedCornerShape(14.dp)
                    )
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "RECOMMENDED ACTION PLAN",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                color = DevSignalCyan
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(DevSignalEmerald.copy(alpha = 0.2f))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text("+12 pts available", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = DevSignalEmerald)
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "7 prioritized fixes to elevate candidate rating to Top 5%",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Go to action plan",
                        tint = DevSignalCyan,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // 5. Recruiter 30-Second View
            RecruiterViewCard(insights = profile.recruiterInsights)

            // 6. Language Donut Chart
            LanguageDonutChart(languages = profile.languages)

            // 7. Contribution Heatmap
            ContributionHeatmap(stats = profile.contributionStats)

            // 8. Top Repositories Table
            TopReposTable(
                repos = profile.topRepos,
                onSelectRepo = { viewModel.selectRepo(it) }
            )

            Spacer(modifier = Modifier.height(20.dp))
        }

        // Repository Deep Dive Dialog (if selected)
        selectedRepo?.let { repo ->
            RepositoryDeepDiveDialog(
                repo = repo,
                onDismiss = { viewModel.selectRepo(null) }
            )
        }
    }
}

@Composable
private fun CandidateProfileHeader(
    profile: com.example.model.DevProfile,
    onReanalyze: () -> Unit,
    onChangeRoleClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("candidate_header_card")
            .border(
                1.dp,
                MaterialTheme.colorScheme.outline.copy(alpha = 0.25f),
                RoundedCornerShape(16.dp)
            )
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Avatar
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .border(2.dp, DevSignalCyan, CircleShape)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.dev_avatar),
                        contentDescription = "Candidate Avatar",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = profile.fullName,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "@${profile.username}",
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Monospace,
                        color = DevSignalCyan
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = profile.location,
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "${profile.publicRepos} repos • ${profile.followers} followers",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Target Role Indicator & Actions Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "TARGET ROLE",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = profile.targetRole.title,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    // Change role button
                    OutlinedButton(
                        onClick = onChangeRoleClick,
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        modifier = Modifier.height(32.dp)
                    ) {
                        Icon(Icons.Default.SwapHoriz, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Change", fontSize = 11.sp)
                    }

                    // Re-analyze button
                    Button(
                        onClick = onReanalyze,
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = DevSignalCyan
                        ),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        modifier = Modifier.height(32.dp).testTag("reanalyze_button_dashboard")
                    ) {
                        Icon(Icons.Default.Autorenew, contentDescription = null, tint = Color.Black, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Re-analyze", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    }
                }
            }
        }
    }
}
