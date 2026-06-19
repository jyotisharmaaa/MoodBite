package com.jyoti.moodbitenew.engine

import androidx.compose.ui.graphics.Color
import com.jyoti.moodbitenew.model.DietPlan
import com.jyoti.moodbitenew.model.FoodCategory
import com.jyoti.moodbitenew.model.FoodItem
import com.jyoti.moodbitenew.model.MoodMeta
import com.jyoti.moodbitenew.model.MoodProfile
import com.jyoti.moodbitenew.model.MoodType

object MoodAIEngine {

    private fun happyProfile(): MoodProfile = MoodProfile(
        mood = MoodType.HAPPY,
        neurotransmitter = "Serotonin & Dopamine",
        description = "You're in a great mood! Let's keep this energy up with foods that boost serotonin.",
        aiInsights = listOf(
            "High serotonin state detected. Tryptophan-rich foods will extend your positive mood.",
            "Your dopamine pathways are activated. Antioxidant-rich foods help sustain motivation.",
            "Joy amplifies when paired with nutrient-dense foods."
        ),
        foods = listOf(
            FoodItem("Salmon", "🐟", "Omega-3 rich", 280, FoodCategory.LUNCH, listOf("Fish", "Protein")),
            FoodItem("Dark Chocolate", "🍫", "Mood booster", 170, FoodCategory.SNACK, listOf("Cocoa", "Antioxidant")),
            FoodItem("Berries", "🫐", "Antioxidants", 85, FoodCategory.SNACK, listOf("Fruit")),
            FoodItem("Nuts", "🥜", "Magnesium", 200, FoodCategory.SNACK, listOf("Protein")),
            FoodItem("Cheese", "🧀", "Calcium", 110, FoodCategory.SNACK, listOf("Dairy")),
            FoodItem("Banana", "🍌", "Potassium", 105, FoodCategory.SNACK, listOf("Fruit"))
        ),
        activities = listOf("Dance to your favorite song", "Go for a walk outside", "Call a friend", "Do yoga"),
        dietPlan = DietPlan(
            breakfast = "Oatmeal with berries and honey",
            morningSnack = "Greek yogurt with granola",
            lunch = "Grilled salmon with quinoa and roasted vegetables",
            eveningSnack = "Mixed nuts and dark chocolate",
            dinner = "Chicken breast with sweet potato",
            hydration = "Water with lemon and herbal tea",
            avoid = listOf("Sugary drinks", "Fast food", "Processed snacks")
        )
    )

    private fun stressedProfile(): MoodProfile = MoodProfile(
        mood = MoodType.STRESSED,
        neurotransmitter = "Cortisol & GABA",
        description = "You're feeling stressed. Let's calm your nervous system with magnesium-rich foods.",
        aiInsights = listOf(
            "Elevated cortisol detected. Magnesium-rich foods can help restore balance.",
            "Your nervous system is overloaded. Complex carbs support serotonin production.",
            "Stress burns nutrients rapidly. Replenishment is the priority."
        ),
        foods = listOf(
            FoodItem("Spinach", "🥬", "Magnesium", 23, FoodCategory.LUNCH, listOf("Vegetable", "Iron")),
            FoodItem("Almonds", "🥜", "Stress relief", 160, FoodCategory.SNACK, listOf("Nuts", "Magnesium")),
            FoodItem("Green Tea", "🍵", "Calming", 2, FoodCategory.SNACK, listOf("Beverage", "Antioxidant")),
            FoodItem("Avocado", "🥑", "Potassium", 160, FoodCategory.SNACK, listOf("Fruit", "Healthy fat")),
            FoodItem("Pumpkin Seeds", "🌱", "Magnesium", 180, FoodCategory.SNACK, listOf("Seeds")),
            FoodItem("Whole Wheat Bread", "🍞", "Complex carbs", 100, FoodCategory.BREAKFAST, listOf("Grain"))
        ),
        activities = listOf("Practice deep breathing", "Meditate", "Take a warm bath", "Progressive muscle relaxation"),
        dietPlan = DietPlan(
            breakfast = "Whole wheat toast with avocado",
            morningSnack = "Green tea with almond biscuits",
            lunch = "Spinach salad with chickpeas",
            eveningSnack = "Chamomile tea with whole grain crackers",
            dinner = "Baked white fish with steamed broccoli",
            hydration = "Herbal tea (chamomile or lavender) and water",
            avoid = listOf("Caffeine", "Energy drinks", "Sugar", "Processed foods")
        )
    )

