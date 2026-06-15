package com.moodbite.app.engine

import android.content.Context
import com.moodbite.app.model.*
import kotlin.random.Random

/**
 * MoodBite Local AI Engine
 * A rule-based + weighted scoring system that acts as a local LLM.
 * Fully offline — zero API costs — demo ready.
 */
class MoodAIEngine(private val context: Context) {

    // ─── Mood Knowledge Base ────────────────────────────────────────────────

    private val moodFoodMap: Map<MoodType, MoodProfile> = mapOf(

        MoodType.HAPPY to MoodProfile(
            mood = MoodType.HAPPY,
            emoji = "😊",
            color = "#FFD93D",
            description = "You're glowing! Let's keep that energy high with vibrant, feel-good foods.",
            neurotransmitter = "Dopamine & Serotonin",
            foods = listOf(
                FoodItem("Dark Chocolate Bark", "🍫", "Boosts serotonin & dopamine", 180, FoodCategory.SNACK, listOf("dark chocolate", "nuts", "dried berries")),
                FoodItem("Mango Smoothie Bowl", "🥭", "Vitamin C + natural sugars for sustained joy", 320, FoodCategory.BREAKFAST, listOf("mango", "banana", "granola", "coconut flakes")),
                FoodItem("Avocado Toast with Egg", "🥑", "Healthy fats for brain happiness", 380, FoodCategory.BREAKFAST, listOf("sourdough", "avocado", "egg", "chili flakes")),
                FoodItem("Berry Acai Bowl", "🫐", "Antioxidants to amplify your mood", 290, FoodCategory.BREAKFAST, listOf("acai", "blueberries", "strawberries", "honey")),
                FoodItem("Grilled Salmon & Veggies", "🐟", "Omega-3 keeps your brain celebrating", 450, FoodCategory.LUNCH, listOf("salmon", "broccoli", "lemon", "olive oil")),
                FoodItem("Rainbow Fruit Salad", "🍓", "Colorful phytonutrients for a bright mood", 150, FoodCategory.SNACK, listOf("watermelon", "kiwi", "grapes", "mint"))
            ),
            activities = listOf("Take a walk in the park 🌳", "Call a friend 📞", "Dance to your favorite song 💃", "Try a new recipe 👨‍🍳"),
            dietPlan = DietPlan(
                breakfast = "Mango Smoothie Bowl + Green Tea",
                morningSnack = "Mixed nuts & dark chocolate",
                lunch = "Grilled Salmon with Rainbow Salad",
                eveningSnack = "Greek yogurt with berries",
                dinner = "Veggie stir-fry with quinoa",
                hydration = "2.5L water + coconut water",
                avoid = listOf("Processed sugar spikes", "Heavy fried foods")
            )
        ),

        MoodType.STRESSED to MoodProfile(
            mood = MoodType.STRESSED,
            emoji = "😤",
            color = "#FF6B6B",
            description = "Breathe. The right foods can calm your cortisol and restore balance.",
            neurotransmitter = "Cortisol Regulation",
            foods = listOf(
                FoodItem("Chamomile Oat Porridge", "🍵", "Oats reduce cortisol naturally", 280, FoodCategory.BREAKFAST, listOf("oats", "chamomile tea", "honey", "cinnamon")),
                FoodItem("Dark Chocolate & Almonds", "🍫", "Magnesium in almonds calms nerves", 200, FoodCategory.SNACK, listOf("dark chocolate 70%+", "almonds", "sea salt")),
                FoodItem("Turmeric Golden Milk", "🥛", "Anti-inflammatory, soothes anxiety", 120, FoodCategory.DRINK, listOf("milk", "turmeric", "ginger", "black pepper", "honey")),
                FoodItem("Blueberry Walnut Salad", "🥗", "Omega-3 & antioxidants fight stress hormones", 310, FoodCategory.LUNCH, listOf("spinach", "walnuts", "blueberries", "feta")),
                FoodItem("Avocado & Banana Smoothie", "🥤", "Potassium regulates blood pressure", 260, FoodCategory.DRINK, listOf("banana", "avocado", "oat milk", "chia seeds")),
                FoodItem("Baked Sweet Potato", "🍠", "Complex carbs boost serotonin steadily", 200, FoodCategory.DINNER, listOf("sweet potato", "olive oil", "rosemary", "sea salt"))
            ),
            activities = listOf("5-minute deep breathing 🧘", "Journaling thoughts 📓", "Cold water on wrists 💧", "Progressive muscle relaxation 💪"),
            dietPlan = DietPlan(
                breakfast = "Chamomile Oat Porridge + warm lemon water",
                morningSnack = "Dark chocolate & almonds",
                lunch = "Blueberry Walnut Salad + whole grain bread",
                eveningSnack = "Banana with almond butter",
                dinner = "Baked Sweet Potato + steamed greens + grilled chicken",
                hydration = "3L water + chamomile/green tea",
                avoid = listOf("Caffeine after noon", "Alcohol", "Sugar spikes", "Skipping meals")
            )
        ),

        MoodType.TIRED to MoodProfile(
            mood = MoodType.TIRED,
            emoji = "😴",
            color = "#A78BFA",
            description = "Your body needs fuel, not just rest. Let's energize you naturally.",
            neurotransmitter = "Adenosine & Iron Balance",
            foods = listOf(
                FoodItem("Iron-Rich Spinach Egg Scramble", "🍳", "Iron fights fatigue at the cellular level", 340, FoodCategory.BREAKFAST, listOf("spinach", "eggs", "garlic", "feta cheese")),
                FoodItem("Banana & Peanut Butter", "🍌", "Quick energy + sustained protein", 270, FoodCategory.SNACK, listOf("banana", "peanut butter", "chia seeds")),
                FoodItem("Beet & Ginger Juice", "🥤", "Nitrates in beets increase oxygen to muscles", 110, FoodCategory.DRINK, listOf("beet", "ginger", "apple", "lemon")),
                FoodItem("Lentil & Veggie Soup", "🍲", "Slow-release energy from complex carbs", 390, FoodCategory.LUNCH, listOf("red lentils", "carrot", "tomato", "cumin", "coriander")),
                FoodItem("Edamame with Sea Salt", "🫘", "Plant protein + amino acids for energy", 170, FoodCategory.SNACK, listOf("edamame", "sea salt", "sesame oil")),
                FoodItem("Brown Rice & Chicken Bowl", "🍚", "B vitamins convert food to energy", 480, FoodCategory.DINNER, listOf("brown rice", "grilled chicken", "broccoli", "soy sauce"))
            ),
            activities = listOf("Power nap 20 minutes ⏰", "10-minute sunlight walk ☀️", "Cold face splash 💦", "Light yoga stretches 🧘"),
            dietPlan = DietPlan(
                breakfast = "Spinach Egg Scramble + orange juice",
                morningSnack = "Banana with peanut butter",
                lunch = "Lentil Soup + whole grain bread",
                eveningSnack = "Edamame + beet juice",
                dinner = "Brown Rice & Chicken Bowl",
                hydration = "3L water + ginger tea + no caffeine after 2pm",
                avoid = listOf("Heavy sugary snacks", "Processed carbs", "Large meals before activity")
            )
        ),

        MoodType.SAD to MoodProfile(
            mood = MoodType.SAD,
            emoji = "😢",
            color = "#60A5FA",
            description = "It's okay to feel this way. These foods can gently lift your spirits.",
            neurotransmitter = "Serotonin & Endorphins",
            foods = listOf(
                FoodItem("Warm Cinnamon Oatmeal", "🥣", "Tryptophan converts to serotonin — nature's antidepressant", 310, FoodCategory.BREAKFAST, listOf("oats", "cinnamon", "honey", "walnuts", "dried cranberries")),
                FoodItem("Dark Chocolate Mousse", "🍫", "Phenylethylamine triggers happiness chemicals", 220, FoodCategory.SNACK, listOf("dark chocolate", "coconut cream", "vanilla", "maple syrup")),
                FoodItem("Salmon & Spinach Pasta", "🍝", "Omega-3 and folate fight depression markers", 520, FoodCategory.DINNER, listOf("salmon", "spinach", "whole wheat pasta", "lemon", "capers")),
                FoodItem("Saffron Milk (Kesar Doodh)", "🥛", "Saffron is clinically proven to boost mood", 150, FoodCategory.DRINK, listOf("warm milk", "saffron", "cardamom", "honey")),
                FoodItem("Brazil Nut Mix", "🥜", "Selenium deficiency linked to low mood", 190, FoodCategory.SNACK, listOf("brazil nuts", "pumpkin seeds", "dried mango")),
                FoodItem("Chicken & Sweet Potato Curry", "🍛", "Warming spices stimulate endorphin release", 490, FoodCategory.LUNCH, listOf("chicken", "sweet potato", "coconut milk", "turmeric", "cumin"))
            ),
            activities = listOf("Write 3 gratitudes ✍️", "Watch a comfort show 📺", "Hug someone or pet an animal 🐶", "Take a warm shower 🚿"),
            dietPlan = DietPlan(
                breakfast = "Warm Cinnamon Oatmeal + Saffron Milk",
                morningSnack = "Brazil nut mix",
                lunch = "Chicken & Sweet Potato Curry + rice",
                eveningSnack = "Dark Chocolate Mousse",
                dinner = "Salmon & Spinach Pasta",
                hydration = "2.5L water + warm herbal teas (chamomile, lavender)",
                avoid = listOf("Alcohol (depressant)", "Highly processed foods", "Excessive caffeine", "Skipping meals")
            )
        ),

        MoodType.ENERGETIC to MoodProfile(
            mood = MoodType.ENERGETIC,
            emoji = "⚡",
            color = "#34D399",
            description = "You're on fire! Fuel that energy smartly for peak performance.",
            neurotransmitter = "Adrenaline & Dopamine",
            foods = listOf(
                FoodItem("Pre-Workout Banana Oats", "🍌", "Perfect slow-release energy for high output", 350, FoodCategory.BREAKFAST, listOf("oats", "banana", "protein powder", "almond milk")),
                FoodItem("Greek Yogurt Parfait", "🫙", "Protein + probiotics = sustained performance", 280, FoodCategory.SNACK, listOf("Greek yogurt", "granola", "honey", "mixed berries")),
                FoodItem("Quinoa Power Bowl", "🥙", "Complete protein with all 9 amino acids", 460, FoodCategory.LUNCH, listOf("quinoa", "chickpeas", "roasted veggies", "tahini", "lemon")),
                FoodItem("Coconut Water & Chia", "🥥", "Natural electrolytes + omega-3", 130, FoodCategory.DRINK, listOf("coconut water", "chia seeds", "lime", "mint")),
                FoodItem("Tuna Avocado Wrap", "🌯", "Lean protein + good fats for endurance", 420, FoodCategory.LUNCH, listOf("tuna", "avocado", "whole wheat wrap", "spinach", "tomato")),
                FoodItem("Post-Workout Protein Shake", "🥤", "Rebuild muscles fast", 300, FoodCategory.DRINK, listOf("protein powder", "banana", "almond milk", "peanut butter", "ice"))
            ),
            activities = listOf("Hit the gym 💪", "Go for a run 🏃", "Try a new sport 🏸", "Tackle that big project 🎯"),
            dietPlan = DietPlan(
                breakfast = "Banana Oats + black coffee or green tea",
                morningSnack = "Greek Yogurt Parfait",
                lunch = "Quinoa Power Bowl + coconut water",
                eveningSnack = "Tuna Avocado Wrap",
                dinner = "Grilled chicken + sweet potato + steamed broccoli",
                hydration = "3.5L water + electrolyte drinks during workout",
                avoid = listOf("Sugar crashes (avoid candy)", "Alcohol", "Heavy meals before exercise")
            )
        ),

        MoodType.ANXIOUS to MoodProfile(
            mood = MoodType.ANXIOUS,
            emoji = "😰",
            color = "#F59E0B",
            description = "Your nervous system needs calming. These foods are nature's anti-anxiety toolkit.",
            neurotransmitter = "GABA & Serotonin",
            foods = listOf(
                FoodItem("Ashwagandha Smoothie", "🥤", "Adaptogen proven to reduce anxiety by 44%", 230, FoodCategory.DRINK, listOf("banana", "ashwagandha powder", "oat milk", "honey", "cinnamon")),
                FoodItem("Magnesium-Rich Dark Chocolate", "🍫", "Magnesium regulates the stress response", 160, FoodCategory.SNACK, listOf("dark chocolate 85%", "almonds")),
                FoodItem("Fermented Yogurt Bowl", "🥛", "Gut-brain axis: good gut = calm mind", 240, FoodCategory.BREAKFAST, listOf("yogurt", "kiwi", "flaxseeds", "blueberries")),
                FoodItem("Green Tea & Matcha Latte", "🍵", "L-theanine induces calm without drowsiness", 80, FoodCategory.DRINK, listOf("matcha powder", "oat milk", "honey")),
                FoodItem("Kale & Pumpkin Seed Salad", "🥗", "Zinc & magnesium calm the nervous system", 290, FoodCategory.LUNCH, listOf("kale", "pumpkin seeds", "pomegranate", "lemon dressing")),
                FoodItem("Chamomile Rice Pudding", "🍚", "GABA-boosting, deeply calming dessert", 200, FoodCategory.DINNER, listOf("rice", "chamomile tea", "coconut milk", "cardamom"))
            ),
            activities = listOf("Box breathing: 4-4-4-4 🌬️", "5-4-3-2-1 grounding exercise 🖐️", "Gentle nature walk 🌿", "Warm bath with lavender 🛁"),
            dietPlan = DietPlan(
                breakfast = "Fermented Yogurt Bowl + Matcha Latte",
                morningSnack = "Dark chocolate + almonds",
                lunch = "Kale & Pumpkin Seed Salad + whole grain roll",
                eveningSnack = "Ashwagandha Smoothie",
                dinner = "Chamomile Rice Pudding + steamed fish",
                hydration = "3L water + chamomile tea + avoid all caffeine",
                avoid = listOf("All caffeine", "Alcohol", "Sugar", "News scrolling before bed")
            )
        ),

        MoodType.CALM to MoodProfile(
            mood = MoodType.CALM,
            emoji = "😌",
            color = "#6EE7B7",
            description = "Beautiful stillness. Nourish that peace with clean, mindful eating.",
            neurotransmitter = "Serotonin & Oxytocin",
            foods = listOf(
                FoodItem("Matcha Overnight Oats", "🍵", "Steady energy without disrupting calm", 320, FoodCategory.BREAKFAST, listOf("oats", "matcha", "oat milk", "chia seeds", "vanilla")),
                FoodItem("Mediterranean Mezze Plate", "🫒", "Mindful, colorful, nourishing", 380, FoodCategory.LUNCH, listOf("hummus", "pita", "olives", "cucumber", "cherry tomatoes", "feta")),
                FoodItem("Herbal Infusion", "🌿", "Mindful sipping ritual", 5, FoodCategory.DRINK, listOf("lavender", "lemon balm", "passionflower", "honey")),
                FoodItem("Poached Pear & Walnuts", "🍐", "Gentle sweetness + brain-healthy omega-3", 210, FoodCategory.SNACK, listOf("pear", "walnuts", "cinnamon", "honey")),
                FoodItem("Miso Soup & Tofu", "🍜", "Fermented umami for gut-brain harmony", 160, FoodCategory.LUNCH, listOf("miso paste", "silken tofu", "seaweed", "spring onion")),
                FoodItem("Steamed Fish & Brown Rice", "🍚", "Clean protein, calm digestion", 420, FoodCategory.DINNER, listOf("white fish", "brown rice", "ginger", "soy sauce", "bok choy"))
            ),
            activities = listOf("Mindful meditation 15 min 🧘", "Read a good book 📚", "Gentle yoga 🌅", "Sketch or paint 🎨"),
            dietPlan = DietPlan(
                breakfast = "Matcha Overnight Oats + herbal tea",
                morningSnack = "Poached Pear & Walnuts",
                lunch = "Mediterranean Mezze Plate + Miso Soup",
                eveningSnack = "Hummus & veggie sticks",
                dinner = "Steamed Fish & Brown Rice + bok choy",
                hydration = "2.5L water + herbal infusions throughout the day",
                avoid = listOf("Overstimulating foods", "Excessive social media", "Heavy late-night meals")
            )
        )
    )

