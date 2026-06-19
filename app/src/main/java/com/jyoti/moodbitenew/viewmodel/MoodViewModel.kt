package com.jyoti.moodbitenew.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.jyoti.moodbitenew.engine.MoodAIEngine
import com.jyoti.moodbitenew.model.MoodType
import com.jyoti.moodbitenew.model.MoodProfile

class MoodViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(MoodUiState())
    val uiState: StateFlow<MoodUiState> = _uiState.asStateFlow()

    data class MoodUiState(
        val selectedMood: MoodType? = null,
        val profile: MoodProfile? = null,
        val aiInsight: String = "",
        val isAnalyzing: Boolean = false,
        val analysisComplete: Boolean = false
    )

    // User taps a mood
    fun selectMood(mood: MoodType) {
        _uiState.value = _uiState.value.copy(
            selectedMood = mood,
            analysisComplete = false
        )
    }

    // User taps "Get Food Plan" button
    fun analyzeAndNavigate() {
        val mood = _uiState.value.selectedMood ?: return
        viewModelScope.launch {
            // Start analyzing
            _uiState.value = _uiState.value.copy(isAnalyzing = true)

            // Simulate AI "thinking" for UX (1.4 seconds)
            delay(1400)

            // Fetch from local engine
            val profile = MoodAIEngine.getProfile(mood)
            val insight = MoodAIEngine.getInsight(mood)

            // Update state (triggers recomposition)
            _uiState.value = _uiState.value.copy(
                profile = profile,
                aiInsight = insight,
                isAnalyzing = false,
                analysisComplete = true
            )
        }
    }

    fun resetAnalysis() {
        _uiState.value = _uiState.value.copy(
            analysisComplete = false,
            profile = null,
            aiInsight = ""
        )
    }
}