package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.components.getDynamicFitColors
import com.example.components.getFitTierLabel
import com.example.data.MockData
import com.example.model.TargetRole
import com.example.ui.theme.DevSignalCyan
import com.example.ui.theme.DevSignalEmeraldBright
import com.example.ui.theme.ScoreAverage
import com.example.ui.theme.ScoreWarning
import com.example.viewmodel.DevSignalScreen
import com.example.viewmodel.DevSignalViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    @Test
    fun `read string from context verifies DevSignal app_name`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("DevSignal", appName)
    }

    @Test
    fun `fitScoreGauge dynamic color thresholds produce correct color stops`() {
        // >= 85%: Emerald
        val topTierColors = getDynamicFitColors(0.92f)
        assertEquals(DevSignalEmeraldBright, topTierColors.first)

        // 70-84%: Cyan
        val goodTierColors = getDynamicFitColors(0.78f)
        assertEquals(DevSignalCyan, goodTierColors.first)

        // 50-69%: Amber / Average
        val moderateTierColors = getDynamicFitColors(0.62f)
        assertEquals(ScoreAverage, moderateTierColors.first)

        // < 50%: Warning / Rose
        val lowTierColors = getDynamicFitColors(0.35f)
        assertEquals(ScoreWarning, lowTierColors.first)
    }

    @Test
    fun `fitScoreGauge tier labels match percentages`() {
        assertEquals("Exceptional Fit", getFitTierLabel(0.95f))
        assertEquals("Strong Fit", getFitTierLabel(0.84f))
        assertEquals("Competitive", getFitTierLabel(0.75f))
        assertEquals("Moderate Alignment", getFitTierLabel(0.55f))
        assertEquals("Action Required", getFitTierLabel(0.40f))
    }

    @Test
    fun `mock data returns valid profile for alexrivera`() {
        val profile = MockData.getProfile("alexrivera", TargetRole.SENIOR_FULLSTACK)
        assertNotNull(profile)
        assertEquals("alexrivera", profile?.username)
        assertEquals(84, profile?.overallScore)
        assertEquals(4, profile?.topRepos?.size)
    }

    @Test
    fun `mock data returns null for nouser123`() {
        val profile = MockData.getProfile("nouser123", TargetRole.SENIOR_FULLSTACK)
        assertNull(profile)
    }

    @Test
    fun `target role change adjusts scores appropriately`() {
        val fullstackProfile = MockData.getProfile("alexrivera", TargetRole.SENIOR_FULLSTACK)
        val mlProfile = MockData.getProfile("alexrivera", TargetRole.ML_SYSTEMS)
        assertNotNull(fullstackProfile)
        assertNotNull(mlProfile)
        assertTrue(fullstackProfile!!.overallScore != mlProfile!!.overallScore)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `viewModel toggling action item updates completed state`() = runTest {
        val viewModel = DevSignalViewModel()
        val firstItem = viewModel.actionItems.value.first()
        val initialStatus = firstItem.completed

        viewModel.toggleActionItem(firstItem.id)
        val updatedFirst = viewModel.actionItems.value.first()
        assertEquals(!initialStatus, updatedFirst.completed)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `viewModel navigation works properly`() = runTest {
        val viewModel = DevSignalViewModel()
        assertEquals(DevSignalScreen.ANALYZE, viewModel.currentScreen.value)

        viewModel.navigateTo(DevSignalScreen.DASHBOARD)
        assertEquals(DevSignalScreen.DASHBOARD, viewModel.currentScreen.value)

        viewModel.navigateTo(DevSignalScreen.ACTION_PLAN)
        assertEquals(DevSignalScreen.ACTION_PLAN, viewModel.currentScreen.value)

        viewModel.navigateTo(DevSignalScreen.PROGRESS)
        assertEquals(DevSignalScreen.PROGRESS, viewModel.currentScreen.value)
    }
}
