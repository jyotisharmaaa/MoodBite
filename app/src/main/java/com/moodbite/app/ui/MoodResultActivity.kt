package com.moodbite.app.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.moodbite.app.R
import com.moodbite.app.databinding.ActivityMoodResultBinding
import com.moodbite.app.model.MoodType
import com.moodbite.app.ui.adapter.FoodAdapter
import com.moodbite.app.viewmodel.MoodViewModel

class MoodResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMoodResultBinding
    private val viewModel: MoodViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMoodResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val moodName = intent.getStringExtra("MOOD_TYPE") ?: return
        val moodType = MoodType.valueOf(moodName)

        // Re-select to load profile if coming fresh
        viewModel.selectMood(moodType)

        setupUI(moodType)
        setupObservers()
        setupClickListeners()
    }

    private fun setupUI(moodType: MoodType) {
        val slideIn = AnimationUtils.loadAnimation(this, R.anim.slide_in_right)
        binding.root.startAnimation(slideIn)

        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupObservers() {
        viewModel.moodProfile.observe(this) { profile ->
            profile ?: return@observe

            // Header
            binding.tvMoodEmoji.text = profile.emoji
            binding.tvMoodTitle.text = "You're feeling ${profile.mood.displayName}"
            binding.tvMoodDescription.text = profile.description

            // Color theme
            try {
                val color = Color.parseColor(profile.color)
                binding.headerCard.setCardBackgroundColor(color)
            } catch (e: Exception) { }

            // Neurotransmitter badge
            binding.tvNeuroLabel.text = "🧠 ${profile.neurotransmitter}"

            // Food list
            val foodAdapter = FoodAdapter(profile.foods)
            binding.rvFoods.apply {
                layoutManager = LinearLayoutManager(this@MoodResultActivity)
                adapter = foodAdapter
            }

            // Activities
            val activityText = profile.activities.joinToString("\n") { "  $it" }
            binding.tvActivities.text = activityText

            // Show content
            binding.contentGroup.visibility = View.VISIBLE
        }

        viewModel.aiInsight.observe(this) { insight ->
            binding.tvAiInsight.text = "💡 $insight"
        }

        viewModel.isAnalyzing.observe(this) { analyzing ->
            binding.progressBar.visibility = if (analyzing) View.VISIBLE else View.GONE
            binding.contentGroup.visibility = if (analyzing) View.GONE else View.VISIBLE
        }
    }

    private fun setupClickListeners() {
        binding.btnDietPlan.setOnClickListener {
            val moodName = intent.getStringExtra("MOOD_TYPE") ?: return@setOnClickListener
            startActivity(Intent(this, DietPlanActivity::class.java).apply {
                putExtra("MOOD_TYPE", moodName)
            })
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        binding.btnChangeMood.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }
    }
}
