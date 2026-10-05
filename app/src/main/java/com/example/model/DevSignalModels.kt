package com.example.model

import androidx.compose.ui.graphics.Color

enum class TargetRole(val id: String, val title: String, val level: String, val subtitle: String) {
    SENIOR_FULLSTACK(
        id = "senior_fullstack",
        title = "Senior Full-Stack Engineer",
        level = "L5 / Senior",
        subtitle = "Full-stack web applications, TypeScript/React, distributed backend, high scalability"
    ),
    STAFF_BACKEND(
        id = "staff_backend",
        title = "Staff Backend Engineer",
        level = "L6 / Staff",
        subtitle = "Distributed systems, Go/Rust/Java, high concurrency, cloud infrastructure, reliability"
    ),
    LEAD_FRONTEND(
        id = "lead_frontend",
        title = "Lead Frontend Architect",
        level = "L5-L6 / Lead",
        subtitle = "Design systems, performance, React/Next.js/TypeScript, state architecture, accessibility"
    ),
    ML_SYSTEMS(
        id = "ml_systems",
        title = "ML & Systems Engineer",
        level = "L5 / Senior",
        subtitle = "Python, PyTorch, model deployment, low-latency inference, data pipelines, Kubernetes"
    )
}

enum class Priority(val label: String, val badgeColor: Long, val textColor: Long) {
    P0_HIGH("P0 High", 0xFFEF4444, 0xFFFFFFFF),
    P1_MEDIUM("P1 Medium", 0xFFF59E0B, 0xFF1E293B),
    P2_LOW("P2 Low", 0xFF06B6D4, 0xFF0F172A)
}

data class SubScore(
    val category: String,
    val score: Int,
    val tier: String,
    val summary: String,
    val breakdownTags: List<String>
)

data class RecruiterInsights(
    val headline: String,
    val strengths: List<String>,
    val gaps: List<String>,
    val interviewTalkingPoints: List<String>
)

data class LanguageStat(
    val name: String,
    val percentage: Float,
    val colorHex: Long,
    val locCount: String
)

data class AnalyzedRepo(
    val name: String,
    val description: String,
    val stars: Int,
    val forks: Int,
    val primaryLanguage: String,
    val languageColor: Long,
    val roleFitScore: Int,
    val hasCiCd: Boolean,
    val hasTests: Boolean,
    val hasDocs: Boolean,
    val hasLicense: Boolean,
    val commitCadence: String,
    val tags: List<String>,
    val keyTakeaway: String
)

data class ContributionStats(
    val totalContributionsLastYear: Int,
    val currentStreakDays: Int,
    val longestStreakDays: Int,
    val weeklyCommitGrid: List<List<Int>> // 16-24 weeks of 7 days (level 0 to 4)
)

data class ActionItem(
    val id: String,
    val title: String,
    val description: String,
    val category: String,
    val priority: Priority,
    val scoreBoost: Int,
    val completed: Boolean = false,
    val codeRecommendation: String
)

data class ProgressSnapshot(
    val id: String,
    val scanDate: String,
    val roleTitle: String,
    val overallScore: Int,
    val testCoverage: Int,
    val docClarity: Int,
    val commitCadence: Int,
    val deltaNote: String,
    val isCurrent: Boolean = false
)

data class DevProfile(
    val username: String,
    val fullName: String,
    val title: String,
    val location: String,
    val publicRepos: Int,
    val followers: Int,
    val following: Int,
    val targetRole: TargetRole,
    val overallScore: Int,
    val percentile: String,
    val fitLabel: String,
    val cadenceScore: SubScore,
    val architectureScore: SubScore,
    val productionScore: SubScore,
    val documentationScore: SubScore,
    val recruiterInsights: RecruiterInsights,
    val languages: List<LanguageStat>,
    val contributionStats: ContributionStats,
    val topRepos: List<AnalyzedRepo>
)
