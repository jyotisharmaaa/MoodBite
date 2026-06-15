package com.moodbite.app.ui

import android.graphics.Color
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.moodbite.app.R
import com.moodbite.app.databinding.ActivityDietPlanBinding
import com.moodbite.app.model.MoodType
import com.moodbite.app.viewmodel.MoodViewModel

class DietPlanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDietPlanBinding
    private val viewModel: MoodViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDietPlanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val moodName = intent.getStringExtra("MOOD_TYPE") ?: return
        val moodType = MoodType.valueOf(moodName)
        viewModel.selectMood(moodType)

        setupObservers()

        binding.btnBack.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }

        val slideIn = AnimationUtils.loadAnimation(this, R.anim.slide_in_right)
        binding.scrollView.startAnimation(slideIn)
    }

    private fun setupObservers() {
        viewModel.moodProfile.observe(this) { profile ->
            profile ?: return@observe
            val plan = profile.dietPlan

            binding.tvTitle.text = "${profile.emoji} ${profile.mood.displayName} Diet Plan"

            try {
                val color = Color.parseColor(profile.color)
                binding.headerStrip.setBackgroundColor(color)
            } catch (e: Exception) { }

            binding.tvBreakfast.text = "🌅 Breakfast\n${plan.breakfast}"
            binding.tvMorningSnack.text = "🍎 Morning Snack\n${plan.morningSnack}"
            binding.tvLunch.text = "☀️ Lunch\n${plan.lunch}"
            binding.tvEveningSnack.text = "🌤️ Evening Snack\n${plan.eveningSnack}"
            binding.tvDinner.text = "🌙 Dinner\n${plan.dinner}"
            binding.tvHydration.text = "💧 Hydration\n${plan.hydration}"

            val avoidText = plan.avoid.joinToString("\n") { "  ❌ $it" }
            binding.tvAvoid.text = "Foods to Avoid Today:\n$avoidText"
        }
    }
}