    private fun tiredProfile(): MoodProfile = MoodProfile(
        mood = MoodType.TIRED,
        neurotransmitter = "ATP & Iron",
        description = "You're feeling tired. Let's boost your energy with iron and B-vitamin rich foods.",
        aiInsights = listOf(
            "Energy reserves appear depleted. Iron-rich foods can improve oxygen delivery.",
            "Your cells need ATP support. B vitamins help convert food into energy.",
            "Slow-release carbohydrates will prevent energy crashes."
        ),
        foods = listOf(
            FoodItem("Red Meat", "🥩", "Iron boost", 250, FoodCategory.LUNCH, listOf("Protein", "Iron")),
            FoodItem("Lentils", "🫘", "Energy", 230, FoodCategory.LUNCH, listOf("Legume", "Protein")),
            FoodItem("Eggs", "🥚", "B vitamins", 155, FoodCategory.BREAKFAST, listOf("Protein")),
            FoodItem("Coffee", "☕", "Caffeine boost", 5, FoodCategory.BREAKFAST, listOf("Beverage")),
            FoodItem("Whole Grain Pasta", "🍝", "Complex carbs", 350, FoodCategory.LUNCH, listOf("Grain")),
            FoodItem("Peanut Butter", "🥜", "Energy", 190, FoodCategory.SNACK, listOf("Protein", "Healthy fat"))
        ),
        activities = listOf("Light exercise", "Get sunlight exposure", "Power nap (20 mins)", "Drink water"),
        dietPlan = DietPlan(
            breakfast = "Eggs and whole grain toast with coffee",
            morningSnack = "Banana with peanut butter",
            lunch = "Grilled chicken with brown rice",
            eveningSnack = "String cheese and whole grain crackers",
            dinner = "Salmon with sweet potato",
            hydration = "Water and green tea (moderate caffeine)",
            avoid = listOf("Heavy, fatty foods", "Too much sugar", "Alcohol")
        )
    )

    private fun sadProfile(): MoodProfile = MoodProfile(
        mood = MoodType.SAD,
        neurotransmitter = "Serotonin & Dopamine",
        description = "You're feeling down. Tryptophan-rich foods may help support your mood recovery.",
        aiInsights = listOf(
            "Low serotonin patterns detected. Tryptophan-rich foods may support mood recovery.",
            "Omega-3 and folate are strongly linked to emotional well-being.",
            "Nourishing your gut may positively influence your mood."
        ),
        foods = listOf(
            FoodItem("Turkey", "🦃", "Tryptophan", 190, FoodCategory.LUNCH, listOf("Protein")),
            FoodItem("Walnuts", "🥜", "Omega-3", 185, FoodCategory.SNACK, listOf("Nuts", "Healthy fat")),
            FoodItem("Leafy Greens", "🥗", "Folate", 35, FoodCategory.LUNCH, listOf("Vegetable")),
            FoodItem("Blueberries", "🫐", "Antioxidants", 57, FoodCategory.SNACK, listOf("Fruit")),
            FoodItem("Chickpeas", "🫘", "Mood support", 270, FoodCategory.LUNCH, listOf("Legume")),
            FoodItem("Honey", "🍯", "Tryptophan helper", 64, FoodCategory.SNACK, listOf("Sweetener"))
        ),
        activities = listOf("Spend time in nature", "Connect with friends", "Journal your feelings", "Practice gratitude"),
        dietPlan = DietPlan(
            breakfast = "Oatmeal with honey and walnuts",
            morningSnack = "Greek yogurt with blueberries",
            lunch = "Turkey sandwich on whole grain",
            eveningSnack = "Mixed nuts and dried fruit",
            dinner = "Chickpea curry with brown rice",
            hydration = "Water with herbs and herbal teas",
            avoid = listOf("Alcohol", "Processed sugar", "Fried foods")
        )
    )

