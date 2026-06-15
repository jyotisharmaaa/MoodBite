package com.moodbite.app.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.moodbite.app.R
import com.moodbite.app.databinding.ActivityMainBinding
import com.moodbite.app.model.MoodType
import com.moodbite.app.ui.adapter.MoodAdapter
import com.moodbite.app.viewmodel.MoodViewModel
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MoodViewModel by viewModels()
    private lateinit var moodAdapter: MoodAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupGreeting()
        setupMoodGrid()
        setupObservers()
    }

    private fun setupGreeting() {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        val greeting = when {
            hour < 12 -> "Good Morning! ☀️"
            hour < 17 -> "Good Afternoon! 🌤️"
            else -> "Good Evening! 🌙"
        }
        binding.tvGreeting.text = greeting
        binding.tvSubtitle.text = "How are you feeling right now?"

        val slideDown = AnimationUtils.loadAnimation(this, R.anim.slide_down)
        binding.tvGreeting.startAnimation(slideDown)
    }

    private fun setupMoodGrid() {
        moodAdapter = MoodAdapter { moodCard ->
            viewModel.selectMood(moodCard.moodType)
            moodAdapter.setSelected(moodCard.moodType)
        }

        binding.rvMoods.apply {
            layoutManager = GridLayoutManager(this@MainActivity, 3)
            adapter = moodAdapter
        }

        moodAdapter.submitList(viewModel.getMoodCards())
    }

    private fun setupObservers() {
        viewModel.isAnalyzing.observe(this) { analyzing ->
            if (analyzing) {
                binding.btnAnalyze.isEnabled = false
                binding.progressBar.visibility = View.VISIBLE
                binding.tvAnalyzingText.visibility = View.VISIBLE
                binding.tvAnalyzingText.text = "🤖 AI is analyzing your mood..."
            } else {
                binding.progressBar.visibility = View.GONE
                binding.tvAnalyzingText.visibility = View.GONE
                binding.btnAnalyze.isEnabled = viewModel.selectedMood.value != null
            }
        }

        viewModel.moodProfile.observe(this) { profile ->
            profile?.let {
                val intent = Intent(this, MoodResultActivity::class.java).apply {
                    putExtra("MOOD_TYPE", it.mood.name)
                }
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
        }

        viewModel.selectedMood.observe(this) { mood ->
            binding.btnAnalyze.isEnabled = mood != null
            if (mood != null) {
                binding.btnAnalyze.text = "✨ Get My Food Plan"
                binding.tvSelectedMood.text = "Selected: ${mood.displayName}"
                binding.tvSelectedMood.visibility = View.VISIBLE
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // Reset selection when coming back
        viewModel.selectedMood.value?.let {
            binding.btnAnalyze.isEnabled = true
        }
    }

    fun onAnalyzeClick(view: View) {
        val mood = viewModel.selectedMood.value ?: return
        viewModel.selectMood(mood)
    }
}
