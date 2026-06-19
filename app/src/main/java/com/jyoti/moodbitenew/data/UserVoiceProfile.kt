package com.jyoti.moodbitenew.data

import com.jyoti.moodbitenew.engine.VoiceEmotionAnalyzer
import kotlin.math.abs

data class UserVoiceProfile(
    val userId: String,
    val baselinePitch: Float,  // User's normal pitch
    val baselineSpeechRate: Float,  // User's normal speech rate
    val baselineStress: Float,  // User's resting stress level
    val createdAt: Long = System.currentTimeMillis()
)

// Calculate relative stress (vs their baseline)
fun calculateRelativeStress(
    currentMetrics: VoiceEmotionAnalyzer.VoiceMetrics,
    userProfile: UserVoiceProfile
): Float {
    val pitchDeviation = abs(currentMetrics.averagePitch - userProfile.baselinePitch) / userProfile.baselinePitch
    val rateDeviation = abs(currentMetrics.speechRate - userProfile.baselineSpeechRate) / userProfile.baselineSpeechRate
    val stressDeviation = currentMetrics.voiceStress - userProfile.baselineStress

    return (pitchDeviation * 0.3f + rateDeviation * 0.3f + stressDeviation * 0.4f).coerceIn(0f, 1f)
}