    private fun energeticProfile(): MoodProfile = MoodProfile(
        mood = MoodType.ENERGETIC,
        neurotransmitter = "Dopamine & Adrenaline",
        description = "You're in peak performance mode! Fuel wisely to sustain this energy.",
        aiInsights = listOf(
            "Peak performance mode activated. Fuel wisely to sustain momentum.",
            "Protein and complex carbs will maximize your productive state.",
            "Your body is ready for higher output and recovery."
        ),
        foods = listOf(
            FoodItem("Chicken Breast", "🍗", "Lean protein", 165, FoodCategory.LUNCH, listOf("Protein")),
            FoodItem("Brown Rice", "🍚", "Energy", 215, FoodCategory.LUNCH, listOf("Grain")),
            FoodItem("Banana", "🍌", "Quick carbs", 105, FoodCategory.SNACK, listOf("Fruit")),
            FoodItem("Greek Yogurt", "🥛", "Protein", 130, FoodCategory.SNACK, listOf("Dairy")),
            FoodItem("Almonds", "🥜", "Sustaining energy", 160, FoodCategory.SNACK, listOf("Nuts")),
            FoodItem("Beetroot", "🌹", "Nitrates", 44, FoodCategory.LUNCH, listOf("Vegetable"))
        ),
        activities = listOf("High-intensity workout", "Competitive sport", "Deep work session", "Cold shower"),
        dietPlan = DietPlan(
            breakfast = "Eggs with whole grain toast",
            morningSnack = "Banana with almonds",
            lunch = "Grilled chicken with brown rice and vegetables",
            eveningSnack = "Greek yogurt with granola",
            dinner = "Lean beef with sweet potato",
            hydration = "Water and sports drinks for hydration",
            avoid = listOf("Slow carbs", "Too much fiber before exercise", "Heavy meals")
        )
    )

    private fun anxiousProfile(): MoodProfile = MoodProfile(
        mood = MoodType.ANXIOUS,
        neurotransmitter = "GABA & Serotonin",
        description = "You're feeling anxious. Magnesium and probiotics can support a calmer nervous system.",
        aiInsights = listOf(
            "Magnesium and probiotics can support a calmer nervous system.",
            "L-theanine promotes relaxation without reducing focus.",
            "Gut-brain communication is highly active during anxiety."
        ),
        foods = listOf(
            FoodItem("Kefir", "🥛", "Probiotics", 100, FoodCategory.SNACK, listOf("Dairy", "Fermented")),
            FoodItem("Pumpkin Seeds", "🌱", "Magnesium", 180, FoodCategory.SNACK, listOf("Seeds")),
            FoodItem("Dark Chocolate", "🍫", "L-theanine", 170, FoodCategory.SNACK, listOf("Cocoa")),
            FoodItem("Camomile Tea", "🍵", "Calming", 2, FoodCategory.SNACK, listOf("Herb", "Beverage")),
            FoodItem("Broccoli", "🥦", "Magnesium", 34, FoodCategory.LUNCH, listOf("Vegetable")),
            FoodItem("Miso Soup", "🍲", "Probiotics", 40, FoodCategory.LUNCH, listOf("Soup", "Fermented"))
        ),
        activities = listOf("Breathing exercises", "Gentle yoga", "Meditation", "Listening to calming music"),
        dietPlan = DietPlan(
            breakfast = "Oatmeal with kefir",
            morningSnack = "Camomile tea with toast",
            lunch = "Miso soup with vegetables",
            eveningSnack = "Dark chocolate with pumpkin seeds",
            dinner = "Baked fish with steamed broccoli",
            hydration = "Herbal teas and warm water with honey",
            avoid = listOf("Caffeine", "Alcohol", "High sugar foods", "Spicy foods")
        )
    )

