package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.MockData
import com.example.model.ActionItem
import com.example.model.AnalyzedRepo
import com.example.model.DevProfile
import com.example.model.ProgressSnapshot
import com.example.model.TargetRole
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class DevSignalScreen(val title: String) {
    ANALYZE("Analyze"),
    DASHBOARD("Dashboard"),
    ACTION_PLAN("Action Plan"),
    PROGRESS("Progress")
}

class DevSignalViewModel : ViewModel() {

    private val _currentScreen = MutableStateFlow(DevSignalScreen.ANALYZE)
    val currentScreen: StateFlow<DevSignalScreen> = _currentScreen.asStateFlow()

    private val _usernameInput = MutableStateFlow("alexrivera")
    val usernameInput: StateFlow<String> = _usernameInput.asStateFlow()

    private val _targetRole = MutableStateFlow(TargetRole.SENIOR_FULLSTACK)
    val targetRole: StateFlow<TargetRole> = _targetRole.asStateFlow()

    private val _jobDescriptionInput = MutableStateFlow("")
    val jobDescriptionInput: StateFlow<String> = _jobDescriptionInput.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _loadingStep = MutableStateFlow("Connecting to GitHub API...")
    val loadingStep: StateFlow<String> = _loadingStep.asStateFlow()

    private val _errorState = MutableStateFlow<String?>(null)
    val errorState: StateFlow<String?> = _errorState.asStateFlow()

    private val _currentProfile = MutableStateFlow<DevProfile>(
        MockData.getProfile("alexrivera", TargetRole.SENIOR_FULLSTACK)!!
    )
    val currentProfile: StateFlow<DevProfile> = _currentProfile.asStateFlow()

    private val _actionItems = MutableStateFlow<List<ActionItem>>(MockData.initialActionItems)
    val actionItems: StateFlow<List<ActionItem>> = _actionItems.asStateFlow()

    private val _snapshots = MutableStateFlow<List<ProgressSnapshot>>(MockData.initialSnapshots)
    val snapshots: StateFlow<List<ProgressSnapshot>> = _snapshots.asStateFlow()

    private val _isDarkMode = MutableStateFlow(true)
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    private val _selectedRepo = MutableStateFlow<AnalyzedRepo?>(null)
    val selectedRepo: StateFlow<AnalyzedRepo?> = _selectedRepo.asStateFlow()

    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

    fun navigateTo(screen: DevSignalScreen) {
        _currentScreen.value = screen
    }

    fun setUsername(name: String) {
        _usernameInput.value = name
        _errorState.value = null
    }

    fun setJobDescription(jd: String) {
        _jobDescriptionInput.value = jd
    }

    fun setTargetRole(role: TargetRole) {
        _targetRole.value = role
        // Immediately adapt current profile if already loaded
        val profile = MockData.getProfile(_usernameInput.value, role)
        if (profile != null) {
            _currentProfile.value = profile
        }
    }

    fun toggleDarkMode() {
        _isDarkMode.value = !_isDarkMode.value
    }

    fun selectRepo(repo: AnalyzedRepo?) {
        _selectedRepo.value = repo
    }

    fun dismissToast() {
        _toastMessage.value = null
    }

    fun dismissError() {
        _errorState.value = null
    }

    fun toggleActionItem(id: String) {
        _actionItems.value = _actionItems.value.map { item ->
            if (item.id == id) item.copy(completed = !item.completed) else item
        }
    }

    fun analyzeProfile() {
        val rawUser = _usernameInput.value.trim()
        if (rawUser.isEmpty()) {
            _errorState.value = "Please enter a valid GitHub username."
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _errorState.value = null

            _loadingStep.value = "Connecting to GitHub API for @$rawUser..."
            delay(400)

            _loadingStep.value = "Indexing repositories, commits & pull requests..."
            delay(450)

            _loadingStep.value = "Auditing CI/CD workflows and test coverage..."
            delay(450)

            _loadingStep.value = "Scoring against ${_targetRole.value.title} rubric..."
            delay(350)

            val profile = MockData.getProfile(rawUser, _targetRole.value)
            if (profile == null) {
                _isLoading.value = false
                _errorState.value = "User not found. Could not locate GitHub user '@$rawUser'. Please check the username or choose one of the verified examples below."
            } else {
                _currentProfile.value = profile
                _isLoading.value = false
                _currentScreen.value = DevSignalScreen.DASHBOARD
                _toastMessage.value = "Profile @${profile.username} analyzed against ${profile.targetRole.title}."
            }
        }
    }

    fun reanalyze() {
        viewModelScope.launch {
            _isLoading.value = true
            _loadingStep.value = "Re-evaluating profile with newly resolved checklist items..."
            delay(1200)

            val current = _currentProfile.value
            val completedBoost = _actionItems.value.filter { it.completed }.sumOf { it.scoreBoost }
            val newScore = minOf(98, current.overallScore + 3 + (completedBoost / 4))

            val updatedCadence = current.cadenceScore.copy(score = minOf(99, current.cadenceScore.score + 2))
            val updatedProduction = current.productionScore.copy(score = minOf(99, current.productionScore.score + 4))

            _currentProfile.value = current.copy(
                overallScore = newScore,
                percentile = "Top ${maxOf(2, 12 - (newScore - 84))}% Candidate",
                fitLabel = if (newScore >= 90) "Top Tier Match" else "Strong Match",
                cadenceScore = updatedCadence,
                productionScore = updatedProduction
            )

            // Add new snapshot point to progress chart
            val timeFmt = SimpleDateFormat("h:mm a", Locale.US).format(Date())
            val newSnapshot = ProgressSnapshot(
                id = "snap-${System.currentTimeMillis()}",
                scanDate = "Today, $timeFmt",
                roleTitle = current.targetRole.title,
                overallScore = newScore,
                testCoverage = minOf(95, current.productionScore.score + 4),
                docClarity = minOf(98, current.documentationScore.score + 2),
                commitCadence = minOf(98, current.cadenceScore.score + 2),
                deltaNote = "+${newScore - current.overallScore} pts (Re-analysis sprint boost)",
                isCurrent = true
            )

            _snapshots.value = listOf(newSnapshot) + _snapshots.value.map { it.copy(isCurrent = false) }

            _isLoading.value = false
            _toastMessage.value = "Re-analysis complete! Overall fit score increased to $newScore/100."
        }
    }
}
