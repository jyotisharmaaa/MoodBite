package com.moodbite.app.model

// ─── Mood Types ─────────────────────────────────────────────────────────────

enum class MoodType(val displayName: String) {
    HAPPY("Happy"),
    STRESSED("Stressed"),
    TIRED("Tired"),
    SAD("Sad"),
    ENERGETIC("Energetic"),
    ANXIOUS("Anxious"),
    CALM("Calm")
}

// ─── Food Models ─────────────────────────────────────────────────────────────

enum class FoodCategory(val label: String) {
    BREAKFAST("Breakfast"),
    LUNCH("Lunch"),
    DINNER("Dinner"),
    SNACK("Snack"),
    DRINK("Drink")
}

data class FoodItem(
    val name: String,
    val emoji: String,
    val benefit: String,
    val calories: Int,
    val category: FoodCategory,
    val ingredients: List<String>
)

// ─── Diet Plan ───────────────────────────────────────────────────────────────

data class DietPlan(
    val breakfast: String,
    val morningSnack: String,
    val lunch: String,
    val eveningSnack: String,
    val dinner: String,
    val hydration: String,
    val avoid: List<String>
)

// ─── Mood Profile ────────────────────────────────────────────────────────────

data class MoodProfile(
    val mood: MoodType,
    val emoji: String,
    val color: String,
    val description: String,
    val neurotransmitter: String,
    val foods: List<FoodItem>,
    val activities: List<String>,
    val dietPlan: DietPlan
)

// ─── UI Model ────────────────────────────────────────────────────────────────

data class MoodCard(
    val moodType: MoodType,
    val emoji: String,
    val label: String,
    val bgColor: String,
    val isSelected: Boolean = false
)