    private fun calmProfile(): MoodProfile = MoodProfile(
        mood = MoodType.CALM,
        neurotransmitter = "GABA & Acetylcholine",
        description = "You're in a peaceful state. Balanced meals will help preserve this calm.",
        aiInsights = listOf(
            "Your parasympathetic nervous system is active and restorative.",
            "Balanced meals will help preserve this peaceful state.",
            "This is an ideal moment for mindful nutrition."
        ),
        foods = listOf(
            FoodItem("Almonds", "🥜", "Magnesium", 160, FoodCategory.SNACK, listOf("Nuts")),
            FoodItem("Herbal Tea", "🍵", "Relaxing", 2, FoodCategory.SNACK, listOf("Beverage")),
            FoodItem("Sweet Potato", "🍠", "Carbs", 86, FoodCategory.DINNER, listOf("Vegetable")),
            FoodItem("Chicken", "🍗", "Tryptophan", 165, FoodCategory.LUNCH, listOf("Protein")),
            FoodItem("Brown Rice", "🍚", "Calm energy", 215, FoodCategory.LUNCH, listOf("Grain")),
            FoodItem("Salmon", "🐟", "Omega-3", 280, FoodCategory.DINNER, listOf("Fish", "Protein"))
        ),
        activities = listOf("Mindful eating", "Reading", "Nature walk", "Journaling"),
        dietPlan = DietPlan(
            breakfast = "Whole grain oatmeal with nuts",
            morningSnack = "Herbal tea with toast",
            lunch = "Grilled chicken with brown rice and vegetables",
            eveningSnack = "Herbal tea with almonds",
            dinner = "Baked salmon with sweet potato",
            hydration = "Herbal teas and water",
            avoid = listOf("Stimulants", "Heavy foods", "Alcohol", "Processed foods")
        )
    )

    private val profiles: Map<MoodType, MoodProfile> = mapOf(
        MoodType.HAPPY to happyProfile(),
        MoodType.STRESSED to stressedProfile(),
        MoodType.TIRED to tiredProfile(),
        MoodType.SAD to sadProfile(),
        MoodType.ENERGETIC to energeticProfile(),
        MoodType.ANXIOUS to anxiousProfile(),
        MoodType.CALM to calmProfile()
    )

    fun getProfile(mood: MoodType): MoodProfile =
        profiles[mood] ?: profiles[MoodType.HAPPY]!!

