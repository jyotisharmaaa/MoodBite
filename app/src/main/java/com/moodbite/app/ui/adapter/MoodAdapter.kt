package com.moodbite.app.ui.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.moodbite.app.R
import com.moodbite.app.databinding.ItemMoodCardBinding
import com.moodbite.app.model.MoodCard
import com.moodbite.app.model.MoodType

class MoodAdapter(
    private val onMoodSelected: (MoodCard) -> Unit
) : ListAdapter<MoodCard, MoodAdapter.MoodViewHolder>(MoodDiffCallback()) {

    private var selectedMood: MoodType? = null

    fun setSelected(moodType: MoodType) {
        val old = selectedMood
        selectedMood = moodType
        currentList.forEachIndexed { index, card ->
            if (card.moodType == old || card.moodType == moodType) {
                notifyItemChanged(index)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoodViewHolder {
        val binding = ItemMoodCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MoodViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MoodViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }

    inner class MoodViewHolder(private val binding: ItemMoodCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(card: MoodCard, position: Int) {
            binding.tvEmoji.text = card.emoji
            binding.tvLabel.text = card.label

            val isSelected = card.moodType == selectedMood

            try {
                val color = Color.parseColor(card.bgColor)
                if (isSelected) {
                    binding.cardMood.setCardBackgroundColor(color)
                    binding.tvEmoji.scaleX = 1.2f
                    binding.tvEmoji.scaleY = 1.2f
                } else {
                    binding.cardMood.setCardBackgroundColor(Color.parseColor("#1A${card.bgColor.removePrefix("#")}"))
                    binding.tvEmoji.scaleX = 1.0f
                    binding.tvEmoji.scaleY = 1.0f
                }
            } catch (e: Exception) { }

            binding.ivCheck.visibility = if (isSelected) View.VISIBLE else View.GONE
           // binding.cardMood.strok = if (isSelected) 4 else 0

            // Staggered entry animation
            val bounceIn = AnimationUtils.loadAnimation(binding.root.context, R.anim.bounce_in)
            bounceIn.startOffset = (position * 60).toLong()
            binding.cardMood.startAnimation(bounceIn)

            binding.cardMood.setOnClickListener {
                val pulse = AnimationUtils.loadAnimation(binding.root.context, R.anim.pulse)
                binding.cardMood.startAnimation(pulse)
                onMoodSelected(card)
            }
        }
    }

    class MoodDiffCallback : DiffUtil.ItemCallback<MoodCard>() {
        override fun areItemsTheSame(oldItem: MoodCard, newItem: MoodCard) =
            oldItem.moodType == newItem.moodType
        override fun areContentsTheSame(oldItem: MoodCard, newItem: MoodCard) =
            oldItem == newItem
    }
}
