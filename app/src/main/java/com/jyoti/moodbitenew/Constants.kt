package com.jyoti.moodbitenew

import androidx.compose.ui.graphics.Color
import com.jyoti.moodbitenew.model.MoodMeta
import com.jyoti.moodbitenew.model.MoodType

val ALL_MOODS = listOf(
    MoodMeta(MoodType.HAPPY, "Happy", "😊", Color(0xFF34D399), Color(0xFFFCD34D), "Feeling great"),
    MoodMeta(MoodType.STRESSED, "Stressed", "😰", Color(0xFFEF4444), Color(0xFFFCA5A5), "Feeling overwhelmed"),
    MoodMeta(MoodType.TIRED, "Tired", "😴", Color(0xFF94A3B8), Color(0xFFCBD5E1), "Need rest"),
    MoodMeta(MoodType.SAD, "Sad", "😢", Color(0xFF60A5FA), Color(0xFFBFDBFE), "Feeling down"),
    MoodMeta(MoodType.ENERGETIC, "Energetic", "⚡", Color(0xFFFCD34D), Color(0xFFFEF08A), "Full of energy"),
    MoodMeta(MoodType.ANXIOUS, "Anxious", "😟", Color(0xFFEC4899), Color(0xFFFCE7F3), "Feeling nervous"),
    MoodMeta(MoodType.CALM, "Calm", "😌", Color(0xFF10B981), Color(0xFFA7F3D0), "Feeling peaceful")
)