    fun getInsight(mood: MoodType): String {
        return when (mood) {
            MoodType.HAPPY -> listOf(
                "High serotonin state detected. Tryptophan-rich foods will extend your positive mood.",
                "Your dopamine pathways are activated. Antioxidant-rich foods help sustain motivation.",
                "Joy amplifies when paired with nutrient-dense foods."
            )

            MoodType.STRESSED -> listOf(
                "Elevated cortisol detected. Magnesium-rich foods can help restore balance.",
                "Your nervous system is overloaded. Complex carbs support serotonin production.",
                "Stress burns nutrients rapidly. Replenishment is the priority."
            )

            MoodType.TIRED -> listOf(
                "Energy reserves appear depleted. Iron-rich foods can improve oxygen delivery.",
                "Your cells need ATP support. B vitamins help convert food into energy.",
                "Slow-release carbohydrates will prevent energy crashes."
            )

            MoodType.SAD -> listOf(
                "Low serotonin patterns detected. Tryptophan-rich foods may support mood recovery.",
                "Omega-3 and folate are strongly linked to emotional well-being.",
                "Nourishing your gut may positively influence your mood."
            )

            MoodType.ENERGETIC -> listOf(
                "Peak performance mode activated. Fuel wisely to sustain momentum.",
                "Protein and complex carbs will maximize your productive state.",
                "Your body is ready for higher output and recovery."
            )

            MoodType.ANXIOUS -> listOf(
                "Magnesium and probiotics can support a calmer nervous system.",
                "L-theanine promotes relaxation without reducing focus.",
                "Gut-brain communication is highly active during anxiety."
            )

            MoodType.CALM -> listOf(
                "Your parasympathetic nervous system is active and restorative.",
                "Balanced meals will help preserve this peaceful state.",
                "This is an ideal moment for mindful nutrition."
            )

            MoodType.NO_FACE_DETECTED  -> {
                listOf(
                    "Your mood is unknown. A balanced diet and mindful activities can help maintain well-being.",
                    "Consider a variety of nutrient-rich foods to support overall health.",
                    "Engage in activities that bring you joy and relaxation."
                )
            }
        }.random()
    }
    fun getVoiceAnalysisInsight(voiceMetrics: VoiceEmotionAnalyzer.VoiceMetrics): String {
        return when {
            voiceMetrics.speechRate > 180f ->
                "Rapid speech detected - you might be feeling energetic or stressed."
            voiceMetrics.speechRate < 100f ->
                "Slower speech pattern - you might be feeling calm or tired."
            voiceMetrics.voiceStress > 0.7f ->
                "Voice stress detected - elevated emotional intensity in your tone."
            voiceMetrics.voiceStress < 0.3f ->
                "Relaxed tone detected - calm and peaceful emotional state."
            else ->
                "Balanced vocal patterns - neutral emotional state."
        }
    }
    fun fuseMoodDetection(
        manualMood: MoodType? = null,
        faceMood: Pair<MoodType, Float>? = null,
        voiceMood: Pair<MoodType, Float>? = null
    ): Pair<MoodType, Float> {

        val weights = mapOf(
            "manual" to 0.20f,  // Manual is least reliable
            "face" to 0.30f,    // Face detection is reliable
            "voice" to 0.50f    // Voice is most reliable
        )

        val moodScores = mutableMapOf<MoodType, Float>()

        // Accumulate scores
        manualMood?.let {
            moodScores[it] = (moodScores[it] ?: 0f) + weights["manual"]!!
        }

        faceMood?.let { (mood, confidence) ->
            moodScores[mood] = (moodScores[mood] ?: 0f) + (confidence * weights["face"]!!)
        }

        voiceMood?.let { (mood, confidence) ->
            moodScores[mood] = (moodScores[mood] ?: 0f) + (confidence * weights["voice"]!!)
        }

        // Find highest scoring mood
       /* val (finalMood, finalScore) = moodScores.maxByOrNull { it.value }
            ?: Pair(MoodType.CALM, 0.5f)*/

        val bestMatch = moodScores.maxByOrNull { it.value }

        val detectedMood = bestMatch?.key ?: MoodType.CALM
        val confidence = bestMatch?.value ?: 0.5f
        return Pair(detectedMood, (confidence / 1.0f).coerceIn(0f, 1f))
    }

    fun calculateConfidenceScore(
        voiceMetrics: VoiceEmotionAnalyzer.VoiceMetrics,
        transcription: String,
        faceMood: MoodType?,
        detectedMood: MoodType
    ): Float {
        // Higher confidence if:
        // 1. Multiple detection methods agree
        // 2. Strong voice metrics
        // 3. Meaningful transcription (>3 words)
        // 4. Good audio quality

        var confidence = 0.5f

        if (faceMood == detectedMood) confidence += 0.15f
        if (voiceMetrics.voiceStress > 0.6f) confidence += 0.1f  // Clear stress indicators
        if (transcription.split(" ").size > 3) confidence += 0.15f
        if (voiceMetrics.loudnessLevel > 0.5f) confidence += 0.1f

        return confidence.coerceIn(0f, 1f)
    }

    fun getFeaturedFood(mood: MoodType): FoodItem =
        getProfile(mood).foods.random()

    fun getActivities(mood: MoodType): List<String> =
        getProfile(mood).activities

    fun getDietPlan(mood: MoodType): DietPlan =
        getProfile(mood).dietPlan
}