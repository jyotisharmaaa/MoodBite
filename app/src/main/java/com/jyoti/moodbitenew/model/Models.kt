package com.jyoti.moodbitenew.model

import androidx.compose.ui.graphics.Color

enum class MoodType {
    HAPPY, STRESSED, TIRED, SAD, ENERGETIC, ANXIOUS, CALM, NO_FACE_DETECTED
}

enum class FoodCategory {
    BREAKFAST, LUNCH, DINNER, SNACK
}

data class DietPlan(
    val breakfast: String,
    val morningSnack: String,
    val lunch: String,
    val eveningSnack: String,
    val dinner: String,
    val hydration: String,
    val avoid: List<String>
)

data class MoodMeta(
    val type: MoodType,
    val label: String,
    val emoji: String,
    val primaryColor: Color,      // Green for Happy
    val secondaryColor: Color,    // Orange accent
    val tagline: String
)

data class FoodItem(
    val name: String,
    val emoji: String,
    val benefit: String,          // "Boosts serotonin"
    val calories: Int,
    val category: FoodCategory,   // BREAKFAST, LUNCH, etc.
    val ingredients: List<String>
)

data class MoodProfile(
    val mood: MoodType,
    val neurotransmitter: String, // "Dopamine & Serotonin"
    val description: String,
    val aiInsights: List<String>, // Multiple insights per mood
    val foods: List<FoodItem>,    // 6 foods per mood
    val activities: List<String>, // 4 mood-specific activities
    val dietPlan: DietPlan        // Full day meal plan
)