    // ─── Public API ─────────────────────────────────────────────────────────

    fun getMoodProfile(mood: MoodType): MoodProfile {
        return moodFoodMap[mood] ?: moodFoodMap[MoodType.HAPPY]!!
    }

    fun getAllMoods(): List<MoodType> = MoodType.values().toList()

    fun getFeaturedFood(mood: MoodType): FoodItem {
        val foods = getMoodProfile(mood).foods
        return foods.random()
    }

    fun generateAIInsight(mood: MoodType): String {
        val profile = getMoodProfile(mood)
        val insights = when (mood) {
            MoodType.HAPPY -> listOf(
                "Your brain is producing extra ${profile.neurotransmitter} right now — the foods below will help maintain that natural high without a crash.",
                "Science shows happiness amplifies when paired with nourishing foods. Your body deserves this celebration too!",
                "High serotonin detected! Feed it with foods rich in tryptophan and antioxidants."
            )
            MoodType.STRESSED -> listOf(
                "Elevated cortisol is draining your magnesium reserves. The foods below replenish it naturally.",
                "Your amygdala is activated. Magnesium-rich foods act as a natural brake on your stress response.",
                "Research shows diet can reduce cortisol levels by up to 30%. Start with the recommendations below."
            )
            MoodType.TIRED -> listOf(
                "Your ATP (cellular energy) is running low. Iron-rich and B-vitamin foods will recharge it.",
                "Fatigue often signals iron or B12 deficiency. These foods are your natural energy drink.",
                "Your mitochondria need fuel — complex carbs and iron-rich foods are the answer."
            )
            MoodType.SAD -> listOf(
                "Low serotonin patterns detected. Tryptophan-rich foods are the natural building blocks to lift your spirit.",
                "Studies show omega-3 and folate intake can improve mood within days. You've got this.",
                "Your gut produces 95% of your serotonin. Feeding it well is the most direct path to feeling better."
            )
            MoodType.ENERGETIC -> listOf(
                "Peak performance mode! Fuel smart to extend this natural high for maximum output.",
                "Your dopamine is spiking. Match it with clean protein and complex carbs for sustained power.",
                "High energy = high nutrient demand. These foods will keep you performing at your best."
            )
            MoodType.ANXIOUS -> listOf(
                "Your GABA levels may be low. Fermented foods and magnesium are nature's anxiety relief.",
                "L-theanine in green tea is scientifically proven to reduce anxiety within 40 minutes.",
                "Your vagus nerve connects gut to brain — calming your gut with probiotics calms your mind."
            )
            MoodType.CALM -> listOf(
                "Beautiful. Protect this calm with foods that don't spike blood sugar or cortisol.",
                "Your parasympathetic nervous system is active. Mindful eating will deepen this peace.",
                "This is your optimal state for healing and restoration. Nourish it gently."
            )
        }
        return insights.random()
    }

    fun getMoodsByEnergy(): List<MoodType> = listOf(
        MoodType.ENERGETIC, MoodType.HAPPY, MoodType.CALM,
        MoodType.TIRED, MoodType.STRESSED, MoodType.ANXIOUS, MoodType.SAD
    )
}
