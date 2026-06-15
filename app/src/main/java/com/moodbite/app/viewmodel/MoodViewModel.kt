package com.moodbite.app.viewmodel

import android.app.Application
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.moodbite.app.engine.MoodAIEngine
import com.moodbite.app.model.*

class MoodViewModel(application: Application) : AndroidViewModel(application) {

    private val engine = MoodAIEngine(application)

    private val _selectedMood = MutableLiveData<MoodType?>()
    val selectedMood: LiveData<MoodType?> = _selectedMood

    private val _moodProfile = MutableLiveData<MoodProfile?>()
    val moodProfile: LiveData<MoodProfile?> = _moodProfile

    private val _aiInsight = MutableLiveData<String>()
    val aiInsight: LiveData<String> = _aiInsight

    private val _isAnalyzing = MutableLiveData<Boolean>(false)
    val isAnalyzing: LiveData<Boolean> = _isAnalyzing

    fun getMoodCards(): List<MoodCard> {
        return listOf(
            MoodCard(MoodType.HAPPY, "😊", "Happy", "#FFD93D"),
            MoodCard(MoodType.STRESSED, "😤", "Stressed", "#FF6B6B"),
            MoodCard(MoodType.TIRED, "😴", "Tired", "#A78BFA"),
            MoodCard(MoodType.SAD, "😢", "Sad", "#60A5FA"),
            MoodCard(MoodType.ENERGETIC, "⚡", "Energetic", "#34D399"),
            MoodCard(MoodType.ANXIOUS, "😰", "Anxious", "#F59E0B"),
            MoodCard(MoodType.CALM, "😌", "Calm", "#6EE7B7")
        )
    }

    fun selectMood(moodType: MoodType) {
        _selectedMood.value = moodType
        _isAnalyzing.value = true

        // Simulate AI processing (runs instantly, local engine)
        Handler(Looper.getMainLooper()).postDelayed({
            val profile = engine.getMoodProfile(moodType)
            val insight = engine.generateAIInsight(moodType)
            _moodProfile.value = profile
            _aiInsight.value = insight
            _isAnalyzing.value = false
        }, 1200) // Brief delay for UX feel of "thinking"
    }

    fun getCurrentProfile(): MoodProfile? = _moodProfile.value
    fun getCurrentInsight(): String = _aiInsight.value ?: ""
}
